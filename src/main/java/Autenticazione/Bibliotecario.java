package Autenticazione;

/**
 * @class Bibliotecario
 * @brief Rappresenta l'utente con privilegi amministrativi (Librarian).
 *
 * Questa classe gestisce le credenziali dell'amministratore del sistema.
 * I campi sono immutabili (`final`) per garantire che, una volta istanziato
 * il bibliotecario, le sue credenziali non possano essere alterate a runtime.
 *
 * @invariant username != null && !username.isEmpty() (Le credenziali devono essere valide).
 * @invariant password != null && !password.isEmpty().
 *
 * @author chiara
 * @version 1.0
 */

public class Bibliotecario {
    
    private final String username;
    private final String password;
    
    /**
     * @brief Costruttore della classe Bibliotecario.
     *
     * Inizializza l'amministratore con le credenziali specificate.
     *
     * @pre username != null && !username.isEmpty()
     * @pre password != null && !password.isEmpty()
     * @post Viene creato un oggetto Bibliotecario con credenziali fissate.
     *
     * @param[in] username Il nome utente amministratore.
     * @param[in] password La password amministratore.
     */
    public Bibliotecario(String username, String password){
        this.username = username;
        this.password = password;
    }

    /**
     * @brief Restituisce lo username.
     * 
     * @return La stringa dello username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @brief Restituisce la password.
     * 
     * @return La stringa della password.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @brief Verifica le credenziali di accesso (Login).
     *
     * Confronta le credenziali fornite in input con quelle memorizzate nell'oggetto.
     *
     * @pre user != null
     * @pre pass != null
     *
     * @param[in] user Lo username inserito dall'utente che tenta il login.
     * @param[in] pass La password inserita dall'utente.
     * @return true se user e pass coincidono con quelli del bibliotecario, false altrimenti.
     */
    public boolean login(String user, String pass){
        // Logica fittizia dello scheletro: return true
        // Logica reale: return this.username.equals(user) && this.password.equals(pass);
        return true;
    }
}