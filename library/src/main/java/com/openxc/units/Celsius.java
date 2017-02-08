package com.openxc.units;

/**
 * Created by michael on 2/29/16.
 *
 * Unit of temperature
 */
public class Celsius extends Quantity<Number> {
    private final String TYPE_STRING = "°C";

    public Celsius(Number value) {
        super(value);
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
