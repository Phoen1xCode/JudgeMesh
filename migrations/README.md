# 数据库迁移

各服务的 Flyway 迁移**唯一权威位置**:

| 库          | 路径                                                                         | 维护者 |
| ----------- | ---------------------------------------------------------------------------- | ------ |
| users_db    | `services/user-service/src/main/resources/db/migration/V*.sql`               | @HashThiin      |
| problems_db | `services/problem-service/src/main/resources/db/migration/V*.sql`            | @KY-raika      |
| submits_db  | `services/submit-service/src/main/resources/db/migration/V*.sql`             | @Nier291      |

## 命名规范

- `V<version>__<desc>.sql` —— Flyway 标准
- `version` 用 `1` `2` `3`,不要用 `1.0.1`
- `desc` 蛇形,如 `V2__add_user_avatar_url.sql`
- 一次 PR 一个迁移文件,**不要在已 apply 过的 V*.sql 上改**

## 本地初始化

服务启动时 Flyway 自动执行,无需手动操作。如果手动:

```bash
docker exec -it mysql mysql -uroot -p < services/user-service/src/main/resources/db/migration/V1__init.sql
```

## 三库同实例创建

```sql
CREATE DATABASE users_db    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE problems_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE submits_db  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE USER 'judgemesh'@'%' IDENTIFIED BY 'judgemesh';
GRANT ALL ON users_db.*    TO 'judgemesh'@'%';
GRANT ALL ON problems_db.* TO 'judgemesh'@'%';
GRANT ALL ON submits_db.*  TO 'judgemesh'@'%';
FLUSH PRIVILEGES;
```

详见 [docs/design/04-数据模型.md](../docs/design/04-数据模型.md)。
