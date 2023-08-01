package org.example;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import org.example.Measurer.NanoProcessor.TimeUnit;
import org.example.Measurer.NanoProcessor.TimeUnit.TimeUnitName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
public class Measurer {
    private final int timesRun;
    private final ReferencedProcedure referencedProcedure;
    private final WarmUpDefender warmUpDefender;

    public Measurer() {
        this.timesRun = Defaults.TIMES_RUN;
        this.referencedProcedure = Defaults.REFERENCED_PROCEDURE;
        this.warmUpDefender = Defaults.WARM_UP_DEFENDER;
    }

    private Measurer(int timesRun, ReferencedProcedure referencedProcedure,
                     WarmUpDefender warmUpDefender) {
        this.timesRun = timesRun;
        this.referencedProcedure = referencedProcedure;
        this.warmUpDefender = warmUpDefender;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int timesRun = Defaults.TIMES_RUN;
        private ReferencedProcedure compareAgainst = Defaults.REFERENCED_PROCEDURE;
        private WarmUpDefender warmUpDefender = Defaults.WARM_UP_DEFENDER;

        public Builder timesRun(int timesRun) {
            if (timesRun < 1) {
                throw Defaults.illegalTimesRunException(timesRun);
            }
            this.timesRun = timesRun;
            return this;
        }
        public Builder compareAgainst(ReferencedProcedure compareAgainst) {
            this.compareAgainst = compareAgainst;
            return this;
        }
        public Builder warmUpDefender(WarmUpDefender warmUpDefender) {
            this.warmUpDefender = warmUpDefender;
            return this;
        }
        public Measurer build() {
            return new Measurer(timesRun, compareAgainst,
                    warmUpDefender);
        }
    }

    public long measureAverage(MeasuredProcedure measuredProcedure) {
        measuredProcedure.setMeasurementsCollection(Defaults.measurementsCollection(timesRun));
        warmUpDefender.performMeasurementsWithDefenses(new MeasurementInputs(List.of(measuredProcedure), timesRun));
        log.info(averageMeasurementLog(measuredProcedure));
        return measuredProcedure.getAverageMeasurement();
    }

    static long measureOnce(MeasuredProcedure measuredProcedure) {
        long start = System.nanoTime();
        measuredProcedure.run();
        long finish = System.nanoTime();
        return finish - start;
    }

    public long compare(MeasuredProcedure firstMeasuredProcedure, MeasuredProcedure secondMeasuredProcedure,
                        MeasuredProcedure... otherMeasuredProcedures) {
        List<MeasuredProcedure> allMeasuredProcedures =
                composeComparedProcedures(firstMeasuredProcedure, secondMeasuredProcedure, otherMeasuredProcedures);
        warmUpDefender.performMeasurementsWithDefenses(new MeasurementInputs(allMeasuredProcedures, timesRun));
        log.info(comparisonStatsLog(allMeasuredProcedures));
        return getSpreadOfAverages(allMeasuredProcedures);
    }

    private List<MeasuredProcedure> composeComparedProcedures(MeasuredProcedure firstMeasuredProcedure,
                                                              MeasuredProcedure secondMeasuredProcedure,
                                                              MeasuredProcedure[] otherMeasuredProcedures) {
        List<MeasuredProcedure> allMeasuredProcedures =
                new ArrayList<>(2 + otherMeasuredProcedures.length);
        allMeasuredProcedures.addAll(List.of(
                firstMeasuredProcedure, secondMeasuredProcedure
        ));
        allMeasuredProcedures.addAll(Arrays.asList(otherMeasuredProcedures));
        return allMeasuredProcedures;
    }

    private long getSpreadOfAverages(List<MeasuredProcedure> allMeasuredProcedures) {
        long max = getMaxAverage(new MeasurementInputs(allMeasuredProcedures, timesRun));
        long min = getMinAverage(new MeasurementInputs(allMeasuredProcedures, timesRun));
        return max - min;
    }

    static long getMaxAverage(MeasurementInputs inputs) {
        List<MeasuredProcedure> measuredProcedures = inputs.measuredProcedures();
        return measuredProcedures.stream()
                .mapToLong(MeasuredProcedure::getAverageMeasurement)
                .max().orElseThrow(() -> Defaults.illegalTimesRunException(inputs.timesRun()));
    }

    static long getMinAverage(MeasurementInputs inputs) {
        List<MeasuredProcedure> measuredProcedures = inputs.measuredProcedures();
        return measuredProcedures.stream()
                .mapToLong(MeasuredProcedure::getAverageMeasurement)
                .min().orElseThrow(() -> Defaults.illegalTimesRunException(inputs.timesRun()));
    }

    private String averageMeasurementLog(MeasuredProcedure measuredProcedure) {
        String renderedAverage = renderReadable(measuredProcedure.getAverageMeasurement());
        String onAverageStringAsNecessary = (timesRun == 1) ? "" : "On average,";
        String runSuffixAsNecessary = (timesRun == 1) ? "" : "s";
        return new StringBuilder()
                .append(onAverageStringAsNecessary).append(measuredProcedure)
                .append(" took ").append(renderedAverage)
                .append(" across ").append(timesRun)
                .append(" run").append(runSuffixAsNecessary)
                .toString();
    }

    private String comparisonStatsLog(List<MeasuredProcedure> measuredProcedures) {
        measuredProcedures.sort((p1, p2) -> Math.toIntExact(p1.getAverageMeasurement() - p2.getAverageMeasurement()));
        AsciiTable table = new AsciiTable();
        table.getContext().setGrid(Defaults.TABLE_GRID);
        addTitle(table);
        addHeading(table);
        addStats(table, measuredProcedures);
        return table.render();
    }

    private void addTitle(AsciiTable table) {
        String runSuffixAsNecessary = (timesRun == 1) ? "" : "s";
        table.addStrongRule();
        table.addRow(null, null, null, null, null, "Comparison statistics (" + timesRun + " run" + runSuffixAsNecessary + ")");
        table.addStrongRule();
    }

    private void addHeading(AsciiTable table) {
        table.addRow(" ", "PROCEDURE", "AVERAGE", "PERCENTAGE OF " + referencedProcedure, "MIN", "MAX");
        table.addRule();
    }

    private void addStats(AsciiTable table, List<MeasuredProcedure> measuredProcedures) {
        long referencedAverage = referencedProcedure.getReference(new MeasurementInputs(measuredProcedures, timesRun));
        MeasuredProcedure currentProcedure;
        long bestResult = measuredProcedures.get(0).getAverageMeasurement();
        long currentAverage, averageAsPercentageOfReference;
        String renderedCurrentAverage, renderedMarginToBestPerformerAsNecessary, renderedMin, renderedMax;
        for (int i = 0; i < measuredProcedures.size(); i++) {
            currentProcedure = measuredProcedures.get(i);
            currentAverage = currentProcedure.getAverageMeasurement();
            renderedCurrentAverage = renderReadable(currentAverage);
            averageAsPercentageOfReference = (long) ((double) currentAverage / referencedAverage * 100);
            renderedMarginToBestPerformerAsNecessary = (i == 0) ? "" :
                    " (+ " + renderReadable(currentAverage - bestResult) + ")";
            renderedMin = renderReadable(currentProcedure.getMinMeasurement());
            renderedMax = renderReadable(currentProcedure.getMaxMeasurement());
            table.addRow(i + 1 + ".", currentProcedure,
                    renderedCurrentAverage + renderedMarginToBestPerformerAsNecessary,
                    averageAsPercentageOfReference + "%",
                    renderedMin,
                    renderedMax);
            if (i < measuredProcedures.size() - 1) {
                table.addRule();
            }
        }
        table.addStrongRule();
        table.setTextAlignment(TextAlignment.CENTER);
        table.getRenderer().setCWC(Defaults.COLUMN_WIDTH_CALCULATOR);
    }

    private static String renderReadable(long nanos) {
        TimeUnit[] timeUnits = new NanoProcessor().process(nanos);
        var joiner = new StringJoiner(", ");
        for (TimeUnit unit : timeUnits) {
            if (unit.value() == 0) {
                continue;
            }
            String unitSuffixAsNecessary = (unit.value() % 10 == 1) ? "" : "s";
            joiner.add(unit.value() + " " + unit.name() + unitSuffixAsNecessary);
        }
        return joiner.toString();
    }

    record MeasurementInputs(List<MeasuredProcedure> measuredProcedures, int timesRun) {}

    static class NanoProcessor {
        TimeUnit[] process(long nanos) {
            long minutes = extractLooseMinutes(nanos);
            long seconds = extractLooseSeconds(nanos);
            long milliseconds = extractLooseMilliseconds(nanos);
            long nanoseconds = extractLooseNanoseconds(nanos);
            return new TimeUnit[]{
                    new TimeUnit(TimeUnitName.MINUTE, minutes),
                    new TimeUnit(TimeUnitName.SECOND, seconds),
                    new TimeUnit(TimeUnitName.MILLISECOND, milliseconds),
                    new TimeUnit(TimeUnitName.NANOSECOND, nanoseconds)
            };
        }

        private long extractLooseMinutes(long nanos) {
            return nanos / 60_000_000_000L;
        }

        private long extractLooseSeconds(long nanos) {
            return nanos % 60_000_000_000L / 1_000_000_000L;
        }

        private long extractLooseMilliseconds(long nanos) {
            return nanos % 1_000_000_000L / 1_000_000L;
        }

        private long extractLooseNanoseconds(long nanos) {
            return nanos % 1_000_000L;
        }

        record TimeUnit(TimeUnitName name, long value) {
            enum TimeUnitName {
                MINUTE, SECOND, MILLISECOND, NANOSECOND;

                @Override
                public String toString() {
                    return name().toLowerCase();
                }
            }
        }
    }
}
