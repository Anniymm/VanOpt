package ge.kursi.vanopt.repository;

import ge.kursi.vanopt.model.OptimizationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface OptimizationRequestRepository extends JpaRepository<OptimizationRequest, UUID> {
}