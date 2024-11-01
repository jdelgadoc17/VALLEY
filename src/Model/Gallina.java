package Model;

import Files.GestionDB;

import java.io.Serializable;

public class Gallina extends Animal implements Serializable {


    public Gallina(int id, String nombre, TipoAnimal tipo, int diaInsercion, Alimento alimento, Producto producto) {
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




    public int producir(int diaActual){
        int diasEnJuego = diaActual - this.diaInsercion;
        if (diasEnJuego > 3 && diasEnJuego <= 40) {
            return 2;
        } else if (diasEnJuego > 40) {
            return 1;
        }
        return 0;
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
            System.out.println("No hay suficiente alimento para " + getNombre() + ".");
            return false;
        }
    }




    @Override
    public String toString() {
        return "Model.Gallina{" +
                "producto=" + producto +
                ", alimento=" + alimento +
                ", diaInsercion=" + diaInsercion +
                ", tipo='" + tipo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", id=" + id +
                '}';
    }
}
