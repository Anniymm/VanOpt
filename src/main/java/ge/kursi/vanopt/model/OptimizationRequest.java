package ge.kursi.vanopt.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "optimization_requests")
public class OptimizationRequest {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "max_volume", nullable = false)
    private Integer maxVolume;

    @Column(name = "total_volume", nullable = false)
    private Integer totalVolume;

    @Column(name = "total_revenue", nullable = false)
    private Long totalRevenue;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectedShipment> selectedShipments = new ArrayList<>();

    protected OptimizationRequest() {}

    public OptimizationRequest(Integer maxVolume, Integer totalVolume, Long totalRevenue) {
        this.id = UUID.randomUUID();
        this.maxVolume = maxVolume;
        this.totalVolume = totalVolume;
        this.totalRevenue = totalRevenue;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public Integer getMaxVolume() { return maxVolume; }
    public Integer getTotalVolume() { return totalVolume; }
    public Long getTotalRevenue() { return totalRevenue; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<SelectedShipment> getSelectedShipments() { return selectedShipments; }


    // public setter mqonda pirdapir da methodad shevcvale
    public void addShipments(List<SelectedShipment> shipments) {
        this.selectedShipments.addAll(shipments);
    }

//    private setterebi
    private void setId(UUID id) { this.id = id; }
    private void setMaxVolume(Integer maxVolume) { this.maxVolume = maxVolume; }
    private void setTotalVolume(Integer totalVolume) { this.totalVolume = totalVolume; }
    private void setTotalRevenue(Long totalRevenue) { this.totalRevenue = totalRevenue; }
    private void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}