/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.logging.domain.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.alliander.osgp.shared.domain.entities.AbstractEntity;

@Entity
@Table(name = "web_service_monitor_log")
public class WebServiceMonitorLogItem extends AbstractEntity {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1610041397859771290L;

    @Column(name = "time_stamp", nullable = false)
    private Date timeStamp;

    @Column(name = "class_name", length = 255)
    private String className;

    @Column(name = "method_name", length = 255)
    private String methodName;

    @Column(name = "organisation_identification", length = 40)
    private String organisationIdentification;

    @Column(name = "user_name", length = 40)
    private String userName;

    @Column(name = "application_name", length = 40)
    private String applicationName;

    @Column(name = "request_device_identification", length = 40)
    private String requestDeviceIdentification;

    @Column(name = "correlation_uid", length = 255)
    private String correlationUid;

    @Column(name = "response_result", length = 15)
    private String responseResult;

    @Column(name = "response_data_size")
    private int responseDataSize;

    @SuppressWarnings("unused")
    private WebServiceMonitorLogItem() {

    }

    public WebServiceMonitorLogItem(final Date timeStamp, final String organisationIdentification,
            final String userName, final String applicationName, final String className, final String methodName,
            final String requestDeviceId, final String correlationUid, final String responseResult,
            final int responseDataSize) {

        this.timeStamp = (Date) timeStamp.clone();
        this.organisationIdentification = organisationIdentification;
        this.userName = userName;
        this.applicationName = applicationName;
        this.className = className;
        this.methodName = methodName;
        this.requestDeviceIdentification = requestDeviceId;
        this.correlationUid = correlationUid;
        this.responseResult = responseResult;
        this.responseDataSize = responseDataSize;
    }

    public Date getTimeStamp() {
        return (Date) this.timeStamp.clone();
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getOrganisationIdentification() {
        return this.organisationIdentification;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getapplicationName() {
        return this.applicationName;
    }

    public String getRequestDeviceIdentification() {
        return this.requestDeviceIdentification;
    }

    public String getCorrelationUid() {
        return this.correlationUid;
    }

    public String getResponseResult() {
        return this.responseResult;
    }

    public int getResponseDataSize() {
        return this.responseDataSize;
    }
}
