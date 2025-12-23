package it.unisa.diem.oop.projectlibraryg13;

/**
 * @class Launcher
 * @brief Entry point principale dell'applicazione.
 *
 * Questa classe funge da punto di ingresso (Launcher) per il programma.
 * Il suo unico scopo è delegare l'esecuzione alla classe principale {@link App}.
 * Questa struttura è utilizzata per garantire la compatibilità durante 
 * la creazione di file JAR eseguibili.
 *
 * @author mello
 */

public class Launcher {
    
    /**
     * @brief Metodo main standard.
     * 
     * * Avvia l'applicazione chiamando il main della classe App.
     * 
     * * @param[in] args Argomenti da riga di comando passati al programma.
     */
    public static void main(String[] args) {
        App.main(args);
    }
}
