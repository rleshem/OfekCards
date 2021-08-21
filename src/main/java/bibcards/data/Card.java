package bibcards.data;

import bibcards.util.Logger;

import java.time.LocalDate;

public class Card {

    // CardType is enum of publication type of the subject
    public enum CardType {
        WRITER,             // card detailing subject's publication
        TRANSLATOR,         // card detailing publication of translation by subject
        ABOUT_WRITER,       // card detailing publication about subject as writer
        ABOUT_TRANSLATOR,   // card detailing publication about subject as translator
        ABOUT_EDITOR,       // card detailing publication about subject as editor
    }

    private NameLine nameLine = null;
    private RemarkLine remarkLine = null;
    private SourceLine sourceLine = null;
    private HebDateLine hebDateLine = null;
    private GregDateLine gregDateLine = null;
    private PageLine pageLine = null;
    private PseudonymLine pseudonymLine = null;
    private SubTitleLine subTitleLine = null;
    private ImportanceLine importanceLine = null;
    private ReporterLine reporterLine = null;
    private SectionLine sectionLine = null;
    private PersonLine personLine = null;
    private CardLine cardLine = null;

    private static String cardWriter = "כרטיס-כותב-גנזים";
    private static String cardTranslator = "כרטיס-מתרגם-גנזים";
    private static String cardAboutWriter = "כרטיס-עליו-גנזים";
    private static String cardAboutTranslator = "כרטיס-אודות-מתרגם-גנזים";
    private static String cardAboutEditor = "כרטיס-אודות-עורך-גנזים";

    private final CardType cardType;
    private int cardNumber;
    private LocalDate localDate;
    private String tmpDescriptor = new String("card: ");

    public static boolean isCardHeaderLine(String line) {
        return (line.startsWith(cardWriter) ||
                line.startsWith(cardTranslator) ||
                line.startsWith(cardAboutWriter) ||
                line.startsWith(cardAboutTranslator) ||
                line.startsWith(cardAboutEditor));
    }

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
        this.cardLine = new CardLine();
//        this.cardLine.setContent(cardHeader);

        // get card number from cardHeader
        String[] parts = cardHeader.split(" ");
        if (parts.length < 2)
            Logger.error("card header <" + cardHeader + "> - no card number part");
        else {
            int num = 0;
            int i = 0;
            // Skip past spaces - the only legal character before the number
            while (i < parts[1].length() && Character.isSpaceChar((parts[1].charAt(i)))) {
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

    public String getLineContent(Line.LineType lineType) {
        switch (lineType) {
            case NAME:
                return (nameLine != null) ? nameLine.getContent() : null;
            case REMARK:
                return (remarkLine != null) ? remarkLine.getContent() : null;
            case SOURCE:
                return (sourceLine != null) ? sourceLine.getContent() : null;
            case IMPORTANCE:
                return (importanceLine != null) ? importanceLine.getContent() : null;
            case PSEUDONYM:
                return (pseudonymLine != null) ? pseudonymLine.getContent() : null;
            case CARD:
                return (cardLine != null) ? cardLine.getContent() : null;
            case SUB_TITLE:
                return (subTitleLine != null) ? subTitleLine.getContent() : null;
            case GREG_DATE:
                return (gregDateLine != null) ? gregDateLine.getContent() : null;
            case HEB_DATE:
                return (hebDateLine != null) ? hebDateLine.getContent() : null;
            case PAGE:
                return (pageLine != null) ? pageLine.getContent() : null;
            case PERSON:
                return (personLine != null) ? personLine.getContent() : null;
            case REPORTER:
                return (reporterLine != null) ? reporterLine.getContent() : null;
            case SECTION:
                return (sectionLine != null) ? sectionLine.getContent() : null;
            default:
                Logger.error("cannot handle line type=" + lineType);
                return null;
        }
    }

    public int getCardNumber() {
        return cardNumber;
    }

    @Override
    public String toString() {
        return "Card{" +
                "nameLine=" + nameLine +
                ", remarkLine=" + remarkLine +
                ", sourceLine=" + sourceLine +
                ", hebDateLine=" + hebDateLine +
                ", gregDateLine=" + gregDateLine +
                ", pageLine=" + pageLine +
                ", pseudonymLine=" + pseudonymLine +
                ", subTitleLine=" + subTitleLine +
                ", importanceLine=" + importanceLine +
                ", reporterLine=" + reporterLine +
                ", sectionLine=" + sectionLine +
                ", personLine=" + personLine +
                ", cardLine=" + cardLine +
                ", cardType=" + cardType +
                ", cardNumber=" + cardNumber +
                ", localDate=" + localDate +
                ", tmpDescriptor='" + tmpDescriptor + '\'' +
                '}';
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Line getLine(Line.LineType lineType) {
        switch (lineType) {
            case NAME:
                return nameLine;
            case REMARK:
                return remarkLine;
            case SOURCE:
                return sourceLine;
            case IMPORTANCE:
                return importanceLine;
            case PSEUDONYM:
                return pseudonymLine;
            case CARD:
                return cardLine;
            case SUB_TITLE:
                return subTitleLine;
            case GREG_DATE:
                return gregDateLine;
            case HEB_DATE:
                return hebDateLine;
            case PAGE:
                return pageLine;
            case PERSON:
                return personLine;
            case REPORTER:
                return reporterLine;
            case SECTION:
                return sectionLine;
            default:
                Logger.error("cannot handle line type=" + lineType);
                return null;
        }
    }

    public void addLine(Line line, int numLines) {
        switch (line.getType()) {
            case NAME:
                nameLine = (nameLine == null) ? (NameLine) line : (NameLine) nameLine.joinContent(line, numLines, cardNumber);
                break;
            case REMARK:
                this.remarkLine = (remarkLine == null) ? (RemarkLine) line : (RemarkLine) remarkLine.joinContent(line, numLines, cardNumber);
                break;
            case SOURCE:
                this.sourceLine = (sourceLine == null) ? (SourceLine) line : (SourceLine) sourceLine.joinContent(line, numLines, cardNumber);;
                break;
            case HEB_DATE:
                this.hebDateLine = (hebDateLine == null) ? (HebDateLine) line : (HebDateLine) hebDateLine.joinContent(line, numLines, cardNumber);
                break;
            case GREG_DATE:
                this.gregDateLine = (gregDateLine == null) ? (GregDateLine) line : (GregDateLine) gregDateLine.joinContent(line, numLines, cardNumber);
                break;
            case PAGE:
                this.pageLine = (pageLine == null) ? (PageLine) line : (PageLine) pageLine.joinContent(line, numLines, cardNumber);
                break;
            case PSEUDONYM:
                this.pseudonymLine = (pseudonymLine == null) ? (PseudonymLine) line : (PseudonymLine) pseudonymLine.joinContent(line, numLines, cardNumber);
                break;
            case SUB_TITLE:
                this.subTitleLine = (subTitleLine == null) ? (SubTitleLine) line : (SubTitleLine) subTitleLine.joinContent(line, numLines, cardNumber);
                break;
            case IMPORTANCE:
                this.importanceLine = (importanceLine == null) ? (ImportanceLine) line : (ImportanceLine) importanceLine.joinContent(line, numLines, cardNumber);
                break;
            case REPORTER:
                this.reporterLine = (reporterLine == null) ? (ReporterLine) line : (ReporterLine) reporterLine.joinContent(line, numLines, cardNumber);
                break;
            case SECTION:
                this.sectionLine = (sectionLine == null) ? (SectionLine) line : (SectionLine) sectionLine.joinContent(line, numLines, cardNumber);
                break;
            case PERSON:
                this.personLine = (personLine == null) ? (PersonLine) line : (PersonLine) personLine.joinContent(line, numLines, cardNumber);
                break;
            case CARD:
                Logger.error("card " + this.cardNumber + " of type <" + this.cardType + "> - cannot accept another card line=<" + line.toString() + ">");
                break;
            default:
                Logger.error("card " + this.cardNumber + " of type <" + this.cardType + "> - unknown type of line=<" + line.toString() + ">");
                break;
        }

        // get the prefix of line
        tmpDescriptor = tmpDescriptor.concat(line.getType().name() + "_");

    }

}
