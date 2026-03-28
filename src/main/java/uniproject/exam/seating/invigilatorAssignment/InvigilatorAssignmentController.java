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
        invigilatorAssignmentService.generateAssignment(examId);

        return "Invigilator assignments have been successfully generated for Exam ID: " + examId;
    }

    @DeleteMapping("/delete-assignment/{AssignmentId}")
    public String deleteAssignment(@PathVariable Integer AssignmentId) {
        invigilatorAssignmentService.deleteAssignmentById(AssignmentId);
        return "Assignment with Id " + AssignmentId + " has been deleted successfully.";
    }

    @DeleteMapping("/delete-room-assignment/{examId}")
    public String deleteRoomAssignment(@PathVariable Integer examId) {
        invigilatorAssignmentService.deleteAllAssignmentByExam(examId);
        return "Assignment with Exam Id " + examId + " has been deleted successfully.";
    }

    @PutMapping("update-assignment/{AssignmentId}")
    public String updateAssignment(@PathVariable Integer AssignmentId, @RequestBody updateAssignment updateAssignment) {
        invigilatorAssignmentService.updateAssignment(AssignmentId, updateAssignment.getRoomName(), updateAssignment.getInvigilatorName());
        return "Successfully updated the Assignment with ID: " + AssignmentId;
    }

}