-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 03, 2023 at 01:24 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `vcms_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `activation`
--

CREATE TABLE `activation` (
  `activation_code` varchar(10) DEFAULT NULL,
  `code_status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `activation`
--

INSERT INTO `activation` (`activation_code`, `code_status`) VALUES
('N5VYMQ2OXA', 'Expired'),
('9M6HBJZ07Z', 'Active');

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_id` varchar(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `admin_email` varchar(100) NOT NULL,
  `security_question` varchar(255) DEFAULT NULL,
  `security_answer` varchar(255) DEFAULT NULL,
  `admin_code` varchar(50) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`admin_id`, `username`, `password`, `admin_email`, `security_question`, `security_answer`, `admin_code`, `is_active`) VALUES
('2c1a9588', 'vetsysadmin', 'vcmspassword', 'email@sample.com', 'What was the first concert you attended?', 'Hololive Link Your Wish', 'A4C789', 0),
('K0sv2WS7', 'test', 'test', 'test', 'What is your dream car/motorcycle?', 'test', 'test', 0),
('NS210461', 'Test4', 'password', 'test4@email.com', 'What is your favorite song?', 'test', 'N5VYMQ2OXA', 0);

-- --------------------------------------------------------

--
-- Table structure for table `appointment`
--

CREATE TABLE `appointment` (
  `appointment_id` varchar(10) NOT NULL,
  `client_name` varchar(255) DEFAULT NULL,
  `client_contact` varchar(20) DEFAULT NULL,
  `service_required` varchar(255) DEFAULT NULL,
  `assigned_personnel` varchar(255) DEFAULT NULL,
  `appointment_date` date DEFAULT NULL,
  `appointment_time` time DEFAULT NULL,
  `appointment_status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appointment`
--

INSERT INTO `appointment` (`appointment_id`, `client_name`, `client_contact`, `service_required`, `assigned_personnel`, `appointment_date`, `appointment_time`, `appointment_status`) VALUES
('AG312323', 'Harry Panganiban', '09776758443', 'Laboratory Services', 'Jake Rosales', '2023-12-01', '14:30:00', 'Ongoing'),
('jTDmTUOS', 'Raul Marquez', '09876543212', 'Diagnosis and Treatment', 'Dwayne Santiago', '2023-11-25', '12:30:00', 'Completed'),
('PG163167', 'Jhon Magno', '09776742212', 'Parasite Control', 'Princess Mae Delgado', '2023-11-29', '13:00:00', 'Completed');

-- --------------------------------------------------------

--
-- Table structure for table `boarders`
--

CREATE TABLE `boarders` (
  `boarder_id` varchar(10) NOT NULL,
  `boarder_name` varchar(255) DEFAULT NULL,
  `boarder_species` varchar(255) DEFAULT NULL,
  `boarder_breed` varchar(255) DEFAULT NULL,
  `boarder_color` varchar(255) DEFAULT NULL,
  `b_special_instructions` text DEFAULT NULL,
  `b_owner_name` varchar(255) DEFAULT NULL,
  `b_owner_contact` varchar(20) DEFAULT NULL,
  `b_owner_address` text DEFAULT NULL,
  `date_boarded` date DEFAULT NULL,
  `boarder_age` decimal(3,1) DEFAULT NULL,
  `boarder_gender` varchar(10) DEFAULT NULL,
  `boarder_weight` decimal(6,2) DEFAULT NULL,
  `date_departed` date DEFAULT NULL,
  `b_owner_email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `boarders`
--

INSERT INTO `boarders` (`boarder_id`, `boarder_name`, `boarder_species`, `boarder_breed`, `boarder_color`, `b_special_instructions`, `b_owner_name`, `b_owner_contact`, `b_owner_address`, `date_boarded`, `boarder_age`, `boarder_gender`, `boarder_weight`, `date_departed`, `b_owner_email`) VALUES
('FD852830', 'Sarge', 'Dog', 'Rottweiler', 'Black', 'n/a', 'Marvin Rosario', '09772113234', 'Batangas City', '2023-11-29', 6.3, 'Female', 47.50, '2024-01-04', 'marvin@email.com'),
('JV263059', 'Buddy', 'Dog', 'Great Dane', 'Black', 'n/a', 'John Rodriguez', '09875644123', 'Lipa City', '2023-11-26', 4.3, 'Male', 56.40, '2024-02-27', 'johndr@email.com'),
('OE681159', 'Phil', 'Cat', 'Russian Blue', 'Gray', 'n/a', 'RB Santos', '09768892332', 'Lipa City', '2023-12-04', 4.5, 'Male', 5.40, '2023-12-30', 'rb@email.com');

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `item_id` varchar(10) NOT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `item_type` varchar(255) DEFAULT NULL,
  `item_quantity` int(11) DEFAULT NULL,
  `unit_cost` decimal(10,2) DEFAULT NULL,
  `item_supplier` varchar(255) DEFAULT NULL,
  `exp_date` date DEFAULT NULL,
  `item_status` varchar(255) DEFAULT NULL,
  `total_cost` decimal(7,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`item_id`, `item_name`, `item_type`, `item_quantity`, `unit_cost`, `item_supplier`, `exp_date`, `item_status`, `total_cost`) VALUES
('UK962075', 'Generic Smart Printer', 'Office Supplies', 2, 6500.00, 'Generic Inc.', NULL, 'In transit', 13000.00),
('YJ636824', 'Brand X Dog Food', 'Food and Water', 30, 350.00, 'X Corp', '2025-04-16', 'In Stock', 10500.00);

-- --------------------------------------------------------

--
-- Table structure for table `kennels`
--

CREATE TABLE `kennels` (
  `kennel_id` varchar(10) NOT NULL,
  `kennel_name` varchar(255) DEFAULT NULL,
  `kennel_capacity` int(11) DEFAULT NULL,
  `kennel_status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kennels`
--

INSERT INTO `kennels` (`kennel_id`, `kennel_name`, `kennel_capacity`, `kennel_status`) VALUES
('NI528516', 'Boarding Kennel', 5, 'Available for Boarding');

-- --------------------------------------------------------

--
-- Table structure for table `laboratories`
--

CREATE TABLE `laboratories` (
  `lab_id` varchar(10) NOT NULL,
  `lab_name` varchar(255) DEFAULT NULL,
  `no_of_lab_equipment` int(11) DEFAULT NULL,
  `lab_status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `laboratories`
--

INSERT INTO `laboratories` (`lab_id`, `lab_name`, `no_of_lab_equipment`, `lab_status`) VALUES
('IX955587', 'Clinical Pathology Lab', 5, 'Available for Testing');

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `patient_id` varchar(10) NOT NULL,
  `patient_name` varchar(255) DEFAULT NULL,
  `patient_species` varchar(255) DEFAULT NULL,
  `patient_breed` varchar(255) DEFAULT NULL,
  `age_in_years` decimal(3,1) DEFAULT NULL,
  `patient_color` varchar(255) DEFAULT NULL,
  `admitted_date` date DEFAULT NULL,
  `medical_history` text DEFAULT NULL,
  `vaccination_history` text DEFAULT NULL,
  `special_instruction` text DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `owner_contact` varchar(20) DEFAULT NULL,
  `owner_email` varchar(255) DEFAULT NULL,
  `owner_address` text DEFAULT NULL,
  `patient_insurance` varchar(255) DEFAULT NULL,
  `patient_weight` decimal(6,2) DEFAULT NULL,
  `mchip_id` varchar(255) DEFAULT NULL,
  `patient_gender` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`patient_id`, `patient_name`, `patient_species`, `patient_breed`, `age_in_years`, `patient_color`, `admitted_date`, `medical_history`, `vaccination_history`, `special_instruction`, `owner_name`, `owner_contact`, `owner_email`, `owner_address`, `patient_insurance`, `patient_weight`, `mchip_id`, `patient_gender`) VALUES
('1dd2af4e', 'Buddy', 'Dog', 'Golden Retriever', 2.0, 'Light Brown', '2023-11-18', 'n/a', 'Rabies Vaccine\nDistemper Vaccine\nParainfluenza Vaccine', 'n/a', 'Juan Dela Cruz', '09458762314', 'jdc@sample.com', 'Batangas City', 'n/a', 34.00, 'n/a', 'Male'),
('1f0d8ddd', 'Ming', 'Cat', 'Tabby Cat', 2.5, 'Orange', '2023-11-19', 'n/a', 'Rabies Vaccine\nParvovirus Vaccine\n', 'n/a', 'Maria Santos', '09454623134', 'maria@sample.com', 'Lemery', 'n/a', 4.50, 'n/a', 'Male'),
('HC734711', 'Scout', 'Dog', 'Chow Chow', 8.5, 'Red', '2023-12-04', 'n/a', 'n/a', 'n/a', 'Paul Bautista', '09454432212', 'paul@email.com', 'Batangas City', 'n/a', 32.00, 'n/a', 'Male'),
('RT493871', 'Dolly', 'Snake', 'Ball Python', 6.5, 'Axanthic', '2023-11-27', 'n/a', 'n/a', 'n/a', 'Richard Perez', '09774563421', 'ricky@email.com', 'Lipa City', 'n/a', 1.80, 'n/a', 'Female'),
('YL271643', 'Billy', 'Bird', 'Red and Green Macaw', 25.5, 'Red and Green', '2023-11-29', 'Regular Checkups', 'Polyomavirus Vaccine', 'n/a', 'Nigel Sanchez', '09855623435', 'nigel@email.com', 'Batangas City', 'n/a', 1.70, 'n/a', 'Male');

-- --------------------------------------------------------

--
-- Table structure for table `personnel`
--

CREATE TABLE `personnel` (
  `personnel_id` varchar(10) NOT NULL,
  `personnel_name` varchar(255) DEFAULT NULL,
  `personnel_email` varchar(255) DEFAULT NULL,
  `personnel_address` text DEFAULT NULL,
  `personnel_contact` varchar(20) DEFAULT NULL,
  `emergency_contact` varchar(20) DEFAULT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  `vet_specialization` varchar(255) DEFAULT NULL,
  `personnel_certification` varchar(255) DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `work_schedule` text DEFAULT NULL,
  `performance_rating` decimal(3,2) DEFAULT NULL,
  `attendance_rating` decimal(3,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `personnel`
--

INSERT INTO `personnel` (`personnel_id`, `personnel_name`, `personnel_email`, `personnel_address`, `personnel_contact`, `emergency_contact`, `job_title`, `vet_specialization`, `personnel_certification`, `hire_date`, `work_schedule`, `performance_rating`, `attendance_rating`) VALUES
('NV069387', 'Jake Rosales', 'jrk@email.com', 'Batangas City', '09877563434', '09778865326', 'Laboratory Technician', 'n/a', 'VLTCC', '2023-11-27', 'Mon-Fri: 7:00 AM - 5:00 PM', NULL, NULL),
('OI127134', 'Dwayne Santiago', 'dwin@email.com', 'Batangas City', '09785672345', '09671234889', 'Veterinarian', 'Veterinary Dermatology', 'PCVD', '2020-07-15', 'Mon, Fri: 5:00 AM - 5:00 PM, Wed: 6:00 AM - 4:00 PM', 9.00, 8.00),
('VP288792', 'Princess Mae Delgado', 'princ355@email.com', 'Batangas City', '09778769999', '097756567743', 'Veterinary Technician', 'n/a', 'PCVD', '2023-11-23', 'Mon-Thurs: 5:00 AM - 7:00 PM', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `transaction_id` varchar(10) NOT NULL,
  `payer` varchar(255) DEFAULT NULL,
  `payee` varchar(255) DEFAULT NULL,
  `transaction_type` varchar(255) DEFAULT NULL,
  `transaction_desc` text DEFAULT NULL,
  `transaction_amt` decimal(10,2) DEFAULT NULL,
  `amt_received` decimal(10,2) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `receipt_no` int(10) DEFAULT NULL,
  `transac_datetime` datetime DEFAULT NULL,
  `transaction_change` decimal(10,2) DEFAULT NULL,
  `transaction_status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`transaction_id`, `payer`, `payee`, `transaction_type`, `transaction_desc`, `transaction_amt`, `amt_received`, `payment_method`, `receipt_no`, `transac_datetime`, `transaction_change`, `transaction_status`) VALUES
('EZ527805', 'Jhon Magno', 'Generic Veterinary Clinic', 'Preventive Care Packages', 'Parasite Control Package', 4200.00, 5000.00, 'Cash', 844017776, '2023-12-01 13:44:10', 800.00, 'Captured'),
('MI203964', 'Raul Marquez', 'Generic Veterinary Clinic', 'Diagnostic Services', 'Parasitology Diagnosis', 4400.00, 5000.00, 'Cash', 208269627, '2023-12-01 13:44:01', 600.00, 'Captured'),
('PD953991', 'John Rodriguez', 'Generic Veterinary Clinc', 'Boarding and Grooming Fees', '2 Month Board + Grooming', 8550.00, 9000.00, 'Cash', 1276307446, '2023-11-26 23:25:20', 450.00, 'Captured');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_id`);

--
-- Indexes for table `appointment`
--
ALTER TABLE `appointment`
  ADD PRIMARY KEY (`appointment_id`);

--
-- Indexes for table `boarders`
--
ALTER TABLE `boarders`
  ADD PRIMARY KEY (`boarder_id`);

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`item_id`);

--
-- Indexes for table `kennels`
--
ALTER TABLE `kennels`
  ADD PRIMARY KEY (`kennel_id`);

--
-- Indexes for table `laboratories`
--
ALTER TABLE `laboratories`
  ADD PRIMARY KEY (`lab_id`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`patient_id`);

--
-- Indexes for table `personnel`
--
ALTER TABLE `personnel`
  ADD PRIMARY KEY (`personnel_id`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`transaction_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
