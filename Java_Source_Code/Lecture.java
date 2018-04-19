/**
 * A Lecture object stores all the data that is unique to one lecture, such as start and end times and title.
 * It also stores positioning information for the rectangle that is drawn using java paint. 
 * Class contains all required getters and setters
 * @author David Hughes
 */
public class Lecture {

    //variables that hold lecture information
    private String title, weeks, room, staff; 
    private int day_of_week, start, end, length;
    //variables that hold rectangle positioning information that represent lecture on timetable
    private double rectX, rectY, rectWidth, rectHeight;
   
    /**
     * Instantiates a Lecture object empty
     */
    public Lecture() {
    }
    /**
     * Instantiates a Lecture object with variables assigned
     * @param title Lecture title
     * @param weeks String that is the length of the amount of weeks the lecture runs for. One character 
     * corresponds to one week, character is either Y(the lecture happens this week) or N(the lecture 
     * does not happen this week)
     * @param day_of_week integer corresponding to a day of the week (0 is a mon)
     * @param start start time
     * @param end end time
     * @param room room number of lecture
     * @param staff member of staff teaching the lecture
     */
    public Lecture(String title, String weeks, int day_of_week, String start, String end, String room, String staff) {
        this.title = title;
        this.weeks = weeks;
        this.day_of_week = day_of_week;
        setStart(convertTimeString(start));
        setEnd(convertTimeString(end));
        this.room = room;
        this.staff = staff;
    }
    //----------------------------- Getter methods ------------------------
    /**
     * returns the lectures title
     * @return String <code>title</code>
     */
    public String getTitle() {
        return title;
    }

    /**
     * returns the weeks that the lecture takes place on
     * @return String <code>weeks</code>
     */
    public String getWeeks() {
        return weeks;
    }

    /**
     * retuns the day of the week that the lecture takes place on
     * 0 = mon
     * 1 = tue
     * . . . 
     * 4 = fri
     * @return int <code>day_of_week</code>
     */
    public int getDay_of_week() {
        return day_of_week;
    }

    /**
     * returns the start time of the lecture
     * @return String <code>start</code>
     */
    public int getStart() {
        return start;
    }

    /**
     * returns the end time of the lecture
     * @return String <code>end</code>
     */
    public int getEnd() {
        return end;
    }

    /**
     * returns the room number of the lecture
     * @return String <code>room</code>
     */
    public String getRoom() {
        return room;
    }

    /**
     * returns the name of the member of staff teaching the lecture
     * @return String <code>staff</code>
     */
    public String getStaff() {
        return staff;
    }
    
    /**
     * returns the length of the lecture in minutes
     * @return int <code>length</code>
     */
    public int getLength(){
        return length;
    }

    /**
     * rectHeight stores the height of the lectures rectangle in pixels, 
     * set by calling <code>setRectHeight(double rectHeight)</code> everytime the applet is resized
     * @return double rectHeight
     */
    public double getRectHeight() {
        return rectHeight;
    }

    /**
     * rectWidth stores the width of the lectures rectangle in pixels, 
     * set by calling <code>setRectWidth(double rectWidth)</code> everytime the applet is resized
     * @return double <code>rectWidth</code>
     */
    public double getRectWidth() {
        return rectWidth;
    }

    /**
     * returns the Xaxis position of the rectangle that represents the lecture in pixels 
     * @return double <code>rectX</code>
     */
    public double getRectX() {
        return rectX;
    }

    /**
     * returns the Yaxis position of the rectanlge that represents the lecture in pixels
     * @return double <code>rectY</code>
     */
    public double getRectY() {
        return rectY;
    }
    
    //--------------------------------------- Setter methods -----------------------------
    /**
     * sets the title of the lecture to the String <code>title</code> that is passed as a parameter
     * @param title 
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * sets the weeks variable to the String <code>weeks</code> that is passed as a parameter
     * @param weeks shows the weeks that this lecture happens on, Y means it occurs on this week, 
     * N means it does not
     */
    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    /**
     * sets the <code>day_of_week</code> of the lecture
     * @param day_of_week denoted by an integer, 0 = mon, 1 = tue...., 4 = fri
     */
    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    /**
     * <code>setStart(int start)</code> is called when passing the 
     * time as an int in the format HHMM or HMM
     * @param start the start time of a lecture
     */
    public void setStart(int start) {
        this.start = start;
    }
    
    /**
     * <code>setStart(String start)</code> is called when passing the time as a String
     * in the format "1899-12-30 10:45:00.0"
     * It converts the start time from a String to an int
     * @param start
     */
    public void setStart(String start) {
        int intTime = convertTimeString(start);
        this.start = intTime;
    }
    
    /**
     * <code>setEnd(int end)</code> is called when passing the 
     * time as an int in the format HHMM or HMM
     * @param end the end time of a lecture
     */
    public void setEnd(int end) {
        this.end = end;
    }
    
    /**
     * <code>setEnd(String end)</code> is called when passing the time as a String
     * in the format "1899-12-30 10:45:00.0"
     * It converts the end time from a String to an int
     * @param end
     * 
     */
    public void setEnd(String end) {
        int intTime = convertTimeString(end);
        this.end = intTime;
    }

    /**
     * sets the room of the lecture
     * @param room room number that the lecture will take place in
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * sets the staff variable to the staff name passed as a parameter
     * @param staff name of member of staff taking the lecture, format = "surname: forename"
     */
    public void setStaff(String staff) {
        this.staff = staff;
    }

    /**
     * sets the length of the lecture
     * @param length denotes the length in minutes
     */
    public void setLength(int length){
        this.length = length;
    }

    /**
     * sets the pixel height of the rectangle representing the lecture
     * @param rectHeight
     */
    public void setRectHeight(double rectHeight) {
        this.rectHeight = rectHeight;
    }

    /**
     * sets the pixel width of the rectangle representing the lecture
     * @param rectWidth
     */
    public void setRectWidth(double rectWidth) {
        this.rectWidth = rectWidth;
    }

    /**
     * sets the Xaxis position of the rectangle in pixels, top left hand corner of rectangle
     * @param rectX
     */
    public void setRectX(double rectX) {
        this.rectX = rectX;
    }

    /**
     * sets the Yaxis position of the rectanle in pixels, top left hand corner of rectanlge
     * @param rectY
     */
    public void setRectY(double rectY) {
        this.rectY = rectY;
    }
    
    /**
     * when the data is parsed from the XML document using <code>SAX_Lecture</code> the times are in the format
     * "1899-12-30 10:45:00.0". SQL server puts the date in when it is queried from the datebase. convertTimeString
     * take the String in this format and trims out the unwanted values.
     * @param time
     * @return the time in format HHMM as an integer
     */
    public int convertTimeString(String time) {

        String a = time.substring(11, 13);//gets the hour
        String b = time.substring(14, 16);//gets the minutes
        String cont = a + b;//concatenates the two together
         int returnTime = Integer.parseInt(cont);

//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(Integer.parseInt(cont));
        return returnTime;
    }

  
    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        String NEW_LINE = System.getProperty("line.separator");

        sb.append("Lecture Details {" + NEW_LINE);
        sb.append(" Title:" + getTitle() + NEW_LINE);
        sb.append(" Weeks:" + getWeeks() + NEW_LINE);
        sb.append(" Day of Week:" + getDay_of_week() + NEW_LINE);
        sb.append(" Start:" + getStart() + NEW_LINE);
        sb.append(" End:" + getEnd() + NEW_LINE);
        sb.append(" Room:" + getRoom() + NEW_LINE);
        sb.append(" Staff:" + getStaff() + NEW_LINE);
        sb.append(" Length:" + getLength() + NEW_LINE);
        sb.append("}" + NEW_LINE);

        return sb.toString();
    }
} //End of Lecture Class
