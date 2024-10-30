
public abstract class Animal {

    protected int id;
    protected String nombre;
    protected TipoAnimal tipo;
    protected int diaInsercion;
    protected Alimento alimento;
    protected Producto producto;

    public Animal(int id, String nombre, TipoAnimal tipo, int diaInsercion, Alimento alimento, Producto producto) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.diaInsercion = diaInsercion;
        this.alimento = alimento;
        this.producto = producto;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoAnimal getTipo() {
        return tipo;
    }

    public void setTipo(TipoAnimal tipo) {
        this.tipo = tipo;
    }

    public int getDiaInsercion() {
        return diaInsercion;
    }

    public void setDiaInsercion(int diaInsercion) {
        this.diaInsercion = diaInsercion;
    }

    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", diaInsercion=" + diaInsercion +
                ", alimento=" + alimento +
                ", producto=" + producto +
                '}';
    }
}
