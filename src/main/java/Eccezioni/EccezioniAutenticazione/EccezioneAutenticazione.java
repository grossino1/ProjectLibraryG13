package Eccezioni.EccezioniAutenticazione;

/**
 * @class EccezioneAutenticazione
 * @brief Classe base astratta per tutte le eccezioni relative al processo di autenticazione.
 *
 * Questa classe estende `Exception` e raggruppa tutti gli errori che possono verificarsi
 * durante le fasi di login o verifica delle credenziali.
 * Essendo astratta, non può essere istanziata direttamente, ma permette di catturare
 * tutti gli errori relativi ai libri in un unico blocco catch.
 *
 * @see java.lang.Exception
 *
 * @author jackross
 * @version 1.0
 */

public abstract class EccezioneAutenticazione extends Exception {

    /**
     * @brief Costruttore di default.
     *
     * Inizializza l'eccezione senza un messaggio di dettaglio specifico.
     */
    public EccezioneAutenticazione(){
    }
    
    /**
     * @brief Costruttore con messaggio di errore.
     *
     * @param[in] msg La descrizione testuale del motivo del fallimento dell'autenticazione.
     */
    public EccezioneAutenticazione(String msg){
        super(msg);
    }
}