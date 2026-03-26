package uniproject.exam.seating.invigilator;

import jakarta.persistence.*;

@Entity
public class invigilator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invigilatorId;

    private String invigilatorName;

    public enum invigilatorRank {
        CHIEF, SENIOR, ASSISTANT
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "invigilator_rank")
    public invigilatorRank rank;

    private String department;

    public invigilator() {}

    public invigilatorRank getRank() {
        return rank;
    }

    public void setRank(invigilatorRank rank) {
        this.rank = rank;
    }

    public invigilator(Integer invigilatorId, String invigilatorName, String department, invigilatorRank rank) {
        this.invigilatorId = invigilatorId;
        this.invigilatorName = invigilatorName;
        this.department = department;
        this.rank = rank;
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
