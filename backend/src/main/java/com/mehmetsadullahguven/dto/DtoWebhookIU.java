package com.mehmetsadullahguven.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoWebhookIU {

    private String error;

    private String type;

    private String correlationId;

    private DtoWebhookPayloadIU webhookPayload;
}
