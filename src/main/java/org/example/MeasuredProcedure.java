package org.example;

import java.util.ArrayList;
import java.util.Collection;

abstract class MeasuredProcedure {
    protected final String name;
    private Collection<Long> measurements;
    protected MeasuredProcedure(String name) {
        if (name.length() < 1) {
            throw new IllegalArgumentException("Procedure name should be at least one character long");
        }
        this.name = name;
    }
    abstract void run();

    public void setMeasurementsCollection(Collection<Long> measurements) {
        this.measurements = measurements;
    }

    public void addMeasurement(long singleMeasurement) {
        if (measurements == null) {
            measurements = new ArrayList<>();
        }
        measurements.add(singleMeasurement);
    }
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public long getAverageMeasurement() {
        ensureValidity();
        return (long) measurements.stream()
                .mapToLong(m -> m)
                .average()
                .getAsDouble();
    }
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public long getMinMeasurement() {
        ensureValidity();
        return measurements.stream()
                .mapToLong(m -> m)
                .min()
                .getAsLong();
    }
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public long getMaxMeasurement() {
        ensureValidity();
        return measurements.stream()
                .mapToLong(m -> m)
                .max()
                .getAsLong();
    }

    private void ensureValidity() {
        if (measurements == null) {
            throw new IllegalStateException("The measurements Collection is null");
        } else if (measurements.isEmpty()) {
            throw new IllegalStateException("The measurements Collection is empty");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
