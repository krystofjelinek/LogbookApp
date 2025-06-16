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
INSERT INTO Lokalita (nazev, zeme, typ_lokality, max_hloubka, popis)
VALUES ('Red Sea', 'Egypt', 'Coral Reef', 50, 'Known for its rich marine life');
INSERT INTO Lokalita (nazev, zeme, typ_lokality, max_hloubka, popis)
VALUES ('Cenote Dos Ojos', 'Mexico', 'Cenote', 60, 'Popular diving cenote with clear water');

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
INSERT INTO Ponor (uzivatel_id, lokalita_id, datum, hloubka, doba, teplota_vody, poznamka)
VALUES (1, 1, '2023-10-03', 15, 60, 24.0, 'Good visibility');
INSERT INTO Ponor (uzivatel_id, lokalita_id, datum, hloubka, doba, teplota_vody, poznamka)
VALUES (2, 2, '2023-10-04', 10, 50, 27.0, 'Strong current');
