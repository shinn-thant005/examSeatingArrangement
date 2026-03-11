package uniproject.exam.seating.exam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("api/v1/exam")
public class examController {
    examService examService;

    public examController(examService examService) {
        this.examService = examService;
    }

    @GetMapping("list-exam")
    public List<Exam> listAllExam() {
        return examService.findAllExam();
    }
}
