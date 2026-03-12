package ge.kursi.vanopt.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OptimizationRequestDto(

        @NotNull(message = "maxVolume must not be null")
        @Min(value = 1, message = "maxVolume must be at least 1")
        Integer maxVolume,

        @NotEmpty(message = "availableShipments must not be empty")
        @Valid
        List<ShipmentDto> availableShipments
) {}