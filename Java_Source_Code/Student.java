/**
 * A Student object stores information about the Student that has been parsed from the xmlFile.
 * There will only be one Student object existing in a running version of the applet.
 * All of the lectures stored in the ArrayList<Lectures> are unique to this student
 * @author David Hughes
 */
public class Student {

    private String studentID, firstName, lastName;
   private boolean anyLectures;
    /**
     * Instantiates a Student object empty
     */
    public Student(){
        
    }
    /**
     * Instantiates a Student object with variables assigned with parameters
     * @param studentID the studentID of the student that the timetable is showing
     */
    public Student(String studentID) {
        this.studentID = studentID.trim();
    }

//--------------------- Getter methods ------------------------------
    /**
     * returns the students firstname
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * returns the students surname
     * @return
     */
    public String getStudentID() {
        return studentID;
    }

    /**
     * returns the students lastname
     * @return
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * if anyLectures = true, there are lectures to display for this studentid, not necessarily on current week
     * if anyLectures = false, there are lectures to display for this studentid at any point
     * @return
     */
    public boolean getAnyLectures(){
        return anyLectures;
    }

    //------------------------ Setter methods ------------------------
    /**
     * sets the firstname variable to the parameter passed
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    /**
     * sets the surname variable to the parameter passed
     * @param studentID
     */
    public void setStudentID(String studentID) {
        this.studentID = studentID.trim();
    }

    /**
     * sets the surname to the parameter passed
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }
    
    /**
     * if anyLectures = true, there are lectures to display for this studentid, not necessarily on current week
     * if anyLectures = false, there are lectures to display for this studentid at any point
     * @param anyLectures 
     */
    public void setAnyLectures(boolean anyLectures){
        this.anyLectures = anyLectures;
    }
    
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        String NEW_LINE = System.getProperty("line.separator");

        sb.append("Student Details {" + NEW_LINE);
        sb.append(" StudentID:" + getStudentID() + NEW_LINE);
        sb.append(" FirstName:" + getFirstName() + NEW_LINE);
        sb.append(" LastName:" + getLastName() + NEW_LINE);
        sb.append("}" + NEW_LINE);

        return sb.toString();
    }
}//End of Student Class
 
