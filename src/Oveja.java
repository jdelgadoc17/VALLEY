import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Oveja extends Animal  implements Serializable {
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

    public LocalDate obtenerFechaUltimoEsquilado() {
        return fechaEsquilado;
    }

    // Método para actualizar la fecha de esquilado
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

    @Override
    public String toString() {
        return "Oveja{" +
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
