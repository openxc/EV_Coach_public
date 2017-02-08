package com.openxc.measurements;

import com.openxc.units.Watt;
import com.openxc.util.Range;

/**
 * The RelativeDrivePower measurement represents the power output of the
 * electric drive.
 *
 * The valid range for this measurement is from 0 to 100000W(?).
 */
public class RelativeDrivePower extends BaseMeasurement<Watt> {
    private final static Range<Watt> RANGE =
        new Range<Watt>(new Watt(0), new Watt(100000));
    public final static String ID = "relative_drive_power";

    public RelativeDrivePower(Number value) {
        super(new Watt(value), RANGE);
    }
    public RelativeDrivePower(Watt value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
