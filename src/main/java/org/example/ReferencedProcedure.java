package org.example;

public enum ReferencedProcedure {
    BEST {
        long getReference(Measurer.MeasurementInputs inputs) {
            return Measurer.getMinAverage(inputs);
        }
    }, WORST {
        long getReference(Measurer.MeasurementInputs inputs) {
            return Measurer.getMaxAverage(inputs);
        }
    };
    abstract long getReference(Measurer.MeasurementInputs inputs);
}

