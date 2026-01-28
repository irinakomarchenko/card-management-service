package com.irinakom.cardmanagementservice.controller;

import com.irinakom.cardmanagementservice.dto.request.TransferRequest;
import com.irinakom.cardmanagementservice.entity.Role;
import com.irinakom.cardmanagementservice.entity.User;
import com.irinakom.cardmanagementservice.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequest request) {
        transferService.transfer(getCurrentUser(), request);
        return ResponseEntity.noContent().build();
    }


    private User getCurrentUser() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        return user;
    }

}
