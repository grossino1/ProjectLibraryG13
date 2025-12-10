/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UtenteTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import GestioneUtente.Utente;
import GestionePrestito.Prestito;
import Eccezioni.EccezioniUtenti.MatricolaNotValidException;
import Eccezioni.EccezioniPrestiti.PrestitiEsauritiException;
import Eccezioni.EccezioniPrestiti.PrestitoNonTrovatoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit; 

/**
 * Classe di test per Utente senza l'utilizzo di Mockito.
 * Utilizza oggetti reali o stub manuali.
 * * @author chiara
 */
public class UtenteTest {
    
    // FIXTURE: Variabili di Istanza
    private Utente utente;
    private final String NOME_VALIDO = "Mario";
    private final String COGNOME_VALIDO = "Rossi";
    private final String MATRICOLA_VALIDA = "1234567890";
    private final String EMAIL_VALIDA = "m.rossi1@studenti.unisa.it";
            
    // Oggetto Prestito reale
    private Prestito prestito;
    
    // FIXTURE: Setup (@BeforeEach)
    @BeforeEach
    void setUp() throws MatricolaNotValidException {
        // Inizializzo l'utente
        utente = new Utente(NOME_VALIDO, COGNOME_VALIDO, MATRICOLA_VALIDA, EMAIL_VALIDA);
        
        // Inizializzo un oggetto Prestito reale usando il costruttore fornito.
        // Uso un ISBN inventato e la matricola dell'utente corrente.
        prestito = new Prestito("978-88-000-0000-1", MATRICOLA_VALIDA);
    }
    
    @AfterEach
    void tearDown(){
        utente = null;
        prestito = null;
    }
    
    // TEST COSTRUTTORE E VALIDAZIONE MATRICOLA
    @Test
    @DisplayName("Costruttore: Creazione Valida")
    void testCostruttoreValido(){
        assertDoesNotThrow(() -> {
            new Utente(NOME_VALIDO, COGNOME_VALIDO, MATRICOLA_VALIDA, EMAIL_VALIDA);
        });    
    }
    
    @Test
    @DisplayName("Costruttore: Matricola Null -> IllegalArgumentException")
    void testCostruttoreMatricolaNull(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Utente(NOME_VALIDO, COGNOME_VALIDO, null, EMAIL_VALIDA);
        });  
        assertEquals("La matricola non può essere nulla!", exception.getMessage());
    }
    
    @Test
    @DisplayName("Costruttore: Matricola Corta -> MatricolaNotValidException")
    void testCostruttoreMatricolaCorta(){
        MatricolaNotValidException exception = assertThrows(MatricolaNotValidException.class, () -> {
            new Utente(NOME_VALIDO, COGNOME_VALIDO, "123", EMAIL_VALIDA);
        });  
        assertEquals("La matricola deve essere di 10 cifre!", exception.getMessage());
    }
    
    @Test
    @DisplayName("Costruttore: Matricola Alfanumerica -> MatricolaNotValidException")
    void testCostruttoreMatricolaAlfanumerica(){
        MatricolaNotValidException exception = assertThrows(MatricolaNotValidException.class, () -> {
            new Utente(NOME_VALIDO, COGNOME_VALIDO, "12DFT34", EMAIL_VALIDA);
        });  
        assertEquals("La matricola deve essere di 10 cifre!", exception.getMessage());
    }
    
    // TEST LOGICI
    @Test
    @DisplayName("getListaDataRestituzione: estrazione corretta")
    void testGetListaDataRestituzione() {
        LocalDate dataAttesa = LocalDate.of(2023, 12, 25);
        
        // STUB MANUALE (Sostituisce il Mock):
        // Creiamo una classe anonima che estende Prestito e sovrascrive getDataRestituzione.
        // In questo modo forziamo il metodo a restituire 'dataAttesa' senza dipendere dalla logica interna di Prestito.
        Prestito prestitoConData = new Prestito("ISBN-TEST-DATA", MATRICOLA_VALIDA) {
            @Override
            public LocalDate getDataRestituzione() {
                return dataAttesa;
            }
        };
        
        // Creo una lista di input
        ArrayList<Prestito> input = new ArrayList<>();
        input.add(prestitoConData);
        
        ArrayList<LocalDate> date = utente.getListaDataRestituzione(input);
        
        assertEquals(1, date.size());
        assertEquals(dataAttesa, date.get(0));
    }
    
    @Test
    @DisplayName("AddPrestito: Eccezione su Null")
    void testAddPrestitoNull() {
        assertThrows(IllegalArgumentException.class, () -> utente.addPrestito(null));
    }
    
    @Test
    @DisplayName("AddPrestito: Limite Massimo Raggiunto (PrestitiEsauritiException)")
    void testAddPrestitoLimiteMassimo() throws PrestitiEsauritiException {
        // 1. Riempiamo la lista fino al limite consentito (3 prestiti)
        utente.addPrestito(prestito);
        utente.addPrestito(prestito);
        utente.addPrestito(prestito);
        
        // Verifica che ce ne siano 3
        assertEquals(3, utente.getListaPrestiti().size());

        // 2. Tentiamo di aggiungere il 4° prestito -> Deve lanciare l'eccezione
        PrestitiEsauritiException exception = assertThrows(PrestitiEsauritiException.class, () -> {
            utente.addPrestito(prestito);
        });

        // 3. Verifica il messaggio dell'eccezione
        assertEquals("L'utente non può avere più di 3 prestiti attivi!", exception.getMessage());
        
        // 4. Verifica che la lista sia rimasta a 3
        assertEquals(3, utente.getListaPrestiti().size());
    }
    
    @Test
    @DisplayName("AddPrestito: Aggiunta valida fino al limite (3)")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testAddPrestitoSottoLimite() {
        assertDoesNotThrow(() -> {
            utente.addPrestito(prestito); // 1° prestito
            utente.addPrestito(prestito); // 2° prestito
            utente.addPrestito(prestito); // 3° prestito (ancora valido)
        });
        
        // Verifica finale: deve averne accettati 3
        assertEquals(3, utente.getListaPrestiti().size());
    }
    
    @Test
    @DisplayName("RimuoviPrestito: Rimozione con successo")
    void testRimuoviPrestitoSuccesso() throws PrestitiEsauritiException, PrestitoNonTrovatoException {
        // 1. Setup: Aggiungo un prestito
        utente.addPrestito(prestito);
        assertEquals(1, utente.getListaPrestiti().size());

        // 2. Azione: Rimuovo il prestito
        assertDoesNotThrow(() -> utente.rimuoviPrestito(prestito));

        // 3. Verifica: La lista deve essere vuota
        assertTrue(utente.getListaPrestiti().isEmpty());
    }

    @Test
    @DisplayName("RimuoviPrestito: Errore Prestito Non Trovato")
    void testRimuoviPrestitoNonTrovato() throws PrestitiEsauritiException {
        // 1. Setup: Aggiungo un prestito "A"
        utente.addPrestito(prestito);
        
        // 2. Creo un secondo prestito "B" (oggetto diverso) che NON aggiungo alla lista
        Prestito prestitoSconosciuto = new Prestito("ISBN-SCONOSCIUTO", MATRICOLA_VALIDA);

        // 3. Azione: Provo a rimuovere "B" -> Deve lanciare PrestitoNonTrovatoException
        PrestitoNonTrovatoException exception = assertThrows(PrestitoNonTrovatoException.class, () -> {
            utente.rimuoviPrestito(prestitoSconosciuto);
        });

        // 4. Verifica messaggio e stato
        assertEquals("Il prestito non è presente nella lista!", exception.getMessage());
        assertEquals(1, utente.getListaPrestiti().size()); 
    }

    @Test
    @DisplayName("RimuoviPrestito: Errore Parametro Null")
    void testRimuoviPrestitoNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            utente.rimuoviPrestito(null);
        });
        
        assertEquals("Errore: Impossibile rimuovere un prestito nullo.", exception.getMessage());
    }

    @Test
    @DisplayName("Workflow: Rimuovi sblocca l'aggiunta (Reset Limite)")
    void testRimuoviPerSbloccareAdd() throws PrestitiEsauritiException, PrestitoNonTrovatoException {
        // 1. Riempiamo fino al limite (3)
        utente.addPrestito(prestito);
        utente.addPrestito(prestito);
        utente.addPrestito(prestito);

        // 2. Rimuoviamo un prestito (chiamata valida)
        utente.rimuoviPrestito(prestito); 
        assertEquals(2, utente.getListaPrestiti().size());

        // 3. Proviamo ad aggiungerne uno nuovo -> Deve funzionare
        assertDoesNotThrow(() -> utente.addPrestito(prestito));
        
        assertEquals(3, utente.getListaPrestiti().size());
    }
       
    // TEST equals E CompareTo

    @Test
    @DisplayName("Equals: Confronto Matricole")
    void testEquals() throws MatricolaNotValidException {
        // Creiamo un altro utente con la stessa matricola
        Utente utenteCopia = new Utente(NOME_VALIDO, COGNOME_VALIDO, MATRICOLA_VALIDA, EMAIL_VALIDA);
        
        // Devono risultare uguali perché equals si basa solo sulla matricola
        assertEquals(utente, utenteCopia);
        
        // Hashcode deve essere identico
        assertEquals(utente.hashCode(), utenteCopia.hashCode());
    }

    @Test
    @DisplayName("CompareTo: Ordinamento")
    void testCompareTo() throws MatricolaNotValidException {
        // utente attuale: Mario Rossi, 1234567890
        
        // Caso 1: Cognome diverso (Bianchi < Rossi)
        Utente u1 = new Utente(NOME_VALIDO, "Bianchi", MATRICOLA_VALIDA, EMAIL_VALIDA);
        assertTrue(utente.compareTo(u1) > 0); // Rossi viene dopo Bianchi

        // Caso 2: Stesso cognome, Nome diverso (Zoro > Mario)
        Utente u2 = new Utente("Orlando", COGNOME_VALIDO, MATRICOLA_VALIDA, EMAIL_VALIDA);
        assertTrue(utente.compareTo(u2) < 0); // Mario viene prima di Zoro

        // Caso 3: Stesso nome/cognome, Matricola diversa
        // Utente ha "1234567890". Creiamo uno con "0000000001"
        Utente u3 = new Utente("Mario", "Rossi", "0000000001", EMAIL_VALIDA);
        assertTrue(utente.compareTo(u3) > 0); // 123... > 000...
    }
}