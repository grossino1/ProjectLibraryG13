package GestioneUtenteTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import GestionePrestito.Prestito;
import Eccezioni.EccezioniUtenti.*;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
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
    private final String MATRICOLA_VALIDA_U2 = "1234509876";
    private final String EMAIL_VALIDA_U2 = "c.bianchi@studenti.unisa.it";
    
    private Utente u3;
    private final String NOME_VALIDO_U3 = "Aurora";
    private final String COGNOME_VALIDO_U3 = "Rossetti";
    private final String MATRICOLA_VALIDA_U3 = "0987654321";
    private final String EMAIL_VALIDA_U3 = "a.rossetti@studenti.unisa.it";
    
    private final String TEST_FILENAME = "test_listaUtenti.txt";
    
    // FIXTURE: BeforeEach
    
    /**
     * @brief Inizializza l'ambiente prima di ogni test.
     * Prima di ogni Test inizializza la listaUtenti e crea i nuovi utenti.
     */
    @BeforeEach
    void setUp() throws Exception {
        // Pulizia preventiva
        new File(TEST_FILENAME).delete();
        
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
     * @AfterEach serve a garantire la pulizia anche se il test fallisce
     */
    @AfterEach 
    void tearDown(){
        // Pulizia del file
        File f = new File(TEST_FILENAME);
        if(f.exists()){
            f.delete();
        }
    }
    
    // TEST COSTRUTTORE 
    
    @Test
    @DisplayName("Costruttore: Caricamento Disattivato (Nuova Lista)")
    void testCostruttoreCaricamentoFalse() throws Exception {
        // Quando caricamentoFile è false
        ListaUtenti nuovaLista = new ListaUtenti(false, TEST_FILENAME);

        // La lista interna deve essere inizializzata ma vuota
        assertNotNull(nuovaLista.getListaUtenti(), "La lista non dovrebbe essere null");
        assertTrue(nuovaLista.getListaUtenti().isEmpty(), "La lista dovrebbe essere vuota");
    }

    @Test
    @DisplayName("Costruttore: Caricamento Attivato - File Esistente (Successo)")
    void testCostruttoreCaricamentoTrueSuccesso() throws Exception {
        // Preparo una lista e la salvo su file
        // Creo una lista temporanea in cui insersco l'utente u1
        ListaUtenti listaDaSalvare = new ListaUtenti(false, TEST_FILENAME);
        listaDaSalvare.registrazioneUtente(u1);
        
        // Scrivo fisicamente questa lista sul file 
        // Questo simula un file salvato precedentemente dal programma
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TEST_FILENAME))) {
            out.writeObject(listaDaSalvare);
        }

        //Chiamo il costruttore dicendogli di caricare (true) da quel file
        ListaUtenti listaCaricata = new ListaUtenti(true, TEST_FILENAME);

        // Verifico che abbia letto i dati correttamente
        assertNotNull(listaCaricata, "L'oggetto caricato non deve essere null");
        
        // La lista deve contenere un solo elemento (u1)
        assertEquals(1, listaCaricata.getListaUtenti().size(), "La lista caricata dovrebbe avere 1 utente");
        
        // Verifico che ci sia proprio l'utente u1
        assertNotNull(listaCaricata.getUtenteByMatricola(MATRICOLA_VALIDA_U1), "L'utente u1 dovrebbe essere stato caricato");
    }

    @Test
    @DisplayName("Costruttore: Caricamento Attivato - File Non Trovato")
    void testCostruttoreCaricamentoTrueFileMancante() {
        String fileInesistente = "fileInesistente.dat";

        // Mi aspetto che venga lanciata una IOException (oppure FileNotFoundException che ne è sottoclasse)
        // perché stiamo chiedendo di caricare un file che non esiste.
        assertThrows(IOException.class, () -> {
            new ListaUtenti(true, fileInesistente);
        });
    }
    
    // TEST GetUtenteByMatricola
    
    @Test
    @DisplayName("Get Utente: Utente Esistente")
    void testGetUtenteByMatricolaEsistente() throws Exception {
        // Aggiungo un utente alla lista
        listaUtenti.registrazioneUtente(u1); 

        // Cerco l'utente usando la sua matricola corretta
        Utente risultato = listaUtenti.getUtenteByMatricola(MATRICOLA_VALIDA_U1);

        // Verifico che l'oggetto restituito sia quello aspettato
        assertNotNull(risultato, "L'utente dovrebbe essere trovato.");
        assertEquals(u1, risultato, "L'utente restituito deve coincidere con quello inserito.");
        assertEquals("Mario", risultato.getNome()); // Controllo ulteriore sui dati
    }

    @Test
    @DisplayName("Get Utente: Utente Non Trovato")
    void testGetUtenteByMatricolaNonTrovato() throws Exception {
        // Aggiungo u1 alla lista, ma NON aggiungo u2
        listaUtenti.registrazioneUtente(u1);

        // Cerco la matricola di u2 (che non è nella lista)
        Utente risultato = listaUtenti.getUtenteByMatricola(MATRICOLA_VALIDA_U2);

        // ASSERT: Il metodo deve restituire null
        assertNull(risultato, "Se la matricola non è presente, il metodo deve restituire null.");
    }

    @Test
    @DisplayName("Get Utente: Errore Matricola Null")
    void testGetUtenteByMatricolaNull() {
        // Verifico che lanci IllegalArgumentException con il messaggio specifico
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            listaUtenti.getUtenteByMatricola(null);
        });
        assertEquals("Errore: La chiave di ricerca non può essere nulla.", e.getMessage());
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
        // Verifico che lanci IllegalArgumentException con il messaggio specifico
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
        
        // Ricerca parziale per Cognome "ros" -> Deve restituire u1 e u3
        ArrayList<Utente> risultatiCognome = listaUtenti.cercaUtente("ros");
        
        assertEquals(2, risultatiCognome.size(), "Dovrebbe trovare due utenti (Rossi e Rossetti)");
        assertTrue(risultatiCognome.contains(u1));
        assertTrue(risultatiCognome.contains(u3));
        assertFalse(risultatiCognome.contains(u2));
        
        // Ricerca per Nome "Chiara" (Parziale o completa) -> Deve restituire u2
        ArrayList<Utente> risultatiNome = listaUtenti.cercaUtente("Chiara");

        assertEquals(1, risultatiNome.size(), "Dovrebbe trovare un utente con nome Chiara");
        assertTrue(risultatiNome.contains(u2));
        
        // Ricerca parziale per Matricola "098765" (completa o parziale) -> Deve restituire u3
        ArrayList<Utente> risultatiMatricola = listaUtenti.cercaUtente("098765");
        assertEquals(1, risultatiMatricola.size());
        assertEquals(u3, risultatiMatricola.get(0));
        
        // Ricerca case-insensitive "ROSSI" -> Deve restituire u1
        ArrayList<Utente> risultatiCase = listaUtenti.cercaUtente("ROSSI");
        assertEquals(1, risultatiCase.size());
        assertEquals(u1, risultatiCase.get(0));
    }
    
    @Test
    @DisplayName("Ricerca Utente con parametro Null")
    void testCercaUtenteNull() {
        // Verifico che lanci IllegalArgumentException 
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
    @DisplayName("Eliminazione: Errore Utente Null (MODIFICATO)")
    void testEliminazioneUtenteNull() {
        // Deve lanciare l'eccezione IllegalArgumentException
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            listaUtenti.eliminazioneUtente(null);
        });
        
        // Verifico il  messaggio
        assertEquals("L'Utente non può essere Null!", e.getMessage());
    }

    @Test
    @DisplayName("Eliminazione: Eccezione Utente Non Presente")
    void testEliminazioneUtenteNonTrovato() {
        // Deve lanciare l'eccezione UtenteNotFoundException
        assertThrows(UtenteNotFoundException.class, () -> {
            listaUtenti.eliminazioneUtente(u1); // u1 non è mai stato aggiunto
        });
    }
    
    @Test
    @DisplayName("Eliminazione: Utente senza prestiti (Successo)")
    void testEliminazioneUtenteSenzaPrestiti() throws Exception {
        // Inserisco un utente u1 nella lista
        listaUtenti.registrazioneUtente(u1);
        // Verifica salvataggio post-registrazione
        assertTrue(new File(TEST_FILENAME).exists()); 

        // Elimino l'utente inserito u1
        listaUtenti.eliminazioneUtente(u1);

        // ASSERT
        assertEquals(0, listaUtenti.getListaUtenti().size());
        assertNull(listaUtenti.getUtenteByMatricola(MATRICOLA_VALIDA_U1));
        
        // Verifica persistenza (il file deve esistere ed essere stato aggiornato)
        assertTrue(new File(TEST_FILENAME).exists());
    }

    @Test
    @DisplayName("Eliminazione: Errore Utente con Prestito Attivo")
    void testEliminazioneUtenteConPrestito() throws Exception {
        // Aggiungo un nuovo utente u1 alla lista
        listaUtenti.registrazioneUtente(u1);
        
        // Aggiungo un prestito all'utente (u1)
        Prestito p = new Prestito("9788808123456", MATRICOLA_VALIDA_U1);
        u1.addPrestito(p); 

        // Deve lanciare l'eccezione
        UtenteWithPrestitoException e = assertThrows(UtenteWithPrestitoException.class, () -> {
            listaUtenti.eliminazioneUtente(u1);
        });

        assertEquals("L'utente ha un prestito attivo!\nEliminare prima il prestito!", e.getMessage());
        
        // Verifico che l'utente sia ancora lì
        assertEquals(1, listaUtenti.getListaUtenti().size());
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
    
    // TEST TO STRING

    @Test
    @DisplayName("toString: Lista Vuota")
    void testToStringListaVuota() {
        // Se la lista è vuota, stampa solo l'intestazione inizializzata nel StringBuffer
        String attesa = "Lista Utenti:";
        assertEquals(attesa, listaUtenti.toString());
    }

    @Test
    @DisplayName("toString: Lista con Utenti (Verifica Ordinamento)")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testToStringListaPiena() throws Exception {
        // Inserisco gli utenti in ordine sparso
        listaUtenti.registrazioneUtente(u1); // Rossi
        listaUtenti.registrazioneUtente(u2); // Bianchi
        listaUtenti.registrazioneUtente(u3); // Rossetti

        /*
         * COSTRUZIONE ATTESA:
         * ListaUtenti usa un TreeSet, quindi gli utenti verranno stampati in ordine alfabetico
         * in base al compareTo (Cognome -> Nome -> Matricola).
         * * Ordine previsto:
         * 1. Bianchi (u2)
         * 2. Rossetti (u3) 
         * 3. Rossi (u1)
         */
        
        String separatore = "\n*****\n";
        StringBuilder sbAttesa = new StringBuilder();
        sbAttesa.append("Lista Utenti:");
        
        // 1. Bianchi
        sbAttesa.append(separatore).append(u2.toString());
        // 2. Rossetti
        sbAttesa.append(separatore).append(u3.toString());
        // 3. Rossi
        sbAttesa.append(separatore).append(u1.toString());

        // VERIFICA
        assertEquals(sbAttesa.toString(), listaUtenti.toString(), 
            "Il toString di ListaUtenti non rispetta l'ordinamento o il formato atteso.");
    }
}