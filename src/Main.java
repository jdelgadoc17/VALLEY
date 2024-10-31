import Model.*;

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

        while (jugando) {
            int opcion;
            do {
                System.out.println("Elige una opción:");
                System.out.println("1. Iniciar Nuevo Día");
                System.out.println("2. Huerto");
                System.out.println("3. Model.Establo");
                System.out.println("4. Salir");

                opcion = pedOpc();
                if (opcion < 1 || opcion > 4) {
                    System.out.println("Opción incorrecta. Por favor elige una opción válida.");
                }
            } while (opcion < 1 || opcion > 4);

            switch (opcion) {
                case 1 -> granja.iniciarNuevoDia();
                case 2 -> mostrarMenuHuerto(granja);
                case 3 -> jugarEstablo(granja); // Solo necesitamos pasar `granja`
                case 4 -> {
                    jugando = false;
                    System.out.println("Gracias por jugar!");
                }
            }
        }
    }

    public static void mostrarMenuHuerto(Granja granja) {
        Path path = Paths.get("Resources/archivoHuerto.dat");

        boolean enHuerto = true;
        while (enHuerto) {
            int opcion;
            do {
                System.out.println("Menú de Huerto:");
                System.out.println("1. Atender Cultivos");
                System.out.println("2. Plantar en Columna");
                System.out.println("3. Vender Cosecha");
                System.out.println("4. Mostrar Información del Huerto");
                System.out.println("5. Volver al Menú Principal");

                opcion = pedOpc();
                if (opcion < 1 || opcion > 5) {
                    System.out.println("Opción incorrecta. Por favor elige una opción válida.");
                }
            } while (opcion < 1 || opcion > 5);

            switch (opcion) {
                case 1 -> granja.atenderCultivos(path);
                case 2 -> {
                    int col = pedColumna();
                    granja.plantarEnColumna(col);
                }
                case 3 -> granja.venderFruta();
                case 4 -> {
                    granja.mostrarInfo();
                    granja.mostrarHuerto();
                }
                case 5 -> enHuerto = false;
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

        GestionDB gestion = GestionDB.getInstance();
        ArrayList<Animal> animales = gestion.getListaAnimales();
        granja.getEstablo().setAnimales(animales);

        jugar(granja);
    }

    /*
    Opcion de cargar partida
     */
    public static void cargarPartida() throws IOException {
        Path pathGranja = Paths.get("Resources/partidaGranja.bin");

        Granja granja = null;

        if (Files.exists(pathGranja)) {
            try (ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(pathGranja.toFile()))) {
                granja = (Granja) objectInput.readObject();
                System.out.println("Partida de la granja cargada correctamente.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar la partida de la granja: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró ninguna partida guardada de la granja.");
            nuevaPartida();
        }

        if (granja != null) {
            jugar(granja);
        }
    }

    public static void jugarEstablo(Granja granja) {
        GestionDB gestionDB = GestionDB.getInstance();
        Scanner sc = new Scanner(System.in);
        int opc;

        do {
            System.out.println("Menú del Model.Establo:");
            System.out.println("1. Producir");
            System.out.println("2. Alimentar");
            System.out.println("3. Vender Productos");
            System.out.println("4. Rellenar Comedero");
            System.out.println("5. Mostrar Estado del Model.Establo");
            System.out.println("6. Volver al Menú");

            opc = sc.nextInt();
            switch (opc) {
                case 1 -> granja.producir(granja.getDiaActual(), granja.getTipoEstacion());
                case 2 -> granja.alimentar();
                case 3 -> gestionDB.venderProductos(granja);
                case 4 -> gestionDB.rellenarComedero(granja);
                case 5 -> {
                    granja.getEstablo().mostrarAnimales();
                    gestionDB.mostrarProductos();
                    gestionDB.mostrarAlimentos();
                }
                case 6 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opc != 6);
    }

    /*********************************************************************************/

    public static void main(String[] args) {
        try {
            menuInicio();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}