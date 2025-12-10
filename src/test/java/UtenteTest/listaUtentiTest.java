/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UtenteTest;

// IMPORT NECESSARI PER VEDERE LE CLASSI DELL'ALTRO PACKAGE
import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import Eccezioni.EccezioniUtenti.*;
import Sorting.SortingUtenti.OrdinamentoUtenti;

// IMPORT PER JUNIT 5
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per ListaUtenti senza Mockito (Integration Test).
 * Scrive e legge file reali su disco.
 * * @author chiara
 */
public class ListaUtentiTest {
    
    // FIXTURE: Variabili di Istanza
    private ListaUtenti listaUtenti;
    private final String TEST_FILENAME = "test_listaUtenti.dat";
    
    // Oggetti Utente
    private Utente u1; // Bianchi
    private Utente u2; // Rossi
    private Utente u3; // Verdi
    
    @BeforeEach
    void setUp() throws Exception {
        // 1. Assicuriamoci che il file non esista prima di iniziare
        File f = new File(TEST_FILENAME);
        if(f.exists()) {
            f.delete();
        }

        // 2. Inizializzo la lista vuota
        listaUtenti = new ListaUtenti(false, TEST_FILENAME);

        // 3. Inizializzazione Utenti Reali
        // ID Basso (Meno recente)
        u1 = new Utente("Mario", "Bianchi", "1234567890", "mario.bianchi@test.it");
        
        // ID Medio
        u2 = new Utente("Luigi", "Rossi", "1234509876", "luigi.rossi@test.it");
        
        // ID Alto (Più recente)
        u3 = new Utente("Anna", "Verdi", "0987654321", "anna.verdi@test.it");
    }
    
    @AfterEach
    void tearDown() {
        // PULIZIA: Cancella il file creato dal test
        File f = new File(TEST_FILENAME);
        if(f.exists()) {
            f.delete();
        }
        listaUtenti = null;
    }
    
    // TEST COSTRUTTORE
    @Test
    @DisplayName("Costruttore: Inizializzazione vuota corretta")
    void testCostruttore() {
        assertNotNull(listaUtenti.getListaUtenti());
        assertTrue(listaUtenti.getListaUtenti().isEmpty());
    }
    
    // TEST METODI
    
    // TEST GET
    @Test
    @DisplayName("GetUtenteByMatricola: Ricerca con successo")
    void testGetUtenteByMatricola() throws Exception {
        // Inserisco l'utente realmente (scrive su file)
        listaUtenti.registrazioneUtente(u1, TEST_FILENAME);

        Utente risultato = listaUtenti.getUtenteByMatricola("1234567890");
        assertNotNull(risultato);
        assertEquals(u1.getCognome(), risultato.getCognome());
    }

    @Test
    @DisplayName("GetUtenteByMatricola: Utente non trovato (return null)")
    void testGetUtenteByMatricolaNotFound() {
        Utente risultato = listaUtenti.getUtenteByMatricola("9999999999");
        assertNull(risultato);
    }
    
    // TEST REGISTRAZIONE
    @Test
    @DisplayName("RegistrazioneUtente: Inserimento con successo e creazione file")
    void testRegistrazioneUtenteSuccesso() {
        // Esecuzione reale
        assertDoesNotThrow(() -> listaUtenti.registrazioneUtente(u1, TEST_FILENAME));

        // Verifiche Logiche
        assertEquals(1, listaUtenti.getListaUtenti().size());
        assertTrue(listaUtenti.getListaUtenti().contains(u1));
        
        // Verifica Fisica: Il file è stato creato?
        File f = new File(TEST_FILENAME);
        assertTrue(f.exists(), "Il file di salvataggio deve esistere");
        assertTrue(f.length() > 0, "Il file non deve essere vuoto");
    }

    @Test
    @DisplayName("RegistrazioneUtente: Errore matricola non valida")
    void testRegistrazioneMatricolaInvalida() {
        // Poiché non usiamo Mock, proviamo a creare un utente con matricola errata.
        // Se il costruttore di Utente è fatto bene, lancerà l'eccezione qui.
        // Se il costruttore fosse permissivo ma ListaUtenti no, l'eccezione verrebbe da ListaUtenti.
        
        assertThrows(MatricolaNotValidException.class, () -> {
            // Tentativo di creare/inserire utente non valido
            Utente utenteBad = new Utente("Bad", "User", "123", "email"); 
            listaUtenti.registrazioneUtente(utenteBad, TEST_FILENAME);
        });
    }

    @Test
    @DisplayName("RegistrazioneUtente: Errore Utente Duplicato")
    void testRegistrazioneUtenteDuplicato() throws Exception {
        // 1. Inseriamo il primo (Scrive su file)
        listaUtenti.registrazioneUtente(u1, TEST_FILENAME);

        // 2. Proviamo a inserire lo stesso utente di nuovo
        UtentePresenteException ex = assertThrows(UtentePresenteException.class, () -> {
            listaUtenti.registrazioneUtente(u1, TEST_FILENAME);
        });
        
        assertEquals("L'utente è già presente all'interno della lista.", ex.getMessage());
    }

    @Test
    @DisplayName("RegistrazioneUtente: Errore Null")
    void testRegistrazioneUtenteNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            listaUtenti.registrazioneUtente(null, TEST_FILENAME);
        });
    }
    
    // TEST ELIMINAZIONE UTENTE
    @Test
    @DisplayName("EliminazioneUtente: Rimozione con successo")
    void testEliminazioneUtente() throws Exception {
        // Setup: aggiungo utente
        listaUtenti.registrazioneUtente(u1, TEST_FILENAME);
        assertEquals(1, listaUtenti.getListaUtenti().size());

        // Azione: rimuovo utente (Aggiorna il file)
        listaUtenti.eliminazioneUtente(u1);

        // Verifica
        assertTrue(listaUtenti.getListaUtenti().isEmpty());
    }

    @Test
    @DisplayName("EliminazioneUtente: Errore se non presente")
    void testEliminazioneUtenteNonPresente() {
        assertThrows(UtenteNotFoundException.class, () -> {
            listaUtenti.eliminazioneUtente(u1); // Lista vuota
        });
    }
    
    // TEST RICERCA
    @Test
    @DisplayName("CercaUtente: Ricerca parziale Cognome/Matricola")
    void testCercaUtente() throws Exception {
        listaUtenti.registrazioneUtente(u1, TEST_FILENAME); // Bianchi
        listaUtenti.registrazioneUtente(u2, TEST_FILENAME); // Rossi
        listaUtenti.registrazioneUtente(u3, TEST_FILENAME); // Verdi

        // Cerca per cognome "Ros" (Rossi)
        ArrayList<Utente> risultati = listaUtenti.cercaUtente("Ros");
        assertEquals(1, risultati.size());
        assertEquals("Rossi", risultati.get(0).getCognome());

        // Cerca per matricola parziale "09876" (Verdi: 0987654321)
        ArrayList<Utente> risultatiMatr = listaUtenti.cercaUtente("09876");
        assertEquals(1, risultatiMatr.size());
        assertEquals("Verdi", risultatiMatr.get(0).getCognome());
    }
    
    // TEST MODIFICA
    @Test
    @DisplayName("ModificaUtente: Verifica persistenza dati")
    void testModificaUtente() throws Exception {
        // 1. Inserimento
        listaUtenti.registrazioneUtente(u1, TEST_FILENAME); // Mario Bianchi
        
        // 2. Modifica in memoria
        u1.setNome("Modificato");
        
        // 3. Salvataggio modifiche
        listaUtenti.modificaUtente(u1, TEST_FILENAME);
        
        // 4. Verifica ricaricando da file (simuliamo riavvio app)
        ListaUtenti listaRicaricata = new ListaUtenti(true, TEST_FILENAME);
        Utente uRicaricato = listaRicaricata.getUtenteByMatricola("1234567890");
        
        assertEquals("Modificato", uRicaricato.getNome());
    }
    
    // TEST SORTING
    @Test
    @DisplayName("SortListaUtenti: Test Ordinamento Alfabetico, Più Recenti, Meno Recenti")
    void testSortListaUtenti() throws Exception {
        // Setup: Inserisco gli utenti nella lista in ordine sparso
        listaUtenti.registrazioneUtente(u2, TEST_FILENAME); // ID medio
        listaUtenti.registrazioneUtente(u3, TEST_FILENAME); // ID alto (più recente)
        listaUtenti.registrazioneUtente(u1, TEST_FILENAME); // ID basso (meno recente)

        // CASO 1: ALFABETICO (Per Cognome A-Z) 
        // Atteso: Bianchi -> Rossi -> Verdi
        ArrayList<Utente> sortedAlfa = listaUtenti.sortListaUtenti(OrdinamentoUtenti.ALFABETICO);
        
        assertEquals(3, sortedAlfa.size());
        assertEquals(u1, sortedAlfa.get(0), "Il primo deve essere Bianchi");
        assertEquals(u2, sortedAlfa.get(1), "Il secondo deve essere Rossi");
        assertEquals(u3, sortedAlfa.get(2), "Il terzo deve essere Verdi");


        // CASO 2: PIU_RECENTI (Dall'ultimo inserito al primo) 
        // Atteso: Verdi (ID alto) -> Rossi -> Bianchi (ID basso)
        ArrayList<Utente> sortedNewest = listaUtenti.sortListaUtenti(OrdinamentoUtenti.PIU_RECENTI);
        
        assertEquals(u3, sortedNewest.get(0), "Il più recente deve essere Verdi");
        assertEquals(u2, sortedNewest.get(1));
        assertEquals(u1, sortedNewest.get(2), "Il meno recente deve essere Bianchi");


        // CASO 3: MENO_RECENTI (Dal primo inserito all'ultimo) 
        // Atteso: Bianchi (ID basso) -> Rossi -> Verdi (ID alto)
        ArrayList<Utente> sortedOldest = listaUtenti.sortListaUtenti(OrdinamentoUtenti.MENO_RECENTI);
        
        assertEquals(u1, sortedOldest.get(0), "Il meno recente deve essere Bianchi");
        assertEquals(u2, sortedOldest.get(1));
        assertEquals(u3, sortedOldest.get(2), "Il più recente deve essere Verdi");
    }
}