package uniproject.exam.seating.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface roomRepository extends JpaRepository<Room,Integer> {
    Optional<Room> findByRoomName(String roomName);
}
