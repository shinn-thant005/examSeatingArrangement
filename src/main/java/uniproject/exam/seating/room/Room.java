package uniproject.exam.seating.room;

import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Controller;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    private Integer floor;
    @Column(unique = true)

    private String roomName;
    private Integer totalCapacity;
    private Integer rowCapacity;
    private Integer columnCapacity;

    @Min(value = 3)
    private Integer numOfInvigilators;

    private Integer maxMajor;

    public Room(Integer roomId, Integer floor, String roomName, Integer totalCapacity, Integer rowCapacity, Integer columnCapacity, Integer numOfInvigilators, Integer maxMajor) {
        this.roomId = roomId;
        this.floor = floor;
        this.roomName = roomName;
        this.totalCapacity = totalCapacity;
        this.rowCapacity = rowCapacity;
        this.columnCapacity = columnCapacity;
        this.numOfInvigilators = numOfInvigilators;
        this.maxMajor = maxMajor;
    }

    public Room() {}

    public Integer getRoomId() {
        return roomId;
    }

    public Integer getFloor() {
        return floor;
    }

    public String getRoomName() {
        return roomName;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public Integer getRowCapacity() {
        return rowCapacity;
    }

    public Integer getColumnCapacity() {
        return columnCapacity;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getNumOfInvigilators() {
        return numOfInvigilators;
    }

    public void setNumOfInvigilators(Integer numOfInvigilators) {
        this.numOfInvigilators = numOfInvigilators;
    }

    public Integer getMaxMajor() {
        return maxMajor;
    }

    public void setMaxMajor(Integer maxMajor) {
        this.maxMajor = maxMajor;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public void setRowCapacity(Integer rowCapacity) {
        this.rowCapacity = rowCapacity;
    }

    public void setColumnCapacity(Integer columnCapacity) {
        this.columnCapacity = columnCapacity;
    }
}
