-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Tempo de geração: 18/12/2018 às 22:01
-- Versão do servidor: 10.1.36-MariaDB-cll-lve
-- Versão do PHP: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `filmaluc_PD`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `Files`
--

CREATE TABLE `Files` (
  `ID` int(10) UNSIGNED NOT NULL COMMENT 'File ID',
  `UserID` int(10) UNSIGNED NOT NULL COMMENT 'Owner ID',
  `Name` varchar(32) NOT NULL COMMENT 'File Name',
  `Directory` varchar(32) NOT NULL COMMENT 'File Directory',
  `Size` int(10) UNSIGNED NOT NULL COMMENT 'File Size (MB)'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Files Information';

-- --------------------------------------------------------

--
-- Estrutura para tabela `History`
--

CREATE TABLE `History` (
  `ID` int(10) UNSIGNED NOT NULL COMMENT 'HistoryID',
  `OwnerID` int(10) UNSIGNED NOT NULL COMMENT 'FK UserID [Owner]',
  `TargetID` int(10) UNSIGNED NOT NULL COMMENT 'FK UserID [Target]',
  `FileName` int(11) NOT NULL COMMENT 'File name at the moment of the transfer',
  `Date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'File sent on ...',
  `Received` tinyint(4) NOT NULL DEFAULT '0' COMMENT '[SUCESS] = 1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Transfer of files History';

-- --------------------------------------------------------

--
-- Estrutura para tabela `Message`
--

CREATE TABLE `Message` (
  `ID` int(10) UNSIGNED NOT NULL COMMENT 'Message ID',
  `UserID` int(10) UNSIGNED NOT NULL,
  `Message` text NOT NULL COMMENT 'Message Text',
  `CreationDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Message Creation Date'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Users Messages';

-- --------------------------------------------------------

--
-- Estrutura para tabela `Servers`
--

CREATE TABLE `Servers` (
  `ID` int(11) UNSIGNED NOT NULL,
  `Name` varchar(32) NOT NULL COMMENT 'Server Name',
  `IP` varchar(32) NOT NULL COMMENT 'Server IP',
  `Port` int(11) NOT NULL COMMENT 'TCP Port',
  `Status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '[ON] = 1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Server Info';

-- --------------------------------------------------------

--
-- Estrutura para tabela `Server_Users`
--

CREATE TABLE `Server_Users` (
  `ServerID` int(10) UNSIGNED NOT NULL,
  `UserID` int(10) UNSIGNED NOT NULL COMMENT 'FK User',
  `Errors` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT 'Error Count',
  `JoinedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Joined this server',
  `Status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '[Connected] = 1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura para tabela `User`
--

CREATE TABLE `User` (
  `ID` int(11) UNSIGNED NOT NULL COMMENT 'User ID',
  `IP_Address` varchar(32) NOT NULL,
  `ConnectionTCP_Port` int(11) NOT NULL,
  `NotificationTCP_Port` int(11) DEFAULT NULL,
  `FileTransferTCP_Port` int(11) DEFAULT NULL,
  `Ping_UDP_Port` int(11) DEFAULT NULL,
  `Name` varchar(32) NOT NULL COMMENT 'User Name',
  `Username` varchar(32) DEFAULT NULL COMMENT 'User Username',
  `Password` varchar(255) DEFAULT NULL COMMENT 'User Passowrd',
  `Blocked` tinyint(4) NOT NULL DEFAULT '0' COMMENT '[BLOCKED] = 1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura para tabela `User_Messages`
--

CREATE TABLE `User_Messages` (
  `MessageID` int(10) UNSIGNED NOT NULL COMMENT 'FK MessageID',
  `TargetID` int(10) UNSIGNED NOT NULL COMMENT 'Message to UserID [Reveiver]',
  `Seen` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Seen'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Índices de tabelas apagadas
--

--
-- Índices de tabela `Files`
--
ALTER TABLE `Files`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `UserID` (`UserID`);

--
-- Índices de tabela `History`
--
ALTER TABLE `History`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ServerID` (`OwnerID`,`TargetID`),
  ADD KEY `fk_history_receiver` (`TargetID`);

--
-- Índices de tabela `Message`
--
ALTER TABLE `Message`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `UserID` (`UserID`);

--
-- Índices de tabela `Servers`
--
ALTER TABLE `Servers`
  ADD PRIMARY KEY (`ID`);

--
-- Índices de tabela `Server_Users`
--
ALTER TABLE `Server_Users`
  ADD KEY `UserID` (`UserID`),
  ADD KEY `ServerID` (`ServerID`);

--
-- Índices de tabela `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`ID`);

--
-- Índices de tabela `User_Messages`
--
ALTER TABLE `User_Messages`
  ADD KEY `UserID` (`MessageID`,`TargetID`),
  ADD KEY `fk_message_target` (`TargetID`);

--
-- AUTO_INCREMENT de tabelas apagadas
--

--
-- AUTO_INCREMENT de tabela `Files`
--
ALTER TABLE `Files`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'File ID';

--
-- AUTO_INCREMENT de tabela `History`
--
ALTER TABLE `History`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'HistoryID';

--
-- AUTO_INCREMENT de tabela `Message`
--
ALTER TABLE `Message`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Message ID';

--
-- AUTO_INCREMENT de tabela `Servers`
--
ALTER TABLE `Servers`
  MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=176;

--
-- AUTO_INCREMENT de tabela `User`
--
ALTER TABLE `User`
  MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'User ID';

--
-- Restrições para dumps de tabelas
--

--
-- Restrições para tabelas `Files`
--
ALTER TABLE `Files`
  ADD CONSTRAINT `fk_files_user` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`);

--
-- Restrições para tabelas `History`
--
ALTER TABLE `History`
  ADD CONSTRAINT `fk_history_owner` FOREIGN KEY (`OwnerID`) REFERENCES `User` (`ID`),
  ADD CONSTRAINT `fk_history_receiver` FOREIGN KEY (`TargetID`) REFERENCES `User` (`ID`);

--
-- Restrições para tabelas `Message`
--
ALTER TABLE `Message`
  ADD CONSTRAINT `fk_message_user` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`);

--
-- Restrições para tabelas `Server_Users`
--
ALTER TABLE `Server_Users`
  ADD CONSTRAINT `conn_server` FOREIGN KEY (`ServerID`) REFERENCES `Servers` (`ID`),
  ADD CONSTRAINT `fk_server_user` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`);

--
-- Restrições para tabelas `User_Messages`
--
ALTER TABLE `User_Messages`
  ADD CONSTRAINT `conn_message` FOREIGN KEY (`MessageID`) REFERENCES `Message` (`ID`),
  ADD CONSTRAINT `fk_message_target` FOREIGN KEY (`TargetID`) REFERENCES `User` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
