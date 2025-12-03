/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Login;

import GUI.GUI_Login.ControllerLogin;
import java.awt.Label;
import java.awt.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

/**
 *
 * @author jackross
 */
public class ViewLogin {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblErrore;

    private ControllerLogin mainController;

    // Metodo fondamentale per l'Injection
    public void setMainController(ControllerLogin controller) {
        this.mainController = controller;
    }

    @FXML
    private void handleLogin() {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        if (mainController != null) {
            boolean successo = mainController.tentaLogin(user, pass);
            
            if (!successo) {
                lblErrore.setText("Credenziali errate!");
                lblErrore.setVisible(true);
            }
        }
    }
}
