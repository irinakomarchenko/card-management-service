package com.irinakom.cardmanagementservice.service;

import com.irinakom.cardmanagementservice.dto.request.TransferRequest;
import com.irinakom.cardmanagementservice.entity.User;

public interface TransferService {
    void transfer(User user, TransferRequest request);
}
