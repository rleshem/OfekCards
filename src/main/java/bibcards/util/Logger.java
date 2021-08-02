package bibcards.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    public static String folderLogfiles = "C:\\Users\\OWNER\\IdeaProjects\\bibcards\\src\\main\\log\\";

    private static int logLevel = 1;
    private static boolean isLog = false;
    //        private static String logUniqueIdenifier = "";
    private static FileWriter logWriter = null;
    private static boolean initialized = false;

/*    static {
        if (Setup.getSetup().isHasKey(Setup.logProp))
            logLevel = Setup.getSetup().getIntProperty(Setup.logProp);
        if (Setup.getSetup().getBooleanProperty(Setup.logOutput) == true) {
            isLog = true;
            //IZKOR logUniqueIdenifier = Setup.getSetup().getStringProperty(Setup.logUniqueIdentifier);
        }
    }*/

    public static void log(int level, String... a) {
        if (!initialized)
            initLogs();
        if (level > logLevel)
            return; // don't log this call
        StringBuilder sb = new StringBuilder();
        if (a.length < 1)
            throw new IllegalArgumentException("log(level, string, ...) expects at least 2 arguments, received only " + a.length);
        sb.append(a[0]); // always expecting String as second argument
        for (int i = 1; i < a.length; i++) {
            sb.append(a[i]);
        }
        String res = sb.toString();
        System.out.println(res);
        if (isLog) {
            if (logWriter == null)
                createLogFile();
            try {
                logWriter.write(res.endsWith("\n") ? res : res + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initLogs() {
        Setup setup = null;
        setup = Setup.getSetup();
        if (Setup.getSetup().isHasKey(Setup.logProp))
            logLevel = Setup.getSetup().getIntProperty(Setup.logProp);
        if (Setup.getSetup().getBooleanProperty(Setup.logOutput) == true) {
            isLog = true;
            //IZKOR logUniqueIdenifier = Setup.getSetup().getStringProperty(Setup.logUniqueIdentifier);
        }
        initialized = true;
    }
    private static void createLogFile() {
        String fileName =
                folderLogfiles
                        + MiscHelper.getCurrentTimeAsString()
                        + Setup.getSetup().getMainClassname()
//                        + "_" + logUniqueIdenifier
                        + "_" + logLevel
                        + ".logo";
        File logFile = new File(fileName);
        try {
            logWriter = new FileWriter(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() throws IOException {
        if (logWriter != null) {
            logWriter.close();
        }
    }
}
