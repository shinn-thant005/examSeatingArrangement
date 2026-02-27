package uniproject.exam.seating.invigilator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class invigilatorController {
    invigilatorService invigilatorService;

    public invigilatorController(invigilatorService invigilatorService) {
        this.invigilatorService = invigilatorService;
    }

    @RequestMapping("/list-invigilator")
    public List<invigilator> listInvigilator() {
        return invigilatorService.findAll();
}
