package uniproject.exam.seating.invigilator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class invigilatorService {
    invigilatorRepository invigilatorRepository;

    public invigilatorService(invigilatorRepository invigilatorRepository) {
        this.invigilatorRepository = invigilatorRepository;
    }

    static List<invigilator> invigilators = new ArrayList<invigilator>();

    public List<invigilator> findAllInvigilator() {
        return invigilatorRepository.findAll();
    }

    public void addInvigilator(Integer id, String name, String department) {
        invigilator invigilator = new invigilator(id, name, department);
        invigilatorRepository.save(invigilator);
    }

    public void deleteInvigilator(Integer id) {
        if (invigilatorRepository.existsById(id)) {
            invigilatorRepository.deleteById(id);
        }
    }
}
