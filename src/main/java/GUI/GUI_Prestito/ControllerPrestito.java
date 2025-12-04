/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Prestito;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

/**
 *
 * @author chiara
 */
public class ControllerPrestito {
    @FXML
    private void initialize(){
        //scheletro del metodo
    }
    
    @FXML
    void handleAggiungiPrestito(ActionEvent event){
        //chiama un metodo che permette di aggiungere un prestito nella
        //lista dei prestiti e aggiorna la vista del catalogo
        //scheletro
    }
    
    @FXML 
    void switchScene(ActionEvent event, String fxmlPath){
        //permette di cambiare scena in base al pulsante cliccato e al path fornito in fxmlPath
        //si potrebbe effettuare un salvataggio dei dati prima del passaggio
        //scheletro
    }
    
    @FXML
    void handleCatalogoLibri(ActionEvent event) {
        //permette di passare alla schermata del catalogo dei libri
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
    void handleModifyPrestito(ActionEvent event){
        //permette di modificare il prestito selezionato tramite handleSelectedLibro
        //scheletro
    }
    
    @FXML
    void handleRemovePrestito(ActionEvent event){
        //permette di rimuovere il prestito selezionato tramite handleSelectedLibro
        //scheletro
    }
    
    @FXML
    void handleLogout(ActionEvent event) {
        //permette di passare alla schermata del login
        //da implemetare con switchScene
        //scheletro
    }
    
    void handleSortReturnDate(ActionEvent event){
        //permette di ordinare in base alla data di restituzione
        //scheletro
    }
    
    void handleSortMostRecent(ActionEvent event){
        //permette di ordinare la lista dei prestiti dal più recente
        //scheletro
    }
    
    void handleSortLatestRecent(ActionEvent event){
        //permette di ordinare la lista dei prestiti dal meno recente
        //scheletro
    }
    
    
    
    private void showError(String msg){
        //crea un alert
    }
}
