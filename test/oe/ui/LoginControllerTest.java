package oe.ui;

import com.sforce.ws.ConnectionException;
import oe.sf.Conn;
import oe.util.Preference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.prefs.Preferences;

import static oe.util.Encryptor.decrypt;
import static oe.util.Encryptor.encrypt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LoginControllerTest {

    @BeforeEach
    void setUp() throws ConnectionException {
        Conn.createInstance("rui.ma@openedgepay.com.adminrm","RJkaiK0920Dashu!",true);

    }
    @Test
     void test_loginButtonPositive() throws ConnectionException {
        Preferences userPref = Preference.getUserPref();
        userPref.put("username", "rui.ma@openedgepay.com.adminrm");

        String userPrefStr = userPref.get("username","");
        assertEquals(userPrefStr, "rui.ma@openedgepay.com.adminrm");
        //test if it update the preference correctly
    }
    @Test
    void test_loginButtonNegative() throws ConnectionException{
        Preferences sysPref = Preference.getSysPref();
        String wcut = sysPref.get(Preference.WhoCanUseTool,"rui.ma@openedgepay.com,charlton.zheng@openedgepay.com,thomas.garcia@openedgepay.com");
        String un = "r.ma@openedgepay.com.admin";
        un = un.substring(0, un.indexOf(".com")+ 4);
        if(wcut.contains(un) == true){

        Preferences userPref = Preference.getUserPref();
        userPref.put("namer","ruim");

        String userPrefStr = userPref.get("namer","");
        assertEquals(userPrefStr,"ruim");}
    }
    @Test
    void test_whocanuseToolPositive() throws ConnectionException{
        Preferences sysPref = Preference.getSysPref();
        String wcut = sysPref.get(Preference.WhoCanUseTool,"rui.ma@openedgepay.com,charlton.zheng@openedgepay.com,thomas.garcia@openedgepay.com");
        String un = "rui.ma@openedgepay.com.admin";
        un = un.substring(0, un.indexOf(".com")+ 4);
        assertEquals(wcut.contains(un),true);
    }
@Test
void test_whocanuseToolNegative()throws ConnectionException{

        Preferences sysPref = Preference.getSysPref();
        String wcut = sysPref.get(Preference.WhoCanUseTool,"rui.ma@openedgepay.com,charlton.zheng@openedgepay.com,thomas.garcia@openedgepay.com");
        String un = "r.ma@openedgepay.com.admin";
        un = un.substring(0, un.indexOf(".com")+ 4);
        assertEquals(wcut.contains(un),false);

}
@Test
    void test_passwordEncryptionPositive(){
        String key= "OeEncryptKey9792";
        String initVector = "OeEncryptionInit";

        String expected = "RJkaiK0920Dashu!";
        String encryptedStr = encrypt(key, initVector, expected);
        String decryptedStr = decrypt(key, initVector, encryptedStr);
        assertEquals(expected, decryptedStr);
        assertNotEquals(expected, encryptedStr);
// test if the password encrypted and decrypted correctly
}

}