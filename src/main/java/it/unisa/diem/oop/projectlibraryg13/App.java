package it.unisa.diem.oop.projectlibraryg13;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @class App
 * @brief Classe principale (Entry Point) dell'applicazione gestionale.
 *
 * Questa classe estende `Application` di JavaFX ed è responsabile dell'avvio
 * dell'interfaccia grafica. Si occupa di inizializzare lo Stage primario (finestra),
 * caricare la vista iniziale (Login) e impostare le configurazioni di base (titolo, icona).
 *
 * @see javafx.application.Application
 * @see javafx.fxml.FXMLLoader
 *
 * @author grossino1
 * @version 1.0
 */

public class App extends Application {

    /**
     * @brief Metodo di avvio del ciclo di vita JavaFX.
     *
     * Viene chiamato automaticamente dal framework dopo l'inizializzazione del sistema.
     * Esegue i seguenti passaggi:
     * 1. Carica il file FXML della vista Login.
     * 2. Configura la Scena e lo Stage (dimensioni, titolo, icona).
     * 3. Rende visibile la finestra.
     *
     * @pre primaryStage != null (Il framework garantisce che lo stage primario sia istanziato).
     * @pre Il file FXML "/GUI/GUI_Login/LoginView.fxml" deve esistere nelle risorse.
     * @post La finestra dell'applicazione è visibile a schermo.
     *
     * @param[in] primaryStage Il contenitore principale della finestra (fornito dal framework).
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Caricamento del file FXML
            // NOTA BENE: Il percorso inizia con "/" che indica la cartella delle risorse (o src)
            // Assicurati che il percorso rifletta esattamente la tua struttura delle cartelle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GUI_Login/LoginView.fxml"));
            
            // Carica la gerarchia degli oggetti (il root del tuo FXML, cioè il GridPane)
            Parent root = loader.load();

            // 2. Configurazione della Scena
            Scene scene = new Scene(root);
            
            // (Opzionale) Aggiungi un foglio di stile CSS se ne hai uno
            // scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            // 3. Configurazione dello Stage (la finestra)
            primaryStage.setTitle("Library G13 - Gestionale Bibliotecario");
            primaryStage.setScene(scene);
            
            // Imposta l'icona dell'applicazione (quella nella barra delle applicazioni)
            // Assicurati di avere un logo nel percorso specificato
            try {
                Image icon = new Image(getClass().getResourceAsStream("/GUI/GUI_Login/logo_standard.png"));
                primaryStage.getIcons().add(icon);
            } catch (Exception e) {
                System.out.println("Icona non trovata, avvio con icona di default.");
            }

            // Imposta dimensioni minime per evitare che l'interfaccia si rompa se rimpicciolita troppo
            //primaryStage.setMinWidth(600);
            //primaryStage.setMinHeight(400);
            primaryStage.setMaximized(true);

            // Avvia la finestra massimizzata (Opzionale, togli il commento se vuoi)
            // primaryStage.setMaximized(true);

            // 4. Mostra la finestra
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERRORE CRITICO: Impossibile caricare l'interfaccia di Login.");
            System.err.println("Controlla che il file 'LoginView.fxml' sia nel percorso corretto.");
        }
    }

    /**
     * @brief Entry point standard per la JVM.
     *
     * Questo metodo è il punto di ingresso per l'esecuzione del programma Java.
     * Delega immediatamente il controllo al metodo `launch()` di JavaFX per avviare la GUI.
     *
     * @param[in] args Argomenti da riga di comando (eventualmente passati al lancio).
     */
    public static void main(String[] args) {
        // Metodo standard per avviare applicazioni JavaFX
        launch(args);
    }
}
