package com.mehmetsadullahguven.controller.rest.impl;

import com.mehmetsadullahguven.controller.rest.RootEntity;
import com.mehmetsadullahguven.controller.rest.RestBaseController;
import com.mehmetsadullahguven.dto.integration.DtoIntegration;
import com.mehmetsadullahguven.dto.integration.DtoIntegrationIU;
import com.mehmetsadullahguven.service.rest.IIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/integration/{slug}")
@RestController
public class RestIntegrationController extends RestBaseController {

    @Autowired
    private IIntegrationService integrationService;

    public RootEntity<DtoIntegration> createOrUpdate(DtoIntegrationIU dtoIntegrationIU) {
        return ok(integrationService.createOrUpdate(dtoIntegrationIU), "success");
    }
}
