package test3;

import encje.Admin;

import java.util.List;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import static test3.Methods.checkConnection;


public class KontrolMain {

    @FXML
    private Button log, getlogged, back, logOut, openPanel;
    @FXML
    private TextField login, password;
    @FXML
    private Label loginLabel, passwordLabel;
    @FXML
    private DialogPane dialogMain;

    @FXML
    void initialize() {
        checkConnection();
        login.setVisible(false);
        password.setVisible(false);
        getlogged.setVisible(false);
        back.setVisible(false);
        logOut.setVisible(false);
        loginLabel.setVisible(false);
        passwordLabel.setVisible(false);
        back.setVisible(false);
        openPanel.setVisible(false);
    }

    @FXML                                                                       //changing a bit layout appearance
    void logg() {
        checkConnection();
        log.setVisible(false);
        login.setVisible(true);
        password.setVisible(true);
        getlogged.setVisible(true);
        loginLabel.setVisible(true);
        passwordLabel.setVisible(true);
        back.setVisible(true);

    }

    @FXML
    String getLogin() {

        String l = login.getText();
        return l;
    }

    @FXML
    String getPassword() {
        String p = password.getText();
        return p;
    }

    boolean check() {                                                           //checking if passes are right, here "checkConnection"           
        checkConnection();                                                      //method idea was born
        Configuration conf = new Configuration().configure();
        SessionFactory factory = conf.buildSessionFactory();
        Session s = factory.openSession();
        Transaction t = s.beginTransaction();

        List<Admin> list = s.createCriteria(Admin.class).list();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getLogin().equals(getLogin()) && list.get(i).getPassword().equals(getPassword())) {
                return true;
            }
        }

        return false;
    }

    @FXML
    void afterLogin() {
        checkConnection();
        if (check() == false) {
            Alert bad = new Alert(Alert.AlertType.WARNING);
            bad.setTitle("Błąd!");
            bad.setHeaderText("Logowanie nie powiodło się!");
            bad.setContentText("Podane dane są niepoprawne");
            DialogPane dialogPane = bad.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialogs");
            bad.showAndWait();
        } else {
            password.setVisible(false);
            login.setVisible(false);
            getlogged.setVisible(false);
            loginLabel.setVisible(false);
            passwordLabel.setVisible(false);
            logOut.setVisible(true);
            openPanel.setVisible(true);
            String logged = getLogin();
            login.setText("");
            password.setText("");
            Alert good = new Alert(AlertType.INFORMATION);
            good.setTitle("Gratulacje");
            good.setHeaderText("Logowanie powiodło się!");
            good.setContentText("Podane dane są prawidłowe, zostaniesz teraz przeniesiony dalej");
            DialogPane dialogPane = good.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialogs");
            good.showAndWait();
        }
    }

    @FXML
    void afterLoginKey(KeyEvent event) {                                        //setting some actions to make login process be more
        if (event.getCode() == KeyCode.ENTER) {                                 //friendly
            getlogged.fire();
        }
    }

    @FXML
    void outlog() {
        log.setVisible(true);
        logOut.setVisible(false);
        openPanel.setVisible(false);
    }

    @FXML
    void openPanelAction(ActionEvent event) throws Exception {                  //Creating new window, and closing "this" one
        checkConnection();
        Parent application = FXMLLoader.load(getClass().getResource("stylePanel.fxml"));
        Scene applicationScene = new Scene(application);
        applicationScene.getStylesheets().add("application.css");
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(applicationScene);
        window.show();

    }

    @FXML
    void backFromLogging() {
        log.setVisible(true);
        login.setVisible(false);
        password.setVisible(false);
        getlogged.setVisible(false);
        loginLabel.setVisible(false);
        passwordLabel.setVisible(false);
        back.setVisible(false);
        openPanel.setVisible(false);
    }

}
