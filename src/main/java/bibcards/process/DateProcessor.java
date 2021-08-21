package bibcards.process;

import bibcards.data.Card;
import bibcards.data.Line;
import bibcards.util.Logger;
import bibcards.util.Setup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DateProcessor {

    private static DateProcessor dateProcessor = null;
    private List<DateTimeFormatter> formatters = new ArrayList<>();
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

        formatters.add(DateTimeFormatter.ofPattern("d.MM.uuuu"));
        formatters.add(DateTimeFormatter.ofPattern("d.MM.uu"));
        formatters.add(DateTimeFormatter.ofPattern("d.M.uuuu"));
        formatters.add(DateTimeFormatter.ofPattern("d.M.uu"));

        formatters.add(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
        formatters.add(DateTimeFormatter.ofPattern("dd.MM.uu"));
        formatters.add(DateTimeFormatter.ofPattern("dd.M.uuuu"));
        formatters.add(DateTimeFormatter.ofPattern("dd.M.uu"));
    }

    public void dumpSummary() {
        Logger.log(1, "parsed OK:   " + okParses);
        Logger.log(1, "parsed fail: " + failedParses);
        Logger.log(1, "parse empty: " + noParses);
    }

    public boolean processCard(Card card) {
        LocalDate localDate = null;
        Line lineOfGregDate = card.getLine(Line.LineType.GREG_DATE);
        if (lineOfGregDate == null)
            return false;

        String gregDateString = lineOfGregDate.getContent();
        String paddedDateString = null;
        if (gregDateString != null) {
            gregDateString = gregDateString.trim();
            String[] parts = gregDateString.split("\\.");
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
                paddedDateString = gregDateString;

            /*
            if (parts.length == 3) {
                if (parts[2].length() == 2) {
                    paddedDateString = parts[0].concat(".").concat(parts[1]).concat(".19").concat(parts[2]);
                }
            } else
                paddedDateString = gregDateString;
            */
            int dtfPos = -1;
            for (DateTimeFormatter dtf : formatters) {
                dtfPos++;
                try {
                    localDate = LocalDate.parse(paddedDateString, dtf);
                    Logger.log(4, "parsed OK date <" + paddedDateString + "> at data line=" + lineOfGregDate.getDataLineNum() + ", using dtf #" + dtfPos);
                    if (localDate.getYear() > maxGregYear) {
                        localDate = localDate.minusYears(100);
                        Logger.log(1, "date reduced to <" + paddedDateString + ">");
                    }
                    card.setLocalDate(localDate);
                    okParses++;
                    return true;
                } catch (Exception e) {
                    Logger.log(5,"failed to parse <" + paddedDateString + "> using <" + dtf.toString() + "> ==>> " + e.getMessage());
                }
            }
            failedParses++;
            Logger.log(2, "failed to parse OK date <" + paddedDateString + "> at data line=" + lineOfGregDate.getDataLineNum());
            return false;
        }
        noParses++;
        return true;
    }
}
