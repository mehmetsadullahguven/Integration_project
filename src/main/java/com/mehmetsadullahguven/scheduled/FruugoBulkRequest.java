package com.mehmetsadullahguven.scheduled;

import com.mehmetsadullahguven.adaptors.impl.Fruugo.AbstractFruugo;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product.ProductObject;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product.Products;
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

@Component
public class FruugoBulkRequest extends AbstractFruugo {

    @Autowired
    private IIntegrationRepository integrationRepository;

    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = CHARACTERS.length();

    //@Scheduled(cron = "0 * * * * ?")
    public void bulkRequest() {

        List<Integration> fruugoList = integrationRepository.findAllBySlug("fruugo");

        if (fruugoList.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "Fruugo does not exist"));
        }

        fruugoList.forEach(fruugo -> {
            bulkOperations(fruugo, "create", "/v1/products");
            bulkOperations(fruugo, "update", "/v1/products");
            bulkOperations(fruugo, "patch", "/v1/products/partial");
            bulkOperations(fruugo, "delete", "/v1/products/partial");
        });
    }

    private void bulkOperations(Integration integration, String status, String path) {
        int currentPage = 0;
        final int pageSize = 100;

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

            postRequest(integration, products.getContent(), requestProducts, path);

            if (products.isLast()) {
                break;
            }

        }
    }


    private void postRequest(Integration integration, List<Product> dbProductList, Products products, String path) {
        RestTemplate restTemplate = new RestTemplate();
        String url = integration.getBaseUrl() + path;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(integration.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(setSerializedObject(products), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String correlationId = correlationEncodeId(System.currentTimeMillis() / 1000);
            dbProductList.forEach(product -> {
                product.setStatus("webhook_waiting");
                product.setCorrelationId(correlationId);
            });
            productRepository.saveAll(dbProductList);
        }
    }

    public static String correlationEncodeId(long timestamp) {
        long combinedId = timestamp;
        StringBuilder code = new StringBuilder();

        while (combinedId > 0) {
            code.insert(0, CHARACTERS.charAt((int) (combinedId % BASE)));
            combinedId /= BASE;
        }

        return code.toString();
    }

}
