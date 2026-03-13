package ge.kursi.vanopt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.kursi.vanopt.dto.OptimizationRequestDto;
import ge.kursi.vanopt.dto.OptimizationResponseDto;
import ge.kursi.vanopt.dto.ShipmentDto;
import ge.kursi.vanopt.exception.InvalidInputException;
import ge.kursi.vanopt.service.OptimizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OptimizationController.class)
class OptimizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OptimizationService optimizationService;

    private OptimizationResponseDto sampleResponse() {
        return new OptimizationResponseDto(
                UUID.randomUUID(),
                List.of(
                        new ShipmentDto("Parcel A", 5, 120L),
                        new ShipmentDto("Parcel B", 10, 200L)
                ),
                15,
                320L,
                LocalDateTime.now()
        );
    }

    @Test
    void shouldReturn201OnValidOptimizeRequest() throws Exception {
        when(optimizationService.optimize(any())).thenReturn(sampleResponse());

        OptimizationRequestDto request = new OptimizationRequestDto(15, List.of(
                new ShipmentDto("Parcel A", 5, 120L),
                new ShipmentDto("Parcel B", 10, 200L)
        ));

        mockMvc.perform(post("/api/v1/optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalRevenue").value(320))
                .andExpect(jsonPath("$.totalVolume").value(15))
                .andExpect(jsonPath("$.requestId").isNotEmpty());
    }

    @Test
    void shouldReturn400WhenMaxVolumeIsInvalid() throws Exception {
        OptimizationRequestDto request = new OptimizationRequestDto(-1, List.of(
                new ShipmentDto("Parcel A", 5, 120L)
        ));

        mockMvc.perform(post("/api/v1/optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenShipmentsListIsEmpty() throws Exception {
        OptimizationRequestDto request = new OptimizationRequestDto(15, List.of());

        mockMvc.perform(post("/api/v1/optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200OnGetById() throws Exception {
        UUID id = UUID.randomUUID();
        when(optimizationService.getById(id)).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/optimizations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(320));
    }

    @Test
    void shouldReturn400OnGetByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(optimizationService.getById(id))
                .thenThrow(new InvalidInputException("Not found: " + id));

        mockMvc.perform(get("/api/v1/optimizations/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldReturn200OnGetAll() throws Exception {
        when(optimizationService.getAll()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/v1/optimizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].totalRevenue").value(320));
    }
}