package uniproject.exam.seating.invigilatorAssignment;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/invigilatorAssignment")
public class InvigilatorAssignmentController {

    private final InvigilatorAssignmentService invigilatorAssignmentService;

    public InvigilatorAssignmentController(InvigilatorAssignmentService invigilatorAssignmentService) {
        this.invigilatorAssignmentService = invigilatorAssignmentService;
    }

    @GetMapping("/list-assignment")
    public List<InvigilatorAssignment> listAllAssignment() {
        return invigilatorAssignmentService.getAllAssignment();
    }

    @PostMapping("/generate-assignment/{examId}")
    public String generateAssignmentPlan(@PathVariable Integer examId) {
        // Calls the Genetic Algorithm in your service
        invigilatorAssignmentService.generateAssignment(examId);

        return "Invigilator assignments have been successfully generated for Exam ID: " + examId;
    }
}