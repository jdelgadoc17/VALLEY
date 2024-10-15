import java.util.ArrayList;

public class Tienda {

    private ArrayList<Semilla> lista_total_semillas;
    private ArrayList<Semilla> semillasSegunEstacion;

    public Tienda() {
        lista_total_semillas = new ArrayList<>();
        semillasSegunEstacion = new ArrayList<>();
        FileWork fileWork = new FileWork();
        lista_total_semillas = fileWork.cargarSemillas("Resources/semillas.xml");

    }

    public void generarSemillasDisponibles(TipoEstacion estacion) {

        for (Semilla semilla : lista_total_semillas) {
            if (semilla.getEstacion() == estacion) {
                semillasEnEstacion.add(semilla);
            }
        }
    }



    public ArrayList<Semilla> getLista_total_semillas() {
        return lista_total_semillas;
    }

    public void setLista_total_semillas(ArrayList<Semilla> lista_total_semillas) {
        this.lista_total_semillas = lista_total_semillas;
    }

    @Override
    public String toString() {
        return "Tienda{" +
                "lista_total_semillas=" + lista_total_semillas +
                '}';
    }
}
