import java.io.IOException;
import java.io.RandomAccessFile;

public class Huerto {

    private RandomAccessFile archivoHuerto;
    private int filas;
    private int columnas;
    private static final int TAMANNO_CELDA = Integer.BYTES+1+Integer.BYTES;


    public Huerto( int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;

        try{

            archivoHuerto = new RandomAccessFile("huerto.dat", "rw");
            iniciarHuerto();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void iniciarHuerto() throws IOException {

        archivoHuerto.setLength(0);

        for(int i = 0; i <filas*columnas; i++){
            //INICIAR CADA CELDA DEL HUERTO
            archivoHuerto.writeInt(-1);
            archivoHuerto.writeBoolean(false);
            archivoHuerto.writeInt(-1);
        }


    }

    private long calcularPosicion(int fila, int columna){
        return (fila*columnas+columna)*TAMANNO_CELDA; //EXPLICAR
    }


    public void reestablecerCultivos(){
        try{
            archivoHuerto.seek(0);
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

    public void actualizarCultivos() {

        try{

            archivoHuerto.seek(0);

            for(int i=0;i<filas*columnas; i++){

                long posicion = i* TAMANNO_CELDA;
                archivoHuerto.seek(posicion);

                int idSemilla = archivoHuerto.readInt();
                boolean regado = archivoHuerto.readBoolean();
                int diasPlantado= archivoHuerto.readInt();

                if(idSemilla!=1 && regado){
                    diasPlantado++;
                    archivoHuerto.seek(posicion +5);
                    archivoHuerto.writeInt(diasPlantado);

                    archivoHuerto.seek(posicion+4);
                    archivoHuerto.writeBoolean(false);



                }

            }
            System.out.println("CULTIVOS ACTUALIZADOS");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void atenderCultivos(){

        try{
            archivoHuerto.seek(0);

            for( int i=0; i<filas*columnas; i++){

                long posicion = i* TAMANNO_CELDA;
                archivoHuerto.seek(posicion);
                int idSemilla = archivoHuerto.readInt();
                boolean regado = archivoHuerto.readBoolean();
                int diasPlantado = archivoHuerto.readInt();

                if(idSemilla!=1){

                    if(!regado){
                        archivoHuerto.seek(posicion+4);
                        archivoHuerto.writeBoolean(true);

                    }

                    Semilla semilla = obtenerSemilla(idSemilla);

                    if(semilla!=null && diasPlantado>=semilla.getDiasCrecimiento()){

                        int frutosRecolectados = semilla.getMaxFrutos();
                        almacen.agregarFrutos(semilla.getNombre(), frutosRecolectados);

                        //PONER DE NUEVO VALORES POR DEFECTO AL RECOLECTAR ESA CELDA-SEMILLA
                        //LA PONEMOS VACIA DE NUEVO
                        archivoHuerto.seek(posicion);
                        archivoHuerto.writeInt(-1);
                        archivoHuerto.writeBoolean(false);
                        archivoHuerto.writeInt(-1);




                    }



                }


            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }


    public Semilla obtenerSemilla(int idSemilla) {
        return mapa_total_semillas.get(idSemilla); // Acceder directamente al TreeMap de semillas
    }


}
