import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Granja implements Serializable  {
    private static final int TAMANNO_CELDA = Integer.BYTES + 1 + Integer.BYTES;

    // Atributos de la clase
    private int diaActual;
    private TipoEstacion tipoEstacion;
    private double presupuesto;
    private Tienda tienda;
    private Almacen almacen;
    private Properties propiedades;  // Cargar las propiedades una sola vez

    // Constructor
    public Granja(int diaActual, TipoEstacion tipoEstacion, double presupuesto, Tienda tienda, Almacen almacen) {
        this.diaActual = diaActual;
        this.tipoEstacion = tipoEstacion;
        this.presupuesto = presupuesto;
        this.tienda = tienda;
        this.almacen = almacen;
        this.propiedades = FileWork.getInstancia().cargarProperties();  // Cargar las propiedades una vez
    }

    // Mostrar la información de la granja
    public void mostrarInfo() {
        System.out.println("INFORMACION DE LA GRANJA");
        System.out.println("- Días de juego: " + diaActual);
        System.out.println("- Dinero disponible: " + presupuesto);
        System.out.println("- Estación: " + tipoEstacion);
        System.out.println("- Semillas en venta: " + tienda.getSemillasSegunEstacion()); //TODAS O SOLO TRES
        System.out.println("- Frutos en almacen: " + almacen.getTotalFruto());
        // Falta mostrar el estado del huerto
    }

    // Crear el huerto si no existe el archivo
    public void crearHuerto(Path path) throws IOException {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(path.toFile(), "rw")) {
            if (archivoHuerto.length() == 0) {  // Inicializar si está vacío
                archivoHuerto.setLength(0);

                for (int i = 0; i < filas * columnas; i++) {
                    archivoHuerto.writeInt(-1);
                    archivoHuerto.writeBoolean(false);
                    archivoHuerto.writeInt(-1);
                }

                System.out.println("Huerto inicializado con " + filas + " filas y " + columnas + " columnas.");
            }
        }
    }

    public void plantarEnColumna(int columnaSeleccionada) {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        // Verificar si la columna seleccionada es válida
        if (columnaSeleccionada < 0 || columnaSeleccionada >= columnas) {
            System.out.println("Columna no válida.");
            return;
        }

        try (RandomAccessFile archivoHuerto = new RandomAccessFile("VALLEY/Resources/archivoHuerto.dat", "rw")) {
            boolean columnaVacia = true;

            // Verificar si toda la columna está vacía
            for (int fila = 0; fila < filas; fila++) {
                long posicion = (fila * columnas + columnaSeleccionada) * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                if (idSemilla != -1) {  // Si hay una semilla plantada en alguna fila de la columna
                    columnaVacia = false;
                    break;
                }
            }

            if (!columnaVacia) {
                System.out.println("No se puede plantar en esta columna, ya tiene cultivos.");
                return;
            }

            // Mostrar semillas disponibles
            ArrayList<Semilla> semillasDisponibles = tienda.seleccionarTresSemillasAleatorias();
            if (semillasDisponibles.isEmpty()) {
                System.out.println("No hay semillas disponibles para plantar.");
                return;
            }

            // Permitir al jugador seleccionar una semilla
            System.out.println("Selecciona una semilla para plantar en la columna " + columnaSeleccionada + ":");
            for (int i = 0; i < semillasDisponibles.size(); i++) {
                Semilla semilla = semillasDisponibles.get(i);
                System.out.println((i + 1) + ". " + semilla.getNombre() + " - Precio: " + semilla.getPrecioCompraSemilla());
            }

            Scanner sc = new Scanner(System.in);
            int seleccion = sc.nextInt() - 1;
            if (seleccion < 0 || seleccion >= semillasDisponibles.size()) {
                System.out.println("Selección no válida.");
                return;
            }

            Semilla semillaSeleccionada = semillasDisponibles.get(seleccion);

            // Obtener el id de la semilla seleccionada desde el mapa de la tienda
            Integer idSemillaSeleccionada = null;
            for (Map.Entry<Integer, Semilla> entry : tienda.getMapa_total_semillas().entrySet()) {
                if (entry.getValue().equals(semillaSeleccionada)) {
                    idSemillaSeleccionada = entry.getKey();
                }
            }

            if (idSemillaSeleccionada == null) {
                System.out.println("Error: No se pudo encontrar el ID de la semilla seleccionada.");
                return;
            }

            // Comprobar si hay suficiente presupuesto
            if (presupuesto < semillaSeleccionada.getPrecioCompraSemilla() * filas) {
                System.out.println("No tienes suficiente dinero para plantar toda la columna con " + semillaSeleccionada.getNombre());
                return;
            }

            // Plantar las semillas en la columna seleccionada
            for (int fila = 0; fila < filas; fila++) {
                long posicion = (fila * columnas + columnaSeleccionada) * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                archivoHuerto.writeInt(idSemillaSeleccionada);  // Usar el id de la semilla desde el mapa
                archivoHuerto.writeBoolean(false);  // Marcar como no regada
                archivoHuerto.writeInt(0);  // Días plantados en 0
            }

            // Restar el dinero por la compra de semillas
            presupuesto -= semillaSeleccionada.getPrecioCompraSemilla() * filas;

            System.out.println("Has plantado " + semillaSeleccionada.getNombre() + " en la columna " + columnaSeleccionada + ".");
        } catch (IOException e) {
            System.out.println("Error al plantar en la columna: " + e.getMessage());
        }
    }





    // Actualizar los cultivos en el huerto
    public void actualizarCultivos() {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));


        try (RandomAccessFile archivoHuerto = new RandomAccessFile(Paths.get("VALLEY/Resources/archivoHuerto.dat").toFile(), "rw")) {
            archivoHuerto.seek(0);



            for (int i = 0; i < filas * columnas; i++) {
                long posicion = i * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                boolean regado = archivoHuerto.readBoolean();
                int diasPlantado = archivoHuerto.readInt();

                if (idSemilla != -1 && regado) {
                    diasPlantado++;
                    archivoHuerto.seek(posicion + 5);
                    archivoHuerto.writeInt(diasPlantado);
                    archivoHuerto.seek(posicion + 4);
                    archivoHuerto.writeBoolean(false);
                }
            }
            System.out.println("CULTIVOS ACTUALIZADOS");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Semilla obtenerSemilla(int idSemilla) {
        return tienda.getMapa_total_semillas().get(idSemilla);  // Buscar la semilla por su id en el TreeMap
    }

    public static void guardarPartida(Granja granja){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("VALLEY/Resources/partida.bin"))) {
            oos.writeObject(granja);
            System.out.println("Partida guardada correctamente.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    //Método para atender los cultivos
    public void atenderCultivos(Path path) {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(Paths.get("VALLEY/Resources/archivoHuerto.dat").toFile(), "rw")) {
            archivoHuerto.seek(0);

            for (int i = 0; i < filas * columnas; i++) {
                long posicion = i * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                boolean regado = archivoHuerto.readBoolean();
                int diasPlantado = archivoHuerto.readInt();

                if (idSemilla != -1) {  // Si hay una semilla plantada
                    if (!regado) {
                        archivoHuerto.seek(posicion + 4);
                        archivoHuerto.writeBoolean(true);  // Marcar como regado
                    }

                    Semilla semilla = obtenerSemilla(idSemilla);
                    if (semilla != null && diasPlantado >= semilla.getDiasCrecimiento()) {
                        int frutosRecolectados = (int) (Math.random() * semilla.getMaxFrutos()) + 1;
                        almacen.annadirCosecha(semilla, frutosRecolectados);

                        archivoHuerto.seek(posicion);
                        archivoHuerto.writeInt(-1);  // Vaciar la celda
                        archivoHuerto.writeBoolean(false);
                        archivoHuerto.writeInt(-1);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para restablecer los cultivos
    public void reestablecerCultivos() {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(Paths.get("VALLEY/Resources/archivoHuerto.dat").toFile(), "rw")) {
            archivoHuerto.seek(0);

            for (int i = 0; i < filas * columnas; i++) {
                long posicion = i * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);
                archivoHuerto.writeInt(-1);
                archivoHuerto.writeBoolean(false);
                archivoHuerto.writeInt(-1);
            }
            System.out.println("CULTIVOS REESTABLECIDOS");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para vender la fruta
    public void venderFruta() {
        almacen.venderFrutos();  // Llamar al método de Almacen
    }

    // Método para cambiar de estación
    public void cambiarEstacion() {
        switch (tipoEstacion) {
            case PRIMAVERA:
                tipoEstacion = TipoEstacion.VERANO;
                break;
            case VERANO:
                tipoEstacion = TipoEstacion.OTOÑO;
                break;
            case OTOÑO:
                tipoEstacion = TipoEstacion.INVIERNO;
                break;
            case INVIERNO:
                tipoEstacion = TipoEstacion.PRIMAVERA;
                break;
        }

        reestablecerCultivos();
    }

    // Método para iniciar un nuevo día
    public void iniciarNuevoDia() {
        diaActual++;

        int diasDuracionEstacion = Integer.parseInt(propiedades.getProperty("diasDuracionEstacion"));
        if (diaActual > diasDuracionEstacion) {
            diaActual = 1;
            cambiarEstacion();
            reestablecerCultivos();
        } else {
            actualizarCultivos();
        }

        tienda.generarSemillasDisponibles(tipoEstacion, presupuesto);  // Generar semillas según la estación actual
    }

    // Getters y Setters
    public int getDiaActual() {
        return diaActual;
    }

    public void setDiaActual(int diaActual) {
        this.diaActual = diaActual;
    }

    public TipoEstacion getTipoEstacion() {
        return tipoEstacion;
    }

    public void setTipoEstacion(TipoEstacion tipoEstacion) {
        this.tipoEstacion = tipoEstacion;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    @Override
    public String toString() {
        return "Granja{" +
                "diaActual=" + diaActual +
                ", tipoEstacion=" + tipoEstacion +
                ", presupuesto=" + presupuesto +
                ", tienda=" + tienda +
                ", almacen=" + almacen +
                '}';
    }
}
