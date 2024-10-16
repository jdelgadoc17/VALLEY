import java.util.TreeMap;

public class Tienda {

    private TreeMap<Integer, Semilla> mapa_total_semillas;
    private TreeMap<Integer, Semilla> semillasSegunEstacion;

    public Tienda() {
        mapa_total_semillas = new TreeMap<>();
        semillasSegunEstacion = new TreeMap<>();

        FileWork fileWork = new FileWork();
        mapa_total_semillas = fileWork.cargarSemillas("Resources/semillas.xml");
    }
        //AL COMPRRAR SEMILLAS, SOLO SE MUESTRAN LAS DISPONIBLES SEGUN EL PRESUPUESTO

    public void generarSemillasDisponibles(TipoEstacion estacion) {
        semillasSegunEstacion.clear();

        for (Integer id : mapa_total_semillas.keySet()) {
            Semilla semilla = mapa_total_semillas.get(id);
            if (semilla.getEstacion() == estacion) {
                semillasSegunEstacion.put(id, semilla);
            }
        }
    }

    public TreeMap<Integer, Semilla> getMapa_total_semillas() {
        return mapa_total_semillas;
    }

    public TreeMap<Integer, Semilla> getSemillasSegunEstacion() {
        return semillasSegunEstacion;
    }

    @Override
    public String toString() {
        return "Tienda{" +
                "mapa_total_semillas=" + mapa_total_semillas +
                ", semillasSegunEstacion=" + semillasSegunEstacion +
                '}';
    }
}
