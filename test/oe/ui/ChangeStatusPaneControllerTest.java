package oe.ui;

import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Action__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import oe.sf.Conn;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangeStatusPaneControllerTest {
    @BeforeAll
    static void initialize() throws ConnectionException {
        Conn.createInstance("rui.ma@openedgepay.com.adminrm", "RJkaiK0920Dashu!", true );
    }

    @Test
    void ApplyButton_positiveTest() throws ConnectionException {
        Conn conn = Conn.getInstance();
        String query = "SELECT ID, Link_to_Record__c, Description__c, Action_Type__c, Action_Detail__c, Object__c, Subject__c, Priority__c FROM Action__c where id = 'a08q0000008ha98'";
        SObject[] actions = conn.query(query);
        Action__c a = (Action__c) actions[0];

        a.setStatus__c("In Process");// set Assignto Charlton Zheng
        actions[0] = a;
        SaveResult[] results = conn.update(actions);
        assert(results[0].isSuccess());
        // test if it update successfully

        String expectedValue = "In Process";
        SObject[] actions2 = conn.query("select id, Status__c FROM Action__c where id  = 'a08q0000008ha98'");
        String actualValue = ((Action__c)actions2[0]).getStatus__c();
        assertEquals(expectedValue, actualValue);
        // test if it update correctly
    }
    @Test
    void ApplyButton_negativeTest() throws ConnectionException {
        Conn conn = Conn.getInstance();
        String query = "SELECT ID, Link_to_Record__c, Description__c, Action_Type__c, Action_Detail__c, Object__c, Subject__c, Priority__c FROM Action__c where id = 'a08q0000008ha98'";
        SObject[] actions = conn.query(query);
        Action__c a = (Action__c) actions[0];

        a.setStatus__c("Chang");
        actions[0] = a;
        SaveResult[] results = conn.update(actions);
        assert (results[0].isSuccess());
    }

}