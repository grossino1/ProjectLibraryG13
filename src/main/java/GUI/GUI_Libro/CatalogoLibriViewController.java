package GUI.GUI_Libro;

import Eccezioni.EccezioniLibri.ISBNNotValidException;
import GestioneLibro.CatalogoLibri;
import GestioneLibro.Libro;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
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
 * @class CatalogoLibriViewController
 * @brief Controller per la gestione dell'interfaccia del Catalogo Libri.
 *
 * Questa classe gestisce la visualizzazione tabellare dei libri, le operazioni di ordinamento,
 * e funge da pannello di controllo per le operazioni CRUD (Aggiunta, Modifica, Rimozione copie)
 * e per la navigazione verso altre sezioni dell'applicazione (Prestiti, Utenti).
 *
 * @see GestioneLibro.CatalogoLibri
 * @see javafx.fxml.Initializable
 *
 * @author mello
 * @version 1.0
 */

public class CatalogoLibriViewController implements Initializable {

    @FXML
    private Button handleAggiungiLibro;
    @FXML
    private Button handleSortTitolo;
    @FXML
    private Button handleSortAnno;
    
    /**
     * Tabella per la visualizzazione dei libri.
     */
    @FXML
    private TableView<Libro> tabellaLibri;
    @FXML
    private TableColumn<Libro, String> colIsbn; 
    @FXML
    private TableColumn<Libro, String> colTitolo;
    @FXML
    private TableColumn<Libro, String> colAutore;
    @FXML
    private TableColumn<Libro, Integer> colAnno;
    @FXML
    private TableColumn<Libro, Integer> colNCopie;
    @FXML
    private Button handleLogout;
    
    private ObservableList<Libro> libroList;
    private CatalogoLibri catalogoLibri;

    /**
     * @brief Inizializza il controller e configura la tabella.
     *
     * Metodo chiamato automaticamente al caricamento della vista.
     * Si occupa di collegare le colonne della TableView alle proprietà dell'oggetto Libro
     * e di caricare i dati iniziali dal catalogo.
     *
     * @param[in] url Location per risolvere i percorsi relativi.
     * @param[in] rb Risorse per la localizzazione.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        libroList = FXCollections.observableArrayList();
        tabellaLibri.setItems(libroList);
        catalogoLibri = new CatalogoLibri();
        
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        colNCopie.setCellValueFactory(new PropertyValueFactory<>("numeroCopie"));
    }    
    
    /**
     * @brief Gestisce l'aggiunta di un nuovo libro.
     *
     * Apre una finestra di dialogo o cambia scena per permettere l'inserimento
     * dei dati di un nuovo libro.
     *
     * @post Il catalogo viene aggiornato con il nuovo libro (se confermato).
     * @post La TableView riflette la nuova aggiunta.
     *
     * @param[in] event L'evento di click sul pulsante.
     */
    @FXML
    void handleAggiungiLibro(ActionEvent event) throws ISBNNotValidException{
        //chiama un metodo che permette di aggiungere un libro nel catalogo dei 
        //libri e aggiorna la vista del catalogo
        //scheletro
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI_CatalogoLibri/LibroView.fxml"));
            Parent child = loader.load();
            
            Stage aggiungiLibroStage = new Stage();
            aggiungiLibroStage.setTitle("Aggiungi Nuovo Libro");
            Scene sceneLibri = new Scene(child);
            aggiungiLibroStage.setScene(sceneLibri);
            aggiungiLibroStage.show();
            
            Button btnSalva = (Button) child.lookup("#btnSalva");
            Button btnAnnulla = (Button) child.lookup("#btnAnnulla");
            
            btnSalva.setOnAction(e -> {
                try {
                    // Leggiamo i dati dai campi che abbiamo appena trovato
                    TextField isbn = (TextField) child.lookup("#txtIsbn");
                    TextField titolo = (TextField) child.lookup("#txtTitolo");
                    TextField autore = (TextField) child.lookup("#txtAutore");
                    TextField anno = (TextField) child.lookup("#txtAnno");
                    TextField numeroCopie = (TextField) child.lookup("#txtNCopie");
            
                    catalogoLibri.registrazioneLibro(new Libro(titolo.getText(), autore.getText(), Integer.parseInt(anno.getText()), isbn.getText(), Integer.parseInt(numeroCopie.getText())));
                    refreshTable();
                    aggiungiLibroStage.close();
                } catch (NumberFormatException ex) {
                    System.out.println("Errore: Anno non valido");
                } catch (Exception ex) {
                    System.out.println("Errore generico: " + ex.getMessage());
                }
            });
            
            btnAnnulla.setOnAction(e -> { 
                try {
                    aggiungiLibroStage.close();
                } catch (Exception ex) {
                    System.out.println("Errore generico: " + ex.getMessage());
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }   
    }
    
    /**
     * @brief Ordina il catalogo per codice ISBN.
     *
     * @post La lista visualizzata nella tabella viene riordinata secondo l'ISBN.
     *
     * @param[in] event L'evento di click sul pulsante di ordinamento.
     */
    @FXML
    void handleSortLibro(ActionEvent event){
        //richiama il metodo sort e ordina il catalogo libri in base al codice ISBN
        //e aggiorna la vista del catalogo
        //scheletro
    }
    
    /**
     * @brief Ordina il catalogo per Anno di Pubblicazione.
     *
     * @post La lista visualizzata nella tabella viene riordinata cronologicamente.
     *
     * @param[in] event L'evento di click sul pulsante di ordinamento.
     */
    @FXML
    void handleSortAnno(ActionEvent event){
        //richiama il metodo sort e ordina il catalogo libri in base all'anno di pubblicazione
        //e aggiorna la vista del catalogo
        //scheletro
    }
    
    @FXML
    void refreshTable(){
        libroList.clear(); // 1. Cancella i dati vecchi dalla vista
        libroList.addAll(catalogoLibri.getCatalogoLibri());
    }
    
    /**
     * @brief Metodo utility per il cambio scena (navigazione).
     *
     * @pre fxmlPath != null && !fxmlPath.isEmpty()
     * @post La scena corrente viene chiusa e sostituita da quella indicata nel path.
     *
     * @param[in] event L'evento che ha scatenato il cambio (per recuperare lo Stage).
     * @param[in] fxmlPath Il percorso del file FXML da caricare.
     */
    @FXML 
    void switchScene(ActionEvent event, String fxmlPath){
        //permette di cambiare scena in base al pulsante cliccato e al path fornito in fxmlPath
        //si potrebbe effettuare un salvataggio dei dati prima del passaggio
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
            showAlert(Alert.AlertType.ERROR,"Errore Critico!","Errore nel caricamento della Scena: " + e.getMessage());
        }
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
    }
    
    /**
     * @brief Naviga alla sezione Gestione Utenti.
     *
     * @see #switchScene(ActionEvent, String)
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleGestioneUtenti(ActionEvent event) {
        //permette di passare alla schermata per la gesione degli utenti
        //da implemetare con switchScene
        //scheletro
    }
    
    /**
     * @brief Gestisce la selezione di un libro nella tabella.
     *
     * Questo metodo viene attivato quando l'utente clicca su una riga della tabella.
     * Abilita i pulsanti contestuali (Aggiungi Copia, Rimuovi Copia, Modifica).
     *
     * @post I pulsanti di modifica diventano visibili/cliccabili.
     * @param[in] event L'evento di selezione.
     */
    @FXML
    void handleSelectedLibro(ActionEvent event){
        //permette di selezionare un libro e far apparire delle icone per la modifica del libro, aggiunta e rimozione di una copia
        //scheletro
    }
    
    /**
     * @brief Incrementa il numero di copie del libro selezionato.
     *
     * @pre Un libro deve essere attualmente selezionato nella tabella.
     * @post Il numero di copie del libro selezionato aumenta di 1.
     * @post La vista della tabella viene aggiornata.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleAddCopyLibro(ActionEvent event){
        //permette di aggiungere una copia del libro selezionato tramite handleSelectedLibro
        //scheletro
    }
    
    /**
     * @brief Decrementa il numero di copie del libro selezionato.
     *
     * @pre Un libro deve essere selezionato.
     * @pre Il numero di copie del libro deve essere sufficiente (logica di business).
     * @post Il numero di copie diminuisce di 1.
     * @post Se le copie scendono a zero (o soglia minima), potrebbe essere rimosso o disattivato.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleRemoveCopyLibro(ActionEvent event){
        //permette di rimuovere una copia del libro selezionato tramite handleSelectedLibro
        //controllo per quanto riguarda presenza di 1 sola copia
        //scheletro
    }
    
    /**
     * @brief Apre l'interfaccia di modifica per il libro selezionato.
     *
     * @pre Un libro deve essere selezionato.
     * @post I dati del libro vengono aggiornati nel catalogo e nella vista.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleModifyLibro(ActionEvent event){
        //permette di modificare il libro selezionato tramite handleSelectedLibro
        //scheletro
    }

    /**
     * @brief Effettua il logout e torna alla schermata di Login.
     *
     * @post La sessione utente corrente viene terminata.
     * @post Viene visualizzata la schermata di Login.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleLogout(ActionEvent event) {
        //permette di passare alla schermata del login
        //da implemetare con switchScene
        //scheletro
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        //scheletro
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
