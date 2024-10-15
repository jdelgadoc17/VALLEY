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

public class FileWork {

    public ArrayList<Semilla> cargarSemillas(String xml) {
        ArrayList<Semilla> semillas = new ArrayList<>();




        try{
            File archivo = new File(xml);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);

            NodeList listaSemillas = doc.getElementsByTagName("semilla");

            for(int i=0; i<listaSemillas.getLength(); i++){
                Node nodosemillas = listaSemillas.item(i);

                if(nodosemillas.getNodeType()==Node.ELEMENT_NODE){
                    Element elemento = (Element) nodosemillas;

                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                    TipoEstacion estacion = TipoEstacion.valueOf(elemento.getElementsByTagName("estacion").item(0).getTextContent());
                    int diasCrecimiento = Integer.parseInt(elemento.getElementsByTagName("diasCrecimiento").item(0).getTextContent());
                    double precioCompraSemilla = Double.parseDouble(elemento.getElementsByTagName("precioCompraSemilla").item(0).getTextContent());
                    double precioVentaFruto = Double.parseDouble(elemento.getElementsByTagName("precioVentaFruto").item(0).getTextContent());
                    int maxFrutos = Integer.parseInt(elemento.getElementsByTagName("maxFrutos").item(0).getTextContent());

                    Semilla semilla = new Semilla(nombre, estacion, diasCrecimiento, precioCompraSemilla, precioVentaFruto, maxFrutos);

                    semillas.add(semilla);

                }

            }



        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        return  semillas;


    }
}
