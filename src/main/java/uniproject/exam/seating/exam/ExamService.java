package uniproject.exam.seating.exam;

import org.springframework.stereotype.Service;
import uniproject.exam.seating.invigilatorAssignment.InvigilatorAssignmentRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExamService {
    public ExamRepository examRepository;
    public InvigilatorAssignmentRepository assignmentRepo;

    public ExamService(ExamRepository examRepository, InvigilatorAssignmentRepository assignmentRepo) {
        this.examRepository = examRepository;
        this.assignmentRepo = assignmentRepo;
    }

    public List<Exam> findAllExam() {
        return examRepository.findAll();
    }

    public void addExam(Exam exam) {
        examRepository.save(exam);
    }

    public void deleteExam(String examId) {
        assignmentRepo.deleteAllByExam_ExamId(examId);
        examRepository.deleteById(examId);
    }

    public void updateExamById(String examId, String subject, LocalDate examDate, Exam.TimeOfDay examTime) {
        Exam newExam = examRepository.findById(examId).get();
        newExam.setSubject(subject);
        newExam.setExamDate(examDate);
        newExam.setExamTime(examTime);
        examRepository.save(newExam);
    }

    public void deleteAllExam() {
        assignmentRepo.deleteAll();
        examRepository.deleteAll();
    }
}
