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
    
    // Si crea un nuovo catalogo e un libro valido per poi toglierlo nell'AfterEach 
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
    
    // si controlla se la registazione del libro valido creato in precedenza avviene correttamente
    @Test
    public void testRegistrazioneLibroValido() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        assertTrue(catalogo.getCatalogoLibri().contains(libroValido));
    }

    // si controlla se la registazione di un libro dublicato viene gestita correttamente: con un'eccezione 
    // che non permette la registrazione
    @Test
    public void testRegistrazioneLibroDuplicato() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        assertThrows(LibroPresenteException.class, () -> {
            catalogo.registrazioneLibro(libroValido);
        });
    }
    

    // si controlla se la registazione di un libro nullo viene gestita correttamente: con un'eccezione
    // che non permette la registrazione

    @Test 
    public void testRegistrazioneLibroNull() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.registrazioneLibro(null);
        });
    }

  
    // questo metodo verifica le varie condizioni per la registrazione di un libro:
    @Test
    public void testRegistrazioneLibro() throws Exception {
        
        catalogo.registrazioneLibro(libroValido);
        
    // inizialmente si prova a registrare una seconda volta lo stesso libro, aspettandosi il lancio di un'eccezione LibroPresenteException
        assertThrows(LibroPresenteException.class, () -> {
            catalogo.registrazioneLibro(libroValido);
        });
        
    // registra un libro null aspettandosi il lancio dell'eccezione LibroNotFoundException
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.registrazioneLibro(null);
        });
    // si creano altri 999 libri in modo da verificare la 1000esima registrazione che non deve poter esser eseguita, aspettandosi il lancio dell'eccezione CatalogoPienoException
        for (int i = 0; i < 999; i++) {
            String is = String.format("6543217890%03d", i);
            Libro l = new Libro(TITOLO + i, AUTORI, ANNO, is, COPIE);
            catalogo.registrazioneLibro(l);
                }
        Libro libroExtra = new Libro("Extra", "Autore", 2022, "9781234567899", COPIE);
        assertThrows(CatalogoPienoException.class, () -> {
            catalogo.registrazioneLibro(libroExtra);
        });
    }

                

    // Questo metodo si occupa di verificare le eliminazioni di un libro da un catalogo
    @Test
    // Inizialmente si verifica se l'elimininazione di un libro creato funziona correttamente
    public void testEliminazioneLibroValido() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        catalogo.eliminazioneLibro(libroValido);
        assertFalse(catalogo.getCatalogoLibri().contains(libroValido));
    }
    // si testa l'eliminazione di un libro che non è presente nel catalogo, aspettandosi il lancio dell'eccezione LibroNotFoundException
    @Test
    public void testEliminazioneLibroNonPresente() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.eliminazioneLibro(libroValido);
        });
    }
    
    // si testa l'eliminazione di un libro con un prestito attivo, aspettandosi il lancio dell'eccezione LibroWithPrestitoException
    @Test
    public void testEliminazioneLibroConPrestitoAttivo() throws Exception {
        // Simula un prestito attivo
        libroValido.setnPrestitiAttivi(1);
        // Aggiungi il libro al catalogo
        catalogo.registrazioneLibro(libroValido);
        
        // Verifica che l'eliminazione lanci l'eccezione LibroWithPrestitoException
        assertThrows(LibroWithPrestitoException.class, () -> {
            catalogo.eliminazioneLibro(libroValido);
        });
    }

    // si testa l'eliminazione di un libro null, aspettandosi il lancio dell'eccezione LibroNotFoundException
    @Test
    public void testEliminazioneLibroNull() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.eliminazioneLibro(null);
        });
    }

    // si testa la ricerca di un libro
    
    @Test
    public void testCercaLibroByTitolo() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        ArrayList<Libro> risultati = catalogo.cercaLibro("Test");
        assertTrue(risultati.contains(libroValido));
    }
    // si testa la ricerca di un libro per autore, registrando prima un libro e poi cercando il nome Mario tra gli autori dei libri presenti nel catalogo
    @Test
    public void testCercaLibroByAutore() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        ArrayList<Libro> risultati = catalogo.cercaLibro("Mario");
        assertTrue(risultati.contains(libroValido));
    }

    // si testa la ricerca di un libro per ISBN, registrando prima un libro e poi cercando l'ISBN: 9781234567897 tra gli ISBN dei libri presenti nel catalogo
    @Test
    public void testCercaLibroByISBN() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        ArrayList<Libro> risultati = catalogo.cercaLibro("9781234567897");
        assertTrue(risultati.contains(libroValido));
    }

    // si testa la ricerca di un libro non presente nel catalogo, aspettandosi il lancio dell'eccezione LibroNotFoundException
    @Test
    public void testCercaLibroNonPresente() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.cercaLibro("Non Esiste");
        });
    }

    // si testa la modifica di un libro valido, creando prima un libro valido per poi modificarlo
    @Test
    public void testModificaLibroValido() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        catalogo.modificaLibro(libroValido, "Nuovo Titolo", "Nuovi Autori", 2021, 10);
        assertEquals("Nuovo Titolo", libroValido.getTitolo());
        assertEquals("Nuovi Autori", libroValido.getAutori());
        assertEquals(2021, libroValido.getAnnoPubblicazione());
        assertEquals(10, libroValido.getNumeroCopie());
    }
    
    // si testa la modifica di un libro nullo, con l'attesa del lancio dell'eccezione LibroNotFoundException
    @Test
    public void testModificaLibroNull() {
        assertThrows(LibroNotFoundException.class, () -> {
            catalogo.modificaLibro(null, "Titolo", "Autore", 2022, 3);
        });
    }
    // si effettua la ricerca di un libro su un ISBN presente nel catalogo
    @Test
    public void testGetLibroByISBNPresente() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        Libro trovato = catalogo.getLibroByISBN(ISBN_VALIDO);
        assertEquals(libroValido, trovato);
    }
    

    // si effettua la ricerca di un libro su un ISBN non presente nel catalogo
    @Test
    public void testGetLibroByISBNNonPresente() {
        Libro trovato = catalogo.getLibroByISBN("0000000000000");
        assertNull(trovato);
    }
    
    // si effettua test per verificare che dopo una registrazione di un libro nel catalogo la dimensione di quest'ultimo venga aggiornata correttamente
    @Test
    public void testGetCatalogoLibri() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        assertEquals(1, catalogo.getCatalogoLibri().size());
    }

    // si effettua test per verificare su un catalogo vuoto venga restituita la stringa "vuoto"
    @Test
    public void testToStringCatalogoVuoto() {
        String descrizione = catalogo.toString();
        assertNotNull(descrizione);
        assertTrue(descrizione.contains("vuoto"));
    }
    // verifica che la registrazione del libro sia avvenuta correttamente, quindi controlla che la stringa non sia nulla, controlla l'ISBN e il Titolo
    @Test
    public void testToStringCatalogoConLibri() throws Exception {
        catalogo.registrazioneLibro(libroValido);
        String descrizione = catalogo.toString();
        assertNotNull(descrizione);
        assertTrue(descrizione.contains(ISBN_VALIDO));
        assertTrue(descrizione.contains(TITOLO));
    }
}
