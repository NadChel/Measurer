package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

@SuppressWarnings("unused")
public final class MeasuredProcedureFactory {
    public static final MeasuredProcedure MILLION_INTS_TO_ARRLIST_NO_PREALLOCATION =
            new MeasuredProcedure("MILLION_INTS_TO_ARRLIST_NO_PREALLOCATION") {
                @Override
                public void run() {
                    int numOfElements = 1_000_000;
                    List<Integer> intList = new ArrayList<>();
                    addRandomInts(intList, numOfElements);
                }
            };
    public static final MeasuredProcedure MILLION_INTS_TO_ARRLIST_WITH_PREALLOCATION =
            new MeasuredProcedure("MILLION_INTS_TO_ARRLIST_WITH_PREALLOCATION") {
                @Override
                public void run() {
                    int numOfElements = 1_000_000;
                    List<Integer> intList = new ArrayList<>(numOfElements);
                    addRandomInts(intList, numOfElements);
                }
            };
    public static final MeasuredProcedure PLUS_CONCATENATION_OF_THOUSAND_LETTERS =
            new MeasuredProcedure("PLUS_CONCATENATION_OF_THOUSAND_LETTER") {
                @Override
                void run() {
                    String res = "";
                    String[] alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
                    for(int i = 0; i < 1_000; i++) {
                        int randomIndex = Util.randomInt(alphabet.length);
                        res += alphabet[randomIndex];
                    }
                }
            };
    public static final MeasuredProcedure STRING_BUILDING_OF_THOUSAND_LETTERS =
            new MeasuredProcedure("STRING_BUILDING_OF_THOUSAND_LETTERS") {
                @Override
                void run() {
                    StringBuilder sb = new StringBuilder();
                    String[] alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
                    for(int i = 0; i < 1_000; i++) {
                        int randomIndex = Util.randomInt(alphabet.length);
                        sb.append(alphabet[randomIndex]);
                    }
                    sb.toString();
                }
            };
    public static final MeasuredProcedure STRING_BUFFERING_OF_THOUSAND_LETTERS =
            new MeasuredProcedure("STRING_BUFFERING_OF_THOUSAND_LETTERS") {
                @Override
                void run() {
                    StringBuffer sb = new StringBuffer();
                    String[] alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
                    for(int i = 0; i < 1_000; i++) {
                        int randomIndex = Util.randomInt(alphabet.length);
                        sb.append(alphabet[randomIndex]);
                    }
                    sb.toString();
                }
            };
    public static final MeasuredProcedure DOES_MILLION_ELEMENT_ARRLIST_CONTAIN_FARTHEST_ELEMENT =
            new MeasuredProcedure("DOES_MILLION_ELEMENT_ARRLIST_CONTAIN_FARTHEST_ELEMENT") {
                private final List<Integer> list = getListOfMillionInts();
                @Override
                void run() {
                    list.contains(1_000_000);
                }
            };
    public static final MeasuredProcedure DOES_MILLION_ELEMENT_LINKED_HASHSET_CONTAIN_FARTHEST_ELEMENT =
            new MeasuredProcedure("DOES_MILLION_ELEMENT_LINKED_HASHSET_CONTAIN_FARTHEST_ELEMENT") {
                private final LinkedHashSet<Integer> set = getLinkedHashSetOfMillionInts();
                @Override
                void run() {
                    set.contains(1_000_000);
                }
            };
    public static final MeasuredProcedure CONCATENATING_TEN_STRINGS_WITH_STRING_FORMAT = new MeasuredProcedure("CONCATENATING_TEN_STRINGS_WITH_STRING_FORMAT") {
        @Override
        void run() {
            var res = String.format("%s%s%s%s%s%s%s%s%s%s", "str1", "str2", "str3", "str4", "str5",
                    "str6", "str7", "str8", "str9", "str10");
        }
    };
    public static final MeasuredProcedure CONCATENATING_TEN_STRINGS_WITH_PLUS_CONCATENATION = new MeasuredProcedure("CONCATENATING_TEN_STRINGS_WITH_PLUS_CONCATENATION") {
        @Override
        void run() {
            var res = "str1" + "str2" + "str3" + "str4" + "str5" +
                    "str6" + "str7" + "str8" + "str9" + "str10";
        }
    };
    public static final MeasuredProcedure CONCATENATING_TEN_STRINGS_WITH_STRING_BUILDER = new MeasuredProcedure("CONCATENATING_TEN_STRINGS_WITH_STRING_BUILDER") {
        @Override
        void run() {
            var res = new StringBuilder()
                    .append("str1").append("str2").append("str3")
                    .append("str4").append("str5").append("str6")
                    .append("str7").append("str8").append("str9")
                    .append("str10").toString();
        }
    };
    public static final MeasuredProcedure REMOVING_FARTHEST_ELEMENT_FROM_ARRLIST_OF_TEN_STRINGS_WITH_ITERATOR = new MeasuredProcedure("REMOVING_TENTH_ELEMENT_FROM_ARRLIST_WITH_ITERATOR") {
        private final List<String> stringList = getArrListOfTenStrings();
        @Override
        void run() {
            for (Iterator<String> iterator = stringList.iterator(); iterator.hasNext(); ) {
                String str = iterator.next();
                if (str.equals("str10")) {
                    iterator.remove();
                }
            }
        }
    };
    public static final MeasuredProcedure REMOVING_FARTHEST_ELEMENT_FROM_ARRLIST_OF_TEN_STRINGS_WITH_BACKWARD_FOR_LOOP = new MeasuredProcedure("REMOVING_TENTH_ELEMENT_FROM_ARRLIST_WITH_BACKWARD_FOR_LOOP") {
        private final List<String> stringList = getArrListOfTenStrings();
        @Override
        void run() {
            for (int i = stringList.size() - 1; i >= 0; i--) {
                String str = stringList.get(i);
                if (str.equals("str1")) {
                    stringList.remove(i);
                }
            }
        }
    };

    private MeasuredProcedureFactory() {
    }

    private static void addRandomInts(List<Integer> intList, int numOfElements) {
        for (int i = 0; i < numOfElements; i++) {
            int randomInt = Util.randomInt(numOfElements);
            intList.add(randomInt);
        }
    }

    private static List<Integer> getListOfMillionInts() {
        List<Integer> intList = new ArrayList<>(1_000_000);
        for (int i = 1; i <= 1_000_000; i++) {
            intList.add(i);
        }
        return intList;
    }

    private static LinkedHashSet<Integer> getLinkedHashSetOfMillionInts() {
        LinkedHashSet<Integer> intSet = new LinkedHashSet<>(1_000_000);
        for (int i = 1; i <= 1_000_000; i++) {
            intSet.add(i);
        }
        return intSet;
    }

    private static ArrayList<String> getArrListOfTenStrings() {
        return new ArrayList<>(List.of("str1", "str2", "str3", "str4", "str5", "str6", "str7", "str8", "str9", "str10"));
    }
}
