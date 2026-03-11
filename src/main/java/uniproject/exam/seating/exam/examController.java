package uniproject.exam.seating.exam;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/exam")
public class examController {
    examService examService;

    public examController(examService examService) {
        this.examService = examService;
    }

    @GetMapping("/list-exam")
    public List<Exam> listAllExam() {
        return examService.findAllExam();
    }

    @PostMapping("/add-exam")
    public String addExam(@RequestBody Exam exam) {
        examService.addExam(exam);
        return "success adding new Exam!";
    }

    @DeleteMapping("/delete-exam/{examId}")
    public String deleteExam(@PathVariable Integer examId) {
        examService.deleteExam(examId);
        return "success deleting Exam!";
    }

    @PutMapping("/update-exam/{examId}")
    public String updateExam(@PathVariable Integer examId, @RequestBody Exam exam) {
        examService.updateExamById(examId, exam.getSubject(), exam.getExamDate(), exam.getExamTime());
        return "success updating Exam!";
    }

    @DeleteMapping("/delete-all/exam")
    public String deleteAllExam() {
        examService.deleteAllExam();
        return "success deleting all Exam!";
    }


}
