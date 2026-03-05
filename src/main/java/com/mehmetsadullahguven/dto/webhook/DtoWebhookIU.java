package com.mehmetsadullahguven.dto.webhook;

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
