package uniproject.exam.seating.seating;

import java.util.List;

public class SeatingPlanResponse {
    private String roomName;
    private List<List<String>> layout;

    public SeatingPlanResponse(String roomName, List<List<String>> layout) {
        this.roomName = roomName;
        this.layout = layout;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<List<String>> getLayout() {
        return layout;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setLayout(List<List<String>> layout) {
        this.layout = layout;
    }
}
