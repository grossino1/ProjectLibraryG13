/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UtenteTest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import Eccezioni.EccezioniUtenti.MatricolaNotValidException;
import Eccezioni.EccezioniUtenti.ListaUtentiPienaException;
import Eccezioni.EccezioniUtenti.UtentePresenteException;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author chiara
 */
public class ListaUtentiTest {
    // FIXTURE: variabili di istanza
    private ListaUtenti listaUtenti;
    
    private Utente u1;
    private final String NOME_VALIDO_U1 = "Mario";
    private final String COGNOME_VALIDO_U1 = "Rossi";
    private final String MATRICOLA_VALIDA_U1 = "1234567890";
    private final String EMAIL_VALIDA_U1 = "m.rossi1@studenti.unisa.it";
    
    private Utente u2;
    private final String NOME_VALIDO_U2 = "Chiara";
    private final String COGNOME_VALIDO_U2 = "Icuzzo";
    private final String MATRICOLA_VALIDA_U2 = "0987654321";
    private final String EMAIL_VALIDA_U2 = "c.iacuzzo@studenti.unisa.it";
    
    private final String TEST_FILENAME = "test_listaUteni.txt";
    
    // FIXTURE: BeforeEach
    @BeforeEach
    void setUp()throws Exception{
        // Inizializzo una lista vuota, che non carico sul file
        listaUtenti = new ListaUtenti(false, TEST_FILENAME);
        
        // Creo i due utenti
        u1 = new Utente(NOME_VALIDO_U1, COGNOME_VALIDO_U1, MATRICOLA_VALIDA_U1, EMAIL_VALIDA_U1);
        u2 = new Utente(NOME_VALIDO_U2, COGNOME_VALIDO_U2, MATRICOLA_VALIDA_U2, EMAIL_VALIDA_U2);
    }
    
    // FIXTURE: AfterEach
    void tearDown(){
        // Pulizia del file
        File f = new File(TEST_FILENAME);
        if(f.exists()){
            f.delete();
        }
    }
    
    // TEST REGISTRAZIONE UTENTE
    @Test
    @DisplayName("Registrazione Reale con Timeout (max 2 secondi)")
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testRegistrazioneUtentePerformace() throws Exception{
        // Registro un utente
        listaUtenti.registrazioneUtente(u1);
        
        // Verifica della registrazione
        assertEquals(1, listaUtenti.getListaUtenti().size());
        assertEquals(u1, listaUtenti.getUtenteByMatricola(MATRICOLA_VALIDA_U1));
        
        // Verifica del file
        File f = new File(TEST_FILENAME);
        assertTrue(f.exists(), "Il file di salvataggio deve esistere.");
    }
    
    @Test
    @DisplayName("Errore: Registrazione di un Utente Null")
    void testRegistrazioneUtenteNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            listaUtenti.registrazioneUtente(null);
        });
    }
    
    @Test
    @DisplayName("Errore: Registrazione di un Utente già presente")
    void testRegistrazioneUtenteEsistente() throws Exception{
        // Inserisco un utente
        listaUtenti.registrazioneUtente(u1);
        
        // Inserisco di nuovo lo stesso utente
        assertThrows(UtentePresenteException.class, () -> {
           listaUtenti.registrazioneUtente(u1);
        });
    }
    
    @Test
    @DisplayName("Errore: Matricola Non Valida")
    void testMatricolaNonValida() throws MatricolaNotValidException{
        // Creazione di un utente con la matricola errata
        Utente utenteErrato = new Utente(NOME_VALIDO_U2, COGNOME_VALIDO_U2, "123", EMAIL_VALIDA_U2);
        
        assertThrows(MatricolaNotValidException.class, () -> {
           listaUtenti.registrazioneUtente(utenteErrato);
        });
    }
    
    // TEST RICERCA
    @Test
    @DisplayName("Ricerca Giusta")
    void testCercaUtenteValida() throws Exception{
        listaUtenti.registrazioneUtente(u1);
        listaUtenti.registrazioneUtente(u2);
        
        // Cerco il primo utente per il cognome
        ArrayList<Utente> risultato = listaUtenti.cercaUtente("Rossi");
        
        assertEquals
    }
}
