-- docker-entrypoint-initdb.d 在 MySQL 容器首次启动时自动执行
CREATE DATABASE IF NOT EXISTS users_db    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE IF NOT EXISTS problems_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE IF NOT EXISTS submits_db  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

GRANT ALL PRIVILEGES ON users_db.*    TO 'judgemesh'@'%';
GRANT ALL PRIVILEGES ON problems_db.* TO 'judgemesh'@'%';
GRANT ALL PRIVILEGES ON submits_db.*  TO 'judgemesh'@'%';
FLUSH PRIVILEGES;
