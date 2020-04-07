package model;




import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;


public class Student{

    private String firstName;
    private String lastName;
    private String album;
    private String avatar="";
    private int year;
    private List<String> courses;

    public Student(){}

    public Student(String firstName, String lastName, String album, int year, List<String> courses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
        this.year = year;
        this.courses = courses;
    }

    public Student(String firstName, String lastName, String album, int year) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.album = album;
        this.year = year;
        this.courses = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlbum() {
        return album;
    }

    public int getYear() {
        return year;
    }

    public String getAvatar() {return avatar;}

    public void setAvatar(String avatar) {this.avatar = avatar;};

    public void setYear(int year) {this.year = year;}

    public void setAlbum(String album) {
        this.album = album;
    }

    @XmlElementWrapper(name = "courses")
    @XmlElement(name = "course")
    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) { this.courses = courses; }

    public void addCourse(String course){
        courses.add(course);
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", album='" + album + '\'' +
                ", year='" + year + '\'' +
                ", courses=" + courses +
                '}';
    }

}