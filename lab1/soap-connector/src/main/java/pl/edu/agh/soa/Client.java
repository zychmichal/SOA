package pl.edu.agh.soa;

import javax.xml.ws.BindingProvider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Client {
    public static void main(String[] args){
        StudentServerService studentServerService = new StudentServerService();
        StudentServer studentServer = studentServerService.getStudentServerPort();
        setAuthorize((BindingProvider) studentServer, "user", "password");
        printAllStudentsLastName(studentServer);
        System.out.println("*****************");
        addCourse(studentServer,"2345");
        System.out.println("*****************");
        addStudentChecker(studentServer);
        System.out.println("*****************");
        decodeAvatar(studentServer);

    }


    private static void setAuthorize(BindingProvider bindingProvider, String user, String password){
        bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user);
        bindingProvider.getRequestContext().put(BindingProvider. PASSWORD_PROPERTY, password);
    }

    private static void addCourse(StudentServer studentServer, String album){
        System.out.println("Before Add Courses:");
        System.out.println(studentServer.getStudentByAlbum(album).getCourses().getCourse().toString());
        System.out.println("After Add Course:");
        System.out.println(studentServer.addCourse(album, "Bazy danych").getCourses().getCourse());
        for (String course: studentServer.addCourse(album, "Bazy danych").getCourses().getCourse()) {
            System.out.println(course);
        }
    }


    private static void printAllStudentsLastName(StudentServer studentServer){
        System.out.println("Last name of all students");
        for (pl.edu.agh.soa.Student student: studentServer.getAllStudents().getStudent()) {
            System.out.println(student.getLastName());
        }
    }

    private static void decodeAvatar(StudentServer studentServer){
        String encode = studentServer.getAvatarOfStudent();
        byte[] decodedImg = Base64.getDecoder().decode(encode.getBytes(StandardCharsets.UTF_8));
        Path destinationFile = Paths.get("responseImage.jpg");
        System.out.println("Path to image");
        System.out.println(destinationFile.toAbsolutePath());
        try {
            Files.write(destinationFile, decodedImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addStudentChecker(StudentServer studentServer){
        System.out.println("Size of all students: ");
        System.out.println(studentServer.getAllStudents().getStudent().size());
        System.out.println("Try to add student with exist album: ");
        System.out.println(studentServer.addStudent("Ms", "Ln", "2345", 3));
        System.out.println("Try to add student with non-exist album: ");
        System.out.println(studentServer.addStudent("Ms", "Ln", "234532", 3));
    }
}
