package com.petru.orderservice.service;

import com.petru.orderservice.dto.InventoryResponse;
import com.petru.orderservice.dto.OrderLineItemsDto;
import com.petru.orderservice.dto.OrderRequest;
import com.petru.orderservice.model.Order;
import com.petru.orderservice.model.OrderLineItems;
import com.petru.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItems(orderLineItemsList);
        List<String> skuCodes = order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode).toList();

        //Call inventory Service and place order if product is in stock, use synchronous code!
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get().uri("http://inventory-service:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class).block();

        Boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

        if(Boolean.TRUE.equals(allProductsInStock)){
            orderRepository.save(order);
            return "Order placed successfully!!!!";
        }else{
            throw new IllegalArgumentException("Product is not in stock try again later!");
        }

    }

    OrderLineItems mapToDto(OrderLineItemsDto dto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setId(dto.getId());
        orderLineItems.setQuantity(dto.getQuantity());
        orderLineItems.setSkuCode(dto.getSkuCode());
        orderLineItems.setPrice(dto.getPrice());
        return orderLineItems;
    }
}
