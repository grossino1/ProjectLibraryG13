/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GestioneLibro.Libro;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author jackross
 */
public class MainDashboard {

    
    @FXML private TableView<Libro> tableLibri;
    @FXML private TableColumn<Libro, String> colTitolo;
    @FXML private TableColumn<Libro, String> colAutore;
    @FXML private TableColumn<Libro, String> colISBN;
    @FXML private TableColumn<Libro, Integer> colCopie;

    private Controller mainController;

    // Metodo chiamato automaticamente da JavaFX dopo il caricamento dell'FXML
    @FXML
    public void initialize() {
        // Configuriamo le colonne per leggere gli attributi della classe Libro
        // I nomi nelle stringhe devono corrispondere ai getter (getTitolo -> "titolo")
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colCopie.setCellValueFactory(new PropertyValueFactory<>("numeroCopie"));
    }

    public void setMainController(Controller controller) {
        this.mainController = controller;
        aggiornaTabelle(); // Appena colleghiamo il controller, carichiamo i dati
    }

    private void aggiornaTabelle() {
        if (mainController != null) {
            // Recuperiamo l'ArrayList dal modello
            ArrayList<Libro> listaLibri = mainController.getCatalogoLibri().getCatalogoLibri();
            
            // Convertiamo in ObservableList per JavaFX
            ObservableList<Libro> datiOsservabili = FXCollections.observableArrayList(listaLibri);
            
            // Inseriamo nella tabella
            tableLibri.setItems(datiOsservabili);
        }
    }

    @FXML
    private void handleLogout() {
        mainController.logout();
    }
    
    @FXML
    private void handleAggiungiLibro() {
        // Qui apriresti un Dialog, raccoglieresti i dati e chiameresti:
        // mainController.getCatalogoLibri().registrazioneLibro(nuovoLibro);
        // aggiornaTabelle();
    }
}
