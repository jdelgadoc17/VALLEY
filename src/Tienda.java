import java.util.TreeMap;

public class Tienda {

    private TreeMap<Integer, Semilla> mapa_total_semillas; // Todas las semillas cargadas desde el XML
    private TreeMap<Integer, Semilla> semillasSegunEstacion; // Semillas disponibles según la estación actual

    public Tienda() {
        mapa_total_semillas = new TreeMap<>(); // Mapa para todas las semillas (ID -> Semilla)
        semillasSegunEstacion = new TreeMap<>(); // Mapa para las semillas según la estación (ID -> Semilla)

        // Usamos FileWork para cargar las semillas desde el archivo XML
        FileWork fileWork = new FileWork();
        mapa_total_semillas = fileWork.cargarSemillas("Resources/semillas.xml");
    }

    // Método para generar las semillas disponibles según la estación actual
    public void generarSemillasDisponibles(TipoEstacion estacion) {
        // Limpiamos el mapa de semillas según la estación antes de actualizarlo
        semillasSegunEstacion.clear();

        // Iteramos sobre todas las semillas del mapa total y filtramos por la estación
        for (Integer id : mapa_total_semillas.keySet()) {
            Semilla semilla = mapa_total_semillas.get(id);
            if (semilla.getEstacion() == estacion) {
                // Añadimos la semilla al mapa de semillas según la estación
                semillasSegunEstacion.put(id, semilla);
            }
        }
    }

    // Método para obtener el mapa de todas las semillas
    public TreeMap<Integer, Semilla> getMapa_total_semillas() {
        return mapa_total_semillas;
    }

    // Método para obtener el mapa de semillas según la estación
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
