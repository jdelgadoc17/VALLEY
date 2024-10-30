import java.sql.*;
import java.util.ArrayList;

public class GestionDB {

    private static GestionDB instancia;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/StardamValley";
    private static final String USER = "root";
    private static final String PASSWORD = "tu_contraseña";

    private ArrayList<Animal> listaAnimales;

    private GestionDB() {
        listaAnimales = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión establecida con éxito");

            cargarAnimales();  //CARGAMOS LOS ANIMALES DE LA TABLA AL ARRAY LIST

        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al establecer la conexión: " + e.getMessage());
        }
    }

    public static GestionDB getInstance() {
        if (instancia == null) {
            instancia = new GestionDB();
        }
        return instancia;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    // Metodo para cargar los animales en memoria
    private void cargarAnimales() {
        String query = "SELECT * FROM ANIMALES";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String tipoString = rs.getString("tipo");
                int diaInsercion = rs.getInt("diaInsercion");

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
            System.out.println("Error al cargar animales: " + e.getMessage());
        }
    }

    // Obtener la lista de animales
    public ArrayList<Animal> getListaAnimales() {
        return listaAnimales;
    }
}