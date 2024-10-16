import java.util.Scanner;

public class Main {

    public static int pedOpc() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Elige una opción:");
        return sc.nextInt();
    }

    public static void jugar(){

        switch (pedOpc()) {
            case 1:
                iniciarNuevoDia();
                break;
            case 2:
                atenderCultivos();
                break;
            case 3:
                plantarEnColumna();
                break;
            case 4:
                venderCosecha();
                break;
            case 5:
                mostrarInfo();
                break;
            case 6:
                salir();
                break;
            default:
                System.out.println("Opción incorrecta");

        }
    }



    public static void elegirPropiedades() {

        switch (pedOpc()) {
            case 1:
                System.out.println("Elegimos Configuración por defecto");
            case 2:
                System.out.println("Vas a elegir tu propia configuracion");
                FileWork.configurarProperties();
        }
    }

    public static void menuInicio() {
        System.out.println("BIENVENIDO A STARDEW VALLEY");
        System.out.println("--------------------------------");
        System.out.println("1. NUEVA PARTIDA");
        System.out.println("2. CARGAR PARTIDA");


        switch (pedOpc()) {
            case 1:
                nuevaPartida();
                FileWork.borrarFilesIniciales();
                //BORRAR TODA LA GRANJA PREVIA (EL BINARIO), EL HUERTO Y EL PROP PERSONALZIADO FUERA TMB
                //PERIMITIER QUE EL USUARIO HAGA SU PROPIO PROERTIES PREGUNTANDALO AL USUARIO
                //inicialziar l granajy  le huerto
                iniciarHuerto();
                elegirPropiedades();
                break;
            case 2:
                cargarPartida(); //SI EL FICHERO BIANRI OEOXTSE SALE LA OPCION, SINO NO
                //inicialziar l granajy  le huerto
                break;
            default:
                System.out.println("Opción incorrecta");
                break;
        }
    }

    public static void nuevaPartida() {

    }

    public static void cargarPartida() {
    }

    public static void main(String[] args) {
        Granja granja = new Granja();
        jugar();
    }
}
