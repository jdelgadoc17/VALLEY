import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Granja implements Serializable  {
    private static final int TAMANNO_CELDA = Integer.BYTES + 1 + Integer.BYTES;

    private int diaActual;
    private TipoEstacion tipoEstacion;
    private double presupuesto;
    private Tienda tienda;
    private Almacen almacen;
    private Properties propiedades;  // Cargar las propiedades una sola vez

    public Granja(int diaActual, TipoEstacion tipoEstacion, double presupuesto, Tienda tienda, Almacen almacen) {
        this.diaActual = diaActual;
        this.tipoEstacion = tipoEstacion;
        this.presupuesto = presupuesto;
        this.tienda = tienda;
        this.almacen = almacen;
        //this.propiedades = FileWork.getInstancia().cargarProperties();  // Cargar las propiedades una vez de ELEGIR AQUI?
    }

    public void mostrarInfo() {
        System.out.println("INFORMACION DE LA GRANJA");
        System.out.println("- Días de juego: " + diaActual);
        System.out.println("- Dinero disponible: " + presupuesto);
        System.out.println("- Estación: " + tipoEstacion);
        System.out.println("- Semillas en venta: " + tienda.getSemillasSegunEstacion()); //TODAS O SOLO TRES
        System.out.println("- Frutos en almacen: " + almacen.getTotalFruto());
    }


    /*
     * Metodo para crear un huerto con la cantidad de filas y columnas especificadas en las propiedades.
     */
    public void crearHuerto(Path path) throws IOException {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile("Resources/archivoHuerto.dat", "rw")) {
            for (int i = 0; i < filas * columnas; i++) {
                archivoHuerto.writeInt(-1);
                archivoHuerto.writeBoolean(false);
                archivoHuerto.writeInt(-1);
            }

            System.out.println("Huerto inicializado con " + filas + " filas y " + columnas + " columnas.");

        }
    }

    /*
     * Plantar una semilla en una columna concreta
     */
    public void plantarEnColumna(int columnaSeleccionada) {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        if (columnaSeleccionada < 0 || columnaSeleccionada >= columnas) {
            System.out.println("Columna no válida.");
            return;
        }

        try (RandomAccessFile archivoHuerto = new RandomAccessFile("Resources/archivoHuerto.dat", "rw")) {
            boolean columnaVacia = true;

            for (int fila = 0; fila < filas; fila++) {
                long posicion = (fila * columnas + columnaSeleccionada) * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                if (idSemilla != -1) {  // Si hay una semilla plantada en alguna fila de la columna
                    columnaVacia = false;
                    return;
                }
            }

            if (!columnaVacia) {
                System.out.println("No se puede plantar en esta columna, ya tiene cultivos.");
                return;
            }

            ArrayList<Semilla> semillasDisponibles = tienda.seleccionarTresSemillasAleatorias();
            if (semillasDisponibles.isEmpty()) {
                System.out.println("No hay semillas disponibles para plantar.");
                return;
            }

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

            if (presupuesto < semillaSeleccionada.getPrecioCompraSemilla() * filas) {
                System.out.println("No tienes suficiente dinero para plantar toda la columna con " + semillaSeleccionada.getNombre());
                return;
            }

            for (int fila = 0; fila < filas; fila++) {
                long posicion = (fila * columnas + columnaSeleccionada) * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                archivoHuerto.writeInt(idSemillaSeleccionada);
                archivoHuerto.writeBoolean(false);
                archivoHuerto.writeInt(0);
            }

            presupuesto -= semillaSeleccionada.getPrecioCompraSemilla() * filas;

            System.out.println("Has plantado "+ semillaSeleccionada.getNombre() +" en la columna " + columnaSeleccionada + ".");
        } catch (IOException e) {
            System.out.println("Error al plantar en la columna: " + e.getMessage());
        }
    }

    /*
     * Mostrar el estado del huerto
     */
    public void mostrarHuerto() {
        Path path = Paths.get("Resources/archivoHuerto.dat");
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        try {
            RandomAccessFile archivoHuerto = new RandomAccessFile(path.toFile(), "rw");

            for (int i = 0; i < filas * columnas; i++) {
                long posicion = i * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                boolean regado = archivoHuerto.readBoolean();
                int diasPlantado = archivoHuerto.readInt();

                // Mostrar el estado del huerto
                if (idSemilla == -1) {
                    System.out.print("[SS]");  // Celda vacía
                } else {
                    System.out.print("[ " + idSemilla + " | " + regado + " | " + diasPlantado + " ]");
                }

                if ((i + 1) % columnas == 0) {
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /*
     * Actualizar el estado de los cultivos
     */
    public void actualizarCultivos() {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(Paths.get("Resources/archivoHuerto.dat").toFile(), "rw")) {
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


    /*
     * Obtener una semilla segun el id
     */
    public Semilla obtenerSemilla(int idSemilla) {
        return tienda.getMapa_total_semillas().get(idSemilla);  // Buscar la semilla por su id en el TreeMap
    }

    /*
     * Guardar la partida
     */

    public static void guardarPartida(Granja granja){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Resources/partida.bin"))) {
            oos.writeObject(granja);
            System.out.println("Partida guardada correctamente.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    /*
     * Atender cultivos y recolectar frutos
     */
    public void atenderCultivos(Path path) {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(Paths.get("Resources/archivoHuerto.dat").toFile(), "rw")) {
            archivoHuerto.seek(0);

            for (int i = 0; i < filas * columnas; i++) {
                long posicion = i * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                boolean regado = archivoHuerto.readBoolean();
                int diasPlantado = archivoHuerto.readInt();

                if (idSemilla != -1) {
                    if (!regado) {
                        archivoHuerto.seek(posicion + 4);
                        archivoHuerto.writeBoolean(true);  // Marcar como regado
                    }

                    Semilla semilla = obtenerSemilla(idSemilla);
                    if (semilla != null && diasPlantado >= semilla.getDiasCrecimiento()) {
                        int frutosRecolectados = (int) (Math.random() * semilla.getMaxFrutos()) + 1;
                        almacen.annadirCosecha(semilla, frutosRecolectados);
                        System.out.println("Se han añadido almacen " +frutosRecolectados+ " frutos de " +semilla.getNombre());

                        archivoHuerto.seek(posicion);
                        archivoHuerto.writeInt(-1);  //PASAR A STATIC
                        archivoHuerto.writeBoolean(false);
                        archivoHuerto.writeInt(-1);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Reestablecer el estado de los cultivos
     */
    public void reestablecerCultivos() {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(Paths.get("Resources/archivoHuerto.dat").toFile(), "rw")) {
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

    /*
     * Vender frutos
     */
    public void venderFruta() {
        almacen.venderFrutos();  // Llamar al método de Almacen
    }


    /*
     * Cambiar la estación
     */
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


    /*
     * Actualizar los cultivos según el día actual
     */
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
