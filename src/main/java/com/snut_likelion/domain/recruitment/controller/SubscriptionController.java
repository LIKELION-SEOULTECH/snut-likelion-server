package com.snut_likelion.domain.recruitment.controller;

import com.snut_likelion.domain.recruitment.entity.SubscriptionType;
import com.snut_likelion.domain.recruitment.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribeMember(
            @RequestParam("email") String email,
            @RequestParam("type") SubscriptionType type
    ) {
        subscriptionService.register(email, type);
    }
}
