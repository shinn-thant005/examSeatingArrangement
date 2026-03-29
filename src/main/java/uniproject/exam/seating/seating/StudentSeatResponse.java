package uniproject.exam.seating.seating;

public class StudentSeatResponse {
    private String name;
    private String roomName;
    private Integer floor;
    private Integer rowNum;
    private Integer columnNum;

    public StudentSeatResponse(String name, String roomName, Integer floor, Integer rowNum, Integer columnNum) {
        this.name = name;
        this.roomName = roomName;
        this.floor = floor;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }

    public Integer getRowNum() { return rowNum; }
    public void setRowNum(Integer rowNum) { this.rowNum = rowNum; }

    public Integer getColumnNum() { return columnNum; }
    public void setColumnNum(Integer columnNum) { this.columnNum = columnNum; }
}