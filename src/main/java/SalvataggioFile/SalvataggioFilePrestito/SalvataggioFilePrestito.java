package SalvataggioFile.SalvataggioFilePrestito;

import GestionePrestito.ElencoPrestiti;

/**
 * @class SalvataggioFilePrestito
 * @brief Gestisce la persistenza su file degli oggetti di tipo Prestito.
 *
 * Questa classe di utilità si occupa di serializzare (salvare) e deserializzare (caricare)
 * le istanze della classe @ref Prestito su file binari.
 *
 * @see GestionePrestito.Prestito
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 *
 * @author grossino1
 * @version 1.0
 */

public class SalvataggioFilePrestito {
    
    /**
     * @brief Salva lo stato di un oggetto Prestito su un file binario.
     *
     * Serializza l'oggetto passato come parametro e lo scrive nel percorso specificato.
     * Se il file esiste già, viene sovrascritto.
     *
     * @pre dati != null (L'oggetto da salvare non può essere nullo).
     * @pre filename != null && !filename.isEmpty() (Il nome del file deve essere valido).
     * @post Viene creato o aggiornato un file contenente i dati dei prestiti.
     *
     * @param[in] dati: L'oggetto ElencoPrestiti da serializzare.
     * @param[in] filename: Il percorso o nome del file di destinazione.
     */
    public static void salva(ElencoPrestiti dati, String filename){
     
    }
    
    /**
     * @brief Carica un oggetto Prestito da un file binario.
     *
     * Tenta di leggere e deserializzare un oggetto Prestito dal percorso specificato.
     *
     * @pre filename != null && !filename.isEmpty()
     * @post Restituisce un oggetto Prestito valido se la lettura ha successo.
     * @post Restituisce null se il file non esiste o la deserializzazione fallisce.
     *
     * @param[in] filename: Il percorso del file da leggere.
     * @return L'istanza di ElencoPrestiti recuperata, oppure null in caso di errore.
     */
    public static ElencoPrestiti carica(String filename){
        return null;
    }
}
