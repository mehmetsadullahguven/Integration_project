package com.mehmetsadullahguven.controller.rest.impl;

import com.mehmetsadullahguven.controller.rest.IRestIntegrationController;
import com.mehmetsadullahguven.controller.rest.RootEntity;
import com.mehmetsadullahguven.controller.rest.RestBaseController;
import com.mehmetsadullahguven.dto.DtoIntegration;
import com.mehmetsadullahguven.dto.DtoIntegrationIU;
import com.mehmetsadullahguven.service.IIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/integration/{slug}")
@RestController
public class RestIntegrationControllerImpl extends RestBaseController implements IRestIntegrationController {

    @Autowired
    private IIntegrationService integrationService;

    @Override
    public RootEntity<DtoIntegration> createOrUpdate(DtoIntegrationIU dtoIntegrationIU) {
        return ok(integrationService.createOrUpdate(dtoIntegrationIU), "success");
    }
}
