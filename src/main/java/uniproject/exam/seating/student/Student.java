package uniproject.exam.seating.student;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity()
public class Student {
    @Id
    @GeneratedValue()
    private String roll_no;

    private String name;
    private String major_id;
    private int assigned_room;


    public Student(){}

    public Student(String roll_no, String name, String major_id) {
        this.roll_no = roll_no;
        this.name = name;
        this.major_id = major_id;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public String getName() {
        return name;
    }

    public String getMajor_id() {
        return major_id;
    }

    public int getAssigned_room() {
        return assigned_room;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMajor_id(String major_id) {
        this.major_id = major_id;
    }

    public void setAssigned_room(int assigned_room) {
        this.assigned_room = assigned_room;
    }
}

