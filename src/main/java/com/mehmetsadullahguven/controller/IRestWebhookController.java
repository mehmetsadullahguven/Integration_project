package com.mehmetsadullahguven.controller;

import com.mehmetsadullahguven.dto.DtoWebhookIU;

public interface IRestWebhookController {

    public void webhook(DtoWebhookIU dtoWebhookIU, String slug);

}
