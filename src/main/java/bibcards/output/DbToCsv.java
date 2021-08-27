package bibcards.output;

import bibcards.input.Card;
import bibcards.input.FieldConstants;
import bibcards.input.Line;
import bibcards.util.Logger;
import bibcards.util.Setup;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DbToCsv {

    File csvFile;
    private CSVPrinter csvPrinter;

    private void initCvsFile() throws IOException {
        String fileName = Setup.getSetup().getStringProperty(Setup.csvFileName);
        csvFile = new File(fileName);
        Writer writer = Files.newBufferedWriter(Path.of(fileName));
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                FieldConstants.FIELD_CARD_ID,
                FieldConstants.FIELD_TYPE,
                FieldConstants.FIELD_SOURCE,
                FieldConstants.FIELD_CANONIZED_DATE,
                FieldConstants.FIELD_TITLE,
                FieldConstants.FIELD_SUB_TITLE,
                FieldConstants.FIELD_IMPORTANCE,
                FieldConstants.FIELD_PERSON,
                FieldConstants.FIELD_REPORTER,
                FieldConstants.FIELD_PSEUDONYM,
                FieldConstants.FIELD_VOLUME,
                FieldConstants.FIELD_SECTION,
                FieldConstants.FIELD_PAGE,
                FieldConstants.FIELD_REMARK,
                FieldConstants.FIELD_GREG_DATE,
                FieldConstants.FIELD_MAN_GREG_DATE,
                FieldConstants.FIELD_HEB_DATE,
                FieldConstants.FIELD_MAN_HEB_DATE
        ));
    }

    public void exportCards(List<Card> cards) throws IOException {
        for (Card card : cards) {
            csvPrinter.printRecord(
                    card.getCardNumber(),
                    card.getCardType().name(),
                    card.getLineContent(Line.LineType.SOURCE, false),
                    card.getCanonizedDate(),
                    card.getLineContent(Line.LineType.TITLE, false),
                    card.getLineContent(Line.LineType.SUB_TITLE, false),
                    card.getLineContent(Line.LineType.IMPORTANCE, false),
                    card.getLineContent(Line.LineType.PERSON, false),
                    card.getLineContent(Line.LineType.REPORTER, false),
                    card.getLineContent(Line.LineType.PSEUDONYM, false),
                    card.getLineContent(Line.LineType.VOLUME, false),
                    card.getLineContent(Line.LineType.SECTION, false),
                    card.getLineContent(Line.LineType.PAGE, false),
                    card.getLineContent(Line.LineType.REMARK, false),
                    card.getLineContent(Line.LineType.GREG_DATE, false),
                    card.getLineContent(Line.LineType.MANUAL_GREG_DATE, false),
                    card.getLineContent(Line.LineType.HEB_DATE, false),
                    card.getLineContent(Line.LineType.MANUAL_HEB_DATE, false));
            Logger.log(3, "exported card " + card.getCardNumber() + ", canon=" + card.getCanonizedDate());
            Logger.log(3, "wrote card " + card.getCardNumber() + " to CSV file " + csvFile.getAbsolutePath());
            Logger.log(1, "exported to CSV card " + card.getCardNumber());
        }
        csvPrinter.flush();
        csvPrinter.close();
    }

    public static void main(String[] args) throws IOException {
        List<Card> cards = SqliteHandler.getHandler().loadAllCards("select * from cards");
        Logger.log(1, "loaded " + cards.size() + " cards");
        DbToCsv dbToCsv = new DbToCsv();
        dbToCsv.initCvsFile();
        dbToCsv.exportCards(cards);
    }


}
