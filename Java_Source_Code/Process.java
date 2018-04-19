import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Process class contains all of the methods that are used to do mainly the preprocessing
 * of lecture information. Also contains methods that carry out processing or calculations
 * for example calulating the pixel length of a string
 * @author David Hughes
 */
public class Process {

    private ArrayList<Lecture> lectures;//ArrayList containing all the lectures
    
    /* FINAL variable storing the difference between the real current 
    week and the current academic week at halesowen college */
    private static final int DIFFERENCE = 23;
    private static final String DATE_FORMAT = "dd.MM.yy";

    /**
     * Instantiates a copy of the Process class
     */
    public Process() {
    }
    //

    /**
     * preprocessing of lectures - sets up ready for use
     * this method is called first from AppletStart before the JPanelGUI draws anything, it 
     * calls the relevent methods to filter out all of the lectures and shows those 
     * only on the current week. 
     * @param lectures
     * @return
     */
    public ArrayList<Lecture> process(ArrayList<Lecture> lectures) {
        this.lectures = new ArrayList<Lecture>(lectures);
        processWeeks();
        setLecLength();
        nameSwitch();
        return this.lectures;
    }

    /**
     * Checks if there are any lectures at any time to show for the student
     * @param lectures
     * @param student
     */
    public void lecturesForStudent(ArrayList<Lecture> lectures, Student student) {
        if (lectures.size() == 0) {
            student.setAnyLectures(false);//sets to false if no lectures for student
        } else {
            student.setAnyLectures(true);//sets to true if no lectures for student
        }
    }

    /**
     * Filters out all of lecturers that do not happen on the current college week
     * The current college week does not correspong to the actual week of the year so
     * this must first be calculated
     */
    public void processWeeks() {
        
        Calendar c = Calendar.getInstance();
        
        // gets current week of the year
        int weekOfYear = c.get(Calendar.WEEK_OF_YEAR);
        //System.out.println("Current weekOfYear: " + weekOfYear);
        int k = c.getFirstDayOfWeek();
        //System.out.println("FirstDayOfWeek: " + k); 
		
	//----------------------------------------------------------------
        // calculates the current week at the college using the current week and FINAL difference variables

        int collegeWeek = 0;

        int currentYear = c.get(Calendar.YEAR);
        if (currentYear == 2008) {//if 2008 the difference must be subtracted
            collegeWeek = weekOfYear - DIFFERENCE;
        } else if (currentYear == 2009) {//if 2009 the difference must be added
            
            //if its a sat or sun returns the timetable for the next week
            if (c.get(Calendar.DAY_OF_WEEK) == 6 || c.get(Calendar.DAY_OF_WEEK) == 1) {

                collegeWeek = weekOfYear + DIFFERENCE + 1;//add one to set the week to the next week
            } else {
                collegeWeek = weekOfYear + DIFFERENCE;
            }
        //-----------------------------------------------------------------
        }
        //loops round each of the lectures in the ArrayList<Lecture> lectures
        for (int i = 0; i < lectures.size(); i++) {
            Lecture lec = lectures.get(i);
            String weeks = lec.getWeeks();
            /**
             * gets string weeks. Length = amount of weeks at college, if char at the 
             * current college week position = "Y" then this lecture occur on this week, 
             * otherwise if = "N" then it doesnt and can be removed from the lectures arrayList
             */

            //get the letter at the current week from weeks
            String p = weeks.substring(collegeWeek - 1, collegeWeek);

            /* if(p = n) then remove that lecture 
             * from the ArrayList, left with just the Lectures for the current week.             
             */
            if (p.toUpperCase().equals("N")) {
                //System.out.println("to be removed");
                lectures.remove(i);
                i = i - 1;
            }
        }
    }

    /**
     * Sets the length of all the length by calculating the difference between
     * the start and end times using the timediff() method
     */
    public void setLecLength() {

        for (int i = 0; i < lectures.size(); i++) {
            lectures.get(i).setLength(timeDiff(lectures.get(i).getStart(), lectures.get(i).getEnd()));
        }

    }

    /**
     * Calculates the difference between two given integers
     * Used to calculate the length of a lecture by calculating the 
     * difference between the given start and end times
     * @param start
     * @param end
     * @return
     */
    public int timeDiff(int start, int end) {
        int diff = hoursToMin(end) - hoursToMin(start);
        return diff;
    }

    /**
     * Converts a given time in the either the format HHMM or HMM and calculates
     * the length in minutes
     * @param time
     * @return
     */
    public int hoursToMin(int time) {

        int minutes = 0000;
        String tempTime = Integer.toString(time);//converts to string
        int a = 00;//hours

        int b = 00;//minutes

        if (tempTime.length() == 3) {
            a = Integer.parseInt(tempTime.substring(0, 1));//substring the hours and min
            b = Integer.parseInt(tempTime.substring(1));
        } else if (tempTime.length() == 4) {
            a = Integer.parseInt(tempTime.substring(0, 2));//substring the hours and min
            b = Integer.parseInt(tempTime.substring(2));
        }
        minutes = (a * 60) + b;//multiply hours by 60 and add the remaining min
        return minutes;
    }

    /**
     * Given an int time in the format either HHMM or HMM convert it to a 
     * string version that can be easily read when shown on the timetable
     * @param time the time to be converted to string
     * @return the string version of the time
     */
    public String timeIntToString(int time) {
        String newTime = Integer.toString(time);
        if (newTime.length() == 3) {//given 900 will return 9:00
            newTime = newTime.substring(0, 1) + ":" + newTime.substring(1, 3);
        } else if (newTime.length() == 4) {//given 1400 will return 14:00
            newTime = newTime.substring(0, 2) + ":" + newTime.substring(2, 4);
        }
        return newTime;
    }

    /**
     * gets the current date and adds the dateMod int passed.
     * This allows the date for 2 days time to be returned by adding
     * 2 to the current date. Can also pass negative number to  find out the previous dates
     * @param dateMod
     * @return
     */
    public String getDate(int dateMod) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, dateMod);//add the dateMod to find a past or future date
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);//apply the format
        return sdf.format(cal.getTime());
    }

    /**
     * Calulates the pixel width of a string
     * @param text the text to measured
     * @param fontSize font size the pixel will be drawn in
     * @return
     */
    public int stringPixelWidth(String text, int fontSize) {
        Font font = new Font("ariel", Font.PLAIN, fontSize);
        FontMetrics metrics = new FontMetrics(font) {//creates a fontMetrics object which can be used to find out the string width
        };
        Rectangle2D bounds = metrics.getStringBounds(text, null);//creates a rectangle that would surround the text
        int widthInPixels = (int) bounds.getWidth();//cast the width of the rectangle bounds to an int
        // System.out.println(widthInPixels);
        return widthInPixels;
    }

    /**
     * Calculates where on the X axis to draw the here now line
     * @param across pixel space between vertical lines which represent 1 hour
     * @return
     */
    public double hereNow(double across) {
        //grid size is width - (X*2), across value is between hours on the grid
        
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfhour = new SimpleDateFormat("HH");//uppercase gives hour in 24hour
        SimpleDateFormat sdfmin = new SimpleDateFormat("mm");
        
        int hour = Integer.parseInt(sdfhour.format(c.getTime()));
        //if its past 6 oclock then do not want to draw here now line.
        int min = Integer.parseInt(sdfmin.format(c.getTime()));

        //calculates min in pixels in ratio to across
        double a = (across / 60) * min;
        double p = (hour - 9) * across;

        return a + p;
    }

    /**
     * SQL SERVER stores the names in the format 'surname: forname'
     * This is not very user friendly to view. This method removes the ":"
     * and switches the forname and surname around
     */
    public void nameSwitch() {
        for (int i = 0; i < lectures.size(); i++) {//loops through each lecture and performs switch
            String staffName = lectures.get(i).getStaff();//get staff name
            String[] surname = staffName.split(":");//split string at the ":" into an array
            String newName = surname[1] + " " +surname[0];//switch around
            newName = newName.trim();//trim whitespace
            lectures.get(i).setStaff(newName);//set namename
            //System.out.println(newName);
           // System.out.println(lectures.get(i).getStaff());
        }
    }

    /**
     * student surnames on the SQL SERVER are stored in Uppercase.
     * This method sets the surname to be lowercase with an uppercase first letter
     * @param student
     */
    public void stringFirstUppercase(Student student){
        String lastname = student.getLastName();//get lastname
        lastname = lastname.toLowerCase();//set all char to lowercase
        lastname = lastname.substring(0, 1).toUpperCase() + lastname.substring(1, lastname.length());//uppercase first char
        student.setLastName(lastname);//set lastname with the new lastname
    }
    
    /**
     * Iterate through the list and print
     * the contents
     * @param lecturesPrint 
     */
    public void printData(ArrayList<Lecture> lecturesPrint) {

        System.out.println("No of Lectures '" + lecturesPrint.size() + "'.");

        Iterator it = lecturesPrint.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    /**
     * TESTING
     * @param args
     */
    public static void main(String[] args) {
        Process p = new Process();
//        int a = p.timeDiff(1100, 1400);
//        System.out.println("minutes difference = " + a);
//        System.out.println("hours (a/60) = " + a / 60);
//        System.out.println("minutes (a%60) = " + a % 60);
        //p.hereNow(45);
        Student student = new Student();
        student.setLastName("JONES");
        System.out.println(student.getLastName());
        p.stringFirstUppercase(student);
        System.out.println(student.getLastName());
        
    }
}// End of Process Class
