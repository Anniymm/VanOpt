package ge.kursi.vanopt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShipmentDto(

        @NotBlank(message = "Shipment name must not be blank")
        String name,

        @NotNull(message = "Volume must not be null")
        @Min(value = 1, message = "Volume must be at least 1 dm³")
        Integer volume,

        @NotNull(message = "Revenue must not be null")
        @Min(value = 1, message = "Revenue must be at least 1")
        Long revenue
) {}