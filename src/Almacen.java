import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Almacen  implements Serializable {

    private TreeMap<Semilla, Integer> mapaFrutoSemilla;

    public Almacen() {
        this.mapaFrutoSemilla = new TreeMap<>();
    }

    public void annadirCosecha(Semilla semilla, int cantidad) {
        if (mapaFrutoSemilla.containsKey(semilla)) {
            int cantidadExistente = mapaFrutoSemilla.get(semilla);
            mapaFrutoSemilla.put(semilla, cantidadExistente + cantidad);
        } else {
            mapaFrutoSemilla.put(semilla, cantidad);
        }
    }

    public int getTotalFruto() {
        int totalFrutos = 0;

        // Recorremos el mapa y sumamos la cantidad de frutos para cada tipo de semilla
        for (Map.Entry<Semilla, Integer> entrada : mapaFrutoSemilla.entrySet()) {
            totalFrutos += entrada.getValue();
        }

        return totalFrutos;
    }

    // Método para vender todos los frutos almacenados
    public void venderFrutos() {
        long gananciaTotal = 0;

        // Recorremos el mapa para calcular la ganancia
        for (Map.Entry<Semilla, Integer> entrada : mapaFrutoSemilla.entrySet()) {
            Semilla semilla = entrada.getKey();
            int cantidadFrutos = entrada.getValue();
            double precioVentaFruto = semilla.getPrecioVentaFruto();

            // Calcular ganancia por cada tipo de semilla
            long ganancia = Math.round(precioVentaFruto * cantidadFrutos);
            gananciaTotal += ganancia;

            System.out.println("Se han vendido " + cantidadFrutos + " unidades de " + semilla.getNombre() + " por " + ganancia + " euros.");
        }

        System.out.println("Ganancias totales: " + gananciaTotal + " euros.");

        mapaFrutoSemilla.clear();
    }

    // Getter para obtener el mapa del almacén
    public TreeMap<Semilla, Integer> getMapaFrutoSemilla() {
        return mapaFrutoSemilla;
    }

    // Setter para establecer un nuevo mapa
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
