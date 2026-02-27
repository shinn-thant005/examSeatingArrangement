package uniproject.exam.seating.invigilator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface invigilatorRepository extends JpaRepository<invigilator, Integer> {
    public List<invigilator> findAllByInvigilatorId(int invigilatorId);
}
