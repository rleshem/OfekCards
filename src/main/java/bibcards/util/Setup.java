package bibcards.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

public class Setup {

    public static String logProp = "log";
    public static String logOutput = "log_output";
    public static String dataFile = "data_file";
    public static String prefixes = "prefixes";

    /*
        public static String dbProp = "db";
        public static String searchProviders = "search_providers";
        public static String queryProp = "query";
        public static String queryArgPrefix = "@";
        public static String queryAllSoldiersProp = "get_all_soldiers_query";
        public static String exploreQuery = "explore_query";
        public static String fromDateArg = "from_date";
        public static String toDateArg = "to_date";
        public static String uuidArg = "uuid";
        public static String utilDataFolder = "util_data_folder";
        public static String uuidsInDbFilename = "db_uuids_file";
        public static String izkor_uuids_file = "izkor_uuids_file";
        public static String uuidsDiffFilename = "diff_uuids_file";
        public static String db_min_skip = "db_min_skip";
        public static String db_max_skip = "db_max_skip";
        public static String db_min_conn_bro = "db_min_conn_bro";
        public static String db_max_conn_bro = "db_max_conn_bro";
        public static String db_batch_size_conn_bro = "db_batch_size_conn_bro";
        public static String db_failed_get_conn_bro = "db_failed_get_conn_bro";
        public static String db_failed_json_conn_bro = "db_failed_json_conn_bro";
        public static String skipNewspapers = "skip_newspapers";
        public static String skipCommunityNewspapers = "skip_community_newspapers";
        public static String logUniqueIdentifier = "log_unique_identifier";
        public static String inputLogFile = "log_input_file";
        public static String filterNews = "apply_filter_news";
        public static String filterIgnore = "apply_filter_ignore";
     */

    private static Setup setup = null;
    private String mainClassname = "~not set~";

    public static void main(String[] args) {
        Setup setup;
        setup = Setup.getSetup();
    }

    public static Setup getSetup() {
        if (setup == null)
            setup = new Setup();
        return setup;
    }

    private static String propFileName = "src/main/resources/bibcards.properties";

    private Properties properties = null;

    private Setup() {
        File propFile = new File(propFileName);
        if (!propFile.exists())
            try {
                throw new IOException("BiblioCards properties file [" + propFileName + "] not found");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

        try (InputStream input = new FileInputStream(propFileName)) {
            properties = new Properties();

            // load a properties file
            properties.load(input);

            // get the property value and print it out
            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                // DO NOT use Logger here - stackoverflow WILL happen
                System.out.println(key + "=" + value);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("end Setup");
    }

    public boolean isHasKey(String key) {
        return properties.containsKey(key);
    }

    public String getStringProperty(String key) {
        if (properties.containsKey(key))
            return properties.getProperty(key);
        throw new NoSuchElementException("property [" + key + "] not found");
    }

    public boolean getBooleanProperty(String key) {
        if (!properties.containsKey(key))
            return false;
        String value = properties.getProperty(key);
        if (value.equals("true"))
            return true;
        if (value.equals("false"))
            return false;
        throw new NoSuchElementException("boolean property [" + key + "] has illegal value [" + value + "]");
    }

    public int getIntProperty(String key) {
        if (properties.containsKey(key))
            return Integer.valueOf(properties.getProperty(key)).intValue();
        throw new NoSuchElementException("property [" + key + "] not found");
    }

    public void setMainClassname(String name) {
        this.mainClassname = name;
    }

    public String getMainClassname() {
        return mainClassname;
    }
}
