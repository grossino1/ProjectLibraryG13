package GUI.GUI_Login;

import Autenticazione.Bibliotecario;
import Eccezioni.EccezioniAutenticazione.LoginCredentialsNotValidException;
import Eccezioni.EccezioniAutenticazione.PasswordFieldEmptyException;
import Eccezioni.EccezioniAutenticazione.UsernameFieldEmptyException;
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
    private Button handleLogin;

    //percorso del file con le credenziali
    private String FILENAME = "us.bin";
    /**
     * @brief Inizializza il controller.
     *
     * Metodo chiamato automaticamente dal framework JavaFX dopo che il file FXML
     * è stato caricato.
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
    void handleLoginAction(ActionEvent event) throws IOException, UsernameFieldEmptyException, PasswordFieldEmptyException, LoginCredentialsNotValidException {
        //gestore dell'evento quanto viene premuto il bottone login
        //scheletro
        
        //prendiamo il contenuto dei textfield
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        //carichiamo le credenziali di accesso corrette presenti nel file FILENAME
        Bibliotecario admin = SalvataggioFile.SalvataggioFileAutenticazione.SalvataggioFileBibliotecario.carica(FILENAME);
        
        boolean accessoConsentito;
        // ----------------------------------------------------------------------------------
        try{
            if (accessoConsentito = admin.login(username, password) ) {
                System.out.println("Login effettuato con successo!");
                switchScene(event, "/GUI/GUI_CatalogoLibri/CatalogoLibriView.fxml");
            }
        }catch (UsernameFieldEmptyException ex){
            showAlert(Alert.AlertType.ERROR, "Errore Login", ex.getMessage());  //gestione delle eccezioni
        }catch (PasswordFieldEmptyException ex){
            showAlert(Alert.AlertType.ERROR, "Errore Login", ex.getMessage());  //gestione delle eccezioni
        }catch (LoginCredentialsNotValidException ex){
            showAlert(Alert.AlertType.ERROR, "Errore Login", ex.getMessage());  //gestione delle eccezioni
        }
    }

    /**
     * @brief Gestisce il cambio scena generico.
     * 
     * @param[in] event Evento scatenante.
     * @param[in] fxmlPath Percorso della nuova vista.
     */
    private void switchScene(ActionEvent event, String fxmlPath) {
        //permette di cambiare scena
        //scheletro   
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Scene stageAttuale = ((Node) event.getSource()).getScene();       
            stageAttuale.setRoot(root);
            
        }catch(IOException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Errore Critico!","Errore nel caricamento della Scena: " + e.getMessage());
        }     
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
    private void showAlert(Alert.AlertType type, String title, String content) {
        //scheletro
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}