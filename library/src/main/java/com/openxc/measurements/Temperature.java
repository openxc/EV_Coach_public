package com.openxc.measurements;

import com.openxc.units.Celsius;
import com.openxc.util.Range;

/**
 * Created by michael on 2/29/16.
 *
 * Finds the temperature of the engine in Celsius
 */
public class Temperature extends BaseMeasurement<Celsius> {
    private final static Range<Celsius> RANGE =
            new Range<Celsius>(new Celsius(-1000), new Celsius(1000));
    public final static String ID = "battery_temperature";

    public Temperature(Number value) {
        super(new Celsius(value), RANGE);
    }
    public Temperature(Celsius value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
