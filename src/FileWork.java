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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

public class FileWork {


    public TreeMap<Integer, Semilla> cargarSemillas(String xml) {
        TreeMap<Integer, Semilla> semillas = new TreeMap<>()



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
}
