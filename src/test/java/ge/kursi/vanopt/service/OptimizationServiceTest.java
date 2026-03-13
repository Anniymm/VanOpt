package ge.kursi.vanopt.service;

import ge.kursi.vanopt.algorithm.KnapsackSolver;
import ge.kursi.vanopt.dto.OptimizationRequestDto;
import ge.kursi.vanopt.dto.OptimizationResponseDto;
import ge.kursi.vanopt.dto.ShipmentDto;
import ge.kursi.vanopt.exception.InvalidInputException;
import ge.kursi.vanopt.model.OptimizationRequest;
import ge.kursi.vanopt.repository.OptimizationRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptimizationServiceTest {

    @Mock
    private KnapsackSolver knapsackSolver;

    @Mock
    private OptimizationRequestRepository requestRepository;

    @InjectMocks
    private OptimizationService optimizationService;

    private List<ShipmentDto> shipments;
    private OptimizationRequestDto requestDto;

    @BeforeEach
    void setUp() {
        shipments = List.of(
                new ShipmentDto("Parcel A", 5, 120L),
                new ShipmentDto("Parcel B", 10, 200L)
        );
        requestDto = new OptimizationRequestDto(15, shipments);
    }

    @Test
    void shouldOptimizeAndPersistResult() {
        List<ShipmentDto> selected = List.of(
                new ShipmentDto("Parcel A", 5, 120L),
                new ShipmentDto("Parcel B", 10, 200L)
        );
        when(knapsackSolver.solve(shipments, 15)).thenReturn(selected);
        when(requestRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        OptimizationResponseDto response = optimizationService.optimize(requestDto);

        assertThat(response.totalRevenue()).isEqualTo(320L);
        assertThat(response.totalVolume()).isEqualTo(15);
        assertThat(response.selectedShipments()).hasSize(2);
        assertThat(response.requestId()).isNotNull();
        verify(requestRepository, times(1)).save(any(OptimizationRequest.class));
    }

    @Test
    void shouldReturnEmptyWhenNoShipmentsFit() {
        when(knapsackSolver.solve(shipments, 15)).thenReturn(List.of());
        when(requestRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        OptimizationResponseDto response = optimizationService.optimize(requestDto);

        assertThat(response.totalRevenue()).isEqualTo(0L);
        assertThat(response.totalVolume()).isEqualTo(0);
        assertThat(response.selectedShipments()).isEmpty();
    }

    @Test
    void shouldGetByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        OptimizationRequest request = new OptimizationRequest(15, 15, 320L);
        when(requestRepository.findById(id)).thenReturn(Optional.of(request));

        OptimizationResponseDto response = optimizationService.getById(id);

        assertThat(response).isNotNull();
        assertThat(response.totalRevenue()).isEqualTo(320L);
    }

    @Test
    void shouldThrowWhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(requestRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> optimizationService.getById(id))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void shouldGetAllSuccessfully() {
        OptimizationRequest r1 = new OptimizationRequest(10, 5, 100L);
        OptimizationRequest r2 = new OptimizationRequest(20, 15, 300L);
        when(requestRepository.findAll()).thenReturn(List.of(r1, r2));

        List<OptimizationResponseDto> all = optimizationService.getAll();

        assertThat(all).hasSize(2);
    }
}