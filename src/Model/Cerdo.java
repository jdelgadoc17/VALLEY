package Model;

import java.io.Serializable;



public class Cerdo extends Animal implements Serializable {


    public Cerdo(int id, String nombre, Model.TipoAnimal tipo, int diaInsercion, Alimento alimento, Producto producto) {
        super(id, nombre, tipo, diaInsercion, alimento, producto);
    }


    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    public void setNombre(String nombre) {
        super.setNombre(nombre);
    }

    @Override
    public Model.TipoAnimal getTipo() {
        return super.getTipo();
    }

    @Override
    public void setTipo(Model.TipoAnimal tipo) {
        super.setTipo(tipo);
    }

    @Override
    public int getDiaInsercion() {
        return super.getDiaInsercion();
    }

    @Override
    public void setDiaInsercion(int diaInsercion) {
        super.setDiaInsercion(diaInsercion);
    }

    @Override
    public Alimento getAlimento() {
        return super.getAlimento();
    }

    @Override
    public void setAlimento(Alimento alimento) {
        super.setAlimento(alimento);
    }

    @Override
    public Producto getProducto() {
        return super.getProducto();
    }

    @Override
    public void setProducto(Producto producto) {
        super.setProducto(producto);
    }

    public int producir(TipoEstacion estacion) {
        return switch (estacion) {
            case TipoEstacion.PRIMAVERA, TipoEstacion.VERANO -> (int) (Math.random() * 2) + 2;
            case TipoEstacion.OTOÑO -> (int) (Math.random() * 2);
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return "Model.Cerdo{" +
                "producto=" + producto +
                ", alimento=" + alimento +
                ", diaInsercion=" + diaInsercion +
                ", tipo='" + tipo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", id=" + id +
                '}';
    }
}



