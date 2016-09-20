package com.openxc.measurements;

import com.openxc.units.Watt;
import com.openxc.util.Range;

/**
 * The AcCompressorPower measurement represents the power being used by the AC
 * compressor.
 *
 * The valid range for this measurement is from 0 to 100000W(?).
 */
public class AcCompressorPower extends BaseMeasurement<Watt> {
    private final static Range<Watt> RANGE =
        new Range<Watt>(new Watt(0), new Watt(100000));
    public final static String ID = "ac_compressor_power";

    public AcCompressorPower(Number value) {
        super(new Watt(value), RANGE);
    }
    public AcCompressorPower(Watt value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
