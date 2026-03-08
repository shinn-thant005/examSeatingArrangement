package uniproject.exam.seating.room;

import org.springframework.stereotype.Service;
import uniproject.exam.seating.seating.seatingRepository;
import uniproject.exam.seating.student.studentRepository;

import java.util.List;

@Service
public class roomService {
    public roomService(studentRepository studentRepository, seatingRepository seatingRepository, roomRepository roomRepository) {
        this.studentRepository = studentRepository;
        this.seatingRepository = seatingRepository;
        this.roomRepository = roomRepository;
    }

    public studentRepository studentRepository;
    public seatingRepository seatingRepository;
    public roomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public void addRoom(Room room) {
        int calculatedTotal = room.getRowCapacity() * room.getColumnCapacity();
        room.setTotalCapacity(calculatedTotal);
        roomRepository.save(room);
    }

    public void deleteRoomById(Integer roomId) {
        if (roomRepository.existsById(roomId)) {
            studentRepository.resetStudentByRoom(roomId);
            seatingRepository.deleteAllByRoom_RoomId(roomId);
            roomRepository.deleteById(roomId);
        }

    }

    public void updateRoomById(Integer roomId, Integer floor, String roomName, Integer rowCapacity, Integer columnCapacity) {
        Room room = new Room(roomId, floor, roomName, rowCapacity * columnCapacity, rowCapacity, columnCapacity);
        roomRepository.save(room);
    }
}
