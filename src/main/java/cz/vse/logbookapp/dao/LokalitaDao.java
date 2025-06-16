package cz.vse.logbookapp.dao;

import cz.vse.logbookapp.model.Lokalita;
import javafx.collections.ObservableMap;

import java.util.List;

public interface LokalitaDao {
    List<Lokalita> findAll();
    Lokalita findByNazev(String nazev);
    ObservableMap<String, Lokalita> findAllMap();

    void save(Lokalita lokalita);
}