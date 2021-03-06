/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.ws.WebServiceResponseMessageSender;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.PushNotificationAlarm;
import com.alliander.osgp.dto.valueobjects.smartmetering.PushNotificationAlarmDto;
import com.alliander.osgp.shared.infra.jms.DeviceMessageMetadata;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;
import com.alliander.osgp.shared.infra.jms.ResponseMessageResultType;

import ma.glasnost.orika.MapperFactory;

@Service(value = "domainSmartMeteringNotificationService")
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private MapperFactory mapperFactory;

    @Autowired
    private WebServiceResponseMessageSender webServiceResponseMessageSender;

    public void handlePushNotificationAlarm(final DeviceMessageMetadata deviceMessageMetadata,
            final PushNotificationAlarmDto pushNotificationAlarm) {

        LOGGER.info("handlePushNotificationAlarm for MessageType: {}", deviceMessageMetadata.getMessageType());

        final PushNotificationAlarm pushNotificationAlarmDomain = this.mapperFactory.getMapperFacade()
                .map(pushNotificationAlarm, PushNotificationAlarm.class);

        /*
         * Send the push notification alarm as a response message to the web
         * service, so it can be handled similar to response messages based on
         * earlier web service requests.
         */
        this.webServiceResponseMessageSender.send(
                new ResponseMessage(deviceMessageMetadata.getCorrelationUid(),
                        deviceMessageMetadata.getOrganisationIdentification(),
                        deviceMessageMetadata.getDeviceIdentification(), ResponseMessageResultType.OK, null,
                        pushNotificationAlarmDomain, deviceMessageMetadata.getMessagePriority()),
                deviceMessageMetadata.getMessageType());
    }

}
