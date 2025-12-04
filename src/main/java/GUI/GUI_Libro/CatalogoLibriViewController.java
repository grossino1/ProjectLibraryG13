/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Libro;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author mello
 */
public class CatalogoLibriViewController implements Initializable {

    @FXML
    private Button btnLogout;
    @FXML
    private Button handleAggiungiLibro;
    @FXML
    private Button handleSortTitolo;
    @FXML
    private Button handleSortAnno;
    @FXML
    private TableView<?> tabellaLibri;
    @FXML
    private TableColumn<?, ?> colIsbn;
    @FXML
    private TableColumn<?, ?> colTitolo;
    @FXML
    private TableColumn<?, ?> colAutore;
    @FXML
    private TableColumn<?, ?> colAnno;
    @FXML
    private TableColumn<?, ?> colDisponibile;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
