package spcore.view.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

public class XmlConvert {

    public static final HashMap<String, String> variables = new HashMap<>();

    public static Renderable deserialize(InputStream stream) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        var doc = builder.parse(stream);
        doc.getDocumentElement().normalize();

        return buildNode(doc.getDocumentElement());
    }

    private static Renderable buildNode(Node element) throws Exception {
        var type = element.getNodeName();
        Renderable main;
        if ("view".equals(type)) {
            main = new ViewComponent();
        } else {
            throw new Exception("NotFound Node type " + type);
        }

        for(var i = 0; i < element.getAttributes().getLength(); i++){
            var xmlAttr = element.getAttributes().item(i);
            var v = xmlAttr.getNodeValue();
            if(variables.containsKey(v)){
                v = variables.get(v);
            }
            main.getStyle()
                    .styles.put(xmlAttr.getNodeName(), v);
        }

        for(var i = 0; i < element.getChildNodes().getLength(); i++){

            var elementNode = element.getChildNodes().item(i);
            if(elementNode.getNodeType() == Node.ELEMENT_NODE){
                var renderChild = buildNode(elementNode);

                main.addChild(renderChild);
            }

        }

        return main;

    }
}
