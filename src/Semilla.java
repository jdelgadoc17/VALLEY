public class Semilla {
    /*atributos:
    *
    * <nombre></nombre>
        <estacion></estacion>
        <diasCrecimiento></diasCrecimiento>
        <precioCompraSemilla></precioCompraSemilla>
        <precioVentaFruto></precioVentaFruto>
        <maxFrutos></maxFrutos>*/

    private String nombre;
    private TipoEstacion estacion;
    private int diasCrecimiento;
    private double precioCompraSemilla;
    private double precioVentaFruto;
    private int maxFrutos;


    public Semilla(String nombre, TipoEstacion estacion, int diasCrecimiento, double precioCompraSemilla, double precioVentaFruto, int maxFrutos) {
        this.nombre = nombre;
        this.estacion = estacion;
        this.diasCrecimiento = diasCrecimiento;
        this.precioCompraSemilla = precioCompraSemilla;
        this.precioVentaFruto = precioVentaFruto;
        this.maxFrutos = maxFrutos;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoEstacion getEstacion() {
        return estacion;
    }

    public void setEstacion(TipoEstacion estacion) {
        this.estacion = estacion;
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
        return "Semilla{" +
                "nombre='" + nombre + '\'' +
                ", estacion=" + estacion +
                ", diasCrecimiento=" + diasCrecimiento +
                ", precioCompraSemilla=" + precioCompraSemilla +
                ", precioVentaFruto=" + precioVentaFruto +
                ", maxFrutos=" + maxFrutos +
                '}';
    }
}
