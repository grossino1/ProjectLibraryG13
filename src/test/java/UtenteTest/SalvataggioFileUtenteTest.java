/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UtenteTest;

import SalvataggioFile.SalvataggioFileUtente.SalvataggioFileUtente;
import GestioneUtente.ListaUtenti;
import GestioneUtente.Utente;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author chiara
 */
public class SalvataggioFileUtenteTest {
    // FIXTURE: Variabili di Ambiente
    private ListaUtenti listaDaSalvare;
    private Utente utenteTest;
    private final String NOME_VALIDO = "Mario";
    private final String COGNOME_VALIDO = "Rossi";
    private final String MATRICOLA_VALIDA = "1234567890";
    private final String EMAIL_VALIDA = "m.rossi1@studenti.unisa.it";
    
    // Nome del file temporaneo che viene usato per il test
    private static final String FILE_TEST = "test_persistenza_temp.bin";
    
    // FIXTURE: BeforEach e AfterEach
    
    /**
     * @brief Setup: Pulisce l'ambiente e prepara i dati.
     * Esegue la pulizia preventiva (defensive programming) e crea un oggetto ListaUtenti popolato.
     */
    @BeforeEach
    void setUp() throws Exception {
        // 1. Pulizia preventiva: Se è rimasto sporco da un crash precedente, lo cancello
        File f = new File(FILE_TEST);
        if (f.exists()) {
            f.delete();
        }
        // 2. Inizializzazione dati
        // Creo una lista vuota (false = non caricare da file)
        listaDaSalvare = new ListaUtenti(false, FILE_TEST);
        
        // Creo un utente e lo aggiungo, così salviamo qualcosa di concreto e non una lista vuota
        utenteTest = new Utente(NOME_VALIDO, COGNOME_VALIDO, MATRICOLA_VALIDA, EMAIL_VALIDA);
        
        // Registro l'utente creato nella lista
        listaDaSalvare.registrazioneUtente(utenteTest);
    }
    
    /**
     * @brief Teardown: Pulisce l'ambiente dopo il test.
     * Rimuove il file creato per non lasciare residui nel progetto.
     */
    @AfterEach
    void tearDown() {
        File f = new File(FILE_TEST);
        if (f.exists()) {
            f.delete();
        }
    }
    
    // TEST SALVA()
    @Test
    @DisplayName("Salvataggio e Caricamento (Round-Trip) con Timeout")
    @Timeout(value = 2, unit = TimeUnit.SECONDS) // Fallisce se l'I/O dura troppo
    void testSalvaECarica() throws IOException, ClassNotFoundException {
        // FASE 1: SALVATAGGIO 
        SalvataggioFileUtente.salva(listaDaSalvare, FILE_TEST);

        // ASSERT (Verifica fisica del file)
        File fileCreato = new File(FILE_TEST);
        assertTrue(fileCreato.exists(), "Il file binario dovrebbe essere stato creato su disco.");
        assertTrue(fileCreato.length() > 0, "Il file non dovrebbe essere vuoto.");

        // FASE 2: CARICAMENTO 
        ListaUtenti listaCaricata = SalvataggioFileUtente.carica(FILE_TEST);

        // ASSERT (Verifica logica dei dati)
        assertNotNull(listaCaricata, "L'oggetto caricato non deve essere null.");
        
        // Verifico che i dati dentro l'oggetto caricato siano corretti.
        // Recupero l'utente dalla lista appena caricata dal file.
        Utente utenteRecuperato = listaCaricata.getUtenteByMatricola(MATRICOLA_VALIDA);
        
        assertNotNull(utenteRecuperato, "L'utente inserito dovrebbe essere presente nella lista caricata.");
        assertEquals(COGNOME_VALIDO, utenteRecuperato.getCognome(), "Il cognome dell'utente caricato deve corrispondere.");
        assertEquals(NOME_VALIDO, utenteRecuperato.getNome(), "Il nome dell'utente caricato deve corrispondere.");
    }

    @Test
    @DisplayName("Errore: Salvataggio con parametri Null")
    void testSalvaParametriNull() {
        // Testo il controllo: if(dati == null)
        IOException ex1 = assertThrows(IOException.class, () -> {
            SalvataggioFileUtente.salva(null, FILE_TEST);
        });
        assertEquals("Non puoi salvare un oggetto vuoto!", ex1.getMessage());

        // Testo il controllo: if(filename == null)
        IOException ex2 = assertThrows(IOException.class, () -> {
            SalvataggioFileUtente.salva(listaDaSalvare, null);
        });
        assertEquals("Percorso non specificato!", ex2.getMessage());
    }

    // TEST CARICA()
    @Test
    @DisplayName("Errore: Caricamento File Inesistente")
    void testCaricaFileNonTrovato() {
        String fileFantasma = "fileNonEsistente.bin";
        
        // Mi assicuro che il file non esista davvero
        File f = new File(fileFantasma);
        if(f.exists()) f.delete();

        // Verifico che lanci IOException 
        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFileUtente.carica(fileFantasma);
        });
        
        assertEquals("File non trovato!", ex.getMessage());
    }

    @Test
    @DisplayName("Errore: Caricamento con Filename Null")
    void testCaricaFilenameNull() {
        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFileUtente.carica(null);
        });
        assertEquals("Percorso non specificato!", ex.getMessage());
    }
}
