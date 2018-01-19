package oe.sf;

import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Action__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import oe.ui.sf.UiAction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class InserterTest {
    @BeforeEach
    void setUp() throws ConnectionException {
        Conn.createInstance("rui.ma@openedgepay.com.adminrm","RJkaiK0920Dashu!",true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_inserterPositive1() throws ConnectionException {
        Conn conn = Conn.getInstance();

        Action__c a1 = new Action__c();
        a1.setSubject__c("test subject");
        a1.setLink_to_Record__c("n/a");
        a1.setDescription__c("test description");
        a1.setAction_Type__c("Bug");
        a1.setAction_Detail__c("Missing Feature");
        a1.setPriority__c("High");
        a1.setObject__c("Account");

        Action__c a2 = new Action__c();
        a2.setSubject__c("test subject2");
        a2.setLink_to_Record__c("n/a");
        a2.setDescription__c("test description2");
        a2.setAction_Type__c("Maintenance");
        a2.setPriority__c("Med");
        a2.setObject__c("Cases");

        Action__c a3 = new Action__c();
        a3.setSubject__c("test subject3");
        a3.setLink_to_Record__c("n/a");
        a3.setDescription__c("test description2");
        a3.setAction_Type__c("Maintenance");
        a3.setPriority__c("Med");
        a3.setObject__c("Cases");
        Action__c[] forInsert = new Action__c[]{a1,a2,a3};

        Inserter inserter = new Inserter(forInsert);
        boolean r = inserter.performOnSf();

        assertTrue(r);
        StringBuffer Ids = new StringBuffer();
        Arrays.stream(inserter.getResults()).forEach(ir -> Ids.append("'").append(ir.getId()).append("',"));
        Ids.deleteCharAt(Ids.length()-1);
        SObject[] actual = conn.query("SELECT ID, Name, Link_to_Record__c, Description__c, Action_Type__c, Action_Detail__c, Object__c, Subject__c, Priority__c FROM Action__c where id in ($ids)".replace("$ids", Ids));
        assertEquals(actual.length, forInsert.length);
        SaveResult[] saveResults = inserter.results;
        String[] resultIDStrings = new String[saveResults.length];
        for(int index = 0; index < actual.length; index++){
            resultIDStrings[index] = saveResults[index].getId();
        }
        for(int index = 0; index < actual.length; index++){
            assertEquals(resultIDStrings[index],actual[index].getId());
        }
    }
    @Test
    void test_inserterNegative1() throws ConnectionException {
        Conn conn = Conn.getInstance();

        Action__c a1 = new Action__c();
        a1.setSubject__c("test subject");
        a1.setLink_to_Record__c(null);
        a1.setDescription__c("test description");
        a1.setAction_Type__c("Bug");
        a1.setAction_Detail__c("Missing Feature");
        a1.setPriority__c("High");
        a1.setObject__c("Account");

        Action__c a2 = new Action__c();
        a2.setSubject__c("test subject2");
        a2.setLink_to_Record__c("n/a");
        a2.setDescription__c("test description2");
        a2.setAction_Type__c("Maintenance");
        a2.setPriority__c("Med");
        a2.setObject__c("Cases");

        Action__c a3 = new Action__c();
        a3.setSubject__c("test subject3");
        a3.setLink_to_Record__c("n/a");
        a3.setDescription__c("test description2");
        a3.setAction_Type__c("Maintenance");
        a3.setPriority__c("Med");
        a3.setObject__c("Cases");
        Action__c[] forInsert = new Action__c[]{a1,a2,a3};

        Inserter inserter = new Inserter(forInsert);
        boolean r = inserter.performOnSf();

        assertFalse(r);

    }
    @Test
    void updateToSf() {
    }

    @Test
    void perform1() {
    }

    @Test
    void getResults() {
    }

    @Test
    void getRearrangedSobjs() {
    }

    @Test
    void isRearrangeNeeded() {
    }

    @Test
    void rearrangeOnSaveResults() {
    }

}