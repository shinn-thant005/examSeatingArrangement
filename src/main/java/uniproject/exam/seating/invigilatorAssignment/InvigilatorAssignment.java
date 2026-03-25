package uniproject.exam.seating.invigilatorAssignment;

import jakarta.persistence.*;
import uniproject.exam.seating.exam.Exam;
import uniproject.exam.seating.invigilator.invigilator;
import uniproject.exam.seating.room.Room;

@Entity
public class InvigilatorAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AssignmentId;

    @ManyToOne
    @JoinColumn(name = "examId", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "invigilatorId", nullable = false)
    private invigilator invigilator;

    public InvigilatorAssignment() {}

    public InvigilatorAssignment(Exam exam, Room room, invigilator invigilator) {
        this.exam = exam;
        this.room = room;
        this.invigilator = invigilator;
    }

    public int getAssignmentId() {
        return AssignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        AssignmentId = assignmentId;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public invigilator getInvigilator() {
        return invigilator;
    }

    public void setInvigilator(invigilator invigilator) {
        this.invigilator = invigilator;
    }
}



