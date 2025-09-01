package com.mehmetsadullahguven.service;


import com.mehmetsadullahguven.dto.DtoIntegration;
import com.mehmetsadullahguven.dto.DtoIntegrationIU;

public interface IIntegrationService {

    public DtoIntegration createOrUpdate(DtoIntegrationIU request);

}
