package model;

import files.GestionDB;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Oveja extends Animal implements Serializable {
    private LocalDate fechaEsquilado;

    public Oveja(int id, String nombre, TipoAnimal tipo, int diaInsercion, Alimento alimento, Producto producto) {
        super(id, nombre, tipo, diaInsercion, alimento, producto);
        this.fechaEsquilado = null;
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
    public TipoAnimal getTipo() {
        return super.getTipo();
    }

    @Override
    public void setTipo(TipoAnimal tipo) {
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
        if (fechaEsquilado == null || ChronoUnit.DAYS.between(fechaEsquilado, LocalDate.now()) >= 2) {
            actualizarFechaEsquilado();
            return 5;
        }
        return 0;
    }

    public LocalDate obtenerFechaUltimoEsquilado() {
        return fechaEsquilado;
    }

    public void actualizarFechaEsquilado() {
        this.fechaEsquilado = LocalDate.now();
    }

    public int producir() {
        if (fechaEsquilado == null || ChronoUnit.DAYS.between(fechaEsquilado, LocalDate.now()) >= 2) {
            actualizarFechaEsquilado();
            return 5;
        }
        return 0;
    }

    /*
    Metodo de alimentar de la clase
     */


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
            System.out.println("No hay suficiente alimento para " + getNombre() + ".");
            return false;
        }
    }

    @Override
    public String toString() {
        return "Model.Oveja{" +
                "producto=" + getProducto() +
                ", alimento=" + getAlimento() +
                ", diaInsercion=" + getDiaInsercion() +
                ", tipo='" + getTipo() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", id=" + getId() +
                ", fechaEsquilado=" + (fechaEsquilado != null ? fechaEsquilado : "Nunca esquilada") +
                '}';
    }
}
