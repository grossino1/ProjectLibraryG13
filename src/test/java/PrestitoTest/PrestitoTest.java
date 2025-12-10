/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrestitoTest;
import Eccezioni.EccezioniPrestiti.dataRestituzioneException;
import GestionePrestito.Prestito;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;
import java.time.LocalDate;

public class PrestitoTest {

    // --- FIXTURE ---
    /*
     Per evitare la duplicazione, in JUnit si crea delle fixture, ovvero
     un insieme di oggetti (memorizzati in variabili di
     istanza della classe di test) reinizializzati allo stesso
     modo prima di lanciare ogni metodo di test
    */ 
    private Prestito prestitoValido;
    private final String ISBN_VALIDO = "9781234567897";
    private final String MATRICOLA_VALIDA = "1234567890";

    // JUnit esegue questo blocco di codice automaticamente prima di ogni singolo metodo @Test.
    @BeforeEach
    public void setUp() {
        // Creiamo un oggetto pulito prima di ogni test
        prestitoValido = new Prestito(ISBN_VALIDO, MATRICOLA_VALIDA);
    }

    // --- TEST ---
    
    @Test
    public void testCostruttore() {
        // Verifica che l'oggetto creato nel setUp sia corretto
        assertNotNull(prestitoValido);
        assertEquals(ISBN_VALIDO, prestitoValido.getISBNLibro());
        assertEquals(MATRICOLA_VALIDA, prestitoValido.getMatricolaUtente());
    }
    
    @Test
    public void testCostruttoreISBNInvalido() {
        
        //assertThrows indica: Mi aspetto che venga lanciata questo tipo di eccezione.
        // Caso 1: ISBN Null
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestito(null, MATRICOLA_VALIDA);
        });

        // Caso 2: ISBN Vuoto
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestito("", MATRICOLA_VALIDA);
        });

        // Caso 3: ISBN Formato errato (es. contiene lettere o lunghezza errata)
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestito("AB12345678901", MATRICOLA_VALIDA); // Lettere invece di numeri
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestito("123", MATRICOLA_VALIDA); // Troppo corto
        });
    }

    @Test
    public void testCostruttoreMatricolaInvalida() {
        // Caso 1: Matricola Null
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestito(ISBN_VALIDO, null);
        });

        // Caso 2: Matricola Vuota
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestito(ISBN_VALIDO, "");
        });

        // Caso 3: Matricola Formato errato
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestito(ISBN_VALIDO, "123"); // Troppo corta (deve essere 10)
        });
    }

    @Test
    // Verifica che il setter sia veloce
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS) 
    public void testSetDataRestituzione() throws dataRestituzioneException {
        LocalDate nuovaData = LocalDate.now().plusDays(15);
        
        // Se questo metodo lancia un'eccezione, il test fallisce automaticamente.
        prestitoValido.setDataRestituzione(nuovaData); 

        // Asserzione
        assertEquals(nuovaData, prestitoValido.getDataRestituzione());
    }

    @Test
    public void testSetDataRestituzioneError() {
        // Testiamo il caso di errore (data > 30 giorni)
        LocalDate dataLontana = LocalDate.now().plusDays(40);
        
        assertThrows(dataRestituzioneException.class, () -> {
            prestitoValido.setDataRestituzione(dataLontana);
        });
    }
}