package pl.edu.agh.soa;
import model.Student;


import org.jboss.annotation.security.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

import org.apache.commons.io.IOUtils;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Stateless
@WebService
@SecurityDomain("my-sec-domain")
@DeclareRoles({"other"})
@WebContext(contextRoot="/soap-api", urlPattern="/StudentServer", authMethod="BASIC",transportGuarantee="NONE", secureWSDLAccess = false)

public class StudentServer {


    @RolesAllowed("other")
    @WebMethod(action = "get")
    @WebResult(name = "gSBN")
    public Student getStudentByAlbum(@WebParam(name = "album") String album){
        //return cleanAvatarToBetterDisplay(this.getStudentByAlbumPrivate(album));
        List<Student> allStudents = this.getAllStudentsPrivate();
        return getStudentByAlbumPrivate(allStudents, album);

    }

    @RolesAllowed("other")
    @WebMethod(action = "get")
    @WebResult(name = "gAll")
    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    public List<Student> getAllStudents(){

        return this.getAllStudentsPrivate();

    }

    @RolesAllowed("other")
    @WebMethod(action = "get")
    @WebResult(name = "gSBY")
    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    public List<Student> getStudentsByYear(@WebParam(name = "year") int year){

        List<Student> students = this.getAllStudentsPrivate();
        return students
                .stream()
                .filter(student -> student.getYear() == year)
                .collect(Collectors.toList());

    }


    @RolesAllowed("other")
    @WebMethod(action = "get")
    @WebResult(name = "gSBRN")
    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    public List<Student> getStudentByFirstLetterLastName(@WebParam(name = "letter") String letter){

        if(letter.length()>1){
            return null;
        }

        List<Student> students = this.getAllStudentsPrivate();
        return students
                .stream()
                .filter(student -> student.getLastName().charAt(0)==letter.charAt(0))
                .collect(Collectors.toList());

    }

    @RolesAllowed("other")
    @WebMethod(action = "get")
    @WebResult(name = "gAOF")
    public String getAvatarOfStudent(){

        String encode = null;
        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("/avatar.jpg");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            encode = Base64.getEncoder().encodeToString(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encode;

    }

    @RolesAllowed("other")
    @WebMethod(action = "add")
    @WebResult(name = "aS")
    public String addStudent(@WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName,
                              @WebParam(name = "album") String album, @WebParam(name = "year") int year){
        List<Student> allStudents = this.getAllStudentsPrivate();
        if(getStudentByAlbumPrivate(allStudents, album)!=null){
            return "Student with this album exist";
        }else{
            allStudents.add(new Student(firstName, lastName, album, year));
            return "Add new student, now is " + Integer.toString(allStudents.size()) + " students";
        }

    }



    @RolesAllowed("other")
    @WebMethod(action = "add")
    @WebResult(name = "aOCA")
    public Student addCourse(@WebParam(name = "album") String album, @WebParam(name = "course") String course){

        //Student student = getStudentByAlbumPrivate(album);
        List<Student> allStudents = this.getAllStudentsPrivate();
        Student student = getStudentByAlbumPrivate(allStudents, album);
        if(student!=null){
            student.addCourse(course);
        }else {
            return null;
        }
        return student;

    }


    @RolesAllowed("other")
    @WebMethod(action = "add")
    @WebResult(name = "aOCA")
    public Student editStudentYear(@WebParam(name = "album") String album, @WebParam(name = "year") int year){

        //Student student = getStudentByAlbumPrivate(album);
        List<Student> allStudents = this.getAllStudentsPrivate();
        Student student = getStudentByAlbumPrivate(allStudents, album);
        if(student!=null){
            student.setYear(year);
        }else {
            return null;
        }
        return student;

    }


    private Student getStudentByAlbumPrivate(List<Student> students, String album){
        for (Student student: students) {
            if(student.getAlbum().equals(album)){
                return student;
            }
        }
        return null;
    }



    private Student cleanAvatarToBetterDisplay(Student student){
        if(student!=null){
            student.setAvatar("Not reading image here");
        }
        return student;
    }

    private List<Student> getAllStudentsPrivate(){
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("M1","L1", "2345", 2));
        students.add(new Student("M1","L2", "2346", 2));
        students.add(new Student("M1","L3", "2347", 2));
        students.add(new Student("M1","L4", "2348", 2));
        students.add(new Student("M1","L5", "2349", 2));
        students.add(new Student("M1","L6", "2340", 3));
        students.add(new Student("M1","G1", "2355", 3));
        students.add(new Student("M1","G2", "2215", 4));
        students.add(new Student("M1","G3", "2365", 3));
        students.add(new Student("M1","G4", "2375", 3));
        return students;

    }


    //Methods to do when implement file state serializing

    /*@RolesAllowed("other")
    @WebMethod(action = "add")
    @WebResult(name = "aOCA")
    public String addOrChangeAvatar(@WebParam(name = "album") String album, @WebParam(name = "avatar") String avatar){

        Student student = getStudentByAlbumPrivate(album);
        if(student!=null){
            student.setAvatar(avatar);
        }else {
            return "No student found";
        }
        return this.addStudentToFile(student);
    }*/


    /*private String addStudentToFile(Student student){
        try {
            File file = new File(student.getAlbum());
            file.createNewFile();
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(student);
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return student.toString();
            //return "Error, File not found";
        } catch (IOException e) {
            System.out.println("Error initializing stream");
            return student.toString();
            //return "Error, Error initializing stream";
        }
        return "OK";

    }*/


    /*private Student getStudentByAlbumPrivate(String album){
        Student student = null;
        try {
            FileInputStream fi = new FileInputStream(new File(album));
            ObjectInputStream oi = new ObjectInputStream(fi);

            student = (Student) oi.readObject();

            oi.close();
            fi.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return student;
    }*/

    /*private boolean appendAlbumToFile(String album){
        try {
            File file = new File("albums.txt");
            file.createNewFile();
            Writer output = new BufferedWriter(new FileWriter(file));
            output.append(album);
            output.close();
        }catch (IOException e) {
            System.out.println("Error initializing stream");
            return true;
        }
        return true;
    }*/


    /*@RolesAllowed("other")
    @WebMethod(action = "add")
    @WebResult(name = "aS")
    public String addStudent(@WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName,
                              @WebParam(name = "album") String album, @WebParam(name = "year") int year){

        if(this.appendAlbumToFile(album)){
            Student student = new Student(firstName, lastName, album, year);
            return this.addStudentToFile(student);
        }else{
            return "Error";
        }

    }*/



}
