package GUI.GUI_Utente;

import Eccezioni.EccezioniUtenti.ListaUtentiPienaException;
import Eccezioni.EccezioniUtenti.MatricolaNotValidException;
import Eccezioni.EccezioniUtenti.UtenteNotFoundException;
import Eccezioni.EccezioniUtenti.UtentePresenteException;
import Eccezioni.EccezioniUtenti.UtenteWithPrestitoException;
import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML
    private Button handleInvio;
    
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
    @FXML
    private TableColumn<Utente, LocalDateTime> colDataReg; // Contatore prestiti correnti

    private ObservableList<Utente> utenteList;
    private FilteredList<Utente> filteredData;
    private ListaUtenti listaUtenti;
    
    private String filename = "listaUtenti.bin";
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
        try {
            listaUtenti = new ListaUtenti(true, filename);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore critico!", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore critico!", ex.getMessage());
        }
        utenteList = FXCollections.observableArrayList(listaUtenti.getListaUtenti());
        filteredData = new FilteredList<>(utenteList, p -> true);
        tabellaUtenti.setItems(utenteList);
        SortedList<Utente> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabellaUtenti.comparatorProperty());
      
        colTessera.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailIstituzionale"));
        colNPrestitiAttivi.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getListaPrestiti().size()).asObject());
        colDataReg.setCellValueFactory(new PropertyValueFactory<>("dataReg"));
        
        colTessera.setSortable(false);
        colNome.setSortable(false);
        colCognome.setSortable(false);
        colEmail.setSortable(false);
        colNPrestitiAttivi.setSortable(false);
        colDataReg.setSortable(false);
        tabellaUtenti.setItems(sortedData);
    
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
        utenteList.clear(); // 1. Cancella i dati vecchi dalla vista
        listaUtenti = SalvataggioFileUtente.carica(filename);
        utenteList.addAll(listaUtenti.getListaUtenti());
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
        SalvataggioFileUtente.salva(listaUtenti, filename);

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
     * @brief Effettua il logout e torna alla schermata di Login.
     *
     * @post La sessione corrente viene terminata.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        // scheletro: 
        switchScene(event, "/GUI/GUI_Login/LoginView.fxml");
    }
    
    /**
     * @brief Passa alla schermata del Catalogo Libri.
     * 
     * @see #switchScene(ActionEvent, String)
     */
    @FXML
    void handleCatalogoLibri(ActionEvent event) throws IOException {
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
    void handlePrestiti(ActionEvent event) throws IOException {
        //permette di passare alla schermata dei prestiti
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_Prestiti/ElencoPrestitiView.fxml");
    }
    
    /**
     * @brief Gestisce la selezione di un utente nella tabella.
     *
     * Questo metodo viene attivato quando l'utente clicca su una riga della tabella.
     * Abilita i pulsanti contestuali (Aggiungi Copia, Rimuovi Copia, Modifica).
     *
     * @post I pulsanti di modifica diventano visibili/cliccabili.
     */
    @FXML
    void handleSelectedLibro(){
        
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            System.out.println("Selezionato: " + selezionato.getNome());
        }
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
        try{
            
            //inizio della parte di codice per il caricamento della finestra per l'aggiunta di un nuovo libro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI_GestioneUtenti/UtenteView.fxml"));
            Parent child = loader.load();
            
            //modifica della label di titolo e desc 
            Label lblTitolo = (Label) child.lookup("#lblTitolo");
            if (lblTitolo != null)
                lblTitolo.setText("Nuovo Utente");
            
            Label lblDesc = (Label) child.lookup("#lblDesc");
            if (lblDesc != null)
                lblDesc.setText("Inserisci i dettagli dello studente da aggiungere.");
            
            Stage aggiungiUtenteStage = new Stage();
            aggiungiUtenteStage.setTitle("Aggiungi Nuovo Utente");
            Scene sceneLibri = new Scene(child);
            aggiungiUtenteStage.setScene(sceneLibri);
            aggiungiUtenteStage.show();
            //fine 
            
            Button btnSalva = (Button) child.lookup("#btnSalva");
            Button btnAnnulla = (Button) child.lookup("#btnAnnulla");
            
            //lambda expression per la registrazione del libro
            btnSalva.setOnAction(e -> {
                try {
                    // Leggiamo i dati dai campi che abbiamo appena trovato
                    TextField nome= (TextField) child.lookup("#txtNome");
                    TextField cognome = (TextField) child.lookup("#txtCognome");
                    TextField matricola = (TextField) child.lookup("#txtMatricola");
                    TextField email = (TextField) child.lookup("#txtEmail");
            
                    System.out.println("DEBUG DATI LETTI:");
                    System.out.println("ISBN letto: '" + nome.getText() + "'");
                    System.out.println("Titolo letto: '" + cognome.getText() + "'");
                    
                    listaUtenti.registrazioneUtente(new Utente(nome.getText(), cognome.getText(), matricola.getText(), email.getText()));
                    System.out.println(listaUtenti.toString());
                    refreshTable();
                    aggiungiUtenteStage.close();
                }catch (MatricolaNotValidException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico1", ex.getMessage());
                } catch (UtentePresenteException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico2", ex.getMessage());
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico3", ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico4", ex.getMessage());
                } catch (ListaUtentiPienaException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico4", ex.getMessage());

                }
            });
            
            btnAnnulla.setOnAction(e -> { 
                try {
                    aggiungiUtenteStage.close();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
                }
            });
        }catch(IOException e){
            showAlert(Alert.AlertType.ERROR, "Errore generico", e.getMessage()); //gestione delle eccezioni
        }
    }
   
    @FXML
    void handleModifyUtente(ActionEvent event){
        Utente u = tabellaUtenti.getSelectionModel().getSelectedItem();
        
        if(u != null){
            try{

                //inizio della parte di codice per il caricamento della finestra per l'aggiunta di un nuovo libro
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI_GestioneUtenti/UtenteView.fxml"));
                Parent child = loader.load();

                //modifica della label di titolo e desc 
                Label lblTitolo = (Label) child.lookup("#lblTitolo");
                if (lblTitolo != null)
                    lblTitolo.setText("Modifica Utente");

                Label lblDesc = (Label) child.lookup("#lblDesc");
                if (lblDesc != null)
                    lblDesc.setText("Inserisci i dettagli dello studente da modificare.");

                Stage aggiungiUtenteStage = new Stage();
                aggiungiUtenteStage.setTitle("Modifica Utente");
                Scene sceneLibri = new Scene(child);
                aggiungiUtenteStage.setScene(sceneLibri);
                aggiungiUtenteStage.show();
                //fine 

                Button btnSalva = (Button) child.lookup("#btnSalva");
                Button btnAnnulla = (Button) child.lookup("#btnAnnulla");

                TextField nome= (TextField) child.lookup("#txtNome");
                nome.setText(u.getNome());
                TextField cognome = (TextField) child.lookup("#txtCognome");
                cognome.setText(u.getCognome());
                TextField matricola = (TextField) child.lookup("#txtMatricola");
                matricola.setText(u.getMatricola());
                matricola.setDisable(true);
                TextField email = (TextField) child.lookup("#txtEmail");
                email.setText(u.getEmailIstituzionale());
                
                //lambda expression per la registrazione del libro
                btnSalva.setOnAction(e -> {
                    try {
                        // Leggiamo i dati dai campi che abbiamo appena trovato

                        System.out.println("DEBUG DATI LETTI:");
                        System.out.println("ISBN letto: '" + nome.getText() + "'");
                        System.out.println("Titolo letto: '" + cognome.getText() + "'");

                        listaUtenti.modificaUtente(u, nome.getText(), cognome.getText() , email.getText());
                        System.out.println(listaUtenti.toString());
                        refreshTable();
                        aggiungiUtenteStage.close();
                    } catch (IOException ex) {
                        showAlert(Alert.AlertType.ERROR, "Errore generico3", ex.getMessage());
                    } catch (ClassNotFoundException ex) {
                        showAlert(Alert.AlertType.ERROR, "Errore generico4", ex.getMessage());
                    }
                });

                btnAnnulla.setOnAction(e -> { 
                    try {
                        aggiungiUtenteStage.close();
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
                    }
                });
            }catch(IOException e){
                showAlert(Alert.AlertType.ERROR, "Errore generico", e.getMessage()); //gestione delle eccezioni
            }
        }else
            showAlert(Alert.AlertType.ERROR, "Errore generico", "Utente non selezionato!");

    }
    
    /**
     * @brief Elimina l'utente selezionato
     * 
     * @pre Un utente deve essere selezionato
     * 
     * @post L'utente viene eliminato dalla lista utenti
     * 
     * @see #refreshTable() 
     *
     * @param[in] event L'evento di click.
     * 
     * @throws IOException se il path passato è errato.
     * @throws ClassNotFoundExcepiton se durante la deserializzazione la classe della lista salvato 
     * non corrisponde alla versione della classe locale.
     */    
    @FXML
    void handleDeleteUtente(ActionEvent event) throws IOException, ClassNotFoundException, UtenteNotFoundException, UtenteWithPrestitoException{
        Utente selected = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Nessuna Selezione", "Per favore, seleziona un utente dalla tabella per eliminarlo.");
            return; // Esce dal metodo subito
        }
        
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma eliminazione");
        conferma.setHeaderText("Sei sicuro di voler rimuovere il prestito selezionato?");
        
        Optional<ButtonType> risultato = conferma.showAndWait();
        
        if (risultato.isPresent() && risultato.get() == ButtonType.OK) {
            try{
                listaUtenti.eliminazioneUtente(selected);
                refreshTable();
            }catch(UtenteNotFoundException ex){
                showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
            }catch(UtenteWithPrestitoException ex){
                showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage()); //gestione delle eccezioni
            }
        }
    }
    
    /**
     * @brief Filtra la tabella in base al testo inserito nella barra di ricerca.
     * 
     * @param[in] event L'evento (es. pressione tasto invio o click su lente).
     */
   @FXML
    void handleCercaUtente(ActionEvent event) {
        String filtro = handleCercaUtente.getText(); 

        if (filtro == null || filtro.trim().isEmpty()) {
        // Qui devi ricaricare TUTTI i libri (es. dal tuo elenco completo)
            utenteList.setAll(listaUtenti.getListaUtenti()); 
        return;
        }
        
        try{
            ArrayList<Utente> risultati = listaUtenti.cercaUtente(filtro);
            utenteList.setAll(risultati);
        }catch (UtenteNotFoundException e) {
            utenteList.clear();
        }
        System.out.println("Ricerca libro effettuata per: " + filtro);
    }

    /**
     * @brief Ordina la lista utenti in ordine alfabetico per Cognome.
     *
     * @post La visualizzazione della tabella è ordinata A-Z per cognome.
     *
     * @param[in] event L'evento di click.
     */
    @FXML
    void handleSortCognome(ActionEvent event) {}
    /*
    void handleSortCognome(ActionEvent event) {
        //richiama il metodo sort e ordina il catalogo libri in base al codice ISBN
        //e aggiorna la vista del catalogo
        colCognome.setSortable(true);
        // 1. Controlla se stiamo già ordinando per questa colonna
        if (tabellaUtenti.getSortOrder().contains(colCognome)) {
            // Se sì, inverti l'ordine (da ASC a DESC o viceversa)
            if (colCognome.getSortType() == TableColumn.SortType.ASCENDING) {
                colCognome.setSortType(TableColumn.SortType.DESCENDING);
                tabellaUtenti.getSortOrder().clear();
                tabellaUtenti.getSortOrder().add(colCognome);
                tabellaUtenti.sort();
            }else if (colCognome.getSortType() == TableColumn.SortType.DESCENDING){
                colIsbn.setSortable(true);
                colIsbn.setSortType(TableColumn.SortType.ASCENDING);
                tabellaUtenti.getSortOrder().clear();
                tabellaUtenti.getSortOrder().add(colIsbn);
                tabellaUtenti.sort();
                colIsbn.setSortable(false);
            }
        } else {
            colTitolo.setSortType(TableColumn.SortType.ASCENDING);
            tabellaLibri.getSortOrder().clear();
            tabellaLibri.getSortOrder().add(colTitolo);
            tabellaLibri.sort();
        }
        colTitolo.setSortable(false);
    }*/

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
    // Ordina l'ObservableList in base alla data di registrazione decrescente
        colDataReg.setSortable(true);
        colCognome.setSortable(true); 

        // 2. Verifichiamo lo stato attuale: 
        // La tabella è GIÀ ordinata per Data?
        boolean isSortedByDate = tabellaUtenti.getSortOrder().contains(colDataReg);

        if (isSortedByDate) {
            // --- CASO RESET: Era ordinato per Data, torniamo a COGNOME ---
            colCognome.setSortType(TableColumn.SortType.ASCENDING);

            tabellaUtenti.getSortOrder().clear();
            tabellaUtenti.getSortOrder().add(colCognome);

        } else {
            // --- CASO ATTIVAZIONE: Non era ordinato per Data, ordiniamo per DATA CRESCENTE ---
            // Nota: Crescente (ASCENDING) mette le date più vecchie in alto.
            colDataReg.setSortType(TableColumn.SortType.ASCENDING);

            tabellaUtenti.getSortOrder().clear();
            tabellaUtenti.getSortOrder().add(colDataReg);
        }

        // 3. Applica l'ordinamento
        tabellaUtenti.sort();

        // 4. Riblocca le intestazioni per l'utente
        colDataReg.setSortable(false);
        colCognome.setSortable(false);
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
        colDataReg.setSortable(true);
        colCognome.setSortable(true); 

        // 2. Verifichiamo lo stato attuale: 
        // La tabella è GIÀ ordinata per Data?
        boolean isSortedByDate = tabellaUtenti.getSortOrder().contains(colDataReg);

        if (isSortedByDate) {
            // --- CASO RESET: Era ordinato per Data, torniamo a COGNOME ---
            colCognome.setSortType(TableColumn.SortType.ASCENDING);

            tabellaUtenti.getSortOrder().clear();
            tabellaUtenti.getSortOrder().add(colCognome);

        } else {
            // --- CASO ATTIVAZIONE: Non era ordinato per Data, ordiniamo per DATA CRESCENTE ---
            // Nota: Crescente (ASCENDING) mette le date più vecchie in alto.
            colDataReg.setSortType(TableColumn.SortType.DESCENDING);

            tabellaUtenti.getSortOrder().clear();
            tabellaUtenti.getSortOrder().add(colDataReg);
        }

        // 3. Applica l'ordinamento
        tabellaUtenti.sort();

        // 4. Riblocca le intestazioni per l'utente
        colDataReg.setSortable(false);
        colCognome.setSortable(false);
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
