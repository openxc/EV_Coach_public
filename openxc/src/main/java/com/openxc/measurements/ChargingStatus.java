package com.openxc.measurements;

import com.openxc.units.*;
import com.openxc.units.Boolean;

/**
 * Created by michael on 2/29/16.
 *
 * True if the car is charging
 */
public class ChargingStatus extends BaseMeasurement<Boolean> {
    public final static String ID = "charging_status";

    public ChargingStatus(com.openxc.units.Boolean value) {
        super(value);
    }

    public ChargingStatus(java.lang.Boolean value) {
        this(new Boolean(value));
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
