
package test3;

import javafx.scene.control.Alert;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;


public class Methods {
    static public  void checkConnection() {                                     //method throws exception, when Connection is lost
        try {

            Configuration conf = new Configuration().configure();
            SessionFactory factory = conf.buildSessionFactory();
            Session s = factory.openSession();
            Transaction t = s.beginTransaction();
            s.close();
        } catch (RuntimeException e) {
            Alert noConnection = new Alert(Alert.AlertType.WARNING);
            noConnection.setTitle("Błąd!");
            noConnection.setContentText("Brak połączenia z bazą!");
            noConnection.showAndWait();
            e.printStackTrace();
        }
    }
}
