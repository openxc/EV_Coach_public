package com.openxc.measurements;

import com.openxc.units.Percentage;
import com.openxc.util.Range;

/**
 * The LastRegenEventScore is the percentage of energy recovered during last
 * brake event.
 */
public class LastRegenEventScore extends BaseMeasurement<Percentage> {
    private final static Range<Percentage> RANGE =
        new Range<Percentage>(new Percentage(0), new Percentage(100));
    public final static String ID = "last_regen_event_score";

    public LastRegenEventScore(Number value) {
        super(new Percentage(value), RANGE);
    }

    public LastRegenEventScore(Percentage value) {
        super(value, RANGE);
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}