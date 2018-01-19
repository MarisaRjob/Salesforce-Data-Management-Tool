package oe.sf;

import com.sforce.ws.ConnectionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnTest {

    private String username;
    private String password;
    private Boolean useSandboxEndpoint;
    Conn CONN;
    @BeforeEach
    void setUp() {
        username = "rui.ma@openedgepay.com.adminrm";
        password = "RJkaiK0920Dashu!";
        useSandboxEndpoint = true;


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void isSandboxLogin() {
    }

    @Test
    void createInstance_positive() throws ConnectionException {
        Conn connection = Conn.createInstance(username,password,useSandboxEndpoint);
        assertEquals(connection != null, true);
    }
    @Test
    void createInstance_negative() throws ConnectionException{
        Conn connection = Conn.createInstance("ri.ma@openedgepay.com.adminrm",password,useSandboxEndpoint);
        assertNotEquals(connection!=null,true);
    }


    @Test
    void getInstance_positive() throws ConnectionException {
        Conn.createInstance(username,password,useSandboxEndpoint);
        Conn connection = Conn.getInstance();
        assertEquals(connection != null, true);
    }
    @Test
    void getInstance_negative() throws ConnectionException {
        Conn.createInstance("ri.ma@openedgepay.com.adminrm",password,useSandboxEndpoint);
        Conn connection = Conn.getInstance();
        assertEquals(connection != null, true);
    }


}