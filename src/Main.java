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
                System.out.println("Salir...");
                System.exit(0);
                break;
            default:
                System.out.println("Opción incorrecta");
                break;
        }
    }

    public static void menuInicio() {
        System.out.println("BIENVENIDO A STARDEW VALLEY");
        System.out.println("--------------------------------");

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
