package org.hope.api.service;

import org.hope.api.dto.Purchase;
import org.hope.api.dto.PurchaseResponse;

public interface ICheckoutService {

    PurchaseResponse placeOrder(final Purchase purchase);
}
