package bibcards.output;

import bibcards.input.Card;
import bibcards.input.Line;
import bibcards.input.line.*;
import bibcards.process.DateProcessor;
import bibcards.util.Logger;
import bibcards.util.Setup;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static bibcards.input.FieldConstants.*;

public class SqliteHandler {

    protected static String jdbcPrefix = "jdbc:sqlite:";
    private static SqliteHandler sqliteHandler = null;
    private Connection database;
    private String sqlInsertCard;

    public static SqliteHandler getHandler(String... dbNameOptional) {
        if (sqliteHandler == null)
            sqliteHandler = new SqliteHandler(dbNameOptional);
        return sqliteHandler;
    }

    private void initQueries() {
        sqlInsertCard = "INSERT INTO cards(" +
                FIELD_CARD_ID + "," +           // 1
                FIELD_TYPE + "," +              // 2

                FIELD_SOURCE + "," +            // 3
                FIELD_CANONIZED_DATE + "," +    // 4

                FIELD_TITLE + "," +             // 5
                FIELD_SUB_TITLE + "," +         // 6

                FIELD_IMPORTANCE + "," +        // 7
                FIELD_PERSON + "," +            // 8
                FIELD_REPORTER + "," +          // 9
                FIELD_PSEUDONYM + "," +         // 10


                FIELD_VOLUME + "," +            // 11
                FIELD_SECTION + "," +           // 12
                FIELD_PAGE + "," +              // 13

                FIELD_REMARK + "," +            // 14

                FIELD_GREG_DATE + "," +         // 15
                FIELD_MAN_GREG_DATE + "," +     // 16
                FIELD_HEB_DATE + "," +          // 17
                FIELD_MAN_HEB_DATE + "," +      // 18

                FIELD_CARD +                    // 19

                ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    private SqliteHandler(String... dbNameOptional) {
        database = getDatabase(dbNameOptional);
        initQueries();
    }

    public static void closeDatabase() throws SQLException {
        if (sqliteHandler == null) {
            Logger.error("database is not open!");
            System.exit(-1);
        }
        sqliteHandler.database.close();
        sqliteHandler.database = null;
        sqliteHandler = null;
    }

    public static Connection reopenDatabase(String dbName) throws SQLException {
        closeDatabase();
        Connection conn = getHandler(dbName).getDatabase(dbName);
        return conn;
    }

    public Connection getDatabase(String... dbNameOptional) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String databaseName;
        if (dbNameOptional.length > 0)
            databaseName = dbNameOptional[0];
        else
            databaseName = Setup.getSetup().getStringProperty(Setup.dbProp);
        String url = jdbcPrefix + databaseName;
        File propFile = new File(databaseName);
        boolean dbExist = propFile.exists();
        Logger.log(1, "db [" + databaseName + "] - " + (dbExist ? "exists" : "not exist"));

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                Logger.log(1, "The driver name is " + meta.getDriverName());
                Logger.log(1, "Database " + databaseName +
                        ((dbExist) ? " was found" : " has been created."));
            }
        } catch (SQLException e) {
            Logger.log(1, e.getMessage());
            System.exit(-2);
        }
        return connection;
    }

    public Card getDbCardById(String id) {
        String query = "select * from cards where " + FIELD_CARD_ID + " = '" + id + "'";
        List<Card> cardList = SqliteHandler.getHandler().executeSelectCardsQuery(query);
        if (cardList.size() != 1) {
            System.err.println("retrieved uuid <" + id + "> from db, got " + cardList.size() + " results, expected exactly 1");
            return null;
        }
        return cardList.get(0);
    }

    public int insertListOfCards(List<Card> cards, boolean isTest) {
        Logger.log(1, "will insert " + cards.size() + " cards");
        int result = 0;
        for (Card card : cards) {
            result += insertCard(card, isTest);
        }
        return result;
    }

    public int insertCard(Card card, boolean isTest) {
        /*
        tests are bypassed, returning successfully, to maintain database purity
         */
        if (isTest)
            return 1;

        if (card == null)
            return 0;
        Logger.log(1, "will insert card " + card.getCardNumber());
        try {
            Logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int result = -1;
        try {
            PreparedStatement statement = database.prepareStatement(sqlInsertCard);

            statement.setInt(   1, card.getCardNumber());
            statement.setString(2, card.getCardType().toString());
            statement.setString(3, card.getLineContent(Line.LineType.SOURCE, true));
            statement.setString(4, card.getCanonizedDate());
            statement.setString(5, card.getLineContent(Line.LineType.TITLE, true));
            statement.setString(6, card.getLineContent(Line.LineType.SUB_TITLE, true));
            statement.setString(7, card.getLineContent(Line.LineType.IMPORTANCE, true));
            statement.setString(8, card.getLineContent(Line.LineType.PERSON, true));
            statement.setString(9, card.getLineContent(Line.LineType.REPORTER, true));
            statement.setString(10, card.getLineContent(Line.LineType.PSEUDONYM, true));
            statement.setString(11, card.getLineContent(Line.LineType.VOLUME, true));
            statement.setString(12, card.getLineContent(Line.LineType.SECTION, true));
            statement.setString(13, card.getLineContent(Line.LineType.PAGE, true));
            statement.setString(14, card.getLineContent(Line.LineType.REMARK, true));
            statement.setString(15, card.getLineContent(Line.LineType.GREG_DATE, true));
            statement.setString(16, card.getLineContent(Line.LineType.MANUAL_GREG_DATE, true));
            statement.setString(17, card.getLineContent(Line.LineType.HEB_DATE, true));
            statement.setString(18, card.getLineContent(Line.LineType.MANUAL_HEB_DATE, true));
            statement.setString(19, card.getLineContent(Line.LineType.CARD, true));

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    private Card resultToCard(ResultSet rs) {
        Card card = null;
        try {
            String typeOfCard = rs.getString(FIELD_TYPE);
            card = Card.genCardByType(typeOfCard);
            card.setCardNumber(rs.getInt(FIELD_CARD_ID));

            addLineToCard(card, new SourceLine(), rs.getString(FIELD_SOURCE));

            LocalDate canonizedDate = DateProcessor.getDateProcessor().getLocalDateFromCanonized(rs.getString(FIELD_CANONIZED_DATE));
            card.setCanonizedDate(canonizedDate);

            addLineToCard(card, new TitleLine(), rs.getString(FIELD_TITLE));
            addLineToCard(card, new SubTitleLine(), rs.getString(FIELD_SUB_TITLE));
            addLineToCard(card, new ImportanceLine(), rs.getString(FIELD_IMPORTANCE));
            addLineToCard(card, new PersonLine(), rs.getString(FIELD_PERSON));
            addLineToCard(card, new ReporterLine(), rs.getString(FIELD_REPORTER));
            addLineToCard(card, new PseudonymLine(), rs.getString(FIELD_PSEUDONYM));
            addLineToCard(card, new VolumeLine(), rs.getString(FIELD_VOLUME));
            addLineToCard(card, new SectionLine(), rs.getString(FIELD_SECTION));
            addLineToCard(card, new PageLine(), rs.getString(FIELD_PAGE));
            addLineToCard(card, new RemarkLine(), rs.getString(FIELD_REMARK));
            addLineToCard(card, new GregDateLine(), rs.getString(FIELD_GREG_DATE));
            addLineToCard(card, new ManualGregDateLine(), rs.getString(FIELD_MAN_GREG_DATE));
            addLineToCard(card, new HebDateLine(), rs.getString(FIELD_HEB_DATE));
            addLineToCard(card, new ManualHebDateLine(), rs.getString(FIELD_MAN_HEB_DATE));
            addLineToCard(card, new CardLine(), rs.getString(FIELD_CARD));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return card;
    }

    private void addLineToCard(Card card, Line line, String content) {
        if (content != null) {
            line.setContent(content, false);
            card.addLine(line);
        }
    }

    public List<Card> executeSelectCardsQuery(String sql) {
        List<Card> results = new ArrayList<>();
        try {
            Statement stmt = database.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through result set
            while (rs.next()) {
                results.add(resultToCard(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    public List<Card> loadAllCards(String query) {
        // query example: "select * from cards"
        List<Card> cards = SqliteHandler.getHandler().executeSelectCardsQuery(query);
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            Logger.log(1, i +
                    ": id=" + card.getCardNumber() +
                    ", type=" + card.getCardType() +
                    ", canonizedDate=" + card.getCanonizedDate());
        }
        Logger.log(1, "loaded " + cards.size() + " cards by query: <" + query + ">");
        return cards;
    }

    public static void main(String[] args) {
        List<Card> cards = SqliteHandler.getHandler().loadAllCards("select * from cards");
        Logger.log(1, "loaded " + cards.size() + " cards");
    }


    /*
    public List<String> executeSelectSoldiersSingleColumn(String fieldName) {
        List<String> results = new ArrayList<>();
        try {
            Statement stmt = database.createStatement();
            String sql = "select " + fieldName + " from soldiers;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String uuid = rs.getString(FIELD_SLD__UUID);
                results.add(uuid);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return results;
    }
     */

}
