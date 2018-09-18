CREATE TABLE `result` (
  `id`        bigint(20)  NOT NULL AUTO_INCREMENT,
  `code`      varchar(50) NOT NULL,
  `number`    bigint(50)  NOT NULL,
  `filenames` varchar(100),
  `error`     varchar(100),
  PRIMARY KEY (`id`)
);