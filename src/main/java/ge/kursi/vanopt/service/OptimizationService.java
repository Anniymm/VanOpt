package ge.kursi.vanopt.service;

import ge.kursi.vanopt.algorithm.KnapsackSolver;
import ge.kursi.vanopt.dto.OptimizationRequestDto;
import ge.kursi.vanopt.dto.OptimizationResponseDto;
import ge.kursi.vanopt.dto.ShipmentDto;
import ge.kursi.vanopt.exception.InvalidInputException;
import ge.kursi.vanopt.model.OptimizationRequest;
import ge.kursi.vanopt.model.SelectedShipment;
import ge.kursi.vanopt.repository.OptimizationRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OptimizationService {

    private final KnapsackSolver knapsackSolver;
    private final OptimizationRequestRepository requestRepository;

    public OptimizationService(KnapsackSolver knapsackSolver,
                               OptimizationRequestRepository requestRepository) {
        this.knapsackSolver = knapsackSolver;
        this.requestRepository = requestRepository;
    }

    @Transactional
    public OptimizationResponseDto optimize(OptimizationRequestDto dto) {

        // algorithm
        List<ShipmentDto> selected = knapsackSolver.solve(
                dto.availableShipments(),
                dto.maxVolume()
        );

        //  totals
        int totalVolume = selected.stream()
                .mapToInt(ShipmentDto::volume)
                .sum();

        long totalRevenue = selected.stream()
                .mapToLong(ShipmentDto::revenue)
                .sum();

        // OptimizationRequest entity
        OptimizationRequest request = new OptimizationRequest(
                dto.maxVolume(),
                totalVolume,
                totalRevenue
        );

        //  shipment entity
        List<SelectedShipment> shipmentEntities = selected.stream()
                .map(s -> new SelectedShipment(request, s.name(), s.volume(), s.revenue()))
                .toList();

        request.setSelectedShipments(shipmentEntities);
        requestRepository.save(request);

        return toResponse(request, selected);
    }

    @Transactional(readOnly = true)
    public OptimizationResponseDto getById(UUID id) {
        OptimizationRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException(
                        "Optimization request not found with id: " + id));

        List<ShipmentDto> shipments = request.getSelectedShipments().stream()
                .map(s -> new ShipmentDto(s.getName(), s.getVolume(), s.getRevenue()))
                .toList();

        return toResponse(request, shipments);
    }

    @Transactional(readOnly = true)
    public List<OptimizationResponseDto> getAll() {
        return requestRepository.findAll().stream()
                .map(request -> {
                    List<ShipmentDto> shipments = request.getSelectedShipments().stream()
                            .map(s -> new ShipmentDto(s.getName(), s.getVolume(), s.getRevenue()))
                            .toList();
                    return toResponse(request, shipments);
                })
                .toList();
    }

    private OptimizationResponseDto toResponse(OptimizationRequest request, List<ShipmentDto> shipments) {
        return new OptimizationResponseDto(
                request.getId(),
                shipments,
                request.getTotalVolume(),
                request.getTotalRevenue(),
                request.getCreatedAt()
        );
    }
}