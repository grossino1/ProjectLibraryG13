package UtenteTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import Eccezioni.EccezioniUtenti.*;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
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
    
    /**
     * @brief Inizializza l'ambiente prima di ogni test.
     * Prima di ogni Test inizializza la listaUtenti e crea i nuovi utenti.
     */
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
    
    /**
     * @brief Pulisce l'ambiente dopo ogni test.
     * Rimuove il file creato dal test per non lasciare sporcizia nel progetto.
     */
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
    @DisplayName("Registrazione Reale Corretta con Timeout (max 2 secondi)")
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
    @DisplayName("Errore: Registrazione di un Utente con una Matricola Non Valida")
    void testRegistrazioneMatricolaNonValida() throws MatricolaNotValidException {
        // Creazione di un utente con la matricola errata
        
        assertThrows(MatricolaNotValidException.class, () -> {
           Utente utenteErrato = new Utente(NOME_VALIDO_U2, COGNOME_VALIDO_U2, "123", EMAIL_VALIDA_U2);
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
    }
    
    @Test
    @DisplayName("Ricerca Utente con parametro Null")
    void testCercaUtenteNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            listaUtenti.cercaUtente(null);
        });
    }
    
    @Test
    @DisplayName("Ricerca Fallita: Deve lanciare UtenteNotFoundException")
    void testCercaUtenteNonTrovato() throws Exception{
        // Aggiungo un utente per non avere la lista vuota a priori
        listaUtenti.registrazioneUtente(u1); // Rossi

        // Cerco una stringa che sicuramente non c'è (es. "Verdi")
        UtenteNotFoundException eccezione = assertThrows(UtenteNotFoundException.class, () -> {
            listaUtenti.cercaUtente("Verdi");
        });

        // Verifico che il messaggio dell'errore sia quello giusto
        assertEquals("Errore: Utente non trovato all'interno della lista!", eccezione.getMessage());
    }
    
    // TEST RIMOZIONE 
    
    @Test
    @DisplayName("Eliminazione Utente")
    void testEliminazioneUtenteValida() throws Exception {
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
}