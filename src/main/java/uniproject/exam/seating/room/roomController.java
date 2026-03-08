package uniproject.exam.seating.room;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
public class roomController {
    roomService roomService;

    public roomController(roomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/list-room")
    public List<Room> listAllRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping("/add-room")
    public String addRoom(@RequestBody Room room) {
        roomService.addRoom(room);
        return "A new room has been added";
    }

    @DeleteMapping("/delete-room/{roomId}")
    public String deleteRoom(@PathVariable Integer roomId) {
        roomService.deleteRoomById(roomId);
        return "Room has been deleted";
    }

    @PutMapping("/update-room/{roomId}")
    public String updateRoom(@PathVariable Integer roomId, @RequestBody Room room) {
        roomService.updateRoomById(roomId, room.getFloor(), room.getRoomName(), room.getColumnCapacity(), room.getRowCapacity(), room.getNumOfInvigilators(), room.getMaxMajor());
    return "Room has been updated";
    }

}