-- problems_db V1 初始化
-- 对应 docs/design/04-数据模型.md §2.2

CREATE TABLE `problem` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT,
    `title`           VARCHAR(255) NOT NULL,
    `description`     MEDIUMTEXT   NOT NULL COMMENT 'Markdown',
    `time_limit_ms`   INT          NOT NULL DEFAULT 1000,
    `memory_limit_mb` INT          NOT NULL DEFAULT 256,
    `difficulty`      VARCHAR(16)  NOT NULL DEFAULT 'EASY',
    `setter_id`       BIGINT       NOT NULL,
    `published`       BOOLEAN      NOT NULL DEFAULT FALSE,
    `total_submit`    INT          NOT NULL DEFAULT 0,
    `total_ac`        INT          NOT NULL DEFAULT 0,
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_setter` (`setter_id`),
    INDEX `idx_published_diff` (`published`, `difficulty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `problem_tag` (
    `problem_id` BIGINT      NOT NULL,
    `tag`        VARCHAR(32) NOT NULL,
    PRIMARY KEY (`problem_id`, `tag`),
    INDEX `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `testcase_manifest` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `problem_id`    BIGINT       NOT NULL,
    `case_index`    INT          NOT NULL,
    `input_object`  VARCHAR(255) NOT NULL COMMENT 'MinIO object key',
    `output_object` VARCHAR(255) NOT NULL,
    `score`         INT          NOT NULL DEFAULT 10,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_problem_case` (`problem_id`, `case_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
