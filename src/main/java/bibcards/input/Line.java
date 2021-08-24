package bibcards.input;

import bibcards.input.line.*;
import bibcards.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class Line {

    private final LineType type;
    protected String prefix;
    protected String content;
    int dataLineNum = 0;

    public enum LineType {
        TITLE,              // ש (כותרת)
        REMARK,             // ה (הערה)
        SOURCE,             // מ (מקור)
        HEB_DATE,           // ע (עברי)
        GREG_DATE,          // ל (לועזי)
        PAGE,               // ד (דף)
        PSEUDONYM,          // ט (שם עט)
        SUB_TITLE,          // ת (תת כותרת)
        IMPORTANCE,         // ח (חשיבות)
        REPORTER,           // ר (רשם, כתב)
        SECTION,            // (מדור, מוסף) ס
        PERSON,             // (אישיות) א
        MANUAL_GREG_DATE,   // ג (תאריך לועזי מתוקנן)
        MANUAL_HEB_DATE,    // ב (תאריך עברי מתוקנן)
        CARD        // כ (כרטיס-גנזים)
    }

    private static Map<String, Supplier<Line>> typeSupplierMap = new HashMap<String, Supplier<Line>>() {{
        put("ש", TitleLine::new);
        put("ה", RemarkLine::new);
        put("מ", SourceLine::new);
        put("ע", HebDateLine::new);
        put("ל", GregDateLine::new);
        put("ד", PageLine::new);
        put("ט", PseudonymLine::new);
        put("ת", SubTitleLine::new);
        put("ח", ImportanceLine::new);
        put("ר", ReporterLine::new);
        put("ס", SectionLine::new);
        put("א", PersonLine::new);
        put("כ", CardLine::new);
        put("ג", ManualGregDateLine::new);
        put("ב", ManualHebDateLine::new);
    }};

    public static Line createLineOf(String kind, int numLines) {
        Supplier<Line> supplier = typeSupplierMap.get(kind);
        if (supplier == null) {
            System.err.println("Unknown line type <" + kind + "> - input line=" + numLines);
            System.exit(-1);
        }
        Line line = supplier.get();
        line.setDataLineNum(numLines);
        return line;
    }

    public Line joinContent(Line line, int numLines, int cardNumber) {
        if (content == null) {
            Logger.error("card=" + cardNumber + ", received line to join, line no. " + numLines + " of type " + type + " - but has no content yet, cannot join");
            return this;
        }
        if ((type != LineType.REMARK) && (type != LineType.SUB_TITLE))
            Logger.error("card=" + cardNumber + " -joining non-comment lines, type=" + type + ", added line=" + numLines);
        Logger.log(4, type + ": card=" + cardNumber + ", joining <" + line.content + "> to existing <" + content + ">");
        content = content.concat(" | ").concat(line.content);
        return this;
    }

    public Line(LineType type) {
        this.type = type;
    }

    public LineType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String inputLine) {
        if (!inputLine.contains(":")) {
            Logger.error("line no. " +
                    Assimilator.getAssimilator().getNumLines() + " has no ':' character - cannot process");
            return;
        }
        int dividerPos = inputLine.indexOf(":") + 1;
        // skip spaces after ':'
        while (inputLine.charAt(dividerPos) == ' ')
            dividerPos++;
        this.content = inputLine.substring(dividerPos);
    }

    public int getDataLineNum() {
        return dataLineNum;
    }

    public void setDataLineNum(int dataLineNum) {
        this.dataLineNum = dataLineNum;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isCanBeMulti() { return false; }

    @Override
    public String toString() {
        return "Line{" +
                "type=" + type +
                ", prefix='" + prefix + '\'' +
                ", content='" + content + '\'' +
                ", dataLineNum=" + dataLineNum +
                '}';
    }

}
