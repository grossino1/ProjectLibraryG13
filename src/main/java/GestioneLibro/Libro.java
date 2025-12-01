/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestioneLibro;
import java.util.Comparator;
/**
 *
 * @author chiara
 */
 class Libro implements Comparable<Libro>{
    private String titolo;
    private String autori;
    private String annoPubblicazione;
    private int isbn;
    private int numeroCopie;

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutori() {
        return autori;
    }

    public void setAutori(String autori) {
        this.autori = autori;
    }

    public String getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(String annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public int getNumeroCopie() {
        return numeroCopie;
    }

    public void setNumeroCopie(int numeroCopie) {
        this.numeroCopie = numeroCopie;
    }
    
    @Override
    public int hashCode(){
        return 0;
    }
    
    @Override
    public boolean equals(Object o){
        return true;
    }
    
    @Override
    public int compareTo(Libro other){
        return 0;
    }
    
    @Override
    public String toString(){
        StringBuffer str = new StringBuffer();
        return str.toString();
    }
}
