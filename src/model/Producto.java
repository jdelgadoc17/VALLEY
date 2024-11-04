package model;

import java.io.Serializable;

public class Producto implements Serializable {

    private int idProducto;
    private String nombreProducto;
    private double precioProducto;
    private int cantidadDisponibleProducto;

    public Producto(int idProducto, String nombreProducto, double precioProducto, int cantidadDisponibleProducto) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
        this.cantidadDisponibleProducto = cantidadDisponibleProducto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public int getCantidadDisponibleProducto() {
        return cantidadDisponibleProducto;
    }

    public void setCantidadDisponibleProducto(int cantidadDisponibleProducto) {
        this.cantidadDisponibleProducto = cantidadDisponibleProducto;
    }

    @Override
    public String toString() {
        return "Model.Producto{" +
                "idProducto=" + idProducto +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", precioProducto=" + precioProducto +
                ", cantidadDisponibleProducto=" + cantidadDisponibleProducto +
                '}';
    }
}
