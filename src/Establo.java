import java.util.ArrayList;

public class Establo {

    private ArrayList<Animal> animales;

    public Establo() {
        this.animales = new ArrayList<>();
    }


    public ArrayList<Animal> getAnimales() {
        return animales;
    }

    public void setAnimales(ArrayList<Animal> animales) {
        this.animales = animales;
    }

    public void agregarAnimal(Animal animal) {
        animales.add(animal);
    }


    public void producir(int diaActual, TipoEstacion tipoEstacion) {
        GestionDB gestionDB = GestionDB.getInstance();  // Obtiene instancia de la base de datos

        // Variables para acumular las producciones
        int totalHuevos = 0;
        int totalLana = 0;
        int totalLeche = 0;
        int totalTrufas = 0;

        for (Animal animal : animales) {
            if (animal.isAlimentado()) {  // Verifica que el animal esté alimentado
                int cantidadProducida = 0;

                // Llama al método `producir` según el tipo de animal
                if (animal instanceof Gallina) {
                    cantidadProducida = ((Gallina) animal).producir(diaActual);
                    totalHuevos += cantidadProducida;
                } else if (animal instanceof Oveja) {
                    cantidadProducida = ((Oveja) animal).producir(diaActual);
                    totalLana += cantidadProducida;
                } else if (animal instanceof Vaca) {
                    cantidadProducida = ((Vaca) animal).producir();
                    totalLeche += cantidadProducida;
                } else if (animal instanceof Cerdo) {
                    cantidadProducida = ((Cerdo) animal).producir(tipoEstacion);
                    totalTrufas += cantidadProducida;
                }

                if (cantidadProducida > 0) {
                    gestionDB.actualizarCantidad(animal.getProducto().getIdProducto(), cantidadProducida);
                    gestionDB.registrarProduccion(animal, cantidadProducida, diaActual);

                    animal.setAlimentado(false);
                } else {
                    System.out.println(animal.getNombre() + " no ha producido nada hoy.");
                }
            } else {
                System.out.println(animal.getNombre() + " no ha sido alimentado y no puede producir.");
            }
        }

        // Mostrar el resumen de producción
        mostrarResumenProduccion(totalHuevos, totalLana, totalLeche, totalTrufas);
    }

    private void mostrarResumenProduccion(int totalHuevos, int totalLana, int totalLeche, int totalTrufas) {
        System.out.println("Resumen de Producción del Día:");
        if (totalHuevos > 0) {
            System.out.println("Se han producido " + totalHuevos + " unidades de huevos.");
        }
        if (totalLana > 0) {
            System.out.println("Se han producido " + totalLana + " unidades de lana.");
        }
        if (totalLeche > 0) {
            System.out.println("Se han producido " + totalLeche + " unidades de leche.");
        }
        if (totalTrufas > 0) {
            System.out.println("Se han producido " + totalTrufas + " unidades de trufas.");
        }
    }













    public void mostrarAnimales(){
        for (Animal animal : animales) {
            System.out.println(animal);
        }
    }

    @Override
    public String toString() {
        return "Establo{" +
                "animales=" + animales +
                '}';
    }
}
