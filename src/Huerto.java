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

}
