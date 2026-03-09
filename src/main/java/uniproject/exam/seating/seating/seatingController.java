package uniproject.exam.seating.seating;

import org.springframework.web.bind.annotation.*;
import uniproject.exam.seating.room.Room;
import uniproject.exam.seating.room.roomService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seating")
public class seatingController {
    seatingService seatingService;
    roomService roomService;

    public seatingController(seatingService seatingService,  roomService roomService) {
        this.seatingService = seatingService;
        this.roomService = roomService;
    }

    @GetMapping("/generate-plan/{roomId}")
    public seatingService.SeatingPlanResponse generatePlan(@PathVariable Integer roomId) {
        return seatingService.generateSeatingPlan(roomId);
    }

    @GetMapping("/list-seating-rooms")
    public List<Room> showSeatingRooms() {
        return roomService.getAllRooms();
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
    public String updatePlan(@RequestBody updateSeatingRequest seating, @PathVariable Integer seatingId) {
        seatingService.updateSeatingPlan(seatingId, seating.getRollNo(), seating.getRoomName(), seating.getRowNum(), seating.getColumnNum());
        return "The seating plan with id " + seatingId + " has been updated.\n" +
                "New student: " + seating.getRollNo() + "\n" +
                "New Room: " + seating.getRoomName() + "\n" +
                "New Position: (" + seating.getRowNum() + ", " + seating.getColumnNum() + ")";

    }

    @PostMapping("add-plan")
    public String addPlan(@RequestBody updateSeatingRequest newSeating) {
        seatingService.addSeatingPlan(newSeating.getRollNo(), newSeating.getRoomName(), newSeating.getRowNum(), newSeating.getColumnNum());
        return "New seating plan has been added!";
    }
}
