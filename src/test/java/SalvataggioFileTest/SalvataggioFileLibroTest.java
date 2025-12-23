/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SalvataggioFileTest;

import SalvataggioFile.SalvataggioFileLibro.SalvataggioFileLibro;
import GestioneLibro.CatalogoLibri;
import GestioneLibro.Libro;

import Eccezioni.EccezioniLibri.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Chris7iaN4
 */
public class SalvataggioFileLibroTest {

    // FIXTURE: Variabili di Ambiente
    private CatalogoLibri catalogoDaSalvare;
    private Libro libroTest;

    private final String ISBN_VALIDO = "9788804503187";
    private final String TITOLO_VALIDO = "Il Signore degli Anelli";
    private final String AUTORI_VALIDI = "J.R.R. Tolkien";
    private final int ANNO = 1954;
    private final int COPIE = 3;

    // Nome file temporaneo
    private static final String FILE_TEST = "test_persistenza_libri_temp.bin";

    
    // FIXTURE

    /**
     * @brief Setup: pulizia ambiente + creazione dati.
     */
    @BeforeEach
    void setUp() throws Exception {

        // Pulizia
        File f = new File(FILE_TEST);
        if (f.exists()) {
            f.delete();
        }

        // Creo un catalogo vuoto
        catalogoDaSalvare = new CatalogoLibri(false, FILE_TEST);

        // Creo un libro di test
        libroTest = new Libro(TITOLO_VALIDO, AUTORI_VALIDI, ANNO, ISBN_VALIDO, COPIE);

        // Registro il libro nel catalogo
        catalogoDaSalvare.registrazioneLibro(libroTest);
    }

    /**
     * @brief Teardown: elimina file generato.
     */
    @AfterEach
    void tearDown() {
        File f = new File(FILE_TEST);
        if (f.exists()) {
            f.delete();
        }
    }

 
    // --- TEST ---

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testSalvaECarica() throws IOException, ClassNotFoundException {

        // SALVATAGGIO
        SalvataggioFileLibro.salva(catalogoDaSalvare, FILE_TEST);

        File fileCreato = new File(FILE_TEST);
        assertTrue(fileCreato.exists(), "Il file dovrebbe essere stato creato.");
        assertTrue(fileCreato.length() > 0, "Il file non dovrebbe essere vuoto.");

        // CARICAMENTO
        CatalogoLibri catalogoCaricato = SalvataggioFileLibro.carica(FILE_TEST);

        assertNotNull(catalogoCaricato, "Il catalogo caricato non deve essere null.");

        // Verifico la presenza del libro caricato
        Libro libroRecuperato = catalogoCaricato.getLibroByISBN(ISBN_VALIDO);

        assertNotNull(libroRecuperato, "Il libro salvato dovrebbe essere presente nel catalogo caricato.");
        assertEquals(TITOLO_VALIDO, libroRecuperato.getTitolo(), "Il titolo deve corrispondere.");
        assertEquals(AUTORI_VALIDI, libroRecuperato.getAutori(), "Gli autori devono corrispondere.");
    }

    // verifica il lancio dell'eccezione quando si prova a salvare un file null
    @Test
    void testSalvaCatalogoNull() {
        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFileLibro.salva(null, FILE_TEST);
        });
        assertEquals("Non puoi salvare un oggetto vuoto!", ex.getMessage());
    }

    // verifica il lancio dell'eccezione quando si prova a salvare un file con un percorso null
    @Test
    void testSalvaFilenameNull() {
        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFileLibro.salva(catalogoDaSalvare, null);
        });
        assertEquals("Percorso non specificato!", ex.getMessage());
    }

    
    // verifica il lancio dell'eccezione quando si vuole caricare un file non esistente 
    @Test
    void testCaricaFileNonTrovato() {

        String fileFantasma = "file_non_esistente_libri.bin";

        File f = new File(fileFantasma);
        if (f.exists()) f.delete();

        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFileLibro.carica(fileFantasma);
        });

        assertEquals("File non trovato!", ex.getMessage());
    }


    // verifica il lancio dell'eccezione quando si vuole caricare un file con un percorso null
    @Test
    void testCaricaFilenameNull() {

        IOException ex = assertThrows(IOException.class, () -> {
            SalvataggioFileLibro.carica(null);
        });

        assertEquals("Percorso non specificato!", ex.getMessage());
    }
}
