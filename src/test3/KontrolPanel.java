package test3;
// JavaFX libs

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

//Hibernate libs
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

//Entities
import encje.*;

//util libs
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
//Joda-Time
import org.joda.time.LocalDate;
import static test3.Methods.checkConnection;


public class KontrolPanel {

    @FXML
    private ChoiceBox<Klient> clientPicker;
    @FXML
    private Button logOut, confirm, add, addClientButton, editButton;
    @FXML
    private ListView<Zlecenie> listView;
    @FXML
    private TextField adressField, dayField, monthField, yearField;
    @FXML
    private ChoiceBox<String> whichClient;

    @FXML
    void initialize() {

        Configuration conf = new Configuration().configure();
        SessionFactory factory = conf.buildSessionFactory();
        Session s = factory.openSession();
        List<Zlecenie> list = s.createCriteria(Zlecenie.class).list();
        listView.getItems().addAll(list);
        whichClient.getItems().addAll("Firmowy", "Prywatny");
        whichClient.getSelectionModel().selectFirst();
        urchoice();
        whichClient.setOnAction( e -> confirm.fire());
        confirm.setVisible(false);
    }

    @FXML
    void outlog(ActionEvent event) throws Exception {

         Parent application = FXMLLoader.load(getClass().getResource("styleMain.fxml"));
        Scene applicationScene = new Scene(application);
        applicationScene.getStylesheets().add("application.css");
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(applicationScene);
        window.show();
    }

    @FXML
    void urchoice() {                                                           //adding required items to list
        adressField.setText(null);
        dayField.setText(null);
        monthField.setText(null);
        yearField.setText(null);
        checkConnection();
        clientPicker.getItems().clear();
        Configuration conf = new Configuration().configure();
        SessionFactory factory = conf.buildSessionFactory();
        Session s = factory.openSession();
        if (whichClient.getValue().equals("Firmowy")) {
            List<KlientFirmowy> list = s.createCriteria(KlientFirmowy.class).list();
            clientPicker.getItems().addAll(list);

        } else if (whichClient.getValue().equals("Prywatny")) {
            List<KlientPrywatny> list = s.createCriteria(KlientPrywatny.class).list();
            clientPicker.getItems().addAll(list);
        }

    }

    @FXML
    void addOrder(ActionEvent event) {                         // add order - nothing more or less
        try {
            Configuration conf = new Configuration().configure();
            SessionFactory factory = conf.buildSessionFactory();
            Session s = factory.openSession();
            Transaction t = s.beginTransaction();
            Zlecenie nowe = new Zlecenie();
            if (adressField.getText() == null || clientPicker.getValue() == null) {
                Alert wrong = new Alert(Alert.AlertType.ERROR);
                wrong.setTitle("Błąd!");
                wrong.setHeaderText("Dane są niepoprawne!");
                wrong.setContentText("Któreś z wymaganych pól pozostało puste");
                DialogPane dialogPane = wrong.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("myDialogs.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialogs");
                wrong.showAndWait();
            } else {
                nowe.setAdres(adressField.getText());
                int rok = Integer.parseInt(yearField.getText());
                int msc = Integer.parseInt(monthField.getText());
                int dz = Integer.parseInt(dayField.getText()) + 1;
                LocalDate c = new LocalDate().withYear(rok).withMonthOfYear(msc).withDayOfMonth(dz);
                nowe.setData(c.toDate());
                nowe.setKlient(clientPicker.getValue());
                s.save(nowe);
                s.flush();
                s.refresh(nowe);
                t.commit();
                listView.getItems().add(nowe);
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
    void addClient(ActionEvent event) throws Exception {                                         //setting up new window without hiding "this" one
        Parent application = FXMLLoader.load(getClass().getResource("StyleNewClient.fxml"));
        Scene applicationScene = new Scene(application);
        applicationScene.getStylesheets().add("application.css");
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(applicationScene);
        window.show();
    }

    @FXML
    void openEdit(ActionEvent event) throws Exception {                                 //opening new Window with data edition, 
        Parent application = FXMLLoader.load(getClass().getResource("styleEdit.fxml"));
        Scene applicationScene = new Scene(application);
        applicationScene.getStylesheets().add("application.css");
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(applicationScene);
        window.show();
    }
}
