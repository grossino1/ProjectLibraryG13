package GUI.GUI_Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

// Assicurati di importare la tua classe di Autenticazione reale
// import gestione.Autenticazione; 

public class ControllerLogin {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button btnLogin;

    @FXML
    void handleLoginAction(ActionEvent event) {
        //gestore dell'evento quanto viene premuto il bottone login
        //scheletro
    }

    /**
     * Chiude la finestra di Login e apre la DashBoard del CatalogoLibri
     */
    private void switchScene(ActionEvent event) {
        //permette di cambiare scena
        //scheletro        
    }

        // Metodo pop-up di alert
    private void mostraAlert(Alert.AlertType type, String title, String content) {
        //scheletro
    }
}