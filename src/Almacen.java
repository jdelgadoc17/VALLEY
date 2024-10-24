import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Almacen  implements Serializable {

    private TreeMap<Semilla, Integer> mapaFrutoSemilla;

    public Almacen() {
        this.mapaFrutoSemilla = new TreeMap<>();
    }

    /*
     * Añade una cosecha a la almacenamiento, si la semilla ya existe, suma la cantidad al ya existente.
     */

    public void annadirCosecha(Semilla semilla, int cantidad) {
        if (mapaFrutoSemilla.containsKey(semilla)) {

            int cantidadExistente = mapaFrutoSemilla.get(semilla);

            mapaFrutoSemilla.put(semilla, cantidadExistente + cantidad);
        } else {
            mapaFrutoSemilla.put(semilla, cantidad);
        }
    }

    /*
     * Devuelve el total de frutos almacenados en la almacenamiento.
     */

    public int getTotalFruto() {
        int totalFrutos = 0;

        for (Map.Entry<Semilla, Integer> entrada : mapaFrutoSemilla.entrySet()) {
            totalFrutos += entrada.getValue();
        }
        return totalFrutos;
    }


    /*
     * Vende todos los frutos almacenados en la almacenamiento y limpia el mapa de frutos.
     */

    public void venderFrutos() {
        long gananciaTotal = 0;

        for (Map.Entry<Semilla, Integer> entrada : mapaFrutoSemilla.entrySet()) {
            Semilla semilla = entrada.getKey();
            int cantidadFrutos = entrada.getValue();
            double precioVentaFruto = semilla.getPrecioVentaFruto();

            long ganancia = Math.round(precioVentaFruto * cantidadFrutos);
            gananciaTotal += ganancia;

            System.out.println("Se han vendido " + cantidadFrutos + " unidades de " + semilla.getNombre() + " por " + ganancia + " euros.");
        }

        System.out.println("Ganancias totales: " + gananciaTotal + " euros.");

        mapaFrutoSemilla.clear();
    }

    public TreeMap<Semilla, Integer> getMapaFrutoSemilla() {
        return mapaFrutoSemilla;
    }

    public void setMapaFrutoSemilla(TreeMap<Semilla, Integer> mapaFrutoSemilla) {
        this.mapaFrutoSemilla = mapaFrutoSemilla;
    }

    @Override
    public String toString() {
        return "Almacen{" +
                "mapaFrutoSemilla=" + mapaFrutoSemilla +
                '}';
    }
}
