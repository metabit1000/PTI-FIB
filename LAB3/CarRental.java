import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import org.jdom2.input.sax.XMLReaders;

public class CarRental {

    /**
     * Read and parse an xml document from the file at example.xml.
     * @return the JDOM document parsed from the file.
     */
    public static Document readDocument() {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document anotherDocument = builder.build(new File("carrental.xml"));
            return anotherDocument;
        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method creates a JDOM document with elements that represent the
     * properties of a car.
     * @return a JDOM Document that represents the properties of a car.
     */
    public static Document createDocument() { //con la estructura indicada; "vacío"
        // Create the root element
        Element carElement = new Element("carrental");
        //create the document
        Document myDocument = new Document(carElement);
        
        return myDocument;
    }


	public static Element askData() {
		/* Cogemos valores por consola */
    	System.out.print("Enter car maker: ");
    	String carMaker = System.console().readLine();
		System.out.print("Enter car model: ");
		String carModel =  System.console().readLine();
		System.out.print("Enter days: ");
		String days =  System.console().readLine();
		System.out.print("Enter units:");
		String units =  System.console().readLine();
		System.out.print("Enter discount:");
		String discount =  System.console().readLine();
    	
    	/* Creamos el nuevo elemento y le ponemos los parametros introducidos por consola */
    	Element rental = new Element("rental");
		rental.setAttribute("id", String.valueOf((int)Math.floor(Math.random()*(20-1+1)+1))); //generamos id random
        rental.addContent(new Element("make").setText(carMaker));
        rental.addContent(new Element("model").setText(carModel));
        rental.addContent(new Element("nofdays").setText(days));
        rental.addContent(new Element("nofunits").setText(units));
        rental.addContent(new Element("discount").setText(discount));

    	return rental;
 	}  

    /**
     * This method accesses a child element of the root element 
     * @param myDocument a JDOM document 
     */
    public static void newRental() {
        /* Cogemos valores por consola */
        Element rental = askData();

        /* Leemos el documento y escribimos nuevo rental*/
        Document document = readDocument();
        Element root = document.getRootElement();
        root.addContent(rental); 
        document.setContent(root);
        
        /* Escribimos en el fichero "carrental.xml"*/
        writeDoc(document);
    }

    /**
     * @param myDocument a JDOM document.
     */
    public static void writeDoc(Document myDocument) {
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(myDocument, new FileWriter("carrental.xml")); //cambio que me lo saque por system.out
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method shows how to use XMLOutputter to output a JDOM document to
     * the stdout.
     * @param myDocument a JDOM document.
     */
    public static void outputDocument(Document myDocument) {
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(myDocument, System.out); 
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method shows how to use XMLOutputter to output a JDOM document to
     * a file located at myFile.xml.
     * @param myDocument a JDOM document.
     */
    public static void outputDocumentToFile(Document myDocument) {
        //setup this like outputDocument
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter();

            //output to a file
            FileWriter writer = new FileWriter("carrental.xml");
            outputter.output(myDocument, writer);
            writer.close();

        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method takes a JDOM document in memory, an XSLT file at example.xslt,
     * and outputs the results to stdout.
     * @param myDocument a JDOM document .
     */
    public static void executeXSLT(Document myDocument) {
        
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
            // Make the input sources for the XML and XSLT documents
            org.jdom2.output.DOMOutputter outputter = new org.jdom2.output.DOMOutputter();
            org.w3c.dom.Document domDocument = outputter.output(myDocument);
            javax.xml.transform.Source xmlSource = new javax.xml.transform.dom.DOMSource(domDocument);
            StreamSource xsltSource = new StreamSource(new FileInputStream("carrental.xslt"));
			//Make the output result for the finished document
            StreamResult xmlResult = new StreamResult(System.out);
			//Get a XSLT transformer
			Transformer transformer = tFactory.newTransformer(xsltSource);
			//do the transform
			transformer.transform(xmlSource, xmlResult);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
		} catch(TransformerException e) {
            e.printStackTrace();
        } catch(org.jdom2.JDOMException e) {
            e.printStackTrace();
        }
	}

    public static void validar () {

        try {

            SAXBuilder builder = new SAXBuilder (XMLReaders.XSDVALIDATING);

            Document altre = builder.build (new File ("carrental.xml"));

            System.out.println ("Root: " + altre.getRootElement ().getName ());


        }
        catch (FileNotFoundException e) {

            e.printStackTrace ();

        }

    }

    /**
     * Main method that allows the various methods to be used.
     * It takes a single command line parameter.  If none are
     * specified, or the parameter is not understood, it prints
     * its usage.
     */
    public static void main(String argv[]) {
        if(argv.length == 1) {
            String command = argv[0];
            if(command.equals("reset")) outputDocumentToFile(createDocument());
            else if(command.equals("new")) newRental();
            else if(command.equals("list")) outputDocument(readDocument());
            else if(command.equals("xslt")) executeXSLT(readDocument());
            else if(command.equals("validate")) validar ();
            else {
                System.out.println(command + " is not a valid option.");
                printUsage();
            }
        } else {
            printUsage();
        }
    }

    /**
     * Convience method to print the usage options for the class.
     */
    public static void printUsage() {
        System.out.println("Usage: Example [option] \n where option is one of the following:");
        System.out.println("  reset - Create a new empty XML document");
        System.out.println("  new - Add a new rental car introducing the data");
        System.out.println("  list - Print the carrental.xml file to the console");
        System.out.println("  xslt   - Create a new document and transform it to HTML with the XSLT stylesheet in example.xslt");
        System.out.println("  validate   - Validate the carrental.xml file against the carrental.xsd file");
    }
}
