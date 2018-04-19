
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.print.PrinterJob;
import javax.swing.Timer;

/**
 * JPanelGUI contains all the methods that generate the visual timetable based on the information
 * gathered from the parsing of the xml document. 
 * @author David Hughes
 */
public class JPanelGUI extends JPanel implements MouseMotionListener, Printable, ActionListener {

    ArrayList<Lecture> lectures;//Stores the Lecture objects
    private Process process;//stores an instance of the Process class
    private Student studentObject;//holds the Student object
   
    //------------------- JPanel as a whole -----------   
    private int X,  Y;//space to leave from the edge of the X and Y axis
    private double width,  height;//width and height of this JPanel in pixels
    private double across,  down;//across - distance between the grid lines going down
                                 //down - distance beween the grid lines going across
    int fontSize = getFontSize();//method gets font size depending on current screen size
    
    //------------------- labelling days -------------------
    private int first,  second,  third,  fourth,  fifth;//used for labelling days    
   
    //-------------------------- variables used for when mouse is moved ---------------------------
    private double mouseOverW,  mouseOverH;//stores rect height and width of the infobox when mouseover 

    //----------------------------------- draw lectures ---------------------------------------------------------
    private double pixelsDownGrid;//how far down y x axis to draw lecture
    private double pixelsAcrossGrid = 0;//how far across x axis to draw lecture
    
    //holds x and y axis pixel positions for drawing the textual information in the correct position
    private int xAxisIndent = 8; //indent from side of lecture rect
    private double stringXpos;//indent from the side of the JPanel
    private double titleStringYpos;//how far down y axis to draw strings
    private double staffStringYpos;
    private double roomStringYpos;
    private static final int TITLEINDENT = 15;//indent down into the y axis
    private static final int STAFFINDENT = 30;
    private static final int ROOMINDENT = 45;
    private int titleIndent = 15;//these can increment if a string needs to go onto a new line
    private int staffIndent = 30;//but are returned back to the FINAL variables for the next lecture
    private int roomIndent = 45;
    
    //--------------------------------- variables for mouseMoved and ActionPerformed ---------------------------------------------------
    private Timer timer = new Timer(1000, this);//when timer reaches end detects if should display the info box
    private int xpos;//gets the x-axis position of mouse on every mouseMove
    private int ypos;
    private boolean mouseOverLec = false;//if withinlec() detects that mouse is within a lecture rect - mouseOverLec = true
    private Lecture newlyFound; //when mouse is within a new lecture its saved here
    private Lecture current = newlyFound; //stores the lecture from the previous mouseMove
    //------------------------

    int colourArrayPos;//tracks the current position in the Color array
    Color[] c = {Color.LIGHT_GRAY, new Color(0x8FD8D8), new Color(0xB3C95A),//colour array to hold various colours to 
        new Color(0x7A67EE), new Color(0xE3CF57), new Color(0x66CC99)       //differentiate between lectures of different types
    };

    private int paintC = 0;//default 0, value determines which methods the paintComponent calls
    private boolean print = false;//if true prepare the timetable for printing
    
    private boolean staff;//if true a staff member is using the applet
    private boolean student;//if true a studentid is present
    private boolean anyLectures;//if true there are lectures available, not necessary this week
    private boolean weekLectures = false;//if true there are lectures this week

    /**
     * Initialises variables with the parameters passed across to the methods
     * @param X space to leave around the X axis of the panel
     * @param Y space to leave around the Y axis of the panel
     * @param lectures ArrayList of Lectures containing the information to be displayed as a timetable
     * @param process Process object that can be used to call its methods
     * @param student Student object containing the students information
     */
    public JPanelGUI(int X, int Y, ArrayList<Lecture> lectures, Process process, Student student) {
        this.X = X;
        this.Y = Y;
        this.lectures = lectures;
        this.process = process;
        this.studentObject = student;

        addMouseMotionListener(this);
    
    }


    /**
     * paintComponet calls all the relevant methods and passes the graphics object to allow them to draw
     * @Override
     */
    @Override
    public void paintComponent(Graphics g) {
        timer.start();//starts the timer so can detect if the mouse is within a lecture when it completes
        Graphics2D g2 = (Graphics2D) g;//casts the graphics to a graphics2D object

        setUpVariables();//sets up the variables used in drawing such as width and height
        checkLectures();//checks if there are any lectures available to draw
        
        if (!print) {//if print true draw the background white otherwise use colours
            
            g2.setPaint(new Color(0xcccccc));

        } else {
            g2.setPaint(Color.WHITE);
        
        }
        g2.fill(new Rectangle.Double(0, 0, width, height));//draws a filled rectangle for the background colour


        if (paintC == 0) {//normal - draws grid labels lectures 

            label(g2);
            drawLectures(g2);
            if (!print) {//no need to call hereNow if print true            

                drawHereNow(g2);
            }
        } else if (paintC == 1) {// draws grid labels lectures and mouse over lectures

            label(g2);
            drawLectures(g2);
            drawHereNow(g2);

            drawLecMouseOver(g2);
        } else if (paintC == 2) {//no lectures found (in checkLectures())

            noLectures(g2);
            label(g2);
        }
    }

    /**
     * Called before paintComponent does any drawing, it gathers the required variables.
     * Sets the correct width and height and boolean variables for whether it is a student
     * using the applet or not
     */
    public void setUpVariables() {
        if (!print) {
            if (this.getWidth() > 800) {//if width is larger than 800 pixels set width to 800, 
                                        //not visually acceptable when larger than 800
                width = 800;
            } else {
                width = this.getWidth();
            }
            if (this.getHeight() > 500) {//if height is larger than 500 pixels set height to 500, 
                                        //not visually acceptable when larger than 500
                height = 500;
            } else {
                height = this.getHeight();
            }
        } else {
            width = 745;//when printing width and height are set to fit on page
            height = 500;
        }
        //divides screen size into equal sections for the spacing between lines across and down
        across = (width - (X * 2)) / 9;
                //System.out.println("spacing between lines across = " + across);
        down = (height - (Y * 2)) / 5;
                // System.out.println("spacing between lines down = " + down);
        
        //checks if there is a studentid present in the StudentObject
        //used when printing - decides whether to include the student infor at top of printout when a stuent is using applet
        if (studentObject.getStudentID().equalsIgnoreCase("null") || studentObject.getStudentID().equalsIgnoreCase("")) {
            student = false;
        } else {
            student = true;
        }

        anyLectures = studentObject.getAnyLectures();//sets boolean true when there are lectures to display at all and visa versa
        if (lectures.size() > 0) {//sets weekLectures true if there are lectures to display this week and visa versa
            weekLectures = true;
        } else {
            weekLectures = false;
        }
    }

    /**
     * If there are no lectures at all to disply or no lectures to display this week paintC is set to 2
     * which calls the noLectures() method to display an error message
     */
    public void checkLectures() {

        if (!anyLectures || !weekLectures) {
            paintC = 2;
        }
    }

    /**
     * Used to setPaintC to a different variable from another class
     * NOT USED - TESTING!!!
     * @param paintCInt
     */
    public void setPaintC(int paintCInt) {
        paintC = paintCInt;
    }

    /**
     * displays an error message on the screen if there are no lectures to display, 
     * shows an informative error when no lecturers exist or if no lectures for that week
     * @param g2
     */
    public void noLectures(Graphics2D g2) {
        g2.setPaint(new Color(0xFF9999));//pale red for background

        g2.fill(new Rectangle.Double(X, Y, width - (X * 2), height - (Y * 2)));//colours grid in pale red

        g2.setPaint(Color.RED);//colour dark red for text

        int localFontSizeMain = 18;//font sizes used within this method
        int localFontSizeSmall = 14;
        g2.setFont(new Font("ariel", Font.BOLD, 18));//sets font

        String tempString;


        if (anyLectures) {//if there are lectures
            if (!weekLectures) {//but none this week
                tempString = "No lectures this week";
                //draws the string in the middle of the screen by using the stringPixelWidth method
                g2.drawString(tempString, (float) (0.5 * (width - process.stringPixelWidth(tempString, localFontSizeMain))), (float) height / 2);
            }
        } else if (!anyLectures) {//no lectures at all to display
            if (staff) {//staff using applet
                if (student) {//and searched for studentid
                    tempString = "No lectures to display for the student ID: " + studentObject.getStudentID();
                    g2.drawString(tempString, (float) (0.5 * (width - process.stringPixelWidth(tempString, localFontSizeMain))), (float) height / 2);
                } else if (!student) {//and not yet search for applet - NO LONGER USED (OLD)
                    tempString = "Welcome Staff";
                    g2.drawString(tempString, (float) (0.5 * (width - process.stringPixelWidth(tempString, localFontSizeMain))), (float) height / 2);

                    g2.setFont(new Font("ariel", Font.BOLD, localFontSizeSmall));
                    tempString = "please use the drop down and text boxes to search for a timetable";
                    g2.drawString(tempString, (float) (0.5 * (width - process.stringPixelWidth(tempString, 14))), (float) ((height / 3) * 2));
                    g2.setFont(new Font("ariel", Font.BOLD, localFontSizeMain));
                }
            } else if (!staff) {//staff not using applet
                if (student) {//and student using applet with not lecturers at all
                    tempString = "No lectures to display for the student ID: " + studentObject.getStudentID();
                    g2.drawString(tempString, (float) (0.5 * (width - process.stringPixelWidth(tempString, localFontSizeMain))), (float) height / 2);
                }
            }
        }
        paintC = 0;//resets paintC to default

    }

    //----------------------------- Labelling methods ----------------------------------------------------
    /**
     * label calls the other labelling methods in the correct order and only the ones needed
     * depending on the current situation 
     * @param g2
     */
    public void label(Graphics2D g2) {
        if (staff) {//will only call labelStudent if staff using timetable application
                    //or if it is being printed

            labelStudent(g2);
        } else if (print) {
            labelStudent(g2);
        }

        drawGrid(g2);
        labelDay(g2);
        labelTime(g2);

    }

    /**
     * Draws the student information at the top of the applet
     * @param g2
     */
    public void labelStudent(Graphics2D g2) {

        if (student && anyLectures) {//only draws when there are lectures to display and there is a studentObject
            g2.setPaint(Color.BLACK);
            g2.setFont(new Font("ariel", Font.PLAIN, 14));
            //concatenate first and last name
            String name = studentObject.getFirstName() + " " + studentObject.getLastName();
            //get the string length in pixels
            double nameLength = process.stringPixelWidth(name, 16);
            if (!print) {

                g2.drawString(name, X, Y / 2);//draws in the middle of the Y border
                g2.setFont(new Font("ariel", Font.PLAIN, 13));
                g2.drawString("(" + studentObject.getStudentID() + ")", (float) (X + nameLength + 10), (float) Y / 2);
            } else {//displays more information for the printout version of the timetable
                //draws the student name as above
                g2.drawString(name, X, 12);
                g2.setFont(new Font("ariel", Font.PLAIN, 10));
                g2.drawString("(" + studentObject.getStudentID() + ")", (float) (X + nameLength + 10), 13);
                //also displays the week commencing date as the timetable can change from week to week
                Calendar cal = Calendar.getInstance();
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                int dateDiff;

                switch (dayOfWeek) {
                    case 0:
                        dateDiff = 2;
                        break;
                    case 1:
                        dateDiff = 1;
                        break;
                    case 2:
                        dateDiff = 0;
                        break;
                    case 3:
                        dateDiff = -1;
                        break;
                    case 4:
                        dateDiff = -2;
                        break;
                    case 5:
                        dateDiff = -3;
                        break;
                    case 6:
                        dateDiff = -4;
                        break;
                    default:
                        dateDiff = 0;
                        break;
                }

                g2.drawString("Timetable week commencing: " + process.getDate(dateDiff), X, 25);
            }

        }
    }

    /**
     * Draws the labels for the day down the X axis of the timetable
     * Dynamically calculates the dates depending on the day of the week and the current week
     * @param g2
     */
    public void labelDay(Graphics2D g2) {

        float xAxisDayLabel = X / 4;//indent into the x axis, depends on size of X border space

        Calendar date = Calendar.getInstance();//get new instance of Calendar, used to get current day etc

        /**
         * if the current day is monday then the first label can be found by just getting the date,
         * 1 will need to be added to the current date to get tuesdays date, and 3 to get wed date
         * and so on. The following calculates the int to be added or subtracted to get the first, second, .....
         * labels date
         */
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {//MONDAY

            first = 0;
            second = 1;
            third = 2;
            fourth = 3;
            fifth = 4;
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {//TUESDAY

            first = -1;
            second = 0;
            third = 1;
            fourth = 2;
            fifth = 3;
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {//WEDNESDAY

            first = -2;
            second = -1;
            third = 0;
            fourth = 1;
            fifth = 2;
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {//THURSDAY

            first = -3;
            second = -2;
            third = -1;
            fourth = 0;
            fifth = 1;
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {//FRIDAY

            first = -4;
            second = -3;
            third = -2;
            fourth = -1;
            fifth = 0;
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {//SATURDAY

            first = 2;
            second = 3;
            third = 4;
            fourth = 5;
            fifth = 6;
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {//SUNDAY

            first = 1;
            second = 2;
            third = 3;
            fourth = 4;
            fifth = 5;
        }
        
        //draws the labels using ints calulated above
        g2.setPaint(Color.BLACK);
        g2.setFont(new Font("ariel", Font.PLAIN, 10));
        g2.drawString("Mon", xAxisDayLabel, (float) (Y + (down / 2) - 5));
        g2.drawString(process.getDate(first), xAxisDayLabel, (float) (Y + (down / 2) + 5));

        g2.drawString("Tue", xAxisDayLabel, (float) (Y + (down / 2) - 5 + (down)));
        g2.drawString(process.getDate(second), xAxisDayLabel, (float) (Y + (down / 2) + 5 + (down)));

        g2.drawString("Wed", xAxisDayLabel, (float) (Y + (down / 2) - 5 + (down) * 2));
        g2.drawString(process.getDate(third), xAxisDayLabel, (float) (Y + (down / 2) + 5 + (down) * 2));

        g2.drawString("Thu", xAxisDayLabel, (float) (Y + (down / 2) - 5 + (down) * 3));
        g2.drawString(process.getDate(fourth), xAxisDayLabel, (float) (Y + (down / 2) + 5 + (down) * 3));

        g2.drawString("Fri", xAxisDayLabel, (float) (Y + (down / 2) - 5 + (down) * 4));
        g2.drawString(process.getDate(fifth), xAxisDayLabel, (float) (Y + (down / 2) + 5 + (down) * 4));
    }

    /**
     * draws the labels across the top of the timetable to show the hours from 9am till 6pm
     * @param g2
     */
    public void labelTime(Graphics2D g2) {
        g2.setPaint(Color.BLACK);
        g2.setFont(new Font("ariel", Font.PLAIN, 10));
        float yAxisDayLabel = Y - 6; // draws in the Y axis border, 6 pixels from the timetable
        String[] timeLabels = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
        for (int i = 0; i < timeLabels.length; i++) {//loops through array above and draws the string in the correct position
            g2.drawString(timeLabels[i], (float) ((X - 14) + (across * i)), yAxisDayLabel);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Draws the grid for the timetable depending on the the width and height of the JPanel and the variables
     * set in setVariables(), across and down
     * @param g2
     */
    public void drawGrid(Graphics2D g2) {

        if (print) {//when print is true the grid draws a light grey so its easy to view the lectures
            g2.setPaint(new Color(0xD3D3D3));
        } else {//when its not printing the grid is drawn in black
            g2.setPaint(Color.DARK_GRAY);
        }
        g2.setStroke(new BasicStroke(1.0f));//thickness of the lines

        double x = X;//local copies of X and Y so they can be changed and then reset
        double y = Y;

        for (int i = 0; i <= 5; i++) {//draws lines going vertical

            g2.draw((new Line2D.Double(x, y, (width - (X)), y)));
            y = y + down;
        //System.out.println(y);
        }

        x = X; //resets x and y in this method to the FINAL variables set at beginning of this class

        y = Y;

        for (int p = 0; p <= 9; p++) {//draw lines going horizontal

            g2.draw((new Line2D.Double(x, y, x, (height - (Y)))));
            x = x + across;
        }

        if (print) {//if print is true - draw a dark rectangle around the outside of the grid

            g2.setPaint(Color.DARK_GRAY);
            g2.draw(new Rectangle.Float(X, Y, (float) width - (X * 2), (float) height - (Y * 2)));
        }

    }

     
    /**
     * Calculates the correct position to draw a lecture in relation to its start time and length and the size of
     * the JPanel, then draws the lecture to the Panel and sets its coordinates in the corresponding Lecture object,
     * this allows to check whether the mouse is within a lecture
     * only called when there are lectures in the current week - for example no lectures half term
     * check carried out in paintComponent
     * @param g2
     */
    public void drawLectures(Graphics2D g2) {
        Color colour = new Color(0xDDDFFF);//sets the colour of the first lecture to be drawn
        
        //tempLec used to check if the next lecture has the same title 
        //as the current one, if not a new colour is fetched from the colour array
        String tempLec = lectures.get(0).getTitle();
        colourArrayPos = 0;//starting position in the colour array, increments when the next colour returned 

        for (int i = 0; i <=lectures.size() - 1; i++) {//loops through all the lectures

            Lecture lec1 = lectures.get(i);//get lecture to look at
            int start = lec1.getStart();//get start timee
            double length = lec1.getLength();//get length of lecture, in minutes
            
            //---------- how far down on the Y axis to draw --------------  
            
            int day = lec1.getDay_of_week();//get day of week the lecture occurs on, 0 - MON, 1 = TUE ....

            switch (day) {// calculates how far down the Y axis to start drawing the lecture

                case 0://if day = 0 then draw Y pixels down
                    pixelsDownGrid = Y;
                    break;

                case 1://if day = 1 then draw Y + down (space in pixels between horizonal lines) pixels down
                    pixelsDownGrid = Y + down;
                    break;

                case 2:
                    pixelsDownGrid = Y + down + down;
                    break;

                case 3:
                    pixelsDownGrid = Y + down + down + down;
                    break;

                case 4:
                    pixelsDownGrid = Y + down + down + down + down;
                    break;

                default:

                    pixelsDownGrid = Y;
                    break;

            }

            //---------- how far across on the X axis to draw --------------  

            //turn start time into a string, split into hours and min to calulate pixel length 
            String tempStart = Integer.toString(start);

            int tempMin = 0;
            //gets substring of min(tempMin) and hours (pixelsAcrossGrid)
            if (tempStart.length() == 3) {//time is in format HMM
                pixelsAcrossGrid = Integer.parseInt(tempStart.substring(0, 1));
                tempMin = Integer.parseInt(tempStart.substring(1));
            } else if (tempStart.length() == 4) {//time is in format HHMM
                pixelsAcrossGrid = Integer.parseInt(tempStart.substring(0, 2));
                tempMin = Integer.parseInt(tempStart.substring(2));

            }
            //if pixelsAcrossGrid = 9, then - 9 = 0, so the lecture starts at 9 which is 0 pixels into the grid
            double q = pixelsAcrossGrid - 9; //if q = 1 then lecture starts at 10, if 2 start at 11 etc..

            pixelsAcrossGrid = X; // set pixelsAcrossGrid to X to move in to start of grid then add additional pixels

            //for each loop q decreases and an hour is added onto how far to start drawing in
            while (q > 0) {
                pixelsAcrossGrid = pixelsAcrossGrid + across;
                q--;

            }

            double minAcross = (across / 60) * tempMin;
            pixelsAcrossGrid = pixelsAcrossGrid + minAcross;//adds the pixels in for min and hours together
            length =(across / 60) * length;//sets the length variable to the length of the lecture for drawing the rectangles

            //draw lecture using the values calculated above

            if (!print) {//if print boolean set to false use colours

                colour = getNewColour(colourArrayPos);

                g2.setPaint(colour);
                //if the currentLec title = the tempLec title then use current colour
                if (tempLec.equals(lectures.get(i).getTitle())) {
                    g2.setPaint(colour);
                } else {//otherwise get new colour, different lecture type being drawn
                    colourArrayPos++;
                    colour = getNewColour(colourArrayPos);
                    g2.setPaint(colour);
                }
               
                //sets the tempLec to the one being looked at now, so it can be used in the next loop
                //to see if a new colour is needed
                tempLec = lectures.get(i).getTitle();

                //draw the rectangle to represent the lecture, with a grey box around it
                g2.fill((new Rectangle.Double(pixelsAcrossGrid, pixelsDownGrid, length, down)));
                g2.setPaint(Color.gray);
                g2.draw(new Rectangle.Double(pixelsAcrossGrid, pixelsDownGrid, length, down));
            } else {//if print boolean set to true use black and white

                g2.setPaint(Color.BLACK);
                g2.setStroke(new BasicStroke(1.5f));

                g2.draw(new Rectangle.Double(pixelsAcrossGrid, pixelsDownGrid, length, down));
                g2.setStroke(new BasicStroke(1.0f));
            }

//------------------ variables for lecture information positioning -------------------
            stringXpos = pixelsAcrossGrid + xAxisIndent; //calculates the positions of where to draw the strings
            titleStringYpos = pixelsDownGrid + titleIndent;//depending on where the lecture is positioned on the grid
            staffStringYpos = pixelsDownGrid + staffIndent;
            roomStringYpos = pixelsDownGrid + roomIndent;
//------------------------------------------------------------------------------------
            g2.setPaint(Color.BLACK);
            fontSize = getFontSize();//the font size changes depending on width and height
            if (width > 400 && height > 400) {//only writes the lecture info when the applet is greater than (400, 400)
                
                //passes the string and pixel position values to another method to do the drawing
                writeLecInfo(lectures.get(i).getTitle(), 1, length - xAxisIndent, g2, stringXpos, titleStringYpos, fontSize);
                writeLecInfo(lectures.get(i).getStaff(), 2, length - xAxisIndent, g2, stringXpos, staffStringYpos, fontSize);
                writeLecInfo(lectures.get(i).getRoom(), 3, length - xAxisIndent, g2, stringXpos, roomStringYpos, fontSize);
            }
               
            //reset the titleIndent etc to the final values ready for the next lecture
            //they may have changed in writeLecInfo if the String could not fit within the lecture
            titleIndent = TITLEINDENT;
            staffIndent = STAFFINDENT;
            roomIndent = ROOMINDENT;
            
            //set the x and y positions of the rectangle representing the lecture in the lecture object so that 
            //the mouseMoved method can calculate whether the mouse is within a lecture
            lectures.get(i).setRectX(pixelsAcrossGrid);
            lectures.get(i).setRectY(pixelsDownGrid);
            lectures.get(i).setRectWidth(pixelsAcrossGrid + length);
            lectures.get(i).setRectHeight(pixelsDownGrid + down);

        }

    }

    /**
     * Draws the info box when the mouse is hovered over a lecture rectangle
     * @param g2
     */
    public void drawLecMouseOver(Graphics2D g2) {
        g2.setPaint(new Color(0xFFFF33));//fill colour

        g2.fill(new Rectangle.Double(xpos, ypos, mouseOverW, mouseOverH));//draws rect
        g2.setPaint(Color.BLACK);//border colour
        g2.draw(new Rectangle.Double(xpos, ypos, mouseOverW, mouseOverH));//draws border rect
        
        //uses writeLecInfo to draw the string, passes the string and positioning values
        writeLecInfo(newlyFound.getTitle(), 1, 200, g2, xpos + xAxisIndent, ypos + titleIndent, 13);
        writeLecInfo(newlyFound.getStaff(), 2, 200, g2, xpos + xAxisIndent, ypos + staffIndent, 13);
        writeLecInfo(newlyFound.getRoom(), 3, 200, g2, xpos + xAxisIndent, ypos + roomIndent, 13);
        writeLecInfo("Start Time: " + process.timeIntToString(newlyFound.getStart()), 0, 200, g2, xpos + xAxisIndent, ypos + 80, 13);
        writeLecInfo("End Time: " + process.timeIntToString(newlyFound.getEnd()), 0, 200, g2, xpos + xAxisIndent, ypos + 95, 13);

        paintC = 0;
    }

    /**
     * Draws the redline going down the grid to show where the student be at the current time
     * Only draws the line mon - fri between 9am - 6pm. Does not draw on printout. Only draws
     * on the current day
     * @param g2
     */
    public void drawHereNow(Graphics2D g2) {
        //HERE NOW LINE
        if (weekLectures) {//if weekLectures = true (there are lectures to display)

            Calendar cal = Calendar.getInstance();
            //will not draw if its sat/sun
            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {

                //if the time is within 9 and 6 oclock
                if (cal.get(Calendar.HOUR_OF_DAY) <= 18 && cal.get(Calendar.HOUR_OF_DAY) >= 9) {

                    g2.setPaint(Color.RED);
                    g2.setFont(new Font("ariel", Font.BOLD, 12));
                    double xNow = process.hereNow(across);//gets the x axis position of where to draw the line
                    
                    //draws string "now"
                    g2.drawString("Now", (float) xNow + (X - 10), (float) Y - 20);
                    //small line at top of grid under "Now" String
                    g2.draw((new Line2D.Double(xNow + X, Y - 10, xNow + X, Y)));

                    int day = cal.get(Calendar.DAY_OF_WEEK) - 2;//subtract 2 to account for the weekend, days start at sat

                    g2.draw((new Line2D.Double(xNow + X, (down * day) + Y, xNow + X, (down * (day + 1) + Y))));//draws line on the current day

                    //small line at bottom of grid so it makes it easier to view where hereNow line is in the middle of the grid
                    g2.draw((new Line2D.Double(xNow + X, (down * 5) + Y, xNow + X, (down * 5) + (Y + 10))));
                    g2.setPaint(Color.BLACK);
                }

            }
        }
    }

    /**
     * Draws the string passed to the method (text) at the position passed to it. 
     * If the string does not fit within the pixel length supplied it will handle it
     * depending whether print is true or false.
     * @param text holds the String that is to be written to the graphicsComponent
     * @param textType 
     * @param lecLength length of the Rectangle in pixels that the String text must fit into
     * @param g2 Graphics2D object
     * @param x x-axis position the String text is to be drawn at
     * @param y y-axis position the Stringtext is to be drawn at
     * @param fontSize 
     */
    public void writeLecInfo(String text, int textType, double lecLength, Graphics g2, double x, double y, int fontSize) {

        g2.setFont(new Font("ariel", Font.PLAIN, fontSize));
        String tempString;

        tempString = text;
        int tempStringLength = process.stringPixelWidth(tempString, fontSize);//gets pixels length of string
        
        boolean stringTrimmed = false;
        if (!print) {//draws as much of the string and concatenate "..." to the end
            //loop through till get to position in the string that fits within length
            while (tempStringLength > lecLength && tempStringLength > 0) {

                tempString = tempString.substring(0, tempString.length() - 4);
                tempString = tempString + "...";
                tempStringLength = process.stringPixelWidth(tempString, fontSize);
            }
            g2.drawString(tempString, (int) x, (int) y);
        } else {//if printing want to draw as much within lec then move down a line and draw the rest
            String buffer = "";//buffer stored the part of the string that will not fit within lec
            boolean firstloop = true;
            while (tempStringLength > lecLength && tempStringLength > 0) {
                if (firstloop) {//for the first loop can substring to the end of the tempString. 
                                //but after that want to remove the "-" that gets concatenated 
                    stringTrimmed = true;
                    buffer = tempString.substring(tempString.length() - 2, tempString.length()) + buffer;
                    firstloop = false;
                } else {
                    buffer = tempString.substring(tempString.length() - 2, tempString.length() - 1) + buffer;
                }
                tempString = tempString.substring(0, tempString.length() - 2);
                tempString = tempString + "-";//adds a "-" to the end to show that the rest of the text is on the next line
                tempStringLength = process.stringPixelWidth(tempString, fontSize);
            }

            if (stringTrimmed) {//if the string has been trimmed down for printing
                g2.drawString(tempString, (int) x, (int) y);//draw both tempString
                g2.drawString(buffer, (int) x, (int) y + 7);//and buffer

                //need to increment the Y axis indents if the string has been moved to a new line
                switch (textType) {//textType corresponds to what the string is, lecture title, room etc..
                    case 0:
                        break;
                    case 1://lecture title
                        titleStringYpos = titleStringYpos + 10;
                        staffStringYpos = staffStringYpos + 10;
                        roomStringYpos = roomStringYpos + 10;
                        break;
                    case 2://staff teaching lecture
                        staffStringYpos = staffStringYpos + 10;
                        roomStringYpos = roomStringYpos + 10;
                        break;
                    case 3://room number
                        roomStringYpos = roomStringYpos + 10;
                        break;
                }            
            } else {//otherwise just draw the tempString
                g2.drawString(tempString, (int) x, (int) y);
            }
        }
    }

    //-----------------------------------------------------------
    /**
     * returns a new colour from the colour array c
     * @param colourArrayPos the array position currently looking at
     * @return
     */
    public Color getNewColour(int colourArrayPos) {

        return c[colourArrayPos];
    }

    /**
     * calculates the font size for text depending on the width and height of the applet
     * @return font size for the current width and height
     */
    public int getFontSize() {
        int newFontSize = 0;
        if (!print) {
            if (width <= 600) {
                newFontSize = 8;
            } else if (width > 600 && width <= 700) {
                newFontSize = 9;
            } else if (width > 700 && width <= 800) {
                newFontSize = 10;
            } else if (width > 800 && width <= 900) {
                newFontSize = 11;
            } else if (width > 900 && width <= 1000) {
                newFontSize = 12;
            } else if (width > 1000) {
                newFontSize = 13;
            }

        } else {
            newFontSize = 5;
        }

        return newFontSize;
    }

    /**
     * sets the print variable to the boolean passed as parameter
     * @param print
     */
    public void setPrint(boolean print) {
        this.print = print;
    }

    /**
     * Sets staff variable to the boolean passed as parameter
     * @param staff
     */
    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    /**
     * checks whether the mouse position is currently over a rectangle representing 
     * a lecture or not
     * @return true or false depending if mouse is over lecture or not
     */
    public boolean withinLec() {
        mouseOverLec = false;
        int i = 0;
        while (!mouseOverLec && i < lectures.size()) {//loop through all lectures
            //checks the mouse x and y values are within a lectures rectangle coordinates
            if (xpos > lectures.get(i).getRectX() && ypos >
                    lectures.get(i).getRectY() && xpos < lectures.get(i).getRectWidth() &&
                    ypos < lectures.get(i).getRectHeight()) {

                mouseOverLec = true;//if so set to true
                newlyFound = lectures.get(i);
            }

            i++;
        }

        return mouseOverLec;
    }


//--------------------- abstract methods for MouseMotionListener ----------------------------
    //Not used
    public void mouseDragged(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }
    
    //everytime the mouse is moved the code in this method is carried out
    public void mouseMoved(MouseEvent e) {

        timer.restart();//resets timer to begin counting again
        
        //gets the x and y axis position of mouse on every mouseMove
        xpos = e.getX();
        ypos = e.getY();
        
        //if the mouse position is within a lecture object the timetable is not repainted
        //This stops the info box from disappearing when within a lecture
        if(!withinLec()){
            paintC = 0;
            Graphics g = getGraphics();
            update(g);
        }
    }
    
/**
 * Handles any actionEvents that occur within the JPanel.
 * The only one being handled in the timetable application is the timer.
 * When it reaches 0 a check is done to see if the mouse position is within a lecture rectangle.
 * If so then it draws an Information box, gives the effect of a tooltip
 * @param e ActionEvent that has occured
 */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timer) {//if the timer reaches 0

            if (getMousePosition() != null) {//will only carry this out if the mouse pointer is within the applet

                if (!print || !anyLectures || !weekLectures) {//wont do anything if print set to true || there are no lectures to display

                    //gets x and y coordinates of mouse pointer
                    xpos = getMousePosition().x;
                    ypos = getMousePosition().y;
                    paintC = 0;

                    //width and height of the mouseOver info box
                    mouseOverW = 200;//change width and height to ajust according to window size!!!!!
                    mouseOverH = 100;

                    //if withinLec true, the current mouse position is within one of the lecture rectangles
                    if (withinLec()) {
                        
                        //if the newlyFound lecture coordinates refer to the same lecture rect as 
                        //the current one then repaint info box with current lecture
                        if (current == newlyFound) {
                            paintC = 1;
                            //gets the graphics object and passes it to update rather than repaint()
                            Graphics g = getGraphics();
                            update(g);
                        } 
                        //else if the newlyFound coordinates refer to a different lecture rectangle then
                        //set current to be newlyFound
                        else {
                            current = newlyFound;
                            paintC = 1;
                            //gets the graphics object and passes it to update rather than repaint()
                            Graphics g = getGraphics();
                            update(g);
                        }
                    } else {//not within a lecture - redraw timetable as normal
                        paintC = 0;
                        //gets the graphics object and passes it to update rather than repaint()
                        Graphics g = getGraphics();
                        update(g);
                    }
                }
            }
        }
    }

    //----------------------- Methods for printing ---------------------------------------------------------------
    /**
     * Sets up the pageFormat and displays the printer dialog, if the user accepts the printer dialog
     * the print method is called and the job is sent to the printer
     */
    public void print() {
       // System.out.println("Print has been called");
        PrinterJob printJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printJob.defaultPage();
        pageFormat.setOrientation(PageFormat.LANDSCAPE);//sets the page to landscape
        //sets this JPanelGUI to be printable, as this contains the graphics context to be printed
        printJob.setPrintable(JPanelGUI.this, pageFormat);
        //System.out.println("Displaying the printer dialog");


        if (printJob.printDialog()) {//if user clicks ok
            try {
                printJob.print();
            } catch (Exception PrintException) {
                System.out.println("Print exception from print()");
            }

        }
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {

        Graphics2D g2 = (Graphics2D) g;
        g2.translate((int) pf.getImageableX(), (int) pf.getImageableY());//gets the x and y of the pageformat and translates it 
        
        this.setDoubleBuffered(false);//graphics on printed page not visually acceptable when this is true
        paint(g2);
        this.setDoubleBuffered(true);//set back to true when the page has been painted
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        } else {
            return Printable.PAGE_EXISTS;
        }
    }
}// End of JPanelGUI Class
