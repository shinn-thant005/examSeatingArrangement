package uniproject.exam.seating.invigilatorAssignment;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvigilatorAssignmentRepository extends JpaRepository<InvigilatorAssignment, Integer> {
    List<InvigilatorAssignment> findByExam_ExamId(Integer examId);

    List<InvigilatorAssignment> findByInvigilator_InvigilatorId(Integer invigilatorId);

    @Transactional
    void deleteByExam_ExamId(Integer examId);

    @Transactional
    @Modifying
    @Query("DELETE FROM InvigilatorAssignment ia WHERE ia.exam.examId = :examId")
    void deleteAllByExam_ExamId(Integer examId);
}
