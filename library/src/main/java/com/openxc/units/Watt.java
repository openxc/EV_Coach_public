package com.openxc.units;

/**
 * A Watt is a unit of power.
 */
public class Watt extends Quantity<Number> {
    private final String TYPE_STRING = "W";

    public Watt(Number value) {
        super(value);
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
