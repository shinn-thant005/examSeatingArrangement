package uniproject.exam.seating.invigilator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface invigilatorRepository extends JpaRepository<invigilator, Integer> {
    Optional<invigilator> findByInvigilatorName(String invigilatorName);
}
