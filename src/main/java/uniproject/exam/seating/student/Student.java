package uniproject.exam.seating.student;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity()
public class Student {
    @Id
    private String RollNo;

    private String name;
    private String majorId;
    private int assignedRoom;


    public Student(){}

    public Student(String RollNo, String name, String majorId) {
        this.RollNo = RollNo;
        this.name = name;
        this.majorId = majorId;
    }

    public String getRollNo() {
        return RollNo;
    }

    public String getName() {
        return name;
    }

    public String getMajorId() {
        return majorId;
    }

    public int getAssignedRoom() {
        return assignedRoom;
    }

    public void setRollNo(String RollNo) {
        this.RollNo = RollNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public void setAssignedRoom(int assignedRoom) {
        this.assignedRoom = assignedRoom;
    }
}

