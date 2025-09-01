package com.mehmetsadullahguven.adaptors.impl.Aliexpress.service;

import com.mehmetsadullahguven.adaptors.impl.Aliexpress.AbstractAliexpress;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.order.*;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.order.OrderFulfillmentRequestIU;
import com.mehmetsadullahguven.dto.*;
import com.mehmetsadullahguven.enums.CurrencyType;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.Order;
import com.mehmetsadullahguven.model.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AliexpressOrderService extends AbstractAliexpress {

    public DtoOrders list(DtoOrderIU dtoOrderIU) {
        int page = 1;
        List<DtoOrder> dtoOrderList = new ArrayList<>();
        while (true) {
            OrderListResponse orderListResponse = remoteOrderList(page, dtoOrderIU);
            if (orderListResponse.getData().getTotalCount() == 0) {
                throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "order not found"));
            }
            if (orderListResponse.getData().getOrders().isEmpty()) {
                break;
            }
            orderListResponse.getData().getOrders().forEach(orderListResponseOrder -> {
                Optional<Order> optionalOrder = orderRepository.findByClientOrderIdAndR2sProductListId(orderListResponseOrder.getId().toString(), dtoOrderIU.getR2sProductListId());
                if (optionalOrder.isEmpty()) {
                    DtoOrder dtoOrder = handleOrder(orderListResponseOrder, dtoOrderIU.getR2sProductListId());
                    super.saveOrder(dtoOrder, dtoOrderIU, null, "create");

                    dtoOrderList.add(dtoOrder);
                }
            });
        }
        return new DtoOrders(dtoOrderList);
    }

    private DtoOrder handleOrder(OrderListResponseOrder order, String r2sProductListId) {
        DtoOrder dtoOrder = new DtoOrder();
        dtoOrder.setOrderId(order.getId().toString());
        dtoOrder.setR2sProductListId(r2sProductListId);
        dtoOrder.setOrderDate(order.getCreatedAt());

        DtoOrderUser dtoOrderUser = new DtoOrderUser();
        dtoOrderUser.setFirstName(order.getDeliveryInfo().getReceiver().getName());
        dtoOrderUser.setLastName(order.getDeliveryInfo().getReceiver().getName());
        dtoOrderUser.setEmail("-");
        dtoOrderUser.setPhone(order.getDeliveryInfo().getReceiver().getPhone());

        dtoOrder.setUser(dtoOrderUser);

        DtoOrderAddress dtoOrderAddress = new DtoOrderAddress();
        dtoOrderAddress.setFirstName(order.getDeliveryInfo().getReceiver().getName());
        dtoOrderAddress.setLastName(order.getDeliveryInfo().getReceiver().getName());
        dtoOrderAddress.setEmail("-");
        dtoOrderAddress.setPhone(order.getDeliveryInfo().getReceiver().getPhone());
        dtoOrderAddress.setAddress(order.getDeliveryAddress());
        dtoOrderAddress.setDistrict(order.getDeliveryInfo().getRegion());
        dtoOrderAddress.setCountry(order.getDeliveryInfo().getCountry());
        dtoOrderAddress.setCity(order.getDeliveryInfo().getCity());

        dtoOrder.setShippingAddress(dtoOrderAddress);
        dtoOrder.setBillingAddress(dtoOrderAddress);

        dtoOrder.setCurrency(CurrencyType.RUB);
        dtoOrder.setSubTotal(order.getTotalAmount());
        dtoOrder.setTotal(order.getTotalAmount());
        dtoOrder.setShippingPrice(null);
        dtoOrder.setProducts(handleOrderProducts(order, r2sProductListId));

        return dtoOrder;
    }

    private List<DtoOrderProduct> handleOrderProducts(OrderListResponseOrder order, String r2sProductListId) {
        List<DtoOrderProduct> dtoOrderProductList = new ArrayList<>();
        order.getOrderLines().forEach(orderLine -> {
            DtoOrderProduct dtoOrderProduct = new DtoOrderProduct();
            dtoOrderProduct.setR2sProductId(orderLine.getItemId());
            dtoOrderProduct.setQuantity(orderLine.getQuantity().intValue());
            dtoOrderProduct.setUnitPrice(orderLine.getItemPrice().floatValue());
            dtoOrderProduct.setTotalPrice(orderLine.getTotalAmount().floatValue());
            dtoOrderProduct.setSellUnitPrice(orderLine.getItemPrice().floatValue());
            dtoOrderProduct.setSellTotalPrice(orderLine.getTotalAmount().floatValue());
            Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(orderLine.getSkuId(), r2sProductListId);
            if (optionalProduct.isEmpty()) {
                dtoOrderProduct.setCombineId(orderLine.getSkuId());
            }
            dtoOrderProductList.add(dtoOrderProduct);
        });
        return dtoOrderProductList;
    }

    private OrderListResponse remoteOrderList(Integer page, DtoOrderIU dtoOrderIU) {
        OrderListRequestIU orderListRequestIU = new OrderListRequestIU();
        orderListRequestIU.setPageNumber(page);

        ResponseEntity<OrderListResponse> responseEntity = super.sendRequest(orderListRequestIU, dtoOrderIU, OrderListResponse.class, null, "seller-api/v1/order/get-order-list");
        if (Objects.nonNull(Objects.requireNonNull(responseEntity.getBody()).getError())) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "order list request error"));
        }
        return responseEntity.getBody();
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

        remoteOrderStatus(dtoOrderFulfillmentIU, "to-in-transit");

    }

    public void statusUpdate(DtoOrderStatusUpdateIU dtoOrderStatusUpdateIU) {
        Optional<Order> optionalOrder = orderRepository.findByClientOrderIdAndR2sProductListId(dtoOrderStatusUpdateIU.getOrderId(), dtoOrderStatusUpdateIU.getR2sProductListId());
        if (optionalOrder.isPresent()) {

            String status;

            switch (dtoOrderStatusUpdateIU.getStatus()) {
                case "shipped":
                    status = "to-in-transit";
                    break;
                case "delivered":
                    status = "to-ready-for-pickup";
                    break;
                default:
                    return;
            }

            remoteOrderStatus(dtoOrderStatusUpdateIU, status);

            Order order = optionalOrder.get();
            order.setStatus(dtoOrderStatusUpdateIU.getStatus());
            orderRepository.save(order);
        }
    }

    private <T> void remoteOrderStatus(T data, String status) {

        DtoOrderFulfillmentIU dtoOrderFulfillmentIU = new DtoOrderFulfillmentIU();
        BeanUtils.copyProperties(data, dtoOrderFulfillmentIU);

        OrderFulfillmentAndStatusRequestIU orderFulfillmentAndStatusRequestIU = new OrderFulfillmentAndStatusRequestIU();
        orderFulfillmentAndStatusRequestIU.setOrderId(dtoOrderFulfillmentIU.getOrderId());
        orderFulfillmentAndStatusRequestIU.setTrackingCode(dtoOrderFulfillmentIU.getOrderForm().getBarcodes().getCode());
        orderFulfillmentAndStatusRequestIU.setTrackingUrl(dtoOrderFulfillmentIU.getOrderForm().getBarcodes().getCargoTrackingUrl());

        ResponseEntity<OrderFulfillmentAndStatusResponse> responseEntity = super.sendRequest(orderFulfillmentAndStatusRequestIU, data, OrderFulfillmentAndStatusResponse.class, null, "api/v1/offline-ship/" + status);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "order status update error" + dtoOrderFulfillmentIU.getOrderId()));
        }
        if (Objects.nonNull(Objects.requireNonNull(responseEntity.getBody()).getData().getError())) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "order status update error" + dtoOrderFulfillmentIU.getOrderId()));
        }
    }
}
