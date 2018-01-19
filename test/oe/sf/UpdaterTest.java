package oe.sf;

import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Action__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by rui.ma on 11/2/2017.
 */
class UpdaterTest {

    private static Logger log = Logger.getLogger(Updater.class.getName());
    private SObject[] action;

    @BeforeAll
    static void initAll() {

    }
    @BeforeEach
    void setup() throws ConnectionException {
        Conn.createInstance("rui.ma@openedgepay.com.adminrm","RJkaiK0920Dashu#",true);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_updaterPositive() throws ConnectionException {
        Conn conn = Conn.getInstance();
        SObject[] IdSObject = conn.query("SELECT  ID, Link_to_Record__c, Description__c, Action_Type__c, Action_Detail__c, Object__c, Subject__c, Priority__c FROM Action__c where id = 'a08q0000008ha98'");
        Action__c action__c0 =(Action__c) IdSObject[0];

        action__c0.setDescription__c("updateTestDescription");
        IdSObject[0] = action__c0;
        Updater updater = new Updater(IdSObject);

        boolean r = updater.performOnSf();
        assertTrue(r);
        // test if it update succeeded or not

        SObject[] afterUpSobjects = conn.query("SELECT ID, Description__c FROM Action__c Where ID = '"+IdSObject[0].getId()+"'");
        Action__c afterAction__c = (Action__c) afterUpSobjects[0];
        assertEquals(action__c0.getDescription__c(),afterAction__c.getDescription__c());

        SaveResult[] results = conn.update(IdSObject);

        assert(results[0].isSuccess());
        // test if the updated data on the salesforce is equal to the data used to update.
    }

    @Test
    void test_updaterNegtive() throws ConnectionException{
        Conn conn = Conn.getInstance();
        SObject[] IdSObject = conn.query("SELECT  ID, Link_to_Record__c, Description__c, Action_Type__c, Action_Detail__c, Object__c, Subject__c, Priority__c FROM Action__c where id = 'a08q0000008ha98'");
        Action__c action__c0 =(Action__c) IdSObject[0];

        action__c0.setPriority__c("top");
        action__c0.setAction_Type__c("updatenegtest");
        IdSObject[0] = action__c0;
        Updater updater = new Updater(IdSObject);
        boolean r = updater.performOnSf();
        assertTrue(r);
        // test if it assert false after updated wrong data
    }
    @Test
    void test_upserterPositive() throws ConnectionException{
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

        SObject[] forInserterSObject = {a1,a2};
        Inserter inserter1 = new Inserter(forInserterSObject);
        boolean m = inserter1.performOnSf();
        assertTrue(m);
        a1.setDescription__c("testUpserter");
        a2.setSubject__c("upserterObject2");
        Updater updater = new Updater(forInserterSObject);
        boolean r = updater.performOnSf();
        assertTrue(r);

    }
    @Test
    void getResults() {
    }

    @Test
    void getRearrangedSobjs() {
    }

}