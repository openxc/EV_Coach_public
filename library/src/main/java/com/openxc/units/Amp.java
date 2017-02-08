package com.openxc.units;

/**
 * An Amp is a unit of current.
 */
public class Amp extends Quantity<Number> {
    private final String TYPE_STRING = "A";

    public Amp(Number value) {
        super(value);
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
