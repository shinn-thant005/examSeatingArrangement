package uniproject.exam.seating.seating;

import org.springframework.web.bind.annotation.*;

@RestController
public class seatingController {
    seatingService seatingService;

    public seatingController(seatingService seatingService) {
        this.seatingService = seatingService;
    }


    @GetMapping("/generate-plan/{roomId}")
    public seatingService.SeatingPlanResponse generatePlan(@PathVariable Integer roomId) {
        return seatingService.generateSeatingPlan(roomId);
    }

    @GetMapping("/view-plan/{roomID}")
    public seatingService.SeatingPlanResponse viewPlan(@PathVariable Integer roomID) {
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
    public String updatePlan(@RequestBody Seating seating, @PathVariable Integer seatingId) {
        seatingService.updateSeatingPlan(seatingId, seating.getStudent().getMajorId(), seating.getRoom().getRoomId(), seating.getColumnNum(), seating.getRowNum());
        return "The seating plan with id " + seatingId + " has been updated.\n" +
                "New student: " + seating.getStudent().getMajorId() + "\n" +
                "New Room: " + seating.getRoom().getRoomId() + "\n" +
                "New Position: (" + seating.getRowNum() + ", " + seating.getColumnNum() + ")";

    }


}
