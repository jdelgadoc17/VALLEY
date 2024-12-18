package model;
import files.FileWork;
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
        mapa_total_semillas = FileWork.cargarSemillas("Resources/semillas.xml");
    }

    public void generarSemillasDisponibles(TipoEstacion estacion, double presupuesto) {
        semillasSegunEstacion.clear();



        for (Integer id : mapa_total_semillas.keySet()) {
            Semilla semilla = mapa_total_semillas.get(id);

            if (semilla.getEstacion().contains(estacion) && presupuesto >= semilla.getPrecioCompraSemilla()) {
                semillasSegunEstacion.put(id, semilla);
            }
        }
    }
    /*
    Sacar tres semillas aleatorias
     */

    public ArrayList<Semilla> seleccionarTresSemillasAleatorias() {
        ArrayList<Semilla> listaSemillas = new ArrayList<>(semillasSegunEstacion.values());

        Collections.shuffle(listaSemillas);

        ArrayList<Semilla> tresSemillas = new ArrayList<>();
        for (int i = 0; i < Math.min(3, listaSemillas.size()); i++) {
            tresSemillas.add(listaSemillas.get(i));
        }

        return tresSemillas;
    }

    /*
    Se venden las semillas
     */

    public boolean venderSemillas(Semilla semilla, int cantidad, double presupuesto) {
        double precioTotal = semilla.getPrecioCompraSemilla() * cantidad;

        if (presupuesto >= precioTotal) {
            presupuesto -= precioTotal;
            System.out.println("Has comprado" + cantidad + " semillas de " + semilla.getNombre() + " por " + precioTotal + " euros.");
            return true;
        } else {
            System.out.println("No tienes suficiente dinero para comprar " + cantidad + " semillas de " + semilla.getNombre() + ".");
            return false;
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
        return "Model.Tienda{" +
                "mapa_total_semillas=" + mapa_total_semillas +
                ", semillasSegunEstacion=" + semillasSegunEstacion +
                '}';
    }
}
