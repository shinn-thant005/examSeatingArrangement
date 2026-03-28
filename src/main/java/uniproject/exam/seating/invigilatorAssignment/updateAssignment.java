package uniproject.exam.seating.invigilatorAssignment;


public class updateAssignment {
    private String roomName;
    private String invigilatorName;

    public updateAssignment(String roomName, String invigilatorName) {
        this.roomName = roomName;
        this.invigilatorName = invigilatorName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getInvigilatorName() {
        return invigilatorName;
    }

    public void setInvigilatorName(String invigilatorName) {
        this.invigilatorName = invigilatorName;
    }
}
