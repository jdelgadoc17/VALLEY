package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Semilla  implements Serializable, Comparable<Semilla> {

    private int id;
    private String nombre;
    private ArrayList<TipoEstacion> estacion;
    private int diasCrecimiento;
    private double precioCompraSemilla;
    private double precioVentaFruto;
    private int maxFrutos;


    public Semilla(int id, String nombre, ArrayList<TipoEstacion> estaciones, int diasCrecimiento, double precioCompraSemilla, double precioVentaFruto, int maxFrutos) {
        this.id=id;
        this.nombre = nombre;
        this.estacion = estaciones;  // Ahora acepta un ArrayList de estaciones
        this.diasCrecimiento = diasCrecimiento;
        this.precioCompraSemilla = precioCompraSemilla;
        this.precioVentaFruto = precioVentaFruto;
        this.maxFrutos = maxFrutos;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<TipoEstacion> getEstacion() {
        return estacion;
    }

    public void setEstacion(ArrayList<TipoEstacion> estaciones) {
        this.estacion = estaciones;
    }

    public int getDiasCrecimiento() {
        return diasCrecimiento;
    }

    public void setDiasCrecimiento(int diasCrecimiento) {
        this.diasCrecimiento = diasCrecimiento;
    }

    public double getPrecioCompraSemilla() {
        return precioCompraSemilla;
    }

    public void setPrecioCompraSemilla(double precioCompraSemilla) {
        this.precioCompraSemilla = precioCompraSemilla;
    }

    public double getPrecioVentaFruto() {
        return precioVentaFruto;
    }

    public void setPrecioVentaFruto(double precioVentaFruto) {
        this.precioVentaFruto = precioVentaFruto;
    }

    public int getMaxFrutos() {
        return maxFrutos;
    }

    public void setMaxFrutos(int maxFrutos) {
        this.maxFrutos = maxFrutos;
    }

    @Override
    public String toString() {
        return "Model.Semilla{" +
                "id=" + id +
                "nombre='" + nombre + '\'' +
                ", estacion=" + estacion +
                ", diasCrecimiento=" + diasCrecimiento +
                ", precioCompraSemilla=" + precioCompraSemilla +
                ", precioVentaFruto=" + precioVentaFruto +
                ", maxFrutos=" + maxFrutos +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Semilla semilla = (Semilla) obj;
        return id == semilla.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public int compareTo(Semilla otraSemilla) {
        return Integer.compare(this.id, otraSemilla.id);
    }
}
