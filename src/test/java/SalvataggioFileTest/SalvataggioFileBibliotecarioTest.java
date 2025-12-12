/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SalvataggioFileTest;

import SalvataggioFile.SalvataggioFileAutenticazione.SalvataggioFileBibliotecario;
import Autenticazione.Bibliotecario;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Chris7iaN4
 */
public class SalvataggioFileBibliotecarioTest {

    private Bibliotecario bibliotecarioTest;
    private final String USERNAME = "mrossi";
    private final String PASSWORD = "password123";
    
    private static final String FILE_BIN = "us.bin"; 

    // FIXTURE

    @BeforeEach
    void setUp() throws IOException {
        File f = new File(FILE_BIN);
        if (f.exists()) {
            f.delete();
        }

        // Creo bibliotecario di test
        bibliotecarioTest = new Bibliotecario(USERNAME, PASSWORD);
    }

    @AfterEach
    void tearDown() throws IOException {
        File f = new File(FILE_BIN);
        if (f.exists()) {
            f.delete();
        }
    }

    // --- TEST ---
    
    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testSalvaECarica() throws IOException, ClassNotFoundException {

        // SALVATAGGIO
        SalvataggioFileBibliotecario.salva(bibliotecarioTest);

        File fileCreato = new File(FILE_BIN);
        assertTrue(fileCreato.exists(), "Il file dovrebbe essere stato creato.");
        assertTrue(fileCreato.length() > 0, "Il file non dovrebbe essere vuoto.");

        // CARICAMENTO
        Bibliotecario bibliotecarioCaricato = SalvataggioFileBibliotecario.carica();

        assertNotNull(bibliotecarioCaricato, "Il bibliotecario caricato non deve essere null.");
        assertEquals(USERNAME, bibliotecarioCaricato.getUsername(), "Lo username deve corrispondere.");
        assertEquals(PASSWORD, bibliotecarioCaricato.getPassword(), "La password deve corrispondere.");
    }


    @Test

    void testSalvaBibliotecarioNull() {
        IOException ex = assertThrows(IOException.class, () -> {
        SalvataggioFileBibliotecario.salva(null);
    });
    assertEquals("Non puoi salvare un oggetto null!", ex.getMessage());
}



    @Test
    void testCaricaFileNonTrovato() {
        // Rinomino temporaneamente il file reale se esiste
        File f = new File(FILE_BIN);
        File backup = null;
        if (f.exists()) {
            backup = new File("us_backup.bin");
            f.renameTo(backup);
        }

        Bibliotecario b = null;
        try {
            b = SalvataggioFileBibliotecario.carica();
        } catch (IOException e) {
            fail("Non dovrebbe lanciare IOException, deve ritornare null");
        }

        assertNull(b, "Il caricamento di un file inesistente deve ritornare null.");

        // Ripristino backup
        if (backup != null) {
            backup.renameTo(f);
        }
    }
}
