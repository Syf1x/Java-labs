package com.office.employeemanagement.controller;

import com.office.employeemanagement.service.RequestCounterService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/counter")
@RequiredArgsConstructor
public class CounterController {

    private final RequestCounterService counterService;

    @Operation(summary = "Инкремент небезопасного счётчика (демонстрация race condition под нагрузкой)")
    @PostMapping("/unsafe/increment")
    public Map<String, Integer> incrementUnsafe() {
        counterService.incrementUnsafe();
        return Map.of("unsafeCount", counterService.getUnsafeCount());
    }

    @Operation(summary = "Инкремент потокобезопасного счётчика (AtomicInteger)")
    @PostMapping("/safe/increment")
    public Map<String, Integer> incrementSafe() {
        counterService.incrementSafe();
        return Map.of("safeCount", counterService.getSafeCount());
    }

    @Operation(summary = "Получить текущие значения обоих счётчиков")
    @GetMapping
    public Map<String, Integer> getCounts() {
        return Map.of("unsafeCount", counterService.getUnsafeCount(), "safeCount", counterService.getSafeCount());
    }

    @Operation(summary = "Сбросить оба счётчика в ноль")
    @PostMapping("/reset")
    public void reset() {
        counterService.reset();
    }
}
