package uniproject.exam.seating.invigilatorAssignment;

import uniproject.exam.seating.seating.SeatingService.SeatingPlanResponse;
import java.time.LocalDate;

public class InvigilatorDutyResponse {
    private String subject;
    private LocalDate examDate;
    private String examTime;
    private String roomName;
    private SeatingPlanResponse seatingPlan;

    public InvigilatorDutyResponse(String subject, LocalDate examDate, String examTime, String roomName, SeatingPlanResponse seatingPlan) {
        this.subject = subject;
        this.examDate = examDate;
        this.examTime = examTime;
        this.roomName = roomName;
        this.seatingPlan = seatingPlan;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public String getExamTime() { return examTime; }
    public void setExamTime(String examTime) { this.examTime = examTime; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public SeatingPlanResponse getSeatingPlan() { return seatingPlan; }
    public void setSeatingPlan(SeatingPlanResponse seatingPlan) { this.seatingPlan = seatingPlan; }
}