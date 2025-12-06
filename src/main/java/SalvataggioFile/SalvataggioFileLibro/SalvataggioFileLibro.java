package SalvataggioFile.SalvataggioFileLibro;

import GestioneLibro.CatalogoLibri;

/**
 * @class SalvataggioFileLibro
 * @brief Gestisce la persistenza su file degli oggetti di tipo Libro.
 *
 * Questa classe di utilità si occupa di serializzare (salvare) e deserializzare (caricare)
 * le istanze della classe @ref Libro su file binari.
 *
 * @see GestioneLibro.Libro
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 *
 * @author grossino1
 * @version 1.0
 */

public class SalvataggioFileLibro {
    
    /**
     * @brief Salva lo stato di un oggetto Libro su un file binario.
     *
     * Serializza l'oggetto passato come parametro e lo scrive nel percorso specificato.
     * Se il file esiste già, viene sovrascritto.
     *
     * @pre dati != null (L'oggetto da salvare non può essere nullo).
     * @pre filename != null && !filename.isEmpty() (Il nome del file deve essere valido).
     * @post Viene creato o aggiornato un file contenente i dati dei libri.
     *
     * @param[in] dati: L'oggetto CatalogoLibri da serializzare.
     * @param[in] filename: Il percorso o nome del file di destinazione (es. "libro.dat").
     */
    public void salva(CatalogoLibri dati, String filename){
        // scheletro: qui andrebbe new ObjectOutputStream(new FileOutputStream(filename))...
    }
    
    /**
     * @brief Carica un oggetto Libro da un file binario.
     *
     * Tenta di leggere e deserializzare un oggetto Libro dal percorso specificato.
     *
     * @pre filename != null && !filename.isEmpty()
     * @post Restituisce un oggetto Libro valido se la lettura ha successo.
     * @post Restituisce null se il file non esiste o la deserializzazione fallisce.
     *
     * @param[in] filename: Il percorso del file da leggere.
     * @return L'istanza di CatalogoLibri recuperata, oppure null in caso di errore.
     */
    public static CatalogoLibri carica(String filename){
        return null;
    }
}