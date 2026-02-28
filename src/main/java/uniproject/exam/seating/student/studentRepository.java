package uniproject.exam.seating.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Add this
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional; // Add this
import java.util.List;

public interface studentRepository extends JpaRepository<Student, String> {
    public List<Student> findByIsSeatedFalse();

    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.isSeated = false, s.assignedRoom = null")
    void resetAllStudent();

    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.isSeated = false, s.assignedRoom = null WHERE s.assignedRoom.roomId = :roomId")
    void resetStudentByRoom(Integer roomId);
}