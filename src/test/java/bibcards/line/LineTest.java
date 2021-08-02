package bibcards.line;

import bibcards.util.Logger;

class LineTest {

    @org.junit.jupiter.api.Test
    void genLine() {
        for (LineType value : LineType.values()) {
            Line line = null;
            try {
                line = Line.genLine(value);
            } catch (IllegalArgumentException e) {
                Logger.log(1, "failed on <" + value + "> as expected");
            } catch (Exception e) {
                System.err.println("exception on Name line: " + e.getMessage());
                System.exit(-1);
            }
            if (line != null) {
                assert line.getType() == value;
                Logger.log(1, "passed create <" + value + "> line");
            } else
                Logger.log(1, "failed on create line of <" + value + ">");
        }
    }

    @org.junit.jupiter.api.Test
    void getType() {
    }
}