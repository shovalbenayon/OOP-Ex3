package Tests;

import gameClient.KML_Logger;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class KML_LoggerTest {

    static KML_Logger log;
    static final double EPS = 0.00001;
    static final String FILE_NAME = "JUNIT_TEST";

    @BeforeAll
    static void createKMLFile() {
        log = new KML_Logger(FILE_NAME);
    }

    @Test
    @Order(1)
    void addNodePlaceMark() {
        double lang = 35.19797411945117;
        double lat = 32.102764564705886;
        for (int i = 1; i <= 25; i++) {
            lang = lang + EPS;
            lat = lat + EPS * EPS;
            log.addNodePlaceMark(lang + "," + lat + ",0.0");
        }
    }

    @Test
    @Order(2)
    void addRobotPlaceMark() {
        double lang = 35.19797411945117;
        double lat = 32.102764564705886;
        for (int i = 1; i <= 3; i++) {
            lang = lang + EPS;
            lat = lat + EPS * EPS;
            log.addRobotPlaceMark(lang + "," + lat + ",0.0");
        }
    }

    @Test
    @Order(3)
    void addFruitPlaceMark() {
        double lang = 35.19797411945117;
        double lat = 32.102764564705886;
        for (int i = 1; i <= 100; i++) {
            lang = lang + EPS;
            lat = lat + EPS * EPS;
            log.addFruitPlaceMark(i % 2 == 0 ? 1 : -1, lang + "," + lat + ",0.0");
        }
    }

    @Test
    @Order(4)
    void closeAndSaveDocument() {
        log.closeDocument();
    }

    @Test
    @Order(5)
    void readKmlLog() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/" + FILE_NAME + ".kml"));
            assertNotEquals(null, br.readLine());
        } catch (IOException e) {
            fail();
        }
    }
}