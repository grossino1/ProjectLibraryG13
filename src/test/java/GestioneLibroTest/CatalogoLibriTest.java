/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestioneLibroTest;

/**
 *
 * @author Chris7iaN4
 */
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import Eccezioni.EccezioniLibri.*;
import Eccezioni.EccezioniPrestiti.CopieEsauriteException;
import GestioneLibro.CatalogoLibri;
import GestioneLibro.Libro;

import java.io.IOException;
import java.util.ArrayList;

public class CatalogoLibriTest {

    // --- FIXTURE ---
    private CatalogoLibri catalogo;
    private Libro libroValido;
    private final String ISBN_VALIDO = "9781234567897";
    private final String TITOLO = "Test Libro";
    private final String AUTORI = "Mario Rossi";
    private final int ANNO = 2020;
    private final int COPIE = 5;
    private final String FILENAME = "testCatalogo.ser";

    @BeforeEach
    public void setUp() throws ISBNNotValidException, IOException, ClassNotFoundException {
        catalogo = new CatalogoLibri(false, FILENAME);
        libroValido = new Libro(TITOLO, AUTORI, ANNO, ISBN_VALIDO, COPIE);
    }

    @AfterEach
    public void pulizia() {
        catalogo = null;
        libroValido = null;
    }

    // --- TEST ---
    @Test
    public void testRegistrazioneLibroValido() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        assertTrue(catalogo.getCatalogoLibri().contains(libroValido));
    }

    @Test
    public void testRegistrazioneLibroDuplicato() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        assertThrows(LibroPresenteException.class, () -> {
            catalogo.registrazioneLibro(libroValido);
        });
    }

    @Test
    public void testRegistrazioneLibroNull() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.registrazioneLibro(null);
        });
    }

    @Test
    public void testRegistrazioneLibro() throws Exception {
        
        catalogo.registrazioneLibro(libroValido);
        assertThrows(LibroPresenteException.class, () -> {
            catalogo.registrazioneLibro(libroValido);
        });
        
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.registrazioneLibro(null);
        });
        for (int i = 0; i < 999; i++) {
            String is = String.format("6543217890%03d", i);
            Libro l = new Libro(TITOLO + i, AUTORI, ANNO, is, COPIE);
            catalogo.registrazioneLibro(l);
                }

        // Ora aggiungere un altro libro deve lanciare CatalogoPienoException
        Libro libroExtra = new Libro("Extra", "Autore", 2022, "9781234567899", COPIE);
        assertThrows(CatalogoPienoException.class, () -> {
            catalogo.registrazioneLibro(libroExtra);
        });
    }
                
    

    @Test
    public void testEliminazioneLibroValido() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        catalogo.eliminazioneLibro(libroValido);
        assertFalse(catalogo.getCatalogoLibri().contains(libroValido));
    }

    @Test
    public void testEliminazioneLibroNonPresente() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.eliminazioneLibro(libroValido);
        });
    }

    @Test
    public void testEliminazioneLibroNull() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.eliminazioneLibro(null);
        });
    }

    @Test
    public void testCercaLibroByTitolo() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        ArrayList<Libro> risultati = catalogo.cercaLibro("Test");
        assertTrue(risultati.contains(libroValido));
    }

    @Test
    public void testCercaLibroByAutore() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        ArrayList<Libro> risultati = catalogo.cercaLibro("Mario");
        assertTrue(risultati.contains(libroValido));
    }

    @Test
    public void testCercaLibroByISBN() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        ArrayList<Libro> risultati = catalogo.cercaLibro("9781234567897");
        assertTrue(risultati.contains(libroValido));
    }

    @Test
    public void testCercaLibroNonPresente() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.cercaLibro("Non Esiste");
        });
    }

    @Test
    public void testModificaLibroValido() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        catalogo.modificaLibro(libroValido, "Nuovo Titolo", "Nuovi Autori", 2021, 10);
        assertEquals("Nuovo Titolo", libroValido.getTitolo());
        assertEquals("Nuovi Autori", libroValido.getAutori());
        assertEquals(2021, libroValido.getAnnoPubblicazione());
        assertEquals(10, libroValido.getNumeroCopie());
    }

    @Test
    public void testModificaLibroNull() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.modificaLibro(null, "Titolo", "Autore", 2022, 3);
        });
    }

    @Test
    public void testGetLibroByISBNPresente() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        Libro trovato = catalogo.getLibroByISBN(ISBN_VALIDO);
        assertEquals(libroValido, trovato);
    }

    @Test
    public void testGetLibroByISBNNonPresente() {
        Libro trovato = catalogo.getLibroByISBN("0000000000000");
        assertNull(trovato);
    }

    @Test
    public void testGetCatalogoLibri() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        assertEquals(1, catalogo.getCatalogoLibri().size());
    }

    @Test
    public void testToStringCatalogoVuoto() {
        String descrizione = catalogo.toString();
        assertNotNull(descrizione);
        assertTrue(descrizione.contains("vuoto"));
    }

    @Test
    public void testToStringCatalogoConLibri() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        String descrizione = catalogo.toString();
        assertNotNull(descrizione);
        assertTrue(descrizione.contains(ISBN_VALIDO));
        assertTrue(descrizione.contains(TITOLO));
    }
}
