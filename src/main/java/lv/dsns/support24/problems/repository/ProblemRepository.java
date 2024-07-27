package lv.dsns.support24.problems.repository;


import lv.dsns.support24.problems.repository.entity.Problems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProblemRepository extends JpaRepository<Problems, UUID> {
    boolean existsByProblem(String problem);
}
