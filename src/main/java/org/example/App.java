package org.example;

public class App {
    public static void main(String[] args) {
        Measurer measurer = Measurer.builder()
                .timesRun(15)
                .warmUpDefender(WarmUpDefenderFactory.WARM_UP_RUNS_DISCARDER.withNumOfWarmUpRuns(40))
                .compareAgainst(ReferencedProcedure.WORST)
                .build();

        measurer.compare(
                MeasuredProcedureFactory.REMOVING_FARTHEST_ELEMENT_FROM_ARRLIST_OF_TEN_STRINGS_WITH_ITERATOR,
                MeasuredProcedureFactory.REMOVING_FARTHEST_ELEMENT_FROM_ARRLIST_OF_TEN_STRINGS_WITH_BACKWARD_FOR_LOOP
        );
    }
}
