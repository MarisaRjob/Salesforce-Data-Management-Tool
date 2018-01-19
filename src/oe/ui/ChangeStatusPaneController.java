package oe.ui;

import com.sforce.soap.enterprise.sobject.Action__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import oe.sf.Conn;
import oe.sf.Updater;
import oe.ui.sf.UiAction;

import java.io.IOException;

public class ChangeStatusPaneController {
    @FXML
    private Label CheckedActionItemCS;
    @FXML
    private ComboBox CsComboBox;
    public  static UiAction actionitem;

    @FXML
    public void initialize(UiAction action) {
        actionitem = action;
        CheckedActionItemCS.setText(action.getName().toString());
        CsComboBox.getItems().addAll(
                "New",
                "Open/Review",
                "In Process",
                "Cancelled",
                "Completed",
                "Deferred",
                "Duplicate",
                "Awaiting Response",
                "Sent to Development",
                "Deployment Pending",
                "Ready for User Sign-off",
                "User Signed Off");}
    public void handleApplyButtonAction(ActionEvent actionEvent) throws IOException, ConnectionException {
         String chosenStatus = CsComboBox.getValue().toString();
         Conn conn = Conn.getInstance();
         SObject[] checkedActionSObject = conn.query("SELECT ID, Link_to_Record__c, Description__c, Assigned_To__c, Action_Type__c, Action_Detail__c, Object__c, Subject__c, Priority__c FROM Action__c where id = '" + actionitem.getId() + "'");
         Action__c clickedAction = (Action__c) checkedActionSObject[0];
         clickedAction.setStatus__c(chosenStatus);

        checkedActionSObject[0] = clickedAction;
        Updater updater = new Updater(checkedActionSObject);
        updater.performOnSf();
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();

    }
    public void handleCancelButtonAction(ActionEvent actionEvent) throws IOException {
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }

}
