package uniproject.exam.seating.invigilator;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class invigilatorController {
    invigilatorService invigilatorService;

    public invigilatorController(invigilatorService invigilatorService) {
        this.invigilatorService = invigilatorService;
    }

    @RequestMapping("/list-invigilator")
    public List<invigilator> listInvigilator() {
        return invigilatorService.findAllInvigilator();
    }

    @PostMapping("/add-invigilator")
    public String addInvigilator(@RequestBody invigilator invigilator) {
        invigilatorService.addInvigilator(invigilator.getInvigilatorId(), invigilator.getInvigilatorName(), invigilator.getDepartment());
        return "New invigilator added successfully.";
    }

    @DeleteMapping("/delete-invigilator/{invigilatorId}")
    public String deleteInvigilatorById(@PathVariable int invigilatorId) {
        invigilatorService.deleteInvigilator(invigilatorId);
        return "Deleted invigilator with id " + invigilatorId;
    }


}
