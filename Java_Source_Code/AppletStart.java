import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;

/**
 * This method is called first when the Applet is first initialised. init() called first
 * and sets up the rest of the information. It gathers the requires data and uses the Process 
 * class to prepare it for the drawing
 * @author David Hughes
 */
public class AppletStart extends JApplet {

    private ArrayList<Lecture> lectures;//stores all llectures assigned to the Student Object
    private Student student;//Student object holding information about the current student
    private String xmlFile;//xml document in the format of a string
    //holds parameters passed from webpage
    String studentParam;
    String staffParam;
    //pixel space left around the timetable edge
    private static final int X = 60;
    private static final int Y = 60;
    private JPanelGUI main;//carries out the drawing of the timetable

    @Override
    public void init() {
        studentParam = getParameter("studentid");//gets the parameters passed from the browser
        staffParam = getParameter("staff");
        
//        studentParam = "BEN07071174";//uncomment these two lines if not testing through a web browser. 
//        staffParam = "staff";//No parameters will be passed  

        setSize(new Dimension(800, 500));//sets the size of the applet
        //gives it a background colour
        Color bgcolor = new Color(0xcccccc);
        setBackground(bgcolor);

        //declares the lectures ArrayList<Lecture>
        lectures = new ArrayList<Lecture>();

        //creates new instance of SAX_Lecture
        SAX_Lecture sax = new SAX_Lecture();
        try {
            //sets xmlFile using the method getXML()
            xmlFile = getXML();
            //System.out.println(xmlFile);
        } catch (IOException ex) {
            Logger.getLogger(AppletStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Object [] returned from sax.run after parsing of the xmlFile. 
        //First position contains the ArrayLeist of lectures, 
        //the second holds the Student object
        Object[] lectStudt = sax.run(lectures, xmlFile);
        
        //sets the arrayList and the Student object to the ones returned from the SAX parser above
        lectures = (ArrayList<Lecture>) lectStudt[0];
        student = (Student) lectStudt[1];

        //new Process object which contains the methods for processing the xml data parsed
        Process process = new Process();
        process.lecturesForStudent(lectures, student);//see if there are any lectures available at any time for the student
        if (student.getAnyLectures()) {//only carry out body if the student has any lectures to display at anytime

            lectures = process.process(lectures);//process the lectures ready for drawing
            process.stringFirstUppercase(student);//sets surname to have uppercase first character
        }
      
        Container cont = getContentPane();//get the container for the applet, will be browser

        //declare the JPanel main and pass parameters X and Y which are the space to leave around border,
        //lectures - ArrayList<Lectures>, process object and student object
        main = new JPanelGUI(X, Y, lectures, process, student);
        
        //if the staffParameter passed is set to "staff" it is a member of staff using the system,
        //sets staff boolean to true or false depending on above
        if (staffParam.equalsIgnoreCase("staff")) { 
            main.setStaff(true);
        } else {
            main.setStaff(false);
        }
        
        //adds JPanelGUI main to the container
        cont.add(main);
        
    }

    @Override
    public void stop() {
    }

    //overrides the setSize method which updates the size of the applet if it is resized
    @Override
    public void setSize(int width, int height) {

        super.setSize(width, height);//sets the size
        validate();//tells container to layout its subcomponents again due to the resizing
    }

    /**
     * Takes the studentParam taken from the browser and calls generatedata.cfm which returns an xml
     * document which the applet reads through an InputStream and saves it as a String
     * @return the xmlFile is returned as a String ready for parsing
     * @throws java.io.IOException
     */
    public String getXML() throws IOException {

        //input stream which opens a stream on the generatedata.cfm with studentParam as a parameter
        InputStream in = new URL("http://localhost:8500/finalyrproject/GenerateData.cfm?studentid=" + studentParam).openStream();
        String line = "";//accumulator for reading data in line by line
        try {
            InputStreamReader inReader = new InputStreamReader(in);//reads bytes and decodes them to characters
            BufferedReader buff = new BufferedReader(inReader);//reads characters from the inputStream, more efficient

            try {
                String temp;
                while ((temp = buff.readLine()) != null) {//reads next line of the inputStream
                    line = line + temp;//appends the next line to the already read lines
                }
            } catch (IOException ex) {
                Logger.getLogger(AppletStart.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            in.close();//close the inputstream after use
        }
        return line;//returns the String line that holds the xmlFile
    }

    /**
     * Sets the print variable in main to true. then repaints.
     */
    public void setPrint() {
        if (lectures.size() > 0) {//if there are lectures to print
            main.setPrint(true);//sets print variable to true
            main.repaint();//calls repaint
            main.print();//calls print
            main.setPrint(false);//sets print back to false
            main.repaint();//calls repaint
        } else {//else there are no lectures to print
            //do nothing
            repaint();
        }
    }

} // End of AppletStart Class
