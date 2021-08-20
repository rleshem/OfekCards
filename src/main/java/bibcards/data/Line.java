package bibcards.data;

import bibcards.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/*
כרטיס-גנזים 109:
ש: בית חולים צבאי
ה: שיר
מ: דבר
ל: 10.5.57 (אולי 51)
ד: עמ 3
 */
public abstract class Line {

    private final LineType type;
    protected String prefix;
    protected String content;

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

    public enum LineType {
        NAME,       // yes: ש (שם)
        REMARK,     // yes: ה (הערה)
        SOURCE,     // yes: מ (מקור)
        HEB_DATE,   // yes: ע (עברי)
        GREG_DATE,  // yes: ל (לועזי)
        PAGE,       // yes: ד (דף)
        PSEUDONYM,  // yes: ט (שם עט)
        SUB_TITLE,  // yes: ת (תת כותרת)
        IMPORTANCE, // yes: ח (חשיבות)
        REPORTER,   // yes: ר (רשם, כתב)
        SECTION,    // yes: (מדור, מוסף) ס
        PERSON,     // yes: (אישיות) א

        CARD        // yes: כ (כרטיס-גנזים)
    }

    /*
    private static Map<String, LineType> typesByKey = new HashMap<String, LineType>() {{
        put("ש", LineType.Name);
        put("ה", LineType.Remark);
    }};
     */
    private static Map<String, Supplier<Line>> typeSupplierMap = new HashMap<String, Supplier<Line>>() {{
        put("ש", NameLine::new);
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
    }};

    public static Line createLineOf(String kind, int numLines) {
        Supplier<Line> supplier = typeSupplierMap.get(kind);
        if (supplier == null) {
            System.err.println("Unknown line type <" + kind + "> - input line=" + numLines);
            System.exit(-1);
        }
        return supplier.get();
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

    // ש: א. שמאלי באור חדש ; ויהי המבול על הארץ החלוצים
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
/*        if (getType() == LineType.CARD)
            Logger.error("CARD LINE ???");
            // expected input: card-card-letters num:
            String[] parts = inputLine.split(" ");
            if (parts.length != 2) {
                Logger.error("card line no. " +
                        Assimilator.getAssimilator().getNumLines() + ": expected 2 parts, has " + parts.length + " parts - cannot process");
                return;
            }
            this.prefix = inputLine.substring(0, inputLine.indexOf(':'));
            // skip spaces after ":" - if exist
            boolean keepLooking = true;
            while (keepLooking) {
                if (inputLine.length() <= dividerPos) {
                    Logger.error("no content in line <" + inputLine + ">");
                    this.content = "~ no content in line ~";
                    return;
                }
                if (inputLine.charAt(dividerPos) == ' ')
                    dividerPos++;
                else
                    keepLooking = false;
            }
*/
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
                '}';
    }

}
