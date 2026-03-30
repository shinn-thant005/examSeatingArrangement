package uniproject.exam.seating.invigilatorAssignment;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uniproject.exam.seating.exam.Exam;
import uniproject.exam.seating.invigilator.Invigilator;

import java.time.LocalDate;
import java.util.List;

public interface InvigilatorAssignmentRepository extends JpaRepository<InvigilatorAssignment, Integer> {
    List<InvigilatorAssignment> findByExam_ExamId(Integer examId);

    List<InvigilatorAssignment> findByInvigilator_InvigilatorId(Integer invigilatorId);

    @Transactional
    void deleteByExam_ExamId(String examId);

    @Transactional
    @Modifying
    @Query("DELETE FROM InvigilatorAssignment ia WHERE ia. invigilator.invigilatorId = :invigilatorId")
    void deleteAllByInvigilator_InvigilatorId(Integer invigilatorId);

    @Transactional
    @Modifying
    @Query("DELETE FROM InvigilatorAssignment ia WHERE ia.exam.examId = :examId")
    void deleteAllByExam_ExamId(String examId);

    @Transactional
    @Modifying
    @Query("DELETE FROM InvigilatorAssignment ia WHERE ia.room.roomId = :roomId")
    void deleteAllByRoom_RoomId(Integer roomId);

    boolean existsByInvigilatorAndExam_ExamDateAndExam_ExamTimeAndAssignmentIdNot(
            Invigilator invigilator,
            LocalDate examDate,
            Exam.TimeOfDay examTime,            Integer assignmentId
    );

    // --- NEW: Finds all invigilators busy at a specific date and time ---
    @Query("SELECT ia.invigilator FROM InvigilatorAssignment ia WHERE ia.exam.examDate = :examDate AND ia.exam.examTime = :examTime")
    List<Invigilator> findBusyInvigilators(@Param("examDate") LocalDate examDate, @Param("examTime") Exam.TimeOfDay examTime);

    List<InvigilatorAssignment> findByInvigilator_InvigilatorName(String invigilatorName);
}

