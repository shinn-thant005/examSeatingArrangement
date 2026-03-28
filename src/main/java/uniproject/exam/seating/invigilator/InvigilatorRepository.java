package uniproject.exam.seating.invigilator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvigilatorRepository extends JpaRepository<Invigilator, Integer> {
    Optional<Invigilator> findByInvigilatorName(String invigilatorName);
}
