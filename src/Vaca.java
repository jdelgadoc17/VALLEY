public class Vaca extends Animal {
    private double peso;

    public Vaca(int id, String nombre,  TipoAnimal tipo, int diaInsercion, Alimento alimento, Producto producto, double peso) {
        super(id, nombre, tipo, diaInsercion, alimento, producto);  // Se usa el enum TipoAnimal.VACA
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

    @Override
    public String toString() {
        return "Vaca{" +
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
