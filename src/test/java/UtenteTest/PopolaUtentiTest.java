/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UtenteTest;

import GestioneUtente.PopolaUtenti;
import GestioneUtente.ListaUtenti;
import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author chiara
 */
public class PopolaUtentiTest {
    // FIXTURE: varabili di ambiente
    private static final String TARGET_FILENAME = "listaUtenti.bin";
    
    /*Poiché nella classe PopolaUtenti il nome del file "listaUtenti.bin" è hardcoded (scritto fisso nel codice), 
    il test lavorerà su quel file reale. 
    Il metodo setUp cancellerà quel file per partire da una situazione pulita.*/
    
    /**
     * @brief Pulisce l'ambiente prima di ogni test.
     * Cancella il file listaUtenti.bin se esiste, per garantire che
     * il test verifichi la creazione da zero.
     */
    @BeforeEach
    void setUp() {
        File f = new File(TARGET_FILENAME);
        if (f.exists()) {
            f.delete();
        }
    }

    /**
     * @brief Pulisce l'ambiente dopo ogni test.
     * Rimuove il file creato dal test per non lasciare sporcizia nel progetto.
     */
    @AfterEach
    void tearDown() {
        File f = new File(TARGET_FILENAME);
        if (f.exists()) {
            f.delete();
        }
    }
    
    @Test
    @DisplayName("Esecuzione Main: Creazione File e Verifica Contenuto")
    @Timeout(value = 5, unit = TimeUnit.SECONDS) // Timeout un po' più alto perché fa molto I/O
    void testMainExecution() throws IOException, ClassNotFoundException {
        // ACT: Eseguo il main della classe. 
        // Passo null o un array vuoto perché il main non usa gli args.
        PopolaUtenti.main(new String[]{});

        // ASSERT 1: Verifico che il file sia stato creato fisicamente
        File fileProdotto = new File(TARGET_FILENAME);
        assertTrue(fileProdotto.exists(), "Il file " + TARGET_FILENAME + " dovrebbe essere stato creato.");
        assertTrue(fileProdotto.length() > 0, "Il file non dovrebbe essere vuoto.");

        // ASSERT 2: Verifico il contenuto logico ricaricando il file
        // Uso la classe SalvataggioFileUtente per rileggere ciò che PopolaUtenti ha scritto
        ListaUtenti catalogoRicaricato = SalvataggioFileUtente.carica(TARGET_FILENAME);

        assertNotNull(catalogoRicaricato, "L'oggetto ricaricato non deve essere null.");
        
        // Verifica che ci siano esattamente 10 utenti (quelli nell'array datiUtenti)
        assertEquals(10, catalogoRicaricato.getListaUtenti().size(), "Dovrebbero esserci 10 utenti nel file.");

        // Verifica a campione di un utente (Il primo: Mario Rossi)
        assertNotNull(catalogoRicaricato.getUtenteByMatricola("0000111122"), "Mario Rossi dovrebbe essere presente.");
        
        // Verifica a campione dell'ultimo utente (Davide Marino)
        assertNotNull(catalogoRicaricato.getUtenteByMatricola("1111000011"), "Davide Marino dovrebbe essere presente.");
    }

    @Test
    @DisplayName("Test Idempotenza: Esecuzione doppia senza crash")
    @Timeout(5)
    void testDoppiaEsecuzione() throws IOException, ClassNotFoundException {
        // Scenario: Lancio il programma una volta per popolare la lista
        PopolaUtenti.main(null);
        
        // Lancio il programma una SECONDA volta sullo stesso file.
        // La classe dovrebbe intercettare l'eccezione UtentePresenteException e stampare a video,
        // ma NON deve lanciare eccezioni verso l'esterno o crashare.
        assertDoesNotThrow(() -> {
            PopolaUtenti.main(null);
        });

        // Verifico che il file sia ancora coerente e non abbia duplicati
        ListaUtenti catalogoRicaricato = SalvataggioFileUtente.carica(TARGET_FILENAME);
        
        // La dimensione deve restare 10, perché i duplicati vengono scartati dal Set
        assertEquals(10, catalogoRicaricato.getListaUtenti().size(), "La dimensione deve rimanere 10 anche dopo la seconda esecuzione.");
    }
}
