CREATE TABLE equipments (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            deviceId VARCHAR(255) UNIQUE NOT NULL,
                            name VARCHAR(255) NOT NULL,
                            description TEXT,
                            category VARCHAR(255) DEFAULT NULL,
                            image VARCHAR(255) DEFAULT NULL,
                            status VARCHAR(255) NOT NULL DEFAULT 'AVAILABLE'

);


-- Equipment Allocation Table

CREATE TABLE equipment_allocation (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      employee_id INT NOT NULL,
                                      employee_name VARCHAR(255) NOT NULL,
                                      employee_email VARCHAR(255) NOT NULL,
                                      allocated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      expected_return_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      return_date TIMESTAMP ,
                                      reason VARCHAR(255) NOT NULL,
                                      equipment_id INT NOT NULL,
                                      status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
                                      CONSTRAINT equipment_fk FOREIGN KEY (equipment_id) REFERENCES equipments(id)
);

-- CREATE TABLE equipment_allocation (
--                                       id INT AUTO_INCREMENT PRIMARY KEY,
--
--                                       equipment_id INT NOT NULL,
--                                       employee_id INT NOT NULL,
--                                       reason VARCHAR(255) NOT NULL,
--                                       allocated_date TIMESTAMP NOW(),
--                                       expected_return_date TIMESTAMP NOT NULL,
--                                       return_date TIMESTAMP
--                                   ,
--
--
--                                       CONSTRAINT equipment_fk FOREIGN KEY (equipment_id) REFERENCES equipments(id)
-- );

