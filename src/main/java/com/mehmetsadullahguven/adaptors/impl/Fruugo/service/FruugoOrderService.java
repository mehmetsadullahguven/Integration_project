package com.mehmetsadullahguven.adaptors.impl.Fruugo.service;

import com.mehmetsadullahguven.adaptors.impl.Fruugo.AbstractFruugo;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.order.OrderFulfillmentRequestIU;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.order.OrderRequestIU;
import com.mehmetsadullahguven.dto.*;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FruugoOrderService extends AbstractFruugo {

    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = CHARACTERS.length();

    public DtoOrders list(DtoOrderIU dtoOrderIU) {
        String correlationId = correlationEncodeId(System.currentTimeMillis() / 1000);
        requestOrders(correlationId, dtoOrderIU);

        List<Order> orderList = orderRepository.findByStatusAndR2sProductListId("created", dtoOrderIU.getR2sProductListId());

        if (orderList.isEmpty()) {
            return null;
        }

        List<DtoOrder> dtoOrderList = orderList.stream()
                .map(order -> getSerializedObject(order.getSerializedData(), DtoOrder.class))
                .toList();

        return new DtoOrders(dtoOrderList);
    }

    public DtoOrders detail(DtoOrderIU dtoOrderIU) {
        Optional<Order> optionalOrder = orderRepository.findByClientOrderIdAndR2sProductListId(dtoOrderIU.getId(), dtoOrderIU.getR2sProductListId());
        return optionalOrder.map(order -> {
            DtoOrders dtoOrders = new DtoOrders();
            dtoOrders.setOrders(List.of(getSerializedObject(order.getSerializedData(), DtoOrder.class)));
            return dtoOrders;
        }).orElse(null);
    }

    public void fulfillment(DtoOrderFulfillmentIU dtoOrderFulfillmentIU) {
        String correlationId = correlationEncodeId(System.currentTimeMillis() / 1000);

        OrderFulfillmentRequestIU orderFulfillmentRequestIU = new OrderFulfillmentRequestIU();
        orderFulfillmentRequestIU.setOrderId(dtoOrderFulfillmentIU.getOrderId());
        orderFulfillmentRequestIU.setShipmentId(null);
        orderFulfillmentRequestIU.setTrackingCode(dtoOrderFulfillmentIU.getOrderForm().getBarcodes().getCode());
        orderFulfillmentRequestIU.setTrackingUrl(dtoOrderFulfillmentIU.getOrderForm().getBarcodes().getCargoTrackingUrl());

        ResponseEntity<String> responseEntity = super.sendRequest(orderFulfillmentRequestIU, dtoOrderFulfillmentIU, String.class, correlationId, "/v3/orders/tracking");

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "Fruugo_fulfillment_error: " + dtoOrderFulfillmentIU.getOrderId()));
        }
    }

    public void statusUpdate(DtoOrderStatusUpdateIU dtoOrderStatusUpdateIU) {
        Optional<Order> optionalOrder = orderRepository.findByClientOrderIdAndR2sProductListId(dtoOrderStatusUpdateIU.getOrderId(), dtoOrderStatusUpdateIU.getR2sProductListId());
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(dtoOrderStatusUpdateIU.getStatus());
        }
    }

    public void webhook(DtoWebhookIU dtoWebhookIU) {
        DtoWebhookPayloadIU webhookPayload = dtoWebhookIU.getWebhookPayload();
        Order order = handleOrder(webhookPayload);
        //super.saveOrder();
    }

    private Order handleOrder(DtoWebhookPayloadIU dtoWebhookPayloadIU) {

        // Burası webhook bilgisinden sonra doldurulacak;
        Order order = new Order();
        DtoOrder dtoOrder = new DtoOrder();
        BeanUtils.copyProperties(dtoWebhookPayloadIU, dtoOrder);
        order.setSerializedData(setSerializedObject(dtoOrder));
        return order;
    }

    private void requestOrders(String correlationId, DtoOrderIU dtoOrderIU) {
        Date lastOrderDate = getLastOrderDate(dtoOrderIU.getR2sProductListId());

        OrderRequestIU requestBody = new OrderRequestIU();
        requestBody.setDateFrom(lastOrderDate);
        requestBody.setDateTo(new Date());

        ResponseEntity<String> responseEntity = super.sendRequest(requestBody, dtoOrderIU, String.class, correlationId, "/v3/orders");

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "order list request error"));
        }
        super.saveOrder(null, dtoOrderIU, correlationId, "webhook_waiting");
    }

    private Date getLastOrderDate(String r2sProductListId) {
        Optional<Order> optionalOrder = orderRepository.findFirstByR2sProductListIdOrderByCreatedAtDesc(r2sProductListId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            return order.getCreatedAt();
        }

        LocalDateTime localDateTime = LocalDateTime.parse("2000-01-01T14:15:22", DateTimeFormatter.ISO_DATE_TIME);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private String correlationEncodeId(long timestamp) {
        long combinedId = timestamp;
        StringBuilder code = new StringBuilder();

        while (combinedId > 0) {
            code.insert(0, CHARACTERS.charAt((int) (combinedId % BASE)));
            combinedId /= BASE;
        }

        return code.toString();
    }

}
