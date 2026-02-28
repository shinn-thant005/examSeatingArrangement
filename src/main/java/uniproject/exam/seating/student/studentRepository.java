package uniproject.exam.seating.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Add this
import org.springframework.transaction.annotation.Transactional; // Add this
import java.util.List;

public interface studentRepository extends JpaRepository<Student, String> {
    public List<Student> findStudentByRollNo(String roll_no);

    @Transactional // Required for custom deletes
    @Modifying    // Required for data modification
    public void deleteStudentByRollNo(String roll_no);

    // Add this method inside the studentRepository interface
    public List<Student> findByIsSeatedFalse();
}