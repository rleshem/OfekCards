package bibcards.input;

import bibcards.input.line.*;
import bibcards.util.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Card {

    @Override
    public String toString() {
        return "Card{" +
                "titleLine=" + titleLine +
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
                ", manualGregDateLine=" + manualGregDateLine +
                ", manualHebDateLine=" + manualHebDateLine +
                ", cardLine=" + cardLine +
                ", cardType=" + cardType +
                ", cardNumber=" + cardNumber +
                ", localDate=" + canonizedDate +
                ", tmpDescriptor='" + tmpDescriptor + '\'' +
                '}';
    }

    // CardType is enum of publication type of the subject
    public enum CardType {
        WRITER,             // card detailing subject's publication
        TRANSLATOR,         // card detailing publication of translation by subject
        ABOUT_WRITER,       // card detailing publication about subject as writer
        ABOUT_TRANSLATOR,   // card detailing publication about subject as translator
        ABOUT_EDITOR,       // card detailing publication about subject as editor
    }

    private TitleLine titleLine = null;
    private SubTitleLine subTitleLine = null;
    private RemarkLine remarkLine = null;
    private SourceLine sourceLine = null;
    private HebDateLine hebDateLine = null;
    private GregDateLine gregDateLine = null;
    private PageLine pageLine = null;
    private PseudonymLine pseudonymLine = null;
    private ImportanceLine importanceLine = null;
    private ReporterLine reporterLine = null;
    private SectionLine sectionLine = null;
    private PersonLine personLine = null;
    private ManualGregDateLine manualGregDateLine = null;
    private ManualHebDateLine manualHebDateLine = null;
    private CardLine cardLine = null;

    private static String cardWriter = "כרטיס-כותב-גנזים";
    private static String cardTranslator = "כרטיס-מתרגם-גנזים";
    private static String cardAboutWriter = "כרטיס-עליו-גנזים";
    private static String cardAboutTranslator = "כרטיס-אודות-מתרגם-גנזים";
    private static String cardAboutEditor = "כרטיס-אודות-עורך-גנזים";

    private final CardType cardType;
    private int cardNumber;
    private LocalDate canonizedDate;
    private String tmpDescriptor = new String("card: ");

    public static Card genCardByType(String typeOfCard) {
        if (typeOfCard.equals(CardType.WRITER))
            return new Card(CardType.WRITER);
        else if (typeOfCard.equals(CardType.TRANSLATOR))
            return new Card(CardType.TRANSLATOR);
        else if (typeOfCard.equals(CardType.ABOUT_WRITER))
            return new Card(CardType.ABOUT_WRITER);
        else if (typeOfCard.equals(CardType.ABOUT_TRANSLATOR))
            return new Card(CardType.ABOUT_TRANSLATOR);
        else if (typeOfCard.equals(CardType.ABOUT_EDITOR))
            return new Card(CardType.ABOUT_EDITOR);
        else
            throw new IllegalArgumentException("unknown card type <" + typeOfCard + ">");
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

    public static boolean isCardHeaderLine(String line) {
        return (line.startsWith(cardWriter) ||
                line.startsWith(cardTranslator) ||
                line.startsWith(cardAboutWriter) ||
                line.startsWith(cardAboutTranslator) ||
                line.startsWith(cardAboutEditor));
    }

    private Card(CardType cardType) {
        this.cardType = cardType;
        this.cardLine = null;
    }

    private Card(CardType cardType, String cardHeader) {
        this.cardType = cardType;
        this.cardLine = new CardLine();

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
            case TITLE:
                return (titleLine != null) ? titleLine.getContent() : null;
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
            case MANUAL_GREG_DATE:
                return (manualGregDateLine != null) ? manualGregDateLine.getContent() : null;
            case MANUAL_HEB_DATE:
                return (manualHebDateLine != null) ? manualHebDateLine.getContent() : null;
            default:
                Logger.error("cannot handle line type=" + lineType);
                return null;
        }
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardHeaderLineNumber(int linNum) {
        cardLine.setDataLineNum(linNum);
    }

    public int getCardHeaderLineNumber() {
        return cardLine.getDataLineNum();
    }

    private static DateTimeFormatter readableFormatter = DateTimeFormatter.ofPattern("dd-MM-yy");

    public String getCanonizedDate() {
        if (canonizedDate == null) {
            Logger.error("card " + cardNumber + ": no canonizedDate");
            return cardNumber + ": ~no canonized date~";
        }

        int month = canonizedDate.getMonthValue();
        String monthValue = (month < 10) ? "0" + String.valueOf(month) : String.valueOf(month);
        int day = canonizedDate.getDayOfMonth();
        String dayValue = (day < 10) ? "0" + String.valueOf(day) : String.valueOf(day);

        String byYearMonthDayDateString =
                canonizedDate.getYear() + "-" +
                        monthValue + "-" +
                        dayValue;
//        String formattedDate = canonizedDate.format(readableFormatter);
        return byYearMonthDayDateString;
    }

    public void setCanonizedDate(LocalDate canonizedDate) {
        this.canonizedDate = canonizedDate;
    }

    public Line getLine(Line.LineType lineType) {
        switch (lineType) {
            case TITLE:
                return titleLine;
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
            case MANUAL_GREG_DATE:
                return manualGregDateLine;
            case MANUAL_HEB_DATE:
                return manualHebDateLine;
            default:
                Logger.error("cannot handle line type=" + lineType);
                return null;
        }
    }

    public void addLine(Line line) {
        this.addLine(line, -1);
    }

    public void addLine(Line line, int numLines) {
        switch (line.getType()) {
            case TITLE:
                titleLine = (titleLine == null) ? (TitleLine) line : (TitleLine) titleLine.joinContent(line, numLines, cardNumber);
                break;
            case REMARK:
                this.remarkLine = (remarkLine == null) ? (RemarkLine) line : (RemarkLine) remarkLine.joinContent(line, numLines, cardNumber);
                break;
            case SOURCE:
                this.sourceLine = (sourceLine == null) ? (SourceLine) line : (SourceLine) sourceLine.joinContent(line, numLines, cardNumber);
                ;
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
            case MANUAL_GREG_DATE:
                this.manualGregDateLine = (manualGregDateLine == null) ? (ManualGregDateLine) line : (ManualGregDateLine) manualGregDateLine.joinContent(line, numLines, cardNumber);
                break;
            case MANUAL_HEB_DATE:
                this.manualHebDateLine = (manualHebDateLine == null) ? (ManualHebDateLine) line : (ManualHebDateLine) manualHebDateLine.joinContent(line, numLines, cardNumber);
                break;
            case CARD:
                Logger.error("card " + this.cardNumber + " at line " + line.getDataLineNum() + " of type <" + this.cardType + "> - cannot accept another card line=<" + line.toString() + ">");
                break;
            default:
                Logger.error("card " + this.cardNumber + " of type <" + this.cardType + "> - unknown type of line=<" + line.toString() + ">");
                break;
        }

        // get the prefix of line
        tmpDescriptor = tmpDescriptor.concat(line.getType().name() + "_");

    }

}
