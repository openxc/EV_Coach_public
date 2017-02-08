package com.openxc.measurements;

import com.openxc.units.Amp;
import com.openxc.util.Range;

/**
 * The HvBatteryCurrent measurement represents the current out of the HV
 * battery pack. The DC to DC converter handles charging and functions
 * of the 12V system of the car.
 *
 * The valid range for this measurement is from -1000 to 1000A(?).
 */
public class HvBatteryCurrent extends BaseMeasurement<Amp> {
    private final static Range<Amp> RANGE =
        new Range<Amp>(new Amp(-1000), new Amp(1000));
    public final static String ID = "hv_battery_current";

    public HvBatteryCurrent(Number value) {
        super(new Amp(value), RANGE);
    }
    public HvBatteryCurrent(Amp value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
