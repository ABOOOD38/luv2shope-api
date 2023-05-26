package org.hope.api.dto;

import lombok.Data;
import org.hope.api.entity.Address;
import org.hope.api.entity.Customer;
import org.hope.api.entity.Order;
import org.hope.api.entity.OrderItem;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;

    private Address shippingAddress;

    private Address billingAddress;

    private Order order;

    private Set<OrderItem> orderItems;
}
