package com.mehmetsadullahguven.controller.rest.impl;


import com.mehmetsadullahguven.controller.rest.IRestWebhookController;
import com.mehmetsadullahguven.dto.DtoWebhookIU;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestWebhookControllerImpl implements IRestWebhookController {

    @PostMapping("/webhook/{slug}")
    @Override
    public void webhook(DtoWebhookIU dtoWebhookIU, String slug) {

    }
}
