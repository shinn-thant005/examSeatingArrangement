package uniproject.exam.seating.seating;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface seatingRepository extends JpaRepository<Seating, Integer> {
    // Fetch all seated students for a specific room
    List<Seating> findByRoom_RoomId(Integer roomId);

    // Delete all seating records for a specific room (Useful for your Reset button!)
    @Transactional
    void deleteByRoom_RoomId(Integer roomId);


    void deleteBySeatingId(Integer seatingId);

    void deleteAllByRoom_RoomId(Integer roomId);
}
