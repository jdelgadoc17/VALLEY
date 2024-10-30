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

    @Override
    public String toString() {
        return "Establo{" +
                "animales=" + animales +
                '}';
    }
}
