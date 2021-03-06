/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.microgrids.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.alliander.osgp.domain.core.exceptions.PlatformException;

@EnableJpaRepositories(transactionManagerRef = "coreTransactionManager", entityManagerFactoryRef = "coreEntityManagerFactory", basePackageClasses = {
        com.alliander.osgp.domain.core.repositories.DeviceRepository.class,
        com.alliander.osgp.domain.microgrids.repositories.RtuDeviceRepository.class })
@Configuration
@PropertySource("classpath:osgp-adapter-ws-microgrids.properties")
@PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true)
@PropertySource(value = "file:${osgp/AdapterWsMicrogrids/config}", ignoreResourceNotFound = true)
public class PersistenceConfigCore extends AbstractPersistenceConfigBase {

    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username.core";
    private static final String PROPERTY_NAME_DATABASE_PW = "db.password.core";

    private static final String PROPERTY_NAME_DATABASE_HOST = "db.host.core";
    private static final String PROPERTY_NAME_DATABASE_PORT = "db.port.core";
    private static final String PROPERTY_NAME_DATABASE_NAME = "db.name.core";

    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan.core";

    public PersistenceConfigCore() {
        super("OSGP_CORE_MICROGRIDS", PROPERTY_NAME_DATABASE_USERNAME, PROPERTY_NAME_DATABASE_PW,
                PROPERTY_NAME_DATABASE_HOST, PROPERTY_NAME_DATABASE_PORT, PROPERTY_NAME_DATABASE_NAME,
                PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN, PersistenceConfigCore.class);
    }

    @Bean(name = "coreTransactionManager")
    public JpaTransactionManager coreTransactionManager() throws PlatformException {
        return this.createTransactionManager();
    }

    @Bean(name = "coreEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean coreEntityManagerFactory() throws ClassNotFoundException {
        return this.createEntityManagerFactory();
    }
}
