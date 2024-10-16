import java.io.IOException;
import java.io.RandomAccessFile;

public class Huerto {
//ESTA CLASE NO HAY QUE HACERLA SE TRABAAJ SOLO POR EL RAF
    //EL CREAR EL JUEGO SE CREA EL FICHERO CON LAS FILAS Y COLUMNAS DADAS EN PROP




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


        public void plantarColumna(){
            // solo se muestran las semillas
            //para llegar a la xoumna hay que saltar 9 bytes por la columna ddne estemos
        //para saltar fila hay que saltar 9 bytes por cada celda
        //algoritmo 2celdas * 9bytes  1 celda *9bytes (num de celda segun el tamaño de nuestro huerto)
        }

    }


    private void iniciarHuerto() throws IOException { //ESTE METODO ESTA BIEN

        archivoHuerto.setLength(0);

        //segun las filas y columans de larchivo properties

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
            archivoHuerto.seek(0); //comprobar si esta relena, si esta vacia no ahce falta hacerlo
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

            for(int i=0;i<filas*columnas; i++){ //hay que comprobar si el cultivo no esta plantado, en ese  caso no se actualiza

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

                        int frutosRecolectados = semilla.getMaxFrutos(); //generar lateatoriuoi segun el maxfrutos y esos guardarlos como frutos
                        almacen.agregarFrutos(semilla.getNombre(), frutosRecolectados);

                        //PONER DE NUEVO VALORES POR DEFECTO AL RECOLECTAR ESA CELDA-SEMILLA
                        //LA PONEMOS VACIA DE NUEVO
                        archivoHuerto.seek(posicion); //est ovulve a tras?
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
