package uniproject.exam.seating.student;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity()
public class Student {
    @Id
    private String rollNo;

    private String name;
    private String majorId;
    private int assignedRoom;


    public Student(){}

    public Student(String rollNo, String name, String majorId) {
        this.rollNo = rollNo;
        this.name = name;
        this.majorId = majorId;
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

    public void setAssignedRoom(int assignedRoom) {
        this.assignedRoom = assignedRoom;
    }
}

