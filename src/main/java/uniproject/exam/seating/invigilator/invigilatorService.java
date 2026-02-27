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
        return invigilatorRepository.findAllByInvigilatorId();
    }
}
