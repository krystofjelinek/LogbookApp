package cz.vse.logbookapp.dao;

import cz.vse.logbookapp.model.Uzivatel;

public interface UzivatelDao {
    Uzivatel findByEmail(String email);
}