/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestioneLibro;

/**
 *
 * @author chiara
 */

public class Libro implements Comparable<Libro> {

    private String titolo;
    private String autori;
    private int annoPubblicazione;
    private String isbn;
    private int numeroCopie;

    public Libro(String titolo, String autori, int annoPubblicazione, String isbn, int numeroCopie) {
        // corpo vuoto (scheletro)
    }

    // Getter e Setter
    public String getTitolo() { return null; }
    public void setTitolo(String titolo) {}

    public String getAutori() { return null; }
    public void setAutori(String autori) {}

    public int getAnnoPubblicazione() { return 0; }
    public void setAnnoPubblicazione(int annoPubblicazione) {}

    public String getIsbn() { return null; }
    public void setIsbn(String isbn) {}

    public int getNumeroCopie() { return 0; }
    public void setNumeroCopie(int numeroCopie) {}

    // Metodi
    public void incrementaCopiaLibro(){}
    public void decrementaCopiaLibro(){}
    
    // hashCode: restituisce un codice numerico univoco basato su ISBN
    @Override
    public int hashCode() { return 0; }

    // equals: confronta due libri per ISBN, evitando duplicati nel catalogo
    @Override
    public boolean equals(Object obj) { return false; }

    // compareTo: ordinamento naturale dei libri per titolo
    @Override
    public int compareTo(Libro other) { return 0; }

    @Override
    public String toString() { return ""; }
}
