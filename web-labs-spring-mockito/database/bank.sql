CREATE TABLE `users` (
  `id` char(36) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accounts` (
  `id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `currency` enum('USD','RON','EUR') NOT NULL,
  `amount` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `accounts_FK` (`user_id`),
  CONSTRAINT `accounts_FK` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `payments` (
  `id` char(36) NOT NULL,
  `sender_account_id` char(36) NOT NULL,
  `receiver_account_id` char(36) NOT NULL,
  `amount` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `payments_FK` (`sender_account_id`),
  KEY `payments_FK_1` (`receiver_account_id`),
  CONSTRAINT `payments_FK` FOREIGN KEY (`sender_account_id`) REFERENCES `accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `payments_FK_1` FOREIGN KEY (`receiver_account_id`) REFERENCES `accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;