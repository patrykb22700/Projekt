/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encje;


import java.util.Set;

public class Klient {
    private int id;
    private String imie;
    private String nazwisko;
    Set<Zlecenie> zamowienia;
    
    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Zlecenie> getZamowienia() {
        return zamowienia;
    }

    public void setZamowienia(Set<Zlecenie> zamowienia) {
        this.zamowienia = zamowienia;
    }
    
    @Override
    public String toString() {
        return imie + " " + nazwisko;
    }

}
