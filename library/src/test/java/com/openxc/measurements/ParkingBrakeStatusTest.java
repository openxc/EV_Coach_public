package com.openxc.measurements;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParkingBrakeStatusTest {
    ParkingBrakeStatus measurement;

    @Before
    public void setUp() {
        measurement = new ParkingBrakeStatus(new Boolean(false));
    }

    @Test
    public void testGet() {
        assertThat(measurement.getValue().booleanValue(), equalTo(false));
    }

    @Test
    public void testHasNoRange() {
        assertFalse(measurement.hasRange());
    }
}
