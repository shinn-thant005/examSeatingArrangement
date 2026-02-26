package uniproject.exam.seating.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class studentController {

    @RequestMapping("list-student")
    public List<Student> listStudent(){
        List<Student> students = new ArrayList<>();
        return students;
    }
}
