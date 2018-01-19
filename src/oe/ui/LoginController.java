package oe.ui;

import com.sforce.soap.enterprise.fault.LoginFault;
import com.sforce.ws.ConnectionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import oe.sf.Conn;
import oe.util.Encryptor;
import oe.util.Preference;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.prefs.Preferences;

public class LoginController {
    //log
    private static Logger log = Logger.getLogger(LoginController.class.getName());
    @FXML
    private TextField username;
    @FXML
    private GridPane LoginWindow;
    @FXML
    private PasswordField password;
    @FXML
    private Button Login;
    @FXML
    private CheckBox saveID;
    @FXML
    private Label loginFailedLabel;
    @FXML
    private Label LoginFailedIfAllowLabel;
    private boolean isToUseSandbox;
    private Preferences userPref = Preference.getUserPref();
    private Preferences sysPref = Preference.getSysPref();
    public static Stage stage = new Stage();
    @FXML
    public void initialize() {

        LoginWindow.setPrefSize(500, 500);
        loginFailedLabel.setVisible(false);
        LoginFailedIfAllowLabel.setVisible(false);
        username.setText(userPref.get("username",""));
        if(!userPref.get("username","").isEmpty()){
            String pwd = userPref.get("password","");
            password.setText(pwd == null || pwd.isEmpty() ? "" : Encryptor.decrypt(pwd));
            saveID.setSelected(true);
        }

        handleUsernameChange("", username.getText()); //initialize login button text
        username.textProperty().addListener((observable, oldValue, newValue) -> handleUsernameChange(oldValue, newValue));
    }

    private void handleUsernameChange(String oldValue, String newValue) {
        if(newValue.contains(".com.")) // sandbox has format xxx.com.yyy
        {
            isToUseSandbox = true;
            Login.setText("Login to Sandbox");
            Login.setStyle("-fx-background-color: aquamarine");
        }else{
            Login.setText("Login to Production");
            Login.setStyle("-fx-background-color: coral");
            isToUseSandbox = false;
        }
    }

    /**
     * connect to Salesforce and saveID if saveID checkbox is selected after click login button
     * @param actionEvent
     * @throws IOException
     */
    public void handleLoginButtonAction(ActionEvent actionEvent) throws IOException {
        LoginFailedIfAllowLabel.setVisible(false);
        loginFailedLabel.setVisible(false);
        String un = username.getText();

        if (CheckIfcanUseTool()){
            try {
                Conn.createInstance(un, password.getText(), isToUseSandbox);
            } catch (ConnectionException e) {
                String msg = ((LoginFault) e).getExceptionMessage();
                log.warn(msg);
                loginFailedLabel.setText(msg);
                loginFailedLabel.setVisible(true);
                return;
            }

            //close LoginPage
            ((Node)actionEvent.getSource()).getScene().getWindow().hide();

            //load MainPane.fxml
            showMainPane();

            if (saveID.isSelected()) {
                userPref.put("username", un);
                userPref.put("password", Encryptor.encrypt( password.getText()));
            }
            else {
                userPref.put("username", "");
                userPref.put("password", "");
            }
        }else {
            LoginFailedIfAllowLabel.setVisible(true);
        }
    }

    //UiAction after click Cancel Button
    public void handleCancelButtonAction(ActionEvent actionEvent) throws IOException {
        //close LoginPage
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }

    private void showMainPane() throws IOException {

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        Parent root = FXMLLoader.load(getClass().getResource("MainPane.fxml"));
        Scene main_scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        stage.setTitle("SysAdminTool - " + username.getText() );
        stage.setScene(main_scene);
        stage.show();
    }

    private boolean CheckIfcanUseTool(){
        String wcut = sysPref.get(Preference.WhoCanUseTool,"charlton.zheng@openedgepay.com,thomas.garcia@openedgepay.com,rui.ma@openedgepay.com");

        String un = username.getText();
        un = un.substring(0, un.indexOf(".com")+ 4);
        boolean allowToUse = !un.isEmpty() && wcut.contains(un);
        if (!allowToUse) {
            log.warn(un + " is not allowed to user tool.");
        }
        return allowToUse;
    }


}
