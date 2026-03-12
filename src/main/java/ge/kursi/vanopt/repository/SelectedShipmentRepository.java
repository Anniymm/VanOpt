package ge.kursi.vanopt.repository;

import ge.kursi.vanopt.model.SelectedShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SelectedShipmentRepository extends JpaRepository<SelectedShipment, UUID> {

    List<SelectedShipment> findByRequestId(UUID requestId);
}