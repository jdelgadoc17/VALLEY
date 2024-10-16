import com.sun.source.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

public class FileWork {


    public TreeMap<Integer, Semilla> cargarSemillas(String xml) {
        TreeMap<Integer, Semilla> semillas = new TreeMap<>();



        //CARGAMOS LAS SEMILLAS DEL XML EN UNA LISTA A DEVOLVER
        try{

            //PREPARAMOS EL XML
            File archivo = new File(xml);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);

            //SE OBTIENEN TODOS LOS NODOS SEMILLA DEL XML
            NodeList listaSemillas = doc.getElementsByTagName("semilla");

            for(int i=0; i<listaSemillas.getLength(); i++){
                Node nodosemillas = listaSemillas.item(i); //CREAMOS UN NODO EN CADA ''OBJETO'' SEMILLA DEL XML

                if(nodosemillas.getNodeType()==Node.ELEMENT_NODE){
                    Element elemento = (Element) nodosemillas; //CREAMOS UN ELEMENTO DE CADA SEMILLA SEGUN EL TIPO DE VALOR
                    int id = Integer.parseInt(elemento.getAttribute("id"));

                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                    TipoEstacion estacion = TipoEstacion.valueOf(elemento.getElementsByTagName("estacion").item(0).getTextContent());
                    int diasCrecimiento = Integer.parseInt(elemento.getElementsByTagName("diasCrecimiento").item(0).getTextContent());
                    double precioCompraSemilla = Double.parseDouble(elemento.getElementsByTagName("precioCompraSemilla").item(0).getTextContent());
                    double precioVentaFruto = Double.parseDouble(elemento.getElementsByTagName("precioVentaFruto").item(0).getTextContent());
                    int maxFrutos = Integer.parseInt(elemento.getElementsByTagName("maxFrutos").item(0).getTextContent());

                    //CREAMOS LA SEMILLA
                    Semilla semilla = new Semilla(nombre, estacion, diasCrecimiento, precioCompraSemilla, precioVentaFruto, maxFrutos);
                    //AÑADIMOS LA SEMILLA, OBJETO, A LA LISTA
                    semillas.put(id, semilla);

                }

            }



        } catch (ParserConfigurationException e) {
            System.out.println("Error: "+e.getMessage());
        } catch (SAXException e) {
            System.out.println("Error: "+e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: "+e.getMessage());
        }

        return  semillas;


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

    public static void configurarProperties(){
        System.out.println("Vamos a añadir una nueva configuración");

        int numFilas = pedFilas();
        int numColumnas = pedColumnas();
        int presupuestoInicial = pedPresupuesto();
        String estacionicial = pedEstacion();
        int diasduracionestacion = pedDiasEstacion();

        Properties properties = new Properties();
        properties.setProperty("numFilas", String.valueOf(numFilas));
        properties.setProperty("numColumnas", String.valueOf(numColumnas));
        properties.setProperty("presupuestoInicial", String.valueOf(presupuestoInicial));
        properties.setProperty("estacionInicial", String.valueOf(estacionicial));
        properties.setProperty("diasDuracionEstacion", String.valueOf(diasduracionestacion));

        File directory = new File("Resources");
        if (!directory.exists()) {
            directory.mkdir();
        }
        try (FileOutputStream output = new FileOutputStream("Resources/config.properties")) {
            properties.store(output, "Configuración del Juego");
            System.out.println("Configuración guardada exitosamente en 'config.properties'.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void borrarFilesIniciales() {
        Path path1 = Paths.get("Resources/stardam_valley.bin");
        Path path2 = Paths.get("Resources/huerto.dat");
        Path path3 = Paths.get("Resources/config.properties");

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
