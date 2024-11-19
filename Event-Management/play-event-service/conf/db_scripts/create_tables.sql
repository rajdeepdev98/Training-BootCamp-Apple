
CREATE TABLE `event` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                         `eventType` VARCHAR(255) NOT NULL,
                         `eventName` VARCHAR(255) NOT NULL,
                         `eventDate` DATETIME NOT NULL,
                         `slotNumber` INT NOT NULL,
                         `guestCount` INT NOT NULL,
                         `specialRequirements` VARCHAR(255) NOT NULL,
                         `eventStatus` VARCHAR(255) NOT NULL,
                         PRIMARY KEY (`id`)
);
CREATE TABLE `team` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `teamName` VARCHAR(255) NOT NULL,
                        `teamType` VARCHAR(255) NOT NULL,
                        PRIMARY KEY (`id`)
);
CREATE TABLE `task` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `eventId` BIGINT NOT NULL,
                        `teamId` BIGINT NOT NULL,
                        `taskDescription` VARCHAR(255) NOT NULL,
                        `deadline` VARCHAR(255) NOT NULL,
                        `specialInstructions` VARCHAR(255) NOT NULL,
                        `status` VARCHAR(255) NOT NULL,
                        `createdAt` VARCHAR(255) NOT NULL,
                        PRIMARY KEY (`id`),
                        KEY `fk_event` (`eventId`),
                        KEY `fk_team` (`teamId`),
                        CONSTRAINT `fk_event` FOREIGN KEY (`eventId`) REFERENCES `event` (`id`),
                        CONSTRAINT `fk_team` FOREIGN KEY (`teamId`) REFERENCES `team` (`id`)
);
CREATE TABLE `issue` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                         `taskId` BIGINT NOT NULL,
                         `eventId` BIGINT NOT NULL,
                         `teamId` BIGINT NOT NULL,
                         `issueType` VARCHAR(255) NOT NULL,
                         `issueDescription` VARCHAR(255) NOT NULL,
                         `reportedAt` VARCHAR(255) NOT NULL,
                         `resolvedAt` VARCHAR(255) NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `fk_task` (`taskId`),
                         KEY `fk_event1` (`eventId`),
                         KEY `fk_team1` (`teamId`),
                         CONSTRAINT `fk_event1` FOREIGN KEY (`eventId`) REFERENCES `event` (`id`),
                         CONSTRAINT `fk_task` FOREIGN KEY (`taskId`) REFERENCES `task` (`id`),
                         CONSTRAINT `fk_team1` FOREIGN KEY (`teamId`) REFERENCES `team` (`id`)
);
