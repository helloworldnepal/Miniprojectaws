package com.mini.ec2.controller;



import com.mini.ec2.model.Instance;
import com.mini.ec2.service.impl.InstanceService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/instances")
    @RequiredArgsConstructor
    public class InstanceController {

        private final InstanceService instanceService;

        @PostMapping
        public ResponseEntity<Instance> launch(@RequestBody LaunchRequest request) {
            Instance instance = instanceService.launchInstance(request.name(), request.image());
            return ResponseEntity.status(HttpStatus.CREATED).body(instance);
        }

        @GetMapping
        public ResponseEntity<List<Instance>> list() {
            return ResponseEntity.ok(instanceService.listInstances());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Instance> getOne(@PathVariable Integer
 id) {
            return ResponseEntity.ok(instanceService.getInstanceOrThrow(id));
        }

        @PostMapping("/{id}/stop")
        public ResponseEntity<Instance> stop(@PathVariable Integer
 id) {
            return ResponseEntity.ok(instanceService.stopInstance(id));
        }

        @PostMapping("/{id}/start")
        public ResponseEntity<Instance> start(@PathVariable Integer id) {
            return ResponseEntity.ok(instanceService.startInstance(id));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> terminate(@PathVariable Integer
 id) {
            instanceService.terminateInstance(id);
            return ResponseEntity.noContent().build();
        }

        public record LaunchRequest(
                @NotBlank String name,
                @NotBlank String image
        ) {}
    }

