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

    // Riferimento al modulo di logica (Model)
    // private Autenticazione autenticazioneService = new Autenticazione();

    /**
     * Metodo chiamato automaticamente da JavaFX quando si clicca il bottone "ACCEDI".
     * Deve essere collegato nel FXML tramite onAction="#handleLoginAction"
     */
    @FXML
    void handleLoginAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 1. Validazione Input (Controllo campi vuoti)
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
            apriDashboard(event);
        } else {
            mostraAlert(Alert.AlertType.ERROR, "Errore Login", "Username o password non corretti.");
        }
    }

    /**
     * Chiude la finestra di Login e apre la MainDashboard
     */
    private void apriDashboard(ActionEvent event) {
        try {
            // Carica la vista della Dashboard (assicurati che il percorso sia corretto)
            // Se la Dashboard è in un altro package, aggiusta il path, es: "/GUI/MainDashboard.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/MainDashboard.fxml")); 
            Parent root = loader.load();

            // Ottieni lo Stage (finestra) attuale dal bottone che è stato cliccato
            Stage stageAttuale = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Opzione A: Chiudi login e apri nuova finestra
            // stageAttuale.close();
            // Stage nuovoStage = new Stage();
            // nuovoStage.setScene(new Scene(root));
            // nuovoStage.setMaximized(true);
            // nuovoStage.show();

            // Opzione B (Consigliata): Cambia semplicemente la scena nella stessa finestra
            Scene dashboardScene = new Scene(root);
            stageAttuale.setScene(dashboardScene);
            stageAttuale.setMaximized(true); // Assicura che si apra a tutto schermo
            stageAttuale.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostraAlert(Alert.AlertType.ERROR, "Errore Critico", "Impossibile caricare la Dashboard: " + e.getMessage());
        }
    }

    // Metodo di utilità per mostrare popup
    private void mostraAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}