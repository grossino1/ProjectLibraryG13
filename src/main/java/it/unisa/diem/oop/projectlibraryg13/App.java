/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.diem.oop.projectlibraryg13;

/**
 *
 * @author jackross
 */

import GUI.GUI_Login.ControllerLogin;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Creiamo il coordinatore passandogli la finestra principale
        ControllerLogin mainController = new ControllerLogin(primaryStage);
        
        // Avviamo l'applicazione mostrando il login
        mainController.avvia();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

