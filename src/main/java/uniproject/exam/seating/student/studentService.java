package uniproject.exam.seating.student;

import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class studentService {
    private studentRepository studentRepository;

    public studentService(studentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAllStudent() {
        return studentRepository.findAll();
    }

    public void addStudent(String roll_no, String name, String major_id) {
        Student student = new Student(roll_no, name, major_id);
        studentRepository.save(student);
    }

    public void deleteStudentByRollNo(String roll_no) {
        studentRepository.deleteStudentByRollNo(roll_no);
    }

}
