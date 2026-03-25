package uniproject.exam.seating.invigilatorAssignment;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvigilatorAssignmentRepository extends JpaRepository<InvigilatorAssignment, Integer> {
    List<InvigilatorAssignment> findByExam_ExamId(Integer examId);

    List<InvigilatorAssignment> findByInvigilator_InvigilatorId(Integer invigilatorId);

    @Transactional
    void deleteByExam_ExamId(Integer examId);
}
