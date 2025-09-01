package com.mehmetsadullahguven.controller.impl;

import com.mehmetsadullahguven.controller.IRestIntegrationController;
import com.mehmetsadullahguven.controller.RootEntity;
import com.mehmetsadullahguven.dto.DtoIntegration;
import com.mehmetsadullahguven.dto.DtoIntegrationIU;
import com.mehmetsadullahguven.service.IIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/integration/{slug}")
@RestController
public class RestIntegrationControllerImpl implements IRestIntegrationController {

    @Autowired
    private IIntegrationService integrationService;

    @Override
    public RootEntity<DtoIntegration> createOrUpdate(DtoIntegrationIU dtoIntegrationIU) {
        return RootEntity.ok(integrationService.createOrUpdate(dtoIntegrationIU), "success");
    }
}
