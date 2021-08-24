package bibcards.input;

import org.junit.jupiter.api.Test;

class LineTest {

    @org.junit.jupiter.api.Test
    void getType() {
    }

    @Test
    void createLineOf() {
        Line line = Line.createLineOf("ט", 0);
        assert line.getType() == Line.LineType.PSEUDONYM;
        line = Line.createLineOf("ה", 0);
        assert line.getType() == Line.LineType.REMARK;
    }
}