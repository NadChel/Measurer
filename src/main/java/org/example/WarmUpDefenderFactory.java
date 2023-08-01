package org.example;

import java.util.List;

public class WarmUpDefenderFactory {
    public static final WarmUpDefender RESTLESS_POINTER = inputs -> {
        List<MeasuredProcedure> allMeasuredProcedures = inputs.measuredProcedures();
        MeasuredProcedure currentMeasuredProcedure;
        int procedurePointer = 0;
        for (int i = 0; i < inputs.timesRun() * allMeasuredProcedures.size(); i++) {
            if (procedurePointer == allMeasuredProcedures.size()) {
                procedurePointer = 0;
            }
            currentMeasuredProcedure = allMeasuredProcedures.get(procedurePointer);
            long singleMeasurement = Measurer.measureOnce(currentMeasuredProcedure);
            currentMeasuredProcedure.addMeasurement(singleMeasurement);
            procedurePointer++;
        }
    };
    public static final WarmUpRunsDiscarder WARM_UP_RUNS_DISCARDER = new WarmUpRunsDiscarder();
    public static final WarmUpDefender NONE = inputs -> {
        List<MeasuredProcedure> allMeasuredProcedures = inputs.measuredProcedures();
        MeasuredProcedure currentMeasuredProcedure;
        for (MeasuredProcedure allMeasuredProcedure : allMeasuredProcedures) {
            currentMeasuredProcedure = allMeasuredProcedure;
            for (int ii = 0; ii < inputs.timesRun(); ii++) {
                long singleMeasurement = Measurer.measureOnce(currentMeasuredProcedure);
                currentMeasuredProcedure.addMeasurement(singleMeasurement);
            }
        }

    };

    static class WarmUpRunsDiscarder implements WarmUpDefender {
        private int numOfWarmUpRuns = Defaults.NUM_OF_WARM_UP_RUNS;

        public WarmUpRunsDiscarder withNumOfWarmUpRuns(int numOfWarmUpRuns) {
            this.numOfWarmUpRuns = numOfWarmUpRuns;
            return this;
        }
        @Override
        public void performMeasurementsWithDefenses(Measurer.MeasurementInputs inputs) {
            List<MeasuredProcedure> allMeasuredProcedures = inputs.measuredProcedures();
            warmUp(allMeasuredProcedures);
            for (MeasuredProcedure measuredProcedure : allMeasuredProcedures) {
                for (int i = 0; i < inputs.timesRun(); i++) {
                    long singleMeasurement = Measurer.measureOnce(measuredProcedure);
                    measuredProcedure.addMeasurement(singleMeasurement);
                }
            }
        }

        private void warmUp(List<MeasuredProcedure> allMeasuredProcedures) {
            for (int i = 0; i < numOfWarmUpRuns; i++) {
                for (MeasuredProcedure measuredProcedure : allMeasuredProcedures) {
                    Measurer.measureOnce(measuredProcedure);
                }
            }
        }

    }
}
