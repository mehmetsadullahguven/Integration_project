package com.mehmetsadullahguven.service.impl;

import com.mehmetsadullahguven.dto.DtoIntegration;
import com.mehmetsadullahguven.dto.DtoIntegrationIU;
import com.mehmetsadullahguven.model.Integration;
import com.mehmetsadullahguven.repository.IIntegrationRepository;
import com.mehmetsadullahguven.service.IIntegrationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IntegrationServiceImpl implements IIntegrationService {
    
    @Autowired
    private IIntegrationRepository integrationRepository;
    
    @Override
    public DtoIntegration createOrUpdate(DtoIntegrationIU dtoIntegrationIU) {
        Optional<Integration> optionalIntegration = integrationRepository.findBySlugAndR2sProductListId(dtoIntegrationIU.getSlug(), dtoIntegrationIU.getR2sProductListId());
        Integration integration = null;
        if (optionalIntegration.isEmpty()) {
            integration = new Integration();
            integration.setSlug(dtoIntegrationIU.getSlug());
            integration.setTitle(dtoIntegrationIU.getTitle());
            integration.setR2sProductListId(dtoIntegrationIU.getR2sProductListId());
            integration.setCreatedAt(new Date());
        }else {
            integration = optionalIntegration.get();
        }
        integration.setUpdatedAt(new Date());
        integration.setBaseUrl(dtoIntegrationIU.getBaseUrl());
        integration.setAccessToken(dtoIntegrationIU.getAccessToken());

        Integration savedIntegration = integrationRepository.save(integration);

        DtoIntegration dtoIntegration = new DtoIntegration();
        BeanUtils.copyProperties(savedIntegration, dtoIntegration);
        return dtoIntegration;
    }
}
