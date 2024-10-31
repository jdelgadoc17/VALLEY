import java.io.Serializable;
import java.util.ArrayList;

public class Establo implements Serializable {

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


    public void mostrarAnimales(){
        for (Animal animal : animales) {
            System.out.println(animal.toString());
        }
    }

    @Override
    public String toString() {
        return "Establo{" +
                "animales=" + animales +
                '}';
    }
}