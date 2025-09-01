package com.mehmetsadullahguven.scheduled;

import com.mehmetsadullahguven.adaptors.impl.Aliexpress.AbstractAliexpress;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product.ProductObject;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product.ProductResponse;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product.Products;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AliexpressBulkRequest extends AbstractAliexpress {

    @Autowired
    private IIntegrationRepository integrationRepository;

    //@Scheduled(cron = "0 * * * * ?")
    public void bulkRequest() {

        List<Integration> aliexpressList = integrationRepository.findAllBySlug("aliexpress");

        if (aliexpressList.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "Aliexpress does not exist"));
        }

        aliexpressList.forEach(aliexpress -> {
            bulkOperations(aliexpress, "create", "/api/v1/product/create");
            bulkOperations(aliexpress, "update", "/api/v1/products/edit");
        });
    }

    private void bulkOperations(Integration integration, String status, String path) {
        int currentPage = 0;
        final int pageSize = 1000;

        while (true) {
            Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("updatedAt").ascending());
            Page<Product> products = productRepository.findByStatusAndR2sProductListId(status + "_bulk_waiting", integration.getR2sProductListId(), pageable);

            List<ProductObject> productObjects = new ArrayList<>();
            products.getContent().forEach(product -> {
                ProductObject productObject = getSerializedObject(product.getSerializedData(), ProductObject.class);
                productObjects.add(productObject);
            });

            Products requestProducts = new Products();
            requestProducts.setProducts(productObjects);

            HttpEntity<ProductResponse> response = postRequest(integration, requestProducts, path);

            updateDbProduct(Objects.requireNonNull(response.getBody()), integration);

            if (products.isLast()) {
                break;
            }

        }
    }

    private void updateDbProduct(ProductResponse productResponse, Integration integration) {
        productResponse.getResults().forEach(productResponseResult -> {
            if (productResponseResult.getOk()) {
                Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(productResponseResult.getExternalId(), integration.getR2sProductListId());
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setStatus("command_waiting");
                    product.setCorrelationId(productResponse.getGroup_id());
                    productRepository.save(product);
                }
            }else {
                Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(productResponseResult.getExternalId(), integration.getR2sProductListId());
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setStatus("error");
                    product.setCorrelationId(productResponse.getGroup_id());
                    productRepository.save(product);
                }
            }
        });
    }

    private ResponseEntity<ProductResponse> postRequest(Integration integration, Products products, String path) {
        RestTemplate restTemplate = new RestTemplate();
        String url = integration.getBaseUrl() + path;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-auth-token", integration.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(setSerializedObject(products), httpHeaders);
        ResponseEntity<ProductResponse> responseEntity = restTemplate.postForEntity(url, requestEntity, ProductResponse.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "Aliexpress Bulk request failed"));
        }
        return responseEntity;
    }
}
