package bibcards.process;

import bibcards.data.Card;
import bibcards.data.Line;

import java.time.LocalDate;

public class DateProcessor {

    private static DateProcessor dateProcessor = null;

    public static DateProcessor getDateProcessor() {
        if (dateProcessor == null)
            dateProcessor = new DateProcessor();
        return dateProcessor;
    }

    private DateProcessor() {

    }

    /*
    public boolean processCard(Card card) {
        LocalDate localDate = null;
        Line gredDate = card.getLine(Line.LineType.GREG_DATE);
        if (gredDate != null)
    }
    */
}
