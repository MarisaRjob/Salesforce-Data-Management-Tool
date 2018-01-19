package oe.ui;


import com.sun.prism.impl.Disposer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import oe.sf.Conn;
import oe.ui.helper.ActionCheckBoxTableCell;
import oe.ui.helper.TableViewHelper;
import oe.ui.sf.UiAction;
import oe.util.Tuple;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;


public class MainPaneController {
    private static Logger log = Logger.getLogger(LoginController.class.getName());
    @FXML
    public TableView<UiAction> actionTableView;

    @FXML
    private BorderPane mainWindow;

    @FXML
    private  TabPane tabPane;
    @FXML
    protected ChoiceBox<String> DateFilter;
    @FXML
    private Tab MainActionItemsTab;
    @FXML
    private MenuItem closeItems;
    @FXML
    private MenuItem openItems;
   @FXML
   private Pagination mainPagination;


    @FXML
    private Button refreshButton;
    public static ObservableList<UiAction> data;
    private String  recordDateFilter;
    private final static int rowsPerPage = 5;
    @FXML
    public void initialize() {

        setMainWindowSize();
        mainPagination.setPageFactory(this::createPage);
        BuildActionTab();


    }
    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        actionTableView.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(actionTableView);
    }


    private void BuildActionTab(){

    DateFilter.getItems().add("This week");
    DateFilter.getItems().add("This month");
    DateFilter.getItems().add("This year");
    DateFilter.getItems().add("Last 2 years");

    DateFilter.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
        String query = "SELECT ID, Name, CreatedBy.id, CreatedBy.Name, Description__c, Assigned_To__c, Status__c, Deployed_to_Sandbox__c, Start_Date__c, Subject__c, CreatedDate, Priority__c FROM Action__c where ";
        switch (newValue.intValue()) {
            case 0:
                query += " CreatedDate >= THIS_WEEK ";
                recordDateFilter = " CreatedDate >= THIS_WEEK ";
                break;
            case 1:
                query += " CreatedDate >= THIS_MONTH ";
                recordDateFilter = " CreatedDate >= THIS_MONTH ";
                break;
            case 2:
                query += " CreatedDate >= THIS_YEAR ";
                recordDateFilter = " CreatedDate >= THIS_YEAR ";
                break;
            case 3:
                query += (" CreatedDate >= LAST_N_DAYS: " + 2*365);
                recordDateFilter = " CreatedDate >= LAST_N_DAYS: 730";
                break;
            default:
                query += " CreatedDate >= THIS_YEAR ";
                recordDateFilter = " CreatedDate >= THIS_YEAR ";
        }

        actionTableView.getColumns().clear();
        CreateTableView(query);
    });
    DateFilter.getSelectionModel().selectFirst();
}

    public void CreateTableView (String query){
         data = FXCollections.observableArrayList(UiAction.queryForActionTab(query));
         data.addListener((ListChangeListener.Change<? extends UiAction> u) ->{
            while(u.next()){
                if(u.wasUpdated()){
                    System.out.println("Updated:");
                    data.subList(u.getFrom(), u.getTo()).forEach(System.out::println);
                }
            }
        });
        Tuple[] columnDefinions = new Tuple[] {
                new Tuple<>("","checkbox"),
                new Tuple<>("Action", ""),
                new Tuple<>("",""),
                new Tuple<>("",""),
                new Tuple<>("",""),
                new Tuple<>("Name", "Name"),
                new Tuple<>("Created By", "CreatedBy"),
                new Tuple<>("Subject", "Subject__c"),
                new Tuple<>("Status", "Status__c"),
                new Tuple<>("Body", "Body"),
                new Tuple<>("Assigned to", "Assign_To__c"),
                new Tuple<>("Deployed to", "Deployed_to_Sandbox__c"),
                new Tuple<>("Start Date", "Start_Date__c"),
                new Tuple<>("Created Date", "CreatedDate"),
                new Tuple<>("Priority", "Priority__c"),
                new Tuple<>("Attachment", "AttachmentAsNumber"),
        };

        Hashtable<Integer, Callback> cellDefinitions = new Hashtable<>();
        cellDefinitions.put(0, p -> new ActionCheckBoxTableCell());

        Hashtable<Integer,Callback> columnSpecialDefinions = new Hashtable<>();
        columnSpecialDefinions.put(1, (Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>) p -> new SimpleBooleanProperty(p.getValue() != null));
        Hashtable<Integer, Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>> cellfactoryDefinitions = new Hashtable<>();
        cellfactoryDefinitions.put(1, p -> new ViewButtonCell("V"));
        columnSpecialDefinions.put(2, (Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>) p -> new SimpleBooleanProperty(p.getValue() != null));
        cellfactoryDefinitions.put(2, p -> new AssignButtonCell("A"));
        columnSpecialDefinions.put(3, (Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>) p -> new SimpleBooleanProperty(p.getValue() != null));
        cellfactoryDefinitions.put(3, p -> new ChangeStatusButtonCell("CS"));
        columnSpecialDefinions.put(4, (Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>) p -> new SimpleBooleanProperty(p.getValue() != null));
        cellfactoryDefinitions.put(4, p -> new AutoCompleteButtonCell("AC"));
        TableViewHelper.initTableView(actionTableView, data, TableViewHelper.defineColumns(actionTableView,columnDefinions,  cellDefinitions, columnSpecialDefinions, cellfactoryDefinitions));
        actionTableView.setEditable(true);
    }

    private class ViewButtonCell extends TableCell<Disposer.Record, Boolean> {

        final Button cellButton ;

        ViewButtonCell(String name) {
            cellButton = new Button(name);
            cellButton.setOnAction((ActionEvent t) -> {
                UiAction currentUiAction = (UiAction) ViewButtonCell.this.getTableView().getItems().get(ViewButtonCell.this.getIndex());
                String url = Conn.getInstance().getUrl();
                url += currentUiAction.getId();
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e1) {
                    log.error(e1);
                }
            });

            Tooltip tt = new Tooltip();
            tt.setText("View Action Item(s)");
            tt.setStyle("-fx-font: normal bold 14 Langdon; "
                    + "-fx-base: #AE3522; "
                    + "-fx-text-fill: orange;");

            cellButton.setTooltip(tt);}
        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }}

    }

    private class AssignButtonCell extends TableCell<Disposer.Record, Boolean> {

        final Button cellButton ;

        AssignButtonCell(String name) {
            cellButton = new Button(name);
            cellButton.setOnAction((ActionEvent t) -> {
                UiAction currentUiAction = (UiAction) AssignButtonCell.this.getTableView().getItems().get(AssignButtonCell.this.getIndex());
                try {
                    showAssignPane(LoginController.stage,currentUiAction);
                } catch (IOException e) {
                    log.error(e);
                }
            });

            Tooltip tt = new Tooltip();
            tt.setText("Assign Action Item(s)");
            tt.setStyle("-fx-font: normal bold 14 Langdon; "
                    + "-fx-base: #AE3522; "
                    + "-fx-text-fill: orange;");

            cellButton.setTooltip(tt);}
        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }}

    }
    private class ChangeStatusButtonCell extends TableCell<Disposer.Record, Boolean> {

        final Button cellButton ;

        ChangeStatusButtonCell(String name) {
            cellButton = new Button(name);
            cellButton.setOnAction((ActionEvent t) -> {
                try {
                    UiAction currentUiAction = (UiAction) ChangeStatusButtonCell.this.getTableView().getItems().get(ChangeStatusButtonCell.this.getIndex());
                    showChangeStatusPane(currentUiAction);
                } catch (IOException e) {
                    log.error(e);
                }
            });

            Tooltip tt = new Tooltip();
            tt.setText("Change Status of Item(s)");
            tt.setStyle("-fx-font: normal bold 14 Langdon; "
                    + "-fx-base: #AE3522; "
                    + "-fx-text-fill: orange;");
            cellButton.setTooltip(tt);}
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }}

    }
    private class AutoCompleteButtonCell extends TableCell<Disposer.Record, Boolean> {

        final Button cellButton ;

        AutoCompleteButtonCell(String name) {
            cellButton = new Button(name);
            cellButton.setOnAction((ActionEvent t) -> {

            });

            Tooltip tt = new Tooltip();
            tt.setText("Auto Complete Item(s)");
            tt.setStyle("-fx-font: normal bold 14 Langdon; "
                    + "-fx-base: #AE3522; "
                    + "-fx-text-fill: orange;");

            cellButton.setTooltip(tt);}
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }}

    }
    private void showAssignPane( Stage mainStage,UiAction currentUiAction) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "AssignPane.fxml"
                )
        );
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
        stage.setTitle("Assign To");
        stage.setScene(
                new Scene(loader.load())
        );
        AssignDialogPaneController controller =
                loader.getController();
        controller.initialize(currentUiAction );
        stage.show();

    }
    private void showChangeStatusPane( UiAction currentUiAction) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "ChangeStatusPane.fxml"
                )
        );
        Stage stage = new Stage();
        stage.setTitle("Change Status");
        stage.setScene(
                new Scene(loader.load())
        );
        ChangeStatusPaneController controller =
                loader.getController();
        controller.initialize(currentUiAction);
        stage.show();

    }
    private void setMainWindowSize() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        mainWindow.setPrefSize(2024, 2024);
    }
    public void handleCloseItemsAction(ActionEvent actionEvent) throws IOException {
        closeTabFromMenu(MainActionItemsTab);
    }
    public void handleOpenItemsAction(ActionEvent actionEvent) throws IOException {
        tabPane.getTabs().add(0,MainActionItemsTab);
    }
    private static void closeTabFromMenu(Tab tab) {
        EventHandler<Event> handler = tab.getOnClosed();
        if (null != handler) {
            handler.handle(null);
        } else {
            tab.getTabPane().getTabs().remove(tab);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) throws IOException{
            actionTableView.getColumns().clear();
            CreateTableView("SELECT ID, Name, CreatedBy.id, CreatedBy.Name, Description__c, Assigned_To__c, Status__c, Deployed_to_Sandbox__c, Start_Date__c, Subject__c, CreatedDate, Priority__c FROM Action__c where "+recordDateFilter);
    }







}


