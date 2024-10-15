public class Granja {

    private int diaActual;
    private TipoEstacion tipoEstacion;
    private double presupuesto;
    private Tienda tienda;
    private Almacen almacen;

    public Granja(int diaActual, TipoEstacion tipoEstacion, double presupuesto, Tienda tienda, Almacen almacen) {
        this.diaActual = diaActual;
        this.tipoEstacion = tipoEstacion;
        this.presupuesto = presupuesto;
        this.tienda = tienda;
        this.almacen = almacen;
    }

    public void cambiarEstacion() {
        switch (tipoEstacion) {
            case PRIMAVERA:
                tipoEstacion = TipoEstacion.VERANO;
                break;
            case VERANO:
                tipoEstacion = TipoEstacion.OTOÑO;
                break;
            case OTOÑO:
                tipoEstacion = TipoEstacion.INVIERNO;
                break;
            case INVIERNO:
                tipoEstacion = TipoEstacion.PRIMAVERA;
                break;
        }

        huerto.reiniciarCultivos();

    }

    public void avanzarDia(){
        diaActual++;

        if (diaActual > diasPorEstacion) {
            diaActual = 1;
            cambiarEstacion();
        } else {
            actualizarCultivos();
        }

        tienda.generarSemillasDisponibles(tipoEstacion);

    }

    public int getDiaActual() {
        return diaActual;
    }

    public void setDiaActual(int diaActual) {
        this.diaActual = diaActual;
    }

    public TipoEstacion getTipoEstacion() {
        return tipoEstacion;
    }

    public void setTipoEstacion(TipoEstacion tipoEstacion) {
        this.tipoEstacion = tipoEstacion;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    @Override
    public String toString() {
        return "Granja{" +
                "diaActual=" + diaActual +
                ", tipoEstacion=" + tipoEstacion +
                ", presupuesto=" + presupuesto +
                ", tienda=" + tienda +
                ", almacen=" + almacen +
                '}';
    }
}
