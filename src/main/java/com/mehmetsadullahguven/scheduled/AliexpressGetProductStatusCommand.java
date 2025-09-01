package com.mehmetsadullahguven.scheduled;

import com.mehmetsadullahguven.adaptors.impl.Aliexpress.AbstractAliexpress;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product.*;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.Integration;
import com.mehmetsadullahguven.model.Product;
import com.mehmetsadullahguven.repository.IIntegrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AliexpressGetProductStatusCommand extends AbstractAliexpress {

    @Autowired
    private IIntegrationRepository integrationRepository;

    //@Scheduled(cron = "0 * * * * ?")
    public void bulkRequest() {

        List<Integration> aliexpressList = integrationRepository.findAllBySlug("aliexpress");

        if (aliexpressList.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "Aliexpress does not exist"));
        }

        aliexpressList.forEach(this::bulkOperations);
    }

    private void bulkOperations(Integration integration) {
        int currentPage = 0;
        final int pageSize = 1000;

        while (true) {

            Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("updatedAt").ascending());
            Page<Product> products = productRepository.findByStatusAndR2sProductListId("command_waiting", integration.getR2sProductListId(), pageable);

            Map<String, List<Product>> groupedProducts = products.getContent().stream().collect(Collectors.groupingBy(Product::getCorrelationId));

            groupedProducts.forEach((groupId, productsData) -> {
                HttpEntity<ProductStatusResponse> response = getRequest(integration, groupId);

                updateDbProduct(Objects.requireNonNull(Objects.requireNonNull(response.getBody()).getData()), integration);
            });

            if (products.isLast()) {
                break;
            }
        }
    }


    private void updateDbProduct(List<ProductStatusDataResponse> dataResponseList, Integration integration) {
        dataResponseList.forEach(dataResponse -> {

            if (Objects.nonNull(dataResponse.getError())) {
                Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(dataResponse.getExternal_id(), integration.getR2sProductListId());
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setStatus("success");
                    product.setClientProductId(dataResponse.getProduct_id());
                    productRepository.save(product);
                }
            }else {
                Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(dataResponse.getExternal_id(), integration.getR2sProductListId());
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setStatus("error");
                    productRepository.save(product);
                }
            }
        });
    }

    private ResponseEntity<ProductStatusResponse> getRequest(Integration integration, String groupId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = integration.getBaseUrl() + "/api/v1/tasks?group_id=" + groupId;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-auth-token", integration.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<ProductStatusResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ProductStatusResponse.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "Aliexpress Bulk request failed"));
        }
        return responseEntity;
    }
}
