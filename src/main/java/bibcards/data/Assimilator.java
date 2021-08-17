package bibcards.data;

import bibcards.util.Logger;
import bibcards.util.Setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Assimilator {

    private static char prefixEnd = ':';
    private static String commentChar = "#";
    private static Assimilator assimilator = null;
    private File dataFile;
    private BufferedReader reader;
    private String nextContentLine = null;
    private int numLines = 0;

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

    private String readContentLine() throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                Logger.log(1, "file exhausted after " + numLines + " lines");
                return null;
            }
            numLines++;
            // skip empty lines or lines starting with comment character
            if (line.length() < 1)  // skip empty lines
                continue;
            if (line.startsWith(commentChar)) // skip comment lines
                continue;
            return line;
        }
    }

    // reads next line - MUST be card header
    public List<Card> readCards() throws IOException {
        List<Card> cards = new ArrayList<>();
        while (true) {
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
            Logger.log(2, "read card at line " + numLines + ": " + nextContentLine);
            fillCard(card);
            cards.add(card);
            Logger.log(2, "filled card no. " + cards.size() + "' reached line " + numLines + ", next line: " + nextContentLine);
        }
    }

    private void fillCard(Card card) {
        while (true) {
            try {
                nextContentLine = readContentLine();
                if (Card.isCardHeaderLine(nextContentLine)) {
                    Logger.log(4, "filled card <" + card.toString() + ">");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String prefixOfLine = nextContentLine.substring(0, 1);
            Line line = Line.createLineOf(prefixOfLine);
            if (line == null) {
                Logger.error("line type not identified, line=<" + nextContentLine + ">");
            } else {
                card.addLine(line);
            }
        }
    }

    private String getFileName() {
        return dataFile.getAbsolutePath();
    }

    @Deprecated
    public void readData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));
        ;
        int numLines = 0;
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                Logger.log(1, "file exhausted after " + numLines + " lines");
                return;
            }
            if (line.length() < 1)  // skip empty lines
                continue;

            if (line.startsWith(commentChar)) // skip comment lines
                continue;

            int separatorPos = line.indexOf(prefixEnd);
            if (separatorPos < 0) { // no prefix in line - problem
                System.err.println("line <" + numLines + "> - no prefix separator found: " + line);
                continue;
            }

            String prefix = line.substring(0, separatorPos);
            boolean okPrefix = PreFixer.isKey(prefix);
            System.out.println("line <" + numLines + ">: prefix=[" + prefix + "]  recognized=" + okPrefix);

            numLines++;
        }
    }

    public static void main(String[] args) throws IOException {
        Assimilator assimilator = Assimilator.getAssimilator();
        List<Card> cards = assimilator.readCards();
        Logger.log(1, "read " + cards.size() + " from file <" + assimilator.getFileName() + ">");
    }

}
