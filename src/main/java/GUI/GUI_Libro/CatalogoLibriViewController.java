package GUI.GUI_Libro;

import Eccezioni.EccezioniLibri.CatalogoPienoException;
import Eccezioni.EccezioniLibri.ISBNNotValidException;
import Eccezioni.EccezioniLibri.LibroNotFoundException;
import Eccezioni.EccezioniLibri.LibroPresenteException;
import Eccezioni.EccezioniLibri.LibroWithPrestitoException;
import Eccezioni.EccezioniPrestiti.CopieEsauriteException;
import GUI.GUI_Prestito.GestionePrestitiViewController;
import GestioneLibro.CatalogoLibri;
import GestioneLibro.Libro;
import SalvataggioFile.SalvataggioFileLibro.SalvataggioFileLibro;
import java.io.IOException;
import java.net.URL;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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

    //bottoni presenti nella GUI
    @FXML
    private Button handleAggiungiLibro;
    @FXML
    private Button handleSortTitolo;
    @FXML
    private Button handleSortAnno;
    @FXML 
    private Button btnModifica;
    @FXML 
    private Button btnElimina;
    @FXML
    private Button btnPiuCopie;
    @FXML
    private Button btnMenoCopie;
    @FXML
    private Button handleLogout;
    @FXML
    private Button handleInvio;
    @FXML
    private TextField handleCercaLibro;
    @FXML
    private Label libriPresentiLabel;
    
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
    
    //ObservableList per il funzionamento della tabella
    private ObservableList<Libro> libroList;
    private FilteredList<Libro> filteredData;

    
    //Oggetto di classe catalogo libro per svolgere diverse funzioni
    private CatalogoLibri catalogoLibri;
    private String filename = "..\\src\\main\\resources\\data\\catalogoLibri.bin";
    
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
        try {
            catalogoLibri = new CatalogoLibri(true, filename);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore critico!", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore generico!", ex.getMessage());
        }
        
        libroList = FXCollections.observableArrayList(catalogoLibri.getCatalogoLibri());
        filteredData = new FilteredList<>(libroList, p -> true);
        tabellaLibri.setItems(libroList);
        SortedList<Libro> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabellaLibri.comparatorProperty());
        
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        colNCopie.setCellValueFactory(new PropertyValueFactory<>("numeroCopie"));
        
        //per rimuovere la funzione si sort quando si clicca il nome della colonna 
        colIsbn.setSortable(false);
        colTitolo.setSortable(false);
        colAutore.setSortable(false);
        colAnno.setSortable(false);
        colNCopie.setSortable(false);
        colTitolo.setSortable(true);
        colTitolo.setSortType(TableColumn.SortType.ASCENDING);
        tabellaLibri.getSortOrder().clear();
        tabellaLibri.getSortOrder().add(colTitolo);
        tabellaLibri.sort();
        colTitolo.setSortable(false);
        tabellaLibri.setItems(sortedData);
        //per il counter dei libri presenti nel catalogo
        String nLibriPresenti = String.valueOf(catalogoLibri.getCatalogoLibri().size());
        libriPresentiLabel.setText("Libri Presenti: " + nLibriPresenti);
    }
    
    /**
     * @brief Aggiorna la tabella dei libri sincronizzandola con il catalogo.
     *
     * Questo metodo svuota la lista visualizzata nella TableView e la ripopola
     * recuperando tutti i libri presenti nel catalogo. Serve per riflettere
     * visivamente eventuali modifiche (come nuove aggiunte o rimozioni).
     *
     * @post La lista visibile (libroList) contiene esattamente gli elementi attuali di catalogoLibri.
     * 
     * @throws IOException se il path passato è errato.
     * @throws ClassNotFoundExcepiton se durante la deserializzazione la classe del catalogo salvato 
     * non corrisponde alla versione della classe locale.
     */    
    @FXML
    void refreshTable() throws IOException, ClassNotFoundException{
        String nLibriPresenti = String.valueOf(catalogoLibri.getCatalogoLibri().size());
        libriPresentiLabel.setText("Libri Presenti: " + nLibriPresenti);

        libroList.clear(); // Cancella i dati vecchi dalla vista

        //catalogoLibri = SalvataggioFileLibro.carica(filename);
        libroList.addAll(catalogoLibri.getCatalogoLibri());
        colTitolo.setSortable(true);
        colTitolo.setSortType(TableColumn.SortType.ASCENDING);
        tabellaLibri.getSortOrder().clear();
        tabellaLibri.getSortOrder().add(colTitolo);
        tabellaLibri.sort();
        colTitolo.setSortable(false);
    }
    
    /**
     * @brief Gestisce il cambio scena generico.
     * 
     * @param[in] event Evento scatenante.
     * @param[in] fxmlPath Percorso della nuova vista.
     */
    @FXML 
    void switchScene(ActionEvent event, String fxmlPath) throws IOException{
        //permette di cambiare scena in base al pulsante cliccato e al path fornito in fxmlPath
        //si potrebbe effettuare un salvataggio dei dati prima del passaggio
        //scheletro
        SalvataggioFileLibro.salva(catalogoLibri, filename);

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
     * @brief Naviga alla sezione Gestione Prestiti.
     *
     * @see #switchScene(ActionEvent, String)
     * @param[in] event L'evento di click.
     * 
     * * @throws IOException se il path passato è errato e/o per SalvataggioFileLibro.
     */
    @FXML
    void handlePrestiti(ActionEvent event) throws IOException {
        //permette di passare alla schermata dei prestiti
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_Prestiti/ElencoPrestitiView.fxml");
    }
    
    /**
     * @brief Naviga alla sezione Gestione Utenti.
     *
     * @see #switchScene(ActionEvent, String)
     * @param[in] event L'evento di click.
     * 
     * @throws IOException se il path passato è errato e/o per SalvataggioFileLibro.
     */
    @FXML
    void handleGestioneUtenti(ActionEvent event) throws IOException {
        //permette di passare alla schermata per la gesione degli utenti
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_GestioneUtenti/GestioneUtentiView.fxml");
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
    void handleLogout(ActionEvent event) throws IOException {
        //permette di passare alla schermata del login
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_Login/LoginView.fxml");
    }
    
    /**
     * @brief Gestisce l'aggiunta di un nuovo libro.
     *
     * Apre una finestra di dialogo che permettere l'inserimento
     * dei dati di un nuovo libro.
     *
     * @post Il catalogo viene aggiornato con il nuovo libro (se confermato).
     * @post La TableView riflette la nuova aggiunta.
     *
     * @param[in] event L'evento di click sul pulsante.
     */
    @FXML
    private void handleAggiungiLibro(ActionEvent event){
        //chiama un metodo che permette di aggiungere un libro nel catalogo dei 
        //libri e aggiorna la vista del catalogo
        //scheletro
        try{
            
            //inizio della parte di codice per il caricamento della finestra per l'aggiunta di un nuovo libro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI_CatalogoLibri/LibroView.fxml"));
            Parent child = loader.load();
            
            //modifica della label di titolo e desc 
            Label lblTitolo = (Label) child.lookup("#lblTitolo");
            if (lblTitolo.getText() != null)
                lblTitolo.setText("Nuovo Libro");
            
            Label lblDesc = (Label) child.lookup("#lblDesc");
            if (lblDesc.getText() != null)
                lblDesc.setText("Inserisci i dettagli del libro da aggiungere al catalogo.");
            
            //creazione dello stage e della scene
            Stage aggiungiLibroStage = new Stage();
            aggiungiLibroStage.setTitle("Aggiungi Nuovo Libro");
            Scene sceneLibri = new Scene(child);
            aggiungiLibroStage.setScene(sceneLibri);
            aggiungiLibroStage.show();
            //fine 
            
            //bottoni di  salvataggio e modifica
            Button btnSalva = (Button) child.lookup("#btnSalva");
            Button btnAnnulla = (Button) child.lookup("#btnAnnulla");
            
            //lambda expression per la registrazione del libro
            btnSalva.setOnAction(e -> {
                try {
                    //textfield presenti nell'interfaccia di aggiuntaLibro
                    TextField isbn = (TextField) child.lookup("#txtIsbn");
                    TextField titolo = (TextField) child.lookup("#txtTitolo");
                    TextField autore = (TextField) child.lookup("#txtAutore");
                    TextField anno = (TextField) child.lookup("#txtAnno");
                    TextField numeroCopie = (TextField) child.lookup("#txtNCopie");
                    
                    //registrazione del libro
                    catalogoLibri.registrazioneLibro(new Libro(titolo.getText(), autore.getText(), Integer.parseInt(anno.getText()), isbn.getText(), Integer.parseInt(numeroCopie.getText())));
                    
                    //refresh della tabella per visualizzare le modifice effettuate
                    refreshTable();
                    
                    //chiudo la finestra  per l'aggiunta di un nuovo libro 
                    aggiungiLibroStage.close();
                } catch (LibroNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
                } catch (ISBNNotValidException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
                } catch (LibroPresenteException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + "\n" + ex.getMessage()); //gestione delle eccezioni
                } catch (ClassNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + "\n" + ex.getMessage()); //gestione delle eccezioni
                } catch (CatalogoPienoException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
                }
            });
            
            //lambda expression per annullare l'aggiunta
            btnAnnulla.setOnAction(e -> { 
                try {
                    aggiungiLibroStage.close();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
                }
            }); 
        }catch(IOException ex){
            showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
        }   
    }
    
    /**
     * @brief Incrementa il numero di copie del libro selezionato.
     *
     * @pre Un libro deve essere attualmente selezionato nella tabella.
     * @post Il numero di copie del libro selezionato aumenta di 1.
     * @post La vista della tabella viene aggiornata.
     * 
     * @see #refreshTable() 
     *
     * @param[in] event L'evento di click.
     * 
     * @throws IOException se il path passato è errato.
     * @throws ClassNotFoundExcepiton se durante la deserializzazione la classe del catalogo salvato 
     * non corrisponde alla versione della classe locale.
     * @throws LibroNotFoundException se il libro non è presente.
     */
    @FXML
    void handleAddCopyLibro(ActionEvent event) throws IOException, ClassNotFoundException, LibroNotFoundException{
        //andiamo a prendere il libro selezionato dalla tabella
        Libro selected = tabellaLibri.getSelectionModel().getSelectedItem();
        
        //verifichiamo che l'utente abbia selezionato o meno un libro dalla tabella
        if (selected != null) {
            //incremento copia e modifica all'interno del catalogo libri
            selected.incrementaCopiaLibro();
            catalogoLibri.modificaLibro(selected, selected.getTitolo(), selected.getAutori(),selected.getAnnoPubblicazione(), selected.getNumeroCopie());
        }else{
            showAlert(Alert.AlertType.WARNING, "Nessuna Selezione", "Per favore, seleziona un libro dalla tabella per aggiungere una copia.");    //alert che viene mostrato se il libro non è stato selezionato
        }
        
        //refresh della tabella per visualizzare le modifice effettuate
        refreshTable();
    }
    
    /**
     * @brief Decrementa il numero di copie del libro selezionato.
     *
     * @pre Un libro deve essere selezionato.
     * @pre Il numero di copie del libro deve essere sufficiente (logica di business).
     * @post Il numero di copie diminuisce di 1.
     * @post Se le copie scendono a zero (o soglia minima), potrebbe essere rimosso o disattivato.
     *
     * @see #refreshTable() 
     *
     * @param[in] event L'evento di click.
     * 
     * @throws IOException se il path passato è errato.
     * @throws ClassNotFoundExcepiton se durante la deserializzazione la classe del catalogo salvato 
     * non corrisponde alla versione della classe locale.
     * @throws LibroNotFoundException se il libro non è presente.
     */
    @FXML
    void handleRemoveCopyLibro(ActionEvent event) throws IOException, ClassNotFoundException, LibroNotFoundException{
        //andiamo a prendere il libro selezionato dalla tabella
        Libro selected = tabellaLibri.getSelectionModel().getSelectedItem();
        
        //verifichiamo che l'utente abbia selezionato o meno un libro dalla tabella
        if(selected != null){
            try{
                //decremento copia del libro e modifica all'interno del catalogo
                selected.decrementaCopiaLibro();
                catalogoLibri.modificaLibro(selected, selected.getTitolo(), selected.getAutori(),selected.getAnnoPubblicazione(), selected.getNumeroCopie());
            }catch(CopieEsauriteException ex){
                showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
            }
        }else{
            showAlert(Alert.AlertType.WARNING, "Nessuna Selezione", "Per favore, seleziona un libro dalla tabella per rimuovere una copia.");   //alert che viene mostrato se il libro non è stato selezionato
        }
        //refresh della tabella per visualizzare le modifice effettuate
        refreshTable();
    }
    
    /**
     * @brief Elimina il libro selezionato
     * 
     * @pre Un libro deve essere selezionato
     * 
     * @post Il libro viene eliminato dal catalogo libri
     * 
     * @see #refreshTable() 
     *
     * @param[in] event L'evento di click.
     * 
     * @throws IOException se il path passato è errato.
     * @throws ClassNotFoundExcepiton se durante la deserializzazione la classe del catalogo salvato 
     * non corrisponde alla versione della classe locale.
     * @throws LibroWithPrestitoException se il libro è in stato di prestito.
     */
    @FXML
    void handleDeleteLibro(ActionEvent event) throws IOException, ClassNotFoundException, LibroWithPrestitoException{
        //andiamo a prendere il libro selezionato dalla tabella
        Libro selected = tabellaLibri.getSelectionModel().getSelectedItem();

        //verifichiamo che l'utente abbia selezionato o meno un libro dalla tabella
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Nessuna Selezione", "Per favore, seleziona un libro dalla tabella per eliminarlo.");
            return; // Esce dal metodo subito
        }
        
        //alert di conferma modifiche        
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma eliminazione");
        conferma.setHeaderText("Sei sicuro di voler rimuovere il libro selezionato?");
        
        Optional<ButtonType> risultato = conferma.showAndWait();
        
        if (risultato.isPresent() && risultato.get() == ButtonType.OK) {
            try{
                //eliminazione del libro e refresh della tabella 
                catalogoLibri.eliminazioneLibro(selected);
                refreshTable();
            }catch(LibroNotFoundException ex){
                showAlert(Alert.AlertType.ERROR, "Errore generico",ex.getMessage());    //gestione delle eccezioni
            }catch (LibroWithPrestitoException ex){
                showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
            }
        }
    }
    
    /**
     * @brief Modifica i dati di un prestito esistente 
     *
     * @pre Un prestito deve essere selezionato nella tabella.
     * @post I dati del prestito vengono aggiornati e la vista refreshata.
     * 
     * @see #refreshTable() 
     *
     * @param[in] event L'evento di click.
     * 
     * @throws LibroNotFoundException se il libro non è presente.
     */
    @FXML
    void handleModifyLibro(ActionEvent event) throws LibroNotFoundException{
        //permette di modificare il libro selezionato tramite handleSelectedLibro
        //scheletro
        
        //andiamo a prendere il libro selezionato dalla tabella
        Libro selected = tabellaLibri.getSelectionModel().getSelectedItem();
        
        //verifichiamo che l'utente abbia selezionato o meno un libro dalla tabella
        if(selected != null){
            try{

                //inizio della parte di codice per il caricamento della finestra per l'aggiunta di un nuovo libro
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI_CatalogoLibri/LibroView.fxml"));
                Parent child = loader.load();
                
                //modifica delle label di Libro.fxml per adattarle con i testi per la modifica
                Label lblTitolo = (Label) child.lookup("#lblTitolo");
                if (lblTitolo != null)
                    lblTitolo.setText("Modifica Libro");
                Label lblDesc = (Label) child.lookup("#lblDesc");
                if(lblDesc != null)
                    lblDesc.setText("Inserisci i dettagli del libro da modificare nel catalogo.");

                //creazione dello stage e della scene 
                Stage aggiungiLibroStage = new Stage();
                aggiungiLibroStage.setTitle("Modifica Libro");
                Scene sceneLibri = new Scene(child);
                aggiungiLibroStage.setScene(sceneLibri);
                aggiungiLibroStage.show();
                
                //funzione che permette di non avere il selezionamento di default del primo campo 
                javafx.application.Platform.runLater(() -> {
                    child.requestFocus(); 
                });
                
                //button e textfield presenti nell'interfaccia di modifica
                Button btnSalva = (Button) child.lookup("#btnSalva");
                Button btnAnnulla = (Button) child.lookup("#btnAnnulla");
                TextField isbn = (TextField) child.lookup("#txtIsbn");
                //textfield già compilati con i dati del libro selezionato
                isbn.setText(selected.getIsbn());
                //disabilitazione del campo ISBN in quanto non può esser modificato
                isbn.setDisable(true);
                TextField titolo = (TextField) child.lookup("#txtTitolo");
                titolo.setText(selected.getTitolo());
                TextField autore = (TextField) child.lookup("#txtAutore");
                autore.setText(selected.getAutori());
                TextField anno = (TextField) child.lookup("#txtAnno");
                anno.setText(String.valueOf(selected.getAnnoPubblicazione()));
                TextField numeroCopie = (TextField) child.lookup("#txtNCopie");
                numeroCopie.setText(String.valueOf(selected.getNumeroCopie()));

                //lambda expression per la modifica del libro
                btnSalva.setOnAction(e -> {
                    try {
                        //modifica del libro, refresh della tabella e  chiusura della finestra 
                        catalogoLibri.modificaLibro(selected, titolo.getText(), autore.getText(), Integer.parseInt(anno.getText()), Integer.parseInt(numeroCopie.getText()));
                        refreshTable();
                        aggiungiLibroStage.close();
                    }catch(NumberFormatException ex){
                        showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage());   //gestione delle eccezioni
                    } catch (IOException ex) {
                        showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage());   //gestione delle eccezioni
                    } catch (ClassNotFoundException ex) {
                        showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage());   //gestione delle eccezioni
                    }catch(IllegalArgumentException ex){
                        showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage());   //gestione delle eccezioni             
                    } catch (LibroNotFoundException ex) {
                        showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage());   //gestione delle eccezioni               
                    }
                });
                
                //lambda expression per annullare
                btnAnnulla.setOnAction(e -> { 
                    try {
                        aggiungiLibroStage.close();
                    } catch (Exception ex) {
                        System.out.println("Errore generico: " + ex.getMessage());  //gestione delle eccezioni
                    }
                });
            }catch(IOException ex){
                showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage());   //gestione delle eccezioni
            }
        }else
            showAlert(Alert.AlertType.WARNING, "Nessuna Selezione", "Per favore, seleziona un libro dalla tabella per modificarlo.");    //alert che viene mostrato se il libro non è stato selezionato
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
        //vado ad abilitare il sorting sulla colonna dell'autore
        colAutore.setSortable(true);
        //controllo se stiamo già ordinando per questa colonna
        if (tabellaLibri.getSortOrder().contains(colAutore)) {
            //in caso affermativo, inverto l'ordine (da ASC a DESC )
            if (colAutore.getSortType() == TableColumn.SortType.ASCENDING) {
                colAutore.setSortType(TableColumn.SortType.DESCENDING);
                tabellaLibri.getSortOrder().clear();
                tabellaLibri.getSortOrder().add(colAutore);
                tabellaLibri.sort();
            }else if (colAutore.getSortType() == TableColumn.SortType.DESCENDING){
                //ordinamento di default al terzo click
                colTitolo.setSortable(true);
                colTitolo.setSortType(TableColumn.SortType.ASCENDING);
                tabellaLibri.getSortOrder().clear();
                tabellaLibri.getSortOrder().add(colTitolo);
                tabellaLibri.sort();
                colTitolo.setSortable(false);
            }
        } else {
            colAutore.setSortType(TableColumn.SortType.ASCENDING);
            tabellaLibri.getSortOrder().clear();
            tabellaLibri.getSortOrder().add(colAutore);
            tabellaLibri.sort();
        }
        
        //disabilito l'ordinamento 
        colAutore.setSortable(false);
    }
    
    /**
     * @brief Ordina il catalogo per Anno di Pubblicazione.
     *
     * @post La lista visualizzata nella tabella viene riordinata cronologicamente.
     *
     * @param[in] event L'evento di click sul pulsante di ordinamento.
     */
    @FXML
    void handleSortAnno(ActionEvent event) {
        colAnno.setSortable(true);
        // controlla se stiamo già ordinando per questa colonna
        if (tabellaLibri.getSortOrder().contains(colAnno)) {
            //in caso affermativo, inverti l'ordine (da ASC a DESC)
            if (colAnno.getSortType() == TableColumn.SortType.ASCENDING) {
                colAnno.setSortType(TableColumn.SortType.DESCENDING);
                tabellaLibri.getSortOrder().clear();
                tabellaLibri.getSortOrder().add(colAnno);
                tabellaLibri.sort();
            }else if (colAnno.getSortType() == TableColumn.SortType.DESCENDING){
                colTitolo.setSortable(true);
                colTitolo.setSortType(TableColumn.SortType.ASCENDING);
                tabellaLibri.getSortOrder().clear();
                tabellaLibri.getSortOrder().add(colTitolo);
                tabellaLibri.sort();
                colTitolo.setSortable(false);
            }
        } else {
            colAnno.setSortType(TableColumn.SortType.ASCENDING);
            tabellaLibri.getSortOrder().clear();
            tabellaLibri.getSortOrder().add(colAnno);
            tabellaLibri.sort();
        }
        colAnno.setSortable(false);
    }
    
    
    /**
     * @brief Gestisce l'operazione di ricerca di un libro all'interno del catalogo
     *
     * @post La lista visualizzata nella tabella viene riordinata cronologicamente.
     * 
     * @see GestioneLibro.CatalogoLibri#cercaLibro(String)
     *
     * @param[in] event L'evento che ha scatenato l'azione.
     */
    @FXML
    void handleCercaLibro(ActionEvent event) {
        //recupero del testo dal TextField 
        String filtro = handleCercaLibro.getText(); 

        if (filtro == null || filtro.trim().isEmpty()) {
            libroList.setAll(catalogoLibri.getCatalogoLibri()); 
            return;
        }
        try{
            ArrayList<Libro> risultati = catalogoLibri.cercaLibro(filtro);
            libroList.setAll(risultati);
        }catch (LibroNotFoundException ex) {
            libroList.clear();
        }
        System.out.println("Ricerca libro effettuata per: " + filtro);
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
    private void showAlert(Alert.AlertType type, String title, String content){
        //scheletro
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
