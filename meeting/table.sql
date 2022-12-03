CREATE TABLE reservations (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  email TEXT NOT NULL,
  time_slot TIMESTAMP NOT NULL,
  purpose TEXT NOT NULL,
  manager_id INTEGER NOT NULL,
  FOREIGN KEY (manager_id) REFERENCES reservation_managers(id)
);

CREATE TABLE reservation_managers (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  email TEXT NOT NULL,
  permission BOOLEAN NOT NULL
);

INSERT INTO reservation_managers(name, phone_number, email, permission)
VALUES ('山田 太郎', '03-1234-5678', 'taro.yamada@example.com', TRUE);
