package uniproject.exam.seating.invigilator;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/invigilator")
public class InvigilatorController {
    InvigilatorService invigilatorService;

    public InvigilatorController(InvigilatorService invigilatorService) {
        this.invigilatorService = invigilatorService;
    }

    @GetMapping("/list-invigilator")
    public List<Invigilator> listInvigilator() {
        return invigilatorService.findAllInvigilator();
    }

    @PostMapping("/add-invigilator")
    public String addInvigilator(@RequestBody Invigilator invigilator) {
        invigilatorService.addInvigilator(invigilator);
        return "New invigilator added successfully.";
    }

    @DeleteMapping("/delete-invigilator/{invigilatorId}")
    public String deleteInvigilatorById(@PathVariable Integer invigilatorId) {
        invigilatorService.deleteInvigilator(invigilatorId);
        return "Deleted invigilator with id " + invigilatorId;
    }

    @PutMapping("/update-invigilator/{invigilatorId}")
    public String updateInvigilator(@PathVariable Integer invigilatorId, @RequestBody Invigilator invigilator) {
        invigilatorService.updateInvigilator(invigilatorId,  invigilator.getInvigilatorName(), invigilator.getDepartment(), invigilator.getRank());
        return "Updated invigilator with id " + invigilatorId;
    }
}
