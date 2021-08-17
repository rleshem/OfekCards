package bibcards.data;

import bibcards.util.Logger;
import org.junit.jupiter.api.Test;

class LineTest {

    @org.junit.jupiter.api.Test
    void genLine() {
        for (Line.LineType value : Line.LineType.values()) {
            Line line = null;
            try {
                line = Line.genLine(value);
            } catch (IllegalArgumentException e) {
                Logger.log(1, "failed on <" + value + "> as expected");
            } catch (Exception e) {
                System.err.println("exception on Name data: " + e.getMessage());
                System.exit(-1);
            }
            if (line != null) {
                assert line.getType() == value;
                Logger.log(1, "passed create <" + value + "> data");
            } else
                Logger.log(1, "failed on create data of <" + value + ">");
        }
    }

    @org.junit.jupiter.api.Test
    void getType() {
    }

    @Test
    void createLineOf() {
        Line line = Line.createLineOf("ט");
        assert line.getType() == Line.LineType.PSEUDONYM;
        line = Line.createLineOf("ה");
        assert line.getType() == Line.LineType.REMARK;
    }
}