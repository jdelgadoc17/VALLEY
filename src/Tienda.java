import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class Tienda implements Serializable {

    private TreeMap<Integer, Semilla> mapa_total_semillas;
    private TreeMap<Integer, Semilla> semillasSegunEstacion;

    public Tienda() {
        mapa_total_semillas = new TreeMap<>();
        semillasSegunEstacion = new TreeMap<>();
        // Cargar las semillas desde el archivo XML
        mapa_total_semillas = FileWork.cargarSemillas("VALLEY/Resources/semillas.xml");
    }

    // Metodo para generar las semillas disponibles según la estación y el presupuesto
    public void generarSemillasDisponibles(TipoEstacion estacion, double presupuesto) {
        semillasSegunEstacion.clear();

        for (Integer id : mapa_total_semillas.keySet()) {
            Semilla semilla = mapa_total_semillas.get(id); //cogemos la semilal segun su id

            // Comprobar si la lista de estaciones de la semilla contiene la estación actual y si el presupuesto es suficiente
            if (semilla.getEstacion().contains(estacion) && presupuesto >= semilla.getPrecioCompraSemilla()) {
                semillasSegunEstacion.put(id, semilla);  // Añadir la semilla al mapa de semillas según la estación
            }
        }
    }

    // Metodo para seleccionar tres semillas aleatorias de las disponibles según la estación
    public ArrayList<Semilla> seleccionarTresSemillasAleatorias() {
        ArrayList<Semilla> listaSemillas = new ArrayList<>(semillasSegunEstacion.values());

        // Mezclar las semillas para elegir aleatoriamente
        Collections.shuffle(listaSemillas);

        ArrayList<Semilla> tresSemillas = new ArrayList<>();
        for (int i = 0; i < Math.min(3, listaSemillas.size()); i++) {
            tresSemillas.add(listaSemillas.get(i));
        }

        return tresSemillas;
    }

    // Método para vender semillas: verifica si el jugador tiene suficiente dinero
    public boolean venderSemillas(Semilla semilla, int cantidad, double presupuesto) {
        double precioTotal = semilla.getPrecioCompraSemilla() * cantidad;

        if (presupuesto >= precioTotal) {
            // Actualizar el presupuesto después de la compra
            presupuesto -= precioTotal;
            System.out.println("Has comprado " + cantidad + " semillas de " + semilla.getNombre() + " por " + precioTotal + " euros.");
            return true;  // Compra exitosa
        } else {
            System.out.println("No tienes suficiente dinero para comprar " + cantidad + " semillas de " + semilla.getNombre() + ".");
            return false;  // Compra fallida
        }
    }

    // Getters para acceder a las semillas
    public TreeMap<Integer, Semilla> getMapa_total_semillas() {
        return mapa_total_semillas;
    }

    public TreeMap<Integer, Semilla> getSemillasSegunEstacion() {
        return semillasSegunEstacion;
    }

    // Método toString para mostrar el contenido de la tienda
    @Override
    public String toString() {
        return "Tienda{" +
                "mapa_total_semillas=" + mapa_total_semillas +
                ", semillasSegunEstacion=" + semillasSegunEstacion +
                '}';
    }
}
