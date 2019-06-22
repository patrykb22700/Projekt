
package test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.hibernate.*;
import encje.*;
import java.util.*;
import java.util.function.UnaryOperator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.hibernate.cfg.Configuration;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import static test3.Methods.checkConnection;


public class EditControl {

    @FXML
    private Button backButton, pickClient, saveClientButton, pickOrder, confirmClient,
            deleteClientButton, deleteOrderButton, saveOrderButton;
    @FXML
    private ChoiceBox<Zlecenie> orderEditChoice;
    @FXML
    private ChoiceBox<String> clientTypeChoice;
    @FXML
    private Label orderEditLabel, clientEditLabel, sliceClient, slashClient,
            slashClient1, nameLabel, surnameLabel, companyOrDate, nipLabel;
    @FXML
    private TextField dayOrderField, companyNameField, monthOrderField,
            yearOrderField, dayClientField, monthClientField, yearClientField, nipField,
            surnameField, nameField, orderAdress, orderClient;
    @FXML
    private ChoiceBox<KlientFirmowy> whichClientC;
    @FXML
    private ChoiceBox<KlientPrywatny> whichClientP;
    @FXML
    private ChoiceBox<Klient> choiceClientInOrder;


    @FXML
    void initialize() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*") || text.matches(("-*"))) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        nipField.setTextFormatter(textFormatter);
        Configuration conf = new Configuration().configure();
        SessionFactory factory = conf.buildSessionFactory();
        Session s = factory.openSession();
        List<Zlecenie> orderList = s.createCriteria(Zlecenie.class).list();
        orderEditChoice.getItems().addAll(orderList);
        List<KlientFirmowy> clientlist = s.createCriteria(KlientFirmowy.class).list();
        whichClientC.getItems().addAll(clientlist);
        clientTypeChoice.getItems().addAll("Firmowy", "Prywatny");
        clientTypeChoice.getSelectionModel().selectFirst();
        whichClientC.getSelectionModel().selectFirst();
        dayClientField.setVisible(false);
        monthClientField.setVisible(false);
        yearClientField.setVisible(false);
        slashClient.setVisible(false);
        slashClient1.setVisible(false);
        whichClientP.setVisible(false);
        confirmClient.fire();
        orderEditChoice.getSelectionModel().selectFirst();
        pickOrder.fire();
        orderEditChoice.setOnAction(e -> {
            pickOrder.fire();
        });
        pickOrder.setVisible(false);
        clientTypeChoice.setOnAction(e -> {
            pickClient.fire();
        });
        pickClient.setVisible(false);
        whichClientC.setOnAction(e -> {
           confirmClient.fire();
        });
        confirmClient.setVisible(false);
        whichClientP.setOnAction(e -> {
            confirmClient.fire();
        });

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
    void clientChoice() {                                                       //set client type up and fills all fields with data
        whichClientC.getItems().clear();
        whichClientP.getItems().clear();
        Configuration conf = new Configuration().configure();
        SessionFactory factory = conf.buildSessionFactory();
        Session s = factory.openSession();
        nameField.setText(null);
        surnameField.setText(null);
        dayClientField.setText(null);
        monthClientField.setText(null);
        yearClientField.setText(null);
        nipField.setText(null);
        companyNameField.setText(null);
        if (clientTypeChoice.getValue().equals("Firmowy")) {
            whichClientC.setVisible(true);
            whichClientP.setVisible(false);
            dayClientField.setVisible(false);
            monthClientField.setVisible(false);
            yearClientField.setVisible(false);
            slashClient.setVisible(false);
            slashClient1.setVisible(false);
            nipField.setVisible(true);
            companyNameField.setVisible(true);
            nipLabel.setVisible(true);
            companyOrDate.setText("Nazwa Firmy");
            List<KlientFirmowy> clientlist = s.createCriteria(KlientFirmowy.class).list();
            whichClientC.getItems().addAll(clientlist);
            whichClientC.getSelectionModel().selectFirst();
            confirmClient.fire();
        } else if (clientTypeChoice.getValue().equals("Prywatny")) {
            whichClientP.setVisible(true);
            whichClientC.setVisible(false);
            dayClientField.setVisible(true);
            monthClientField.setVisible(true);
            yearClientField.setVisible(true);
            slashClient.setVisible(true);
            slashClient1.setVisible(true);
            nipField.setVisible(false);
            nipLabel.setVisible(false);
            companyNameField.setVisible(false);
            companyOrDate.setText("Data urodzenia");
            companyOrDate.setLayoutX(companyOrDate.getLayoutX()-15);
            List<KlientPrywatny> clientlist = s.createCriteria(KlientPrywatny.class).list();
            whichClientP.getItems().addAll(clientlist);
            whichClientP.getSelectionModel().selectFirst();
            confirmClient.fire();

        }
    }

    @FXML
    void pickedOrder() {                                                        //fills all fields with data, and set them to defaults
        choiceClientInOrder.getItems().clear();
        Configuration conf = new Configuration().configure();
        SessionFactory factory = conf.buildSessionFactory();
        Session s = factory.openSession();
        Transaction t = s.beginTransaction();
        LocalDate p = convertUtilDateToLocalDate(orderEditChoice.getValue().getData());
        dayOrderField.setText(Integer.toString(p.getDayOfMonth()));
        monthOrderField.setText(Integer.toString(p.getMonthOfYear()));
        yearOrderField.setText(Integer.toString(p.getYear()));
        orderAdress.setText(orderEditChoice.getValue().getAdres());
        List<Klient> lista = s.createCriteria(Klient.class).list();
        choiceClientInOrder.getItems().addAll(lista);

        int id = orderEditChoice.getValue().getKlient().getId();
        for (int i = 0; i < lista.size(); i++) {
            if (id == lista.get(i).getId()) {
                choiceClientInOrder.getSelectionModel().select(lista.get(i));
            }
        }

    }

    public LocalDate convertUtilDateToLocalDate(Date date) {                    //method, that converts  "java.util.Date" to 
        if (date == null) {                                                     //LocalDate, which's more friendly to format
            return null;
        }
        DateTime dt = new DateTime(date);
        return dt.toLocalDate();
    }

    @FXML
    void pickedClient() {
        try {                                                                   //fill fields with data
            checkConnection();
            if (clientTypeChoice.getValue().equals("Prywatny")) {
                nameField.setText(whichClientP.getValue().getImie());
                surnameField.setText(whichClientP.getValue().getNazwisko());
                LocalDate p = convertUtilDateToLocalDate(whichClientP.getValue().getDataUrodzenia());
                dayClientField.setText(Integer.toString(p.getDayOfMonth()));
                monthClientField.setText(Integer.toString(p.getMonthOfYear()));
                yearClientField.setText(Integer.toString(p.getYear()));
            } else if (clientTypeChoice.getValue().equals("Firmowy")) {
                nameField.setText(whichClientC.getValue().getImie());
                surnameField.setText(whichClientC.getValue().getNazwisko());
                companyNameField.setText(whichClientC.getValue().getNazwa_firmy());
                nipField.setText(whichClientC.getValue().getNIP());
            }
        } catch (NullPointerException e) {
            dayOrderField.setText(null);
            companyNameField.setText(null);
            monthOrderField.setText(null);
            yearOrderField.setText(null);
            dayClientField.setText(null);
            monthClientField.setText(null);
            yearClientField.setText(null);
            nipField.setText(null);
            surnameField.setText(null);
            nameField.setText(null);

        }
    }

    @FXML
    void deleteClient() {
        try {                                                                   //deleting clients using HQL, fastest solution I found
            checkConnection();
            Configuration conf = new Configuration().configure();
            SessionFactory factory = conf.buildSessionFactory();
            Session s = factory.openSession();
            Transaction t = s.beginTransaction();
            if (clientTypeChoice.getValue().equals("Firmowy")) {
                int id = whichClientC.getValue().getId();
                String hql = "delete from Klient where id= :id";
                String hql2 = "delete from Zamowienie where klient= :id";
                Query query = s.createQuery(hql);
                Query q = s.createQuery(hql2);
                query.setInteger("id", id);
                q.setInteger("id", id);
                query.executeUpdate();
                q.executeUpdate();
                s.flush();
                t.commit();
                whichClientC.getItems().clear();
                List<KlientFirmowy> refreshed = s.createCriteria(KlientFirmowy.class).list();
                whichClientC.getItems().addAll(refreshed);
                whichClientC.getSelectionModel().selectFirst();
                confirmClient.fire();
                orderEditChoice.getSelectionModel().selectFirst();
                pickOrder.fire();
                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("Zrobione!");
                done.setHeaderText("Akcja zakończona sukcesem");
                done.setContentText("Dane zostały zapisane");
                DialogPane dialogPane = done.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("myDialogs.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialogs");
                done.showAndWait();

            } else if (clientTypeChoice.getValue().equals("Prywatny")) {
                int id = whichClientP.getValue().getId();
                String hql = "delete from Klient where id= :id";
                String hql2 = "delete from Zamowienie where klient= :id";
                Query query = s.createQuery(hql);
                Query q = s.createQuery(hql2);
                query.setInteger("id", id);
                q.setInteger("id", id);
                s.flush();
                query.executeUpdate();
                q.executeUpdate();
                s.flush();
                whichClientP.getItems().clear();
                List<KlientPrywatny> refreshed = s.createCriteria(KlientPrywatny.class).list();
                whichClientP.getItems().addAll(refreshed);
                whichClientP.getSelectionModel().selectFirst();
                confirmClient.fire();
                orderEditChoice.getSelectionModel().selectFirst();
                pickOrder.fire();
                t.commit();
                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("Zrobione!");
                done.setHeaderText("Akcja zakończona sukcesem");
                done.setContentText("Dane zostały zapisane");
                DialogPane dialogPane = done.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("myDialogs.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialogs");
                done.showAndWait();
            }
        } catch (Exception e) {
            Alert wrong = new Alert(Alert.AlertType.ERROR);
            wrong.setTitle("Błąd!");
            wrong.setHeaderText("Coś poszło nie tak");
            wrong.setContentText("Zwróć się do programisty");
            DialogPane dialogPane = wrong.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialogs");
            wrong.showAndWait();
        }
    }

    @FXML
    void saveClient() {
        try {
            Alert done = new Alert(Alert.AlertType.INFORMATION);
            done.setTitle("Zrobione!");
            done.setHeaderText("Akcja zakończona sukcesem");
            done.setContentText("Dane zostały zapisane");
            DialogPane dialogPane = done.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialogs");
            done.showAndWait();                                             //Action that saves any updates from fields in app
            checkConnection();                                                  //This time, I used session methods        
            if (clientTypeChoice.getValue().equals("Firmowy")) {
                KlientFirmowy updated = new KlientFirmowy();
                Configuration conf = new Configuration().configure();
                SessionFactory factory = conf.buildSessionFactory();
                Session s = factory.openSession();
                Transaction t = s.beginTransaction();
                KlientFirmowy update = (KlientFirmowy) s.get(KlientFirmowy.class, whichClientC.getValue().getId());
                update.setImie(nameField.getText());
                update.setNazwisko(surnameField.getText());
                update.setNazwa_firmy(companyNameField.getText());
                update.setNIP(nipField.getText());
                s.update(update);
                s.flush();
                t.commit();
                pickClient.fire();
                orderEditChoice.getSelectionModel().selectFirst();
                pickOrder.fire();
                done.showAndWait();
            } else if (clientTypeChoice.getValue().equals("Prywatny")) {
                Configuration conf = new Configuration().configure();
                SessionFactory factory = conf.buildSessionFactory();
                Session s = factory.openSession();
                Transaction t = s.beginTransaction();
                KlientPrywatny updated = (KlientPrywatny) s.get(KlientPrywatny.class, whichClientP.getValue().getId());
                updated.setImie(nameField.getText());
                updated.setNazwisko(surnameField.getText());
                int rok = Integer.parseInt(yearClientField.getText());
                int msc = Integer.parseInt(monthClientField.getText());
                int dz = Integer.parseInt(dayClientField.getText()) + 1;
                LocalDate c = new LocalDate().withYear(rok).withMonthOfYear(msc).withDayOfMonth(dz);
                updated.setDataUrodzenia(c.toDate());
                s.update(updated);
                s.flush();
                t.commit();
                s.close();
                pickClient.fire();
                orderEditChoice.getSelectionModel().selectFirst();
                pickOrder.fire();

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
    void deleteOrder() {                                                        //deleting orders once again with HQL                          
        checkConnection();
        Configuration conf = new Configuration().configure();
        SessionFactory factory = conf.buildSessionFactory();
        Session s = factory.openSession();
        Transaction t = s.beginTransaction();
        int id = orderEditChoice.getValue().getId();
        String hql = "delete from Zamowienie where id= :id";
        Query query = s.createQuery(hql);
        query.setInteger("id", id);
        query.executeUpdate();
        s.flush();
        t.commit();
        orderEditChoice.getItems().clear();
        List<Zlecenie> list = s.createCriteria(Zlecenie.class).list();
        orderEditChoice.getItems().addAll(list);
        orderEditChoice.getSelectionModel().selectFirst();
        pickOrder.fire();

    }

    @FXML
    void saveOrder() {
                                                                //saving updates using session methods
        try {
            checkConnection();
            Zlecenie updated = new Zlecenie();
            Configuration conf = new Configuration().configure();
            SessionFactory factory = conf.buildSessionFactory();
            Session s = factory.openSession();
            Transaction t = s.beginTransaction();
            Zlecenie update = (Zlecenie) s.get(Zlecenie.class, orderEditChoice.getValue().getId());
            update.setAdres(orderAdress.getText());
            int rok = Integer.parseInt(yearOrderField.getText());
            int msc = Integer.parseInt(monthOrderField.getText());
            int dz = Integer.parseInt(dayOrderField.getText()) + 1;
            LocalDate c = new LocalDate().withYear(rok).withMonthOfYear(msc).withDayOfMonth(dz);
            update.setData(c.toDate());
            update.setKlient(choiceClientInOrder.getValue());
            s.update(update);
            s.flush();
            t.commit();
            pickClient.fire();
            Alert done = new Alert(Alert.AlertType.INFORMATION);
            done.setTitle("Zrobione!");
            done.setHeaderText("Akcja zakończona sukcesem");
            done.setContentText("Dane zostały zapisane");
            DialogPane dialogPane = done.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialogs");
            done.showAndWait();
        }
        catch(NumberFormatException e){
            Alert bad = new Alert(Alert.AlertType.ERROR);
            bad.setTitle("Błąd!");
            bad.setHeaderText("Akcja zakończona niepowodzeniem");
            bad.setContentText("Zły format daty");
            DialogPane dialogPane = bad.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialogs");
            bad.showAndWait();
        }

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

}
