package uniproject.exam.seating.student;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class studentController {

    private studentService studentService;

    public studentController(studentService studentService) {
        this.studentService = studentService;
    }

    @RequestMapping("student-list")
    public List<Student> goToStudent() {
        return studentService.findAllStudent();
    }

}
