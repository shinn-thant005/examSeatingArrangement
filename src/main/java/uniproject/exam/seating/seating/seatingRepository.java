package uniproject.exam.seating.seating;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uniproject.exam.seating.room.Room;
import uniproject.exam.seating.student.Student;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

public interface seatingRepository extends JpaRepository<Seating, Integer> {
    // Fetch all seated students for a specific room
    List<Seating> findByRoom_RoomId(Integer roomId);

    // Delete all seating records for a specific room (Useful for your Reset button!)
    @Transactional
    void deleteByRoom_RoomId(Integer roomId);

    @Transactional
    void deleteBySeatingId(Integer seatingId);

    // Replace your current deleteAllByRoom_RoomId with this:
    @Transactional
    @Modifying
    @Query("DELETE FROM Seating s WHERE s.room.roomId = :roomId")
    void deleteAllByRoom_RoomId(Integer roomId);

    Optional<Seating> findByRoom_RoomIdAndRowNumAndColumnNum(Integer roomId, Integer rowNum, Integer columnNum);

}
