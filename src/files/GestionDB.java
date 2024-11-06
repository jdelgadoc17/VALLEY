package files;

import model.*;

import java.sql.*;
import java.util.ArrayList;

public class GestionDB {

    private static GestionDB instancia;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/StardamValley";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private ArrayList<Animal> listaAnimales;


    /*
    Singleton de nuestro GestionDB
     */

    private GestionDB() {
        listaAnimales = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión establecida con éxito");

            cargarAnimales();

        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al establecer la conexión:" + e.getMessage());
        }
    }

    public static GestionDB getInstance() {
        if (instancia == null) {
            instancia = new GestionDB();
        }
        return instancia;
    }

    /*
    Opcion de rellenar el comedero
     */

    public void rellenarComedero(Granja granja) {
        String selectAlimentos = "SELECT id, cantidad_disponible, precio FROM Alimentos";
        String updateAlimento = "UPDATE Alimentos SET cantidad_disponible = 25 WHERE id = ?";
        String insertTransaccion = "INSERT INTO Transacciones (tipo_transaccion, tipo_elemento, precio, fecha_transaccion) VALUES ('COMPRA', 'ALIMENTO', ?, NOW())";

        double totalCosto = 0;

        try (PreparedStatement selectStmt = connection.prepareStatement(selectAlimentos);
             ResultSet rs = selectStmt.executeQuery();
             PreparedStatement updateStmt = connection.prepareStatement(updateAlimento);
             PreparedStatement insertStmt = connection.prepareStatement(insertTransaccion)) {

            while (rs.next()) {
                int idAlimento = rs.getInt("id");
                int cantidadDisponible = rs.getInt("cantidad_disponible");
                double precioUnidad = rs.getDouble("precio");

                if (cantidadDisponible >= 25) {
                    System.out.println("El alimento con ID " + idAlimento + " ya está en la capacidad máxima de 25 unidades.");
                }

                int unidadesAComprar = 25 - cantidadDisponible;
                double costoAlimento = unidadesAComprar * precioUnidad;

                if (granja.getPresupuesto() < costoAlimento) {
                    System.out.println("No hay recursos suficientes para comprar el alimento con ID " + idAlimento);
                }

                updateStmt.setInt(1, idAlimento);
                updateStmt.executeUpdate();

                totalCosto += costoAlimento;
            }

            granja.setPresupuesto(granja.getPresupuesto() - totalCosto);
            insertStmt.setDouble(1, totalCosto);
            insertStmt.executeUpdate();

            System.out.println("Comedero rellenado con éxito. Gasto total: " + totalCosto);
            System.out.println("El presupuesto de la granja ahora es: " + granja.getPresupuesto());

        } catch (SQLException e) {
            System.out.println("Error al rellenar el comedero: " + e.getMessage());
        }
    }

    /*
    Mostramos los productos
     */

    public void mostrarProductos() {
        String query = "SELECT id, nombre, precio, cantidad_disponible FROM Productos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Productos en la base de datos:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad_disponible");

                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Precio: " + precio + ", Cantidad Disponible: " + cantidad);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar productos: " + e.getMessage());
        }
    }

    /*
    Mostramos los alimentos
     */

    public void mostrarAlimentos() {
        String query = "SELECT id, nombre, precio, cantidad_disponible FROM Alimentos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Alimentos en la base de datos:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad_disponible");

                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Precio: " + precio + ", Cantidad Disponible: " + cantidad);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar alimentos: " + e.getMessage());
        }
    }

    /*
    Vendemos los productos existentes
     */



    public void venderProductos(Granja granja) {
        String queryProductos = "SELECT id, cantidad_disponible, precio FROM Productos";
        String updateProductos = "UPDATE Productos SET cantidad_disponible = 0 WHERE id = ?";
        String insertTransaccion = "INSERT INTO Transacciones (tipo_transaccion, tipo_elemento, precio, fecha_transaccion) VALUES ('VENTA', 'PRODUCTO', ?, NOW())";


        double totalVenta = 0;
        try (PreparedStatement selectStmt = connection.prepareStatement(queryProductos);
             ResultSet rs = selectStmt.executeQuery();
             PreparedStatement updateStmt = connection.prepareStatement(updateProductos);
             PreparedStatement insertStmt = connection.prepareStatement(insertTransaccion)) {

            while (rs.next()) {
                int idProducto = rs.getInt("id");
                int cantidadDisponible = rs.getInt("cantidad_disponible");
                double precio = rs.getDouble("precio");

                double subtotal = cantidadDisponible * precio;
                totalVenta += subtotal;

                updateStmt.setInt(1, idProducto);
                updateStmt.executeUpdate();
            }

            insertStmt.setDouble(1, totalVenta);
            insertStmt.executeUpdate();

            granja.setPresupuesto(granja.getPresupuesto() + totalVenta);

            System.out.println("Se han vendido todos los productos por un total de: " + totalVenta);
            System.out.println("El presupuesto de la granja ahora es: " + granja.getPresupuesto());

        } catch (SQLException e) {
            System.out.println("Error al vender productos: " + e.getMessage());
        }
    }


    /*
    Cargamos los animales de la BBDD
     */

    private void cargarAnimales() {
        String query = """
                SELECT 
                    Animales.id, Animales.nombre, Animales.tipo, Animales.dia_insercion, Animales.peso,
                    Alimentos.id AS idAlimento, Alimentos.nombre AS nombreAlimento, Alimentos.precio AS precioAlimento, Alimentos.cantidad_disponible AS cantidadAlimento,
                    Productos.id AS idProducto, Productos.nombre AS nombreProducto, Productos.precio AS precioProducto, Productos.cantidad_disponible AS cantidadProducto
                FROM 
                    Animales
                INNER JOIN 
                    Alimentos ON Animales.id_alimento = Alimentos.id
                INNER JOIN 
                    Productos ON Animales.id_producto = Productos.id
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String tipoString = rs.getString("tipo");
                int diaInsercion = rs.getInt("dia_insercion");

                Alimento alimento = new Alimento(rs.getInt("idAlimento"), rs.getString("nombreAlimento"),
                        rs.getDouble("precioAlimento"), rs.getInt("cantidadAlimento"));
                Producto producto = new Producto(rs.getInt("idProducto"), rs.getString("nombreProducto"),
                        rs.getDouble("precioProducto"), rs.getInt("cantidadProducto"));

                Animal animal = null;
                try {
                    TipoAnimal tipo = TipoAnimal.valueOf(tipoString.trim().toUpperCase());
                    switch (tipo) {
                        case GALLINA:
                            animal = new Gallina(id, nombre, tipo, diaInsercion, alimento, producto);
                            break;
                        case OVEJA:
                            animal = new Oveja(id, nombre, tipo, diaInsercion, alimento, producto);
                            break;
                        case VACA:
                            double peso = rs.getDouble("peso");
                            animal = new Vaca(id, nombre, tipo, diaInsercion, alimento, producto, peso);
                            break;
                        case CERDO:
                            animal = new Cerdo(id, nombre, tipo, diaInsercion, alimento, producto);
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Tipo de animal no válido en la base de datos: " + tipoString);
                }
                if (animal != null) {
                    listaAnimales.add(animal);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar los animales: " + e.getMessage());
        }
    }


    public int getCantidadDisponibleAlimento(int idAlimento) {
        String query = "SELECT cantidad_disponible FROM Alimentos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idAlimento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidad_disponible");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cantidad disponible de alimento: " + e.getMessage());
        }
        return 0;
    }


    public void registrarConsumo(Animal animal, int cantidadConsumida) {
        String query = "INSERT INTO HistorialConsumo (id_animal, cantidad_consumida, fecha_consumo) VALUES (?, ?, NOW())";
       //OTRO METODO String query2 = "UPDATE Productos SET cantidad_disponible = cantidad_disponible - ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, animal.getId());
            stmt.setInt(2, cantidadConsumida);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al registrar el consumo: " + e.getMessage());
        }
    }

    /*
    Restauramos a los valores iniciales
     */

    public void restaurarValores() {

        String queryAlimentos = "UPDATE Alimentos SET cantidad_disponible = 5";
        String queryProductos = "UPDATE Productos SET cantidad_disponible = 0";
        String queryHistorialProduccion = "TRUNCATE TABLE HistorialProduccion";
        String queryHistorialConsumo = "TRUNCATE TABLE HistorialConsumo";
        String queryTransacciones = "TRUNCATE TABLE transacciones";

        try (
                PreparedStatement stmtAlimentos = connection.prepareStatement(queryAlimentos);
                PreparedStatement stmtProductos = connection.prepareStatement(queryProductos);
                Statement stmtHistorialProduccion = connection.prepareStatement(queryHistorialProduccion);
                Statement stmtHistorialConsumo = connection.prepareStatement(queryHistorialConsumo);
                Statement stmtTransacciones = connection.prepareStatement(queryTransacciones);

        ) {
            stmtAlimentos.executeUpdate();
            stmtProductos.executeUpdate();
            stmtHistorialProduccion.executeUpdate(queryHistorialProduccion);
            stmtHistorialConsumo.executeUpdate(queryHistorialConsumo);
            stmtHistorialConsumo.executeUpdate(queryTransacciones);


            System.out.println("Valores restaurados en Alimentos y Productos  y tablas de historial vaciadas.");
        } catch (SQLException e) {
            System.out.println("Error al restaurar valores en la base de datos: " + e.getMessage());
        }
    }



    public void actualizarCantidad(int idProducto, int cantidadCambio) {
        String selectCantidadQuery = "SELECT cantidad_disponible FROM Productos WHERE id = ?";
        String updateCantidadQuery = "UPDATE Productos SET cantidad_disponible = ? WHERE id = ?";

        try (
                PreparedStatement selectStmt = connection.prepareStatement(selectCantidadQuery);
                PreparedStatement updateStmt = connection.prepareStatement(updateCantidadQuery)
        ) {
            selectStmt.setInt(1, idProducto);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int cantidadActual = rs.getInt("cantidad_disponible");
                int nuevaCantidad = cantidadActual + cantidadCambio;

                if (nuevaCantidad < 0) {
                    nuevaCantidad = 0;
                }

                updateStmt.setInt(1, nuevaCantidad);
                updateStmt.setInt(2, idProducto);
                updateStmt.executeUpdate();

            } else {
                System.out.println("Producto no encontrado con el ID especificado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar la cantidad de producto: " + e.getMessage());
        }
    }


    /*
    Metodos para registrar los historiales de prroduccion
     */


    public void registrarProduccion(Animal animal, int cantidadProducida, int diaActual) {
        String query = "INSERT INTO HistorialProduccion (id_animal, cantidad, fecha_produccion) VALUES (?, ?, NOW())";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, animal.getId());
            stmt.setInt(2, cantidadProducida);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Registro de producción agregado al HistorialProduccion para " + animal.getNombre());
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar la producción: " + e.getMessage());
        }
    }

    public ArrayList<Animal> getListaAnimales() {
        return new ArrayList<>(listaAnimales);
    }


}









