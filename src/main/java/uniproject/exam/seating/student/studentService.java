package uniproject.exam.seating.student;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class studentService {
    static List<Student> students = new ArrayList<>();

    private static int studentCount = 3;

    static {
        students.add(new Student("II CST-1", "Max Vesteppan", "CST"));
        students.add(new Student("II CST-2", "Lewis Hilminton", "CST"));
        students.add(new Student("II CST-3", "Mr Beast", "CST"));
        students.add(new Student("II CST-4", "Jake Paul", "CST"));
    }

    private void addStudent(String roll_no, String name, String major_id) {
        Student student = new Student(roll_no, name, major_id);
        students.add(student);
    }

}
