package uniproject.exam.seating.student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface studentRepository extends JpaRepository<Student, String> {
    public List<Student> findStudentByRollNo(String roll_no);

    public void deleteStudentByRollNo(String roll_no);
}
