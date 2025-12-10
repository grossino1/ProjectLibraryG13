package GestioneUtente; // <--- Controlla il package corretto

import Eccezioni.EccezioniUtenti.*; // <--- Controlla il package delle eccezioni
import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;
import java.io.IOException;

public class PopolaUtenti {

    public static void main(String[] args) {
        String filename = "listaUtenti.bin"; // Il nome del file dove salvi gli utenti
        ListaUtenti catalogo;

        // 1. Inizializzazione Catalogo
        try {
            System.out.println("Inizializzazione catalogo utenti...");
            // True = prova a caricare, False = crea vuoto. Adatta in base al tuo costruttore
            catalogo = new ListaUtenti(false, filename); 
        } catch (Exception e) {
            System.out.println("Creazione nuovo catalogo vuoto.");
            try {
                catalogo = new ListaUtenti(false, filename);
            } catch (Exception ex) {
                System.err.println("Errore critico: " + ex.getMessage());
                return;
            }
        }

        // 2. Creazione utenti dummy
        // ATTENZIONE: Adatta i parametri del costruttore alla tua classe Utente!
        // Esempio ipotetico: new Utente(Nome, Cognome, CodiceFiscale, Password, Ruolo/Email)
        String[][] datiUtenti = {
            {"Mario", "Rossi", "0000111122", "mario.rossi@studenti.unina.it"},
            {"Luca", "Bianchi", "0000222233", "luca.bianchi@studenti.unina.it"},
            {"Sofia", "Verdi", "0000333344", "sofia.verdi@studenti.unina.it"},
            {"Giulia", "Esposito", "0000444455", "giulia.esposito@studenti.unina.it"},
            {"Andrea", "Colombo", "0000555566", "andrea.colombo@studenti.unina.it"},
            {"Marco", "Ricci", "0000666677", "marco.ricci@studenti.unina.it"},
            {"Francesca", "Romano", "0000777788", "francesca.romano@studenti.unina.it"},
            {"Alessandro", "Galli", "0000888899", "alessandro.galli@studenti.unina.it"},
            {"Martina", "Ferrara", "0000999900", "martina.ferrara@studenti.unina.it"},
            {"Davide", "Marino", "1111000011", "davide.marino@studenti.unina.it"}
        };

        for (String[] dati : datiUtenti) {
            try {
                // Costruttore: Utente(nome, cognome, matricola, email)
                Utente u = new Utente(dati[0], dati[1], dati[2], dati[3]);
                
                // Assumo il metodo si chiami 'registrazioneUtente' o 'aggiungiUtente'
                catalogo.registrazioneUtente(u); 
                
                System.out.println("Inserito: " + u.getNome() + " " + u.getCognome() + " (Matr: " + u.getMatricola() + ")");

            } catch (MatricolaNotValidException e) {
                System.err.println("Errore Matricola per " + dati[1] + ": " + e.getMessage());
            } catch (UtentePresenteException e) {
                System.out.println("Utente già presente: " + dati[1]);
            } catch (IllegalArgumentException e) {
                 System.err.println("Dati non validi: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Errore generico: " + e.getMessage());
            }
        }

        // 4. Salvataggio
        try {
            SalvataggioFileUtente.salva(catalogo, filename);
            System.out.println("\nOperazione completata! Utenti inseriti/verificati: " );
        } catch (IOException e) {
            System.err.println("Errore grave nel salvataggio su file: " + e.getMessage());
        }
    }
    }