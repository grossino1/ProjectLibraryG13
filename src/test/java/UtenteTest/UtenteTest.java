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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit; // Necessario per l'unità di misura del tempo

import static org.mockito.Mockito.when;


/**
 *
 * @author chiara
 */
@ExtendWith(MockitoExtension.class)
public class UtenteTest {
    // FIXTURE: Variabili di Istanza
    private Utente utente;
    private final String NOME_VALIDO = "Mario";
    private final String COGNOME_VALIDO = "Rossi";
    private final String MATRICOLA_VALIDA = "1234567890";
    private final String EMAIL_VALIDA = "m.rossi1@studenti.unisa.it";
            
    // Mock della dipendenza Prestito
    @Mock
    private Prestito prestitoMock;
    
    // FIXTURE: Setup (@BeforeEach)
    
    // Utilizzato prima di ogni test
    // Inizializzo l'utente con una matricola valida
    @BeforeEach
    void setUp() throws MatricolaNotValidException{
        utente = new Utente(NOME_VALIDO , COGNOME_VALIDO , MATRICOLA_VALIDA , EMAIL_VALIDA);
    }
    
    @AfterEach
    // Utilizzato dopo ogni test per pulire la  memoria
    void tearDown(){
        utente = null;
    }
    
    // TEST COSTRUTTORE E VALIDAZIONE MATRICOLA
    @Test
    @DisplayName("Costruttore: Creazione Valida")
    void testCostruttoreValido(){
        assertDoesNotThrow(() -> {
            new Utente(NOME_VALIDO , COGNOME_VALIDO , MATRICOLA_VALIDA , EMAIL_VALIDA);
        });   
    }
    
    @Test
    @DisplayName("Costruttore: Matricola Null -> IllegalArgumentException")
    void testCostruttoreMatricolaNull(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Utente(NOME_VALIDO , COGNOME_VALIDO , "123" , EMAIL_VALIDA);
        });  
        assertEquals("La matricola non può essere nulla!", exception.getMessage());
    }
    
    @Test
    @DisplayName("Costruttore: Matricola Corta -> MatricolaNotValidException")
    void testCostruttoreMatricolaCorta(){
        MatricolaNotValidException exception = assertThrows(MatricolaNotValidException.class, () -> {
            new Utente(NOME_VALIDO , COGNOME_VALIDO , null , EMAIL_VALIDA);
        });  
        assertEquals("La matricola deve essere di 10 cifre!", exception.getMessage());
    }
    
    @Test
    @DisplayName("Costruttore: Matricola Corta -> MatricolaNotValidException")
    void testCostruttoreMatricolaAlfanumerica(){
        MatricolaNotValidException exception = assertThrows(MatricolaNotValidException.class, () -> {
            new Utente(NOME_VALIDO , COGNOME_VALIDO , "12DFT34" , EMAIL_VALIDA);
        });  
        assertEquals("La matricola deve essere di 10 cifre!", exception.getMessage());
    }
    
    // TEST LOGICI
    @Test
    @DisplayName("getListaDataRestituzione: estrazione corretta")
    void testGetListaDataRestituzione() {
        // Configuro il mock per restituire una data
        LocalDate dataMock = LocalDate.of(2023, 12, 25);
        when(prestitoMock.getDataRestituzione()).thenReturn(dataMock);
        
        // Creo una lista di input
        ArrayList<Prestito> input = new ArrayList<>();
        input.add(prestitoMock);
        
        ArrayList<LocalDate> date = utente.getListaDataRestituzione(input);
        
        assertEquals(1, date.size());
        assertEquals(dataMock, date.get(0));
    }
    
    @Test
    @DisplayName("AddPrestito: Limite Massimo Raggiunto (PrestitiEsauritiException)")
    void testAddPrestitoLimiteMassimo() throws PrestitiEsauritiException {
        // 1. Riempiamo la lista fino al limite consentito (3 prestiti)
        utente.addPrestito(prestitoMock);
        utente.addPrestito(prestitoMock);
        utente.addPrestito(prestitoMock);
        
        // Verifica che ce ne siano 3
        assertEquals(3, utente.getListaPrestiti().size());

        // 2. Tentiamo di aggiungere il 4° prestito -> Deve lanciare l'eccezione
        PrestitiEsauritiException exception = assertThrows(PrestitiEsauritiException.class, () -> {
            utente.addPrestito(prestitoMock);
        });

        // 3. Verifica il messaggio dell'eccezione
        assertEquals("L'utente non può avere più di 3 prestiti attivi!", exception.getMessage());
        
        // 4. Verifica che la lista sia rimasta a 3 (nessun inserimento sporco)
        assertEquals(3, utente.getListaPrestiti().size());
    }
    
    @Test
    @DisplayName("AddPrestito: Aggiunta valida sotto il limite")
    @Timeout(value = 1, unit = TimeUnit.SECONDS) // Deve essere istantaneo
    void testAddPrestitoSottoLimite() {
        // Verifica che aggiungere 1 o 2 prestiti non dia problemi
        assertDoesNotThrow(() -> {
            utente.addPrestito(prestitoMock); // 1° prestito
            utente.addPrestito(prestitoMock); // 2° prestito
        });
        assertEquals(2, utente.getListaPrestiti().size());
        
        // Verifichiamo che il lancio dell'eccezione sia rapido
        assertThrows(PrestitiEsauritiException.class, () -> {
            utente.addPrestito(prestitoMock);
        });
    }
    
    // TEST equals E CompareTo

    @Test
    @DisplayName("Equals: Confronto Matricole")
    void testEquals() throws MatricolaNotValidException {
        // Creiamo un altro utente con la stessa matricola
        Utente utenteCopia = new Utente(NOME_VALIDO , COGNOME_VALIDO , MATRICOLA_VALIDA , EMAIL_VALIDA);
        
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
        Utente u1 = new Utente(NOME_VALIDO , "Bianchi" , MATRICOLA_VALIDA , EMAIL_VALIDA);
        assertTrue(utente.compareTo(u1) > 0); // Rossi viene dopo Bianchi

        // Caso 2: Stesso cognome, Nome diverso (Zoro > Mario)
        Utente u2 = new Utente("Zoro", "Rossi", "9999999999", "m");
        assertTrue(utente.compareTo(u2) < 0); // Mario viene prima di Zoro

        // Caso 3: Stesso nome/cognome, Matricola diversa
        // Utente ha "1234567890". Creiamo uno con "0000000001"
        Utente u3 = new Utente("Mario", "Rossi", "0000000001", "m");
        assertTrue(utente.compareTo(u3) > 0); // 123... > 000...
    }
    
}
