package org.hope.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hope.api.dao.ICustomerRepository;
import org.hope.api.dto.Purchase;
import org.hope.api.dto.PurchaseResponse;
import org.hope.api.entity.Customer;
import org.hope.api.entity.Order;
import org.hope.api.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements ICheckoutService {

    private final ICustomerRepository customerRepository;

    @Override
    @Transactional
    public PurchaseResponse placeOrder(final Purchase purchase) {

        // retrieve the order info from dto (purchase)
        final Order order = purchase.getOrder();

        // generate Tracking Number
        final String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with order items
        final Set<OrderItem> orderItemSet = purchase.getOrderItems();
        orderItemSet.forEach(order::add);

        // populate order with billingAddress and shippingAddress
        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        // populate customer with order
        Customer customer = purchase.getCustomer();

        // check if this an existing customer
        final Optional<Customer> customerFromDB = customerRepository.findByEmail(
                customer.getEmail()
        );

        if (customerFromDB.isPresent())
            customer = customerFromDB.get();

        customer.add(order);

        // save to the database
        customerRepository.save(customer);

        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {

        // generate a random UUID number (version 4)
        return UUID.randomUUID().toString();
    }
}
