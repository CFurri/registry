-- Inserció de dades a la taula EMPLOYEE
INSERT INTO EMPLOYEE (first_name, last_name, email, phone_number, hire_date) VALUES
('Joan', 'Martínez', 'joan.martinez@empresa.com', '612345678', '2020-05-15'),
('Maria', 'Garcia', 'maria.garcia@empresa.com', '623456789', '2021-02-10'),
('Pere', 'Roca', 'pere.roca@empresa.com', '634567890', '2022-01-20'),
('Anna', 'Soler', 'anna.soler@empresa.com', '645678901', '2019-11-05'),
('Marc', 'Valls', 'marc.valls@empresa.com', '656789012', '2023-03-15');

-- Inserció de dades a la taula EMPLOYEE_DETAIL
INSERT INTO EMPLOYEE_DETAIL (employee_id, address, national_id, emergency_contact_name, emergency_contact_phone) VALUES
(1, 'Carrer Major 123, Barcelona', '12345678A', 'Laura Martínez', '611223344'),
(2, 'Avinguda Diagonal 456, Barcelona', '87654321B', 'Josep Garcia', '622334455'),
(3, 'Plaça Catalunya 789, Barcelona', '56781234C', 'Marta Roca', '633445566'),
(4, 'Carrer Balmes 321, Barcelona', '43218765D', 'David Soler', '644556677'),
(5, 'Rambla Catalunya 654, Barcelona', '98765432E', 'Elena Valls', '655667788');

-- Inserció de dades a la taula SHIFT
INSERT INTO SHIFT (name, start_time, end_time, location) VALUES
('Matí', '08:00:00', '16:00:00', 'Oficina Central'),
('Tarda', '16:00:00', '00:00:00', 'Oficina Central'),
('Nit', '00:00:00', '08:00:00', 'Oficina Central'),
('Teletreball', '09:00:00', '17:00:00', 'Remot'),
('Part-time', '10:00:00', '14:00:00', 'Oficina Central');

-- Inserció de dades a la taula EMPLOYEE_SHIFT
INSERT INTO EMPLOYEE_SHIFT (employee_id, shift_id) VALUES
(1, 1), -- Joan fa torn de matí
(2, 2), -- Maria fa torn de tarda
(3, 1), -- Pere fa torn de matí
(4, 4), -- Anna teletreball
(5, 5); -- Marc part-time

-- Inserció de dades a la taula TRAINING
INSERT INTO TRAINING (title, description, duration_hours, mandatory) VALUES
('Seguretat a la feina', 'Formació bàsica en seguretat laboral', 4, TRUE),
('Protecció de dades', 'Curs sobre RGPD i protecció de dades', 6, TRUE),
('Trello avançat', 'Gestió de projectes amb Trello', 3, FALSE),
('Comunicació efectiva', 'Tècniques de comunicació interpersonal', 5, FALSE),
('Primers auxilis', 'Formació bàsica en primers auxilis', 8, TRUE);

-- Inserció de dades a la taula EMPLOYEE_TRAINING
INSERT INTO EMPLOYEE_TRAINING (employee_id, training_id, completion_date, passed, score) VALUES
(1, 1, '2023-01-15', TRUE, 92.5),
(1, 2, '2023-02-20', TRUE, 85.0),
(2, 1, '2023-01-20', TRUE, 95.0),
(2, 3, '2023-03-10', TRUE, 88.0),
(3, 1, '2023-01-25', TRUE, 90.0),
(4, 1, '2023-01-10', TRUE, 97.5),
(4, 2, '2023-02-15', TRUE, 91.0),
(4, 5, '2023-04-05', TRUE, 89.5),
(5, 1, '2023-02-05', TRUE, 93.0);

-- Inserció de dades a la taula TIME_LOG
-- Setmana laboral normal (dilluns a divendres)
INSERT INTO TIME_LOG (employee_id, log_date, check_in_time, check_out_time, total_hours, is_late, is_remote) VALUES
-- Joan (torn de matí)
(1, '2023-06-05', '08:05:00', '16:02:00', 7.95, TRUE, FALSE),
(1, '2023-06-06', '07:58:00', '15:55:00', 7.95, FALSE, FALSE),
(1, '2023-06-07', '08:10:00', '16:05:00', 7.92, TRUE, FALSE),
(1, '2023-06-08', '07:55:00', '15:58:00', 8.05, FALSE, FALSE),
(1, '2023-06-09', '08:00:00', '16:00:00', 8.00, FALSE, FALSE),

-- Maria (torn de tarda)
(2, '2023-06-05', '15:55:00', '23:58:00', 8.05, FALSE, FALSE),
(2, '2023-06-06', '16:10:00', '00:05:00', 7.92, TRUE, FALSE),
(2, '2023-06-07', '15:50:00', '23:55:00', 8.08, FALSE, FALSE),
(2, '2023-06-08', '16:05:00', '00:02:00', 7.95, TRUE, FALSE),
(2, '2023-06-09', '16:00:00', '00:00:00', 8.00, FALSE, FALSE),

-- Pere (torn de matí)
(3, '2023-06-05', '08:15:00', '16:10:00', 7.92, TRUE, FALSE),
(3, '2023-06-06', '07:50:00', '15:45:00', 7.92, FALSE, FALSE),
(3, '2023-06-07', '08:00:00', '16:00:00', 8.00, FALSE, FALSE),
(3, '2023-06-08', '08:20:00', '16:15:00', 7.92, TRUE, FALSE),
(3, '2023-06-09', '07:55:00', '15:50:00', 7.92, FALSE, FALSE),

-- Anna (teletreball)
(4, '2023-06-05', '09:05:00', '17:10:00', 8.08, TRUE, TRUE),
(4, '2023-06-06', '08:55:00', '17:00:00', 8.08, FALSE, TRUE),
(4, '2023-06-07', '09:10:00', '17:15:00', 8.08, TRUE, TRUE),
(4, '2023-06-08', '09:00:00', '17:05:00', 8.08, FALSE, TRUE),
(4, '2023-06-09', '08:50:00', '16:55:00', 8.08, FALSE, TRUE),

-- Marc (part-time)
(5, '2023-06-05', '10:05:00', '14:00:00', 3.92, TRUE, FALSE),
(5, '2023-06-06', '09:55:00', '13:55:00', 4.00, FALSE, FALSE),
(5, '2023-06-07', '10:10:00', '14:05:00', 3.92, TRUE, FALSE),
(5, '2023-06-08', '10:00:00', '14:00:00', 4.00, FALSE, FALSE),
(5, '2023-06-09', '09:50:00', '13:50:00', 4.00, FALSE, FALSE);