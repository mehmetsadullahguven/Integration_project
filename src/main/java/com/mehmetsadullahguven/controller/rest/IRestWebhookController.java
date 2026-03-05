package com.mehmetsadullahguven.controller.rest;

import com.mehmetsadullahguven.dto.webhook.DtoWebhookIU;

public interface IRestWebhookController {

    public void webhook(DtoWebhookIU dtoWebhookIU, String slug);

}
