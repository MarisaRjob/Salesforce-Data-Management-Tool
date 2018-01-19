package oe.util;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Created by rui.ma on 9/25/2017.
 */
class TestPreference  {
    //log
    private static Logger log = Logger.getLogger(TestPreference.class.getName());

    @BeforeAll
   static void initAll() {

        Preference.overwriteDefaultPrefName("oe.sfAdminTools.test");

        try {
            Preference.getSysPref().clear();
            Preference.getUserPref().clear();
        } catch (BackingStoreException e) {
            log.error(e);
        }
    }

    @BeforeEach
    void initEach() {
        try {
            Preference.getSysPref().clear();
            Preference.getUserPref().clear();
        } catch (BackingStoreException e) {
            log.error(e);
        }
    }

    @Test
    void testExportUserAndSysPref() throws IOException {
        Preferences up = Preference.getUserPref();
        up.put("string","testing");
        up.putInt("int", 1);
        up.putBoolean("bool", true);
        up.putDouble("double", 2.0);

        String exportPath = "userPref.testExportUserAndSysPref.xml";
        Preference.exportPref(up, exportPath);

        Preferences sp = Preference.getSysPref();
        sp.put("string","testing");
        sp.putInt("int", 1);
        sp.putBoolean("bool", true);
        sp.putDouble("double", 2.0);

        String exportPath1 = "sysPref.testExportUserAndSysPref.xml";
        Preference.exportPref(sp, exportPath1);
    }

    @Test
    void test_importPrefIfFileExist() throws IOException, BackingStoreException {
        Preferences up = Preference.getUserPref();
        up.put("string","testing");
        up.putInt("int", 1);
        up.putBoolean("bool", true);
        up.putDouble("double", 2.0);

        File exportFile = File.createTempFile("test", "xml");
        String exportPath = exportFile.getPath();
        Preference.exportPref(up, exportPath);

        up.clear();

        Preference.importPrefIfFileExistAndDelete(up, exportPath);
        assertFalse(exportFile.exists());

        assertEquals("testing", up.get("string", ""));
        assertEquals(1, up.getInt("int", 0));
        assertEquals(true, up.getBoolean("bool", false));
        assertEquals(2.0, up.getDouble("double", 0));
    }

   @Test
    void testExportWithError() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Preference.exportPref( null, "c:/test.xml"));
   }


    @Test
    void testExportUserPrefWithoutError() throws IOException {
        Preferences up = Preference.getUserPref();
        up.put("test","testing");

        Preference.exportPref(up, "c:/test.xml");
        Files.delete(Paths.get("c:/test.xml"));
    }


    @Test
    void testImportUserPrefWithoutError() throws BackingStoreException {

        Preferences up = Preference.getUserPref();
        up.put("test","testing");

        Preference.exportPref(up, "c:/test.xml");

        up.clear();

        Preference.importPref(up, "c:/test.xml");

        assertEquals("testing", up.get("test", ""));

    }


    @AfterAll
    static void cleanAll() {

        try {
            Preference.getSysPref().removeNode();
            Preference.getUserPref().removeNode();
        } catch (BackingStoreException e) {
            log.error(e);
        }
    }
}
