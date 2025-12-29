package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.IProcessOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("processOrderService")
public class ProcessOrderService implements IProcessOrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Long processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        System.out.println(order);
        List<Long> orderIdList = new ArrayList<>();
        orderIdList.add(orderId);
        Set<Product> products = order.getItems();
        for (Product product : products) {
            if (product.getType().equals("NORMAL")) {
                if (product.getAvailable() > 0) {
                    product.setAvailable(product.getAvailable() - 1);
                    productRepository.save(product);
                } else {
                    int leadTime = product.getLeadTime();
                    if (leadTime > 0) {
                        productService.notifyDelay(leadTime, product);
                    }
                }
            } else if (product.getType().equals("SEASONAL")) {
                // Add new season rules
                if ((LocalDate.now().isAfter(product.getSeasonStartDate()) && LocalDate.now().isBefore(product.getSeasonEndDate())
                        && product.getAvailable() > 0)) {
                    product.setAvailable(product.getAvailable() - 1);
                    productRepository.save(product);
                } else {
                    productService.handleSeasonalProduct(product);
                }
            } else if (product.getType().equals("EXPIRABLE")) {
                if (product.getAvailable() > 0 && product.getExpiryDate().isAfter(LocalDate.now())) {
                    product.setAvailable(product.getAvailable() - 1);
                    productRepository.save(product);
                } else {
                    productService.handleExpiredProduct(product);
                }
            }
        }

        return order.getId();
    }
}
