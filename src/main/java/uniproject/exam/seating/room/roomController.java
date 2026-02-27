package uniproject.exam.seating.room;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class roomController {
    roomService roomService;

    public roomController(roomService roomService) {
        this.roomService = roomService;
    }

    @RequestMapping("/list-room")
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

    @PostMapping("/update-room/{roomId}")
    public String updateRoom(@PathVariable Integer roomId, @RequestBody Room room) {
        roomService.updateRoomById(roomId, room.getFloor(), room.getRoomName(), room.getTotalCapacity(), room.getColumnCapacity(), room.getRowCapacity());
    return "Room has been updated";
    }

}