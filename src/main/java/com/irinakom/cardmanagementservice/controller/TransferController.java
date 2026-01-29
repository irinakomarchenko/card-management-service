package com.irinakom.cardmanagementservice.controller;

import com.irinakom.cardmanagementservice.dto.request.TransferRequest;
import com.irinakom.cardmanagementservice.entity.Role;
import com.irinakom.cardmanagementservice.entity.User;
import com.irinakom.cardmanagementservice.security.CustomUserDetails;
import com.irinakom.cardmanagementservice.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        transferService.transfer(userDetails.getUser(), request);
        return ResponseEntity.noContent().build();
    }
}
