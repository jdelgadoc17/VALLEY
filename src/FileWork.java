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



    // Instancia única (Singleton)
    protected static FileWork instanciaUnica;

    // Constructor privado para evitar la creación de múltiples instancias
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
                    Semilla semilla = new Semilla(nombre, estaciones, diasCrecimiento, precioCompraSemilla, precioVentaFruto, maxFrutos);

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
        System.out.println("Introduce el número de filas:");
        return sc.nextInt();
    }
    public static int pedColumnas() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el número de columnas:");
        return sc.nextInt();
    }

    public static int pedPresupuesto() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el presupuesto inicial:");
        return sc.nextInt();
    }

    public static String pedEstacion() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce la estación inicial:");
        return sc.nextLine();
    }

    public static int pedDiasEstacion() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce los días de la estación:");
        return sc.nextInt();
    }

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




    public static void configurarProperties() {
        System.out.println("Vamos a añadir una nueva configuración");

        int numFilas = pedFilas();
        int numColumnas = pedColumnas();
        int presupuestoInicial = pedPresupuesto();
        String estacionInicial = pedEstacion();
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



    public Properties cargarProperties() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("Resources/default_config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


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
