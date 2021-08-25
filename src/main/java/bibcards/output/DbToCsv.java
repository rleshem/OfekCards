package bibcards.output;

import bibcards.input.Card;
import bibcards.util.Logger;

import java.util.List;

// TODO: implement
public class DbToCsv {

    private static final String CSV_DELIMITER = " | ";

    public static void main(String[] args) {
        List<Card> cards = SqliteHandler.getHandler().loadAllCards("select * from cards");
        Logger.log(1, "loaded " + cards.size() + " cards");
    }

}
