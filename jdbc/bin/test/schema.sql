
CREATE TABLE EMPLOYEE (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    phone_number VARCHAR(20),
    hire_date DATE
);

CREATE TABLE EMPLOYEE_DETAIL (
    employee_id INT PRIMARY KEY,  -- S'ha d'afegir com a PK
    address VARCHAR(255),
    national_id VARCHAR(20),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    FOREIGN KEY (employee_id) REFERENCES EMPLOYEE(employee_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TIME_LOG (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    log_date DATE,
    check_in_time TIME,
    check_out_time TIME,
    total_hours DECIMAL(4,2),
    is_late BOOLEAN,
    is_remote BOOLEAN,
    FOREIGN KEY (employee_id) REFERENCES EMPLOYEE(employee_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE SHIFT (
    shift_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    start_time TIME,
    end_time TIME,
    location VARCHAR(100)
);

CREATE TABLE EMPLOYEE_SHIFT (
    employee_id INT,
    shift_id INT,
    PRIMARY KEY (employee_id, shift_id),
    FOREIGN KEY (employee_id) REFERENCES EMPLOYEE(employee_id) ON DELETE CASCADE ON UPDATE CASCADE ,
    FOREIGN KEY (shift_id) REFERENCES SHIFT(shift_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TRAINING (
    training_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    description TEXT,
    duration_hours INT,
    mandatory BOOLEAN
);

CREATE TABLE EMPLOYEE_TRAINING (
    employee_id INT,
    training_id INT,
    completion_date DATE,
    passed BOOLEAN,
    score DECIMAL(5,2),
    PRIMARY KEY (employee_id, training_id),
    FOREIGN KEY (employee_id) REFERENCES EMPLOYEE(employee_id) ON DELETE CASCADE ON UPDATE CASCADE ,
    FOREIGN KEY (training_id) REFERENCES TRAINING(training_id) ON DELETE CASCADE ON UPDATE CASCADE
);
