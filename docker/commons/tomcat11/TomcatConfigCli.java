package it.link;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import java.util.HashMap;
import java.util.Map;

public class TomcatConfigCli {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Errore: Specificare il percorso del file di input.");
            return;
        }
        String inputFilePath = args[0];
        modifyTomcatConfig(inputFilePath);
    }

    // Funzione principale
    public static void modifyTomcatConfig(String inputFilePath) {
        // Ottenere il percorso di CATALINA_HOME
        String catalinaHome = System.getenv("CATALINA_HOME");
        if (catalinaHome == null || catalinaHome.isEmpty()) {
            System.out.println("Errore: La variabile d'ambiente CATALINA_HOME non è impostata.");
            return;
        }

        // Costruire il percorso ai file di configurazione
        String serverXmlPath = catalinaHome + File.separator + "conf" + File.separator + "server.xml";
        String contextXmlPath = catalinaHome + File.separator + "conf" + File.separator + "context.xml";

        try {
            // Carica i documenti XML
            Document documentServerXml = loadXMLDocument(serverXmlPath);
            Document documentContextXml = loadXMLDocument(contextXmlPath);

            // Leggi le direttive dal file di input e applicale
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilePath))) {
                String line;
                StringBuilder currentDirective = new StringBuilder();
                
                while ((line = bufferedReader.readLine()) != null) {
                    line = line.trim();
                    
                    // Salta le linee vuote e i commenti
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    
                    // Controlla se la linea termina con backslash
                    if (line.endsWith("\\")) {
                        // Rimuovi il backslash e aggiungi la linea alla direttiva corrente
                        currentDirective.append(line.substring(0, line.length() - 1));
                    } else {
                        // Aggiungi la linea corrente e processa la direttiva completa
                        currentDirective.append(line);
                        
                        // Processa la direttiva
                        processDirective(documentServerXml, documentContextXml, currentDirective.toString());
                        
                        // Resetta per la prossima direttiva
                        currentDirective.setLength(0);
                    }
                }
                
                // Se c'è una direttiva incompleta alla fine del file, processala
                if (currentDirective.length() > 0) {
                    processDirective(documentServerXml, documentContextXml, currentDirective.toString());
                }
            }

            // Salva i documenti modificati
            saveXMLDocument(documentServerXml, serverXmlPath);
            saveXMLDocument(documentContextXml, contextXmlPath);

            System.out.println("Configurazione modificata con successo.");
        } catch (Exception e) {
            System.out.println("Errore durante la modifica della configurazione: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Carica il documento XML
    private static Document loadXMLDocument(String filePath) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(filePath));
    }

    // Salva il documento XML
    private static void saveXMLDocument(Document document, String filePath) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    // Processa ogni direttiva
    private static void processDirective(Document documentServerXml, Document documentContextXml, String directive) throws XPathExpressionException {
        String[] parts = directive.split(":", 2);
        if (parts.length != 2) {
            System.out.println("Direttiva invalida: " + directive);
            return;
        }

        String xpath = parts[0].trim();
        System.out.println("XPath: " + xpath);
        
        int idx = directive.indexOf(':');
        String remaining = directive.substring(idx + 1).trim();
        
        String[] operationAndParams = remaining.split("\\s+", 2);
        String operation = operationAndParams[0].trim();
        System.out.println("- Operazione: " + operation);
        
        String onlyParams = "";
        if (operationAndParams.length > 1) {
            onlyParams = operationAndParams[1];
        }
        
        Map<String, String> params = new HashMap<>();
        if (!onlyParams.isEmpty()) {
            String[] paramPairs = onlyParams.split(",");
            for (int i = 0; i < paramPairs.length; i++) {
                String paramPair = paramPairs[i].trim();
                int equalIndex = paramPair.indexOf('=');
                
                if (equalIndex == -1) {
                    System.out.println("- Parametro[" + i + "] = " + paramPair);
                    params.put(String.valueOf(i), paramPair);
                } else {
                    String key = paramPair.substring(0, equalIndex).trim();
                    String value = paramPair.substring(equalIndex + 1).trim();
                    System.out.println("- Parametro[" + key + "] = " + value);
                    params.put(key, value);
                }
            }
        }

        Document document = (xpath.startsWith("/Context")) ? documentContextXml : documentServerXml;

        switch (operation) {
            case "add":
            case "append":
                addElement(document, xpath, params);
                break;
            case "top":
                addElementAtTop(document, xpath, params);
                break;
            case "delete":
                deleteElement(document, xpath);
                break;
            case "write-attribute":
                writeAttribute(document, xpath, params);
                break;
            case "read-attribute":
                readAttribute(document, xpath, params);
                break;
            case "delete-attribute":
                deleteAttribute(document, xpath, params);
                break;
            default:
                System.out.println("Operazione non supportata: " + operation);
        }
    }

    // Aggiungi un nuovo elemento come ultimo figlio
    private static void addElement(Document document, String xpath, Map<String, String> attributes) throws XPathExpressionException {
        int lastSlashIndex = xpath.lastIndexOf('/');
        String parentXpath = xpath.substring(0, lastSlashIndex);
        String elementName = xpath.substring(lastSlashIndex + 1);

        Node parent = getElementByXPath(document, parentXpath);
        if (parent == null) {
            System.out.println("XPath non trovato: " + parentXpath);
            return;
        }

        Element newElement = document.createElement(elementName);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            newElement.setAttribute(entry.getKey(), entry.getValue());
        }
        parent.appendChild(newElement);
    }

    // Aggiungi un nuovo elemento come primo figlio
    private static void addElementAtTop(Document document, String xpath, Map<String, String> attributes) throws XPathExpressionException {
        int lastSlashIndex = xpath.lastIndexOf('/');
        String parentXpath = xpath.substring(0, lastSlashIndex);
        String elementName = xpath.substring(lastSlashIndex + 1);

        Node parent = getElementByXPath(document, parentXpath);
        if (parent == null) {
            System.out.println("XPath non trovato: " + parentXpath);
            return;
        }

        Element newElement = document.createElement(elementName);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            newElement.setAttribute(entry.getKey(), entry.getValue());
        }

        // Inserisce il nuovo elemento come primo figlio
        Node firstChild = parent.getFirstChild();
        parent.insertBefore(newElement, firstChild);
    }

    // Elimina un elemento
    private static void deleteElement(Document document, String xpath) throws XPathExpressionException {
        Node element = getElementByXPath(document, xpath);
        if (element != null) {
            element.getParentNode().removeChild(element);
        } else {
            System.out.println("XPath non trovato: " + xpath);
        }
    }

    // Scrivi attributi a un elemento
    private static void writeAttribute(Document document, String xpath, Map<String, String> attributes) throws XPathExpressionException {
        Node element = getElementByXPath(document, xpath);
        if (element == null) {
            System.out.println("XPath non trovato: " + xpath);
            return;
        }

        Element elementNode = (Element) element;
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            elementNode.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    // Leggi attributi da un elemento
    private static void readAttribute(Document document, String xpath, Map<String, String> attributes) throws XPathExpressionException {
        Node element = getElementByXPath(document, xpath);
        if (element == null) {
            System.out.println("XPath non trovato: " + xpath);
            return;
        }

        Element elementNode = (Element) element;
        for (String key : attributes.values()) {
            System.out.println(key + " = " + elementNode.getAttribute(key));
        }
    }

    // Elimina attributi da un elemento
    private static void deleteAttribute(Document document, String xpath, Map<String, String> attributes) throws XPathExpressionException {
        Node element = getElementByXPath(document, xpath);
        if (element == null) {
            System.out.println("XPath non trovato: " + xpath);
            return;
        }

        Element elementNode = (Element) element;
        for (String key : attributes.values()) {
            if (elementNode.hasAttribute(key)) {
                elementNode.removeAttribute(key);
            }
        }
    }

    // Helper per ottenere un elemento tramite XPath
    private static Node getElementByXPath(Document document, String xpath) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr = xPath.compile(xpath);
        return (Node) expr.evaluate(document, XPathConstants.NODE);
    }
}