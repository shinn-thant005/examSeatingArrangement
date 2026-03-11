package uniproject.exam.seating.exam;

import jakarta.persistence.*;
import uniproject.exam.seating.room.Room;

import java.time.LocalDate;

@Entity
@Table(name = "exam", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_sub_date_time_room",
                columnNames = {"subject", "examDate", "examTime", "room"}
        )
})
public class Exam {

    public enum TimeOfDay {
        MORNING, AFTERNOON
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer examId;

    private String subject;

    private LocalDate examDate;
    private TimeOfDay examTime;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public Exam() {
    }

    public Exam(String subject, LocalDate examDate, TimeOfDay examTime, Room room) {
        this.subject = subject;
        this.examDate = examDate;
        this.examTime = examTime;
        this.room = room;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public TimeOfDay getExamTime() {
        return examTime;
    }

    public void setTime(TimeOfDay examTime) {
        this.examTime = examTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
