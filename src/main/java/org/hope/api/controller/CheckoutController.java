package org.hope.api.controller;

import lombok.RequiredArgsConstructor;
import org.hope.api.dto.Purchase;
import org.hope.api.dto.PurchaseResponse;
import org.hope.api.service.ICheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final ICheckoutService checkoutService;



    @PostMapping("/purchase")
    protected ResponseEntity<PurchaseResponse> placeOrder(@RequestBody Purchase purchase){

        return ResponseEntity.ok(checkoutService.placeOrder(purchase));
    }
}
