package org.example;

import de.vandermeer.asciitable.AT_ColumnWidthCalculator;
import de.vandermeer.asciitable.CWC_LongestWordMax;
import de.vandermeer.asciithemes.TA_Grid;
import de.vandermeer.asciithemes.u8.U8_Grids;

import java.util.ArrayList;
import java.util.Collection;

public class Defaults {
        static final int TIMES_RUN = 10;
        static final ReferencedProcedure REFERENCED_PROCEDURE = ReferencedProcedure.WORST;
        static final WarmUpDefender WARM_UP_DEFENDER = WarmUpDefenderFactory.RESTLESS_POINTER;
        static final int NUM_OF_WARM_UP_RUNS = 5;
        static final TA_Grid TABLE_GRID = U8_Grids.borderStrongDoubleLight();
        static final AT_ColumnWidthCalculator COLUMN_WIDTH_CALCULATOR = new CWC_LongestWordMax(100);
        static RuntimeException illegalTimesRunException(int illegalTimesRun) {
            return new IllegalArgumentException
                    ("A procedure cannot be run less than one time. Your value: " + illegalTimesRun);
        }
        static Collection<Long> measurementsCollection(int initialCapacity) {
            return new ArrayList<>(initialCapacity);
        }
}
