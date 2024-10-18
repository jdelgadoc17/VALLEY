import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static int pedOpc() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Elige una opción:");
        return sc.nextInt();
    }

    public static void jugar(Granja granja) {
        boolean jugando = true;

        Path path = Paths.get("Resources/huerto.dat");

        while (jugando) {
            System.out.println("Elige una opción:");
            System.out.println("1. Iniciar Nuevo Día");
            System.out.println("2. Atender Cultivos");
            System.out.println("3. Plantar en Columna");
            System.out.println("4. Vender Cosecha");
            System.out.println("5. Mostrar Información de la Granja");
            System.out.println("6. Salir");


            switch (pedOpc()) {
                case 1:
                    granja.iniciarNuevoDia();
                    break;
                case 2:
                    granja.atenderCultivos(path);
                    break;
                case 3:
                    granja.plantarEnColumna();
                    break;
                case 4:
                    granja.venderCosecha();
                    break;
                case 5:
                    granja.mostrarInfo();
                    break;
                case 6:
                    jugando = false;
                    System.out.println("¡Gracias por jugar!");
                    break;
                default:
                    System.out.println("Opción incorrecta. Por favor elige una opción válida.");
            }
        }
    }




    public static void elegirPropiedades() throws IOException {
        System.out.println("¿Deseas usar la configuración por defecto o personalizar?");
        System.out.println("1. Configuración por defecto");
        System.out.println("2. Configuración personalizada");

        int opcion = pedOpc();

        if (opcion == 1) {
            System.out.println("Usando configuración por defecto...");
            FileWork.cargarPropertiesDefault();
        } else if (opcion == 2) {
            System.out.println("Personalizando configuración...");
            FileWork.configurarProperties();
        } else {
            System.out.println("Opción incorrecta. Volviendo al menú de inicio.");
            menuInicio();
        }
    }




    public static void menuInicio() throws IOException {
        System.out.println("BIENVENIDO A STARDEW VALLEY");
        System.out.println("--------------------------------");
        System.out.println("1. NUEVA PARTIDA");
        System.out.println("2. CARGAR PARTIDA");

        switch (pedOpc()) {
            case 1:
                nuevaPartida();
                break;
            case 2:
                Path path = Paths.get("Resources/huerto.dat");
                if (Files.exists(path)) {
                    cargarPartida();  // Cargar la partida si existe el archivo
                } else {
                    System.out.println("No se encontró ninguna partida guardada.");
                    nuevaPartida();
                }
                break;
            default:
                System.out.println("Opción incorrecta. Por favor elige una opción válida.");
                break;
        }
    }


    public static void nuevaPartida() throws IOException {
        FileWork.borrarFilesIniciales();

        Path path = Paths.get("Resources/huerto.dat");

        elegirPropiedades();
        FileWork fileWork = FileWork.getInstancia();
        Properties properties = fileWork.cargarProperties();
        int diaActual = 1;
        TipoEstacion tipoEstacion = TipoEstacion.valueOf(properties.getProperty("estacionInicial").toUpperCase());
        double presupuesto = Double.parseDouble(properties.getProperty("presupuestoInicial"));

        Granja granja = new Granja(diaActual, tipoEstacion, presupuesto, new Tienda(), new Almacen());

        granja.crearHuerto(path);

        jugar(granja);

    }

    public static void cargarPartida() {
        Path path = Paths.get("Resources/huerto.dat");

        if (Files.exists(path)) {
            try {
                FileInputStream file = new FileInputStream(path.toFile());
                ObjectInputStream objectInput = new ObjectInputStream(file);
                Granja granja = (Granja) objectInput.readObject();
                objectInput.close();
                file.close();

                System.out.println("Partida cargada correctamente.");

                jugar(granja);

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar la partida: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró ningún archivo de huerto.");
            menuInicio();  // Volver al menú de inicio
        }
    }



    public static void main(String[] args) {

        try{
            menuInicio();

        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
