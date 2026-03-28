package uniproject.exam.seating.seating;

import org.springframework.web.bind.annotation.*;
import uniproject.exam.seating.room.Room;
import uniproject.exam.seating.room.RoomService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import uniproject.exam.seating.util.PdfGeneratorUtil;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seating")
public class SeatingController {
    SeatingService seatingService;
    RoomService roomService;

    public SeatingController(SeatingService seatingService, RoomService roomService) {
        this.seatingService = seatingService;
        this.roomService = roomService;
    }

    @GetMapping("/generate-plan/{roomId}")
    public SeatingService.SeatingPlanResponse generatePlan(@PathVariable Integer roomId) {
        return seatingService.generateSeatingPlan(roomId);
    }

    @GetMapping("/list-seating-rooms")
    public List<Room> showSeatingRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/view-plan/{roomID}")
    public SeatingService.SeatingPlanResponse viewPlan(@PathVariable Integer roomID) {
        return seatingService.getSavedSeatingPlan(roomID);
    }

    @DeleteMapping("delete-plan/{seatingId}")
    public String  deletePlan(@PathVariable Integer seatingId) {
        seatingService.deleteSeatingPlan(seatingId);
        return "The seating plan with id " + seatingId + " has been removed.";
    }

    @DeleteMapping("delete-room-plan/{roomId}")
    public String deletePlanByRoomId(@PathVariable Integer roomId) {
        seatingService.deleteSeatingPlanByRoomId(roomId);
        return "The seating plan with id " + roomId + " has been deleted.";
    }

    @PutMapping("update-plan/{seatingId}")
    public String updatePlan(@RequestBody UpdateSeatingRequest seating, @PathVariable Integer seatingId) {
        seatingService.updateSeatingPlan(seatingId, seating.getRollNo(), seating.getRoomName(), seating.getRowNum(), seating.getColumnNum());
        return "The seating plan with id " + seatingId + " has been updated.\n" +
                "New student: " + seating.getRollNo() + "\n" +
                "New Room: " + seating.getRoomName() + "\n" +
                "New Position: (" + seating.getRowNum() + ", " + seating.getColumnNum() + ")";

    }

    @PostMapping("add-plan")
    public String addPlan(@RequestBody UpdateSeatingRequest newSeating) {
        seatingService.addSeatingPlan(newSeating.getRollNo(), newSeating.getRoomName(), newSeating.getRowNum(), newSeating.getColumnNum());
        return "New seating plan has been added!";
    }

    @GetMapping("/download-plan-pdf/{roomId}")
    public ResponseEntity<byte[]> downloadSeatingPlanPdf(@PathVariable Integer roomId) {
        // 1. Get the existing plan data
        SeatingService.SeatingPlanResponse plan = seatingService.getSavedSeatingPlan(roomId);

        // 2. Generate PDF bytes
        byte[] pdfBytes = PdfGeneratorUtil.generateSeatingGridPdf(plan);

        // 3. Return as a downloadable file
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SeatingPlan_" + plan.getRoomName() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
