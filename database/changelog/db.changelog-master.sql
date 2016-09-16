--liquibase formatted sql

--changeset rc-lab-springboot:0.0.1

SET foreign_key_checks = 0;

DROP TABLE IF EXISTS `REVINFO`;
CREATE TABLE `REVINFO` (
  `REV` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Revision number',
  `REVTSTMP` bigint(20) NOT NULL COMMENT 'Timestamp of the revision in epoch time',
  PRIMARY KEY (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='This Table is the table to keep track of audit table data';

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `RoleId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `Name` varchar(255) NOT NULL DEFAULT '' COMMENT '',
  `Description_en` varchar(255) DEFAULT NULL COMMENT 'English Name of the role',
  `Description_tc` varchar(255) DEFAULT NULL COMMENT 'Traditional Chinese Name of the role',
  `Description_sc` varchar(255) DEFAULT NULL COMMENT 'Simplified Chinese Name of the role',
  `UpdateTimestamp` bigint(20) DEFAULT NULL COMMENT 'millisecond time in epoch',
  `Version` bigint(20) DEFAULT NULL COMMENT 'Optimistic Locking Version Column',
  PRIMARY KEY (`RoleId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Role for the Application';

DROP TABLE IF EXISTS `role_AUD`;
CREATE TABLE `role_AUD` (
  `REV` bigint(20) NOT NULL COMMENT 'Revision number.',
  `REVTYPE` int(11) NOT NULL COMMENT 'Database action to record, Values are:0 (ADD) 1 (MOD) 2 (DEL)',
  `RoleId` bigint(20) NOT NULL COMMENT 'Primary Key',
  `Name` varchar(255) NOT NULL DEFAULT '' COMMENT 'Possible Values for Name.­ Buyer_ViewOnly­ Buyer­ Seller_ViewOnly­ Seller­ System -Administrator',
  `description_en` varchar(255) DEFAULT NULL COMMENT 'English Name of the role',
  `description_tc` varchar(255) DEFAULT NULL COMMENT 'Traditional Chinese Name of the role',
  `description_sc` varchar(255) DEFAULT NULL COMMENT 'Simplified Chinese Name of the role',
  `UpdateTimestamp` bigint(20) DEFAULT NULL COMMENT 'millisecond time in epoch',
  `Version` bigint(20) DEFAULT NULL COMMENT 'Optimistic Locking Version Column',
  PRIMARY KEY (`REV`,`RoleId`),
  KEY `FK_role_AUD_REVINFO` (`REV`),
  CONSTRAINT `FK_role_AUD_REVINFO` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Role Audit Table';

DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `CountryId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `Name_en` varchar(255) DEFAULT NULL COMMENT 'English Name of the role',
  `Name_tc` varchar(255) DEFAULT NULL COMMENT 'Traditional Chinese Name of the role',
  `Name_sc` varchar(255) DEFAULT NULL COMMENT 'Simplified Chinese Name of the role',
  `Flag_uri` varchar(255) DEFAULT NULL COMMENT 'URI of the Country Flag icon in SVG',
  `UpdateTimestamp` bigint(20) DEFAULT NULL COMMENT 'millisecond time in epoch',
  `Version` bigint(20) DEFAULT NULL COMMENT 'Optimistic Locking Version Column',
  PRIMARY KEY (`CountryId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Country for the application';

DROP TABLE IF EXISTS `country_AUD`;
CREATE TABLE `country_AUD` (
  `REV` bigint(20) NOT NULL COMMENT 'Revision number.',
  `REVTYPE` int(11) NOT NULL COMMENT 'Database action to record, Values are:0 (ADD) 1 (MOD) 2 (DEL)',
  `CountryId` bigint(20) NOT NULL COMMENT 'Primary key',
  `Name_en` varchar(255) DEFAULT NULL COMMENT 'English Name of the role',
  `Name_tc` varchar(255) DEFAULT NULL COMMENT 'Traditional Chinese Name of the role',
  `Name_sc` varchar(255) DEFAULT NULL COMMENT 'Simplified Chinese Name of the role',
  `flag_uri` varchar(255) DEFAULT NULL COMMENT 'URI of the Country Flag icon in SVG',
  `UpdateTimestamp` bigint(20) DEFAULT NULL COMMENT 'millisecond time in epoch',
  `Version` bigint(20) DEFAULT NULL COMMENT 'Optimistic Locking Version Column',
  PRIMARY KEY (`REV`, `CountryId`),
  KEY `FK_country_AUD_REVINFO` (`REV`),
  CONSTRAINT `FK_country_AUD_REVINFO` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Country Audit Table';

DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `CompanyId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `Name_en` varchar(255) DEFAULT NULL COMMENT 'English Name of the Company',
  `Name_tc` varchar(255) DEFAULT NULL COMMENT 'Traditional Chinese Name of the Company',
  `Name_sc` varchar(255) DEFAULT NULL COMMENT 'Simplified Chinese Name of the Company',
  `CountryId` bigint(20) NOT NULL COMMENT 'Foreign Key to Country. Represent Origin of the company',
  `UpdateTimestamp` bigint(20) DEFAULT NULL COMMENT 'millisecond time in epoch',
  `Version` bigint(20) DEFAULT NULL COMMENT 'Optimistic Locking Version Column',
  PRIMARY KEY (`CompanyId`),
  KEY `FK_company_country` (`CountryId`),
  CONSTRAINT `FK_company_country` FOREIGN KEY (`CountryId`) REFERENCES `country` (`CountryId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Company table for the application';

DROP TABLE IF EXISTS `company_AUD`;
CREATE TABLE `company_AUD` (
  `REV` bigint(20) NOT NULL COMMENT 'Revision number.',
  `REVTYPE` int(11) NOT NULL COMMENT 'Database action to record, Values are:0 (ADD) 1 (MOD) 2 (DEL)',
  `CompanyId` bigint(20) NOT NULL COMMENT 'Primary Key',
  `Name_en` varchar(255) DEFAULT NULL COMMENT 'English Name of the Company',
  `Name_tc` varchar(255) DEFAULT NULL COMMENT 'Traditional Chinese Name of the Company',
  `Name_sc` varchar(255) DEFAULT NULL COMMENT 'Simplified Chinese Name of the Company',
  `CountryId` bigint(20) NOT NULL COMMENT 'Foreign Key to Country. Represent Origin of the company',
  `UpdateTimestamp` bigint(20) DEFAULT NULL COMMENT 'millisecond time in epoch',
  `Version` bigint(20) DEFAULT NULL COMMENT 'Optimistic Locking Version Column',
  PRIMARY KEY (`REV`,`CompanyId`),
  KEY `FK_company_AUD_REVINFO` (`REV`),
  CONSTRAINT `FK_company_AUD_REVINFO` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Company Audit Table';

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `UserId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `LoginId` varchar(255) DEFAULT NULL COMMENT 'Unique across the system. Same Login Id cannot be used for different company ',
  `Password_hash` varchar(255) DEFAULT NULL COMMENT 'user input password is hashed together with the salt.',
  `Password_salt` varchar(255) DEFAULT NULL COMMENT 'a base­64 encoded string of 20 random bytes',
  `RoleId` bigint(20) DEFAULT NULL COMMENT 'Foreign key to Role Table. Represent the Role of this',
  `CompanyId` bigint(20) DEFAULT NULL COMMENT 'Foreign key to Company Table. Represent the Company of this user',
  `Email` varchar(255) DEFAULT NULL COMMENT 'Email Address of this user',
  `PhoneNo` varchar(255) DEFAULT NULL COMMENT 'Phone Number of this user',
  `MobileNo` varchar(255) DEFAULT NULL COMMENT 'Mobile Number of this user',
  `UpdateTimestamp` bigint(20) DEFAULT NULL COMMENT 'millisecond time in epoch',
  `Version` bigint(20) DEFAULT NULL COMMENT 'Optimistic Locking Version Column',
  PRIMARY KEY (`UserId`),
  KEY `FK_user_role` (`RoleId`),
  KEY `FK_user_company` (`CompanyId`),
  CONSTRAINT `FK_user_company` FOREIGN KEY (`CompanyId`) REFERENCES `company` (`CompanyId`),
  CONSTRAINT `FK_user_role` FOREIGN KEY (`RoleId`) REFERENCES `role` (`RoleId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='This is a system table to store all the user eligible to use';

--
-- Table structure for table `user_AUD`
--

DROP TABLE IF EXISTS `user_AUD`;
CREATE TABLE `user_AUD` (
  `REV` bigint(20) NOT NULL COMMENT 'Revision number.',
  `REVTYPE` int(11) NOT NULL COMMENT 'Database action to record, Values are:0 (ADD) 1 (MOD) 2 (DEL)',
  `UserId` bigint(20) NOT NULL COMMENT 'Primary Key',
  `LoginId` varchar(255) DEFAULT NULL COMMENT 'Unique across the system. Same Login Id cannot be used for different company ',
  `password_hash` varchar(255) DEFAULT NULL COMMENT 'user input password is hashed together with the salt.',
  `password_salt` varchar(255) DEFAULT NULL COMMENT 'a base­64 encoded string of 20 random bytes',
  `RoleId` bigint(20) DEFAULT NULL COMMENT 'Foreign key to Role Table. Represent the Role of this',
  `CompanyId` bigint(20) DEFAULT NULL COMMENT 'Foreign key to Company Table. Represent the Company of this user',
  `Email` varchar(255) DEFAULT NULL COMMENT 'Email Address of this user',
  `PhoneNo` varchar(255) DEFAULT NULL COMMENT 'Phone Number of this user',
  `MobileNo` varchar(255) DEFAULT NULL COMMENT 'Mobile Number of this user',
  `UpdateTimestamp` bigint(20) DEFAULT NULL COMMENT 'millisecond time in epoch',
  `Version` bigint(20) DEFAULT NULL COMMENT 'Optimistic Locking Version Column',
  PRIMARY KEY (`REV`,`UserId`),
  KEY `FK_user_AUD_REVINFO` (`REV`),
  CONSTRAINT `FK_user_AUD_REVINFO` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='User Audit Table';


SET foreign_key_checks = 1;

--rollback drop table `user`;
--rollback drop table `company`;
--rollback drop table `country`;
--rollback drop table `role`;