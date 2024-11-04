package model;

import files.GestionDB;

import java.io.Serializable;

import static model.TipoEstacion.*;


public class Cerdo extends Animal implements Serializable {


    public Cerdo(int id, String nombre, model.TipoAnimal tipo, int diaInsercion, Alimento alimento, Producto producto) {
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
    public model.TipoAnimal getTipo() {
        return super.getTipo();
    }

    @Override
    public void setTipo(model.TipoAnimal tipo) {
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

    @Override
    public int producir(int diaActual, TipoEstacion tipoEstacion) {
        return switch (tipoEstacion) {
            case PRIMAVERA, VERANO -> (int) (Math.random() * 2) + 2;
            case OTOÑO ->   (int) (Math.random() * 2);
            default -> 0;
        };
    }



    public boolean alimentar() {
        GestionDB gestionDB = GestionDB.getInstance();
        int cantidadNecesaria = 1;
        int cantidadDisponible = gestionDB.getCantidadDisponibleAlimento(this.getAlimento().getIdAlimento());

        if (cantidadDisponible >= cantidadNecesaria) {
            gestionDB.actualizarCantidad(this.getAlimento().getIdAlimento(), -cantidadNecesaria);
            gestionDB.registrarConsumo(this, cantidadNecesaria);
            this.setAlimentado(true);
            System.out.println(getNombre() + " ha sido alimentada.");
            return true;
        } else {
            System.out.println("No hay suficiente alimento para" + getNombre() + ".");
            return false;
        }
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



