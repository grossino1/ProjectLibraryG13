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

import Eccezioni.EccezioniLibri.ISBNNotValidException;
import Eccezioni.EccezioniPrestiti.CopieEsauriteException;
import GestioneLibro.Libro;

public class LibroTest {

    // --- FIXTURE ---
    private Libro libroValido;
    private final String ISBN_VALIDO = "9781234567897";
    private final String TITOLO = "Test Libro";
    private final String AUTORI = "Mario Rossi";
    private final int ANNO = 2020;
    private final int COPIE_INIZIALI = 5;

    @BeforeEach
    public void setUp() throws ISBNNotValidException {
        libroValido = new Libro(TITOLO, AUTORI, ANNO, ISBN_VALIDO, COPIE_INIZIALI);
    }

    @AfterEach
    public void pulizia() {
        libroValido = null;
    }

    // --- TEST ---
    @Test
    public void testCostruttoreValido() {
        assertNotNull(libroValido);
        assertEquals(TITOLO, libroValido.getTitolo());
        assertEquals(AUTORI, libroValido.getAutori());
        assertEquals(ANNO, libroValido.getAnnoPubblicazione());
        assertEquals(ISBN_VALIDO, libroValido.getIsbn());
        assertEquals(COPIE_INIZIALI, libroValido.getNumeroCopie());
    }

    @Test
    public void testCostruttoreISBNInvalido() {
        assertThrows(ISBNNotValidException.class, () -> {
            new Libro(TITOLO, AUTORI, ANNO, null, COPIE_INIZIALI);
        });

        assertThrows(ISBNNotValidException.class, () -> {
            new Libro(TITOLO, AUTORI, ANNO, "", COPIE_INIZIALI);
        });

        assertThrows(ISBNNotValidException.class, () -> {
            new Libro(TITOLO, AUTORI, ANNO, "123456", COPIE_INIZIALI); // troppo corto
        });

        assertThrows(ISBNNotValidException.class, () -> {
            new Libro(TITOLO, AUTORI, ANNO, "AB12345678901", COPIE_INIZIALI); // lettere
        });
    }

    @Test
    public void testSetTitolo() {
        String nuovoTitolo = "Nuovo Titolo";
        libroValido.setTitolo(nuovoTitolo);
        assertEquals(nuovoTitolo, libroValido.getTitolo());
    }

    @Test
    public void testSetAutori() {
        String nuoviAutori = "Luigi Bianchi";
        libroValido.setAutori(nuoviAutori);
        assertEquals(nuoviAutori, libroValido.getAutori());
    }

    @Test
    public void testSetAnnoPubblicazione() {
        int nuovoAnno = 2022;
        libroValido.setAnnoPubblicazione(nuovoAnno);
        assertEquals(nuovoAnno, libroValido.getAnnoPubblicazione());
    }

    @Test
    public void testSetNumeroCopieValido() {
        libroValido.setNumeroCopie(10);
        assertEquals(10, libroValido.getNumeroCopie());
    }

    @Test
    public void testSetNumeroCopieInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            libroValido.setNumeroCopie(-1);
        });
    }

    @Test
    public void testIncrementaCopia() {
        int copiePrima = libroValido.getNumeroCopie();
        libroValido.incrementaCopiaLibro();
        assertEquals(copiePrima + 1, libroValido.getNumeroCopie());
    }

    @Test
    public void testDecrementaCopiaValida() throws CopieEsauriteException {
        libroValido.setNumeroCopie(3); // Assicuriamoci che >1
        int copiePrima = libroValido.getNumeroCopie();
        libroValido.decrementaCopiaLibro();
        assertEquals(copiePrima - 1, libroValido.getNumeroCopie());
    }

    @Test
    public void testDecrementaCopiaEsaurite() {
        libroValido.setNumeroCopie(1); // limite per eccezione
        assertThrows(CopieEsauriteException.class, () -> {
            libroValido.decrementaCopiaLibro();
        });
    }

    @Test
    public void testEqualsHashCode() throws ISBNNotValidException {
        Libro stessoLibro = new Libro("Altro titolo", "Autore diverso", 2019, ISBN_VALIDO, 2);
        assertTrue(libroValido.equals(stessoLibro));
        assertEquals(libroValido.hashCode(), stessoLibro.hashCode());
    }

    @Test
    public void testCompareTo() throws ISBNNotValidException {
        Libro libroDiverso = new Libro("Titolo 2", "Autore 2", 2021, "9781234567898", 1);
        assertTrue(libroValido.compareTo(libroDiverso) < 0 || libroValido.compareTo(libroDiverso) > 0);
    }

    @Test
    public void testToString() {
        String descrizione = libroValido.toString();
        assertNotNull(descrizione);
        assertTrue(descrizione.contains(TITOLO));
        assertTrue(descrizione.contains(AUTORI));
        assertTrue(descrizione.contains(ISBN_VALIDO));
    }
}
