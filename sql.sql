CREATE TABLE `users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) DEFAULT NULL,
  `password` VARCHAR(255) DEFAULT NULL,
  `firstName` VARCHAR(255) DEFAULT NULL,
  `lastName` VARCHAR(255) DEFAULT NULL,
  `confirmed` TINYINT(1) NOT NULL DEFAULT '0',
  `active` TINYINT(1) NOT NULL DEFAULT '0',
  `groupID` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `groupID` (`groupID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `groups` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `group` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `books` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) DEFAULT NULL,
  `description` TEXT,
  `authors` TEXT,
  `path` VARCHAR(255) DEFAULT NULL,
  `fileName` VARCHAR(255) DEFAULT NULL,
  `image` VARCHAR(255) DEFAULT NULL,
  `keyWords` TEXT,
  `rate` INT(2) NOT NULL DEFAULT '0',
  `uploadDate` DATETIME DEFAULT NULL,
  `uploadUserId` INT(11) DEFAULT NULL,
  `updateDate` DATETIME DEFAULT NULL,
  `updateUserId` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `uploadUserId` (`uploadUserId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `comments` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `bookID` INT(11) NOT NULL,
  `comment` TEXT,
  `parentCommentID` INT(11) DEFAULT NULL,
  `userID` INT(11) DEFAULT NULL,
  `date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bookID` (`bookID`),
  KEY `parentCommentID` (`parentCommentID`),
  KEY `userID` (`userID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `ratings` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `bookID` INT(11) DEFAULT NULL,
  `rating` INT(2) DEFAULT NULL,
  `userID` INT(11) DEFAULT NULL,
  `rateDate` DATE DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bookID` (`bookID`),
  KEY `userID` (`userID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `groups` (`id`, `group`) VALUES('1','ADMIN');
INSERT INTO `groups` (`id`, `group`) VALUES('2','USER');

INSERT INTO `users` (`id`, `username`, `password`, `firstName`, `lastName`, `confirmed`, `active`, `groupID`) VALUES('1','admin',MD5('123456'),'admin','admin','1','1','1');
INSERT INTO `users` (`id`, `username`, `password`, `firstName`, `lastName`, `confirmed`, `active`, `groupID`) VALUES('2','user',MD5('123456'),'User','First','1','1','2');

INSERT INTO `books` (`id`, `name`, `description`, `authors`, `path`, `fileName`, `image`, `keyWords`, `rate`, `uploadDate`, `uploadUserId`, `updateDate`, `updateUserId`) VALUES('1','Effective Java (2nd Edition)','Are you looking for a deeper understanding of the Java programming language so that you can write code that is clearer, more correct, more robust, and more reusable? Look no further! Effective Java, Second Edition, brings together seventy-eight indispensable programmers rules of thumb: working, best-practice solutions for the programming challenges you encounter every day.','Joshua Bloch','cf/59/24/30/46/e3/d5/5c/a62884bb84eb3dc2','Effective Java 2nd Edition.pdf','5b/e8/15/56/36/5a/b7/65/0183d501c1024f54.jpg',NULL,'4','2015-02-23 11:39:45','1','2015-02-23 11:39:52','1');
INSERT INTO `books` (`id`, `name`, `description`, `authors`, `path`, `fileName`, `image`, `keyWords`, `rate`, `uploadDate`, `uploadUserId`, `updateDate`, `updateUserId`) VALUES('2','Thinking in Java (4th Edition)','Thinking in Java should be read cover to cover by every Java programmer, then kept close at hand for frequent reference. The exercises are challenging, and the chapter on Collections is superb! Not only did this book help me to pass the Sun Certified Java Programmer exam; its also the first book I turn to whenever I have a Java question.','Bruce Eckel','9a/5a/85/d8/ad/b2/96/0e/86bc532e706efa09','Bruce_Eckel-Thinking_in_Java_(4th_Edition).pdf','40/f6/ae/36/31/33/98/ef/503e79141e8bad64.jpg',NULL,'0','2015-02-23 12:39:57','1','2015-02-23 12:40:01','1');
INSERT INTO `books` (`id`, `name`, `description`, `authors`, `path`, `fileName`, `image`, `keyWords`, `rate`, `uploadDate`, `uploadUserId`, `updateDate`, `updateUserId`) VALUES('3','Spring in Action, Third Edition','Spring in Action, Third Edition continues the practical, hands-on style of the previous bestselling editions. Author Craig Walls has a special knack for crisp and entertaining examples that zoom in on the features and techniques you really need. This edition highlights the most important aspects of Spring 3.0 including REST, remote services, messaging, Security, MVC, Web Flow, and more.','Craig Walls','0f/4b/a0/db/57/18/7b/39/63f2a733ead66c08','Spring.pdf','72/8f/52/3c/3a/0f/5a/e5/b9ce2fafa9398574.jpg',NULL,'0','2015-02-23 13:40:03','1','2015-02-23 13:40:08','1');
