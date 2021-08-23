package bibcards.data;

import bibcards.process.DateProcessor;
import bibcards.util.Logger;
import bibcards.util.Setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assimilator {

    private Map<Line.LineType, Integer> maxLineLenMap = new HashMap<Line.LineType, Integer>();
    private static char prefixEnd = ':';
    private static String commentChar = "#";
    private static Assimilator assimilator = null;
    private File dataFile;
    private BufferedReader reader;
    private String nextContentLine = null;
    private int numLines = 0;
    private boolean fileExhausted = false;

    public static Assimilator getAssimilator() {
        if (assimilator == null) {
            try {
                assimilator = new Assimilator();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return assimilator;
    }

    private Assimilator() throws IOException {
        String fileName = Setup.getSetup().getStringProperty(Setup.dataFile);
        dataFile = new File(fileName);
        if (!dataFile.exists())
            throw new IOException("file not found: " + fileName);
        reader = new BufferedReader(new FileReader(dataFile));
    }

    public int getNumLines() {
        return numLines;
    }

    private String readContentLine() throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                Logger.log(1, "file exhausted after " + numLines + " lines");
                fileExhausted = true;
                return null;
            }
            numLines++;
            // skip empty lines or lines starting with comment character
            if (line.length() < 1)  // skip empty lines
                continue;
            if (line.endsWith(commentChar) || line.startsWith(commentChar)) // skip comment lines
                continue;
            return line;
        }
    }

    // reads next line - MUST be card header
    public List<Card> readCards() throws IOException {
        List<Card> cards = new ArrayList<>();
        while (true) {
            if (nextContentLine == null)
                nextContentLine = readContentLine();
            Card card = Card.genCard(nextContentLine);
            if (card == null) {
                if (cards.size() > 0) {
                    Logger.log(1, "read " + cards.size() + " cards - reached EOF");
                    return cards;
                } else {
                    Logger.error("No cards found in file <" + dataFile.getAbsolutePath() + "> !!!");
                    return null;
                }
            }
            Logger.log(5, "read card at line " + numLines + ": " + nextContentLine);
            fillCard(card);
            cards.add(card);
            Logger.log(5, "filled card no. " + cards.size() + " - reached line " + numLines + ", next line: " + nextContentLine);
            if (fileExhausted) {
                Logger.log(1, "File exhausted, read " + cards.size() + " cards, stored in " + numLines + " data lines");
                return cards;
            }
        }
    }

    private void fillCard(Card card) {
        card.setCardHeaderLineNumber(numLines);
        while (true) {
            try {
                nextContentLine = readContentLine();
                if (fileExhausted)
                    return;
                if (Card.isCardHeaderLine(nextContentLine)) {
                    Logger.log(5, "filled card <" + card.toString() + ">");
                    LocalDate localDate = DateProcessor.getDateProcessor().processCardDate(card);
                    if (localDate == null) {
                        Logger.error("no date found in card " + card.getCardNumber() + " at line " + card.getCardHeaderLineNumber());
                    } else {
                        card.setLocalDate(localDate);
                        Logger.log(1, "date=" + DateProcessor.getDateProcessor().presentableDate(localDate) + " in card " + card.getCardNumber());
                    }
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String prefixOfLine = nextContentLine.substring(0, 1);
            Line line = Line.createLineOf(prefixOfLine, numLines);
            if (line == null) {
                Logger.error("line type not identified, line=<" + nextContentLine + ">");
            } else {
                line.setContent(nextContentLine);
                card.addLine(line, numLines);

                // check if current line is longer than previous lines of the same type
                Integer currMax =maxLineLenMap.get(line.getType());
                int currMaxLen = (currMax == null) ? 0 : maxLineLenMap.get(line.getType()).intValue();
                if (currMaxLen < line.getContent().length())
                    maxLineLenMap.put(line.getType(), line.getContent().length());
            }
        }
    }

    private String getFileName() {
        return dataFile.getAbsolutePath();
    }

    private void dumpMaxLineLength() {
        for (Map.Entry mapElement : maxLineLenMap.entrySet()) {
            Line.LineType entry = (Line.LineType) mapElement.getKey();
            int value = maxLineLenMap.get(entry);
            Logger.log(1, entry.name() + ": " + value);
        }
    }

    public static void main(String[] args) throws IOException {
        Assimilator assimilator = Assimilator.getAssimilator();
        List<Card> cards = assimilator.readCards();
        DateProcessor.getDateProcessor().dumpSummary();
        int i = 0;
        if (Setup.getSetup().getBooleanProperty(Setup.dumpCards)) {
            for (Card card : cards) {
                Logger.log(4, "card=" + card.getCardNumber() + ": name=" + card.getLineContent(Line.LineType.NAME) + ", source=" + card.getLineContent(Line.LineType.SOURCE) + ", importance=" + card.getLineContent(Line.LineType.IMPORTANCE));
                if ((i++ % 50) == 0)
                    Logger.log(4, "### " + card.toString());
            }
        }
        assimilator.dumpMaxLineLength();
        Logger.log(1, "read " + cards.size() + " from file <" + assimilator.getFileName() + ">");
    }

}
