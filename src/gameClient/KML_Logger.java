package gameClient;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class KML_Logger {
    private String name;
    private StringBuilder content;

    private static final String NodeStyle = "node";
    private static final String AndroidStyle = "fruit-android";
    private static final String AppleStyle = "fruit-apple";
    private static final String RobotStyle = "robot";

    public KML_Logger(String name) {
        this.name = name;
        content = new StringBuilder();
        content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        content.append("<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n");
        content.append("  <Document>\r\n");
        content.append("    <name>stage: " + name + " The maze of waze" + "</name>\r\n");
        content.append("	 <Style id=\"" + NodeStyle + "\">\r\n");
        content.append("      <IconStyle>\r\n");
        content.append("        <Icon>\r\n");
        content.append(
                "          <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>\r\n");
        content.append("        </Icon>\r\n");
        content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
        content.append("      </IconStyle>\r\n");
        content.append("    </Style>");
        content.append("	 <Style id=\"" + AndroidStyle + "\">\r\n");
        content.append("      <IconStyle>\r\n");
        content.append("        <Icon>\r\n");
        content.append("          <href>http://maps.google.com/mapfiles/kml/paddle/ylw-circle.png</href>\r\n");
        content.append("        </Icon>\r\n");
        content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
        content.append("      </IconStyle>\r\n");
        content.append("    </Style>");
        content.append("	 <Style id=\"" + AppleStyle + "\">\r\n");
        content.append("      <IconStyle>\r\n");
        content.append("        <Icon>\r\n");
        content.append("          <href>http://maps.google.com/mapfiles/kml/paddle/red-circle.png</href>\r\n");
        content.append("        </Icon>\r\n");
        content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
        content.append("      </IconStyle>\r\n");
        content.append("    </Style>");
        content.append("	 <Style id=\"" + RobotStyle + "\">\r\n");
        content.append("      <IconStyle>\r\n");
        content.append("        <Icon>\r\n");
        content.append("          <href>http://maps.google.com/mapfiles/kml/pal4/icon62.png</href>\r\n");
        content.append("        </Icon>\r\n");
        content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
        content.append("      </IconStyle>\r\n");
        content.append("    </Style>\r\n");
    }

    private void addPlaceMark(String type, String pos) {
        LocalDateTime now = LocalDateTime.now();
        content.append("    <Placemark>\r\n");
        content.append("      <TimeStamp>\r\n");
        content.append("        <when>" + now + "</when>\r\n");
        content.append("      </TimeStamp>\r\n");
        content.append("      <styleUrl>#" + type + "</styleUrl>\r\n");
        content.append("      <Point>\r\n");
        content.append("        <coordinates>" + pos + "</coordinates>\r\n");
        content.append("      </Point>\r\n");
        content.append("    </Placemark>\r\n");

    }

    public void addNodePlaceMark(String pos) {
        addPlaceMark(NodeStyle, pos);
    }

    public void addRobotPlaceMark(String pos) {
        addPlaceMark(RobotStyle, pos);
    }

    public void addFruitPlaceMark(int type, String pos) {
        addPlaceMark(type == 1 ? AppleStyle : AndroidStyle, pos);
    }

    public void closeDocument() {
        content.append("  </Document>\r\n");
        content.append("</kml>");
        try {
            PrintWriter pw = new PrintWriter(new File("data/" + name + ".kml")); // change to save on data folder
            pw.write(content.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

}