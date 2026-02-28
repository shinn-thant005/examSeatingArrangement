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
    private int assignedRoomId;
    private boolean isSeated; // Default to false

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

    public int getAssignedRoom() {
        return assignedRoomId;
    }

    public boolean isSeated() {
        return isSeated;
    }

    public int getAssignedRoomId() {
        return assignedRoomId;
    }

    public void setRollNo(String RollNo) {
        this.rollNo = RollNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public void setAssignedRoomId(int assignedRoomId) {
        this.assignedRoomId = assignedRoomId;
    }

    public void setSeated(boolean seated) {
        isSeated = seated;
    }

    public void setAssignedRoom(Room assignedRoom) {
        this.assignedRoom = assignedRoom;
    }

}

