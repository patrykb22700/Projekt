package test3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import encje.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.joda.time.LocalDate;
import static test3.Methods.checkConnection;


public class AddClientControl {

    @FXML
    private ChoiceBox<String> clientTypeChoice;

    @FXML
    private Label dateOrCompanyLabel, slash, slash1, nipLabel;
    @FXML
    private TextField nameField, dayF, yearF, monthF, dateOrCompanyField, nipField, surnameField;
    @FXML
    private Button confirmButton, addButton, backButton, logoutButton;

    @FXML
    void initialize() {
        checkConnection();
        clientTypeChoice.getItems().addAll("Prywatny", "Firmowy");
        nipField.setText(null);
        dateOrCompanyField.setText(null);
        nameField.setText(null);
        surnameField.setText(null);
        clientTypeChoice.getSelectionModel().selectLast();
        dayF.setVisible(false);
        monthF.setVisible(false);
        yearF.setVisible(false);
        slash.setVisible(false);
        slash1.setVisible(false);
        clientTypeChoice.setOnAction(e -> {
            confirmButton.fire();
        });
        confirmButton.setVisible(false);

    }

    void showPrivate() {                                                        //showing required fields for Private Clients
        checkConnection();
        dayF.setVisible(true);
        monthF.setVisible(true);
        yearF.setVisible(true);
        slash.setVisible(true);
        slash1.setVisible(true);
        nipLabel.setVisible(false);
        nipField.setVisible(false);
        dateOrCompanyField.setVisible(false);
    }

    void showCompany() {                                                        //showing required fields for Company Clients
        checkConnection();
        dayF.setVisible(false);
        monthF.setVisible(false);
        yearF.setVisible(false);
        slash.setVisible(false);
        slash1.setVisible(false);
        nipLabel.setVisible(true);
        nipField.setVisible(true);
        dateOrCompanyField.setVisible(true);
    }

    @FXML
    void whichClient() {                                                        //method that sets positioning for controls, and uses
        checkConnection();                                                      // 2 methods above
        if (clientTypeChoice.getValue().equals("Firmowy")) {
            dateOrCompanyLabel.setText("Nazwa Firmy");
            showCompany();
        } else if (clientTypeChoice.getValue().equals("Prywatny")) {
            dateOrCompanyLabel.setText("Data urodzenia");
            showPrivate();
        }

    }

    @FXML
    void clientAdd(ActionEvent event) throws Exception {                                         //adding client to database
        try {                                                                   //found some problems with choosing exception type
            checkConnection();
            Configuration conf = new Configuration().configure();
            SessionFactory factory = conf.buildSessionFactory();
            Session s = factory.openSession();
            Transaction t = s.beginTransaction();
            if (clientTypeChoice.getValue().equals("Firmowy")) {
                if (nameField.getText() == null || surnameField.getText() == null || dateOrCompanyField.getText() == null
                        || nipField.getText() == null) {
                    Alert wrong = new Alert(AlertType.ERROR);
                    wrong.setTitle("Błąd!");
                    wrong.setHeaderText("Dane są niepoprawne!");
                    wrong.setContentText("Któreś z wymaganych pól pozostało puste");
                    DialogPane dialogPane = wrong.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialogs");
                    wrong.showAndWait();

                } else {

                    KlientFirmowy nowy = new KlientFirmowy();
                    nowy.setImie(nameField.getText());
                    nowy.setNazwisko(surnameField.getText());
                    nowy.setNazwa_firmy(dateOrCompanyField.getText());
                    nowy.setNIP(nipField.getText());
                    s.persist(nowy);
                    t.commit();
                    Alert done = new Alert(AlertType.INFORMATION);
                    done.setTitle("Okej!");
                    done.setHeaderText("Dane są poprawne!");
                    done.setContentText("Użytkownik " + dateOrCompanyField.getText() + " został dodany!");
                    DialogPane dialogPane = done.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialogs");
                    done.showAndWait();
                    Parent application = FXMLLoader.load(getClass().getResource("StylePanel.fxml"));
                    Scene applicationScene = new Scene(application);
                    applicationScene.getStylesheets().add("application.css");
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(applicationScene);
                    window.show();

                }
            } else if (clientTypeChoice.getValue().equals("Prywatny")) {
                KlientPrywatny nowy = new KlientPrywatny();
                nowy.setImie(nameField.getText());
                nowy.setNazwisko(surnameField.getText());
                int rok = Integer.parseInt(yearF.getText());
                int msc = Integer.parseInt(monthF.getText());
                int dz = Integer.parseInt(dayF.getText()) + 1;
                LocalDate c = new LocalDate().withYear(rok).withMonthOfYear(msc).withDayOfMonth(dz);
                nowy.setDataUrodzenia(c.toDate());
                s.persist(nowy);
                t.commit();
                if (nameField.getText() == null || surnameField.getText() == null || dayF.getText() == null
                        || monthF.getText() == null || yearF.getText() == null) {
                    Alert wrong = new Alert(AlertType.ERROR);
                    wrong.setTitle("Błąd!");
                    wrong.setHeaderText("Dane są niepoprawne!");
                    wrong.setContentText("Któreś z wymaganych pól pozostało puste");
                    DialogPane dialogPane = wrong.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialogs");
                    wrong.showAndWait();
                } else {
                    Alert done = new Alert(AlertType.INFORMATION);
                    done.setTitle("Okej!");
                    done.setHeaderText("Dane są poprawne!");
                    done.setContentText("Użytkownik " + nameField.getText() + " " + surnameField.getText() + " został dodany!");
                    DialogPane dialogPane = done.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialogs");
                    done.showAndWait();
                    Parent application = FXMLLoader.load(getClass().getResource("StylePanel.fxml"));
                    Scene applicationScene = new Scene(application);
                    applicationScene.getStylesheets().add("application.css");
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(applicationScene);
                    window.show();
                }
            }
        } catch (NumberFormatException e) {
            Alert wrong = new Alert(Alert.AlertType.ERROR);
            wrong.setTitle("Błąd!");
            wrong.setHeaderText("Dane są niepoprawne!");
            wrong.setContentText("Podana jest niepoprawna data!");
            DialogPane dialogPane = wrong.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialogs");
            wrong.showAndWait();
        }
    }

    @FXML
    void goBack(ActionEvent event) throws Exception {
        Parent application = FXMLLoader.load(getClass().getResource("stylePanel.fxml"));
        Scene applicationScene = new Scene(application);
        applicationScene.getStylesheets().add("application.css");
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(applicationScene);
        window.show();
    }

    @FXML
    void logOut(ActionEvent event) throws Exception {
         Parent application = FXMLLoader.load(getClass().getResource("styleMain.fxml"));
        Scene applicationScene = new Scene(application);
        applicationScene.getStylesheets().add("application.css");
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(applicationScene);
        window.show();
    }

    @FXML
    void addClientKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {                                 //friendly
            addButton.fire();
        }

    }
}
