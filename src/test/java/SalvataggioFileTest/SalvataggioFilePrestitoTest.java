/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SalvataggioFileTest;

import Eccezioni.EccezioniLibri.LibroNotFoundException;
import Eccezioni.EccezioniPrestiti.CopieEsauriteException;
import Eccezioni.EccezioniPrestiti.PrestitiEsauritiException;
import Eccezioni.EccezioniUtenti.UtenteNotFoundException;
import GestioneLibro.CatalogoLibri;
import SalvataggioFile.SalvataggioFilePrestito.SalvataggioFilePrestito;
import GestionePrestito.ElencoPrestiti;
import GestionePrestito.GestorePrestito;
import GestionePrestito.Prestito;
import GestionePrestitoTest.ElencoPrestitiTest;
import GestioneUtente.ListaUtenti;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Chris7iaN4
 */

public class SalvataggioFilePrestitoTest {

    private ElencoPrestiti elencoDaSalvare;
    private Prestito prestitoTest;

    private final String ISBN = "9788804503187";
    private final String MATRICOLA = "1234567890";
    private final String FILENAMELIBRI_TEST = "stub_libri.dat";
    private final String FILENAMEUTENTI_TEST = "stub_utenti.dat";

    // Nome file temporaneo
    private static final String FILE_TEST = "test_persistenza_prestiti_temp.bin";


    class GestoreStub extends GestorePrestito {
        
        // Flag di controllo
        public boolean risultatoNuovoPrestito = true; 
        public boolean lanciaEccezioneLibro = false;  
        public boolean lanciaEccezioneUtente = false;
        public boolean lanciaEccezioneCopia = false;
        public boolean lanciaEccezionePrestito = false;
        
        public GestoreStub() throws IOException, ClassNotFoundException {
            // Passiamo file fittizi
            super(FILENAMELIBRI_TEST, FILENAMEUTENTI_TEST);
        }

        @Override
        public boolean nuovoPrestito(String isbn, String matricola) throws LibroNotFoundException, UtenteNotFoundException, CopieEsauriteException, PrestitiEsauritiException {
            if (lanciaEccezioneLibro) {
                throw new LibroNotFoundException();
            }
            if(lanciaEccezioneUtente) {
                throw new UtenteNotFoundException();
            }
            if(lanciaEccezioneCopia) {
                throw new CopieEsauriteException();
            }
            if(lanciaEccezionePrestito) {
                throw new PrestitiEsauritiException();
            }
            return risultatoNuovoPrestito;
        }
        @Override
        public void diminuisciCopiaPrestitoLibro(String isbn) {
            // Non fare nulla: stiamo simulando che sia andato tutto bene
        }

        @Override
        public void aggiungiPrestitoListaUtente(String matricola, Prestito p) {
            // Non fare nulla
        }

        @Override
        public void aggiungiCopiaPrestitoLibro(String isbn) {
            // Non fare nulla (serve per il test di eliminazione)
        }

        @Override
        public void rimuoviPrestitoListaUtente(String matricola, Prestito p) {
            // Non fare nulla (serve per il test di eliminazione)
        }
    }
    
    @BeforeEach
    void setUp() throws Exception {

        // Pulizia preventiva
        File f = new File(FILE_TEST);
        if (f.exists()) {
            f.delete();
        }
        
        File fl = new File(FILENAMELIBRI_TEST);
        fl.createNewFile(); 
            
        try (ObjectOutputStream outLibri = new ObjectOutputStream(new FileOutputStream(fl))) {
            outLibri.writeObject(new CatalogoLibri());
        }
            
        File fu = new File(FILENAMEUTENTI_TEST);
        fu.createNewFile(); 
        try (ObjectOutputStream outUtenti = new ObjectOutputStream(new FileOutputStream(fu))) {
            outUtenti.writeObject(new ListaUtenti()); 
        }
        
        GestoreStub gestoreStub = new GestoreStub();
        // Creo un ElencoPrestiti senza gestore (null), per test persistenza
        elencoDaSalvare = new ElencoPrestiti(false, FILE_TEST, gestoreStub);

        // Creo un prestito manualmente
        
        elencoDaSalvare.registrazionePrestito(ISBN, MATRICOLA);
    }

    // FIXTURE

    @AfterEach
    void tearDown() {
        File f = new File(FILE_TEST);
        if (f.exists()) {
            f.delete();
        }
        
        File fl = new File(FILENAMELIBRI_TEST);
        if (fl.exists()) {
            fl.delete();
        }
        
        File fu = new File(FILENAMEUTENTI_TEST);
        if (fu.exists()) {
            fu.delete();
        }
    }

    // --- TEST ---
    
    @Test
    @DisplayName("Salvataggio e Caricamento Prestiti (Round-Trip) con Timeout")
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testSalvaECarica() throws IOException, ClassNotFoundException {

        // SALVATAGGIO
        SalvataggioFilePrestito.salva(elencoDaSalvare, FILE_TEST);

        File fileCreato = new File(FILE_TEST);
        assertTrue(fileCreato.exists(), "Il file deve essere stato creato.");
        assertTrue(fileCreato.length() > 0, "Il file non dovrebbe essere vuoto.");

        // CARICAMENTO
        ElencoPrestiti elencoCaricato = SalvataggioFilePrestito.carica(FILE_TEST);

        assertNotNull(elencoCaricato, "L'elenco caricato non deve essere null.");
        assertNotNull(elencoCaricato.getElencoPrestiti(), "La lista interna non deve essere null.");
        assertFalse(elencoCaricato.getElencoPrestiti().isEmpty(), "L'elenco caricato non deve essere vuoto.");

        // Controllo che il prestito sia stato recuperato correttamente
        Prestito prestitoRecuperato = elencoCaricato.getElencoPrestiti().get(0);

        assertEquals(MATRICOLA, prestitoRecuperato.getMatricolaUtente(), "La matricola dell'utente deve corrispondere.");
        assertEquals(ISBN, prestitoRecuperato.getISBNLibro(), "L'ISBN del libro deve corrispondere.");
        assertNotNull(prestitoRecuperato.getDataRestituzione(), "La data di restituzione non deve essere null.");
    }


    @Test
    void testSalvaElencoNull() {
        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFilePrestito.salva(null, FILE_TEST);
        });
        assertEquals("Non puoi salvare un oggetto vuoto!", ex.getMessage());
    }

    @Test
    void testSalvaFilenameNull() {
        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFilePrestito.salva(elencoDaSalvare, null);
        });
        assertEquals("Percorso non specificato!", ex.getMessage());
    }


    @Test
    void testCaricaFileNonTrovato() {
        String fileFantasma = "file_non_esistente_prestiti.bin";

        File f = new File(fileFantasma);
        if (f.exists()) f.delete();

        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFilePrestito.carica(fileFantasma);
        });

        assertEquals("File non trovato!", ex.getMessage());
    }

    @Test
    void testCaricaFilenameNull() {
        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFilePrestito.carica(null);
        });

        assertEquals("Percorso non specificato!", ex.getMessage());
    }
}
