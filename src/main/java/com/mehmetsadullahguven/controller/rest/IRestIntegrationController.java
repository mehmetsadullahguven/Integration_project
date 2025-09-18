package com.mehmetsadullahguven.controller.rest;

import com.mehmetsadullahguven.dto.DtoIntegration;
import com.mehmetsadullahguven.dto.DtoIntegrationIU;

public interface IRestIntegrationController {
    public RootEntity<DtoIntegration> createOrUpdate(DtoIntegrationIU dtoIntegrationIU);
}
