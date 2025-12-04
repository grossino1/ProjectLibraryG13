/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Utente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 *
 * @author chiara
 */
public class ControllerUtente {
    @FXML
    private void initialize(){
        //scheletro del metodo
    }
    @FXML
    void handleAggiungiUtente(ActionEvent event){
        //chiama un metodo che permette di aggiungere un utente nella
        //lista degli utenti e aggiorna la vista del catalogo
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
    void handlePrestiti(ActionEvent event) {
        //permette di passare alla schermata per la gesione dei prestiti
        //da implemetare con switchScene
        //scheletro
    }
    
    @FXML
    void handleModifyUtente(ActionEvent event){
        //permette di modificare l'utente selezionato tramite handleSelectedLibro
        //scheletro
    }
    
    @FXML
    void handleRemoveUtente(ActionEvent event){
        //permette di rimuovere l'utente selezionato tramite handleSelectedLibro
        //scheletro
    }
    
    @FXML
    void handleLogout(ActionEvent event) {
        //permette di passare alla schermata del login
        //da implemetare con switchScene
        //scheletro
    }
    
    void handleSortCognome(ActionEvent event){
        //permette di ordinare la lista degli utenti per cognome
        //scheletro
    }
    
    void handleSortMostRecent(ActionEvent event){
        //permette di ordinare la lista degli utenti 
        //scheletro
    }
    
    void handleSortLatestRecent(ActionEvent event){
        //permette di ordinare la lista degli utenti 
        //scheletro
    }
    
    private void showError(String msg){
        //crea un alert
    }
    
}
