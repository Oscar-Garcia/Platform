/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.infra.jms.ws.messageprocessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.domain.smartmetering.application.services.ConfigurationService;
import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.ws.WebServiceRequestMessageProcessor;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.PushSetupSms;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;

@Component("domainSmartmeteringSetPushSetupSmsRequestMessageProcessor")
public class SetPushSetupSmsRequestMessageProcessor extends WebServiceRequestMessageProcessor {

    @Autowired
    @Qualifier("domainSmartMeteringConfigurationService")
    private ConfigurationService configurationService;

    protected SetPushSetupSmsRequestMessageProcessor() {
        super(DeviceFunction.SET_PUSH_SETUP_SMS);
    }

    @Override
    protected void handleMessage(final String organisationIdentification, final String deviceIdentification,
            final String correlationUid, final Object dataObject, final String messageType) throws FunctionalException {

        final PushSetupSms pushSetupSms = (PushSetupSms) dataObject;

        this.configurationService.setPushSetupSms(organisationIdentification, deviceIdentification, correlationUid,
                pushSetupSms, messageType);
    }
}