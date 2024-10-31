package Model;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;


public class Granja implements Serializable {

    /*
     estáticas para el tamaño de celda, claves de propiedades, rutas de archivos, y valores por defecto
     */
    private static final int TAMANNO_CELDA = Integer.BYTES + 1 + Integer.BYTES;
    private static final String FILAS_KEY = "numFilas";
    private static final String COLUMNAS_KEY = "numColumnas";
    private static final String ESTACION_KEY = "estacionInicial";
    private static final String PRESUPUESTO_KEY = "presupuestoInicial";
    private static final String DIAS_DURACION_ESTACION_KEY = "diasDuracionEstacion";
    private static final String HUERTO_FILE_PATH = "Resources/archivoHuerto.dat";
    private static final String PARTIDA_FILE_PATH = "Resources/partida.bin";
    private static final int ID_SEMILLA_VACIA = -1;
    private static final boolean REGADO_DEFAULT = false;
    private static final int DIAS_PLANTADO_DEFAULT = 0;

    private int diaActual;
    private TipoEstacion tipoEstacion;
    private double presupuesto;
    private Tienda tienda;
    private Almacen almacen;
    private Properties propiedades;
    private Establo establo;

    public Granja(int diaActual, TipoEstacion tipoEstacion, double presupuesto, Tienda tienda, Almacen almacen, String tipoConfig) {
        this.diaActual = diaActual;
        this.tipoEstacion = tipoEstacion;
        this.presupuesto = presupuesto;
        this.tienda = tienda;
        this.almacen = almacen;
        this.establo = new Establo();

        this.propiedades = FileWork.getInstancia().cargarProperties(tipoConfig);

        if (this.propiedades == null) {
            System.out.println("Error: No se pudo cargar el archivo de configuración. Usando valores por defecto.");
            this.propiedades = FileWork.getInstancia().cargarProperties("default");
        }
    }

    public void mostrarInfo() {
        System.out.println("INFORMACION DE LA GRANJA");
        System.out.println("- Días de juego: " + diaActual);
        System.out.println("- Dinero disponible: " + presupuesto);
        System.out.println("- Estación: " + tipoEstacion);
        System.out.println("- Semillas en venta: " + tienda.getSemillasSegunEstacion());
        System.out.println("- Frutos en almacen: " + almacen.getTotalFruto());
    }

    /*
    Crera huerto
     */
    public void crearHuerto(Path path) throws IOException {
        int filas = Integer.parseInt(propiedades.getProperty("numFilas"));
        int columnas = Integer.parseInt(propiedades.getProperty("numColumnas"));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(HUERTO_FILE_PATH, "rw")) {
            for (int i = 0; i < filas * columnas; i++) {
                archivoHuerto.writeInt(ID_SEMILLA_VACIA);
                archivoHuerto.writeBoolean(REGADO_DEFAULT);
                archivoHuerto.writeInt(DIAS_PLANTADO_DEFAULT);
            }

            System.out.println("Huerto inicializado con " + filas + " filas y " + columnas + " columnas.");

        }
    }

    /*
    Plantar en la columna dada
     */

    public void plantarEnColumna(int columnaSeleccionada) {
        int filas = Integer.parseInt(propiedades.getProperty(FILAS_KEY));
        int columnas = Integer.parseInt(propiedades.getProperty(COLUMNAS_KEY));

        if (columnaSeleccionada < 0 || columnaSeleccionada >= columnas) {
            System.out.println("Columna no válida.");
            return;
        }

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(HUERTO_FILE_PATH, "rw")) {
            boolean columnaVacia = true;

            for (int fila = 0; fila < filas; fila++) {
                long posicion = (fila * columnas + columnaSeleccionada) * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);
                int idSemilla = archivoHuerto.readInt();
                if (idSemilla != ID_SEMILLA_VACIA) {
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
                archivoHuerto.writeBoolean(REGADO_DEFAULT);
                archivoHuerto.writeInt(0);
            }

            presupuesto -= semillaSeleccionada.getPrecioCompraSemilla() * filas;
            System.out.println("Has plantado " + semillaSeleccionada.getNombre() + " en la columna " + columnaSeleccionada + ".");
        } catch (IOException e) {
            System.out.println("Error al plantar en la columna: " + e.getMessage());
        }
    }

    /*
    Mostramos el huerto
     */

    public void mostrarHuerto() {
        Path path = Paths.get(HUERTO_FILE_PATH);
        int filas = Integer.parseInt(propiedades.getProperty(FILAS_KEY));
        int columnas = Integer.parseInt(propiedades.getProperty(COLUMNAS_KEY));

        try {
            RandomAccessFile archivoHuerto = new RandomAccessFile(path.toFile(), "rw");

            for (int i = 0; i < filas * columnas; i++) {
                long posicion = i * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                boolean regado = archivoHuerto.readBoolean();
                int diasPlantado = archivoHuerto.readInt();

                if (idSemilla == ID_SEMILLA_VACIA) {
                    System.out.print("[SS]");
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
    Se actualizan los cultivos
     */

    public void actualizarCultivos() {
        int filas = Integer.parseInt(propiedades.getProperty(FILAS_KEY));
        int columnas = Integer.parseInt(propiedades.getProperty(COLUMNAS_KEY));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(Paths.get(HUERTO_FILE_PATH).toFile(), "rw")) {
            archivoHuerto.seek(0);

            for (int i = 0; i < filas * columnas; i++) {
                long posicion = i * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                boolean regado = archivoHuerto.readBoolean();
                int diasPlantado = archivoHuerto.readInt();

                if (idSemilla != ID_SEMILLA_VACIA && regado) {
                    diasPlantado++;
                    archivoHuerto.seek(posicion + 5);
                    archivoHuerto.writeInt(diasPlantado);
                    archivoHuerto.seek(posicion + 4);
                    archivoHuerto.writeBoolean(REGADO_DEFAULT);
                }
            }
            System.out.println("CULTIVOS ACTUALIZADOS");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Semilla obtenerSemilla(int idSemilla) {
        return tienda.getMapa_total_semillas().get(idSemilla);
    }

    public static void guardarPartida(Granja granja) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PARTIDA_FILE_PATH))) {
            oos.writeObject(granja);
            System.out.println("Partida guardada correctamente.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Se etienden los cultivos con posible cosecha
     */

    public void atenderCultivos(Path path) {
        int filas = Integer.parseInt(propiedades.getProperty(FILAS_KEY));
        int columnas = Integer.parseInt(propiedades.getProperty(COLUMNAS_KEY));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(path.toFile(), "rw")) {
            archivoHuerto.seek(0);

            for (int i = 0; i < filas * columnas; i++) {
                long posicion = i * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                boolean regado = archivoHuerto.readBoolean();
                int diasPlantado = archivoHuerto.readInt();

                if (idSemilla != ID_SEMILLA_VACIA) {
                    if (!regado) {
                        archivoHuerto.seek(posicion + 4);
                        archivoHuerto.writeBoolean(true);
                    }

                    Semilla semilla = obtenerSemilla(idSemilla);
                    if (semilla != null && diasPlantado >= semilla.getDiasCrecimiento()) {
                        int frutosRecolectados = (int) (Math.random() * semilla.getMaxFrutos()) + 1;
                        almacen.annadirCosecha(semilla, frutosRecolectados);
                        System.out.println("Se han añadido " + frutosRecolectados + " frutos de " + semilla.getNombre());

                        archivoHuerto.seek(posicion);
                        archivoHuerto.writeInt(ID_SEMILLA_VACIA);
                        archivoHuerto.writeBoolean(REGADO_DEFAULT);
                        archivoHuerto.writeInt(DIAS_PLANTADO_DEFAULT);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Reestablecer después de la cosecha
     */

    public void reestablecerCultivos() {
        int filas = Integer.parseInt(propiedades.getProperty(FILAS_KEY));
        int columnas = Integer.parseInt(propiedades.getProperty(COLUMNAS_KEY));

        try (RandomAccessFile archivoHuerto = new RandomAccessFile(Paths.get(HUERTO_FILE_PATH).toFile(), "rw")) {
            archivoHuerto.seek(0);

            for (int i = 0; i < filas * columnas; i++) {
                long posicion = i * TAMANNO_CELDA;
                archivoHuerto.seek(posicion);
                archivoHuerto.writeInt(ID_SEMILLA_VACIA);
                archivoHuerto.writeBoolean(false);
                archivoHuerto.writeInt(DIAS_PLANTADO_DEFAULT);
            }
            System.out.println("CULTIVOS REESTABLECIDOS");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void venderFruta() {
        almacen.venderFrutos();
    }

    public void cambiarEstacion() {
        switch (tipoEstacion) {
            case TipoEstacion.PRIMAVERA:
                tipoEstacion = TipoEstacion.VERANO;
                break;
            case TipoEstacion.VERANO:
                tipoEstacion = TipoEstacion.OTOÑO;
                break;
            case TipoEstacion.OTOÑO:
                tipoEstacion = TipoEstacion.INVIERNO;
                break;
            case TipoEstacion.INVIERNO:
                tipoEstacion = TipoEstacion.PRIMAVERA;
                break;
        }

        reestablecerCultivos();
    }

    /*
    Flow general de un nuevo dia
     */

    public void iniciarNuevoDia() {
        diaActual++;

        int diasDuracionEstacion = Integer.parseInt(propiedades.getProperty(DIAS_DURACION_ESTACION_KEY));
        if (diaActual > diasDuracionEstacion) {
            diaActual = 1;
            cambiarEstacion();
            reestablecerCultivos();
        } else {
            actualizarCultivos();
        }

        tienda.generarSemillasDisponibles(tipoEstacion, presupuesto);
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


    public void alimentar() {
        for (Animal animal : establo.getAnimales()) {
            animal.setAlimentado(true);
            System.out.println(animal.getNombre() + " ha sido alimentado.");
        }
    }



    private void mostrarResumenProduccion(int totalHuevos, int totalLana, int totalLeche, int totalTrufas) {
        System.out.println("Resumen de Producción del Día:");
        if (totalHuevos > 0) {
            System.out.println("Se han producido " + totalHuevos + " unidades de huevos.");
        }
        if (totalLana > 0) {
            System.out.println("Se han producido " + totalLana + " unidades de lana.");
        }
        if (totalLeche > 0) {
            System.out.println("Se han producido " + totalLeche + " unidades de leche.");
        }
        if (totalTrufas > 0) {
            System.out.println("Se han producido " + totalTrufas + " unidades de trufas.");
        }
    }

    public void producir(int diaActual, TipoEstacion tipoEstacion) {
        GestionDB gestionDB = GestionDB.getInstance();

        int totalHuevos = 0;
        int totalLana = 0;
        int totalLeche = 0;
        int totalTrufas = 0;

        for (Animal animal : establo.getAnimales()) {
            if (animal.isAlimentado()) {
                int cantidadProducida = 0;

                if (animal instanceof Gallina) {
                    cantidadProducida = ((Gallina) animal).producir(diaActual);
                    totalHuevos += cantidadProducida;
                } else if (animal instanceof Oveja) {
                    cantidadProducida = ((Oveja) animal).producir();
                    totalLana += cantidadProducida;
                } else if (animal instanceof Vaca) {
                    cantidadProducida = ((Vaca) animal).producir();
                    totalLeche += cantidadProducida;
                } else if (animal instanceof Cerdo) {
                    cantidadProducida = ((Cerdo) animal).producir(tipoEstacion);
                    totalTrufas += cantidadProducida;
                }

                if (cantidadProducida > 0) {
                    gestionDB.actualizarCantidad(animal.getProducto().getIdProducto(), cantidadProducida);
                    gestionDB.registrarProduccion(animal, cantidadProducida, diaActual);
                    animal.setAlimentado(false);
                } else {
                    System.out.println(animal.getNombre() + " no ha producido nada hoy.");
                }
            } else {
                System.out.println(animal.getNombre() + " no ha sido alimentado y no puede producir.");
            }
        }

        mostrarResumenProduccion(totalHuevos, totalLana, totalLeche, totalTrufas);
    }


    public Establo getEstablo() {
        return establo;
    }













    @Override
    public String toString() {
        return "Model.Granja{" +
                "diaActual=" + diaActual +
                ", tipoEstacion=" + tipoEstacion +
                ", presupuesto=" + presupuesto +
                ", tienda=" + tienda +
                ", almacen=" + almacen +
                '}';
    }
}
