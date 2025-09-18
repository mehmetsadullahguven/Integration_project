package com.mehmetsadullahguven.controller.rest;

import com.mehmetsadullahguven.dto.DtoWebhookIU;

public interface IRestWebhookController {

    public void webhook(DtoWebhookIU dtoWebhookIU, String slug);

}
