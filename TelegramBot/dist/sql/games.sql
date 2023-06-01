DROP TABLE IF EXISTS `games`;

CREATE TABLE `games` (
		`chat_id`     BIGINT                           NOT NULL,
		`is_started`  BOOLEAN                          DEFAULT NULL,
		`lang`        VARCHAR(128)                     DEFAULT NULL,
		`complexity`  ENUM ('easy', 'medium', 'hard')  DEFAULT NULL,
		`word`        VARCHAR(255)                     DEFAULT NULL,
		`tries`       SMALLINT                         DEFAULT NULL,
		PRIMARY KEY (`chat_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
