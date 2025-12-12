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
    
    private String filename = "catalogoLibri.bin";
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

    }
    
    /**
     * @brief Aggiorna la tabella dei libri sincronizzandola con il catalogo.
     *
     * Questo metodo svuota la lista visualizzata nella TableView e la ripopola
     * recuperando tutti i libri presenti nel catalogo. Serve per riflettere
     * visivamente eventuali modifiche (come nuove aggiunte o rimozioni).
     * Inoltre, questo metodo salva le operazioni effettuate e ricarica il catalogo e di conseguenza la tabella
     *
     * @post La lista visibile (libroList) contiene esattamente gli elementi attuali di catalogoLibri.
     * 
     * @throws IOException se il path passato è errato.
     * @throws ClassNotFoundExcepiton se durante la deserializzazione la classe del catalogo salvato 
     * non corrisponde alla versione della classe locale.
     */    
    @FXML
    void refreshTable() throws IOException, ClassNotFoundException{
        libroList.clear(); // 1. Cancella i dati vecchi dalla vista
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
     * @brief Metodo utility per il cambio scena (navigazione).
     *
     * @pre fxmlPath != null && !fxmlPath.isEmpty()
     * @post La scena corrente viene chiusa e sostituita da quella indicata nel path.
     *
     * @param[in] event L'evento che ha scatenato il cambio (per recuperare lo Stage).
     * @param[in] fxmlPath Il percorso del file FXML da caricare.
     * 
     * @throws IOException se il path passato è errato (per maggiori informazioni si veda la classe SalvataggioFileLibro).
     */
    @FXML 
    void switchScene(ActionEvent event, String fxmlPath) throws IOException{
        //permette di cambiare scena in base al pulsante cliccato e al path fornito in fxmlPath
        //si potrebbe effettuare un salvataggio dei dati prima del passaggio
        //scheletro
        SalvataggioFileLibro.salva(catalogoLibri, "catalogoLibri.bin");

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
     * Apre una finestra di dialogo o cambia scena per permettere l'inserimento
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
            
            Stage aggiungiLibroStage = new Stage();
            aggiungiLibroStage.setTitle("Aggiungi Nuovo Libro");
            Scene sceneLibri = new Scene(child);
            aggiungiLibroStage.setScene(sceneLibri);
            aggiungiLibroStage.show();
            //fine 
            
            Button btnSalva = (Button) child.lookup("#btnSalva");
            Button btnAnnulla = (Button) child.lookup("#btnAnnulla");
            
            //lambda expression per la registrazione del libro
            btnSalva.setOnAction(e -> {
                try {
                    // Leggiamo i dati dai campi che abbiamo appena trovato
                    TextField isbn = (TextField) child.lookup("#txtIsbn");
                    TextField titolo = (TextField) child.lookup("#txtTitolo");
                    TextField autore = (TextField) child.lookup("#txtAutore");
                    TextField anno = (TextField) child.lookup("#txtAnno");
                    TextField numeroCopie = (TextField) child.lookup("#txtNCopie");
            
                    System.out.println("DEBUG DATI LETTI:");
                    System.out.println("ISBN letto: '" + isbn.getText() + "'");
                    System.out.println("Titolo letto: '" + titolo.getText() + "'");
                    
                    catalogoLibri.registrazioneLibro(new Libro(titolo.getText(), autore.getText(), Integer.parseInt(anno.getText()), isbn.getText(), Integer.parseInt(numeroCopie.getText())));
                    System.out.println(catalogoLibri.toString());
                    refreshTable();
                    aggiungiLibroStage.close();
                } catch (LibroNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
                } catch (ISBNNotValidException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico2", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
                } catch (LibroPresenteException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico3",ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico4", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
                } catch (ClassNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico5", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
                    showAlert(Alert.AlertType.ERROR, "Errore generico5", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
                } catch (CatalogoPienoException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico5", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
                }
            });
            
            btnAnnulla.setOnAction(e -> { 
                try {
                    aggiungiLibroStage.close();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
                }
            }); 
        }catch(IOException e){
            showAlert(Alert.AlertType.ERROR, "Errore generico", e.getMessage()); //gestione delle eccezioni
        }   
    }
    
    /**
     * @brief Gestisce la selezione di un libro nella tabella.
     *
     * Questo metodo viene attivato quando l'utente clicca su una riga della tabella.
     * Abilita i pulsanti contestuali (Aggiungi Copia, Rimuovi Copia, Modifica).
     *
     * @post I pulsanti di modifica diventano visibili/cliccabili.
     */
    @FXML
    void handleSelectedLibro(){
        // Grazie al binding in initialize, non serve scrivere nulla qui per attivare i bottoni.
        // JavaFX lo fa da solo.
        // Puoi lasciare questo metodo vuoto o usarlo per debug:
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            System.out.println("Selezionato: " + selezionato.getTitolo());
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
     */
    @FXML
    void handleAddCopyLibro(ActionEvent event) throws IOException, ClassNotFoundException, LibroNotFoundException{
        // 1. Recupera il libro selezionato
        Libro selected = tabellaLibri.getSelectionModel().getSelectedItem();
        
        if (selected != null) {
            selected.incrementaCopiaLibro();
            catalogoLibri.modificaLibro(selected, selected.getTitolo(), selected.getAutori(),selected.getAnnoPubblicazione(), selected.getNumeroCopie());
            // Feedback visivo (opzionale)
            System.out.println("Copia aggiunta. Totale: " + selected.getNumeroCopie());
        }
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
     */
    @FXML
    void handleRemoveCopyLibro(ActionEvent event) throws IOException, ClassNotFoundException, LibroNotFoundException{
        //permette di rimuovere una copia del libro selezionato tramite handleSelectedLibro
        //controllo per quanto riguarda presenza di 1 sola copia
        //scheletro
        Libro selected = tabellaLibri.getSelectionModel().getSelectedItem();
        
        if(selected != null){
            try{selected.decrementaCopiaLibro();
                catalogoLibri.modificaLibro(selected, selected.getTitolo(), selected.getAutori(),selected.getAnnoPubblicazione(), selected.getNumeroCopie());
            }catch(CopieEsauriteException ex){
                showAlert(Alert.AlertType.ERROR, "Errore generico5", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
            }
        }   
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
     */
    @FXML
    void handleDeleteLibro(ActionEvent event) throws IOException, ClassNotFoundException, LibroWithPrestitoException{
        Libro selected = tabellaLibri.getSelectionModel().getSelectedItem();
        
        try{
            catalogoLibri.eliminazioneLibro(selected);
            refreshTable();
        }catch(LibroNotFoundException ex){
            showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage());
        }catch (LibroWithPrestitoException ex){
            showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
        }
    }
    
    /**
     * @brief Apre l'interfaccia di modifica per il libro selezionato.
     *
     * @pre Un libro deve essere selezionato.
     * @post I dati del libro vengono aggiornati nel catalogo e nella vista.
     * 
     * @see #refreshTable() 
     *
     * @param[in] event L'evento di click.
     * 
     * @throws IOException se il path passato è errato.
     * @throws ClassNotFoundExcepiton se durante la deserializzazione la classe del catalogo salvato 
     * non corrisponde alla versione della classe locale.
     */
    @FXML
    void handleModifyLibro(ActionEvent event) throws LibroNotFoundException{
        //permette di modificare il libro selezionato tramite handleSelectedLibro
        //scheletro
        Libro selected = tabellaLibri.getSelectionModel().getSelectedItem();
        if(selected != null){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI_CatalogoLibri/LibroView.fxml"));
            Parent child = loader.load();
            
            Label lblTitolo = (Label) child.lookup("#lblTitolo");
            if (lblTitolo != null)
                lblTitolo.setText("Modifica Libro");
            
            Label lblDesc = (Label) child.lookup("#lblDesc");
            if(lblDesc != null)
                lblDesc.setText("Inserisci i dettagli del libro da modificare nel catalogo.");
            
            Stage aggiungiLibroStage = new Stage();
            aggiungiLibroStage.setTitle("Modifica Libro");
            Scene sceneLibri = new Scene(child);
            aggiungiLibroStage.setScene(sceneLibri);
            aggiungiLibroStage.show();
            javafx.application.Platform.runLater(() -> {
                child.requestFocus(); 
            });
            
            Button btnSalva = (Button) child.lookup("#btnSalva");
            Button btnAnnulla = (Button) child.lookup("#btnAnnulla");
            TextField isbn = (TextField) child.lookup("#txtIsbn");
            isbn.setText(selected.getIsbn());
            isbn.setDisable(true);
            TextField titolo = (TextField) child.lookup("#txtTitolo");
            titolo.setText(selected.getTitolo());
            TextField autore = (TextField) child.lookup("#txtAutore");
            autore.setText(selected.getAutori());
            TextField anno = (TextField) child.lookup("#txtAnno");
            anno.setText(String.valueOf(selected.getAnnoPubblicazione()));
            TextField numeroCopie = (TextField) child.lookup("#txtNCopie");
            numeroCopie.setText(String.valueOf(selected.getNumeroCopie()));
                    
            btnSalva.setOnAction(e -> {
                try {
                    // Leggiamo i dati dai campi che abbiamo appena trovato           
                    System.out.println("DEBUG DATI LETTI:");
                    System.out.println("ISBN letto: '" + isbn.getText() + "'");
                    System.out.println("Titolo letto: '" + titolo.getText() + "'");
                    
                    catalogoLibri.modificaLibro(selected, titolo.getText(), autore.getText(), Integer.parseInt(anno.getText()), Integer.parseInt(numeroCopie.getText()));
                    
                    System.out.println(catalogoLibri.toString());
                    refreshTable();
                    aggiungiLibroStage.close();
                }catch(NumberFormatException ex){
                    showAlert(Alert.AlertType.ERROR, "Errore generico ", ex.getMessage());
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico ", ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico ", ex.getMessage());
                }catch(IllegalArgumentException ex){
                    showAlert(Alert.AlertType.ERROR, "Errore generico ", ex.getMessage());                    
                } catch (LibroNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico ", ex.getMessage());                    
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
        }else
            showAlert(Alert.AlertType.ERROR, "Errore generico", "Libro non selezionato!");
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
        colAutore.setSortable(true);
        // 1. Controlla se stiamo già ordinando per questa colonna
        if (tabellaLibri.getSortOrder().contains(colAutore)) {
            // Se sì, inverti l'ordine (da ASC a DESC o viceversa)
            if (colAutore.getSortType() == TableColumn.SortType.ASCENDING) {
                colAutore.setSortType(TableColumn.SortType.DESCENDING);
                tabellaLibri.getSortOrder().clear();
                tabellaLibri.getSortOrder().add(colAutore);
                tabellaLibri.sort();
            }else if (colAutore.getSortType() == TableColumn.SortType.DESCENDING){
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
        // 1. Controlla se stiamo già ordinando per questa colonna
        if (tabellaLibri.getSortOrder().contains(colAnno)) {
            // Se sì, inverti l'ordine (da ASC a DESC o viceversa)
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
    
    @FXML
    void handleCercaLibro(ActionEvent event) {
        // Recupera il testo dal TextField (assicurati che il TextField si chiami handleCercaLibro)
        String filtro = handleCercaLibro.getText(); 

        if (filtro == null || filtro.trim().isEmpty()) {
        // Qui devi ricaricare TUTTI i libri (es. dal tuo elenco completo)
            libroList.setAll(catalogoLibri.getCatalogoLibri()); 
        return;
        }
        
        try{
            ArrayList<Libro> risultati = catalogoLibri.cercaLibro(filtro);
            libroList.setAll(risultati);
        }catch (LibroNotFoundException e) {
        // 3. Se il metodo lancia l'eccezione, significa che non ha trovato nulla.
        // Svuotiamo la tabella per mostrare che non ci sono risultati.
            libroList.clear();
        }
        /*
        filteredData.setPredicate(libro -> {
            // 1. Se il campo è vuoto, mostra tutto
            if (filtro == null || filtro.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = filtro.toLowerCase();

            // 2. Recuperiamo il TITOLO in modo sicuro (gestione null)
            // Se il titolo è null, usiamo una stringa vuota "" per evitare crash
            String titolo = (libro.getTitolo() != null) ? libro.getTitolo().toLowerCase() : "";

            // 3. Verifichiamo se il filtro è contenuto nel Titolo
            return titolo.contains(lowerCaseFilter);
        });*/

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
    private void showAlert(Alert.AlertType type, String title, String content) {
        //scheletro
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
