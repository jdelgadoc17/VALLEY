public class Oveja extends Animal{
    public Oveja(int id, String nombre, TipoAnimal tipo, int diaInsercion, Alimento alimento, Producto producto) {
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




    public int producir(int diaActual) {
        int diasDesdeUltimoEsquilado = diaActual - obtenerDiaUltimoEsquilado();  //QUE ES ESTO
        if (diasDesdeUltimoEsquilado >= 2) {
            actualizarFechaEsquilado(diaActual);
            return 5;
        }
        return 0;
    }





    @Override
    public String toString() {
        return "Oveja{" +
                "producto=" + producto +
                ", alimento=" + alimento +
                ", diaInsercion=" + diaInsercion +
                ", tipo='" + tipo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", id=" + id +
                '}';
    }
}
