package bibcards.util;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class HebToGregDateConverter {
    File file = new File("src/main/resources/tmp");
    private BufferedReader reader;
    private String nextContentLine = null;
    private int numLines = 0;
    private boolean fileExhausted = false;

    private Map<String, Integer> mapYears = new HashMap<>() {{
        put("תר",	1840);
        put("תרא",	1841);
        put("תרב",	1842);
        put("תרג",	1843);
        put("תרד",	1844);
        put("תרה",	1845);
        put("תרו",	1846);
        put("תרז",	1847);
        put("תרח",	1848);
        put("תרט",	1849);
        put("תרי",	1850);
        put("תריא",	1851);
        put("תריב",	1852);
        put("תריג",	1853);
        put("תריד",	1854);
        put("תרטו",	1855);
        put("תרטז",	1856);
        put("תריז",	1857);
        put("תריח",	1858);
        put("תריט",	1859);
        put("תרכ",	1860);
        put("תרכא",	1861);
        put("תרכב",	1862);
        put("תרכג",	1863);
        put("תרכד",	1864);
        put("תרכה",	1865);
        put("תרכו",	1866);
        put("תרכז",	1867);
        put("תרכח",	1868);
        put("תרכט",	1869);
        put("תרל",	1870);
        put("תרלא",	1871);
        put("תרלב",	1872);
        put("תרלג",	1873);
        put("תרלד",	1874);
        put("תרלה",	1875);
        put("תרלו",	1876);
        put("תרלז",	1877);
        put("תרלח",	1878);
        put("תרלט",	1879);
        put("תרמ",	1880);
        put("תרמא",	1881);
        put("תרמב",	1882);
        put("תרמג",	1883);
        put("תרמד",	1884);
        put("תרמה",	1885);
        put("תרמו",	1886);
        put("תרמז",	1887);
        put("תרמח",	1888);
        put("תרמט",	1889);
        put("תרנ",	1890);
        put("תרנא",	1891);
        put("תרנב",	1892);
        put("תרנג",	1893);
        put("תרנד",	1894);
        put("תרנה",	1895);
        put("תרנו",	1896);
        put("תרנז",	1897);
        put("תרנח",	1898);
        put("תרנט",	1899);
        put("תרס",	1900);
        put("תרסא",	1901);
        put("תרסב",	1902);
        put("תרסג",	1903);
        put("תרסד",	1904);
        put("תרסה",	1905);
        put("תרסו",	1906);
        put("תרסז",	1907);
        put("תרסח",	1908);
        put("תרסט",	1909);
        put("תרע",	1910);
        put("תרעא",	1911);
        put("תרעב",	1912);
        put("תרעג",	1913);
        put("תרעד",	1914);
        put("תרעה",	1915);
        put("תרעו",	1916);
        put("תרעז",	1917);
        put("תרעח",	1918);
        put("תרעט",	1919);
        put("תרפ",	1920);
        put("תרפא",	1921);
        put("תרפב",	1922);
        put("תרפג",	1923);
        put("תרפד",	1924);
        put("תרפה",	1925);
        put("תרפו",	1926);
        put("תרפז",	1927);
        put("תרפח",	1928);
        put("תרפט",	1929);
        put("תרצ",	1930);
        put("תרצא",	1931);
        put("תרצב",	1932);
        put("תרצג",	1933);
        put("תרצד",	1934);
        put("תרצה",	1935);
        put("תרצו",	1936);
        put("תרצז",	1937);
        put("תרצח",	1938);
        put("תרצט",	1939);
        put("תש",	1940);
        put("תשא",	1941);
        put("תשב",	1942);
        put("תשג",	1943);
        put("תשד",	1944);
        put("תשה",	1945);
        put("תשו",	1946);
        put("תשז",	1947);
        put("תשח",	1948);
        put("תשט",	1949);
        put("תשי",	1950);
        put("תשיא",	1951);
        put("תשיב",	1952);
        put("תשיג",	1953);
        put("תשיד",	1954);
        put("תשיה",	1955);
        put("תשיו",	1956);
        put("תשיז",	1957);
        put("תשיח",	1958);
        put("תשיט",	1959);
        put("תשכ",	1960);
        put("תשכא",	1961);
        put("תשכב",	1962);
        put("תשכג",	1963);
        put("תשכד",	1964);
        put("תשכה",	1965);
        put("תשכו",	1966);
        put("תשכז",	1967);
        put("תשכח",	1968);
        put("תשכט",	1969);
        put("תשל",	1970);
        put("תשלא",	1971);
        put("תשלב",	1972);
        put("תשלג",	1973);
        put("תשלד",	1974);
        put("תשלה",	1975);
        put("תשלו",	1976);
        put("תשלז",	1977);
        put("תשלח",	1978);
        put("תשלט",	1979);
        put("תשם",	1980);
        put("תשמ",	1980);
        put("תשמא",	1981);
        put("תשמב",	1982);
        put("תשמג",	1983);
        put("תשמד",	1984);
        put("תשמה",	1985);
        put("תשמו",	1986);
        put("תשמז",	1987);
        put("תשמח",	1988);
        put("תשמט",	1989);
        put("תשנ",	1990);
        put("תשן",	1990);
        put("תשנא",	1991);
        put("תשנב",	1992);
        put("תשנג",	1993);
        put("תשנד",	1994);
        put("תשנה",	1995);
        put("תשנו",	1996);
        put("תשנז",	1997);
        put("תשנח",	1998);
        put("תשנט",	1999);
    }};
    private Map<String, Integer> mapMonths = new HashMap<>() {{
        put("תשרי", 1);
        put("חשון", 2);
        put("כסלו", 3);
        put("טבת", 4);
        put("שבט", 5);
        put("אדר", 6);
        put("ניסן", 7);
        put("אייר", 8);
        put("סיון", 9);
        put("תמוז", 10);
        put("אב", 11);
        put("אלול", 12);
    }};
    private Map<String, String> mapDays = new HashMap<>() {{
        put("א",	"1");
        put("ב",	"2");
        put("ג",	"3");
        put("ד",	"4");
        put("ה",	"5");
        put("ו",	"6");
        put("ז",	"7");
        put("ח",	"8");
        put("ט",	"9");
        put("י",	"10");
        put("יא",	"11");
        put("יב",	"12");
        put("יג",	"13");
        put("יד",	"14");
        put("טו",	"15");
        put("טז",	"16");
        put("יז",	"17");
        put("יח",	"18");
        put("יט",	"19");
        put("כ",	"20");
        put("כא",	"21");
        put("כב",	"22");
        put("כג",	"23");
        put("כד",	"24");
        put("כה",	"25");
        put("כו",	"26");
        put("כז",	"27");
        put("כח",	"28");
        put("כט",	"29");
        put("ל",	"30");
        put("לא",	"31");
    }};

    private void convert() throws IOException {
        reader = new BufferedReader(new FileReader(file));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                Logger.log(1, "file exhausted after " + numLines + " lines");
                fileExhausted = true;
                return;
            }
            numLines++;
            // skip empty lines or lines starting with comment character
            if (line.length() < 1)  // skip empty lines
                continue;
            if (line.startsWith("#")) // skip comment lines
                continue;

            // create the URL
            Logger.log(4, numLines + ": parsing line <" + line + ">");
            Logger.flush();
            String[] parts = line.split(" ");
            Logger.log(4, numLines + ": parts length=" + parts.length);
            Logger.flush();

            String hebYear = null;
            String hebMonth = null;
            String hebDay = null;
            if (parts.length == 3) {
                hebDay = parts[0];
                hebMonth = parts[1];
                hebYear = parts[2];

            } else if (parts.length == 2) {
                hebDay = "טו";
                hebMonth = parts[0];
                hebYear = parts[1];
            } else if (parts.length == 1) {
                hebDay = "טו";
                hebMonth = "ניסן";
                hebYear = parts[0];
            } else
                Logger.error(numLines + "<" + line + "> - parts length=" + parts.length);

            if ((hebYear != null)  && !mapYears.containsKey(hebYear)) {
                Logger.log(1, hebYear + " : not in map");
                continue;
            }
            String urlYear = (hebYear != null) ? mapYears.get(hebYear).toString() : "";
            String urlMonth = (hebMonth != null) ? mapMonths.get(hebMonth).toString() : "";
            String urlDay = (hebDay != null) ? mapDays.get(hebDay) : "";

            String gregDate = urlDay.concat(".").concat(urlMonth).concat(".").concat(urlYear);
            Logger.log(1, numLines + ": " + line + "\t\tY=" + urlYear + "\t\tM=" + urlMonth + "\t\tD=" + urlDay);
            Logger.log(1, gregDate);

//            String convertUrl = "https://www.hebcal.com/converter?cfg=json&hy=".concat(String.valueOf(urlYear));
//            if (!urlMonth.isEmpty())
//                convertUrl = convertUrl.concat("&hm=").concat(urlMonth);
//            if (!urlDay.isEmpty())
//                convertUrl = convertUrl.concat("&hd=").concat(urlDay);
//            convertUrl = convertUrl.concat("&h2g=1");
//
//            Logger.log(1, numLines + ": " + line + "\t\tY=" + urlYear + "\t\tM=" + urlMonth + "\t\tD=" + urlDay);
//            Logger.log(1, convertUrl);
//
//            Logger.log(1, "URL: " + convertUrl);
//            try {
//                Desktop.getDesktop().browse(new URI(convertUrl));
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
        }

    }

    public static void main(String[] args) throws IOException {
        HebToGregDateConverter converter = new HebToGregDateConverter();
        converter.convert();
    }

}
