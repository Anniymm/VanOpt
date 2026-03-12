package ge.kursi.vanopt.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "selected_shipments")
public class SelectedShipment {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private OptimizationRequest request;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer volume;

    @Column(nullable = false)
    private Long revenue;

    protected SelectedShipment() {}

    public SelectedShipment(OptimizationRequest request, String name, Integer volume, Long revenue) {
        this.id = UUID.randomUUID();
        this.request = request;
        this.name = name;
        this.volume = volume;
        this.revenue = revenue;
    }

    public UUID getId() { return id; }
    public OptimizationRequest getRequest() { return request; }
    public String getName() { return name; }
    public Integer getVolume() { return volume; }
    public Long getRevenue() { return revenue; }

//    private setter-ebi
    private void setId(UUID id) { this.id = id; }
    private void setRequest(OptimizationRequest request) { this.request = request; }
    private void setName(String name) { this.name = name; }
    private void setVolume(Integer volume) { this.volume = volume; }
    private void setRevenue(Long revenue) { this.revenue = revenue; }
}