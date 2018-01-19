package oe.ui;

import com.sforce.ws.ConnectionException;
import oe.sf.Conn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainPaneControllerTest {
    @BeforeEach
    void setUp() throws ConnectionException {
        Conn.createInstance("rui.ma@openedgepay.com.adminrm","RJkaiK0920Dashu!",true);
    }

    @Test
    void handleCloseItemsAction() {
    }

}