import com.sun.source.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

public class FileWork implements Serializable {



    protected static FileWork instanciaUnica;

    private FileWork() {
    }

    /**
     * Obtiene la instancia unica (Singleton)
     */
    public static FileWork getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new FileWork();
        }
        return instanciaUnica;
    }

    public static Properties getProperties() {
        Properties properties = new Properties();

        try {
            if (Files.exists(Paths.get("Resources/default_config.properties"))) {
                try (FileInputStream input = new FileInputStream("Resources/default_config.properties")) {
                    properties.load(input);
                    System.out.println("Configuración por defecto cargada.");
                }
            } else if (Files.exists(Paths.get("Resources/personal_config.properties"))) {
                try (FileInputStream input = new FileInputStream("Resources/personal_config.properties")) {
                    properties.load(input);
                    System.out.println("Configuración personalizada cargada.");
                }
            } else {
                System.out.println("No se encontraron archivos de configuración.");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar propiedades: " + e.getMessage());
            e.printStackTrace();
        }

        return properties;
    }




    /*
     * Carga las propiedades del archivo de propiedades del xml
     */

    public static TreeMap<Integer, Semilla> cargarSemillas(String xml) {

        TreeMap<Integer, Semilla> semillas = new TreeMap<>();

        // CARGAMOS LAS SEMILLAS DEL XML EN UNA LISTA A DEVOLVER
        try {
            // PREPARAMOS EL XML
            File archivo = new File(xml);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo);


            // SE OBTIENEN TODOS LOS NODOS SEMILLA DEL XML
            NodeList listaSemillas = doc.getElementsByTagName("semilla");

            for (int i = 0; i < listaSemillas.getLength(); i++) {
                Node nodoSemilla = listaSemillas.item(i); // CREAMOS UN NODO EN CADA ''OBJETO'' SEMILLA DEL XML

                if (nodoSemilla.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoSemilla; // CREAMOS UN ELEMENTO DE CADA SEMILLA SEGÚN EL TIPO DE VALOR
                    int id = Integer.parseInt(elemento.getAttribute("id"));

                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();

                    // Procesar el atributo estacion para manejar múltiples estaciones
                    String estacionesStr = elemento.getElementsByTagName("estacion").item(0).getTextContent();
                    String[] estacionesArray = estacionesStr.split("-"); // Separar estaciones usando "-"
                    ArrayList<TipoEstacion> estaciones = new ArrayList<>();

                    for (String estacion : estacionesArray) {
                        estaciones.add(TipoEstacion.valueOf(estacion.trim().toUpperCase()));
                    }

                    int diasCrecimiento = Integer.parseInt(elemento.getElementsByTagName("diasCrecimiento").item(0).getTextContent());
                    double precioCompraSemilla = Double.parseDouble(elemento.getElementsByTagName("precioCompraSemilla").item(0).getTextContent());
                    double precioVentaFruto = Double.parseDouble(elemento.getElementsByTagName("precioVentaFruto").item(0).getTextContent());
                    int maxFrutos = Integer.parseInt(elemento.getElementsByTagName("maxFrutos").item(0).getTextContent());

                    // CREAMOS LA SEMILLA CON MÚLTIPLES ESTACIONES
                    Semilla semilla = new Semilla(id, nombre, estaciones, diasCrecimiento, precioCompraSemilla, precioVentaFruto, maxFrutos);

                    // AÑADIMOS LA SEMILLA A LA LISTA

                    semillas.put(id, semilla);
                }
            }
            System.out.println("SEMILLAS CARGADAS CON EXITO");

        } catch (ParserConfigurationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return semillas;
    }








    public static int pedFilas() {
        Scanner sc = new Scanner(System.in);
        int filas;

        do {
            System.out.println("Introduce el número de filas (debe ser mayor que 0):");
            filas = sc.nextInt();
            if (filas <= 0) {
                System.out.println("Error: el número de filas no puede ser 0 o negativo.");
            }
        } while (filas <= 0);
        return filas;
    }

    public static int pedColumnas() {
        Scanner sc = new Scanner(System.in);
        int columnas;
        do {
            System.out.println("Introduce el número de columnas (debe ser mayor que 0):");
            columnas = sc.nextInt();
            if (columnas <= 0) {
                System.out.println("Error: el número de columnas no puede ser 0 o negativo.");
            }
        } while (columnas <= 0);

        return columnas;
    }


    public static int pedPresupuesto() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el presupuesto inicial:");
        return sc.nextInt();
    }

    public static String pedEstacion() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Introduce la estación inicial:");
        System.out.println("1. Primavera");
        System.out.println("2. Verano");
        System.out.println("3. Otoño");
        System.out.println("4. Invierno");

        int opcion = sc.nextInt();
        String estacionSeleccionada = "";

        switch (opcion) {
            case 1:
                estacionSeleccionada = "Primavera";
                break;
            case 2:
                estacionSeleccionada = "Verano";
                break;
            case 3:
                estacionSeleccionada = "Otoño";
                break;
            case 4:
                estacionSeleccionada = "Invierno";
                break;
            default:
                System.out.println("Opción no válida. Se seleccionará Primavera por defecto.");
                estacionSeleccionada = "Primavera";
                break;
        }

        return estacionSeleccionada;
    }


    public static int pedDiasEstacion() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce los días de la estación:");
        return sc.nextInt();
    }

    /*
     * Carga las propiedades  ya existentes por defecto
     */
    public static void cargarPropertiesDefault() {
        Properties properties = new Properties(); //SACAR LOS DATOS DEL ARCHIVO QUE YA EXISTE

        properties.setProperty("numFilas", "4");  // Configuración por defecto
        properties.setProperty("numColumnas", "4");
        properties.setProperty("presupuestoInicial", "1000");
        properties.setProperty("estacionInicial", "primavera");
        properties.setProperty("diasDuracionEstacion", "30");

        // Crear el directorio si no existe
        File directory = new File("Resources");
        if (!directory.exists()) {
            directory.mkdirs();  // Crear directorios intermedios si no existen

        }

        try (FileOutputStream output = new FileOutputStream("Resources/default_config.properties")) {
            properties.store(output, "Configuración por defecto del juego");
            System.out.println("Configuración por defecto guardada exitosamente en 'default_config.properties'.");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo de configuración: " + e.getMessage());
            e.printStackTrace();
        }
    }



    /*
     * Configurar las propiedades personalizadas
     */
    public static void configurarProperties() {
        System.out.println("Vamos a añadir una nueva configuración");

        int numFilas = pedFilas();
        int numColumnas = pedColumnas();
        int presupuestoInicial = pedPresupuesto();
        String estacionInicial = pedEstacion().toUpperCase();
        int diasDuracionEstacion = pedDiasEstacion();

        Properties properties = new Properties();
        properties.setProperty("numFilas", String.valueOf(numFilas));
        properties.setProperty("numColumnas", String.valueOf(numColumnas));
        properties.setProperty("presupuestoInicial", String.valueOf(presupuestoInicial));
        properties.setProperty("estacionInicial", estacionInicial);
        properties.setProperty("diasDuracionEstacion", String.valueOf(diasDuracionEstacion));

        File directory = new File("Resources");
        if (!directory.exists()) {
            directory.mkdir();  // Crear la carpeta si no existe
        }
        try (FileOutputStream output = new FileOutputStream("Resources/personal_config.properties")) {
            properties.store(output, "Configuración del Juego");
            System.out.println("Configuración personalizada guardada exitosamente en 'personal_config.properties'.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * Carga las propiedades desde el archivo de configuración
     */

    public Properties cargarProperties() {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("Resources/default_config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo de configuración por defecto: " + e.getMessage());
        }

        try (FileInputStream input = new FileInputStream("Resources/personal_config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo de configuración personalizado: " + e.getMessage());
        }

        return properties;
    }



    /*
     * Borra los archivos de inicio de la partida (stardam_valley.bin, huerto.dat, default_config.properties y personal_config.properties)
     */

    public static void borrarFilesIniciales() {
        Path path1 = Paths.get("VALLEY/Resources/stardam_valley.bin");
        Path path2 = Paths.get("VALLEY/Resources/huerto.dat");
        Path path3 = Paths.get("VALLEY/Resources/default_config.properties");
        Path path4 = Paths.get("VALLEY/Resources/personal_config.properties");


        try {
            if (Files.exists(path1)) {
                Files.delete(path1);
            }
            if (Files.exists(path2)) {
                Files.delete(path2);
            }
            if (Files.exists(path3)) {
                Files.delete(path3);
            }
            if (Files.exists(path4)) {
                Files.delete(path4);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
