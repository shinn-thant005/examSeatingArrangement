package uniproject.exam.seating.exam;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "exam", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_sub_date_time_major",
                columnNames = {"subject", "examDate", "examTime", "targetMajor"}
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

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_time")
    private TimeOfDay examTime;

    private String targetMajor;

    public Exam() {
    }

    public Exam(Integer examId, String subject, LocalDate examDate, TimeOfDay examTime, String targetMajor) {
        this.examId = examId;
        this.subject = subject;
        this.examDate = examDate;
        this.examTime = examTime;
        this.targetMajor = targetMajor;
    }

    public String getTargetMajor() {
        return targetMajor;
    }

    public void setTargetMajor(String targetMajor) {
        this.targetMajor = targetMajor;
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

    public void setExamTime(TimeOfDay examTime) {
        this.examTime = examTime;
    }

}
