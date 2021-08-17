package bibcards.data;

import bibcards.util.Logger;

public class Card {

    public static boolean isCardHeaderLine(String line) {
        return (line.startsWith(cardWriter) ||
                line.startsWith(cardTranslator) ||
                line.startsWith(cardAboutWriter) ||
                line.startsWith(cardAboutTranslator) ||
                line.startsWith(cardAboutEditor));
    }

    // CardType is enum of publication type of the subject
    public enum CardType {
        WRITER,             // card detailing subject's publication
        TRANSLATOR,         // card detailing publication of translation by subject
        ABOUT_WRITER,       // card detailing publication about subject as writer
        ABOUT_TRANSLATOR,   // card detailing publication about subject as translator
        ABOUT_EDITOR,       // card detailing publication about subject as editor
    }

    private static String cardWriter = "כרטיס-כותב-גנזים";
    private static String cardTranslator = "כרטיס-מתרגם-גנזים";
    private static String cardAboutWriter = "כרטיס-עליו-גנזים";
    private static String cardAboutTranslator = "כרטיס-אודות-מתרגם-גנזים";
    private static String cardAboutEditor = "כרטיס-עורך-אודות-גנזים";

    private final CardType cardType;
    private int cardNumber;
    private String tmpDescriptor = new String("card: ");

    public static Card genCard(String header) {
        if (header.startsWith(cardWriter))
            return new Card(CardType.WRITER, header);
        else if (header.startsWith(cardTranslator))
            return new Card(CardType.TRANSLATOR, header);
        else if (header.startsWith(cardAboutWriter))
            return new Card(CardType.ABOUT_WRITER, header);
        else if (header.startsWith(cardAboutTranslator))
            return new Card(CardType.ABOUT_TRANSLATOR, header);
        else if (header.startsWith(cardAboutEditor))
            return new Card(CardType.ABOUT_EDITOR, header);
        else
            throw new IllegalArgumentException("unknown card header <" + header + ">");
    }

    public Card(CardType cardType, String cardHeader) {
        this.cardType = cardType;

        // get card number from cardHeader
        String[] parts = cardHeader.split(" ");
        if (parts.length < 2)
            Logger.error("card header <" + cardHeader + "> - no card number part");
        else {
            int num = 0;
            int i = 0;
            // Skip past spaces - the only legal character before the number
            while (i < parts[1].length() && !Character.isSpaceChar((parts[1].charAt(i)))) {
                ++i;
            }
            if (i < parts[1].length()) {
                // Accumulate the digits into the result.
                while (i < parts[1].length() && Character.isDigit(parts[1].charAt(i))) {
                    num = 10 * num + Character.getNumericValue(parts[1].charAt(i));
                    ++i;
                }
            }
            // No digits found.
            if (num == 0)
                Logger.error("no number found in card header <" + cardHeader + ">");
            else
                this.cardNumber = num;
        }
    }

    public void addLine(Line line) {
        // get the prefix of line
        String[] parts = line.getContent().split(":");
        this.tmpDescriptor.concat(parts[0] + "_");
    }


}
