package vt.access.workshop;

//Swing imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//Vt access imports
import com.vtaccess.Semester;
import com.vtaccess.exceptions.HokieSpaTimeoutException;
import com.vtaccess.exceptions.WrongLoginException;
import com.vtaccess.schedule.Course;


public class UI extends JFrame{

    //~Main Method-------------------------------------------
    public static void main(String [] args) {
        
        @SuppressWarnings("unused")
        UI run = new UI();
    }
    
    //~Constants----------------------------------------------
    private static final long serialVersionUID = -3991615760872986389L;
    private static final String SPRING = "spring";
    private static final String SUMMER1 = "summer1";
    private static final String SUMMER2 = "summer2";
    private static final String FALL = "fall";

    //~Data Fields--------------------------------------------
    //GUI fields
    private JButton openSession;
    private JButton switchSession;
    private JButton closeSession;
    
    private JButton grabCourses;
    private JButton grabAllCourses;
    private JButton grabPrerequisites;
    
    private JTextField semester1;
    private JTextField year1;
    
    private JLabel semesterLabel1;
    private JLabel yearLabel1;
    
    private JTextField semester2;
    private JTextField year2;
    
    private JLabel semesterLabel2;
    private JLabel yearLabel2;
    
    
    private JTextField username;
    private JPasswordField password;
    
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    
    
    private JTextField semester;
    private JTextField year;
    
    private JLabel semesterLabel;
    private JLabel yearLabel;
    
    
    private JTextField courseEntries; 
    
    private JTextArea curCoursesLbl;
    private JTextArea allCoursesLbl;
    
    private JLabel prerequisiteCoursesLbl;
    private JTextArea curPrerequisitesLbl;
    
    private JPanel pane;
    private JScrollPane scroll;
    
    //Non-GUI Fields
    private Scraping scraper;
    private List<Course> curCourses;
    private List<Course> allCourses;
    private List<Course> curPrerequisites;

    //~Constructors--------------------------------------------
    /**
     * Default constructor, initializes the pretty user interface.
     */
    public UI() {
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Official hokie colors
        Color maroon = new Color(102, 0, 0);
        Color orange = new Color(255, 102, 0);
        Color white = new Color(255, 255, 255);
        
        int textWidth = 500;
        int textHeight = 25;
        
        int minWidth = 500;
        int minHeight = 300;
        
        usernameLabel = new JLabel("Enter your username below:");
        usernameLabel.setForeground(orange);
        username = new JTextField();
        username.setMaximumSize(new Dimension(textWidth, textHeight));
        passwordLabel = new JLabel("Enter your password below:");
        passwordLabel.setForeground(orange);
        password = new JPasswordField();
        password.setMaximumSize(new Dimension(textWidth, textHeight));
        
        openSession = new JButton("Open HokieSpa Session");
        openSession.addActionListener(new ButtonListener());
        openSession.setActionCommand("openSession");
        openSession.setForeground(orange);
        openSession.setBackground(maroon);
        
        closeSession = new JButton("Close HokieSpa Session");
        closeSession.setActionCommand("closeSession");
        closeSession.addActionListener(new ButtonListener());
        closeSession.setForeground(orange);
        closeSession.setBackground(maroon);
        
        switchSession = new JButton("Switch HokieSpa Session");
        switchSession.setActionCommand("switchSession");
        switchSession.addActionListener(new ButtonListener());
        switchSession.setForeground(orange);
        switchSession.setBackground(maroon);
        
        
        semesterLabel = new JLabel("Enter the semester below: (fall, spring, summer1, or summer2)");
        semesterLabel.setForeground(orange);
        semester = new JTextField();
        semester.setMaximumSize(new Dimension(textWidth, textHeight));
        yearLabel = new JLabel("Enter the year below: ");
        yearLabel.setForeground(orange);
        year = new JTextField();
        year.setMaximumSize(new Dimension(textWidth, textHeight));
        
        
        grabCourses = new JButton("Get Courses");
        grabCourses.setActionCommand("grabCourses");
        grabCourses.addActionListener(new ButtonListener());
        grabCourses.setForeground(orange);
        grabCourses.setBackground(maroon);

        
        semesterLabel1 = new JLabel("Enter the first semester below: (fall, spring, summer1, or summer2)");
        semesterLabel1.setForeground(orange);
        semester1 = new JTextField();
        semester1.setMaximumSize(new Dimension(textWidth, textHeight));
        yearLabel1 = new JLabel("Enter the first year below: ");
        yearLabel1.setForeground(orange);
        year1 = new JTextField();
        year1.setMaximumSize(new Dimension(textWidth, textHeight));

        semesterLabel2 = new JLabel("Enter the second semester below: (fall, spring, summer1, or summer2)");
        semesterLabel2.setForeground(orange);
        semester2 = new JTextField();
        semester2.setMaximumSize(new Dimension(textWidth, textHeight));
        yearLabel2 = new JLabel("Enter the second year below: ");
        yearLabel2.setForeground(orange);
        year2 = new JTextField();
        year2.setMaximumSize(new Dimension(textWidth, textHeight));
        
        
        grabAllCourses = new JButton("Get All Courses Between...");
        grabAllCourses.setActionCommand("grabAllCourses");
        grabAllCourses.addActionListener(new ButtonListener());
        grabAllCourses.setForeground(orange);
        grabAllCourses.setBackground(maroon);
        
        
        grabPrerequisites = new JButton("Get Prerequisites for Courses");
        grabPrerequisites.setActionCommand("grabPrerequisites");
        grabPrerequisites.addActionListener(new ButtonListener());
        grabPrerequisites.setForeground(orange);
        grabPrerequisites.setBackground(maroon);
        
        prerequisiteCoursesLbl = new JLabel("Enter in a course to find prerequisites for in the format: " +
        		"XXXX NNNN; XXXX NNNN for example: (CS 2114; MATH 2534;CS 2505");
        prerequisiteCoursesLbl.setForeground(orange);
        prerequisiteCoursesLbl.setBackground(maroon);
        
        courseEntries = new JTextField();
        courseEntries.setMaximumSize(new Dimension(textWidth, textHeight));
        
        
        curCoursesLbl = new JTextArea("Your current courses will be listed here.");
        curCoursesLbl.setBackground(maroon);
        curCoursesLbl.setForeground(white);
        curCoursesLbl.setSize(new Dimension(minWidth, minHeight));
        curCoursesLbl.setMinimumSize(new Dimension(minWidth, minHeight));
        
        allCoursesLbl = new JTextArea("All of your courses between the specified range will be listed here.");
        allCoursesLbl.setBackground(maroon);
        allCoursesLbl.setForeground(white);
        allCoursesLbl.setMinimumSize(new Dimension(minWidth, minHeight));
        allCoursesLbl.setSize(new Dimension(minWidth, minHeight));
        
        curPrerequisitesLbl = new JTextArea("All of the courses that are prerequisites for the entered courses are listed here.");
        curPrerequisitesLbl.setBackground(maroon);
        curPrerequisitesLbl.setForeground(white);
        curPrerequisitesLbl.setMinimumSize(new Dimension(minWidth, minHeight));
        curPrerequisitesLbl.setSize(new Dimension(minWidth, minHeight));
        
        
        int smallCompSpacing = 50;
        int largeCompSpacing = 100;

        pane = new JPanel();
        
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(usernameLabel);
        pane.add(username);
        pane.add(passwordLabel);
        pane.add(password);
        pane.add(openSession);
        pane.add(switchSession);
        pane.add(closeSession);
        pane.add(Box.createVerticalStrut(smallCompSpacing));
        
        pane.add(semesterLabel);
        pane.add(semester);
        pane.add(yearLabel);
        pane.add(year);
        pane.add(grabCourses);
        pane.add(curCoursesLbl);
        pane.add(Box.createVerticalStrut(largeCompSpacing));
        
        pane.add(semesterLabel1);
        pane.add(semester1);
        pane.add(yearLabel1);
        pane.add(year1);
        pane.add(semesterLabel2);
        pane.add(semester2);
        pane.add(yearLabel2);
        pane.add(year2);
        pane.add(grabAllCourses);
        pane.add(allCoursesLbl);
        pane.add(Box.createVerticalStrut(largeCompSpacing));
        
        pane.add(prerequisiteCoursesLbl);
        pane.add(courseEntries);
        pane.add(grabPrerequisites);
        pane.add(curPrerequisitesLbl);
        pane.add(Box.createVerticalStrut(largeCompSpacing));

        pane.setBackground(maroon);
        pane.setForeground(orange);
        
        scroll = new JScrollPane(pane);
        
        this.setTitle("VT Access Demo");
        this.setVisible(true);
        this.setSize(600, 750);
        this.add(scroll);
        
        //NON-GUI
        curCourses = null;
        allCourses = null;
        curPrerequisites = null;
    }

    //~Methods-------------------------------------------------   
    /**
     * Takes the string from courseEntries JTextField and parses it into a List of Strings
     * 
     * @return
     */
    private List<String[]> getCourseEntries() {
        
        List<String[]> list = new LinkedList<String[]>();
        
        String courseCodes = courseEntries.getText();
        StringBuilder temp = new StringBuilder();
        if (courseCodes.length() > 0) {
            
            String[] container = new String[2];
            
            for (int i = 0; i < courseCodes.length() + 1; i++) {
                
                //add the subjectCode to the container array
                if (courseCodes.length() > i && courseCodes.charAt(i) == ' ') {

                    container[0] = temp.toString();
                    temp = new StringBuilder();
                }
                //add text to the string builder
                else if (courseCodes.length() > i && courseCodes.charAt(i) != ';') {
                    
                    temp.append(courseCodes.charAt(i));
                }
                //add the container object with subjectCode and courseNumber to the list.
                else {

                    container[1] = temp.toString();
                    list.add(container);
                    temp = new StringBuilder();
                    container = new String[2];
                }
            }
        }
        
        return list;
    }
    
    
    //~Sub-Classes----------------------------------------------------------------//
    /**
     * ActionListener for the Buttons.
     * 
     * @author Ethan Gaebel (egaebel)
     *
     */
    public class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            String action = event.getActionCommand();
            
            //openSession pressed
            if (action.equals("openSession")) {
                
                try {
                    scraper = new Scraping(username.getText().trim().toCharArray(), 
                            password.getPassword());
                }
                catch (WrongLoginException e) {
                    e.printStackTrace();
                    wrongLoginError();
                }
                catch (HokieSpaTimeoutException e) {
                    e.printStackTrace();
                    hokieSpaTimeoutError();
                }
            }
            //switchSession pressed
            else if (action.equals("switchSession")) {
                if (scraper != null ) {
                    try {
                        scraper.changeUserSession(username.getText().trim().toCharArray(), 
                                password.getPassword());
                    }
                    catch (WrongLoginException e) {
                        e.printStackTrace();
                        wrongLoginError();
                    }
                }
                else {    
                    scraperNullError();
                }
            }
            //closeSession pressed
            else if (action.equals("closeSession")) {
                if (scraper != null) {
                    scraper.killSession();
                }
                else {    
                    scraperNullError();
                }
            }
            //grabCourses pressed
            else if (action.equals("grabCourses")) {
                   
                if (scraper != null) {
                    
                    //check that semester and year contain input
                    if (noErrors(semesterToCode(semester.getText()), year.getText())) {
                    
                        //display courses
                        StringBuilder build = new StringBuilder();
                        try {
                            curCourses = scraper.getCourses(year.getText().trim() + semesterToCode(semester.getText().trim()));
                        }
                        catch (HokieSpaTimeoutException e) {
                            e.printStackTrace();
                            hokieSpaTimeoutError();
                        }
                        
                        for (Course course : curCourses) {
                            
                            build.append(course.toString());
                        }
                        
                        
                        curCoursesLbl.setText(build.toString());
                    }
                }
                else {
                    
                    scraperNullError();
                }
            }
            else if (action.equals("grabAllCourses")) {
                
                if (scraper != null) {
                    if (noErrors(semesterToCode(semester1.getText().trim()), year1.getText()) 
                            && noErrors(semesterToCode(semester2.getText().trim()), year1.getText())) {
                        
                        //check that start year/semester is less than finish
                        if (Integer.parseInt(year1.getText().trim() + semesterToCode(semester1.getText().trim())) 
                                <= Integer.parseInt(year2.getText().trim() + semesterToCode(semester2.getText().trim()))) {
                            
                            //display courses
                            StringBuilder build = new StringBuilder();
                            
                            try {
                                allCourses = scraper.getAllCourses(year1.getText().trim() + semesterToCode(semester1.getText().trim()), 
                                        year2.getText().trim() + semesterToCode(semester2.getText().trim()));
                            }
                            catch (HokieSpaTimeoutException e) {
                                e.printStackTrace();
                                hokieSpaTimeoutError();
                            }
                            
                            if (allCourses != null) {
                                
                                for (Course course : allCourses) {
                                    
                                    build.append(course.toString());
                                }
                                
                                allCoursesLbl.setText(build.toString());
                            }
                        }
                    }
                }
                else {    
                    scraperNullError();
                }
            }
            else if (action.equals("grabPrerequisites")) {

                //display courses
                StringBuilder build = new StringBuilder();
                List<String[]> coursesEntered = getCourseEntries();
                
                if (coursesEntered != null) {

                    curPrerequisites = Scraping.getPrerequisiteCourses(coursesEntered);
                    
                    for (Course course : curPrerequisites) {
                        
                        if (course.getName().equals("PLACEHOLDER")) {
                         
                            build.append("\n\nPrerequisites for: " + course.getSubjectCode() + " " + course.getCourseNumber() + "\n");
                        }
                        else if (course.getName().equals("ERROR")) {
                            
                            build.append("Either the course or subjectCode for the course entered was null\n");
                        }
                        else {
                            build.append(course.toString());
                        }
                    }
                    
                    curPrerequisitesLbl.setText(build.toString());
                }
                else {
                    
                    JOptionPane.showMessageDialog(null, "You must enter in some courses to find prerequisites for!");
                }
            }
        } 
        
        /**
         * Checks input fields for errors. Prints errors if there are any.
         * 
         * @return true if no errors, false if there are.
         */
        private boolean noErrors(String semester, String year) {
            
            //check that semester and year contain valid input
            if (Semester.isSemesterCode(year + semester)) {
                
                return true;
            }
            else {
                
                JOptionPane.showMessageDialog(null, "Either your year or semester input is invalid, please re-enter it and try again", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            return false;
        }
    }
    
    private String semesterToCode(String semesterString) {
        
        String returnString = "invalid";
        if (FALL.equalsIgnoreCase(semesterString.trim())) {
            returnString = "09";
        }
        else if (SPRING.equalsIgnoreCase(semesterString.trim())) {
            returnString = "01";
        }
        else if (SUMMER1.equalsIgnoreCase(semesterString.trim())) {
            returnString = "06";
        }
        else if (SUMMER2.equalsIgnoreCase(semesterString.trim())) {
            returnString = "07";
        }

        return returnString;
    }
    
    private void scraperNullError() {
        
        JOptionPane.showMessageDialog(null, "You have to open a session with HokieSpa before you do that!");
    }
    
    private void hokieSpaTimeoutError() {
        
        JOptionPane.showMessageDialog(null, "Your HokieSpa session has timed out, OR you do not have an internet connection. Please open one (in either case)!!");
    }
    
    private void wrongLoginError() {
        
        JOptionPane.showMessageDialog(null, "Your username or password was incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
