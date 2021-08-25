package bibcards.process;

import bibcards.input.Card;
import bibcards.input.Line;
import bibcards.util.Logger;
import bibcards.util.Setup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateProcessor {

    private static DateProcessor dateProcessor = null;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d.MM.uuuu");
    private DateTimeFormatter canonizedDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
    //    private List<DateTimeFormatter> formatters = new ArrayList<>();
    private int maxGregYear = Setup.getSetup().getIntProperty(Setup.maxGregorianYear);
    private int okParses = 0;
    private int failedParses = 0;
    private int noParses = 0;

    public static DateProcessor getDateProcessor() {
        if (dateProcessor == null)
            dateProcessor = new DateProcessor();
        return dateProcessor;
    }

    private DateProcessor() {

//        formatters.add(DateTimeFormatter.ofPattern("d.MM.uuuu"));
//        formatters.add(DateTimeFormatter.ofPattern("d.MM.uu"));
//        formatters.add(DateTimeFormatter.ofPattern("d.M.uuuu"));
//        formatters.add(DateTimeFormatter.ofPattern("d.M.uu"));
//
//        formatters.add(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
//        formatters.add(DateTimeFormatter.ofPattern("dd.MM.uu"));
//        formatters.add(DateTimeFormatter.ofPattern("dd.M.uuuu"));
//        formatters.add(DateTimeFormatter.ofPattern("dd.M.uu"));
    }

    public void dumpSummary() {
        Logger.log(1, "parsed OK:   " + okParses);
        Logger.log(1, "parsed fail: " + failedParses);
        Logger.log(1, "parse empty: " + noParses);
    }

    public LocalDate processCardDate(Card card) {
        LocalDate finalLocalDate = processGregDate(card);
        if (finalLocalDate == null)
            finalLocalDate = processHebDate(card);
        if (finalLocalDate != null) {
            return finalLocalDate;
        } else {
            return null;
        }
    }

    private LocalDate processHebDate(Card card) {
        Line lineOfManualHebDate = card.getLine(Line.LineType.MANUAL_HEB_DATE);
        if (lineOfManualHebDate != null) {
            return getLocalDateFromGregDate(lineOfManualHebDate);
        }
        Line lineOfHebDate = card.getLine(Line.LineType.HEB_DATE);
        if (lineOfHebDate == null) {
            return null;
        }
        return null;
    }

    private LocalDate getLocalDateFromGregDate(Line lineWithDate) {
        return getLocalDateFromString(lineWithDate.getContent().trim(), lineWithDate.getDataLineNum());
    }

    public LocalDate getLocalDateFromCanonized(String canonized) {
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(canonized, canonizedDateTimeFormatter);
            Logger.log(4, "parsed OK canonized date <" + canonized + ">");
            return localDate;
        } catch (Exception e) {
            Logger.error("failed on canonized date parsing of <" + canonized + ">");
            return null;
        }
    }

    public LocalDate getLocalDateFromString(String usableGregDateString, int lineNumber) {
        LocalDate localDate = null;
        String paddedDateString = null;

        String[] parts = usableGregDateString.split("\\.");
        if (parts.length == 3) {
            String day = parts[0];
            String month = parts[1];
            String year = parts[2];

            // assure month is 2 digits long, with 0 used to pad if needed
            if (month.length() == 1)
                month = "0" + month;
            // assure date year starts with <19>
            if (year.length() == 2)
                year = "19" + year;
            paddedDateString = day.concat(".").concat(month).concat(".").concat(year);
        } else
            paddedDateString = usableGregDateString;

        try {
            localDate = LocalDate.parse(paddedDateString, dateTimeFormatter);
            Logger.log(4, "parsed OK date <" + paddedDateString + "> at input line=" + lineNumber);
            if (localDate.getYear() > maxGregYear) {
                localDate = localDate.minusYears(100);
                Logger.log(1, "date reduced to <" + paddedDateString + ">");
            }
            okParses++;
            Logger.log(1, lineNumber + ": got OK date <" + presentableDate(localDate) + ">");
            return localDate;
        } catch (Exception e) {
            failedParses++;
            Logger.error(lineNumber + ": failed on date <" + paddedDateString + ">");
            return null;
        }
    }

    public String presentableDate(LocalDate localDate) {
        return localDate.getDayOfMonth() + "." + localDate.getMonthValue() + "." + localDate.getYear();
    }

    private LocalDate processGregDate(Card card) {
        Line lineOfManualGreDate = card.getLine(Line.LineType.MANUAL_GREG_DATE);
        if (lineOfManualGreDate != null)
            return getLocalDateFromGregDate(lineOfManualGreDate);

        Line lineOfGregDate = card.getLine(Line.LineType.GREG_DATE);
        if (lineOfGregDate != null)
            return getLocalDateFromGregDate(lineOfGregDate);

        return null;
    }
}
