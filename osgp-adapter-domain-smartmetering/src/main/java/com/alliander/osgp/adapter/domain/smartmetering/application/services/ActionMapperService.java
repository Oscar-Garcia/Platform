/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import ma.glasnost.orika.impl.ConfigurableMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.CommonMapper;
import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.ConfigurationMapper;
import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.ManagementMapper;
import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.MonitoringMapper;
import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.PeriodicReadsRequestGasDataConverter;
import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.customconverters.ActualMeterReadsRequestGasRequestDataConverter;
import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.customconverters.CustomValueToDtoConverter;
import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.customconverters.SetEncryptionKeyExchangeOnGMeterDataConverter;
import com.alliander.osgp.domain.core.entities.SmartMeter;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.ActionRequest;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.ActivityCalendarData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.ActualMeterReadsGasRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.ActualMeterReadsRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.AdministrativeStatusTypeData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.BundleMessageDataContainer;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.FindEventsQuery;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.GetAdministrativeStatusData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.GetConfigurationRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.GetFirmwareVersionRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.KeySet;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReadsGasRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReadsRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.ReadAlarmRegisterData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SetAlarmNotificationsRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SetConfigurationObjectRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SetEncryptionKeyExchangeOnGMeterRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SetPushSetupAlarmRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SetPushSetupSmsRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SpecialDaysRequestData;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SynchronizeTimeRequestData;
import com.alliander.osgp.dto.valueobjects.smartmetering.ActionDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ActivityCalendarDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ActualMeterReadsDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.AdministrativeStatusTypeDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.BundleMessageDataContainerDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.FindEventsQueryDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.GetAdministrativeStatusDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.GetConfigurationRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.GetFirmwareVersionRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.KeySetDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.PeriodicMeterReadsRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ReadAlarmRegisterDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SetAlarmNotificationsRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SetConfigurationObjectRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SetPushSetupAlarmRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SetPushSetupSmsRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SpecialDaysRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SynchronizeTimeRequestDataDto;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.FunctionalExceptionType;

@Service(value = "domainSmartMeteringActionMapperService")
@Validated
public class ActionMapperService {

    @Autowired
    @Qualifier("configurationMapper")
    private ConfigurationMapper configurationMapper;

    @Autowired
    private ManagementMapper managementMapper;

    @Autowired
    private MonitoringMapper monitoringMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private DomainHelperService domainHelperService;

    @Autowired
    private PeriodicReadsRequestGasDataConverter periodicReadsRequestGasDataConverter;

    @Autowired
    private ActualMeterReadsRequestGasRequestDataConverter actualReadsRequestGasDataConverter;

    @Autowired
    private SetEncryptionKeyExchangeOnGMeterDataConverter setEncryptionKeyExchangeOnGMeterDataConverter;

    private static Map<Class<? extends ActionRequest>, ConfigurableMapper> CLASS_TO_MAPPER_MAP = new HashMap<>();
    private static Map<Class<? extends ActionRequest>, CustomValueToDtoConverter<? extends ActionRequest, ? extends ActionDto>> CUSTOM_CONVERTER_FOR_CLASS = new HashMap<>();

    /**
     * Specifies which mapper to use for the core class received.
     */
    @PostConstruct
    private void postConstruct() {

        CUSTOM_CONVERTER_FOR_CLASS.put(PeriodicMeterReadsGasRequestData.class,
                this.periodicReadsRequestGasDataConverter);
        CUSTOM_CONVERTER_FOR_CLASS.put(ActualMeterReadsGasRequestData.class, this.actualReadsRequestGasDataConverter);
        CUSTOM_CONVERTER_FOR_CLASS.put(SetEncryptionKeyExchangeOnGMeterRequestData.class,
                this.setEncryptionKeyExchangeOnGMeterDataConverter);

        CLASS_TO_MAPPER_MAP.put(PeriodicMeterReadsRequestData.class, this.monitoringMapper);
        CLASS_TO_MAPPER_MAP.put(ActualMeterReadsRequestData.class, this.commonMapper);
        CLASS_TO_MAPPER_MAP.put(SpecialDaysRequestData.class, this.commonMapper);
        CLASS_TO_MAPPER_MAP.put(ReadAlarmRegisterData.class, this.monitoringMapper);
        CLASS_TO_MAPPER_MAP.put(FindEventsQuery.class, this.managementMapper);
        CLASS_TO_MAPPER_MAP.put(GetAdministrativeStatusData.class, this.commonMapper);
        CLASS_TO_MAPPER_MAP.put(AdministrativeStatusTypeData.class, this.configurationMapper);
        CLASS_TO_MAPPER_MAP.put(ActivityCalendarData.class, this.configurationMapper);
        CLASS_TO_MAPPER_MAP.put(SetConfigurationObjectRequestData.class, this.configurationMapper);
        CLASS_TO_MAPPER_MAP.put(SetAlarmNotificationsRequestData.class, this.configurationMapper);
        CLASS_TO_MAPPER_MAP.put(SetConfigurationObjectRequestData.class, this.configurationMapper);
        CLASS_TO_MAPPER_MAP.put(SetPushSetupAlarmRequestData.class, this.configurationMapper);
        CLASS_TO_MAPPER_MAP.put(SetPushSetupSmsRequestData.class, this.configurationMapper);
        CLASS_TO_MAPPER_MAP.put(SynchronizeTimeRequestData.class, this.commonMapper);
        CLASS_TO_MAPPER_MAP.put(GetConfigurationRequestData.class, this.commonMapper);
        CLASS_TO_MAPPER_MAP.put(GetFirmwareVersionRequestData.class, this.commonMapper);
        CLASS_TO_MAPPER_MAP.put(KeySet.class, this.configurationMapper);

    }

    /**
     * Specifies to which DTO object the core object needs to be mapped.
     */
    private static Map<Class<? extends ActionRequest>, Class<? extends ActionDto>> CLASS_MAP = new HashMap<>();
    static {
        CLASS_MAP.put(PeriodicMeterReadsRequestData.class, PeriodicMeterReadsRequestDataDto.class);
        CLASS_MAP.put(ActualMeterReadsRequestData.class, ActualMeterReadsDataDto.class);
        CLASS_MAP.put(SpecialDaysRequestData.class, SpecialDaysRequestDataDto.class);
        CLASS_MAP.put(ReadAlarmRegisterData.class, ReadAlarmRegisterDataDto.class);
        CLASS_MAP.put(FindEventsQuery.class, FindEventsQueryDto.class);
        CLASS_MAP.put(GetAdministrativeStatusData.class, GetAdministrativeStatusDataDto.class);
        CLASS_MAP.put(AdministrativeStatusTypeData.class, AdministrativeStatusTypeDataDto.class);
        CLASS_MAP.put(ActivityCalendarData.class, ActivityCalendarDataDto.class);
        CLASS_MAP.put(SetAlarmNotificationsRequestData.class, SetAlarmNotificationsRequestDataDto.class);
        CLASS_MAP.put(SetConfigurationObjectRequestData.class, SetConfigurationObjectRequestDataDto.class);
        CLASS_MAP.put(SetPushSetupAlarmRequestData.class, SetPushSetupAlarmRequestDataDto.class);
        CLASS_MAP.put(SetPushSetupSmsRequestData.class, SetPushSetupSmsRequestDataDto.class);
        CLASS_MAP.put(SynchronizeTimeRequestData.class, SynchronizeTimeRequestDataDto.class);
        CLASS_MAP.put(GetConfigurationRequestData.class, GetConfigurationRequestDataDto.class);
        CLASS_MAP.put(GetFirmwareVersionRequestData.class, GetFirmwareVersionRequestDataDto.class);
        CLASS_MAP.put(KeySet.class, KeySetDto.class);

    }

    public BundleMessageDataContainerDto mapAllActions(final BundleMessageDataContainer bundleMessageDataContainer,
            final SmartMeter smartMeter) throws FunctionalException {

        final List<ActionDto> actionValueObjectDtoList = new ArrayList<ActionDto>();

        for (final ActionRequest action : bundleMessageDataContainer.getBundleList()) {

            @SuppressWarnings("unchecked")
            // suppress else the compiler will complain
            final CustomValueToDtoConverter<ActionRequest, ActionDto> customValueToDtoConverter = (CustomValueToDtoConverter<ActionRequest, ActionDto>) CUSTOM_CONVERTER_FOR_CLASS
            .get(action.getClass());

            if (customValueToDtoConverter != null) {
                actionValueObjectDtoList.add(customValueToDtoConverter.convert(action, smartMeter));
            } else {

                final ConfigurableMapper mapper = CLASS_TO_MAPPER_MAP.get(action.getClass());
                final Class<? extends ActionDto> clazz = CLASS_MAP.get(action.getClass());
                if (mapper != null) {
                    actionValueObjectDtoList.add(this.performDefaultMapping(action, mapper, clazz));
                } else {
                    throw new FunctionalException(FunctionalExceptionType.VALIDATION_ERROR,
                            ComponentType.DOMAIN_SMART_METERING, new AssertionError("No mapper defined for class: "
                                    + clazz.getName()));
                }
            }
        }
        return new BundleMessageDataContainerDto(actionValueObjectDtoList);
    }

    private ActionDto performDefaultMapping(final ActionRequest action, final ConfigurableMapper mapper,
            final Class<? extends ActionDto> clazz) throws FunctionalException {
        final ActionDto actionValueObjectDto = mapper.map(action, clazz);

        if (actionValueObjectDto == null) {
            throw new FunctionalException(FunctionalExceptionType.UNSUPPORTED_DEVICE_ACTION,
                    ComponentType.DOMAIN_SMART_METERING, new RuntimeException("Object: " + action.getClass().getName()
                            + " could not be converted to " + clazz.getName()));
        }
        return actionValueObjectDto;
    }

}