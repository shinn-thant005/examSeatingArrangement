package uniproject.exam.seating.student;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class studentController {

    studentService studentService;

    public studentController(studentService studentService) {
        this.studentService = studentService;
    }

    @RequestMapping("/list-student")
    public List<Student> listStudent(){
        return studentService.findAllStudent();
    }


    @PostMapping("/add-student")
    public String addStudent(@RequestBody Student student){
        studentService.addStudent(student.getRollNo(), student.getName(), student.getMajorId());
        return "success adding student";
    }

    @DeleteMapping("delete-student/{roll_no}")
    public String deleteStudent(@PathVariable String roll_no) {
        studentService.deleteStudentByRollNo(roll_no);
        return "Student with roll no " + roll_no + " deleted";
    }

    @PostMapping("/update-student")
    public String  updateStudent(@RequestBody Student student) {
        studentService.updateStudentByRollNo(student.getRollNo(), student.getName(), student.getMajorId());
        return "Student with roll no " + student.getRollNo() + " updated";
    }
}

