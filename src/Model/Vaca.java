package Model;
import Files.GestionDB;
import java.io.Serializable;

public class Vaca extends Animal implements Serializable {
    private double peso;

    public Vaca(int id, String nombre, TipoAnimal tipo, int diaInsercion, Alimento alimento, Producto producto, double peso) {
        super(id, nombre, tipo, diaInsercion, alimento, producto);  // Se usa el enum Model.TipoAnimal.VACA
        this.peso = peso;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

     public int producir() {
        return (int) (0.01 * this.getPeso());
    }


    public boolean alimentar(int diaActual) {
        GestionDB gestionDB = GestionDB.getInstance();
        int diasEnJuego = diaActual - getDiaInsercion();
        int cantidadNecesaria;

        if (diasEnJuego < 10) {
            cantidadNecesaria = 1;
        } else if (diasEnJuego < 40) {
            cantidadNecesaria = 3;
        } else {
            cantidadNecesaria = 2;
        }

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
        return "Model.Vaca{" +
                "peso=" + peso +
                ", id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", diaInsercion=" + diaInsercion +
                ", alimento=" + alimento +
                ", producto=" + producto +
                '}';
    }
}
