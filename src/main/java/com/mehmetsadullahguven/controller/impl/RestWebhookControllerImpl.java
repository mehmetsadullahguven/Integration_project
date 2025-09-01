package com.mehmetsadullahguven.controller.impl;

import com.mehmetsadullahguven.adaptors.IGeneralAdaptor;
import com.mehmetsadullahguven.adaptors.GeneralAdaptorFactory;
import com.mehmetsadullahguven.controller.IRestWebhookController;
import com.mehmetsadullahguven.dto.DtoWebhookIU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestWebhookControllerImpl implements IRestWebhookController {

    @Autowired
    private GeneralAdaptorFactory generalAdaptorFactory;

    @PostMapping("/webhook/{slug}")
    @Override
    public void webhook(DtoWebhookIU dtoWebhookIU, String slug) {
        IGeneralAdaptor adaptor = generalAdaptorFactory.getAdaptor(slug);
        adaptor.webhook(dtoWebhookIU);
    }
}
