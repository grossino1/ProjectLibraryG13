package GUI.GUI_Utente;

import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * @class GestioneUtentiViewController
 * @brief Controller per la gestione dell'anagrafica degli Utenti.
 *
 * Questa classe gestisce la visualizzazione della lista utenti, le operazioni di 
 * registrazione (CRUD), l'ordinamento e la ricerca. Permette al bibliotecario
 * di monitorare chi è iscritto al servizio e quanti prestiti ha attivi.
 *
 * @see GestioneUtente.ListaUtenti
 * @see GestioneUtente.Utente
 * @see javafx.fxml.Initializable
 *
 * @author mello
 * @version 1.0
 */

public class GestioneUtentiViewController implements Initializable {

    @FXML
    private Button handleLogout;
    @FXML
    private Button handleAggiungiUtente;
    
    // Bottoni per l'ordinamento
    @FXML
    private Button handleSortCognome;
    @FXML
    private Button handleSortMostRecent;
    @FXML
    private Button handleSortLatestRecent;
    
    /**
     * Campo di testo per la ricerca (es. per nome o matricola).
     */
    @FXML
    private TextField handleCercaUtente;
    
    /**
     * Tabella per la visualizzazione degli utenti registrati.
     */
    @FXML
    private TableView<Utente> tabellaUtenti;
    
    // Colonne della tabella
    @FXML
    private TableColumn<Utente, String> colTessera;       // Corrisponde alla Matricola
    @FXML
    private TableColumn<Utente, String> colNome;
    @FXML
    private TableColumn<Utente, String> colCognome;
    @FXML
    private TableColumn<Utente, String> colEmail;
    @FXML
    private TableColumn<Utente, Integer> colNPrestitiAttivi; // Contatore prestiti correnti

    private ObservableList<Utente> utenteList;
    private ListaUtenti listaUtenti;
    /**
     * @brief Inizializza il controller.
     *
     * Configura le colonne della tabella (binding con le proprietà dell'oggetto Utente)
     * e carica la lista iniziale degli utenti dal database/file.
     *
     * @param[in] url Location per risolvere i percorsi relativi.
     * @param[in] rb Risorse per la localizzazione.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        utenteList = FXCollections.observableArrayList();
        tabellaUtenti.setItems(utenteList);
        listaUtenti = new ListaUtenti();;
        
        colTessera.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailIstituzionale"));
        colNPrestitiAttivi.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getListaPrestiti().size()).asObject());
        
        colTessera.setSortable(false);
        colNome.setSortable(false);
        colCognome.setSortable(false);
        colEmail.setSortable(false);
        colNPrestitiAttivi.setSortable(false);
    
    }     

    /**
     * @brief Gestisce l'aggiunta di un nuovo utente al sistema.
     *
     * Apre il form di registrazione utente.
     *
     * @post Se confermato, un nuovo utente viene aggiunto alla ListaUtenti.
     * @post La tabella viene aggiornata includendo il nuovo iscritto.
     *
     * @param[in] event L'evento di click sul pulsante.
     */
    @FXML
    void handleAggiungiUtente(ActionEvent event) {
        // scheletro
    }

    /**
     * @brief Ordina la lista utenti in ordine alfabetico per Cognome.
     *
     * @post La visualizzazione della tabella è ordinata A-Z per cognome.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleSortCognome(ActionEvent event) {
        // scheletro
    }

    /**
     * @brief Ordina gli utenti dal più recente al meno recente (Newest First).
     *
     * Basato sulla data di iscrizione o sull'ordine di inserimento (Matricola decrescente).
     *
     * @post Gli utenti iscritti per ultimi appaiono in cima.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleSortMostRecent(ActionEvent event) {
        // scheletro
    }

    /**
     * @brief Ordina gli utenti dal meno recente al più recente (Oldest First).
     *
     * @post Gli utenti iscritti per primi appaiono in cima.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleSortLatestRecent(ActionEvent event) {
        // scheletro
    }

    /**
     * @brief Filtra la tabella in base al testo inserito nella barra di ricerca.
     * 
     * @param[in] event L'evento (es. pressione tasto invio o click su lente).
     */
    @FXML
    void handleCercaUtente(ActionEvent event) {
        // scheletro
    }

    /**
     * @brief Effettua il logout e torna alla schermata di Login.
     *
     * @post La sessione corrente viene terminata.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleLogout(ActionEvent event) {
        // scheletro: 
        switchScene(event, "/GUI/GUI_Login/LoginView.fxml");
    }
    
    /**
     * @brief Passa alla schermata del Catalogo Libri.
     * 
     * @see #switchScene(ActionEvent, String)
     */
    @FXML
    void handleCatalogoLibri(ActionEvent event) {
        //permette di passare alla schermata del catalogo dei libri
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_CatalogoLibri/CatalogoLibriView.fxml");
    }
    
    /**
     * @brief Naviga alla sezione Gestione Prestiti.
     *
     * @see #switchScene(ActionEvent, String)
     * @param[in] event L'evento di click.
     */
    @FXML
    void handlePrestiti(ActionEvent event) {
        //permette di passare alla schermata dei prestiti
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_Prestiti/PrestitiView.fxml");
    }
    
    /**
     * @brief Gestisce il cambio scena generico.
     * 
     * @param[in] event Evento scatenante.
     * @param[in] fxmlPath Percorso della nuova vista.
     */
    @FXML 
    void switchScene(ActionEvent event, String fxmlPath){
        //permette di cambiare scena in base al pulsante cliccato e al path fornito in fxmlPath
        //si potrebbe effettuare un salvataggio dei dati prima del passaggio
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