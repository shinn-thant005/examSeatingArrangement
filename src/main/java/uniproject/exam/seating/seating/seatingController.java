package uniproject.exam.seating.seating;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
