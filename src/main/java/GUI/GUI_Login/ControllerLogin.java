/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUI_Login;

import Autentificazione.Bibliotecario;
import GUI.MainDashboard;
import GestioneLibro.CatalogoLibri;
import GestionePrestito.GestorePrestito;
import GestioneUtente.ListaUtenti;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ControllerLogin {

    // Riferimento alla finestra (Stage) per cambiare scene
    private final Stage stage;

    // --- IL MODELLO (Logica di Business) ---
    private Bibliotecario bibliotecario;
    private CatalogoLibri catalogoLibri;
    private ListaUtenti listaUtenti;
    private GestorePrestito gestorePrestito;

    public ControllerLogin(Stage stage) {
        this.stage = stage;
        inizializzaModello();
    }

    private void inizializzaModello() {
        // Qui caricheresti i dati da file, per ora istanziamo a vuoto
        this.bibliotecario = new Bibliotecario("admin", "1234");
        this.catalogoLibri = new CatalogoLibri();
        this.listaUtenti = new ListaUtenti();
        this.gestorePrestito = new GestorePrestito();
    }

    // --- NAVIGAZIONE ---
    public void avvia() {
        mostraLoginView();
    }

    public void mostraLoginView() {
        try {
            // Carica l'FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent root = loader.load();

            // Ottieni il controller della vista e passagli QUESTO controller centrale
            ViewLogin viewCtrl = loader.getController();
            viewCtrl.setMainController(this);

            // Mostra la scena
            stage.setTitle("Accesso Biblioteca");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostraDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainDashboard.fxml"));
            Parent root = loader.load();

            // Collega la dashboard al coordinatore
            MainDashboard viewCtrl = loader.getController();
            viewCtrl.setMainController(this);

            stage.setTitle("Dashboard Biblioteca");
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- METODI FUNZIONALI (API per le View) ---

    // Chiamato da ViewLogin
    public boolean tentaLogin(String user, String pass) {
        boolean esito = bibliotecario.login(user, pass);
        if (esito) {
            mostraDashboard(); // Se login ok, cambia scena
        }
        return esito;
    }

    public void logout() {
        mostraLoginView();
    }

    // Getter per dare accesso ai dati alla Dashboard
    public CatalogoLibri getCatalogoLibri() { return catalogoLibri; }
    public ListaUtenti getListaUtenti() { return listaUtenti; }
    public GestorePrestito getGestorePrestito() { return gestorePrestito; }
}
