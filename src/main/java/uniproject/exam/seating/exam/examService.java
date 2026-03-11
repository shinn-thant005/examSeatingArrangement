package uniproject.exam.seating.exam;

import org.springframework.stereotype.Service;

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
}
