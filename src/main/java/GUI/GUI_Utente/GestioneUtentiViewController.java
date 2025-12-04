/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Utente;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author mello
 */
public class GestioneUtentiViewController implements Initializable {

    @FXML
    private Button handleLogout;
    @FXML
    private Button handleAggiungiUtente;
    @FXML
    private Button handleSortCognome;
    @FXML
    private Button handleSortMostRecent;
    @FXML
    private Button handleSortLatestRecent;
    @FXML
    private TextField handleCercaUtente;
    @FXML
    private TableView<?> tabellaUtenti;
    @FXML
    private TableColumn<?, ?> colTessera;
    @FXML
    private TableColumn<?, ?> colNome;
    @FXML
    private TableColumn<?, ?> colCognome;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colNPrestitiAttivi;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
