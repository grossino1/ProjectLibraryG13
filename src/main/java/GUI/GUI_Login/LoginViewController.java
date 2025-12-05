/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private Button handleLogin;

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
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            mostraAlert(Alert.AlertType.WARNING, "Campi mancanti", "Per favore inserisci username e password.");
            return;
        }

        // 2. Verifica Credenziali (Simulazione connessione al Model)
        // boolean accessoConsentito = autenticazioneService.login(username, password);
        
        // --- SIMULAZIONE PER TEST (Rimuovi questo if/else quando colleghi il vero Model) ---
        boolean accessoConsentito = "admin".equals(username) && "1234".equals(password);
        // ----------------------------------------------------------------------------------

        if (accessoConsentito) {
            System.out.println("Login effettuato con successo!");
            switchScene(event, "/GUI/GUI_CatalogoLibri/CatalogoLibriView.fxml");
        } else {
            mostraAlert(Alert.AlertType.ERROR, "Errore Login", "Username o password non corretti.");
        }
    }

    /**
     * Chiude la finestra di Login e apre la DashBoard del CatalogoLibri
     */
    private void switchScene(ActionEvent event, String filePath) {
        //permette di cambiare scena
        //scheletro   
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
            Parent root = loader.load();
            
            Stage stageAttuale = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            Scene DashScene = new Scene(root);
            stageAttuale.setScene(DashScene);
            stageAttuale.setMaximized(true);
            stageAttuale.show();
            
        }catch(IOException e){
            e.printStackTrace();
            mostraAlert(Alert.AlertType.ERROR,"Errore Critico!","Errore nel caricamento della Scena: " + e.getMessage());
        }
        
    }
    
    // Metodo pop-up di alert
    private void mostraAlert(Alert.AlertType type, String title, String content) {
        //scheletro
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}