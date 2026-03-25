package ge.kursi.vanopt.controller;

import ge.kursi.vanopt.dto.OptimizationRequestDto;
import ge.kursi.vanopt.dto.OptimizationResponseDto;
import ge.kursi.vanopt.dto.ShipmentDto;
import ge.kursi.vanopt.service.OptimizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/optimizations")
public class OptimizationController {

    private final OptimizationService optimizationService;

    public OptimizationController(OptimizationService optimizationService) {
        this.optimizationService = optimizationService;
    }

    @PostMapping
    public ResponseEntity<OptimizationResponseDto> optimize(
            @Valid @RequestBody OptimizationRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(optimizationService.optimize(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptimizationResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(optimizationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OptimizationResponseDto>> getAll() {
        return ResponseEntity.ok(optimizationService.getAll());
    }

//    @GetMapping("/selected")
//    public ResponseEntity<List<ShipmentDto>> getSelected() {
//        return ResponseEntity.ok(optimizationService.getSelected());
//    }
}