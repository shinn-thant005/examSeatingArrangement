package uniproject.exam.seating.invigilatorAssignment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvigilatorAssignmentService {
    InvigilatorAssignmentRepository invigilatorAssignmentRepository;

    public InvigilatorAssignmentService(InvigilatorAssignmentRepository invigilatorAssignmentRepository) {
        this.invigilatorAssignmentRepository = invigilatorAssignmentRepository;
    }

    public List<InvigilatorAssignment> getAllAssignment() {
        return invigilatorAssignmentRepository.findAll();
    }

    public List<InvigilatorAssignment> generateAssignment() {
    }
}
