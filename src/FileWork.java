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

    private static FileWork instanciaUnica;

    // Constructor privado para el Singleton
    private FileWork() {}

    public static FileWork getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new FileWork();
        }
        return instanciaUnica;
    }

    // Método para obtener propiedades del archivo especificado
    public Properties cargarProperties(String tipoConfig) {
        Properties properties = new Properties();
        String archivoConfig = tipoConfig.equals("default") ? "Resources/default_config.properties" : "Resources/personal_config.properties";

        try (FileInputStream input = new FileInputStream(archivoConfig)) {
            properties.load(input);
            System.out.println("Configuración " + tipoConfig + " cargada.");
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo de configuración " + tipoConfig + ": " + e.getMessage());
        }

        return properties;
    }

    public static void configurarProperties() {
        Properties properties = new Properties();

        int filas = pedFilas();
        int columnas = pedColumnas();
        int presupuestoInicial = pedirPresupuesto();
        String estacionInicial = pedirEstacion();
        int diasDuracionEstacion = pedirDiasEstacion();

        properties.setProperty("numFilas", String.valueOf(filas));
        properties.setProperty("numColumnas", String.valueOf(columnas));
        properties.setProperty("presupuestoInicial", String.valueOf(presupuestoInicial));
        properties.setProperty("estacionInicial", estacionInicial);
        properties.setProperty("diasDuracionEstacion", String.valueOf(diasDuracionEstacion));

        guardarProperties(properties, "personalizado");
    }

    // Cargar semillas desde archivo XML
    public static TreeMap<Integer, Semilla> cargarSemillas(String xml) {
        TreeMap<Integer, Semilla> semillas = new TreeMap<>();
        try {
            File archivo = new File(xml);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo);

            NodeList listaSemillas = doc.getElementsByTagName("semilla");
            for (int i = 0; i < listaSemillas.getLength(); i++) {
                Node nodoSemilla = listaSemillas.item(i);
                if (nodoSemilla.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodoSemilla;
                    int id = Integer.parseInt(elemento.getAttribute("id"));
                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();

                    // Procesar estaciones
                    String estacionesStr = elemento.getElementsByTagName("estacion").item(0).getTextContent();
                    ArrayList<TipoEstacion> estaciones = new ArrayList<>();
                    for (String estacion : estacionesStr.split("-")) {
                        estaciones.add(TipoEstacion.valueOf(estacion.trim().toUpperCase()));
                    }

                    int diasCrecimiento = Integer.parseInt(elemento.getElementsByTagName("diasCrecimiento").item(0).getTextContent());
                    double precioCompraSemilla = Double.parseDouble(elemento.getElementsByTagName("precioCompraSemilla").item(0).getTextContent());
                    double precioVentaFruto = Double.parseDouble(elemento.getElementsByTagName("precioVentaFruto").item(0).getTextContent());
                    int maxFrutos = Integer.parseInt(elemento.getElementsByTagName("maxFrutos").item(0).getTextContent());

                    Semilla semilla = new Semilla(id, nombre, estaciones, diasCrecimiento, precioCompraSemilla, precioVentaFruto, maxFrutos);
                    semillas.put(id, semilla);
                }
            }
            System.out.println("Semillas cargadas con éxito desde XML.");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Error al cargar semillas: " + e.getMessage());
        }

        return semillas;
    }

    // Guardar propiedades por defecto o personalizadas
    public static void guardarProperties(Properties properties, String tipoConfig) {
        String archivoConfig = tipoConfig.equals("default") ? "Resources/default_config.properties" : "Resources/personal_config.properties";

        File directory = new File("Resources");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileOutputStream output = new FileOutputStream(archivoConfig)) {
            properties.store(output, "Configuración " + tipoConfig + " del juego");
            System.out.println("Configuración " + tipoConfig + " guardada exitosamente en " + archivoConfig);
        } catch (IOException e) {
            System.out.println("Error al guardar configuración " + tipoConfig + ": " + e.getMessage());
        }
    }

    // Métodos auxiliares de entrada
    public static int pedFilas() {
        return pedirNumero("Introduce el número de filas (debe ser mayor que 0):");
    }

    public static int pedColumnas() {
        return pedirNumero("Introduce el número de columnas (debe ser mayor que 0):");
    }

    public static int pedirNumero(String mensaje) {
        Scanner sc = new Scanner(System.in);
        int numero;
        do {
            System.out.println(mensaje);
            numero = sc.nextInt();
            if (numero <= 0) {
                System.out.println("Error: el número debe ser mayor que 0.");
            }
        } while (numero <= 0);
        return numero;
    }

    public static String pedirEstacion() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce la estación inicial:\n1. Primavera\n2. Verano\n3. Otoño\n4. Invierno");
        switch (sc.nextInt()) {
            case 1: return "PRIMAVERA";
            case 2: return "VERANO";
            case 3: return "OTOÑO";
            case 4: return "INVIERNO";
            default: System.out.println("Selección no válida. Se seleccionará Primavera por defecto."); return "PRIMAVERA";
        }
    }

    public static int pedirPresupuesto() {
        return pedirNumero("Introduce el presupuesto inicial:");
    }

    public static int pedirDiasEstacion() {
        return pedirNumero("Introduce los días de la estación:");
    }

    // Método para borrar archivos iniciales
    public static void borrarArchivosIniciales() {
        String[] archivos = {
                "Resources/partida.bin",
                "Resources/huerto.dat",
                "Resources/default_config.properties",
                "Resources/personal_config.properties"
        };

        for (String archivo : archivos) {
            try {
                Path path = Paths.get(archivo);
                if (Files.exists(path)) Files.delete(path);
            } catch (IOException e) {
                System.out.println("No se pudo borrar " + archivo + ": " + e.getMessage());
            }
        }
    }
}
