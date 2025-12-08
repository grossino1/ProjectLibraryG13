package GUI.GUI_Prestito;

import GestioneLibro.CatalogoLibri;
import GestionePrestito.ElencoPrestiti;
import GestionePrestito.Prestito;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
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
 * @class GestionePrestitiViewController
 * @brief Controller per la gestione dell'interfaccia relativa ai Prestiti.
 *
 * Questa classe gestisce la visualizzazione dello storico prestiti, permette di
 * registrarne di nuovi, di restituire libri (rimozione prestito) e di filtrare
 * o ordinare la lista per facilitare la consultazione.
 *
 * @see GestionePrestito.ElencoPrestiti
 * @see javafx.fxml.Initializable
 *
 * @author mello
 * @version 1.0
 */

public class GestionePrestitiViewController implements Initializable {

    @FXML
    private Button handleLogout;
    @FXML
    private Button handleNuovoPrestito;
    
    // Bottoni per l'ordinamento
    @FXML
    private Button handleSortReturnData;
    @FXML
    private Button handleSortMostRecent;
    @FXML
    private Button handleSortLatestRecent;
    
    @FXML
    private TextField handleCercaPrestito;
    @FXML
    private Button filterScaduti;
    
    /**
     * Tabella per la visualizzazione dei prestiti.
     */
    @FXML
    private TableView<Prestito> tabellaPrestiti;
    
    // Colonne della tabella
    @FXML
    private TableColumn<Prestito, String> colIdPrestito;
    @FXML
    private TableColumn<Prestito, String> colLibro;
    @FXML
    private TableColumn<Prestito, String> colUtente;
    @FXML
    private TableColumn<Prestito, LocalDate> colDataScadenza;
    @FXML
    //private TableColumn<?, ?> colStato;
    
    private ObservableList<Prestito> prestitoList;
    private ElencoPrestiti elencoPrestiti;

    /**
     * @brief Inizializza il controller.
     *
     * Configura le colonne della tabella (binding con le proprietà di Prestito)
     * e carica la lista iniziale dei prestiti attivi.
     *
     * @param[in] url Location per risolvere i percorsi relativi.
     * @param[in] rb Risorse per la localizzazione.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prestitoList = FXCollections.observableArrayList();
        tabellaPrestiti.setItems(prestitoList);
        //elencoPrestiti= new ElencoPrestiti();
        
        colIdPrestito.setCellValueFactory(new PropertyValueFactory<>("IDPrestito"));
        colLibro.setCellValueFactory(new PropertyValueFactory<>("ISBNLibro"));
        colUtente.setCellValueFactory(new PropertyValueFactory<>("matricolaUtente"));
        colDataScadenza.setCellValueFactory(new PropertyValueFactory<>("dataRestituzione"));
        //colStato.setCellValueFactory(new PropertyValueFactory<>(""));
        
        //no sorting 
        colIdPrestito.setSortable(false);
        colLibro.setSortable(false);
        colUtente.setSortable(false);
        colDataScadenza.setSortable(false);
    
    }     
    
    /**
     * @brief Gestisce l'apertura del modulo per un nuovo prestito.
     *
     * @post Se l'operazione va a buon fine, un nuovo prestito viene aggiunto alla lista.
     * @post La TableView viene aggiornata per mostrare il nuovo record.
     *
     * @param[in] event L'evento di click sul pulsante.
     */
    @FXML
    void handleAggiungiPrestito(ActionEvent event){
        //chiama un metodo che permette di aggiungere un prestito nella
        //lista dei prestiti e aggiorna la vista del catalogo
        //scheletro
    }
    
    /**
     * @brief Metodo di utilità per la navigazione tra le schermate (Scene).
     *
     * @pre fxmlPath != null && !fxmlPath.isEmpty()
     * @post La scena corrente viene sostituita.
     *
     * @param[in] event L'evento scatenante.
     * @param[in] fxmlPath Il percorso della risorsa FXML da caricare.
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
     * @brief Passa alla schermata di Gestione Utenti.
     * 
     * @see #switchScene(ActionEvent, String)
     */
    @FXML
    void handleGestioneUtenti(ActionEvent event) {
        //permette di passare alla schermata per la gesione degli utenti
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_GestioneUtenti/GestioneUtentiView.fxml");
    }
    
    /**
     * @brief Modifica i dati di un prestito esistente (es. proroga scadenza).
     *
     * @pre Un prestito deve essere selezionato nella tabella.
     * @post I dati del prestito vengono aggiornati e la vista rinfrescata.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleModifyPrestito(ActionEvent event){
        //permette di modificare il prestito selezionato tramite handleSelectedLibro
        //scheletro
    }
    
    /**
     * @brief Rimuove un prestito (es. restituzione libro).
     *
     * @pre Un prestito deve essere selezionato nella tabella.
     * @post Il prestito viene rimosso dalla lista dei prestiti attivi (o marcato come chiuso).
     * @post Il numero di copie del libro associato viene incrementato (tramite logica di business).
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleRemovePrestito(ActionEvent event){
        //permette di rimuovere il prestito selezionato tramite handleSelectedLibro
        //scheletro
    }
    
    /**
     * @brief Effettua il logout dal sistema.
     * 
     * @post Ritorna alla schermata di Login.
     */
    @FXML
    void handleLogout(ActionEvent event) {
        //permette di passare alla schermata del login
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_Login/LoginView.fxml");
    }
    
    /**
     * @brief Ordina i prestiti per Data di Restituzione (Scadenza).
     *
     * Utile per visualizzare quali prestiti sono in scadenza o scaduti.
     *
     * @post La tabella visualizza i prestiti ordinati per data di fine.
     */
    @FXML
    void handleSortReturnDate(ActionEvent event){
        //permette di ordinare in base alla data di restituzione
        //scheletro
    }
    
    /**
     * @brief Ordina i prestiti dal più recente al meno recente (Newest First).
     *
     * Basato sulla data di inizio prestito.
     *
     * @post I prestiti appena creati appaiono in cima alla lista.
     */
    @FXML
    void handleSortMostRecent(ActionEvent event){
        //permette di ordinare la lista dei prestiti dal più recente
        //scheletro
    }
    
    /**
     * @brief Ordina i prestiti dal meno recente al più recente (Oldest First).
     *
     * Basato sulla data di inizio prestito.
     *
     * @post I prestiti più vecchi appaiono in cima alla lista.
     */
    @FXML
    void handleSortLatestRecent(ActionEvent event){
        //permette di ordinare la lista dei prestiti dal meno recente
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
    private void showAlert(Alert.AlertType type, String title, String content) {
        //scheletro
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}