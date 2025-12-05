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
 * @class LoginViewController
 * @brief Controller della vista FXML per il Login.
 *
 * Questa classe gestisce l'interazione utente nella schermata di accesso.
 * Si occupa di raccogliere le credenziali inserite, invocare la validazione
 * e gestire la transizione verso la schermata principale (Dashboard) o la
 * visualizzazione di errori.
 *
 * @see javafx.fxml.Initializable
 *
 * @author mello
 * @version 1.0
 */

public class LoginViewController implements Initializable {

    /**
     * Campo di input testuale per lo username.
     */
    @FXML
    private TextField usernameField;

    /**
     * Campo di input mascherato per la password.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Bottone per avviare la procedura di autenticazione.
     */
    @FXML
    private Button btnLogin;

    /**
     * @brief Inizializza il controller.
     *
     * Metodo chiamato automaticamente dal framework JavaFX dopo che il file FXML
     * è stato caricato. Può essere usato per configurare lo stato iniziale della vista
     * (es. focus sul campo username).
     *
     * @param[in] url La location utilizzata per risolvere i percorsi relativi all'oggetto root, o null se non nota.
     * @param[in] rb Le risorse utilizzate per localizzare l'oggetto root, o null se non localizzate.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * @brief Gestisce l'evento di click sul bottone di Login.
     *
     * Recupera i dati dai campi di testo e tenta l'autenticazione.
     *
     * @pre event != null
     * @post Se le credenziali sono corrette -> Chiama switchScene().
     * @post Se le credenziali sono errate -> Chiama mostraAlert().
     *
     * @param[in] event L'evento generato dal click sul bottone.
     */
    @FXML
    void handleLoginAction(ActionEvent event) {
        //gestore dell'evento quanto viene premuto il bottone login
        //scheletro
        /* Esempio logica:
         String user = usernameField.getText();
         String pass = passwordField.getText();
         if(autenticazione.login(user, pass)) {
             switchScene(event);
         } else {
             mostraAlert(Alert.AlertType.ERROR, "Errore", "Credenziali non valide");
         }
        */
    }

    /**
     * @brief Effettua il cambio scena verso la Dashboard del Catalogo.
     *
     * Carica il file FXML della schermata principale e sostituisce la scena corrente.
     *
     * @pre Il file FXML di destinazione deve esistere e essere corretto.
     * @post La finestra di Login viene chiusa e si apre la Dashboard.
     *
     * @param[in] event L'evento che ha scatenato il cambio scena (necessario per recuperare lo Stage).
     */
    private void switchScene(ActionEvent event) {
        //permette di cambiare scena
        //scheletro        
    }

    /**
     * @brief Mostra una finestra di dialogo (Pop-up) all'utente.
     *
     * Utility per visualizzare messaggi di errore, avvisi o conferme in modo modale.
     *
     * @param[in] type Il tipo di alert (es. ERROR, INFORMATION, WARNING).
     * @param[in] title Il titolo della finestra di dialogo.
     * @param[in] content Il messaggio principale da visualizzare.
     */
    private void mostraAlert(Alert.AlertType type, String title, String content) {
        //scheletro
    }
    
}