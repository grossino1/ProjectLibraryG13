/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author mello
 */
public class LoginViewController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button btnLogin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

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
