package ge.kursi.vanopt.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OptimizationResponseDto(
        UUID requestId,
        List<ShipmentDto> selectedShipments,
        Integer totalVolume,
        Long totalRevenue,
        LocalDateTime createdAt
) {}