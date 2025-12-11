package UtenteTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import Eccezioni.EccezioniUtenti.*;
import Sorting.SortingUtenti.OrdinamentoUtenti; 

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author chiara
 */
@DisplayName("Test Suite Completa ListaUtenti")
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
    private final String COGNOME_VALIDO_U2 = "Bianchi";
    private final String MATRICOLA_VALIDA_U2 = "0987654321";
    private final String EMAIL_VALIDA_U2 = "c.bianchi@studenti.unisa.it";
    
    private Utente u3;
    private final String NOME_VALIDO_U3 = "Aurora";
    private final String COGNOME_VALIDO_U3 = "Rossetti";
    private final String MATRICOLA_VALIDA_U3 = "1234509876";
    private final String EMAIL_VALIDA_U3 = "a.rossetti@studenti.unisa.it";
    
    private final String TEST_FILENAME = "test_listaUteni.txt";
    
    // FIXTURE: BeforeEach
    @BeforeEach
    void setUp() throws Exception {
        // Inizializzo una lista vuota, che non carico sul file (false)
        listaUtenti = new ListaUtenti(false, TEST_FILENAME);
        
        // Creo i tre utenti
        u1 = new Utente(NOME_VALIDO_U1, COGNOME_VALIDO_U1, MATRICOLA_VALIDA_U1, EMAIL_VALIDA_U1); // Rossi
        u2 = new Utente(NOME_VALIDO_U2, COGNOME_VALIDO_U2, MATRICOLA_VALIDA_U2, EMAIL_VALIDA_U2); // Bianchi
        u3 = new Utente(NOME_VALIDO_U3, COGNOME_VALIDO_U3, MATRICOLA_VALIDA_U3, EMAIL_VALIDA_U3); // Rossetti
    }
    
    // FIXTURE: AfterEach
    // @AfterEach serve a garantire la pulizia anche se il test fallisce
    @AfterEach 
    void tearDown(){
        // Pulizia del file
        File f = new File(TEST_FILENAME);
        if(f.exists()){
            f.delete();
        }
    }
    
    // TEST REGISTRAZIONE 
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
    void testMatricolaNonValida() throws MatricolaNotValidException {
        // Creazione di un utente con la matricola errata
        Utente utenteErrato = new Utente(NOME_VALIDO_U2, COGNOME_VALIDO_U2, "123", EMAIL_VALIDA_U2);
        
        assertThrows(MatricolaNotValidException.class, () -> {
           listaUtenti.registrazioneUtente(utenteErrato);
        });
    }
    
    // TEST RICERCA
    @Test
    @DisplayName("Ricerca Giusta (StartsWith)")
    void testCercaUtenteValida() throws Exception{
        // Creazione dei 3 utenti
        listaUtenti.registrazioneUtente(u1); // Rossi
        listaUtenti.registrazioneUtente(u2); // Bianchi
        listaUtenti.registrazioneUtente(u3); // Rossetti
        
        // Ricerca parziale per Cognome "ros" -> devo ottenere u1 e u3
        ArrayList<Utente> risultatiCognome = listaUtenti.cercaUtente("ros");
        
        assertEquals(2, risultatiCognome.size(), "Dovrebbe trovare due utenti (Rossi e Rossetti)");
        assertTrue(risultatiCognome.contains(u1));
        assertTrue(risultatiCognome.contains(u3));
        assertFalse(risultatiCognome.contains(u2));
        
        // Ricerca parziale per Matricola "098765" (completa o parziale) -> devo ottenere u2
        ArrayList<Utente> risultatiMatricola = listaUtenti.cercaUtente("098765");
        assertEquals(1, risultatiMatricola.size());
        assertEquals(u2, risultatiMatricola.get(0));
        
        // Ricerca case-insensitive "ROSSI"
        ArrayList<Utente> risultatiCase = listaUtenti.cercaUtente("ROSSI");
        assertEquals(1, risultatiCase.size());
        assertEquals(u1, risultatiCase.get(0));
        
        // Stringa che non matcha l'inizio (es. "setti" è contenuto in Rossetti, ma non inizia con esso)
        ArrayList<Utente> risultatiFail = listaUtenti.cercaUtente("setti");
        assertTrue(risultatiFail.isEmpty(), "Non dovrebbe trovare nulla con 'contains', stiamo usando 'startsWith'");
    }
    
    @Test
    @DisplayName("Ricerca Utente con parametro Null")
    void testCercaUtenteNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            listaUtenti.cercaUtente(null);
        });
    }
    
    // TEST RIMOZIONE 
    @Test
    @DisplayName("Eliminazione Utente")
    void testEliminazioneUtente() throws Exception {
        // Creazione di un utente
        listaUtenti.registrazioneUtente(u1);
        assertEquals(1, listaUtenti.getListaUtenti().size());

        // Eliminazione dell'utente eliminato
        listaUtenti.eliminazioneUtente(u1);
        assertEquals(0, listaUtenti.getListaUtenti().size());
        assertNull(listaUtenti.getUtenteByMatricola(u1.getMatricola()));
    }

    @Test
    @DisplayName("Eccezione Eliminazione Utente Non Presente")
    void testEliminazioneUtenteNonTrovato() {
        assertThrows(UtenteNotFoundException.class, () -> {
            listaUtenti.eliminazioneUtente(u1); // u1 non è mai stato aggiunto
        });
    }
    
    // TEST MODIFICA
    @Test
    @DisplayName("Modifica Utente")
    void testModificaUtente() throws Exception {
        // Creazione di un utente
        listaUtenti.registrazioneUtente(u1);

        // Modifica dell'utente
        String nuovoNome = "MarioModificato";
        String nuovoCognome = "RossiModificato";
        String nuovaEmail = "nuova@email.it";

        listaUtenti.modificaUtente(u1, nuovoNome, nuovoCognome, nuovaEmail);

        // Verifica che l'utente modificato sia nella lista
        Utente utenteAggiornato = listaUtenti.getUtenteByMatricola(u1.getMatricola());
        
        assertAll("Verifica campi aggiornati",
            () -> assertEquals(nuovoNome, utenteAggiornato.getNome()),
            () -> assertEquals(nuovoCognome, utenteAggiornato.getCognome()),
            () -> assertEquals(nuovaEmail, utenteAggiornato.getEmailIstituzionale())
        );
    }
    
    // TEST ORDINAMENTO (NUOVO) 
    @Test
    @DisplayName("Ordinamento Alfabetico della Lista")
    void testSortListaUtenti() throws Exception {
        // Inserisco gli utenti in ordine sparso
        listaUtenti.registrazioneUtente(u1); // Rossi
        listaUtenti.registrazioneUtente(u2); // Bianchi
        listaUtenti.registrazioneUtente(u3); // Rossetti
        
        // Eseguo il sort usando il Comparator fornito 
        ArrayList<Utente> listaOrdinata = listaUtenti.sortListaUtenti(OrdinamentoUtenti.ALFABETICO);
        
        // VERIFICA DELL'ORDINE:
        // 1. Bianchi (B)
        // 2. Rossetti (R...e) -> Viene prima di Rossi perchè 'e' < 'i'
        // 3. Rossi (R...i)
        
        assertEquals(u2, listaOrdinata.get(0), "Bianchi dovrebbe essere il primo");
        assertEquals(u3, listaOrdinata.get(1), "Rossetti dovrebbe essere il secondo");
        assertEquals(u1, listaOrdinata.get(2), "Rossi dovrebbe essere il terzo");
    }
    
    @Test
    @DisplayName("Errore: Ordinamento con Comparator Null")
    void testSortComparatorNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            listaUtenti.sortListaUtenti(null);
        });
    }

    // TEST PERSISTENZA E RICARICAMENTO (NUOVO) 
    @Test
    @DisplayName("Test Persistenza: Ricaricamento da File")
    void testRicaricamentoDaFile() throws Exception {
        // 1. Inserisco utenti nella lista corrente (che salva automaticamente su file)
        listaUtenti.registrazioneUtente(u1);
        listaUtenti.registrazioneUtente(u2);
        listaUtenti.registrazioneUtente(u3);
        
        // 2. Simulo un riavvio dell'applicazione creando una NUOVA istanza di ListaUtenti
        // impostando il flag di caricamento a TRUE e puntando allo stesso file.
        ListaUtenti listaRicaricata = new ListaUtenti(true, TEST_FILENAME);
        
        // 3. Verifico che i dati siano stati recuperati
        assertEquals(2, listaRicaricata.getListaUtenti().size());
        
        // Verifica presenza di u1
        Utente u1Caricato = listaRicaricata.getUtenteByMatricola(MATRICOLA_VALIDA_U1);
        assertNotNull(u1Caricato);
        assertEquals(NOME_VALIDO_U1, u1Caricato.getNome());
        
        // Verifica presenza di u2
        Utente u2Caricato = listaRicaricata.getUtenteByMatricola(MATRICOLA_VALIDA_U2);
        assertNotNull(u2Caricato);
        
        // Verifica presenza di u3
        Utente u3Caricato = listaRicaricata.getUtenteByMatricola(MATRICOLA_VALIDA_U3);
        assertNotNull(u3Caricato);
    }
}