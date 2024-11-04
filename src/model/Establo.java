package model;

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


    public void mostrarAnimales() {
        System.out.println(String.format("%-10s %-15s %-10s %-15s %-15s %-15s",
                "ID", "Nombre", "Tipo", "Día Inserción", "Alimento", "Producto"));
        System.out.println("--------------------------------------------------------------------------------");

        for (Animal animal : animales) {
            System.out.println(String.format("%-10d %-15s %-10s %-15d %-15s %-15s",
                    animal.getId(),
                    animal.getNombre(),
                    animal.getTipo(),
                    animal.getDiaInsercion(),
                    animal.getAlimento() != null ? animal.getAlimento().getNombreAlimento() : "N/A",
                    animal.getProducto() != null ? animal.getProducto().getNombreProducto() : "N/A"));
        }
    }


    @Override
    public String toString() {
        return "Model.Establo{" +
                "animales=" + animales +
                '}';
    }
}