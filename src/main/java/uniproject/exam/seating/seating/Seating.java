package uniproject.exam.seating.seating;

import jakarta.persistence.*;
import uniproject.exam.seating.room.Room;
import uniproject.exam.seating.student.Student;

@Entity
@Table(name = "seating", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_room_row_column",
                columnNames = {"roomId", "rowNum", "columnNum"}
        )
})
public class Seating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer SeatingId;

    @OneToOne
    @JoinColumn(name = "rollNo")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    private Integer rowNum;
    private Integer columnNum;

    public Seating() {}

    public Seating(Integer seatingId, Student student, Room room, Integer rowNum, Integer columnNum) {
        SeatingId = seatingId;
        this.student = student;
        this.room = room;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
    }

    public Integer getSeatingId() {
        return SeatingId;
    }

    public Student getStudent() {
        return student;
    }

    public Room getRoom() {
        return room;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public Integer getColumnNum() {
        return columnNum;
    }

    public void setSeatingId(Integer seatingId) {
        SeatingId = seatingId;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public void setColumnNum(Integer columnNum) {
        this.columnNum = columnNum;
    }
}
