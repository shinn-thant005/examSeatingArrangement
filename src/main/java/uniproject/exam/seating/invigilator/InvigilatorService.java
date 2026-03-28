package uniproject.exam.seating.invigilator;

import org.springframework.stereotype.Service;
import uniproject.exam.seating.invigilatorAssignment.InvigilatorAssignmentRepository;

import java.util.List;

@Service
public class InvigilatorService {
    InvigilatorRepository invigilatorRepository;
    InvigilatorAssignmentRepository assignmentRepository;

    public InvigilatorService(InvigilatorRepository invigilatorRepository, InvigilatorAssignmentRepository assignmentRepository) {
        this.invigilatorRepository = invigilatorRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public List<Invigilator> findAllInvigilator() {
        return invigilatorRepository.findAll();
    }

    public void addInvigilator(Invigilator invigilator) {
        invigilatorRepository.save(invigilator);
    }

    public void deleteInvigilator(Integer id) {
        if (invigilatorRepository.existsById(id)) {
            assignmentRepository.deleteAllByInvigilator_InvigilatorId(id);
            invigilatorRepository.deleteById(id);
        }
    }

    public void updateInvigilator(Integer id, String name, String department, Invigilator.invigilatorRank rank) {
        Invigilator invigilator = new Invigilator(id, name, department, rank);
        invigilatorRepository.save(invigilator);
    }
}
