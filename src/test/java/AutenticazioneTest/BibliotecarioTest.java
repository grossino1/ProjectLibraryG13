/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutenticazioneTest;

import Autenticazione.Bibliotecario;
import Eccezioni.EccezioniAutenticazione.LoginCredentialsNotValidException;
import Eccezioni.EccezioniAutenticazione.PasswordFieldEmptyException;
import Eccezioni.EccezioniAutenticazione.UsernameFieldEmptyException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class BibliotecarioTest {

    private Bibliotecario bibliotecario;
    
    // Credenziali corrette per i test
    private final String USERNAME_CORRETTO = "Admin";
    private final String PASSWORD_CORRETTA = "Password";

    @BeforeEach
    void setUp() {
        bibliotecario = new Bibliotecario(USERNAME_CORRETTO, PASSWORD_CORRETTA);
    }

    @AfterEach
    void Pulizia() {
        bibliotecario = null;
    }

    // --- TEST ---

    @Test
    void testCostruttoreEGetters() {
        assertNotNull(bibliotecario, "L'oggetto bibliotecario non deve essere null");
        assertEquals(USERNAME_CORRETTO, bibliotecario.getUsername(), "Lo username restituito deve corrispondere a quello impostato");
        assertEquals(PASSWORD_CORRETTA, bibliotecario.getPassword(), "La password restituita deve corrispondere a quella impostata");
    }
    
    @Test
    void testSetUsername() {
        String nuovoUsername = "Nuovo Username";
        bibliotecario.setUsername(nuovoUsername);
        assertEquals(nuovoUsername, bibliotecario.getUsername());
    }
    
     @Test
    void testSetPassword() {
        String nuovaPassword = "Nuova Password";
        bibliotecario.setPassword(nuovaPassword);
        assertEquals(nuovaPassword, bibliotecario.getPassword());
    }

    @Test
    void testLoginSuccesso() throws Exception {
        boolean risultato = bibliotecario.login(USERNAME_CORRETTO, PASSWORD_CORRETTA);
        assertTrue(risultato, "Il login deve restituire TRUE se le credenziali sono esatte");
    }

    @Test
    void testLoginEccezione() {
        
        // Username vuoto
        assertThrows(UsernameFieldEmptyException.class, () -> {
            bibliotecario.login("", PASSWORD_CORRETTA);
        }, "Dovrebbe lanciare UsernameFieldEmptyException");
        
        // Password vuota
        assertThrows(PasswordFieldEmptyException.class, () -> {
            bibliotecario.login(USERNAME_CORRETTO, "");
        }, "Dovrebbe lanciare PasswordFieldEmptyException");
        
        // Username sbagiato
        String usernameSbagliato = "utenteSbagliato";
        assertThrows(LoginCredentialsNotValidException.class, () -> {
            bibliotecario.login(usernameSbagliato, PASSWORD_CORRETTA);
        }, "Dovrebbe lanciare LoginCredentialsNotValidException se l'username non coincide");

        // Password sbagliata
        String passwordSbagliata = "passwordSbagliata";
        assertThrows(LoginCredentialsNotValidException.class, () -> {
            bibliotecario.login(USERNAME_CORRETTO, passwordSbagliata);
        }, "Dovrebbe lanciare LoginCredentialsNotValidException se la password non coincide");
    }
}