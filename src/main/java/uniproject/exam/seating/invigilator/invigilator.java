package uniproject.exam.seating.invigilator;

import jakarta.persistence.*;

@Entity
public class invigilator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invigilatorId;

    private String invigilatorName;

    public enum InvigilatorRank {
        CHIEF, SENIOR, ASSISTANT
    }

    @Enumerated(EnumType.STRING)
    public InvigilatorRank InvigilatorRank;

    private String department;

    public invigilator() {}

    public InvigilatorRank getInvigilatorRank() {
        return InvigilatorRank;
    }

    public void setInvigilatorRank(InvigilatorRank invigilatorRank) {
        InvigilatorRank = invigilatorRank;
    }

    public invigilator(Integer invigilatorId, String invigilatorName, String department) {
        this.invigilatorId = invigilatorId;
        this.invigilatorName = invigilatorName;
        this.department = department;
    }

    public Integer getInvigilatorId() {
        return invigilatorId;
    }

    public String getInvigilatorName() {
        return invigilatorName;
    }

    public String getDepartment() {
        return department;
    }

    public void setInvigilatorId(Integer invigilatorId) {
        this.invigilatorId = invigilatorId;
    }

    public void setInvigilatorName(String invigilatorName) {
        this.invigilatorName = invigilatorName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
