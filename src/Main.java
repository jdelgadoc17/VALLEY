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




    public static void elegirPropiedades() {
        System.out.println("¿Deseas usar la configuración por defecto o personalizar?");
        System.out.println("1. Configuración por defecto");
        System.out.println("2. Configuración personalizada");

        switch (pedOpc()) {
            case 1:
                System.out.println("Usando configuración por defecto...");
                FileWork.cargarPropertiesDefault();
                break;
            case 2:
                System.out.println("Personalizando configuración...");
                FileWork.configurarProperties();
                break;
            default:
                System.out.println("Opción incorrecta. Usando configuración por defecto.");
                FileWork.cargarPropertiesDefault();
                break;
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
                    menuInicio();  // Volver al menú de inicio
                }
                break;
            default:
                System.out.println("Opción incorrecta. Por favor elige una opción válida.");
                menuInicio(); // Repetir el menú si se elige una opción incorrecta
                break;
        }
    }


    public static void nuevaPartida() throws IOException {
        elegirPropiedades();

        FileWork fileWork = FileWork.getInstancia();
        Properties properties = fileWork.cargarProperties();
        int diaActual = 1;
        TipoEstacion tipoEstacion = TipoEstacion.valueOf(properties.getProperty("estacionInicial").toUpperCase());
        double presupuesto = Double.parseDouble(properties.getProperty("presupuestoInicial"));

        Granja granja = new Granja(diaActual, tipoEstacion, presupuesto, new Tienda(), new Almacen());

        FileWork.borrarFilesIniciales();
        granja.iniciarHuerto();

        jugar(granja);

    }

    public static void cargarPartida() {
        Path path = Paths.get("Resources/huerto.dat");

        try {
            FileInputStream file = new FileInputStream(path.toFile());
            ObjectInputStream objectInput = new ObjectInputStream(file);
            Granja granja = (Granja) objectInput.readObject();
            objectInput.close();
            file.close();

            System.out.println("Partida cargada correctamente.");
            jugar(granja);

        } catch (IOException e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada al cargar la partida: " + e.getMessage());
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
