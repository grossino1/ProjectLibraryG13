/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestioneUtenteTest;

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
 * @author chiara
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
    private final String ISBN_VALIDO = "1234567890123";
    
    // FIXTURE: Setup (@BeforeEach) e tearDown (@AfterEach)
    
    /**
     * @brief Inizializza l'oggetto Utente e Prestito prima di ogni test.
     */
    @BeforeEach
    void setUp() throws Exception {
        // Inizializzo l'utente
        utente = new Utente(NOME_VALIDO, COGNOME_VALIDO, MATRICOLA_VALIDA, EMAIL_VALIDA);
        
        // Inizializzo un oggetto Prestito reale usando il costruttore fornito.
        // Uso un ISBN e la matricola dell'utente corrente.
        prestito = new Prestito(ISBN_VALIDO, MATRICOLA_VALIDA);
    }
    
    /**
     * @brief Pulisce l'ambiente dopo ogni test.
     */
    @AfterEach
    void tearDown(){
        utente = null;
        prestito = null;
    }
    
    // TEST COSTRUTTORE E VERIFICA SULLA MATRICOLA
    
    @Test
    @DisplayName("Costruttore: Creazione Valida")
    void testCostruttoreValido(){
        // Non deve lanciare eccezioni.
        assertDoesNotThrow(() -> {
            new Utente(NOME_VALIDO, COGNOME_VALIDO, MATRICOLA_VALIDA, EMAIL_VALIDA);
        });    
    }
    
    @Test
    @DisplayName("Costruttore: Matricola Null -> IllegalArgumentException")
    void testCostruttoreMatricolaNull(){
        // Deve lanciare l'eccezione IllegalArgumentException.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Utente(NOME_VALIDO, COGNOME_VALIDO, null, EMAIL_VALIDA);
        });  
        assertEquals("La matricola non può essere nulla!", exception.getMessage());
    }
    
    @Test
    @DisplayName("Costruttore: Matricola Corta -> MatricolaNotValidException")
    void testCostruttoreMatricolaCorta(){
        // Deve lanciare l'eccezione MatricolaNotValidExceptio.
        MatricolaNotValidException exception = assertThrows(MatricolaNotValidException.class, () -> {
            new Utente(NOME_VALIDO, COGNOME_VALIDO, "123", EMAIL_VALIDA);
        });  
        assertEquals("La matricola deve essere di 10 cifre!", exception.getMessage());
    }
    
    @Test
    @DisplayName("Costruttore: Matricola Alfanumerica -> MatricolaNotValidException")
    void testCostruttoreMatricolaAlfanumerica(){
        // Deve lanciare l'eccezione MatricolaNotValidExceptio.
        MatricolaNotValidException exception = assertThrows(MatricolaNotValidException.class, () -> {
            new Utente(NOME_VALIDO, COGNOME_VALIDO, "12DFT34", EMAIL_VALIDA);
        });  
        assertEquals("La matricola deve essere di 10 cifre!", exception.getMessage());
    }
    
    // TEST GetListaDataRestituzione
    
    @Test
    @DisplayName("getListaDataRestituzione: estrazione corretta")
    void testGetListaDataRestituzione() {
        LocalDate dataAttesa = LocalDate.of(2023, 12, 25);
        
        // Creo una classe anonima che estende Prestito e sovrascrive getDataRestituzione.
        // In questo modo forzo il metodo a restituire 'dataAttesa' senza dipendere dalla logica interna di Prestito.
        Prestito prestitoConData = new Prestito(ISBN_VALIDO, MATRICOLA_VALIDA) {
            @Override
            public LocalDate getDataRestituzione() {
                return dataAttesa;
            }
        };
        
        // Creo una lista di input e aggiungo il prestito
        ArrayList<Prestito> input = new ArrayList<>();
        input.add(prestitoConData);
        
        // Prelevo la data di restituzione dal prestito
        ArrayList<LocalDate> date = utente.getListaDataRestituzione(input);
        
        // La lista deve contenere la data dataAttesa
        assertEquals(1, date.size());
        assertEquals(dataAttesa, date.get(0));
    }
    
    // TEST AGGIUNTA PRESTITO
    
    @Test
    @DisplayName("AddPrestito: Eccezione su Null")
    void testAddPrestitoNull() {
        // Deve lanciare l'eccezione IllegalArgumentException.
        assertThrows(IllegalArgumentException.class, () -> utente.addPrestito(null));
    }
    
    @Test
    @DisplayName("AddPrestito: Limite Massimo Raggiunto -> PrestitiEsauritiException")
    void testAddPrestitoLimiteMassimo() throws PrestitiEsauritiException {
        // Riempio la lista fino al limite consentito (3 prestiti)
        utente.addPrestito(prestito);
        utente.addPrestito(prestito);
        utente.addPrestito(prestito);
        
        // Verifico che ci siano 3 prestiti nella lista
        assertEquals(3, utente.getListaPrestiti().size());

        // Tento di aggiungere il 4° prestito -> Deve lanciare l'eccezione
        PrestitiEsauritiException exception = assertThrows(PrestitiEsauritiException.class, () -> {
            utente.addPrestito(prestito);
        });

        // Verifico il messaggio dell'eccezione
        assertEquals("L'utente non può avere più di 3 prestiti attivi!", exception.getMessage());
        
        // Verifico che la lista sia rimasta a 3
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
    
    // TEST RIMOZIONE PRESTITO
    
    @Test
    @DisplayName("RimuoviPrestito: Rimozione con successo")
    void testRimuoviPrestitoSuccesso() throws PrestitiEsauritiException, PrestitoNonTrovatoException {
        // Aggiungo un prestito
        utente.addPrestito(prestito);
        assertEquals(1, utente.getListaPrestiti().size());

        // Rimuovo il prestito
        assertDoesNotThrow(() -> utente.rimuoviPrestito(prestito));

        // La lista deve essere vuota
        assertTrue(utente.getListaPrestiti().isEmpty());
    }

    @Test
    @DisplayName("RimuoviPrestito: Errore Prestito Non Trovato -> PrestitiEsauritiException")
    void testRimuoviPrestitoNonTrovato() throws PrestitiEsauritiException {
        // Aggiungo un prestito 
        utente.addPrestito(prestito);
        
        // Creo un secondo prestito (oggetto diverso) che NON aggiungo alla lista
        Prestito prestitoSconosciuto = new Prestito("1111111111111", MATRICOLA_VALIDA);

        // Provo a rimuovere prestitoSconosciuto -> Deve lanciare PrestitoNonTrovatoException
        PrestitoNonTrovatoException exception = assertThrows(PrestitoNonTrovatoException.class, () -> {
            utente.rimuoviPrestito(prestitoSconosciuto);
        });

        // Verifica messaggio e stato
        assertEquals("Il prestito non è presente nella lista!", exception.getMessage());
        assertEquals(1, utente.getListaPrestiti().size()); 
    }

    @Test
    @DisplayName("RimuoviPrestito: Errore Parametro Null")
    void testRimuoviPrestitoNull() {
        // Deve lanciare l'eccezione IllegalArgumentException.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            utente.rimuoviPrestito(null);
        });
        
        assertEquals("Errore: Impossibile rimuovere un prestito nullo.", exception.getMessage());
    }

    @Test
    @DisplayName("Rimuovo un prestito per sbloccare l'aggiunta (Reset Limite)")
    void testRimuoviPerSbloccareAdd() throws PrestitiEsauritiException, PrestitoNonTrovatoException {
        // Riempiamo fino al limite (3)
        utente.addPrestito(prestito);
        utente.addPrestito(prestito);
        utente.addPrestito(prestito);

        // Rimuovo un prestito (chiamata valida)
        utente.rimuoviPrestito(prestito); 
        assertEquals(2, utente.getListaPrestiti().size());

        // Provo ad aggiungerne uno nuovo -> Deve funzionare
        assertDoesNotThrow(() -> utente.addPrestito(prestito));
        
        assertEquals(3, utente.getListaPrestiti().size());
    }
       
    // TEST EQUALS E COMPARE TO

    @Test
    @DisplayName("Equals: Confronto Matricole")
    void testEquals() throws MatricolaNotValidException {
        // Creo un altro utente con la stessa matricola di utente
        Utente utenteCopia = new Utente(NOME_VALIDO, COGNOME_VALIDO, MATRICOLA_VALIDA, EMAIL_VALIDA);
        
        // Devono risultare uguali perché equals si basa solo sulla matricola
        assertEquals(utente, utenteCopia);
        
        // Hashcode deve essere identico
        assertEquals(utente.hashCode(), utenteCopia.hashCode());
    }

    @Test
    @DisplayName("CompareTo: Ordinamento")
    void testCompareTo() throws MatricolaNotValidException {
        // Utente attuale: Mario Rossi, 1234567890
        
        // Caso 1: Cognome diverso (Bianchi < Rossi)
        Utente u1 = new Utente(NOME_VALIDO, "Bianchi", MATRICOLA_VALIDA, EMAIL_VALIDA);
        assertTrue(utente.compareTo(u1) > 0); // Rossi viene dopo Bianchi

        // Caso 2: Stesso cognome, Nome diverso (Orlando > Mario)
        Utente u2 = new Utente("Orlando", COGNOME_VALIDO, MATRICOLA_VALIDA, EMAIL_VALIDA);
        assertTrue(utente.compareTo(u2) < 0); // Mario viene prima di Orlando

        // Caso 3: Stesso nome/cognome, Matricola diversa
        // Utente ha "1234567890". Ne creo uno con "0000000001"
        Utente u3 = new Utente("Mario", "Rossi", "0000000001", EMAIL_VALIDA);
        assertTrue(utente.compareTo(u3) > 0); // 123... > 000...
        
        Utente utenteDiverso = new Utente("Mario", "Rossi", "0000001234",EMAIL_VALIDA);
        assertTrue(utenteDiverso.compareTo(utente) < 0 || utente.compareTo(utenteDiverso) > 0);
    }
    
    // TEST TOSTRING
    
    @Test
    @DisplayName("toString: Verifica formato stringa (senza prestiti)")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void testToString() {
        // Costruisco la stringa basandomi sull'implementazione della classe
        StringBuilder sbAttesa = new StringBuilder();
        sbAttesa.append("Utente: \n");
        sbAttesa.append("Nome: ").append(NOME_VALIDO);
        sbAttesa.append("Cognome: ").append(COGNOME_VALIDO);
        sbAttesa.append("Matricola: ").append(MATRICOLA_VALIDA);
        sbAttesa.append("E-Mail Istituzionale: ").append(EMAIL_VALIDA);
        
        // Poiché nel setUp la lista prestiti è vuota, l'ArrayList restituisce "[]"
        sbAttesa.append("Elenco dei Prestiti Attivi: []");

        assertEquals(sbAttesa.toString(), utente.toString(), 
            "Il formato della stringa restituita da toString non corrisponde alle attese.");
    }
}