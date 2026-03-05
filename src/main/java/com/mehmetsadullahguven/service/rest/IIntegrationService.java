package com.mehmetsadullahguven.service.rest;


import com.mehmetsadullahguven.dto.integration.DtoIntegration;
import com.mehmetsadullahguven.dto.integration.DtoIntegrationIU;

public interface IIntegrationService {

    public DtoIntegration createOrUpdate(DtoIntegrationIU request);

}
