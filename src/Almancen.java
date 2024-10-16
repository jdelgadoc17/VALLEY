import java.util.TreeMap;

public class Almancen {

    private TreeMap<Integer, Semilla> mapa_fruto_semilla;
    //AL COSECHAR METER LA SEMILLA ENTERA, LA CLAVE DEL MAPA LA SEMILLA, GUARDAS LAS SEMILLAS Y SE MUESTRAN




    public Almancen() {
        this.mapa_fruto_semilla = new TreeMap<>();
    }

    public void venderFrutos(){
        //recorrer mapa
        long gananciaTotal =0;

        for(Semilla semilla : mapa_fruto_semilla.values()){
            double cadaPrecio = semilla.getPrecioVentaFruto();

        }
    }


    public TreeMap<Integer, Semilla> getMapa_fruto_semilla() {
        return mapa_fruto_semilla;
    }

    public void setMapa_fruto_semilla(TreeMap<Integer, Semilla> mapa_fruto_semilla) {
        this.mapa_fruto_semilla = mapa_fruto_semilla;
    }


    @Override
    public String toString() {
        return "Almancen{" +
                "mapa_fruto_semilla=" + mapa_fruto_semilla +
                '}';
    }
}
