package cz.vse.logbookapp.dao;

import cz.vse.logbookapp.model.Ponor;
import cz.vse.logbookapp.model.Uzivatel;

import java.util.List;

public interface PonorDao {
    void save(Ponor ponor);
    void update(Ponor ponor);
    void delete(Long id);
    List<Ponor> findAllByUser(Uzivatel uzivatel);
}