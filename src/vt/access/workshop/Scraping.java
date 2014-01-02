package vt.access.workshop;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.vtaccess.Cas;
import com.vtaccess.CourseInfo;
import com.vtaccess.ScheduleScraper;
import com.vtaccess.Semester;
import com.vtaccess.exceptions.HokieSpaTimeoutException;
import com.vtaccess.exceptions.WrongLoginException;
import com.vtaccess.schedule.Course;
import com.vtaccess.schedule.Schedule;

/**
 * Class used to scrape course information.
 * 
 * @author Ethan Gaebel (egaebel)
 *
 */
public class Scraping {

    //~Constants----------------------------------------------

    //~Data Fields--------------------------------------------
    /**
     * ScheduleScraper object that contains an active Cas Session and allows
     * the user to scrape their schedule and exam schedule.
     */
    private ScheduleScraper scrape;
    /**
     * Cas session that is to be initialized and given to the ScheduleScraper class.
     */
    private Cas cas;
    /**
     * The path on which the SSL certificate is to be saved.
     */
    private String filePath;

    //~Constructors--------------------------------------------
    public Scraping(char[] username, char[] password) throws WrongLoginException, HokieSpaTimeoutException {
        
        String filePath = System.getProperty("user.dir");
        
        cas = new Cas(username, password, filePath);
        scrape = new ScheduleScraper(cas);
    }
    
    //~Methods-------------------------------------------------
    /**
     * Switches the Cas session and ScheduleScraper object session to one
     * for the user with the passed in username and password.
     * 
     * @param username the username of the user whose session is being switched to.
     * @param password the password of the user whose session is being switched to
     * @throws WrongLoginException 
     */
    public void changeUserSession(char[] username, char[] password) throws WrongLoginException {
     
        cas.switchUsers(username, password, filePath);
    }
    
    /**
     * Gets the courses for the passed in semestercode.
     * 
     * NOTE: A semestercode is of the format: YYYYMM
     *       where MM is the month a semester begins in:
     *          e.g. 09 for Fall, 01 for Spring, 05 for summer 1, 07 for summer 2
     * 
     * @param semesterCode the semestercode for the semester courses are desired from.
     * @return List of Courses.
     * @throws HokieSpaTimeoutException 
     */
    public List<Course> getCourses(String semesterCode) throws HokieSpaTimeoutException {

        List<Course> courses = new LinkedList<Course>();
        Schedule schedule = new Schedule();
        
        if (scrape.retrieveSchedule(schedule, semesterCode)) {

            courses = schedule.getAllCourses();
        }

        return courses;
    }
    
    /**
     * Gets every course you've taken at Virginia Tech between startSemesterCode and endSemesterCode.
     * 
     * NOTE: A semestercode is of the format: YYYYMM
     *       where MM is the month a semester begins in:
     *          e.g. 09 for Fall, 01 for Spring, 05 for summer 1, 07 for summer 2
     *           
     * @param startSemesterCode the semesterCode for the first semester.
     * @param endSemesterCode the semesterCode for the end semester.
     * @return List of Course objects that are all of the courses taken between startSemesterCode and endSemesterCode
     *          by the user who is currently engaged in the hokiespa session.
     * @throws HokieSpaTimeoutException 
     */
    public List<Course> getAllCourses(String startSemesterCode, String endSemesterCode) throws HokieSpaTimeoutException {
        
        List<Course> courses = new LinkedList<Course>();
        Schedule schedule = new Schedule();
        String curSemesterCode = startSemesterCode;
        
        //Loop through all semesters in range
        while (true) {
            //Scrape schedule for curSemester
            scrape.retrieveSchedule(schedule, curSemesterCode);
            courses.addAll(schedule.getAllCourses());
            schedule = new Schedule();
            
            //break out condition
            if (curSemesterCode.equals(endSemesterCode)) {
                
                break;
            }
            else {
                //Increment curSemester
                curSemesterCode = Semester.nextSemesterCode(curSemesterCode);
            }
        }
        
        return courses;
    }

    /**
     * Gets the courses offerred in the passed in semesterCode that have been made available for
     * you to take via the passed in curCourses list.
     * 
     *  i.e. CS 1114 is a pre-req for CS 2114, so if CS 1114 is in the curCourses list, it will be 
     *       in the returned List.
     * 
     * NOTE: A semestercode is of the format: YYYYMM
     *       where MM is the month a semester begins in:
     *          e.g. 09 for Fall, 01 for Spring, 05 for summer 1, 07 for summer 2
     * 
     * @param semesterCode the semestercode for the semester courses are desired from.
     * @param courseCodes the specific courseCodes for the courses you want pre-reqs for, in 2 element string arrays. 
     *          (ex. {CS, 3114}; {CS, 2114})
     * @return List of Courses.
     */
    public static List<Course> getPrerequisiteCourses(List<String[]> courseCodes) {

        List<Course> courses = new LinkedList<Course>();
        List<Course> prereqs = null;
        Course tempCourse;
        
        //Loop over all current courses, adding all post reqs to the courses
        for (String[] course : courseCodes) {

            tempCourse = new Course();
            tempCourse.setSubjectCode(course[0]);
            tempCourse.setCourseNumber(course[1]);
            tempCourse.setName("PLACEHOLDER");
            courses.add(tempCourse);
            
            if (course[0] == null || course[1] == null) {
            
                tempCourse = new Course();
                tempCourse.setName("ERROR");
                courses.add(tempCourse);    
            }
            else {
                
                prereqs = CourseInfo.getPrerequisites(course[0].toUpperCase(), course[1]);
            }

            if (prereqs != null) {
                courses.addAll(prereqs);
            }
        }
        
        //Eliminate duplicates
        Set<Course> set = new HashSet<Course>();
        for (Course course : courses) {
            
            if (!set.contains(course)) {
                set.add(course);
            }
            else {
                courses.remove(course);
            }
        }
        
        return courses;
    }
    
    /**
     * Kills the current hokie spa session and wipes the user data from memory.
     */
    public void killSession() {
        
        scrape.closeSession();
    }
}