package com.mehmetsadullahguven.adaptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GeneralAdaptorFactory {

    private final Map<String, IGeneralAdaptor> adaptors;

    @Autowired
    public GeneralAdaptorFactory(Map<String, IGeneralAdaptor> adaptors) {
        this.adaptors = adaptors;
    }

    public IGeneralAdaptor getAdaptor(String integration) {
        return adaptors.get(integration);
    }

}
