CREATE DATABASE `rclab` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT all privileges on rclab.* to 'rclab'@'%' identified by 'rclab';
GRANT all privileges on rclab.* to 'rclab'@'localhost' identified by 'rclab';