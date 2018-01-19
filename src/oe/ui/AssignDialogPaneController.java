package oe.ui;

import com.sforce.soap.enterprise.sobject.Action__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.User;
import com.sforce.ws.ConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import jdk.nashorn.internal.lookup.Lookup;
import oe.sf.Conn;
import oe.sf.Inserter;
import oe.sf.Updater;
import oe.sf.Upserter;
import oe.ui.sf.UiAction;
import sun.applet.Main;

import java.io.IOException;

public class AssignDialogPaneController{
    @FXML
    private ComboBox assignComboBox;
    @FXML
    private Label CheckedActionItem;

    public  static UiAction actionitem;

    @FXML
    public void initialize(UiAction action) {

        actionitem = action;

        CheckedActionItem.setText(action.getName().toString());
        String[]  adminNames = getAdminName();
        for(int count = 0; count < adminNames.length; count ++){
            assignComboBox.getItems().add(adminNames[count]);
        }
    }

    private String[] getAdminName(){
        String query = "select id, name from user where profile.name = 'APT Sys Admin'";
        Conn conn = Conn.getInstance();
        SObject[] admins = new SObject[0];
        try {
            admins = conn.query(query);
        } catch (ConnectionException e) {
            e.printStackTrace();
            //todo: display error to user
        }
        String[] adminNames = new String[admins.length];
        for(int count = 0; count < admins.length; count++){
            adminNames[count] = ((User) admins[count]).getName();
        }
        return adminNames;
    }

    public void handleApplyButtonAction(ActionEvent actionEvent) throws IOException, ConnectionException {
        String chosenAdmin = assignComboBox.getValue().toString();
        Conn conn = Conn.getInstance();
        SObject[] checkedActionSObject = conn.query("SELECT ID, Link_to_Record__c, Description__c, Assigned_To__c, Action_Type__c, Action_Detail__c, Object__c, Subject__c, Priority__c FROM Action__c where id = '" + actionitem.getId() + "'");
        Action__c clickedAction = (Action__c) checkedActionSObject[0];

        SObject[] AdminsSObject = conn.query("select id, name from user where name = '" + chosenAdmin+"'");
        String assigntoID = AdminsSObject[0].getId();
        clickedAction.setAssigned_To__c(assigntoID);

        checkedActionSObject[0] = clickedAction;
        Updater updater = new Updater(checkedActionSObject);
        updater.performOnSf();
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();

    }
    public void handleCancelButtonAction(ActionEvent actionEvent) throws IOException {
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }
}
