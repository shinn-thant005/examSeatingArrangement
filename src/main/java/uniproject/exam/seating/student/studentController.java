package uniproject.exam.seating.student;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static uniproject.exam.seating.student.studentService.students;

@RestController
public class studentController {

    @RequestMapping("student-list")
    public String goToStudent() {
        return students;
    }
}
