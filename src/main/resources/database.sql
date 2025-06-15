-- Create table Lokalita
CREATE TABLE IF NOT EXISTS Lokalita (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nazev VARCHAR(255) NOT NULL,
                          zeme VARCHAR(255) NOT NULL,
                          typ_lokality VARCHAR(255),
                          max_hloubka DOUBLE,
                          popis TEXT
);

-- Create table Uzivatel
CREATE TABLE IF NOT EXISTS Uzivatel (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          jmeno VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          certifikace VARCHAR(255),
                          datum_registrace DATE
);

-- Create table Ponor
CREATE TABLE IF NOT EXISTS Ponor (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       uzivatel_id BIGINT NOT NULL,
                       lokalita_id BIGINT NOT NULL,
                       datum DATE NOT NULL,
                       hloubka DOUBLE NOT NULL,
                       doba INT NOT NULL,
                       teplota_vody DOUBLE NOT NULL,
                       poznamka VARCHAR(255),
                       FOREIGN KEY (uzivatel_id) REFERENCES Uzivatel(id),
                       FOREIGN KEY (lokalita_id) REFERENCES Lokalita(id)
);

-- Insert data into Lokalita
INSERT INTO Lokalita (nazev, zeme, typ_lokality, max_hloubka, popis)
VALUES ('Great Barrier Reef', 'Australia', 'Coral Reef', 70, 'Largest coral reef system');
INSERT INTO Lokalita (nazev, zeme, typ_lokality, max_hloubka, popis)
VALUES ('Blue Hole', 'Belize', 'Sinkhole', 124, 'Famous underwater sinkhole');

-- Insert data into Uzivatel
INSERT INTO Uzivatel (jmeno, email, certifikace, datum_registrace)
VALUES ('John Doe', 'john.doe@example.com', 'IANTD AOWD', '2025-05-02');
INSERT INTO Uzivatel (jmeno, email, certifikace, datum_registrace)
VALUES ('Jane Smith', 'jane.smith@example.com', 'PADI OWD', '2024-08-15');

-- Insert data into Ponor
INSERT INTO Ponor (uzivatel_id, lokalita_id, datum, hloubka, doba, teplota_vody, poznamka)
VALUES (1, 1, '2023-10-01', 30, 45, 25.5, 'Clear');
INSERT INTO Ponor (uzivatel_id, lokalita_id, datum, hloubka, doba, teplota_vody, poznamka)
VALUES (1, 2, '2023-10-02', 20, 30, 26.0, 'Moderate');

SELECT * FROM UZIVATEL WHERE email = 'john.doe@example.com';

SELECT
    p.id AS ponor_id,
    p.datum AS ponor_datum,
    p.doba AS ponor_doba,
    p.hloubka AS ponor_hloubka,
    p.teplota_vody AS ponor_teplota_vody,
    p.poznamka AS ponor_poznamka,
    l.nazev AS lokalita_nazev,
    l.zeme AS lokalita_zeme,
    u.jmeno AS uzivatel_jmeno,
    u.email AS uzivatel_email
FROM
    Ponor p
        JOIN
    Lokalita l ON p.lokalita_id = l.id
        JOIN
    Uzivatel u ON p.uzivatel_id = u.id
WHERE
    p.uzivatel_id = 1;

select
    p1_0.id,
    p1_0.datum,
    p1_0.doba,
    p1_0.hloubka,
    p1_0.lokalita_id,
    p1_0.poznamka,
    p1_0.teplota_vody,
    p1_0.uzivatel_id
from
    Ponor p1_0
where
    p1_0.uzivatel_id=1;

select * from PONOR