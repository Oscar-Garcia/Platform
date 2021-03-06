/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.adapter.ws.da.infra.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.shared.infra.jms.MessageProcessor;
import com.alliander.osgp.shared.infra.jms.MessageProcessorMap;

public class DistributionAutomationResponseMessageListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributionAutomationResponseMessageListener.class);

    @Autowired
    private MessageProcessorMap domainResponseMessageProcessorMap;

    public DistributionAutomationResponseMessageListener() {
        // empty constructor
    }

    @Override
    public void onMessage(final Message message) {
        try {
            LOGGER.info("Received message of type: {}", message.getJMSType());

            final ObjectMessage objectMessage = (ObjectMessage) message;
            final String correlationUid = objectMessage.getJMSCorrelationID();
            LOGGER.info("objectMessage CorrelationUID: {}", correlationUid);

            final MessageProcessor processor = this.domainResponseMessageProcessorMap
                    .getMessageProcessor(objectMessage);

            processor.processMessage(objectMessage);

        } catch (final JMSException ex) {
            LOGGER.error("Exception: {} ", ex.getMessage(), ex);
        }
    }
}
