package uniproject.exam.seating.seating;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class seatingController {
    seatingService seatingService;

    public seatingController(seatingService seatingService) {
        this.seatingService = seatingService;
    }


    @GetMapping("/generate-plan/{roomId}")
    public seatingService.SeatingPlanResponse generatePlan(@PathVariable Integer roomId) {
        return seatingService.generateSeatingPlan(roomId);
    }
}
