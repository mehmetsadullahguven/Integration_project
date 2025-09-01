package com.mehmetsadullahguven.adaptors.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehmetsadullahguven.dto.*;
import com.mehmetsadullahguven.enums.LanguageType;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.Order;
import com.mehmetsadullahguven.model.Product;
import com.mehmetsadullahguven.repository.IOrderRepository;
import com.mehmetsadullahguven.repository.IProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AbstractAdaptor {

    @Autowired
    protected IProductRepository productRepository;

    @Autowired
    protected IOrderRepository orderRepository;

    protected <T, K, L> ResponseEntity<L> sendRequest(T requestBody, K requestHeaders, Class<L> responseType, String correlationId, String path) {
        DtoBaseRequest dtoBaseRequest = new DtoBaseRequest();
        BeanUtils.copyProperties(requestHeaders, dtoBaseRequest);

        RestTemplate restTemplate = new RestTemplate();
        String url = dtoBaseRequest.getBaseUrl() + path;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", dtoBaseRequest.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (Objects.nonNull(correlationId)) {
            httpHeaders.set("X-Correlation-ID", correlationId);
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(setSerializedObject(requestBody), httpHeaders);

        return restTemplate.postForEntity(url, httpEntity, responseType);
    }

    protected <T> String setSerializedObject(T body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T getSerializedObject(String serializedProduct, Class<T> type) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(serializedProduct, type);
        } catch (Exception e) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, e.getMessage()));
        }
    }

    protected <T> T getTranslation(List<T> translations) {
        for (T translation : translations) {
            if (Objects.isNull(translation)) {
                continue;
            }
            DtoBaseTranslation translationObject = new DtoBaseTranslation();
            BeanUtils.copyProperties(translation, translationObject);
            if (Objects.equals(translationObject.getLanguage(), LanguageType.en)) {
                return translation;
            }
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "translation not found"));
    }

    protected Product saveProduct(DtoProductIU dtoProductIU, String serializedProduct, String operation) {
        Product product;

        if (operation.equalsIgnoreCase("create_bulk_waiting")) {
            product = new Product();
            product.setR2sProductId(dtoProductIU.getR2sProductId());
            product.setR2sProductListId(dtoProductIU.getR2sProductListId());
            product.setCreatedAt(new Date());
        } else {
            Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(dtoProductIU.getR2sProductId(), dtoProductIU.getR2sProductListId());
            if (optionalProduct.isEmpty()) {
                throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "product not found"));
            }
            product = optionalProduct.get();
        }

        product.setStatus(operation);
        product.setUpdatedAt(new Date());
        product.setSerializedData(serializedProduct);
        product = productRepository.save(product);
        return product;
    }

    protected void saveOrder(DtoOrder dtoOrder, DtoOrderIU dtoOrderIU, String correlationId, String status) {

        Order order = new Order();
        order.setR2sProductListId(dtoOrderIU.getR2sProductListId());
        order.setStatus(status);
        order.setUpdatedAt(new Date());
        order.setCreatedAt(new Date());
        if (Objects.nonNull(dtoOrder)) {
            order.setClientOrderId(dtoOrder.getOrderId());
            order.setSerializedData(setSerializedObject(dtoOrder));
        }
        if (Objects.nonNull(correlationId)) {
            order.setCorrelationId(correlationId);
        }
        orderRepository.save(order);
    }
}
