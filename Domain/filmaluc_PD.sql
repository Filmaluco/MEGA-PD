-- phpMyAdmin SQL Dump
-- version 4.4.15.9
-- https://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 01-Jan-2019 às 19:46
-- Versão do servidor: 5.6.37
-- PHP Version: 7.1.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `filmaluc_PD`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `Files`
--

CREATE TABLE IF NOT EXISTS `Files` (
  `ID` int(10) unsigned NOT NULL COMMENT 'File ID',
  `UserID` int(10) unsigned NOT NULL COMMENT 'Owner ID',
  `Name` varchar(32) NOT NULL COMMENT 'File Name',
  `Directory` varchar(32) NOT NULL COMMENT 'File Directory',
  `Size` int(10) unsigned NOT NULL COMMENT 'File Size (MB)'
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COMMENT='Files Information';

-- --------------------------------------------------------

--
-- Estrutura da tabela `History`
--

CREATE TABLE IF NOT EXISTS `History` (
  `ID` int(10) unsigned NOT NULL COMMENT 'HistoryID',
  `OwnerID` int(10) unsigned NOT NULL COMMENT 'FK UserID [Owner]',
  `TargetID` int(10) unsigned NOT NULL COMMENT 'FK UserID [Target]',
  `FileName` varchar(32) NOT NULL COMMENT 'File name at the moment of the transfer',
  `Date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'File sent on ...',
  `Received` tinyint(4) NOT NULL DEFAULT '0' COMMENT '[SUCESS] = 1'
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COMMENT='Transfer of files History';

-- --------------------------------------------------------

--
-- Estrutura da tabela `Servers`
--

CREATE TABLE IF NOT EXISTS `Servers` (
  `ID` int(11) unsigned NOT NULL,
  `Name` varchar(32) NOT NULL COMMENT 'Server Name',
  `IP` varchar(32) NOT NULL COMMENT 'Server IP',
  `Port` int(11) NOT NULL COMMENT 'TCP Port',
  `Status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '[ON] = 1'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COMMENT='Server Info';

-- --------------------------------------------------------

--
-- Estrutura da tabela `Server_Users`
--

CREATE TABLE IF NOT EXISTS `Server_Users` (
  `ServerID` int(10) unsigned NOT NULL,
  `UserID` int(10) unsigned NOT NULL COMMENT 'FK User',
  `Errors` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'Error Count',
  `JoinedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Joined this server',
  `Status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '[Connected] = 1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `ID` int(11) unsigned NOT NULL COMMENT 'User ID',
  `IP_Address` varchar(32) NOT NULL,
  `ConnectionTCP_Port` int(11) NOT NULL,
  `NotificationTCP_Port` int(11) DEFAULT NULL,
  `FileTransferTCP_Port` int(11) DEFAULT NULL,
  `Ping_UDP_Port` int(11) DEFAULT NULL,
  `Name` varchar(32) NOT NULL COMMENT 'User Name',
  `Username` varchar(32) DEFAULT NULL COMMENT 'User Username',
  `Password` varchar(255) DEFAULT NULL COMMENT 'User Passowrd',
  `Blocked` tinyint(4) NOT NULL DEFAULT '0' COMMENT '[BLOCKED] = 1'
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `User`
--

INSERT INTO `User` (`ID`, `IP_Address`, `ConnectionTCP_Port`, `NotificationTCP_Port`, `FileTransferTCP_Port`, `Ping_UDP_Port`, `Name`, `Username`, `Password`, `Blocked`) VALUES
(23, '192.168.192.1', 0, 51257, 51255, NULL, 'FilipeA', 'user1', 'd1e6wrgP1s1ZKYjMP3PhUjCWsqFb6OKbmVQiwKnWZbs=', 0),
(25, '192.168.1.110', 0, 58946, 58944, NULL, 'RicardoS', 'user3', 'd1e6wrgP1s1ZKYjMP3PhUjCWsqFb6OKbmVQiwKnWZbs=', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Files`
--
ALTER TABLE `Files`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `History`
--
ALTER TABLE `History`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ServerID` (`OwnerID`,`TargetID`),
  ADD KEY `fk_history_receiver` (`TargetID`);

--
-- Indexes for table `Servers`
--
ALTER TABLE `Servers`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Server_Users`
--
ALTER TABLE `Server_Users`
  ADD KEY `UserID` (`UserID`),
  ADD KEY `ServerID` (`ServerID`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Username` (`Username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Files`
--
ALTER TABLE `Files`
  MODIFY `ID` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'File ID',AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `History`
--
ALTER TABLE `History`
  MODIFY `ID` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'HistoryID',AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `Servers`
--
ALTER TABLE `Servers`
  MODIFY `ID` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `ID` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'User ID',AUTO_INCREMENT=26;
--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `Files`
--
ALTER TABLE `Files`
  ADD CONSTRAINT `fk_files_user` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`);

--
-- Limitadores para a tabela `History`
--
ALTER TABLE `History`
  ADD CONSTRAINT `fk_history_owner` FOREIGN KEY (`OwnerID`) REFERENCES `User` (`ID`),
  ADD CONSTRAINT `fk_history_receiver` FOREIGN KEY (`TargetID`) REFERENCES `User` (`ID`);

--
-- Limitadores para a tabela `Server_Users`
--
ALTER TABLE `Server_Users`
  ADD CONSTRAINT `conn_server` FOREIGN KEY (`ServerID`) REFERENCES `Servers` (`ID`),
  ADD CONSTRAINT `fk_server_user` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
