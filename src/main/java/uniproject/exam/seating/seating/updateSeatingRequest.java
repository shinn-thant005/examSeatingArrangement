package uniproject.exam.seating.seating;

public class updateSeatingRequest {
    private String rollNo;
    private String roomName;
    private Integer rowNum;
    private Integer columnNum;

    public updateSeatingRequest(String rollNo, String roomName, Integer rowNum, Integer columnNum) {
        this.rollNo = rollNo;
        this.roomName = roomName;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(Integer columnNum) {
        this.columnNum = columnNum;
    }
}
