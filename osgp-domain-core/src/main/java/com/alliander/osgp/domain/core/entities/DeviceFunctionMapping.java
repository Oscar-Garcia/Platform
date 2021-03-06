/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.entities;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunctionGroup;
import com.alliander.osgp.shared.domain.entities.AbstractEntity;

@Entity
public class DeviceFunctionMapping extends AbstractEntity {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 988621596635222266L;

    @Enumerated(EnumType.STRING)
    private DeviceFunctionGroup functionGroup;

    @Enumerated(EnumType.STRING)
    private DeviceFunction function;

    public DeviceFunctionMapping() {
        // Default constructor
    }

    public DeviceFunctionMapping(final DeviceFunctionGroup functionGroup, final DeviceFunction function) {
        this.functionGroup = functionGroup;
        this.function = function;
    }

    public DeviceFunctionGroup getFunctionGroup() {
        return this.functionGroup;
    }

    public DeviceFunction getFunction() {
        return this.function;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceFunctionMapping)) {
            return false;
        }
        final DeviceFunctionMapping mapping = (DeviceFunctionMapping) o;
        final boolean isDeviceFunctionGroupEqual = Objects.equals(this.functionGroup, mapping.functionGroup);
        final boolean isDeviceFunctionEqual = Objects.equals(this.function, mapping.function);

        return isDeviceFunctionGroupEqual && isDeviceFunctionEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.functionGroup, this.function);
    }
}
