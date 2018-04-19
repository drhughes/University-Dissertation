import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX_Lecture class contains the methods to parse an XML document containing
 * lecture information read from generateData.cfm. An ArrayList of Lecture objects 
 * is created from the XML document
 * @author David Hughes
 */
public class SAX_Lecture extends DefaultHandler {

    //lecturesList will hold all the lecture objects
    private ArrayList<Lecture> lecturesList;
    private Student student;//holds the student object when it is created and populated with data found during parsing
    private String tempVal;//accumulator for whitespace in XML
    private Lecture tempLecture; //holds a Lecture while it is being processed


    /**
     * Instantiates a SAX_Lecture object
     */
    public SAX_Lecture() {
    }

    /**
     * Calls parseDocument and passes the String xmlFile as a parameter. While parsing lecture objects are
     * created and added to the lectures ArrayList. run returns an Object[] containing everything that has
     * been created from the parsing of xmlFile
     * @param lectures The ArrayList<Lectures> where the lecture object will be stored
     * @param xmlFile XML file in the form of a String, read from Server
     * @return lectStudt Object[] containing two elements. A student object and ArrayList of lecture objects
     */
    public Object[] run(ArrayList<Lecture> lectures, String xmlFile) {
        this.lecturesList = lectures;
        parseDocument(xmlFile);
        Object[] lectStudt = {lecturesList, student};
        return lectStudt;
    }

    /**
     * 
     * @param xmlFile
     */
    private void parseDocument(String xmlFile) {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse(new InputSource(new StringReader(xmlFile)), this);

        } catch (FileNotFoundException fe) {//thrown when an error has occured with locating the xmlFile
            System.err.println("ERROR: The xmlFile containing the lecture information cannot be found!");
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        tempVal = "";//resets tempVal to empty
        if (qName.equalsIgnoreCase("Student")) {
            //create a new instance of a Student
            student = new Student();
            student.setStudentID(attributes.getValue("student_id"));//set Student variables with parsed attribute data
            student.setFirstName(attributes.getValue("firstname"));
            student.setLastName(attributes.getValue("lastname"));
        } else if (qName.equalsIgnoreCase("Lecture")) {
            //create a new instance of a Lecture
            tempLecture = new Lecture();
            tempLecture.setTitle(attributes.getValue("title"));//set Lecture variables with parsed attribute data
            tempLecture.setWeeks(attributes.getValue("weeks"));
        }
    }

    // When the parser encounters plain text (not XML elements), it calls
    // this method, which accumulates them in a string buffer.
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        //If a closing tag in the XML is hit then the IF statement decides what to do with the data read
        if (qName.equalsIgnoreCase("Lecture")) {
            //add temporary version of Lecture to the lecturesList
            lecturesList.add(tempLecture);

        } else if (qName.equalsIgnoreCase("DOW")) {
            tempLecture.setDay_of_week(Integer.parseInt(tempVal));
        } else if (qName.equalsIgnoreCase("Start")) {
            tempLecture.setStart(tempVal);
        } else if (qName.equalsIgnoreCase("End")) {
            tempLecture.setEnd(tempVal);
        } else if (qName.equalsIgnoreCase("Room")) {
            tempLecture.setRoom(tempVal);
        } else if (qName.equalsIgnoreCase("Staff")) {
            tempLecture.setStaff(tempVal);
        }


    }
} // End of SAX_Lecture Class