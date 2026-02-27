package uniproject.exam.seating.room;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class roomService {

    public roomRepository roomRepository;

    public roomService(roomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public void addRoom(Room room) {
        roomRepository.save(room);
    }

    public void deleteRoomById(Integer roomId) {
        if (roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
        }
    }

    public void updateRoomById(Integer roomId, Integer floor, String roomName, Integer totalCapacity, Integer rowCapacity, Integer columnCapacity) {
        Room room = new Room(roomId, floor, roomName, totalCapacity, rowCapacity, columnCapacity);
        roomRepository.save(room);
    }

}
