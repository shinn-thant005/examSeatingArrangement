package uniproject.exam.seating.invigilator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class invigilator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invigilatorId;

    private String invigilatorName;
    private String department;

    public invigilator() {}

    public invigilator(int invigilatorId, String invigilatorName, String department) {
        this.invigilatorId = invigilatorId;
        this.invigilatorName = invigilatorName;
        this.department = department;
    }

    public int getInvigilatorId() {
        return invigilatorId;
    }

    public String getInvigilatorName() {
        return invigilatorName;
    }

    public String getDepartment() {
        return department;
    }

    public void setInvigilatorId(int invigilatorId) {
        this.invigilatorId = invigilatorId;
    }

    public void setInvigilatorName(String invigilatorName) {
        this.invigilatorName = invigilatorName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
