-- users_db V1 初始化
-- 对应 docs/design/04-数据模型.md §2.1

CREATE TABLE `user` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `username`      VARCHAR(64)  NOT NULL UNIQUE,
    `email`         VARCHAR(128) NOT NULL UNIQUE,
    `password_hash` CHAR(60)     NOT NULL,
    `nickname`      VARCHAR(64),
    `avatar_url`    VARCHAR(255),
    `balance`       INT          NOT NULL DEFAULT 100,
    `total_ac`      INT          NOT NULL DEFAULT 0,
    `total_submit`  INT          NOT NULL DEFAULT 0,
    `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`    DATETIME,
    PRIMARY KEY (`id`),
    INDEX `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_role` (
    `user_id` BIGINT      NOT NULL,
    `role`    VARCHAR(16) NOT NULL COMMENT 'STUDENT / SETTER / ADMIN',
    PRIMARY KEY (`user_id`, `role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Seata AT 模式必需
CREATE TABLE `undo_log` (
    `branch_id`     BIGINT       NOT NULL,
    `xid`           VARCHAR(128) NOT NULL,
    `context`       VARCHAR(128) NOT NULL,
    `rollback_info` LONGBLOB     NOT NULL,
    `log_status`    INT          NOT NULL,
    `log_created`   DATETIME     NOT NULL,
    `log_modified`  DATETIME     NOT NULL,
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
