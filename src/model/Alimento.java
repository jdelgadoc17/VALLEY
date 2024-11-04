package model;

import java.io.Serializable;

public class Alimento implements Serializable {

    private int idAlimento;
    private String nombreAlimento;
    private double precioAlimento;
    private int cantidadDisponibleAlimento;

    public Alimento(int idAlimento, String nombreAlimento, double precioAlimento, int cantidadDisponibleAlimento) {
        this.idAlimento = idAlimento;
        this.nombreAlimento = nombreAlimento;
        this.precioAlimento = precioAlimento;
        this.cantidadDisponibleAlimento = cantidadDisponibleAlimento;
    }

    public Alimento(String alimento) {
    }


    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public String getNombreAlimento() {
        return nombreAlimento;
    }

    public void setNombreAlimento(String nombreAlimento) {
        this.nombreAlimento = nombreAlimento;
    }

    public double getPrecioAlimento() {
        return precioAlimento;
    }

    public void setPrecioAlimento(double precioAlimento) {
        this.precioAlimento = precioAlimento;
    }

    public int getCantidadDisponibleAlimento() {
        return cantidadDisponibleAlimento;
    }

    public void setCantidadDisponibleAlimento(int cantidadDisponibleAlimento) {
        this.cantidadDisponibleAlimento = cantidadDisponibleAlimento;
    }

    @Override
    public String toString() {
        return "Model.Alimento{" +
                "idAlimento=" + idAlimento +
                ", nombreAlimento='" + nombreAlimento + '\'' +
                ", precioAlimento=" + precioAlimento +
                ", cantidadDisponibleAlimento=" + cantidadDisponibleAlimento +
                '}';
    }
}
