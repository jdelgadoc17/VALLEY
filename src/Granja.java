import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Granja {
    private static final int TAMANNO_CELDA = Integer.BYTES+1+Integer.BYTES;

    //CREAR CONSTANTES
    private int diaActual;
    private TipoEstacion tipoEstacion;
    private double presupuesto;
    private Tienda tienda;
    private Almacen almacen;

    public Granja(int diaActual, TipoEstacion tipoEstacion, double presupuesto, Tienda tienda, Almacen almacen) {
        this.diaActual = diaActual;
        this.tipoEstacion = tipoEstacion;
        this.presupuesto = presupuesto;
        this.tienda = tienda;
        this.almacen = almacen;

    }

    public void mostrarInfo(){
        System.out.println("INFORMACION DE LA GRANJA");
        System.out.println("- Días de juego: "+diaActual);
        System.out.println("- Dinero disponible: "+presupuesto);
        System.out.println("- Estacion: "+tipoEstacion);
        System.out.println("- Semillas en venta: "+tienda.getSemillasSegunEstacion());
        System.out.println("- Frutos en almacen: "+almacen.getTotalFruto());
        System.out.println("- Estado del huerto: "+);

    }


    public void crearHuerto(Path path) throws IOException {
        FileWork fileWork = FileWork.getInstancia();
        Properties properties = fileWork.cargarProperties();

        int filas = Integer.parseInt(properties.getProperty("numFilas"));
        int columnas = Integer.parseInt(properties.getProperty("numColumnas"));

        Path archivoHuerto = Paths.get("Resources/archivoHuerto.dat");
        if (!path.toFile().exists()) {
            archivoHuerto = new RandomAccessFile(path.toFile(), "rw");
            archivoHuerto.setLength(0);

            for (int i = 0; i < filas * columnas; i++) {
                archivoHuerto.writeInt(-1);
                archivoHuerto.writeBoolean(false);
                archivoHuerto.writeInt(-1);
            }

            System.out.println("Huerto inicializado con " + filas + " filas y " + columnas + " columnas.");
        }
    }



    public void plantarColumna(){
        FileWork fileWork = FileWork.getInstancia();
        Properties properties = fileWork.cargarProperties();
        int filas = Integer.parseInt(properties.getProperty("numFilas"));
        int columnas = Integer.parseInt(properties.getProperty("numColumnas"));

        for(){

            long posicion = (x * columnas + y) * TAMANNO_CELDA;
            archivoHuerto.seek(posicion);

            int idSemilla = fileWork.pedSemilla();
            if (idSemilla!= -1 && presupuesto >= fileWork.pedPrecioSemilla(idSemilla)) {
                archivoHuerto.writeInt(idSemilla);
                archivoHuerto.writeBoolean(true);
                archivoHuerto.writeInt(0);
                presupuesto -= fileWork.pedPrecioSemilla(idSemilla);

                System.out.println("Semilla plantada en la celda (" + x + "," + y + ").");
            } else {
                System.out.println("No hay suficiente dinero
            }




            // solo se muestran las semillas
            //para llegar a la xoumna hay que saltar 9 bytes por la columna ddne estemos
            //para saltar fila hay que saltar 9 bytes por cada celda
            //algoritmo 2celdas * 9bytes  1 celda *9bytes (num de celda segun el tamaño de nuestro huerto)

        }

    }










        public void actualizarCultivos() {
            FileWork fileWork = FileWork.getInstancia();
            Properties properties = fileWork.cargarProperties();
            int filas = Integer.parseInt(properties.getProperty("numFilas"));
            int columnas = Integer.parseInt(properties.getProperty("numColumnas"));
            try {
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


        //PASAR POR PAREMTRO EL MAPA DE TODAS LAS SEMILALS CARGADAS (ESTA EN LA TIENDA) PARA SACAR EL ID AL VER SI SE PUEDE COSECHAR
        public void atenderCultivos(Path path) {
            FileWork fileWork = FileWork.getInstancia();
            Properties properties = fileWork.cargarProperties();
            int filas = Integer.parseInt(properties.getProperty("numFilas"));
            int columnas = Integer.parseInt(properties.getProperty("numColumnas"));

            Path archivoHuerto = Paths.get(path.toUri());

            try {
                archivoHuerto.seek(0);
                for (int i = 0; i < filas * columnas; i++) {
                    long posicion = i * TAMANNO_CELDA;
                    archivoHuerto.seek(posicion);

                    int idSemilla = archivoHuerto.readInt();
                    boolean regado = archivoHuerto.readBoolean();
                    int diasPlantado = archivoHuerto.readInt();

                    if (idSemilla != -1) { // Si hay una semilla plantada

                        // Regar el cultivo si no ha sido regado
                        if (!regado) {
                            archivoHuerto.seek(posicion + 4);
                            archivoHuerto.writeBoolean(true); // Marcar como regado
                        }

                        // Obtener la semilla y verificar si está lista para la cosecha
                        Semilla semilla = obtenerSemilla(idSemilla);
                        if (semilla != null && diasPlantado >= semilla.getDiasCrecimiento()) {
                            // Generar frutos de manera aleatoria hasta el máximo
                            int frutosRecolectados = (int) (Math.random() * semilla.getMaxFrutos()) + 1;
                            almacen.annadirCosecha(semilla.getNombre(), frutosRecolectados);

                            // Restablecer la celda a su estado predeterminado
                            archivoHuerto.seek(posicion);
                            archivoHuerto.writeInt(-1); // Vaciar ID de la semilla
                            archivoHuerto.writeBoolean(false); // Estado de regado
                            archivoHuerto.writeInt(-1); // Restablecer días plantados
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



        public void reestablecerCultivos(){
            FileWork fileWork = FileWork.getInstancia();
            Properties properties = fileWork.cargarProperties();
            int filas = Integer.parseInt(properties.getProperty("numFilas"));
            int columnas = Integer.parseInt(properties.getProperty("numColumnas"));

            try{
                archivoHuerto.seek(0); //comprobar si esta rellena, si esta vacia no ahce falta hacerlo
                for(int i=0;i<filas*columnas; i++){
                    long posicion = i* TAMANNO_CELDA;
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



        public void venderFruta(){


        }

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



        public static void iniciarNuevoDia(){
            FileWork fileWork = FileWork.getInstancia();
            Properties properties = fileWork.cargarProperties();
        }
            diaActual++;

            if (diaActual > properties.getdiasDuracionEstacion()) { //CUANTOS DIAS?
                diaActual = 1;
                cambiarEstacion();
                reestablecerCultivos();
            } else {
                actualizarCultivos();
            }

            tienda.generarSemillasDisponibles(tipoEstacion); //y de esas tres sacar tres semillas O UN METODO ENTERO GENERAR_TIENDA() CON 3 random de esa estacion

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
