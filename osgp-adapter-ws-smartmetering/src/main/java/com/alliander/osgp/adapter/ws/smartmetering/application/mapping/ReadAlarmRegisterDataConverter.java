/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.smartmetering.application.mapping;

import com.alliander.osgp.domain.core.valueobjects.smartmetering.ReadAlarmRegisterData;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class ReadAlarmRegisterDataConverter extends
        CustomConverter<com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.ReadAlarmRegisterData, ReadAlarmRegisterData> {

    @Override
    public ReadAlarmRegisterData convert(
            final com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.ReadAlarmRegisterData source,
            final Type<? extends ReadAlarmRegisterData> destinationType, final MappingContext context) {
        return new ReadAlarmRegisterData();
    }

}
