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
                avanzarDia();
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

    public static void menuInicio() {
        System.out.println("BIENVENIDO A STARDEW VALLEY");
        System.out.println("--------------------------------");
        System.out.println("1. NUEVA PARTIDA");
        System.out.println("2. CARGAR PARTIDA");

        switch (pedOpc()) {
            case 1:
                nuevaPartida();
                break;
            case 2:
                cargarPartida();
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
    }
}
