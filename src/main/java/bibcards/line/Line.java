package bibcards.line;

/*
כרטיס-גנזים 109:
ש: בית חולים צבאי
ה: שיר
מ: דבר
ל: 10.5.57 (אולי 51)
ד: עמ 3

 */
public abstract class Line {

    public static Line genLine(LineType type) {
        switch (type) {
            case Pseudonym:
                return new PseudonymLine();
            default:
                throw new IllegalArgumentException("cannot generate line type <" + type + ">");
        }
    }

    private final LineType type;
    protected String content;

    public Line(LineType type) {
        this.type = type;
    }

    public LineType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
