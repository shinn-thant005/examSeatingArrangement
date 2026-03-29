package uniproject.exam.seating.invigilatorAssignment;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import uniproject.exam.seating.util.PdfGeneratorUtil;

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
    public String updateAssignment(@PathVariable Integer AssignmentId, @RequestBody UpdateAssignment updateAssignment) {
        invigilatorAssignmentService.updateAssignment(AssignmentId, updateAssignment.getRoomName(), updateAssignment.getInvigilatorName());
        return "Successfully updated the Assignment with ID: " + AssignmentId;
    }

    @GetMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadAssignmentPdf() {
        // 1. Fetch all assignments from the database
        List<InvigilatorAssignment> assignments = invigilatorAssignmentService.getAllAssignment();

        // 2. Pass the list to the utility method to generate the PDF bytes
        byte[] pdfBytes = PdfGeneratorUtil.generateAssignmentPdf(assignments);

        // 3. Return the response as a downloadable file
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Invigilator_Assignments.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/my-duties/{invigilatorName}")
    public ResponseEntity<?> viewMyDuties(@PathVariable String invigilatorName) {
        try {
            List<InvigilatorDutyResponse> duties = invigilatorAssignmentService.getInvigilatorDuties(invigilatorName);
            return ResponseEntity.ok(duties);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}