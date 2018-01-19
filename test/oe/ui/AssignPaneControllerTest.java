package oe.ui;

import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Action__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import oe.sf.Conn;
import oe.ui.sf.UiAction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignPaneControllerTest {
    private static Conn conn;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    static void initialize() throws ConnectionException {

        //todo encrypted password
        Conn.createInstance("rui.ma@openedgepay.com.adminrm", "RJkaiK0920Dashu!", true );
    }

    @Test
    void ApplyButton_positiveTest() throws ConnectionException {

        Conn conn = Conn.getInstance();
        String query = "SELECT ID, Link_to_Record__c, Description__c, Action_Type__c, Action_Detail__c, Object__c, Subject__c, Priority__c FROM Action__c where id = 'a08q0000008ha98'";
        SObject[] actions = conn.query(query);
        Action__c a = (Action__c) actions[0];

        a.setAssigned_To__c("005F0000003aLynIAE");// set Assignto Charlton Zheng
        actions[0] = a;
        SaveResult[] results = conn.update(actions);
        assert(results[0].isSuccess());
       // test if it update successfully

        String expectedValue = "005F0000003aLynIAE";
        SObject[] actions2 = conn.query("select id, Assigned_To__c FROM Action__c where id  = 'a08q0000008ha98'");
        String actualValue = ((Action__c)actions2[0]).getAssigned_To__c();
        assertEquals(expectedValue, actualValue);
        // test if it update correctly
    }
    @Test
    void ApplyButton_negativeTest() throws ConnectionException {
        Conn conn = Conn.getInstance();
        String query = "SELECT ID, Link_to_Record__c, Description__c, Action_Type__c, Action_Detail__c, Object__c, Subject__c, Priority__c FROM Action__c where id = 'a08q0000008ha98'";
        SObject[] actions = conn.query(query);
        Action__c a = (Action__c) actions[0];

        a.setAssigned_To__c("005F0000003aLynI");
        actions[0] = a;
        SaveResult[] results = conn.update(actions);
        assert (!results[0].isSuccess());
    }
    @Test
    void handleCancelButtonAction() {
    }

}