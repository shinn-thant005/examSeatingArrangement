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

    @DeleteMapping("delete-student/{rollNo}")
    public String deleteStudent(@PathVariable String rollNo) {
        studentService.deleteStudentByRollNo(rollNo);
        return "Student with roll no " + rollNo + " deleted";
    }

    @PostMapping("/update-student")
    public String  updateStudent(@RequestBody Student student) {
        studentService.updateStudentByRollNo(student.getRollNo(), student.getName(), student.getMajorId());
        return "Student with roll no " + student.getRollNo() + " updated";
    }

    @PostMapping("/reset-all")
    public String resetAllStudent() {
        studentService.resetAllStudent();
        return "All students have been reset";
    }

    @PostMapping("/reset-room/{roomId}")
    public String resetRoom(@PathVariable Integer roomId) {
        studentService.resetRoomSelection(roomId);
        return "Assignments for Room ID " + roomId + " have been cleared.";
    }




}

