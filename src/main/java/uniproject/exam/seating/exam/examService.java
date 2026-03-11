package uniproject.exam.seating.exam;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class examService {
    public examRepository examRepository;

    public examService(examRepository examRepository) {
        this.examRepository = examRepository;
    }

    public List<Exam> findAllExam() {
        return examRepository.findAll();
    }

    public void addExam(Exam exam) {
        examRepository.save(exam);
    }

    public void deleteExam(Integer examId) {
        examRepository.deleteById(examId);
    }

    public void updateExamById(Integer examId, String subject, LocalDate examDate, Exam.TimeOfDay examTime) {
        Exam newExam = examRepository.findById(examId).get();
        newExam.setSubject(subject);
        newExam.setExamDate(examDate);
        newExam.setExamTime(examTime);
        examRepository.save(newExam);
    }

    public void deleteAllExam() {
        examRepository.deleteAll();
    }
}
