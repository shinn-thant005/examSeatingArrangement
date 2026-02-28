package uniproject.exam.seating.room;

import jakarta.persistence.*;
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

    public Room(Integer roomId, Integer floor, String roomName, Integer totalCapacity, Integer rowCapacity, Integer columnCapacity) {
        this.roomId = roomId;
        this.floor = floor;
        this.roomName = roomName;
        this.totalCapacity = totalCapacity;
        this.rowCapacity = rowCapacity;
        this.columnCapacity = columnCapacity;
    }

    public Room() {

    }

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
