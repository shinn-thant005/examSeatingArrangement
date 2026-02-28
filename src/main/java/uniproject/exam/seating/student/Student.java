package uniproject.exam.seating.student;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import uniproject.exam.seating.room.Room;

@Entity()
public class Student {
    @Id
    private String rollNo;

    private String name;
    private String majorId;
    private boolean isSeated; // Default to false

    // This relationship handles the assigned_room_id column entirely
    @ManyToOne
    @JoinColumn(name = "assigned_room_id")
    private Room assignedRoom;

    public Student(){}

    public Student(String rollNo, String name, String majorId) {
        this.rollNo = rollNo;
        this.name = name;
        this.majorId = majorId;
        this.isSeated = false;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getName() {
        return name;
    }

    public String getMajorId() {
        return majorId;
    }

    public boolean isSeated() {
        return isSeated;
    }

    // Corrected to return the Room object
    public Room getAssignedRoom() {
        return assignedRoom;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public void setSeated(boolean seated) {
        isSeated = seated;
    }

    public void setAssignedRoom(Room assignedRoom) {
        this.assignedRoom = assignedRoom;
    }
}