package uniproject.exam.seating.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class studentService {
    private studentService studentService;

    public studentService(studentService studentService) {
        this.studentService = studentService;
    }

    public List<Student> findAllStudent() {
        return studentService.findAll();
    }

    public void addStudent(String roll_no, String name, String major_id) {
        Student student = new Student(roll_no, name, major_id);
        studentService.save(student);
    }

}
