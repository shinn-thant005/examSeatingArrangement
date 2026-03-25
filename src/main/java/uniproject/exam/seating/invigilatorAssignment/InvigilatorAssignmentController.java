package uniproject.exam.seating.invigilatorAssignment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvigilatorAssignmentController {
    InvigilatorAssignmentService invigilatorAssignmentService;

    public InvigilatorAssignmentController(InvigilatorAssignmentService invigilatorAssignmentService) {
        this.invigilatorAssignmentService = invigilatorAssignmentService;
    }

    @GetMapping("/list-assignment")
    public List<InvigilatorAssignment> getAllAssignment() {
        return invigilatorAssignmentService.getAllAssignment();
    }

}
