package uniproject.exam.seating.seating;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatingRepository extends JpaRepository<Seating, Integer> {
    // Fetch all seated students for a specific room
    List<Seating> findByRoom_RoomId(Integer roomId);

    // Delete all seating records for a specific room (Useful for your Reset button!)
    @Transactional
    void deleteByRoom_RoomId(Integer roomId);

    @Transactional
    void deleteBySeatingId(Integer seatingId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Seating s WHERE s.room.roomId = :roomId")
    void deleteAllByRoom_RoomId(Integer roomId);

    Optional<Seating> findByRoom_RoomIdAndRowNumAndColumnNum(Integer roomId, Integer rowNum, Integer columnNum);

    @Transactional
    void deleteByStudent_RollNo(String rollNo);
}
