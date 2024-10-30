import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static int pedOpc() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Elige una opción:");
        return sc.nextInt();
    }

    public static int pedColumna() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Elige una columna donde plantar:");
        return sc.nextInt();
    }

    /*
    Flow del juego
     */

    public static void jugar(Granja granja) {
        boolean jugando = true;
        Path path = Paths.get("Resources/archivoHuerto.dat");

        while (jugando) {
            int opcion;
            do {
                System.out.println("Elige una opción:");
                System.out.println("1. Iniciar Nuevo Día");
                System.out.println("2. Atender Cultivos");
                System.out.println("3. Plantar en Columna");
                System.out.println("4. Vender Cosecha");
                System.out.println("5. Mostrar Información de la Granja");
                System.out.println("6. Salir");

                opcion = pedOpc();
                if (opcion < 1 || opcion > 6) {
                    System.out.println("Opción incorrecta. Por favor elige una opción válida.");
                }
            } while (opcion < 1 || opcion > 6);

            switch (opcion) {
                case 1 -> granja.iniciarNuevoDia();
                case 2 -> granja.atenderCultivos(path);
                case 3 -> {
                    int col = pedColumna();
                    granja.plantarEnColumna(col);
                }
                case 4 -> granja.venderFruta();
                case 5 -> {
                    granja.mostrarInfo();
                    granja.mostrarHuerto();
                }
                case 6 -> {
                    Granja.guardarPartida(granja);
                    jugando = false;
                    System.out.println("Gracias por jugar!");
                }
            }
        }
    }

    /*
    Trabajamos las propiedades según la elección dada
     */


    public static String elegirPropiedades() throws IOException {
        System.out.println("¿Deseas usar la configuración por defecto o personalizar?");
        System.out.println("1. Configuración por defecto");
        System.out.println("2. Configuración personalizada");

        int opcion = pedOpc();

        if (opcion == 1) {
            System.out.println("Usando configuración por defecto...");
            Properties properties = new Properties();
            properties.setProperty("numFilas", "4");
            properties.setProperty("numColumnas", "4");
            properties.setProperty("presupuestoInicial", "1000");
            properties.setProperty("estacionInicial", "PRIMAVERA");
            properties.setProperty("diasDuracionEstacion", "30");
            FileWork.guardarProperties(properties, "default");
            return "default";
        } else if (opcion == 2) {
            System.out.println("Personalizando configuración...");
            FileWork.configurarProperties();
            return "personalizado";
        } else {
            System.out.println("Opción incorrecta. Usando configuración por defecto.");
            return "default";
        }
    }

    /*
    Menu inicial para comenzar
     */

    public static void menuInicio() throws IOException {
        System.out.println("BIENVENIDO A STARDEW VALLEY");
        System.out.println("--------------------------------");

        int opcion;
        do {
            System.out.println("1. NUEVA PARTIDA");
            System.out.println("2. CARGAR PARTIDA");

            opcion = pedOpc();

            switch (opcion) {
                case 1 -> nuevaPartida();
                case 2 -> {
                    Path path = Paths.get("Resources/partida.bin");
                    if (Files.exists(path)) {
                        cargarPartida();
                    } else {
                        System.out.println("No se encontró ninguna partida guardada.");
                        nuevaPartida();
                    }
                }
                default -> System.out.println("Opción incorrecta. Por favor elige una opción válida.");
            }
        } while (opcion != 1 && opcion != 2);
    }


    /*
    Opcion de nueva partida
     */
    public static void nuevaPartida() throws IOException {
        FileWork.borrarArchivosIniciales();
        String tipoConfig = elegirPropiedades();
        FileWork fileWork = FileWork.getInstancia();
        Properties properties = fileWork.cargarProperties(tipoConfig);
        int diaActual = 1;
        TipoEstacion tipoEstacion = TipoEstacion.valueOf(properties.getProperty("estacionInicial").toUpperCase());
        double presupuesto = Double.parseDouble(properties.getProperty("presupuestoInicial"));

        Granja granja = new Granja(diaActual, tipoEstacion, presupuesto, new Tienda(), new Almacen(), tipoConfig);
        granja.crearHuerto(Paths.get("Resources/archivoHuerto.dat"));


        Establo establo = new Establo();
        GestionDB gestion = GestionDB.getInstance();
        ArrayList<Animal> animales = gestion.getListaAnimales();
        for(Animal animal: animales){
            establo.agregarAnimal(animal);

        }


        jugar(granja);
    }

    /*
    Opcion de cargar partida
     */

    public static void cargarPartida() throws IOException {
        Path path = Paths.get("Resources/partida.bin");

        if (Files.exists(path)) {
            try (ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                Granja granja = (Granja) objectInput.readObject();
                System.out.println("Partida cargada correctamente.");
                jugar(granja);
                jugar(establo);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar la partida: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró ningún archivo de huerto.");
            nuevaPartida();
        }
    }

    /*********************************************************************************/

    public static void main(String[] args) {
        try {
            menuInicio(); //MAIN FLOW GENERAL DEL JUEGO
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
