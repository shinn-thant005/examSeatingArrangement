package uniproject.exam.seating.invigilatorAssignment;


import java.time.LocalDate;

public class InvigilatorDutyResponse {
    private String subject;
    private LocalDate examDate;
    private String examTime;
    private String roomName;
    private Integer roomId;

    public InvigilatorDutyResponse(String subject, LocalDate examDate, String examTime, String roomName, Integer roomId) {
        this.subject = subject;
        this.examDate = examDate;
        this.examTime = examTime;
        this.roomName = roomName;
        this.roomId = roomId;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public String getExamTime() { return examTime; }
    public void setExamTime(String examTime) { this.examTime = examTime; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public Integer getRoomId() {return roomId;}
    public void setRoomId(Integer roomId) {this.roomId = roomId;}
}