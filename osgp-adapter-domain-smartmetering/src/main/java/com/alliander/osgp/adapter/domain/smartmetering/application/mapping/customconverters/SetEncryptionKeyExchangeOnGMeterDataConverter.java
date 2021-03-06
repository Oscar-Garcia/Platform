/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.mapping.customconverters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.domain.smartmetering.application.services.DomainHelperService;
import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.SmartMeter;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SetEncryptionKeyExchangeOnGMeterRequestData;
import com.alliander.osgp.dto.valueobjects.smartmetering.GMeterInfoDto;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.FunctionalExceptionType;

@Component
public class SetEncryptionKeyExchangeOnGMeterDataConverter implements
        CustomValueToDtoConverter<SetEncryptionKeyExchangeOnGMeterRequestData, GMeterInfoDto> {

    @Autowired
    private DomainHelperService domainHelperService;

    @Override
    public GMeterInfoDto convert(final SetEncryptionKeyExchangeOnGMeterRequestData value, final SmartMeter smartMeter)
            throws FunctionalException {

        final SmartMeter gasDevice = this.domainHelperService.findSmartMeter(value.getDeviceIdentification());

        final Device gatewayDevice = gasDevice.getGatewayDevice();
        if (gatewayDevice == null) {
            /*
             * For now throw a FunctionalException, based on the same reasoning
             * as with the channel a couple of lines up. As soon as we have
             * scenario's with direct communication with gas meters this will
             * have to be changed.
             */
            throw new FunctionalException(FunctionalExceptionType.VALIDATION_ERROR,
                    ComponentType.DOMAIN_SMART_METERING, new AssertionError(
                            "Meter for gas reads should have an energy meter as gateway device."));
        }

        return new GMeterInfoDto(gasDevice.getChannel(), gasDevice.getDeviceIdentification());
    }
}
