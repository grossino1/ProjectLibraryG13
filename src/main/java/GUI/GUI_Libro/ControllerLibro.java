/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Libro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 *
 * @author chiara
 */
public class ControllerLibro {
    
    @FXML
    private void initialize(){
        //scheletro del metodo
    }
    
    @FXML
    void handleAggiungiLibro(ActionEvent event){
        //chiama un metodo che permette di aggiungere un libro nel catalogo dei 
        //libri e aggiorna la vista del catalogo
        //scheletro
    }
    
    @FXML
    void handleSortLibro(ActionEvent event){
        //richiama il metodo sort e ordina il catalogo libri in base al codice ISBN
        //e aggiorna la vista del catalogo
        //scheletro
    }
    
    @FXML
    void handleSortAnno(ActionEvent event){
        //richiama il metodo sort e ordina il catalogo libri in base all'anno di pubblicazione
        //e aggiorna la vista del catalogo
        //scheletro
    }
    
    @FXML 
    void switchScene(ActionEvent event, String fxmlPath){
        //permette di cambiare scena in base al pulsante cliccato e al path fornito in fxmlPath
        //si potrebbe effettuare un salvataggio dei dati prima del passaggio
        //scheletro
    }
    
    @FXML
    void handlePrestiti(ActionEvent event) {
        //permette di passare alla schermata dei prestiti
        //da implemetare con switchScene
        //scheletro
    }
    
    @FXML
    void handleGestioneUtenti(ActionEvent event) {
        //permette di passare alla schermata per la gesione degli utenti
        //da implemetare con switchScene
        //scheletro
    }
    
    @FXML
    void handleSelectedLibro(ActionEvent event){
        //permette di selezionare un libro e far apparire delle icone per la modifica del libro, aggiunta e rimozione di una copia
        //scheletro
    }
    
    @FXML
    void handleAddCopyLibro(ActionEvent event){
        //permette di aggiungere una copia del libro selezionato tramite handleSelectedLibro
        //scheletro
    }
    
    @FXML
    void handleRemoveCopyLibro(ActionEvent event){
        //permette di rimuovere una copia del libro selezionato tramite handleSelectedLibro
        //controllo per quanto riguarda presenza di 1 sola copia
        //scheletro
    }
    
    @FXML
    void handleModifyLibro(ActionEvent event){
        //permette di modificare il libro selezionato tramite handleSelectedLibro
        //scheletro
    }

    @FXML
    void handleLogout(ActionEvent event) {
        //permette di passare alla schermata del login
        //da implemetare con switchScene
        //scheletro
    }
    
    private void showError(String msg){
        //crea un alert
    }
}
