/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Prestito;

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
public class GestionePrestitiViewController implements Initializable {

    @FXML
    private Button handleLogout;
    @FXML
    private Button handleNuovoPrestito;
    @FXML
    private Button handleSortReturnData;
    @FXML
    private Button handleSortMostRecent;
    @FXML
    private Button handleSortLatestRecent;
    @FXML
    private TextField handleCercaPrestito;
    @FXML
    private Button filterScaduti;
    @FXML
    private TableView<?> tabellaPrestiti;
    @FXML
    private TableColumn<?, ?> colIdPrestito;
    @FXML
    private TableColumn<?, ?> colLibro;
    @FXML
    private TableColumn<?, ?> colUtente;
    @FXML
    private TableColumn<?, ?> colDataInizio;
    @FXML
    private TableColumn<?, ?> colDataScadenza;
    @FXML
    private TableColumn<?, ?> colStato;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
