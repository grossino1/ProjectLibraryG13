/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionePrestitoTest;
import Eccezioni.EccezioniPrestiti.dataRestituzioneException;
import GestionePrestito.Prestito;
import java.lang.reflect.Field;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // JUnit esegue questo blocco di codice automaticamente dopo ogni singolo metodo @Test.
    @AfterEach
    public void Pulizia() {
        prestitoValido = null;
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
        
        LocalDate dataPassata = LocalDate.now().minusDays(20);
        assertThrows(dataRestituzioneException.class, () -> {
            prestitoValido.setDataRestituzione(dataLontana);
        });
        
        LocalDate dataNulla = null;
        assertThrows(dataRestituzioneException.class, () -> {
            prestitoValido.setDataRestituzione(dataLontana);
        });
    }
    
    @Test
    public void testEqualsAndHashCode() {
        // Creiamo un secondo prestito
        Prestito prestitoCopia = new Prestito(ISBN_VALIDO, MATRICOLA_VALIDA);

        /*
        Poiché equals si basa sulla Data di Registrazione (che include i nanosecondi),
        Il "metodo helper" è una funzione che ho scritto solo dentro il file di Test. 
        Serve a scavalcare le regole di Java per dire: "Ehi, prendi questo oggetto Prestito, entra di forza nella variabile 
        privata dataRegistrazionePrestito e metti questa data precisa che dico io".
        In questo modo, posso forzare due prestiti ad avere la stessa identica data, così il test equals può passare.
        */ 
        LocalDateTime dataFissa = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        injectDataRegistrazione(prestitoValido, dataFissa);
        injectDataRegistrazione(prestitoCopia, dataFissa);

        // Verifica Uguaglianza
        assertEquals(prestitoValido, prestitoCopia, "Due prestiti con la stessa data di registrazione devono essere uguali");
        
        // Verifica Coerenza HashCode
        assertEquals(prestitoValido.hashCode(), prestitoCopia.hashCode(), "Se due oggetti sono uguali, l'hashCode deve coincidere");

        // Verifica Disuguaglianza
        injectDataRegistrazione(prestitoCopia, dataFissa.plusSeconds(1));
        assertNotEquals(prestitoValido, prestitoCopia, "Date diverse devono rendere i prestiti diversi");
        
        // Casi limite
        assertNotEquals(prestitoValido, null);
        assertNotEquals(prestitoValido, "Una Stringa");
    }

    @Test
    public void testCompareTo() throws dataRestituzioneException {
        Prestito prestitoConfronto = new Prestito("1111111111111", "0000000000");
        
        LocalDate oggi = LocalDate.now();
        
        // Caso 1: Date di restituzione diverse
        prestitoValido.setDataRestituzione(oggi);           // Scade oggi
        prestitoConfronto.setDataRestituzione(oggi.plusDays(1)); // Scade domani
        
        // prestitoValido scade PRIMA -> deve restituire valore negativo
        assertTrue(prestitoValido.compareTo(prestitoConfronto) < 0, "Dovrebbe essere < 0 se scade prima");

        // Caso 2: Stessa data di restituzione, vince la data di registrazione
        prestitoConfronto.setDataRestituzione(oggi); // Ora scadono insieme
        
        // Forziamo le date di creazione
        LocalDateTime prima = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime dopo = LocalDateTime.of(2024, 1, 1, 11, 0);
        
        injectDataRegistrazione(prestitoValido, prima);
        injectDataRegistrazione(prestitoConfronto, dopo);
        
        assertTrue(prestitoValido.compareTo(prestitoConfronto) < 0, "A parità di scadenza, vince chi è stato creato prima");
    }

    @Test
    public void testToString() {
        String risultato = prestitoValido.toString();
        
        assertNotNull(risultato);
        assertTrue(risultato.contains(ISBN_VALIDO));
        assertTrue(risultato.contains(MATRICOLA_VALIDA));
        assertTrue(risultato.contains(String.valueOf(prestitoValido.getDataRegistrazionePrestito())));
        assertTrue(risultato.contains(String.valueOf(prestitoValido.getDataRestituzione())));
    }

    // --- HELPER METHOD ---
    /*
    Questo metodo serve SOLO per i test.
    Ci permette di impostare la dataRegistrazionePrestito (che è privata)
    senza dover aggiungere setter pubblici nella classe originale.
    */
    private void injectDataRegistrazione(Prestito p, LocalDateTime data) {
        try {
            // "dataRegistrazionePrestito" deve essere ESATTAMENTE il nome della variabile nella classe Prestito
            Field field = Prestito.class.getDeclaredField("dataRegistrazionePrestito");
            field.setAccessible(true); // Sblocca l'accesso privato
            field.set(p, data);        // Imposta il valore
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Errore nella configurazione del test: impossibile accedere al campo dataRegistrazionePrestito. " + e.getMessage());
        }
    }
}