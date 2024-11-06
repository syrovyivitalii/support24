package lv.dsns.support24.problem.repository;


import lv.dsns.support24.problem.repository.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {

    boolean existsByProblem(String problem);
}
