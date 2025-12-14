package GUI.GUI_Prestito;

import Eccezioni.EccezioniLibri.LibroNotFoundException;
import Eccezioni.EccezioniPrestiti.EccezioniPrestito;
import Eccezioni.EccezioniPrestiti.PrestitoNonTrovatoException;
import Eccezioni.EccezioniPrestiti.dataRestituzioneException;
import Eccezioni.EccezioniUtenti.UtenteNotFoundException;
import GestioneLibro.CatalogoLibri;
import GestioneLibro.Libro;
import GestionePrestito.ElencoPrestiti;
import GestionePrestito.GestorePrestito;
import GestionePrestito.Prestito;
import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import SalvataggioFile.SalvataggioFileLibro.SalvataggioFileLibro;
import SalvataggioFile.SalvataggioFilePrestito.SalvataggioFilePrestito;
import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
    //@FXML
    //private Button handleSortLatestRecent;
    
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
    private TableColumn<Prestito, String> colLibro;
    @FXML
    private TableColumn<Prestito, String> colUtente;
    @FXML
    private TableColumn<Prestito, String> colDataRegistrazione;
    @FXML
    private TableColumn<Prestito, LocalDate> colDataScadenza;
    
    private ObservableList<Prestito> prestitoList;
    private ElencoPrestiti elencoPrestiti;
    private GestorePrestito gestorePrestito;
    private FilteredList<Prestito> filteredData;
    
    private ListaUtenti lista;
    private CatalogoLibri catalogo;
    
    private String fileNamePrestiti ="elencoPrestiti.bin";
    private String fileNameLibri = "catalogoLibri.bin";
    private String fileNameUtenti = "listaUtenti.bin";

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
        try {
            gestorePrestito = new GestorePrestito(fileNameLibri, fileNameUtenti);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore Critico!", ex.getMessage());
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore Critico!", ex.getMessage());
            System.exit(0);
        }
        try {
            elencoPrestiti= new ElencoPrestiti(true, fileNamePrestiti, gestorePrestito);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore Critico!", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore Critico!", ex.getMessage());
        }
        
        prestitoList = FXCollections.observableArrayList(elencoPrestiti.getElencoPrestiti());
        filteredData = new FilteredList<>(prestitoList, p -> true);
        tabellaPrestiti.setItems(prestitoList);
        SortedList<Prestito> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabellaPrestiti.comparatorProperty());
        
        try {
            catalogo = SalvataggioFileLibro.carica(fileNameLibri);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
        }
        
        try {
            lista = SalvataggioFileUtente.carica(fileNameUtenti);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
        }
        
        colLibro.setCellValueFactory(new PropertyValueFactory<>("ISBNLibro"));
        colUtente.setCellValueFactory(new PropertyValueFactory<>("matricolaUtente"));
        colDataRegistrazione.setCellValueFactory(new PropertyValueFactory<>("dataRegistrazione"));
        colDataScadenza.setCellValueFactory(new PropertyValueFactory<>("dataRestituzione"));
        
        //colStato.setCellValueFactory(new PropertyValueFactory<>(""));

        colLibro.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<Prestito, String>() {
                @Override
                protected void updateItem(String isbn, boolean empty) {
                    super.updateItem(isbn, empty);

                    if (isbn == null || empty) {
                        setText(null);
                        setTooltip(null); // Rimuove il tooltip se la cella è vuota
                    } else {
                        // Imposta il testo della cella (Solo ISBN)
                        setText(isbn);

                        // Logica per trovare i dettagli del libro
                        String dettagliLibro = "Libro non trovato nel catalogo.";
                        if (catalogo != null) {
                            for (Libro l : catalogo.getCatalogoLibri()) {
                                if (l.getIsbn().equals(isbn)) {
                                    // Qui puoi mettere tutte le info che vuoi nel fumetto giallo
                                    dettagliLibro = l.getTitolo() + "\n" + 
                                                    "Autore: " + l.getAutori() + "\n";
                                    break;
                                }
                            }
                        }

                        // Crea il Tooltip e lo installa sulla cella
                        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(dettagliLibro);

                        // Opzionale: rende il tooltip istantaneo (senza ritardo)
                        tooltip.setShowDelay(javafx.util.Duration.millis(100)); 

                        setTooltip(tooltip);
                    }
                }
            };
        });

        colUtente.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<Prestito, String>() {
                @Override
                protected void updateItem(String matricola, boolean empty) {
                    super.updateItem(matricola, empty);

                    if (matricola == null || empty) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(matricola);

                        String dettagliUtente = "Utente sconosciuto";
                        if (lista != null) {
                            for (Utente u : lista.getListaUtenti()) {
                                if (u.getMatricola().equals(matricola)) {
                                    dettagliUtente = u.getNome() + " " + u.getCognome() + "\n" + 
                                                     u.getEmailIstituzionale();
                                    break;
                                }
                            }
                        }
                        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(dettagliUtente);
                        setTooltip(tooltip);
                    }
                }
            };
        });

        tabellaPrestiti.setRowFactory(tv -> {
    
            // Definiamo la riga sovrascrivendo updateItem per colorarla SUBITO all'avvio
            TableRow<Prestito> row = new TableRow<Prestito>() {
                @Override
                protected void updateItem(Prestito item, boolean empty) {
                    super.updateItem(item, empty);
                    // Richiamiamo la logica di stile ogni volta che la riga viene disegnata
                    aggiornaStileRiga(this);
                }
            };

            // Aggiungiamo ANCHE un listener per quando la riga viene selezionata/deselezionata
            row.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                aggiornaStileRiga(row);
            });

            return row;
        });
        
        
        //no sorting 
        colLibro.setSortable(false);
        colUtente.setSortable(false);
        colDataScadenza.setSortable(false);
        colDataRegistrazione.setSortable(false);
        try {
            refreshTable();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
        }
    
    }

    private void aggiornaStileRiga(TableRow<Prestito> row) {
        Prestito prestito = row.getItem();

        // 1. Se la riga è vuota -> Nessuno stile
        if (prestito == null || row.isEmpty()) {
            row.setStyle("");
            return;
        }

        // 2. Se la riga è SELEZIONATA -> Stile di default (Blu/Azzurro di JavaFX)
        if (row.isSelected()) {
            row.setStyle(""); 
            return;
        }

        // 3. Altrimenti -> Calcola Colore (Rosso/Giallo)
        LocalDate scadenza = prestito.getDataRestituzione();
        LocalDate oggi = LocalDate.now();
        long giorni = java.time.temporal.ChronoUnit.DAYS.between(oggi, scadenza);

        if (giorni < 0) {
            // SCADUTO: Rosso
            row.setStyle("-fx-background-color: #ffcccc;"); 
        } else if (giorni <= 3) {
            // IN SCADENZA: Giallo
            row.setStyle("-fx-background-color: #ffffc4;"); 
        } else {
            // NORMALE: Bianco/Default
            row.setStyle("");
        }
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
    void switchScene(ActionEvent event, String fxmlPath) throws IOException{
        //permette di cambiare scena in base al pulsante cliccato e al path fornito in fxmlPath
        //si potrebbe effettuare un salvataggio dei dati prima del passaggio
        //scheletro
        SalvataggioFilePrestito.salva(elencoPrestiti, fileNamePrestiti);

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Scene stageAttuale = ((Node) event.getSource()).getScene();       
            stageAttuale.setRoot(root);
        }catch(IOException e){
            showAlert(Alert.AlertType.ERROR,"Errore Critico!","Errore nel caricamento della Scena: " + e.getMessage());
        }
    }
    
    /**
     * @brief Aggiorna la tabella dei prestiti sincronizzandola con l'elenco.
     *
     * Questo metodo svuota la lista visualizzata nella TableView e la ripopola
     * recuperando tutti i prestiti presenti nell'elenco. Serve per riflettere
     * visivamente eventuali modifiche (come nuove aggiunte o rimozioni).
     * Inoltre, questo metodo salva le operazioni effettuate e ricarica l'elenco e di conseguenza la tabella
     *
     * @post La lista visibile (prestitiList) contiene esattamente gli elementi attuali di elencoPrestiti.
     * 
     * @throws IOException se il path passato è errato.
     * @throws ClassNotFoundExcepiton se durante la deserializzazione la classe dell'elenco salvato 
     * non corrisponde alla versione della classe locale.
     */    
    @FXML
    void refreshTable() throws IOException, ClassNotFoundException{
        prestitoList.clear(); // 1. Cancella i dati vecchi dalla vista
        //catalogoLibri = SalvataggioFileLibro.carica(filename);
        prestitoList.addAll(elencoPrestiti.getElencoPrestiti());
        colDataScadenza.setSortable(true);
        colDataScadenza.setSortType(TableColumn.SortType.ASCENDING);
        tabellaPrestiti.getSortOrder().clear();
        tabellaPrestiti.getSortOrder().add(colDataScadenza);
        tabellaPrestiti.sort();
        colDataScadenza.setSortable(false);
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
     * @brief Passa alla schermata di Gestione Utenti.
     * 
     * @see #switchScene(ActionEvent, String)
     */
    @FXML
    void handleGestioneUtenti(ActionEvent event) throws IOException {
        //permette di passare alla schermata per la gesione degli utenti
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_GestioneUtenti/GestioneUtentiView.fxml");
    }
    
    /**
     * @brief Effettua il logout dal sistema.
     * 
     * @post Ritorna alla schermata di Login.
     */
    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        //permette di passare alla schermata del login
        //da implemetare con switchScene
        //scheletro
        switchScene(event, "/GUI/GUI_Login/LoginView.fxml");
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
    void handleAggiungiPrestito(ActionEvent event) throws ClassNotFoundException{
        //chiama un metodo che permette di aggiungere un prestito nella
        //lista dei prestiti e aggiorna la vista del catalogo
        //scheletro
        try{
            
            //inizio della parte di codice per il caricamento della finestra per l'aggiunta di un nuovo libro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI_Prestiti/PrestitoView.fxml"));
            Parent child = loader.load();
            
            //modifica della label di titolo e desc 
            Label lblTitolo = (Label) child.lookup("#lblTitolo");
            if (lblTitolo.getText() != null)
                lblTitolo.setText("Nuovo Prestito");
            
            Label lblDesc = (Label) child.lookup("#lblDesc");
            if (lblDesc.getText() != null)
                lblDesc.setText("Inserisci i dettagli del prestito da aggiungere all'elenco.");
            Label lblData= (Label) child.lookup("#lblData");
            lblData.setVisible(false);
            TextField txtData= (TextField) child.lookup("#txtData");
            txtData.setVisible(false);
            
            ComboBox<String> cmbMatricola = (ComboBox<String>) child.lookup("#cmbMatricola");
            ComboBox<String> cmbISBN = (ComboBox<String>) child.lookup("#cmbISBN");

            ArrayList<String> listaCodiciLibri = new ArrayList<>();
            for(Libro l : catalogo.getCatalogoLibri()) { // Ipotizzo tu abbia accesso alla lista libri
                listaCodiciLibri.add(l.getIsbn() + " - " + l.getTitolo());
            }
            cmbISBN.getItems().addAll(listaCodiciLibri);
            cmbISBN.setEditable(true);
            
            ArrayList<String> listaMatricole = new ArrayList<>();
            for(Utente u : lista.getListaUtenti()) {
                listaMatricole.add(u.getMatricola() + " - " + u.getNome() + " " + u.getCognome());
            }
            cmbMatricola.getItems().addAll(listaMatricole);
            cmbMatricola.setEditable(true);
            
            TextField isbn = (TextField) child.lookup("#txtISBN");
            TextField matricola = (TextField) child.lookup("#txtMatricola");
            isbn.setVisible(false);
            matricola.setVisible(false);
                        
            Stage aggiungiPrestitoStage = new Stage();
            aggiungiPrestitoStage.setTitle("Aggiungi Nuovo Prestito");
            Scene sceneLibri = new Scene(child);
            aggiungiPrestitoStage.setScene(sceneLibri);
            aggiungiPrestitoStage.show();
            javafx.application.Platform.runLater(() -> {
                child.requestFocus(); 
            });
            //fine 
            
            Button btnSalva = (Button) child.lookup("#btnSalva");
            Button btnAnnulla = (Button) child.lookup("#btnAnnulla");
            
            //lambda expression per la registrazione del libro
            btnSalva.setOnAction(e -> {
                try {
                    // Leggiamo i dati dai campi che abbiamo appena trovato
                    
                    String selezioneLibro = cmbISBN.getValue();
                    String selezioneUtente = cmbMatricola.getValue();
                    
                    String isbnPuro = selezioneLibro.split(" - ")[0].trim();
                    String matricolaPura = selezioneUtente.split(" - ")[0].trim();
                    
                    elencoPrestiti.registrazionePrestito(isbnPuro, matricolaPura);
                    System.out.println(elencoPrestiti.toString());
                    refreshTable();
                    aggiungiPrestitoStage.close();
                } catch (LibroNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
                } catch (UtenteNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
                } catch (EccezioniPrestito ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
                }
            });
            
            btnAnnulla.setOnAction(e -> { 
                try {
                    aggiungiPrestitoStage.close();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getMessage()); //gestione delle eccezioni
                }
            }); 
        }catch(IOException e){
            showAlert(Alert.AlertType.ERROR, "Errore generico", e.getMessage()); //gestione delle eccezioni
        }   
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
        Prestito selected = tabellaPrestiti.getSelectionModel().getSelectedItem();
        
        if(selected != null){
            try{
                //inizio della parte di codice per il caricamento della finestra per l'aggiunta di un nuovo libro
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI_Prestiti/PrestitoView.fxml"));
                Parent child = loader.load();

                //modifica della label di titolo e desc 
                Label lblTitolo = (Label) child.lookup("#lblTitolo");
                if (lblTitolo.getText() != null)
                    lblTitolo.setText("Modifica Prestito");

                Label lblDesc = (Label) child.lookup("#lblDesc");
                if (lblDesc.getText() != null)
                    lblDesc.setText("Inserisci i dettagli del prestito da modificare all'elenco.");
                
                Label lblData= (Label) child.lookup("#lblData");
                lblData.setVisible(true);
                TextField dataRestituzione = (TextField) child.lookup("#txtData");
                dataRestituzione.setVisible(true);
                dataRestituzione.setText(String.valueOf(selected.getDataRestituzione()));
                ComboBox<String> cmbMatricola = (ComboBox<String>) child.lookup("#cmbMatricola");
                ComboBox<String> cmbISBN = (ComboBox<String>) child.lookup("#cmbISBN");
                cmbISBN.setVisible(false);
                cmbMatricola.setVisible(false);

                Stage aggiungiPrestitoStage = new Stage();
                aggiungiPrestitoStage.setTitle("Modifica Prestito");
                Scene sceneLibri = new Scene(child);
                aggiungiPrestitoStage.setScene(sceneLibri);
                aggiungiPrestitoStage.show();
                javafx.application.Platform.runLater(() -> {
                child.requestFocus(); 
            });
                //fine 
                
                TextField isbn = (TextField) child.lookup("#txtISBN");
                isbn.setText(selected.getISBNLibro());
                TextField matricola = (TextField) child.lookup("#txtMatricola");
                matricola.setText(selected.getMatricolaUtente());
                isbn.setVisible(true);
                matricola.setVisible(true);
                isbn.setDisable(true);
                matricola.setDisable(true);
                Button btnSalva = (Button) child.lookup("#btnSalva");
                Button btnAnnulla = (Button) child.lookup("#btnAnnulla");

                //lambda expression per la registrazione del libro
                btnSalva.setOnAction(e -> {
                    try {
                        // Leggiamo i dati dai campi che abbiamo appena trovato
                        System.out.println("DEBUG DATI LETTI:");
                        System.out.println("ISBN letto: '" + isbn.getText() + "'");
                        System.out.println("Titolo letto: '" + matricola.getText() + "'");
                        System.out.println("Data letto: '" + dataRestituzione.getText() + "'");

                        elencoPrestiti.modificaPrestito(selected, LocalDate.parse(dataRestituzione.getText()));
                        System.out.println(elencoPrestiti.toString());
                        refreshTable();
                        aggiungiPrestitoStage.close();
                    } catch (EccezioniPrestito ex) {
                        showAlert(Alert.AlertType.ERROR, "Errore generico1", ex.getClass().getName() + " " + ex.getMessage());
                    } catch (IOException ex) {
                        showAlert(Alert.AlertType.ERROR, "Errore generico2", ex.getClass().getName() + " " + ex.getMessage());
                    } catch (ClassNotFoundException ex) {
                        showAlert(Alert.AlertType.ERROR, "Errore generico3", ex.getClass().getName() + " " + ex.getMessage());
                    }
                });

                btnAnnulla.setOnAction(e -> { 
                try {
                    aggiungiPrestitoStage.close();
                } catch (Exception ex) {
                    System.out.println("Errore generico: " + ex.getMessage());
                }
            });
            }catch(IOException e){
                showAlert(Alert.AlertType.ERROR, "Errore generico", e.getMessage()); //gestione delle eccezioni
            }
        }else{
            showAlert(Alert.AlertType.ERROR, "Errore generico", "Prestito non selezionato!");
        }
           
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
    void handleRemovePrestito(ActionEvent event) throws IOException, ClassNotFoundException{
        //permette di rimuovere il prestito selezionato tramite handleSelectedLibro
        //scheletro
        Prestito selected = tabellaPrestiti.getSelectionModel().getSelectedItem();  
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Nessuna Selezione", "Per favore, seleziona un prestito dalla tabella per eliminarlo.");
            return; // Esce dal metodo subito
        }
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma eliminazione");
        conferma.setHeaderText("Sei sicuro di voler rimuovere il prestito selezionato?");
        
        Optional<ButtonType> risultato = conferma.showAndWait();
        
        if (risultato.isPresent() && risultato.get() == ButtonType.OK) {
            try{   
                elencoPrestiti.eliminazionePrestito(selected);
                refreshTable();
            }catch(PrestitoNonTrovatoException ex){
                showAlert(Alert.AlertType.ERROR, "Errore generico", ex.getClass().getName() + " " + ex.getMessage());
            }
        }
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
        
        // CIAO SONO GIACOMO, IL TREESET DEL PRESTITO DI BASE NON ORDINA PER DATA DI RESTITUZIONE 
        // PERCHE SENNO MI SFANCULAVA I DUPLICATI. TE LO DEVI IMPLEMEMTARE DA TE.
        colDataScadenza.setSortable(true);
        // 1. Controlla se stiamo già ordinando per questa colonna
            // Se sì, inverti l'ordine (da ASC a DESC o viceversa)
            if (colDataScadenza.getSortType() == TableColumn.SortType.ASCENDING) {
                colDataScadenza.setSortType(TableColumn.SortType.DESCENDING);
                tabellaPrestiti.getSortOrder().clear();
                tabellaPrestiti.getSortOrder().add(colDataScadenza);
                tabellaPrestiti.sort();
            }else if (colDataScadenza.getSortType() == TableColumn.SortType.DESCENDING){
                colDataScadenza.setSortType(TableColumn.SortType.ASCENDING);
                tabellaPrestiti.getSortOrder().clear();
                tabellaPrestiti.getSortOrder().add(colDataScadenza);
                tabellaPrestiti.sort();
            }
        colDataScadenza.setSortable(false);
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
        colDataRegistrazione.setSortable(true);
        // 1. Controlla se stiamo già ordinando per questa colonna
        if (tabellaPrestiti.getSortOrder().contains(colDataRegistrazione)) {
            // Se sì, inverti l'ordine (da ASC a DESC o viceversa)
            if (colDataRegistrazione.getSortType() == TableColumn.SortType.ASCENDING) {
                colDataRegistrazione.setSortType(TableColumn.SortType.DESCENDING);
                tabellaPrestiti.getSortOrder().clear();
                tabellaPrestiti.getSortOrder().add(colDataRegistrazione);
                tabellaPrestiti.sort();
            }else if (colDataRegistrazione.getSortType() == TableColumn.SortType.DESCENDING){
                colDataScadenza.setSortable(true);
                colDataScadenza.setSortType(TableColumn.SortType.ASCENDING);
                tabellaPrestiti.getSortOrder().clear();
                tabellaPrestiti.getSortOrder().add(colDataScadenza);
                tabellaPrestiti.sort();
                colDataScadenza.setSortable(false);
            }
        } else {
            colDataRegistrazione.setSortType(TableColumn.SortType.ASCENDING);
            tabellaPrestiti.getSortOrder().clear();
            tabellaPrestiti.getSortOrder().add(colDataRegistrazione);
            tabellaPrestiti.sort();
        }
        colDataRegistrazione.setSortable(false);
    }
    
    /**
     * @brief Filtra la tabella in base al testo inserito nella barra di ricerca.
     * 
     * @param[in] event L'evento (es. pressione tasto invio o click su lente).
     */
   @FXML
    void handleCercaPrestito(ActionEvent event) {
        String filtro = handleCercaPrestito.getText(); 

        if (filtro == null || filtro.trim().isEmpty()) {
        // Qui devi ricaricare TUTTI i libri (es. dal tuo elenco completo)
            prestitoList.setAll(elencoPrestiti.getElencoPrestiti()); 
        return;
        }
        
        try{
            ArrayList<Prestito> risultati = elencoPrestiti.cercaPrestito(filtro);
            prestitoList.setAll(risultati);
        }catch (PrestitoNonTrovatoException e) {
            prestitoList.clear();
        }
        System.out.println("Ricerca libro effettuata per: " + filtro);
    }
    
    /**
     * @brief Ordina i prestiti dal meno recente al più recente (Oldest First).
     *
     * Basato sulla data di inizio prestito.
     *
     * @post I prestiti più vecchi appaiono in cima alla lista.
     
    @FXML
    void handleSortLatestRecent(ActionEvent event){
        //permette di ordinare la lista dei prestiti dal meno recente
        //scheletro
    }
    * */
    
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