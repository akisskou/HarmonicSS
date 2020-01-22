-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Φιλοξενητής: 127.0.0.1
-- Χρόνος δημιουργίας: 22 Ιαν 2020 στις 01:46:35
-- Έκδοση διακομιστή: 10.1.29-MariaDB
-- Έκδοση PHP: 7.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Βάση δεδομένων: `harmonicssdb`
--

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `cohort`
--

CREATE TABLE `cohort` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `PARAMETER` varchar(25) NOT NULL,
  `VALUE` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `cohort`
--

INSERT INTO `cohort` (`ID`, `PARAMETER`, `VALUE`) VALUES
(1, 'Biopsy', 'Biopsy'),
(2, 'Body-Weight', 'Weight'),
(3, 'Chemotherapy', 'Chemotherapy'),
(4, 'Clinical-Trial', 'Clinical Trial'),
(5, 'Diagnosis', 'Diagnosis'),
(6, 'ESSDAI-Domain-AL', 'ESSDAI Domain Activity Level'),
(7, 'Education', 'Education'),
(8, 'Ethnicity', 'Ethnicity'),
(9, 'Gender', 'Gender'),
(10, 'Laboratory-Test', 'Laboratory Examination / Test'),
(11, 'Medication', 'Medication'),
(12, 'Occupation', 'Occupation'),
(13, 'Person', 'Person'),
(14, 'Pregnancy', 'Pregnancy Data'),
(15, 'Questionnaire-Score', 'Questionnaire Score'),
(16, 'Surgery', 'Surgery / Operation'),
(17, 'Symptom-or-Sign', 'Symptom / Sign'),
(18, 'Tobacco-Consumption', 'Tobacco Consumption Data'),
(19, 'ACTLVL-00', 'NO'),
(20, 'ACTLVL-01', 'LOW'),
(21, 'ACTLVL-02', 'MODERATE'),
(22, 'ACTLVL-03', 'HIGH'),
(23, 'ANA-PAT-01', 'Nuclear Speckled'),
(24, 'ANA-PAT-02', 'Nuclear Homogeneous'),
(25, 'ANA-PAT-03', 'Nucleolar'),
(26, 'ANA-PAT-05', 'Cytoplasmic Antimitochondrial'),
(27, 'ASSESS-20', 'Abnormal'),
(28, 'BIOPSY-11', 'Germinal Centers [check]'),
(29, 'BIOPSY-21', 'Greenspan Focus Score'),
(30, 'BIOPSY-22', 'Chisholm-Mason Score'),
(31, 'BLOOD-110', 'Hemoglobin [Mass/volume]'),
(32, 'BLOOD-120', 'Leukocytes [#/volume]'),
(33, 'BLOOD-130', 'Neutrophils [#/volume]'),
(34, 'BLOOD-140', 'Lymphocytes [#/volume]'),
(35, 'BLOOD-150', 'Platelets (aka Thrombocytes)  [#/volume]'),
(36, 'BLOOD-211', 'Albumin [Mass/volume]'),
(37, 'BLOOD-212', 'Creatinine [Mass/volume]'),
(38, 'BLOOD-221', 'Alkaline phosphatase (ALP)'),
(39, 'BLOOD-222', 'Alanine aminotransferase (ALT)'),
(40, 'BLOOD-223', 'Aspartate aminotransferase (AST)'),
(41, 'BLOOD-224', 'Gamma glutamyl transferase (?-GT)'),
(42, 'BLOOD-225', 'Lactate dehydrogenase (LDH)'),
(43, 'BLOOD-233', 'Creatine phosphokinase (CPK) Muscle Enzymes [Enzymatic activity/volume]'),
(44, 'BLOOD-311', 'Cryoglobulins [presence]'),
(45, 'BLOOD-312', 'Cryoglobulins Type [term]'),
(46, 'BLOOD-322', 'Immunoglobulin A (IgA) [Mass/volume]'),
(47, 'BLOOD-323', 'Immunoglobulin G (IgG) [Mass/volume]'),
(48, 'BLOOD-325', 'Immunoglobulin M (IgM) [Mass/volume]'),
(49, 'BLOOD-326', 'Kappa Free light chains [Mass/volume] (in Serum)'),
(50, 'BLOOD-327', 'Lambda Free light chains [Mass/volume] (in Serum)'),
(51, 'BLOOD-330', 'Monoclonal M-component [presence] (in Serum)'),
(52, 'BLOOD-511', 'Rheumatoid Factor (RF) [Units/volume]'),
(53, 'BLOOD-521', 'Antinuclear Antibodies (ANA) [presence]'),
(54, 'BLOOD-522', 'Antinuclear Antibodies (ANA) titer'),
(55, 'BLOOD-523', 'Antinuclear Antibodies (ANA) pattern [term]'),
(56, 'BLOOD-530', 'Anti-Ro/SSA [presence]'),
(57, 'BLOOD-531', 'Anti-Ro52/SSA [presence]'),
(58, 'BLOOD-540', 'Anti-La/SSB [presence]'),
(59, 'BLOOD-610', 'C3 levels (Serum complement) [Mass/volume]'),
(60, 'BLOOD-620', 'C4 levels (Serum complement) [Mass/volume]'),
(61, 'BLOOD-710', 'CD3 (T cells) [#/volume]'),
(62, 'BLOOD-720', 'CD4 (T helper/inducer cells) [#/volume]'),
(63, 'BLOOD-730', 'CD8 (T suppressor/cytotoxic cells) [#/volume]'),
(64, 'BLOOD-740', 'CD19 (B-lymphocyte antigen) [#/volume]'),
(65, 'BLOOD-810', 'C reactive protein [Mass/volume]'),
(66, 'BLOOD-820', 'Anti-Hepatitis C Virus (HCV) antibody [presence]'),
(67, 'BLOOD-860', 'Hepatitis B virus (HBV) DNA [Presence]'),
(68, 'BLOOD-910', 'Beta-2 Microglobulin [Mass/volume] (in Serum)'),
(69, 'CHEM-10000', 'Glucocorticoids'),
(70, 'CHEM-21040', 'Hydroxychloroquine'),
(71, 'CHEM-21070', 'Cyclosporine'),
(72, 'CHEM-22100', 'Rituximab'),
(73, 'CHEM-43000', 'Oral Pilocarpine'),
(74, 'COND-010000', 'Circulatory System Disease'),
(75, 'COND-020000', 'Digestive System Disease'),
(76, 'COND-021000', 'Liver Disease'),
(77, 'COND-021100', 'Autoimmune Hepatitis'),
(78, 'COND-021200', 'Primary Biliary Cholangitis'),
(79, 'COND-022000', 'Celiac Disease'),
(80, 'COND-031000', 'Autoimmune Thyroid Disease'),
(81, 'COND-040000', 'Cutaneous Disease'),
(82, 'COND-045000', 'Palpable Purpura'),
(83, 'COND-050000', 'Lymphatic System Disease'),
(84, 'COND-052300', 'Hodgkin\'s Lymphoma'),
(85, 'COND-052400', 'Non-Hodgkin lymphoma (NHL)'),
(86, 'COND-052510', 'B-cell Marginal Zone Lymphoma'),
(87, 'COND-052511', 'B-cell Mucosa-associated Lymphoid Tissue (MALT) Lymphoma'),
(88, 'COND-052513', 'B-cell Splenic Marginal Zone Lymphoma'),
(89, 'COND-052522', 'Diffuse Large B-cell Lymphoma'),
(90, 'COND-052524', 'T-Cell/Histiocyte-Rich B-Cell Lymphoma'),
(91, 'COND-052527', 'Anaplastic large cell lymphoma'),
(92, 'COND-060000', 'Muscular System Disease'),
(93, 'COND-071000', 'Central Nervous System Disease'),
(94, 'COND-072000', 'Peripheral Nervous System Disease'),
(95, 'COND-072200', 'Sensorimotor Axonal Polyneuropathy'),
(96, 'COND-081000', 'Renal Disease'),
(97, 'COND-091000', 'Pulmonary Disease'),
(98, 'COND-101000', 'Arthritis'),
(99, 'COND-102000', 'Fibromyalgia'),
(100, 'COND-110000', 'Hematologic Disease'),
(101, 'COND-140000', 'Cancer'),
(102, 'COND-152200', 'Inflammatory bowel disease'),
(103, 'CRYO-03', 'Type III'),
(104, 'EDU-LEV-01', 'Primary Education'),
(105, 'EDU-LEV-02', 'Secondary Education'),
(106, 'EDU-LEV-03', 'Tertiary education'),
(107, 'ESSDAI-01', 'Constitutional'),
(108, 'ESSDAI-02', 'Lymphadenopathy and lymphoma'),
(109, 'ESSDAI-03', 'Glandular'),
(110, 'ESSDAI-04', 'Articular'),
(111, 'ESSDAI-05', 'Cutaneous'),
(112, 'ESSDAI-06', 'Pulmonary'),
(113, 'ESSDAI-07', 'Renal'),
(114, 'ESSDAI-08', 'Muscular'),
(115, 'ESSDAI-09', 'Peripheral nervous system'),
(116, 'ESSDAI-10', 'Central nervous system'),
(117, 'ESSDAI-11', 'Hematological'),
(118, 'ESSDAI-12', 'Biological'),
(119, 'ETHN-01', 'Latin'),
(120, 'ETHN-03', 'Asian'),
(121, 'ETHN-06', 'Caucasian'),
(122, 'OCULAR-01', 'Schirmer\'s Test (Worst Eye) Value'),
(123, 'OCULAR-02', 'Rose Bengal Staining / Van Bijsterveld Score'),
(124, 'OCULAR-03', 'Ocular Staining Score / Fluorescein Lissamine Green'),
(125, 'ORAL-11', 'Unstimulated saliva flow Value'),
(126, 'QUEST-010', 'EULAR Sjogren\'s syndrome Disease Activity Index (ESSDAI) score'),
(127, 'QUEST-020', 'EULAR Sjogren\'s Syndrome Patient Reported Index (ESSPRI) score'),
(128, 'QUEST-030', 'Charlson Age-Comorbidity Index (CACI) score'),
(129, 'QUEST-081', 'Oral Sicca Visual Analogue Scale (VAS) score'),
(130, 'QUEST-082', 'Ocular Sicca Visual Analogue Scale (VAS) score'),
(131, 'QUEST-083', 'Vaginal sicca Visual Analogue Scale (VAS) score'),
(132, 'QUEST-084', 'Dyspareunia Visual Analogue Scale (VAS) score'),
(133, 'QUEST-085', 'Fatigue Visual Analogue Scale (VAS) score'),
(134, 'QUEST-111', 'HADS - Anxiety Score'),
(135, 'QUEST-112', 'HADS - Depression Score'),
(136, 'QUEST-140', 'Profile of Fatigue and Discomfort (PROFAD) score'),
(137, 'QUEST-150', 'Sicca Symptoms Inventory (SSI) score'),
(138, 'QUEST-160', 'Profile of Fatigue and Discomfort - Sicca Symptoms Inventory - Short Form (PROFAD-SSI-SF) score'),
(139, 'SAL-BIO-21', 'Labial Gland (lip) Biopsy'),
(140, 'SEX-01', 'Male'),
(141, 'SEX-02', 'Female'),
(142, 'SMOK-STAT-01', 'Active '),
(143, 'SMOK-STAT-02', 'Ex-Smoker '),
(144, 'SMOK-STAT-03', 'Never Smoker'),
(145, 'SYMPT-010', 'Parotid or Submandibular swelling'),
(146, 'SYMPT-020', 'Dry Mouth'),
(147, 'SYMPT-030', 'Dry Eyes'),
(148, 'SYMPT-160', 'Fatigue'),
(149, 'SYMPT-180', 'Raynaud\'s Phenomenon'),
(150, 'URINE-110', 'Urine pH'),
(151, 'URINE-130', 'Urine Specific Gravity'),
(152, 'URINE-140', 'Blood [Presence] in Urine'),
(153, 'URINE-150', 'Protein [Presence] in Urine'),
(154, 'URINE-300', 'Bence-Jones Proteinuria [Presence]');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `cond_diagnosis`
--

CREATE TABLE `cond_diagnosis` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `CONDITION_ID` smallint(5) UNSIGNED NOT NULL,
  `STAGE_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `PERFORMANCE_STATUS_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `DIAGNOSIS_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL,
  `STMT_ID` tinyint(3) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `cond_diagnosis`
--

INSERT INTO `cond_diagnosis` (`ID`, `PATIENT_ID`, `CONDITION_ID`, `STAGE_ID`, `PERFORMANCE_STATUS_ID`, `DIAGNOSIS_DATE_ID`, `VISIT_ID`, `STMT_ID`) VALUES
(158037, 8621, 25, NULL, NULL, 859776, 8615, 1),
(158038, 8621, 114, NULL, NULL, 859777, 8615, 1),
(158039, 8621, 33, NULL, NULL, 859778, 8615, 1),
(158040, 8621, 45, NULL, NULL, 859779, 8615, 1),
(158041, 8621, 113, NULL, NULL, 859780, 8615, 1),
(158042, 8621, 14, NULL, NULL, 859781, 8615, 1),
(158043, 8621, 106, NULL, NULL, 859798, 8615, 2),
(158044, 8621, 22, NULL, NULL, 859799, 8615, 2),
(158045, 8621, 73, NULL, NULL, 859801, 8615, 2),
(158046, 8621, 99, NULL, NULL, 859802, 8615, 2),
(158047, 8621, 17, NULL, NULL, NULL, 8615, 2),
(158048, 8621, 65, NULL, NULL, 859803, 8615, 2),
(158049, 8621, 67, NULL, NULL, 859804, 8615, 2),
(158050, 8621, 7, NULL, NULL, 859805, 8615, 2),
(158051, 8621, 8, NULL, NULL, 859806, 8615, 2),
(158052, 8621, 1, NULL, NULL, 859807, 8615, 2),
(158053, 8621, 58, NULL, NULL, 859808, 8615, 2),
(158054, 8621, 54, NULL, NULL, 859809, 8615, 2),
(158055, 8621, 11, NULL, NULL, 859810, 8615, 2),
(158056, 8621, 131, NULL, NULL, 859811, 8615, 2),
(158057, 8622, 17, NULL, NULL, NULL, 8616, 1),
(158058, 8622, 35, NULL, NULL, 859907, 8616, 1),
(158059, 8622, 113, NULL, NULL, 859908, 8616, 1),
(158060, 8622, 106, NULL, NULL, 859917, 8616, 2),
(158061, 8622, 22, NULL, NULL, 859918, 8616, 2),
(158062, 8622, 25, NULL, NULL, 859919, 8616, 2),
(158063, 8622, 73, NULL, NULL, 859920, 8616, 2),
(158064, 8622, 114, NULL, NULL, 859921, 8616, 2),
(158065, 8622, 99, NULL, NULL, 859922, 8616, 2),
(158066, 8622, 65, NULL, NULL, 859923, 8616, 2),
(158067, 8622, 67, NULL, NULL, 859924, 8616, 2),
(158068, 8622, 7, NULL, NULL, 859925, 8616, 2),
(158069, 8622, 8, NULL, NULL, 859926, 8616, 2),
(158070, 8622, 1, NULL, NULL, 859927, 8616, 2),
(158071, 8622, 58, NULL, NULL, 859928, 8616, 2),
(158072, 8622, 54, NULL, NULL, 859929, 8616, 2),
(158073, 8622, 33, NULL, NULL, 859930, 8616, 2),
(158074, 8622, 11, NULL, NULL, 859931, 8616, 2),
(158075, 8622, 14, NULL, NULL, 859932, 8616, 2),
(158076, 8622, 131, NULL, NULL, 859933, 8616, 2),
(158077, 8623, 8, NULL, NULL, 860034, 8617, 1),
(158078, 8623, 42, NULL, NULL, 860035, 8617, 1),
(158079, 8623, 113, NULL, NULL, 860036, 8617, 1),
(158080, 8623, 9, NULL, NULL, 860037, 8617, 1),
(158081, 8623, 106, NULL, NULL, 860050, 8617, 2),
(158082, 8623, 22, NULL, NULL, 860051, 8617, 2),
(158083, 8623, 25, NULL, NULL, 860053, 8617, 2),
(158084, 8623, 73, NULL, NULL, 860054, 8617, 2),
(158085, 8623, 114, NULL, NULL, 860055, 8617, 2),
(158086, 8623, 99, NULL, NULL, 860056, 8617, 2),
(158087, 8623, 17, NULL, NULL, NULL, 8617, 2),
(158088, 8623, 65, NULL, NULL, 860057, 8617, 2),
(158089, 8623, 67, NULL, NULL, 860058, 8617, 2),
(158090, 8623, 7, NULL, NULL, 860059, 8617, 2),
(158091, 8623, 1, NULL, NULL, 860060, 8617, 2),
(158092, 8623, 58, NULL, NULL, 860061, 8617, 2),
(158093, 8623, 54, NULL, NULL, 860062, 8617, 2),
(158094, 8623, 33, NULL, NULL, 860063, 8617, 2),
(158095, 8623, 11, NULL, NULL, 860064, 8617, 2),
(158096, 8623, 14, NULL, NULL, 860065, 8617, 2),
(158097, 8623, 131, NULL, NULL, 860066, 8617, 2),
(158098, 8624, 106, NULL, NULL, 860165, 8618, 1),
(158099, 8624, 36, NULL, NULL, 860166, 8618, 1),
(158100, 8624, 113, NULL, NULL, 860167, 8618, 1),
(158101, 8624, 11, NULL, NULL, 860168, 8618, 1),
(158102, 8624, 14, NULL, NULL, 860169, 8618, 1),
(158103, 8624, 40, NULL, NULL, 860170, 8618, 1),
(158104, 8624, 22, NULL, NULL, 860182, 8618, 2),
(158105, 8624, 25, NULL, NULL, 860184, 8618, 2),
(158106, 8624, 73, NULL, NULL, 860185, 8618, 2),
(158107, 8624, 114, NULL, NULL, 860186, 8618, 2),
(158108, 8624, 99, NULL, NULL, 860187, 8618, 2),
(158109, 8624, 17, NULL, NULL, NULL, 8618, 2),
(158110, 8624, 65, NULL, NULL, 860188, 8618, 2),
(158111, 8624, 67, NULL, NULL, 860189, 8618, 2),
(158112, 8624, 7, NULL, NULL, 860190, 8618, 2),
(158113, 8624, 8, NULL, NULL, 860191, 8618, 2),
(158114, 8624, 1, NULL, NULL, 860192, 8618, 2),
(158115, 8624, 58, NULL, NULL, 860193, 8618, 2),
(158116, 8624, 54, NULL, NULL, 860194, 8618, 2),
(158117, 8624, 33, NULL, NULL, 860195, 8618, 2),
(158118, 8624, 131, NULL, NULL, 860196, 8618, 2),
(158119, 8625, 114, NULL, NULL, 860297, 8619, 1),
(158120, 8625, 32, NULL, NULL, 860298, 8619, 1),
(158121, 8625, 113, NULL, NULL, 860299, 8619, 1),
(158122, 8625, 14, NULL, NULL, 860300, 8619, 1),
(158123, 8625, 40, NULL, NULL, 860301, 8619, 1),
(158124, 8625, 131, NULL, NULL, 860302, 8619, 1),
(158125, 8625, 106, NULL, NULL, 860307, 8619, 2),
(158126, 8625, 22, NULL, NULL, 860308, 8619, 2),
(158127, 8625, 25, NULL, NULL, 860310, 8619, 2),
(158128, 8625, 73, NULL, NULL, 860311, 8619, 2),
(158129, 8625, 99, NULL, NULL, 860312, 8619, 2),
(158130, 8625, 17, NULL, NULL, NULL, 8619, 2),
(158131, 8625, 65, NULL, NULL, 860313, 8619, 2),
(158132, 8625, 67, NULL, NULL, 860314, 8619, 2),
(158133, 8625, 7, NULL, NULL, 860315, 8619, 2),
(158134, 8625, 8, NULL, NULL, 860316, 8619, 2),
(158135, 8625, 1, NULL, NULL, 860317, 8619, 2),
(158136, 8625, 58, NULL, NULL, 860318, 8619, 2),
(158137, 8625, 54, NULL, NULL, 860319, 8619, 2),
(158138, 8625, 33, NULL, NULL, 860320, 8619, 2),
(158139, 8625, 11, NULL, NULL, 860321, 8619, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `cond_diagnosis_organs`
--

CREATE TABLE `cond_diagnosis_organs` (
  `ID` int(10) UNSIGNED NOT NULL,
  `DIAGNOSIS_ID` int(10) UNSIGNED NOT NULL,
  `ORGAN_ID` smallint(5) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `cond_link_diagnosis_lab_tests`
--

CREATE TABLE `cond_link_diagnosis_lab_tests` (
  `ID` int(10) UNSIGNED NOT NULL,
  `DIAGNOSIS_ID` int(10) UNSIGNED NOT NULL,
  `LAB_TEST_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `cond_link_diagnosis_symptoms`
--

CREATE TABLE `cond_link_diagnosis_symptoms` (
  `ID` int(10) UNSIGNED NOT NULL,
  `DIAGNOSIS_ID` int(10) UNSIGNED NOT NULL,
  `SYMPTOMS_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `cond_symptom`
--

CREATE TABLE `cond_symptom` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `CONDITION_ID` smallint(5) UNSIGNED NOT NULL,
  `OBSERVE_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL,
  `STMT_ID` tinyint(3) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `cond_symptom`
--

INSERT INTO `cond_symptom` (`ID`, `PATIENT_ID`, `CONDITION_ID`, `OBSERVE_DATE_ID`, `VISIT_ID`, `STMT_ID`) VALUES
(40967, 8621, 13, 859775, 8615, 1),
(40968, 8621, 4, 859782, 8615, 1),
(40969, 8621, 5, 859783, 8615, 1),
(40970, 8621, 15, 859800, 8615, 2),
(40971, 8621, 1, 859812, 8615, 2),
(40972, 8622, 13, 859905, 8616, 1),
(40973, 8622, 15, 859906, 8616, 1),
(40974, 8622, 4, 859909, 8616, 1),
(40975, 8622, 5, 859910, 8616, 1),
(40976, 8622, 1, 859934, 8616, 2),
(40977, 8623, 1, 860038, 8617, 1),
(40978, 8623, 13, 860049, 8617, 2),
(40979, 8623, 15, 860052, 8617, 2),
(40980, 8623, 4, 860067, 8617, 2),
(40981, 8623, 5, 860068, 8617, 2),
(40982, 8624, 13, 860164, 8618, 1),
(40983, 8624, 4, 860171, 8618, 1),
(40984, 8624, 5, 860172, 8618, 1),
(40985, 8624, 15, 860183, 8618, 2),
(40986, 8624, 1, 860197, 8618, 2),
(40987, 8625, 4, 860303, 8619, 1),
(40988, 8625, 5, 860304, 8619, 1),
(40989, 8625, 13, 860306, 8619, 2),
(40990, 8625, 15, 860309, 8619, 2),
(40991, 8625, 1, 860322, 8619, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `demo_education_level_data`
--

CREATE TABLE `demo_education_level_data` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `EDUCATION_LEVEL_ID` smallint(5) UNSIGNED NOT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `demo_education_level_data`
--

INSERT INTO `demo_education_level_data` (`ID`, `PATIENT_ID`, `EDUCATION_LEVEL_ID`, `VISIT_ID`) VALUES
(5821, 8621, 2, 8615),
(5822, 8622, 2, 8616),
(5823, 8623, 1, 8617),
(5824, 8624, 2, 8618),
(5825, 8625, 1, 8619);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `demo_ethnicity_data`
--

CREATE TABLE `demo_ethnicity_data` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `ETHNICITY_ID` smallint(5) UNSIGNED NOT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `demo_ethnicity_data`
--

INSERT INTO `demo_ethnicity_data` (`ID`, `PATIENT_ID`, `ETHNICITY_ID`, `VISIT_ID`) VALUES
(7011, 8621, 6, 8615),
(7012, 8622, 6, 8616),
(7013, 8623, 6, 8617),
(7014, 8624, 6, 8618),
(7015, 8625, 6, 8619);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `demo_occupation_data`
--

CREATE TABLE `demo_occupation_data` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `LOSS_OF_WORK_DUE_TO_PSS_ID` tinyint(4) UNSIGNED NOT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `demo_occupation_data`
--

INSERT INTO `demo_occupation_data` (`ID`, `PATIENT_ID`, `LOSS_OF_WORK_DUE_TO_PSS_ID`, `VISIT_ID`) VALUES
(5821, 8621, 2, 8615),
(5822, 8622, 2, 8616),
(5823, 8623, 2, 8617),
(5824, 8624, 2, 8618),
(5825, 8625, 2, 8619);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `demo_pregnancy_data`
--

CREATE TABLE `demo_pregnancy_data` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `CONCEPTION_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `OUTCOME_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `OUTCOME_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `SS_CONCORDANT_ID` tinyint(3) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL,
  `STMT_ID` tinyint(3) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `demo_pregnancy_data`
--

INSERT INTO `demo_pregnancy_data` (`ID`, `PATIENT_ID`, `CONCEPTION_DATE_ID`, `OUTCOME_DATE_ID`, `OUTCOME_ID`, `SS_CONCORDANT_ID`, `VISIT_ID`, `STMT_ID`) VALUES
(6976, 8621, 859797, NULL, NULL, NULL, 8615, 1),
(6977, 8622, 859953, NULL, NULL, NULL, 8616, 2),
(6978, 8623, 860086, NULL, NULL, NULL, 8617, 2),
(6979, 8624, 860214, NULL, NULL, NULL, 8618, 2),
(6980, 8625, 860344, NULL, NULL, NULL, 8619, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `demo_sex_data`
--

CREATE TABLE `demo_sex_data` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `SEX_ID` smallint(5) UNSIGNED NOT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `demo_sex_data`
--

INSERT INTO `demo_sex_data` (`ID`, `PATIENT_ID`, `SEX_ID`, `VISIT_ID`) VALUES
(8588, 8621, 2, 8615),
(8589, 8622, 2, 8616),
(8590, 8623, 2, 8617),
(8591, 8624, 2, 8618),
(8592, 8625, 2, 8619);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `demo_weight_data`
--

CREATE TABLE `demo_weight_data` (
  `ID` int(11) UNSIGNED NOT NULL,
  `PATIENT_ID` int(11) UNSIGNED NOT NULL,
  `BMI_CLASS_ID` smallint(5) UNSIGNED NOT NULL,
  `VISIT_ID` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `demo_weight_data`
--

INSERT INTO `demo_weight_data` (`ID`, `PATIENT_ID`, `BMI_CLASS_ID`, `VISIT_ID`) VALUES
(5067, 8621, 1, 8615),
(5068, 8622, 4, 8616),
(5069, 8624, 2, 8618),
(5070, 8625, 2, 8619);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `dt_amount`
--

CREATE TABLE `dt_amount` (
  `ID` int(10) UNSIGNED NOT NULL,
  `VALUE` float NOT NULL,
  `UNIT_ID` smallint(5) UNSIGNED NOT NULL,
  `OP_ID` tinyint(4) NOT NULL DEFAULT '2',
  `VALUE2` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `dt_amount`
--

INSERT INTO `dt_amount` (`ID`, `VALUE`, `UNIT_ID`, `OP_ID`, `VALUE2`) VALUES
(227538, 70, 6, 2, NULL),
(227539, 0.79, 8, 2, NULL),
(227540, 11.3, 4, 2, NULL),
(227541, 3180, 1, 2, NULL),
(227542, 1250, 1, 2, NULL),
(227543, 1450, 1, 2, NULL),
(227544, 365, 1, 2, NULL),
(227545, 59.3, 8, 2, NULL),
(227546, 15.9, 8, 2, NULL),
(227547, 951.25, 3, 2, NULL),
(227548, 486.25, 3, 2, NULL),
(227549, 452.5, 3, 2, NULL),
(227550, 201.25, 3, 2, NULL),
(227551, 0.01, 8, 2, NULL),
(227552, 4, 4, 2, NULL),
(227553, 21.2, 5, 2, NULL),
(227554, 2.62, 5, 2, NULL),
(227555, 1.37, 5, 2, NULL),
(227556, 1025, 19, 2, NULL),
(227557, 5.5, 14, 2, NULL),
(227558, 10.8, 4, 2, NULL),
(227559, 3310, 1, 2, NULL),
(227560, 1270, 1, 2, NULL),
(227561, 1700, 1, 2, NULL),
(227562, 358, 1, 2, NULL),
(227563, 0.01, 8, 2, NULL),
(227564, 4, 4, 2, NULL),
(227565, 17, 5, 2, NULL),
(227566, 1.93, 5, 2, NULL),
(227567, 1.61, 5, 2, NULL),
(227568, 0.72, 8, 2, NULL),
(227569, 89.2, 8, 2, NULL),
(227570, 20.8, 8, 2, NULL),
(227571, 1026, 19, 2, NULL),
(227572, 5.5, 14, 2, NULL),
(227573, 0.00625, 19, 2, NULL),
(227574, 4.5, 10, 2, NULL),
(227575, 1.835, 19, 2, NULL),
(227576, 4, 19, 2, NULL),
(227577, 10, 22, 2, 20),
(227578, 10, 22, 2, 20),
(227579, 33, 6, 2, NULL),
(227580, 0.57, 8, 2, NULL),
(227581, 13.3, 4, 2, NULL),
(227582, 6770, 1, 2, NULL),
(227583, 2010, 1, 2, NULL),
(227584, 3940, 1, 2, NULL),
(227585, 329, 1, 2, NULL),
(227586, 144, 8, 2, NULL),
(227587, 25.1, 8, 2, NULL),
(227588, 0.21, 8, 2, NULL),
(227589, 4.1, 4, 2, NULL),
(227590, 14, 5, 2, NULL),
(227591, 2.38, 5, 2, NULL),
(227592, 1.71, 5, 2, NULL),
(227593, 2.15, 16, 2, NULL),
(227594, 1010, 19, 2, NULL),
(227595, 6.5, 14, 2, NULL),
(227596, 13.4, 4, 2, NULL),
(227597, 5330, 1, 2, NULL),
(227598, 2940, 1, 2, NULL),
(227599, 1890, 1, 2, NULL),
(227600, 283, 1, 2, NULL),
(227601, 0.2, 8, 2, NULL),
(227602, 4.5, 4, 2, NULL),
(227603, 14.4, 5, 2, NULL),
(227604, 1.62, 5, 2, NULL),
(227605, 0.28, 5, 2, NULL),
(227606, 0.56, 8, 2, NULL),
(227607, 139, 8, 2, NULL),
(227608, 22.7, 8, 2, NULL),
(227609, 1010, 19, 2, NULL),
(227610, 6, 14, 2, NULL),
(227611, 0.00625, 19, 2, NULL),
(227612, 20, 21, 2, NULL),
(227613, 27, 6, 2, NULL),
(227614, 0.88, 8, 2, NULL),
(227615, 8.5, 4, 2, NULL),
(227616, 11750, 1, 2, NULL),
(227617, 1763, 1, 2, NULL),
(227618, 8225, 1, 2, NULL),
(227619, 240, 1, 2, NULL),
(227620, 109, 8, 2, NULL),
(227621, 20, 8, 2, NULL),
(227622, 0.01, 8, 2, NULL),
(227623, 3.7, 4, 2, NULL),
(227624, 11.9, 5, 2, NULL),
(227625, 1.6, 5, 2, NULL),
(227626, 2.73, 5, 2, NULL),
(227627, 1013, 19, 2, NULL),
(227628, 5, 14, 2, NULL),
(227629, 9.2, 4, 2, NULL),
(227630, 11130, 1, 2, NULL),
(227631, 7123, 1, 2, NULL),
(227632, 2783, 1, 2, NULL),
(227633, 251, 1, 2, NULL),
(227634, 0.2, 8, 2, NULL),
(227635, 4, 4, 2, NULL),
(227636, 27.74, 5, 2, NULL),
(227637, 1.72, 5, 2, NULL),
(227638, 2.2, 5, 2, NULL),
(227639, 0.8, 8, 2, NULL),
(227640, 121, 8, 2, NULL),
(227641, 29, 8, 2, NULL),
(227642, 1020, 19, 2, NULL),
(227643, 5.5, 14, 2, NULL),
(227644, 0.00625, 19, 2, NULL),
(227645, 2.3, 10, 2, NULL),
(227646, 0.8, 19, 2, NULL),
(227647, 3, 19, 2, NULL),
(227648, 100, 21, 2, NULL),
(227649, 10, 6, 2, NULL),
(227650, 0.72, 8, 2, NULL),
(227651, 14.1, 4, 2, NULL),
(227652, 7030, 1, 2, NULL),
(227653, 1880, 1, 2, NULL),
(227654, 4680, 1, 2, NULL),
(227655, 233, 1, 2, NULL),
(227656, 100, 8, 2, NULL),
(227657, 17, 8, 2, NULL),
(227658, 0.2, 8, 2, NULL),
(227659, 4.3, 4, 2, NULL),
(227660, 1.5, 16, 2, NULL),
(227661, 1015, 19, 2, NULL),
(227662, 5.5, 14, 2, NULL),
(227663, 13.2, 4, 2, NULL),
(227664, 6650, 1, 2, NULL),
(227665, 4400, 1, 2, NULL),
(227666, 1360, 1, 2, NULL),
(227667, 224, 1, 2, NULL),
(227668, 0.2, 8, 2, NULL),
(227669, 4, 4, 2, NULL),
(227670, 0.7, 8, 2, NULL),
(227671, 99, 8, 2, NULL),
(227672, 16, 8, 2, NULL),
(227673, 1010, 19, 2, NULL),
(227674, 6.5, 14, 2, NULL),
(227675, 0.00625, 19, 2, NULL),
(227676, 2.6, 19, 2, NULL),
(227677, 4, 19, 2, NULL),
(227678, 18, 6, 2, NULL),
(227679, 0.81, 8, 2, NULL),
(227680, 11.9, 4, 2, NULL),
(227681, 3510, 1, 2, NULL),
(227682, 1410, 1, 2, NULL),
(227683, 1600, 1, 2, NULL),
(227684, 261, 1, 2, NULL),
(227685, 104, 8, 2, NULL),
(227686, 16, 8, 2, NULL),
(227687, 0.02, 8, 2, NULL),
(227688, 4, 4, 2, NULL),
(227689, 6.45, 5, 2, NULL),
(227690, 1.15, 5, 2, NULL),
(227691, 1.17, 5, 2, NULL),
(227692, 1020, 19, 2, NULL),
(227693, 5.5, 14, 2, NULL),
(227694, 12.7, 4, 2, NULL),
(227695, 5030, 1, 2, NULL),
(227696, 2710, 1, 2, NULL),
(227697, 1770, 1, 2, NULL),
(227698, 270, 1, 2, NULL),
(227699, 0.018, 8, 2, NULL),
(227700, 4, 4, 2, NULL),
(227701, 6.63, 5, 2, NULL),
(227702, 1.55, 5, 2, NULL),
(227703, 1.18, 5, 2, NULL),
(227704, 0.86, 8, 2, NULL),
(227705, 111, 8, 2, NULL),
(227706, 16.1, 8, 2, NULL),
(227707, 1051.38, 3, 2, NULL),
(227708, 684.99, 3, 2, NULL),
(227709, 336.3, 3, 2, NULL),
(227710, 469.05, 3, 2, NULL),
(227711, 1020, 19, 2, NULL),
(227712, 5.5, 14, 2, NULL),
(227713, 0.00625, 19, 2, NULL),
(227714, 4, 19, 2, NULL),
(227715, 150, 18, 2, NULL),
(227716, 500, 18, 2, NULL),
(227717, 230, 18, 2, NULL);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `dt_amount_range`
--

CREATE TABLE `dt_amount_range` (
  `ID` int(10) UNSIGNED NOT NULL,
  `VALUE1` float DEFAULT NULL,
  `VALUE2` float DEFAULT NULL,
  `UNIT_ID` smallint(5) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='[V1, V2] For >= V use [V, null] & For <= V use [null, V]';

--
-- Άδειασμα δεδομένων του πίνακα `dt_amount_range`
--

INSERT INTO `dt_amount_range` (`ID`, `VALUE1`, `VALUE2`, `UNIT_ID`) VALUES
(35777, 0, 15, 6),
(35778, 0, 15, 6),
(35779, 0, 15, 6),
(35780, 0, 15, 6),
(35781, 0, 15, 6),
(35782, 0, 15, 6),
(35783, 0, 15, 6),
(35784, 0, 15, 6),
(35785, 0, 15, 6),
(35786, 0, 15, 6);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `dt_date`
--

CREATE TABLE `dt_date` (
  `ID` int(10) UNSIGNED NOT NULL,
  `YEAR` year(4) NOT NULL,
  `MONTH` smallint(5) UNSIGNED DEFAULT NULL,
  `DAY` smallint(5) UNSIGNED DEFAULT NULL,
  `OP_ID` tinyint(4) NOT NULL COMMENT 'Indicates whether we are interested about the period specified or the one before or after it.',
  `YEAR2` year(4) DEFAULT NULL,
  `MONTH2` smallint(6) DEFAULT NULL,
  `DAY2` smallint(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `dt_date`
--

INSERT INTO `dt_date` (`ID`, `YEAR`, `MONTH`, `DAY`, `OP_ID`, `YEAR2`, `MONTH2`, `DAY2`) VALUES
(859688, 1972, NULL, NULL, 2, NULL, NULL, NULL),
(859689, 2009, NULL, NULL, 2, NULL, NULL, NULL),
(859690, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859691, 2019, 10, 23, 2, NULL, NULL, NULL),
(859692, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859693, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859694, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859695, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859696, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859697, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859698, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859699, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859700, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859701, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859702, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859703, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859704, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859705, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859706, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859707, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859708, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859709, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859710, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859711, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859712, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859713, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859714, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859715, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859716, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859717, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859718, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859719, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859720, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859721, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859722, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859723, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859724, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859725, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859726, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859727, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859728, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859729, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859730, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859731, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859732, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859733, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859734, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859735, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859736, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859737, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859738, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859739, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859740, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859741, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859742, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859743, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859744, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859745, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859746, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859747, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859748, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859749, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859750, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859751, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859752, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859753, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859754, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859755, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859756, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859757, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859758, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859759, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859760, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859761, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859762, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859763, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859764, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859765, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859766, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859767, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859768, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859769, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859770, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859771, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859772, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859773, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859774, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859775, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859776, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859777, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859778, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859779, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859780, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859781, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859782, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859783, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859784, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859785, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859786, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859787, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859788, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859789, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859790, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859791, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859792, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859793, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859794, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859795, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859796, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859797, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859798, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859799, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859800, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859801, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859802, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859803, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859804, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859805, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859806, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859807, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859808, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859809, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859810, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859811, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859812, 2014, NULL, NULL, 2, 2017, NULL, NULL),
(859813, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859814, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859815, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859816, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859817, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859818, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859819, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859820, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859821, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859822, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859823, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859824, 2017, NULL, NULL, 3, NULL, NULL, NULL),
(859825, 2014, NULL, NULL, 2, NULL, NULL, NULL),
(859826, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859827, 1957, NULL, NULL, 2, NULL, NULL, NULL),
(859828, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859829, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859830, 2019, 10, 23, 2, NULL, NULL, NULL),
(859831, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859832, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859833, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859834, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859835, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859836, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859837, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859838, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859839, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859840, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859841, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859842, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859843, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859844, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859845, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859846, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859847, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859848, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859849, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859850, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859851, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859852, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859853, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859854, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859855, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859856, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859857, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859858, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859859, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859860, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859861, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859862, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859863, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859864, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859865, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859866, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859867, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859868, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859869, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859870, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859871, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859872, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859873, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859874, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859875, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859876, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859877, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859878, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859879, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859880, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859881, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859882, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859883, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859884, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859885, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859886, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859887, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859888, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859889, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859890, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859891, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859892, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859893, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859894, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859895, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859896, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859897, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859898, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859899, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859900, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859901, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859902, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859903, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859904, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859905, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859906, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859907, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859908, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859909, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859910, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859911, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859912, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859913, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859914, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859915, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859916, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859917, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859918, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859919, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859920, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859921, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859922, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859923, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859924, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859925, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859926, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859927, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859928, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859929, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859930, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859931, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859932, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859933, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859934, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859935, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859936, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859937, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859938, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859939, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859940, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859941, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859942, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859943, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859944, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859945, 2007, NULL, NULL, 2, NULL, NULL, NULL),
(859946, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859947, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859948, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859949, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859950, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859951, 2017, NULL, NULL, 2, NULL, NULL, NULL),
(859952, 2017, NULL, NULL, 3, NULL, NULL, NULL),
(859953, 2007, NULL, NULL, 2, 2017, NULL, NULL),
(859954, 1965, NULL, NULL, 2, NULL, NULL, NULL),
(859955, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859956, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859957, 2019, 10, 23, 2, NULL, NULL, NULL),
(859958, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859959, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859960, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859961, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859962, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859963, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859964, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859965, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859966, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859967, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859968, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859969, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(859970, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859971, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859972, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859973, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859974, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859975, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859976, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859977, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859978, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859979, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859980, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859981, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859982, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859983, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859984, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(859985, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859986, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859987, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859988, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859989, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859990, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859991, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859992, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859993, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859994, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859995, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859996, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859997, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859998, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(859999, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860000, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860001, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860002, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860003, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860004, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860005, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860006, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860007, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860008, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860009, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860010, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860011, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860012, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860013, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860014, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860015, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860016, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860017, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860018, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860019, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860020, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860021, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860022, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860023, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860024, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860025, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860026, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860027, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860028, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860029, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860030, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860031, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860032, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860033, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860034, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860035, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860036, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860037, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860038, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860039, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860040, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860041, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860042, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860043, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860044, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860045, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860046, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860047, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860048, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860049, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860050, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860051, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860052, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860053, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860054, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860055, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860056, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860057, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860058, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860059, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860060, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860061, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860062, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860063, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860064, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860065, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860066, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860067, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860068, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860069, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860070, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860071, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860072, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860073, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860074, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860075, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860076, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860077, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860078, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860079, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860080, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860081, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860082, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860083, 2015, NULL, NULL, 3, NULL, NULL, NULL),
(860084, 2008, NULL, NULL, 2, NULL, NULL, NULL),
(860085, 2015, NULL, NULL, 2, NULL, NULL, NULL),
(860086, 2008, NULL, NULL, 2, 2015, NULL, NULL),
(860087, 1972, NULL, NULL, 2, NULL, NULL, NULL),
(860088, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860089, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860090, 2019, 10, 23, 2, NULL, NULL, NULL),
(860091, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860092, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860093, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860094, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860095, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860096, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860097, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860098, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860099, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860100, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860101, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860102, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860103, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860104, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860105, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860106, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860107, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860108, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860109, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860110, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860111, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860112, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860113, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860114, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860115, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860116, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860117, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860118, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860119, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860120, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860121, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860122, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860123, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860124, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860125, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860126, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860127, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860128, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860129, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860130, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860131, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860132, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860133, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860134, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860135, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860136, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860137, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860138, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860139, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860140, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860141, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860142, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860143, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860144, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860145, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860146, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860147, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860148, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860149, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860150, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860151, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860152, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860153, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860154, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860155, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860156, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860157, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860158, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860159, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860160, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860161, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860162, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860163, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860164, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860165, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860166, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860167, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860168, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860169, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860170, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860171, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860172, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860173, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860174, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860175, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860176, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860177, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860178, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860179, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860180, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860181, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860182, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860183, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860184, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860185, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860186, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860187, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860188, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860189, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860190, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860191, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860192, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860193, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860194, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860195, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860196, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860197, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860198, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860199, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860200, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860201, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860202, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860203, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860204, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860205, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860206, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860207, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860208, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860209, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860210, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860211, 2016, NULL, NULL, 3, NULL, NULL, NULL),
(860212, 2012, NULL, NULL, 2, NULL, NULL, NULL),
(860213, 2016, NULL, NULL, 2, NULL, NULL, NULL),
(860214, 2012, NULL, NULL, 2, 2016, NULL, NULL),
(860215, 1959, NULL, NULL, 2, NULL, NULL, NULL),
(860216, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860217, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860218, 2019, 10, 23, 2, NULL, NULL, NULL),
(860219, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860220, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860221, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860222, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860223, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860224, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860225, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860226, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860227, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860228, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860229, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860230, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860231, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860232, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860233, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860234, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860235, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860236, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860237, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860238, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860239, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860240, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860241, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860242, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860243, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860244, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860245, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860246, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860247, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860248, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860249, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860250, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860251, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860252, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860253, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860254, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860255, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860256, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860257, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860258, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860259, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860260, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860261, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860262, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860263, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860264, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860265, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860266, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860267, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860268, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860269, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860270, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860271, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860272, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860273, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860274, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860275, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860276, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860277, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860278, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860279, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860280, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860281, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860282, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860283, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860284, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860285, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860286, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860287, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860288, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860289, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860290, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860291, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860292, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860293, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860294, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860295, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860296, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860297, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860298, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860299, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860300, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860301, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860302, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860303, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860304, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860305, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860306, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860307, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860308, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860309, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860310, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860311, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860312, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860313, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860314, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860315, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860316, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860317, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860318, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860319, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860320, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860321, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860322, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860323, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860324, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860325, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860326, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860327, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860328, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860329, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860330, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860331, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860332, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860333, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860334, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860335, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860336, 2001, NULL, NULL, 2, 2018, NULL, NULL),
(860337, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860338, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860339, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860340, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860341, 2018, NULL, NULL, 3, NULL, NULL, NULL),
(860342, 2001, NULL, NULL, 2, NULL, NULL, NULL),
(860343, 2018, NULL, NULL, 2, NULL, NULL, NULL),
(860344, 2001, NULL, NULL, 2, 2018, NULL, NULL);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `dt_int_range`
--

CREATE TABLE `dt_int_range` (
  `ID` int(10) UNSIGNED NOT NULL,
  `INT1` int(11) DEFAULT NULL,
  `INT2` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='[INT1, INT2] For >= N use [N, null] & For <= N use [null, N]';

--
-- Άδειασμα δεδομένων του πίνακα `dt_int_range`
--

INSERT INTO `dt_int_range` (`ID`, `INT1`, `INT2`) VALUES
(11819, 0, 0),
(11820, 0, 0),
(11821, 0, 0),
(11822, 0, 0),
(11823, 0, 0),
(11824, 0, 0),
(11825, 0, 0),
(11826, 0, 0),
(11827, 0, 0),
(11828, 0, 0);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `dt_period_of_time`
--

CREATE TABLE `dt_period_of_time` (
  `ID` int(10) UNSIGNED NOT NULL,
  `START_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `END_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `EXACT_ID` tinyint(4) UNSIGNED NOT NULL COMMENT 'Indicates whether the period specified in the real one or a broader period of time.',
  `BEFORE_PERIOD_ID` int(10) UNSIGNED DEFAULT NULL COMMENT 'For periods of time ordering, if needed'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `dt_period_of_time`
--

INSERT INTO `dt_period_of_time` (`ID`, `START_DATE_ID`, `END_DATE_ID`, `EXACT_ID`, `BEFORE_PERIOD_ID`) VALUES
(90248, 859786, 859787, 1, NULL),
(90249, 859788, 859789, 1, NULL),
(90250, 859790, NULL, 1, NULL),
(90251, 859791, 859792, 1, NULL),
(90252, 859793, 859794, 1, NULL),
(90253, 859796, NULL, 1, NULL),
(90254, 859813, NULL, 1, NULL),
(90255, 859814, 859815, 1, NULL),
(90256, 859816, 859817, 1, NULL),
(90257, 859818, 859819, 1, NULL),
(90258, 859820, NULL, 1, NULL),
(90259, 859821, NULL, 1, NULL),
(90260, 859822, NULL, 1, NULL),
(90261, 859823, NULL, 1, NULL),
(90262, 859825, 859826, 1, NULL),
(90263, 859911, 859912, 1, NULL),
(90264, 859913, 859914, 1, NULL),
(90265, 859915, 859916, 1, NULL),
(90266, 859935, 859936, 1, NULL),
(90267, 859937, NULL, 1, NULL),
(90268, 859938, NULL, 1, NULL),
(90269, 859939, 859940, 1, NULL),
(90270, 859941, 859942, 1, NULL),
(90271, 859943, 859944, 1, NULL),
(90272, 859945, 859946, 1, NULL),
(90273, 859948, NULL, 1, NULL),
(90274, 859949, NULL, 1, NULL),
(90275, 859950, NULL, 1, NULL),
(90276, 859951, NULL, 1, NULL),
(90277, 860042, 860043, 1, NULL),
(90278, 860044, 860045, 1, NULL),
(90279, 860046, NULL, 1, NULL),
(90280, 860047, 860048, 1, NULL),
(90281, 860069, NULL, 1, NULL),
(90282, 860070, 860071, 1, NULL),
(90283, 860072, 860073, 1, NULL),
(90284, 860074, 860075, 1, NULL),
(90285, 860076, 860077, 1, NULL),
(90286, 860079, NULL, 1, NULL),
(90287, 860080, NULL, 1, NULL),
(90288, 860081, NULL, 1, NULL),
(90289, 860082, NULL, 1, NULL),
(90290, 860084, 860085, 1, NULL),
(90291, 860176, 860177, 1, NULL),
(90292, 860178, NULL, 1, NULL),
(90293, 860179, 860180, 1, NULL),
(90294, 860181, NULL, 1, NULL),
(90295, 860198, 860199, 1, NULL),
(90296, 860200, NULL, 1, NULL),
(90297, 860201, 860202, 1, NULL),
(90298, 860203, 860204, 1, NULL),
(90299, 860205, 860206, 1, NULL),
(90300, 860208, NULL, 1, NULL),
(90301, 860209, NULL, 1, NULL),
(90302, 860210, NULL, 1, NULL),
(90303, 860212, 860213, 1, NULL),
(90304, 860323, 860324, 1, NULL),
(90305, 860325, NULL, 1, NULL),
(90306, 860326, 860327, 1, NULL),
(90307, 860328, 860329, 1, NULL),
(90308, 860330, 860331, 1, NULL),
(90309, 860332, 860333, 1, NULL),
(90310, 860334, 860335, 1, NULL),
(90311, 860337, NULL, 1, NULL),
(90312, 860338, NULL, 1, NULL),
(90313, 860339, NULL, 1, NULL),
(90314, 860340, NULL, 1, NULL),
(90315, 860342, 860343, 1, NULL);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `exam_biopsy`
--

CREATE TABLE `exam_biopsy` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `BIOPSY_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `TEST_ID` smallint(5) UNSIGNED NOT NULL,
  `OUTCOME_AMOUNT_ID` int(10) UNSIGNED DEFAULT NULL,
  `NORMAL_RANGE_ID` int(10) UNSIGNED DEFAULT NULL,
  `ASSESSMENT_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `OUTCOME_CHECK_ID` tinyint(3) UNSIGNED DEFAULT NULL,
  `BIOPSY_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `exam_biopsy`
--

INSERT INTO `exam_biopsy` (`ID`, `PATIENT_ID`, `BIOPSY_ID`, `TEST_ID`, `OUTCOME_AMOUNT_ID`, `NORMAL_RANGE_ID`, `ASSESSMENT_ID`, `OUTCOME_CHECK_ID`, `BIOPSY_DATE_ID`, `VISIT_ID`) VALUES
(15076, 8621, 6, 507, 227575, NULL, NULL, NULL, 859784, 8615),
(15077, 8621, 6, 508, 227576, NULL, NULL, NULL, 859785, 8615),
(15078, 8623, 6, 507, 227646, NULL, NULL, NULL, 860039, 8617),
(15079, 8623, 6, 502, NULL, NULL, NULL, 2, 860040, 8617),
(15080, 8623, 6, 508, 227647, NULL, NULL, NULL, 860041, 8617),
(15081, 8624, 6, 507, 227676, NULL, NULL, NULL, 860173, 8618),
(15082, 8624, 6, 502, NULL, NULL, NULL, 1, 860174, 8618),
(15083, 8624, 6, 508, 227677, NULL, NULL, NULL, 860175, 8618),
(15084, 8625, 6, 508, 227714, NULL, NULL, NULL, 860305, 8619);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `exam_caci_condition`
--

CREATE TABLE `exam_caci_condition` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `CACI_ID` smallint(5) UNSIGNED NOT NULL,
  `VALUE_ID` tinyint(3) UNSIGNED NOT NULL,
  `QUESTIONNAIRE_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `exam_essdai_domain`
--

CREATE TABLE `exam_essdai_domain` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `DOMAIN_ID` smallint(5) UNSIGNED NOT NULL,
  `ACTIVITY_LEVEL_ID` smallint(11) UNSIGNED NOT NULL,
  `QUESTIONNAIRE_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `exam_essdai_domain`
--

INSERT INTO `exam_essdai_domain` (`ID`, `PATIENT_ID`, `DOMAIN_ID`, `ACTIVITY_LEVEL_ID`, `QUESTIONNAIRE_DATE_ID`, `VISIT_ID`) VALUES
(61565, 8621, 1, 1, 859748, 8615),
(61566, 8621, 4, 1, 859749, 8615),
(61567, 8621, 2, 1, 859750, 8615),
(61568, 8621, 7, 4, 859751, 8615),
(61569, 8621, 12, 1, 859752, 8615),
(61570, 8621, 6, 4, 859753, 8615),
(61571, 8621, 5, 4, 859754, 8615),
(61572, 8621, 9, 4, 859755, 8615),
(61573, 8621, 10, 4, 859756, 8615),
(61574, 8621, 8, 4, 859757, 8615),
(61575, 8621, 3, 4, 859774, 8615),
(61576, 8622, 1, 4, 859885, 8616),
(61577, 8622, 4, 1, 859886, 8616),
(61578, 8622, 2, 4, 859887, 8616),
(61579, 8622, 7, 4, 859888, 8616),
(61580, 8622, 11, 4, 859889, 8616),
(61581, 8622, 6, 4, 859890, 8616),
(61582, 8622, 9, 4, 859891, 8616),
(61583, 8622, 10, 4, 859892, 8616),
(61584, 8622, 8, 4, 859893, 8616),
(61585, 8622, 3, 4, 859904, 8616),
(61586, 8623, 1, 4, 860012, 8617),
(61587, 8623, 4, 4, 860013, 8617),
(61588, 8623, 2, 4, 860014, 8617),
(61589, 8623, 7, 4, 860015, 8617),
(61590, 8623, 11, 4, 860016, 8617),
(61591, 8623, 12, 1, 860017, 8617),
(61592, 8623, 6, 4, 860018, 8617),
(61593, 8623, 5, 4, 860019, 8617),
(61594, 8623, 9, 4, 860020, 8617),
(61595, 8623, 10, 4, 860021, 8617),
(61596, 8623, 8, 4, 860022, 8617),
(61597, 8623, 3, 1, 860033, 8617),
(61598, 8624, 1, 4, 860138, 8618),
(61599, 8624, 4, 2, 860139, 8618),
(61600, 8624, 2, 4, 860140, 8618),
(61601, 8624, 7, 4, 860141, 8618),
(61602, 8624, 11, 4, 860142, 8618),
(61603, 8624, 6, 4, 860143, 8618),
(61604, 8624, 5, 4, 860144, 8618),
(61605, 8624, 9, 4, 860145, 8618),
(61606, 8624, 10, 4, 860146, 8618),
(61607, 8624, 8, 4, 860147, 8618),
(61608, 8624, 3, 4, 860163, 8618),
(61609, 8625, 1, 4, 860275, 8619),
(61610, 8625, 4, 1, 860276, 8619),
(61611, 8625, 2, 4, 860277, 8619),
(61612, 8625, 7, 4, 860278, 8619),
(61613, 8625, 6, 4, 860279, 8619),
(61614, 8625, 5, 4, 860280, 8619),
(61615, 8625, 9, 4, 860281, 8619),
(61616, 8625, 10, 4, 860282, 8619),
(61617, 8625, 8, 4, 860283, 8619),
(61618, 8625, 3, 4, 860296, 8619);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `exam_lab_test`
--

CREATE TABLE `exam_lab_test` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `TEST_ID` smallint(5) UNSIGNED NOT NULL,
  `OUTCOME_AMOUNT_ID` int(10) UNSIGNED DEFAULT NULL,
  `OUTCOME_ASSESSMENT_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `NORMAL_RANGE_ID` int(10) UNSIGNED DEFAULT NULL,
  `OUTCOME_TERM_ID` int(10) UNSIGNED DEFAULT NULL COMMENT 'For those test with outcome a Boolean Value or another Term',
  `SAMPLE_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `exam_lab_test`
--

INSERT INTO `exam_lab_test` (`ID`, `PATIENT_ID`, `TEST_ID`, `OUTCOME_AMOUNT_ID`, `OUTCOME_ASSESSMENT_ID`, `NORMAL_RANGE_ID`, `OUTCOME_TERM_ID`, `SAMPLE_DATE_ID`, `VISIT_ID`) VALUES
(365346, 8621, 54, NULL, NULL, NULL, 2, 859692, 8615),
(365347, 8621, 46, NULL, 2, 35777, NULL, 859693, 8615),
(365348, 8621, 46, 227538, 4, 35778, NULL, 859694, 8615),
(365349, 8621, 27, NULL, NULL, NULL, 2, 859695, 8615),
(365350, 8621, 9, 227539, 1, NULL, NULL, 859696, 8615),
(365351, 8621, 210, NULL, NULL, NULL, 2, 859697, 8615),
(365352, 8621, 2, 227540, 1, NULL, NULL, 859698, 8615),
(365353, 8621, 3, 227541, 2, NULL, NULL, 859699, 8615),
(365354, 8621, 5, 227542, 2, NULL, NULL, 859700, 8615),
(365355, 8621, 4, 227543, 1, NULL, NULL, 859701, 8615),
(365356, 8621, 6, 227544, 1, NULL, NULL, 859702, 8615),
(365357, 8621, 38, NULL, NULL, NULL, 2, 859703, 8615),
(365358, 8621, 21, NULL, 1, NULL, NULL, 859704, 8615),
(365359, 8621, 18, NULL, 1, NULL, NULL, 859705, 8615),
(365360, 8621, 86, 227545, 2, NULL, NULL, 859706, 8615),
(365361, 8621, 87, 227546, 2, NULL, NULL, 859707, 8615),
(365362, 8621, 95, NULL, NULL, NULL, 2, 859708, 8615),
(365363, 8621, 99, NULL, NULL, NULL, 2, 859709, 8615),
(365364, 8621, 89, 227547, NULL, NULL, NULL, 859710, 8615),
(365365, 8621, 90, 227548, NULL, NULL, NULL, 859711, 8615),
(365366, 8621, 91, 227549, NULL, NULL, NULL, 859712, 8615),
(365367, 8621, 92, 227550, NULL, NULL, NULL, 859713, 8615),
(365368, 8621, 94, 227551, NULL, NULL, NULL, 859714, 8615),
(365369, 8621, 8, 227552, NULL, NULL, NULL, 859715, 8615),
(365370, 8621, 32, 227553, NULL, NULL, NULL, 859716, 8615),
(365371, 8621, 31, 227554, NULL, NULL, NULL, 859717, 8615),
(365372, 8621, 34, 227555, NULL, NULL, NULL, 859718, 8615),
(365373, 8621, 206, 227556, NULL, NULL, NULL, 859719, 8615),
(365374, 8621, 202, 227557, NULL, NULL, NULL, 859720, 8615),
(365375, 8621, 207, NULL, NULL, NULL, 1, 859721, 8615),
(365376, 8621, 210, NULL, NULL, NULL, 2, 859722, 8615),
(365377, 8621, 2, 227558, NULL, NULL, NULL, 859723, 8615),
(365378, 8621, 3, 227559, NULL, NULL, NULL, 859724, 8615),
(365379, 8621, 4, 227560, NULL, NULL, NULL, 859725, 8615),
(365380, 8621, 5, 227561, NULL, NULL, NULL, 859726, 8615),
(365381, 8621, 6, 227562, NULL, NULL, NULL, 859727, 8615),
(365382, 8621, 94, 227563, NULL, NULL, NULL, 859728, 8615),
(365383, 8621, 8, 227564, NULL, NULL, NULL, 859729, 8615),
(365384, 8621, 32, 227565, NULL, NULL, NULL, 859730, 8615),
(365385, 8621, 31, 227566, NULL, NULL, NULL, 859731, 8615),
(365386, 8621, 34, 227567, NULL, NULL, NULL, 859732, 8615),
(365387, 8621, 9, 227568, NULL, NULL, NULL, 859733, 8615),
(365388, 8621, 86, 227569, NULL, NULL, NULL, 859734, 8615),
(365389, 8621, 87, 227570, NULL, NULL, NULL, 859735, 8615),
(365390, 8621, 206, 227571, NULL, NULL, NULL, 859736, 8615),
(365391, 8621, 202, 227572, NULL, NULL, NULL, 859737, 8615),
(365392, 8621, 207, NULL, NULL, NULL, 2, 859738, 8615),
(365393, 8621, 48, NULL, NULL, NULL, 1, 859739, 8615),
(365394, 8621, 50, NULL, NULL, NULL, 1, 859740, 8615),
(365395, 8621, 401, NULL, 3, NULL, NULL, 859741, 8615),
(365396, 8621, 403, NULL, 1, NULL, NULL, 859742, 8615),
(365397, 8621, 49, 227573, NULL, NULL, NULL, 859743, 8615),
(365398, 8621, 51, NULL, NULL, NULL, 1, 859744, 8615),
(365399, 8621, 52, NULL, NULL, NULL, 1, 859745, 8615),
(365400, 8621, 302, 227574, 1, NULL, NULL, 859746, 8615),
(365401, 8621, 402, NULL, 1, NULL, NULL, 859747, 8615),
(365402, 8622, 54, NULL, NULL, NULL, 1, 859831, 8616),
(365403, 8622, 46, NULL, 2, 35779, NULL, 859832, 8616),
(365404, 8622, 46, 227579, 4, 35780, NULL, 859833, 8616),
(365405, 8622, 27, NULL, NULL, NULL, 2, 859834, 8616),
(365406, 8622, 9, 227580, 1, NULL, NULL, 859835, 8616),
(365407, 8622, 210, NULL, NULL, NULL, 2, 859836, 8616),
(365408, 8622, 2, 227581, 1, NULL, NULL, 859837, 8616),
(365409, 8622, 3, 227582, 1, NULL, NULL, 859838, 8616),
(365410, 8622, 5, 227583, 1, NULL, NULL, 859839, 8616),
(365411, 8622, 4, 227584, 1, NULL, NULL, 859840, 8616),
(365412, 8622, 6, 227585, 1, NULL, NULL, 859841, 8616),
(365413, 8622, 38, NULL, NULL, NULL, 2, 859842, 8616),
(365414, 8622, 21, NULL, 1, NULL, NULL, 859843, 8616),
(365415, 8622, 18, NULL, 1, NULL, NULL, 859844, 8616),
(365416, 8622, 86, 227586, 1, NULL, NULL, 859845, 8616),
(365417, 8622, 87, 227587, 1, NULL, NULL, 859846, 8616),
(365418, 8622, 95, NULL, NULL, NULL, 2, 859847, 8616),
(365419, 8622, 99, NULL, NULL, NULL, 2, 859848, 8616),
(365420, 8622, 94, 227588, NULL, NULL, NULL, 859849, 8616),
(365421, 8622, 8, 227589, NULL, NULL, NULL, 859850, 8616),
(365422, 8622, 32, 227590, NULL, NULL, NULL, 859851, 8616),
(365423, 8622, 31, 227591, NULL, NULL, NULL, 859852, 8616),
(365424, 8622, 34, 227592, NULL, NULL, NULL, 859853, 8616),
(365425, 8622, 102, 227593, NULL, NULL, NULL, 859854, 8616),
(365426, 8622, 212, NULL, NULL, NULL, 2, 859855, 8616),
(365427, 8622, 206, 227594, NULL, NULL, NULL, 859856, 8616),
(365428, 8622, 202, 227595, NULL, NULL, NULL, 859857, 8616),
(365429, 8622, 207, NULL, NULL, NULL, 2, 859858, 8616),
(365430, 8622, 210, NULL, NULL, NULL, 2, 859859, 8616),
(365431, 8622, 2, 227596, NULL, NULL, NULL, 859860, 8616),
(365432, 8622, 3, 227597, NULL, NULL, NULL, 859861, 8616),
(365433, 8622, 4, 227598, NULL, NULL, NULL, 859862, 8616),
(365434, 8622, 5, 227599, NULL, NULL, NULL, 859863, 8616),
(365435, 8622, 6, 227600, NULL, NULL, NULL, 859864, 8616),
(365436, 8622, 94, 227601, NULL, NULL, NULL, 859865, 8616),
(365437, 8622, 8, 227602, NULL, NULL, NULL, 859866, 8616),
(365438, 8622, 32, 227603, NULL, NULL, NULL, 859867, 8616),
(365439, 8622, 31, 227604, NULL, NULL, NULL, 859868, 8616),
(365440, 8622, 34, 227605, NULL, NULL, NULL, 859869, 8616),
(365441, 8622, 9, 227606, NULL, NULL, NULL, 859870, 8616),
(365442, 8622, 86, 227607, NULL, NULL, NULL, 859871, 8616),
(365443, 8622, 87, 227608, NULL, NULL, NULL, 859872, 8616),
(365444, 8622, 206, 227609, NULL, NULL, NULL, 859873, 8616),
(365445, 8622, 202, 227610, NULL, NULL, NULL, 859874, 8616),
(365446, 8622, 207, NULL, NULL, NULL, 2, 859875, 8616),
(365447, 8622, 48, NULL, NULL, NULL, 1, 859876, 8616),
(365448, 8622, 50, NULL, NULL, NULL, 2, 859877, 8616),
(365449, 8622, 401, NULL, 3, NULL, NULL, 859878, 8616),
(365450, 8622, 403, NULL, 2, NULL, NULL, 859879, 8616),
(365451, 8622, 49, 227611, NULL, NULL, NULL, 859880, 8616),
(365452, 8622, 51, NULL, NULL, NULL, 1, 859881, 8616),
(365453, 8622, 52, NULL, NULL, NULL, 2, 859882, 8616),
(365454, 8622, 302, NULL, 1, NULL, NULL, 859883, 8616),
(365455, 8622, 402, NULL, 4, NULL, NULL, 859884, 8616),
(365456, 8623, 54, NULL, NULL, NULL, 1, 859958, 8617),
(365457, 8623, 46, NULL, 2, 35781, NULL, 859959, 8617),
(365458, 8623, 46, 227613, 4, 35782, NULL, 859960, 8617),
(365459, 8623, 27, NULL, NULL, NULL, 2, 859961, 8617),
(365460, 8623, 9, 227614, 1, NULL, NULL, 859962, 8617),
(365461, 8623, 210, NULL, NULL, NULL, 2, 859963, 8617),
(365462, 8623, 2, 227615, 1, NULL, NULL, 859964, 8617),
(365463, 8623, 3, 227616, 1, NULL, NULL, 859965, 8617),
(365464, 8623, 5, 227617, 1, NULL, NULL, 859966, 8617),
(365465, 8623, 4, 227618, 1, NULL, NULL, 859967, 8617),
(365466, 8623, 6, 227619, 1, NULL, NULL, 859968, 8617),
(365467, 8623, 38, NULL, NULL, NULL, 2, 859969, 8617),
(365468, 8623, 16, NULL, 2, NULL, NULL, 859970, 8617),
(365469, 8623, 15, NULL, 2, NULL, NULL, 859971, 8617),
(365470, 8623, 17, NULL, 2, NULL, NULL, 859972, 8617),
(365471, 8623, 14, NULL, 2, NULL, NULL, 859973, 8617),
(365472, 8623, 21, NULL, 1, NULL, NULL, 859974, 8617),
(365473, 8623, 18, NULL, 1, NULL, NULL, 859975, 8617),
(365474, 8623, 86, 227620, 1, NULL, NULL, 859976, 8617),
(365475, 8623, 87, 227621, 1, NULL, NULL, 859977, 8617),
(365476, 8623, 95, NULL, NULL, NULL, 2, 859978, 8617),
(365477, 8623, 99, NULL, NULL, NULL, 2, 859979, 8617),
(365478, 8623, 94, 227622, NULL, NULL, NULL, 859980, 8617),
(365479, 8623, 8, 227623, NULL, NULL, NULL, 859981, 8617),
(365480, 8623, 32, 227624, NULL, NULL, NULL, 859982, 8617),
(365481, 8623, 31, 227625, NULL, NULL, NULL, 859983, 8617),
(365482, 8623, 34, 227626, NULL, NULL, NULL, 859984, 8617),
(365483, 8623, 206, 227627, NULL, NULL, NULL, 859985, 8617),
(365484, 8623, 202, 227628, NULL, NULL, NULL, 859986, 8617),
(365485, 8623, 207, NULL, NULL, NULL, 2, 859987, 8617),
(365486, 8623, 210, NULL, NULL, NULL, 2, 859988, 8617),
(365487, 8623, 2, 227629, NULL, NULL, NULL, 859989, 8617),
(365488, 8623, 3, 227630, NULL, NULL, NULL, 859990, 8617),
(365489, 8623, 4, 227631, NULL, NULL, NULL, 859991, 8617),
(365490, 8623, 5, 227632, NULL, NULL, NULL, 859992, 8617),
(365491, 8623, 6, 227633, NULL, NULL, NULL, 859993, 8617),
(365492, 8623, 94, 227634, NULL, NULL, NULL, 859994, 8617),
(365493, 8623, 8, 227635, NULL, NULL, NULL, 859995, 8617),
(365494, 8623, 32, 227636, NULL, NULL, NULL, 859996, 8617),
(365495, 8623, 31, 227637, NULL, NULL, NULL, 859997, 8617),
(365496, 8623, 34, 227638, NULL, NULL, NULL, 859998, 8617),
(365497, 8623, 9, 227639, NULL, NULL, NULL, 859999, 8617),
(365498, 8623, 86, 227640, NULL, NULL, NULL, 860000, 8617),
(365499, 8623, 87, 227641, NULL, NULL, NULL, 860001, 8617),
(365500, 8623, 206, 227642, NULL, NULL, NULL, 860002, 8617),
(365501, 8623, 202, 227643, NULL, NULL, NULL, 860003, 8617),
(365502, 8623, 207, NULL, NULL, NULL, 2, 860004, 8617),
(365503, 8623, 48, NULL, NULL, NULL, 1, 860005, 8617),
(365504, 8623, 50, NULL, NULL, NULL, 1, 860006, 8617),
(365505, 8623, 401, NULL, 1, NULL, NULL, 860007, 8617),
(365506, 8623, 49, 227644, NULL, NULL, NULL, 860008, 8617),
(365507, 8623, 51, NULL, NULL, NULL, 1, 860009, 8617),
(365508, 8623, 302, 227645, 1, NULL, NULL, 860010, 8617),
(365509, 8623, 402, NULL, 1, NULL, NULL, 860011, 8617),
(365510, 8624, 54, NULL, NULL, NULL, 1, 860091, 8618),
(365511, 8624, 46, NULL, 2, 35783, NULL, 860092, 8618),
(365512, 8624, 46, 227649, 1, 35784, NULL, 860093, 8618),
(365513, 8624, 27, NULL, NULL, NULL, 2, 860094, 8618),
(365514, 8624, 9, 227650, 1, NULL, NULL, 860095, 8618),
(365515, 8624, 210, NULL, NULL, NULL, 2, 860096, 8618),
(365516, 8624, 2, 227651, 1, NULL, NULL, 860097, 8618),
(365517, 8624, 3, 227652, 1, NULL, NULL, 860098, 8618),
(365518, 8624, 5, 227653, 1, NULL, NULL, 860099, 8618),
(365519, 8624, 4, 227654, 1, NULL, NULL, 860100, 8618),
(365520, 8624, 6, 227655, 1, NULL, NULL, 860101, 8618),
(365521, 8624, 38, NULL, NULL, NULL, 2, 860102, 8618),
(365522, 8624, 21, NULL, 1, NULL, NULL, 860103, 8618),
(365523, 8624, 18, NULL, 1, NULL, NULL, 860104, 8618),
(365524, 8624, 86, 227656, 1, NULL, NULL, 860105, 8618),
(365525, 8624, 87, 227657, 1, NULL, NULL, 860106, 8618),
(365526, 8624, 95, NULL, NULL, NULL, 2, 860107, 8618),
(365527, 8624, 99, NULL, NULL, NULL, 2, 860108, 8618),
(365528, 8624, 94, 227658, NULL, NULL, NULL, 860109, 8618),
(365529, 8624, 8, 227659, NULL, NULL, NULL, 860110, 8618),
(365530, 8624, 102, 227660, NULL, NULL, NULL, 860111, 8618),
(365531, 8624, 206, 227661, NULL, NULL, NULL, 860112, 8618),
(365532, 8624, 202, 227662, NULL, NULL, NULL, 860113, 8618),
(365533, 8624, 207, NULL, NULL, NULL, 2, 860114, 8618),
(365534, 8624, 210, NULL, NULL, NULL, 2, 860115, 8618),
(365535, 8624, 2, 227663, NULL, NULL, NULL, 860116, 8618),
(365536, 8624, 3, 227664, NULL, NULL, NULL, 860117, 8618),
(365537, 8624, 4, 227665, NULL, NULL, NULL, 860118, 8618),
(365538, 8624, 5, 227666, NULL, NULL, NULL, 860119, 8618),
(365539, 8624, 6, 227667, NULL, NULL, NULL, 860120, 8618),
(365540, 8624, 94, 227668, NULL, NULL, NULL, 860121, 8618),
(365541, 8624, 8, 227669, NULL, NULL, NULL, 860122, 8618),
(365542, 8624, 9, 227670, NULL, NULL, NULL, 860123, 8618),
(365543, 8624, 86, 227671, NULL, NULL, NULL, 860124, 8618),
(365544, 8624, 87, 227672, NULL, NULL, NULL, 860125, 8618),
(365545, 8624, 206, 227673, NULL, NULL, NULL, 860126, 8618),
(365546, 8624, 202, 227674, NULL, NULL, NULL, 860127, 8618),
(365547, 8624, 207, NULL, NULL, NULL, 2, 860128, 8618),
(365548, 8624, 48, NULL, NULL, NULL, 1, 860129, 8618),
(365549, 8624, 50, NULL, NULL, NULL, 1, 860130, 8618),
(365550, 8624, 401, NULL, 3, NULL, NULL, 860131, 8618),
(365551, 8624, 403, NULL, 1, NULL, NULL, 860132, 8618),
(365552, 8624, 49, 227675, NULL, NULL, NULL, 860133, 8618),
(365553, 8624, 51, NULL, NULL, NULL, 1, 860134, 8618),
(365554, 8624, 52, NULL, NULL, NULL, 2, 860135, 8618),
(365555, 8624, 302, NULL, 1, NULL, NULL, 860136, 8618),
(365556, 8624, 402, NULL, 4, NULL, NULL, 860137, 8618),
(365557, 8625, 54, NULL, NULL, NULL, 2, 860219, 8619),
(365558, 8625, 46, NULL, 2, 35785, NULL, 860220, 8619),
(365559, 8625, 46, 227678, 4, 35786, NULL, 860221, 8619),
(365560, 8625, 27, NULL, NULL, NULL, 2, 860222, 8619),
(365561, 8625, 9, 227679, 1, NULL, NULL, 860223, 8619),
(365562, 8625, 210, NULL, NULL, NULL, 2, 860224, 8619),
(365563, 8625, 2, 227680, 1, NULL, NULL, 860225, 8619),
(365564, 8625, 3, 227681, 2, NULL, NULL, 860226, 8619),
(365565, 8625, 5, 227682, 1, NULL, NULL, 860227, 8619),
(365566, 8625, 4, 227683, 1, NULL, NULL, 860228, 8619),
(365567, 8625, 6, 227684, 1, NULL, NULL, 860229, 8619),
(365568, 8625, 38, NULL, NULL, NULL, 2, 860230, 8619),
(365569, 8625, 21, NULL, 1, NULL, NULL, 860231, 8619),
(365570, 8625, 18, NULL, 1, NULL, NULL, 860232, 8619),
(365571, 8625, 86, 227685, 1, NULL, NULL, 860233, 8619),
(365572, 8625, 87, 227686, 1, NULL, NULL, 860234, 8619),
(365573, 8625, 95, NULL, NULL, NULL, 2, 860235, 8619),
(365574, 8625, 99, NULL, NULL, NULL, 2, 860236, 8619),
(365575, 8625, 94, 227687, NULL, NULL, NULL, 860237, 8619),
(365576, 8625, 8, 227688, NULL, NULL, NULL, 860238, 8619),
(365577, 8625, 32, 227689, NULL, NULL, NULL, 860239, 8619),
(365578, 8625, 31, 227690, NULL, NULL, NULL, 860240, 8619),
(365579, 8625, 34, 227691, NULL, NULL, NULL, 860241, 8619),
(365580, 8625, 206, 227692, NULL, NULL, NULL, 860242, 8619),
(365581, 8625, 202, 227693, NULL, NULL, NULL, 860243, 8619),
(365582, 8625, 207, NULL, NULL, NULL, 2, 860244, 8619),
(365583, 8625, 210, NULL, NULL, NULL, 2, 860245, 8619),
(365584, 8625, 2, 227694, NULL, NULL, NULL, 860246, 8619),
(365585, 8625, 3, 227695, NULL, NULL, NULL, 860247, 8619),
(365586, 8625, 4, 227696, NULL, NULL, NULL, 860248, 8619),
(365587, 8625, 5, 227697, NULL, NULL, NULL, 860249, 8619),
(365588, 8625, 6, 227698, NULL, NULL, NULL, 860250, 8619),
(365589, 8625, 94, 227699, NULL, NULL, NULL, 860251, 8619),
(365590, 8625, 8, 227700, NULL, NULL, NULL, 860252, 8619),
(365591, 8625, 32, 227701, NULL, NULL, NULL, 860253, 8619),
(365592, 8625, 31, 227702, NULL, NULL, NULL, 860254, 8619),
(365593, 8625, 34, 227703, NULL, NULL, NULL, 860255, 8619),
(365594, 8625, 9, 227704, NULL, NULL, NULL, 860256, 8619),
(365595, 8625, 86, 227705, NULL, NULL, NULL, 860257, 8619),
(365596, 8625, 87, 227706, NULL, NULL, NULL, 860258, 8619),
(365597, 8625, 89, 227707, NULL, NULL, NULL, 860259, 8619),
(365598, 8625, 90, 227708, NULL, NULL, NULL, 860260, 8619),
(365599, 8625, 91, 227709, NULL, NULL, NULL, 860261, 8619),
(365600, 8625, 92, 227710, NULL, NULL, NULL, 860262, 8619),
(365601, 8625, 212, NULL, NULL, NULL, 2, NULL, 8619),
(365602, 8625, 206, 227711, NULL, NULL, NULL, 860263, 8619),
(365603, 8625, 202, 227712, NULL, NULL, NULL, 860264, 8619),
(365604, 8625, 207, NULL, NULL, NULL, 2, 860265, 8619),
(365605, 8625, 48, NULL, NULL, NULL, 1, 860266, 8619),
(365606, 8625, 50, NULL, NULL, NULL, 1, 860267, 8619),
(365607, 8625, 401, NULL, 3, NULL, NULL, 860268, 8619),
(365608, 8625, 403, NULL, 1, NULL, NULL, 860269, 8619),
(365609, 8625, 49, 227713, NULL, NULL, NULL, 860270, 8619),
(365610, 8625, 51, NULL, NULL, NULL, 2, 860271, 8619),
(365611, 8625, 52, NULL, NULL, NULL, 2, 860272, 8619),
(365612, 8625, 302, NULL, 1, NULL, NULL, 860273, 8619),
(365613, 8625, 402, NULL, 1, NULL, NULL, 860274, 8619);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `exam_medical_imaging_test`
--

CREATE TABLE `exam_medical_imaging_test` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `TEST_ID` smallint(5) UNSIGNED NOT NULL,
  `ASSESSMENT_ID` smallint(5) UNSIGNED NOT NULL,
  `TEST_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `exam_questionnaire_score`
--

CREATE TABLE `exam_questionnaire_score` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `SCORE_ID` smallint(5) UNSIGNED NOT NULL,
  `VALUE` float DEFAULT NULL,
  `ASSESSMENT_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `NORMAL_RANGE_ID` int(10) UNSIGNED DEFAULT NULL,
  `OTHER_TERM_ID` int(10) UNSIGNED DEFAULT NULL COMMENT 'For those test with outcome: "Number and/or Term"',
  `QUESTIONNAIRE_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `exam_questionnaire_score`
--

INSERT INTO `exam_questionnaire_score` (`ID`, `PATIENT_ID`, `SCORE_ID`, `VALUE`, `ASSESSMENT_ID`, `NORMAL_RANGE_ID`, `OTHER_TERM_ID`, `QUESTIONNAIRE_DATE_ID`, `VISIT_ID`) VALUES
(73542, 8621, 1, 10, NULL, NULL, NULL, 859758, 8615),
(73543, 8621, 1, 7, NULL, NULL, NULL, 859759, 8615),
(73544, 8621, 2, 7.3333, NULL, NULL, NULL, 859760, 8615),
(73545, 8621, 13, 7, NULL, NULL, NULL, 859761, 8615),
(73546, 8621, 12, 2, NULL, NULL, NULL, 859762, 8615),
(73547, 8621, 16, 10, NULL, NULL, NULL, 859763, 8615),
(73548, 8621, 14, 5, NULL, NULL, NULL, 859764, 8615),
(73549, 8621, 15, 3, NULL, NULL, NULL, 859765, 8615),
(73550, 8621, 2, 7.3333, NULL, NULL, NULL, 859766, 8615),
(73551, 8621, 13, 8, NULL, NULL, NULL, 859767, 8615),
(73552, 8621, 12, 2, NULL, NULL, NULL, 859768, 8615),
(73553, 8621, 16, 10, NULL, NULL, NULL, 859769, 8615),
(73554, 8621, 31, 0.375, NULL, NULL, NULL, 859770, 8615),
(73555, 8621, 28, 2, NULL, NULL, NULL, 859771, 8615),
(73556, 8621, 6, 1, 4, 11819, NULL, 859772, 8615),
(73557, 8621, 6, 1, 4, 11820, NULL, 859773, 8615),
(73558, 8622, 1, 2, NULL, NULL, NULL, 859894, 8616),
(73559, 8622, 1, 0, NULL, NULL, NULL, 859895, 8616),
(73560, 8622, 2, 6.6667, NULL, NULL, NULL, 859896, 8616),
(73561, 8622, 13, 7, NULL, NULL, NULL, 859897, 8616),
(73562, 8622, 12, 10, NULL, NULL, NULL, 859898, 8616),
(73563, 8622, 16, 8, NULL, NULL, NULL, 859899, 8616),
(73564, 8622, 31, 22, NULL, NULL, NULL, 859900, 8616),
(73565, 8622, 28, 0, NULL, NULL, NULL, 859901, 8616),
(73566, 8622, 6, 2, 4, 11821, NULL, 859902, 8616),
(73567, 8622, 6, 2, 4, 11822, NULL, 859903, 8616),
(73568, 8623, 1, 3, NULL, NULL, NULL, 860023, 8617),
(73569, 8623, 1, 1, NULL, NULL, NULL, 860024, 8617),
(73570, 8623, 2, 2, NULL, NULL, NULL, 860025, 8617),
(73571, 8623, 13, 2, NULL, NULL, NULL, 860026, 8617),
(73572, 8623, 12, 2, NULL, NULL, NULL, 860027, 8617),
(73573, 8623, 16, 4, NULL, NULL, NULL, 860028, 8617),
(73574, 8623, 31, 6375, NULL, NULL, NULL, 860029, 8617),
(73575, 8623, 28, 4, NULL, NULL, NULL, 860030, 8617),
(73576, 8623, 6, 1, 4, 11823, NULL, 860031, 8617),
(73577, 8623, 6, 1, 4, 11824, NULL, 860032, 8617),
(73578, 8624, 1, 4, NULL, NULL, NULL, 860148, 8618),
(73579, 8624, 1, 0, NULL, NULL, NULL, 860149, 8618),
(73580, 8624, 2, 9, NULL, NULL, NULL, 860150, 8618),
(73581, 8624, 13, 8, NULL, NULL, NULL, 860151, 8618),
(73582, 8624, 12, 9, NULL, NULL, NULL, 860152, 8618),
(73583, 8624, 16, 9, NULL, NULL, NULL, 860153, 8618),
(73584, 8624, 14, 6, NULL, NULL, NULL, 860154, 8618),
(73585, 8624, 15, 6, NULL, NULL, NULL, 860155, 8618),
(73586, 8624, 2, 7.6667, NULL, NULL, NULL, 860156, 8618),
(73587, 8624, 13, 7, NULL, NULL, NULL, 860157, 8618),
(73588, 8624, 12, 8, NULL, NULL, NULL, 860158, 8618),
(73589, 8624, 16, 7, NULL, NULL, NULL, 860159, 8618),
(73590, 8624, 31, 2.35, NULL, NULL, NULL, 860160, 8618),
(73591, 8624, 6, 2, 4, 11825, NULL, 860161, 8618),
(73592, 8624, 6, 2, 4, 11826, NULL, 860162, 8618),
(73593, 8625, 1, 2, NULL, NULL, NULL, 860284, 8619),
(73594, 8625, 1, 0, NULL, NULL, NULL, 860285, 8619),
(73595, 8625, 2, 5.3333, NULL, NULL, NULL, 860286, 8619),
(73596, 8625, 13, 10, NULL, NULL, NULL, 860287, 8619),
(73597, 8625, 12, 10, NULL, NULL, NULL, 860288, 8619),
(73598, 8625, 16, 0, NULL, NULL, NULL, 860289, 8619),
(73599, 8625, 14, 5, NULL, NULL, NULL, 860290, 8619),
(73600, 8625, 15, 5, NULL, NULL, NULL, 860291, 8619),
(73601, 8625, 31, 0, NULL, NULL, NULL, 860292, 8619),
(73602, 8625, 28, 55, NULL, NULL, NULL, 860293, 8619),
(73603, 8625, 6, 2, 4, 11827, NULL, 860294, 8619),
(73604, 8625, 6, 3, 4, 11828, NULL, 860295, 8619);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `interv_chemotherapy`
--

CREATE TABLE `interv_chemotherapy` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `DUE_TO_PSS_ID` tinyint(4) UNSIGNED DEFAULT NULL,
  `PERIOD_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL,
  `STMT_ID` tinyint(3) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `interv_chemotherapy`
--

INSERT INTO `interv_chemotherapy` (`ID`, `PATIENT_ID`, `DUE_TO_PSS_ID`, `PERIOD_ID`, `VISIT_ID`, `STMT_ID`) VALUES
(11622, 8621, NULL, 90252, 8615, 1),
(11623, 8621, NULL, 90261, 8615, 2),
(11624, 8622, NULL, 90272, 8616, 2),
(11625, 8622, NULL, 90276, 8616, 2),
(11626, 8623, NULL, 90285, 8617, 2),
(11627, 8623, NULL, 90289, 8617, 2),
(11628, 8624, NULL, 90299, 8618, 2),
(11629, 8624, NULL, 90302, 8618, 2),
(11630, 8625, NULL, 90310, 8619, 2),
(11631, 8625, NULL, 90314, 8619, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `interv_medication`
--

CREATE TABLE `interv_medication` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `MEDICATION_ID` smallint(5) UNSIGNED NOT NULL,
  `DOSAGE_ID` int(10) UNSIGNED DEFAULT NULL COMMENT 'Dosage Amount/Frequency (specified in the unit)',
  `PERIOD_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL,
  `STMT_ID` tinyint(3) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `interv_medication`
--

INSERT INTO `interv_medication` (`ID`, `PATIENT_ID`, `MEDICATION_ID`, `DOSAGE_ID`, `PERIOD_ID`, `VISIT_ID`, `STMT_ID`) VALUES
(81264, 8621, 1, NULL, 90248, 8615, 1),
(81265, 8621, 8, NULL, 90249, 8615, 1),
(81266, 8621, 8, NULL, 90250, 8615, 1),
(81267, 8621, 5, 227577, 90251, 8615, 1),
(81268, 8621, 5, 227578, 90253, 8615, 1),
(81269, 8621, 1, NULL, 90254, 8615, 2),
(81270, 8621, 19, NULL, 90255, 8615, 2),
(81271, 8621, 37, NULL, 90256, 8615, 2),
(81272, 8621, 11, NULL, 90257, 8615, 2),
(81273, 8621, 19, NULL, 90258, 8615, 2),
(81274, 8621, 37, NULL, 90259, 8615, 2),
(81275, 8621, 11, NULL, 90260, 8615, 2),
(81276, 8622, 8, NULL, 90263, 8616, 1),
(81277, 8622, 6, 227612, 90264, 8616, 1),
(81278, 8622, 1, NULL, 90266, 8616, 2),
(81279, 8622, 1, NULL, 90267, 8616, 2),
(81280, 8622, 8, NULL, 90268, 8616, 2),
(81281, 8622, 19, NULL, 90269, 8616, 2),
(81282, 8622, 37, NULL, 90270, 8616, 2),
(81283, 8622, 11, NULL, 90271, 8616, 2),
(81284, 8622, 19, NULL, 90273, 8616, 2),
(81285, 8622, 37, NULL, 90274, 8616, 2),
(81286, 8622, 11, NULL, 90275, 8616, 2),
(81287, 8623, 1, NULL, 90277, 8617, 1),
(81288, 8623, 8, NULL, 90278, 8617, 1),
(81289, 8623, 8, NULL, 90279, 8617, 1),
(81290, 8623, 7, 227648, 90280, 8617, 1),
(81291, 8623, 1, NULL, 90281, 8617, 2),
(81292, 8623, 19, NULL, 90282, 8617, 2),
(81293, 8623, 37, NULL, 90283, 8617, 2),
(81294, 8623, 11, NULL, 90284, 8617, 2),
(81295, 8623, 19, NULL, 90286, 8617, 2),
(81296, 8623, 37, NULL, 90287, 8617, 2),
(81297, 8623, 11, NULL, 90288, 8617, 2),
(81298, 8624, 8, NULL, 90291, 8618, 1),
(81299, 8624, 8, NULL, 90292, 8618, 1),
(81300, 8624, 37, NULL, 90293, 8618, 1),
(81301, 8624, 37, NULL, 90294, 8618, 1),
(81302, 8624, 1, NULL, 90295, 8618, 2),
(81303, 8624, 1, NULL, 90296, 8618, 2),
(81304, 8624, 19, NULL, 90297, 8618, 2),
(81305, 8624, 11, NULL, 90298, 8618, 2),
(81306, 8624, 19, NULL, 90300, 8618, 2),
(81307, 8624, 11, NULL, 90301, 8618, 2),
(81308, 8625, 1, NULL, 90304, 8619, 2),
(81309, 8625, 1, NULL, 90305, 8619, 2),
(81310, 8625, 8, NULL, 90306, 8619, 2),
(81311, 8625, 19, NULL, 90307, 8619, 2),
(81312, 8625, 37, NULL, 90308, 8619, 2),
(81313, 8625, 11, NULL, 90309, 8619, 2),
(81314, 8625, 19, NULL, 90311, 8619, 2),
(81315, 8625, 37, NULL, 90312, 8619, 2),
(81316, 8625, 11, NULL, 90313, 8619, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `interv_surgery`
--

CREATE TABLE `interv_surgery` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `DUE_TO_PSS_ID` tinyint(3) UNSIGNED DEFAULT NULL,
  `SURGERY_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL,
  `STMT_ID` tinyint(3) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `interv_surgery`
--

INSERT INTO `interv_surgery` (`ID`, `PATIENT_ID`, `DUE_TO_PSS_ID`, `SURGERY_DATE_ID`, `VISIT_ID`, `STMT_ID`) VALUES
(11609, 8621, NULL, 859795, 8615, 1),
(11610, 8621, NULL, 859824, 8615, 2),
(11611, 8622, NULL, 859947, 8616, 2),
(11612, 8622, NULL, 859952, 8616, 2),
(11613, 8623, NULL, 860078, 8617, 2),
(11614, 8623, NULL, 860083, 8617, 2),
(11615, 8624, NULL, 860207, 8618, 2),
(11616, 8624, NULL, 860211, 8618, 2),
(11617, 8625, NULL, 860336, 8619, 2),
(11618, 8625, NULL, 860341, 8619, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `lifestyle_smoking`
--

CREATE TABLE `lifestyle_smoking` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `STATUS_ID` smallint(5) UNSIGNED NOT NULL,
  `AMOUNT_ID` int(10) UNSIGNED DEFAULT NULL COMMENT 'packets of cigarettes per year',
  `PERIOD_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL,
  `STMT_ID` tinyint(3) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `lifestyle_smoking`
--

INSERT INTO `lifestyle_smoking` (`ID`, `PATIENT_ID`, `STATUS_ID`, `AMOUNT_ID`, `PERIOD_ID`, `VISIT_ID`, `STMT_ID`) VALUES
(6834, 8621, 1, 227716, NULL, 8615, 1),
(6835, 8622, 1, NULL, NULL, 8616, 1),
(6836, 8623, 1, 227715, NULL, 8617, 1),
(6837, 8624, 1, 227717, NULL, 8618, 1),
(6838, 8625, 1, NULL, NULL, 8619, 1);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `other_clinical_trials`
--

CREATE TABLE `other_clinical_trials` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `PERIOD_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL,
  `STMT_ID` tinyint(3) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `other_clinical_trials`
--

INSERT INTO `other_clinical_trials` (`ID`, `PATIENT_ID`, `PERIOD_ID`, `VISIT_ID`, `STMT_ID`) VALUES
(5799, 8621, 90262, 8615, 2),
(5800, 8622, 90265, 8616, 1),
(5801, 8623, 90290, 8617, 2),
(5802, 8624, 90303, 8618, 2),
(5803, 8625, 90315, 8619, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `other_family_history`
--

CREATE TABLE `other_family_history` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `MEDICAL_CONDITION_ID` smallint(5) UNSIGNED NOT NULL,
  `RELATIVE_DEGREE_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL,
  `STMT_ID` tinyint(3) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `other_healthcare_visit`
--

CREATE TABLE `other_healthcare_visit` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `SPECIALIST_ID` smallint(5) UNSIGNED NOT NULL,
  `DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `VISIT_ID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `patient`
--

CREATE TABLE `patient` (
  `ID` int(10) UNSIGNED NOT NULL,
  `UID` varchar(20) NOT NULL,
  `DATE_OF_BIRTH_ID` int(10) UNSIGNED DEFAULT NULL,
  `PSS_SYMPTOMS_ONSET_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `PSS_DIAGNOSIS_DATE_ID` int(10) UNSIGNED DEFAULT NULL,
  `COHORT_INCLUSION_DATE_ID` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `patient`
--

INSERT INTO `patient` (`ID`, `UID`, `DATE_OF_BIRTH_ID`, `PSS_SYMPTOMS_ONSET_DATE_ID`, `PSS_DIAGNOSIS_DATE_ID`, `COHORT_INCLUSION_DATE_ID`) VALUES
(8621, '8621', 859688, 859689, 859690, NULL),
(8622, '8622', 859827, 859828, 859829, NULL),
(8623, '8623', 859954, 859955, 859956, NULL),
(8624, '8624', 860087, 860088, 860089, NULL),
(8625, '8625', 860215, 860216, 860217, NULL);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `patient_visit`
--

CREATE TABLE `patient_visit` (
  `ID` int(10) UNSIGNED NOT NULL,
  `PATIENT_ID` int(10) UNSIGNED NOT NULL,
  `VISIT_DATE_ID` int(10) UNSIGNED NOT NULL,
  `VISIT_TYPE_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `VISIT_STATUS_ID` smallint(5) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `patient_visit`
--

INSERT INTO `patient_visit` (`ID`, `PATIENT_ID`, `VISIT_DATE_ID`, `VISIT_TYPE_ID`, `VISIT_STATUS_ID`) VALUES
(8615, 8621, 859691, 1, 2),
(8616, 8622, 859830, 1, 2),
(8617, 8623, 859957, 1, 2),
(8618, 8624, 860090, 1, 2),
(8619, 8625, 860218, 1, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_activity_level`
--

CREATE TABLE `voc_activity_level` (
  `ID` smallint(10) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `ACTIVITY_LEVEL` varchar(10) NOT NULL,
  `VALUE` smallint(5) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_activity_level`
--

INSERT INTO `voc_activity_level` (`ID`, `CODE`, `ACTIVITY_LEVEL`, `VALUE`) VALUES
(1, 'ACTLVL-01', 'LOW', 1),
(2, 'ACTLVL-02', 'MODERATE', 2),
(3, 'ACTLVL-03', 'HIGH', 3),
(4, 'ACTLVL-00', 'NO', 0);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_ana_pattern`
--

CREATE TABLE `voc_ana_pattern` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_ana_pattern`
--

INSERT INTO `voc_ana_pattern` (`ID`, `CODE`, `NAME`) VALUES
(1, 'ANA-PAT-01', 'Nuclear speckled'),
(2, 'ANA-PAT-02', 'Nuclear homogeneous'),
(3, 'ANA-PAT-03', 'Nucleolar'),
(4, 'ANA-PAT-04', 'Anticentromere'),
(5, 'ANA-PAT-05', 'Cytoplasmic antimitochondrial'),
(6, 'ANA-PAT-06', 'Other nuclear '),
(7, 'ANA-PAT-07', 'Other cytoplasmic');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_assessment`
--

CREATE TABLE `voc_assessment` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `BROADER_TERM_ID` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Normal / Abnormal values';

--
-- Άδειασμα δεδομένων του πίνακα `voc_assessment`
--

INSERT INTO `voc_assessment` (`ID`, `CODE`, `NAME`, `BROADER_TERM_ID`) VALUES
(1, 'ASSESS-10', 'Normal ', NULL),
(2, 'ASSESS-20', 'Abnormal ', NULL),
(3, 'ASSESS-21', 'Below Down Normal Limit (DNL)', 2),
(4, 'ASSESS-22', 'Above Upper Normal Limit (UNL)', 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_biopsy`
--

CREATE TABLE `voc_biopsy` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  `BROADER_TERM_ID` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Salivary gland biopsy';

--
-- Άδειασμα δεδομένων του πίνακα `voc_biopsy`
--

INSERT INTO `voc_biopsy` (`ID`, `CODE`, `NAME`, `BROADER_TERM_ID`) VALUES
(1, 'SAL-BIO-10', 'Major Salivary Gland Biopsy', NULL),
(2, 'SAL-BIO-11', 'Parotid Gland Biopsy', 1),
(3, 'SAL-BIO-12', 'Submandibular Gland Biopsy', 1),
(4, 'SAL-BIO-13', 'Sublingual Gland Biopsy', 1),
(5, 'SAL-BIO-20', 'Minor Salivary Gland Biopsy', NULL),
(6, 'SAL-BIO-21', 'Labial Gland (lip) Biopsy', 5);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_bmi_class`
--

CREATE TABLE `voc_bmi_class` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Body mass index (BMI)';

--
-- Άδειασμα δεδομένων του πίνακα `voc_bmi_class`
--

INSERT INTO `voc_bmi_class` (`ID`, `CODE`, `NAME`) VALUES
(1, 'BMI-01', 'Underweight (BMI < 18.5)'),
(2, 'BMI-02', 'Normal Weight (BMI 18.5 - 24.9)'),
(3, 'BMI-03', 'Overweight (BMI 25 - 29.9)'),
(4, 'BMI-04', 'Obesity (BMI >= 30)');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_caci_condition`
--

CREATE TABLE `voc_caci_condition` (
  `ID` smallint(10) UNSIGNED NOT NULL,
  `CODE` varchar(20) NOT NULL,
  `CONDITION` varchar(150) NOT NULL,
  `SCORE` smallint(5) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Charlson-Age Comorbidity Index (CACI)';

--
-- Άδειασμα δεδομένων του πίνακα `voc_caci_condition`
--

INSERT INTO `voc_caci_condition` (`ID`, `CODE`, `CONDITION`, `SCORE`) VALUES
(1, 'CACI-01', 'Acquired immunodeficiency Syndrome (AIDS)', 6),
(2, 'CACI-02', 'Metastatic solid tumor', 6),
(3, 'CACI-03', 'Moderate or severe liver disease', 3),
(4, 'CACI-04', 'Any non-metastatic solid tumor', 2),
(5, 'CACI-05', 'Malignant lymphoma', 2),
(6, 'CACI-06', 'Leukemia', 2),
(7, 'CACI-07', 'Diabetes with end organ damage', 2),
(8, 'CACI-08', 'Moderate or severe renal disease', 2),
(9, 'CACI-09', 'Hemiplegia', 2),
(10, 'CACI-10', 'Diabetes without end organ damage', 1),
(11, 'CACI-11', 'Mild liver disease', 1),
(12, 'CACI-12', 'Ulcer disease', 1),
(13, 'CACI-13', 'Connective tissue disease', 1),
(14, 'CACI-14', 'Chronic pulmonary disease', 1),
(15, 'CACI-15', 'Dementia', 1),
(16, 'CACI-16', 'Cerebrovascular disease', 1),
(17, 'CACI-17', 'Peripheral vascular disease', 1),
(18, 'CACI-18', 'Congestive heart failure', 1),
(19, 'CACI-19', 'Myocardial infarction', 1);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_confirmation`
--

CREATE TABLE `voc_confirmation` (
  `ID` tinyint(3) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `ACRONYM` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Yes or No values';

--
-- Άδειασμα δεδομένων του πίνακα `voc_confirmation`
--

INSERT INTO `voc_confirmation` (`ID`, `CODE`, `NAME`, `ACRONYM`) VALUES
(1, 'CONFIRM-01', 'True / Yes', 'T'),
(2, 'CONFIRM-02', 'False / No', 'F');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_cryo_type`
--

CREATE TABLE `voc_cryo_type` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Cryoglobulin Type';

--
-- Άδειασμα δεδομένων του πίνακα `voc_cryo_type`
--

INSERT INTO `voc_cryo_type` (`ID`, `CODE`, `NAME`, `DESCRIPTION`) VALUES
(1, 'CRYO-01', 'Type I', 'Monoclonal Cryoglobulin Monoclonal IgG, IgM, IgA, or their κ or λ light chains'),
(2, 'CRYO-02', 'Type II', 'Mixed Cryoglobulin with a Monoclonal component Monoclonal IgM plus polyclonal IgG or, rarely, IgA'),
(3, 'CRYO-03', 'Type III', 'Mixed Polyclonal Cryoglobulin Polyclonal IgM plus polyclonal IgG or IgA');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_direction`
--

CREATE TABLE `voc_direction` (
  `ID` tinyint(4) NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `ACRONYM` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_direction`
--

INSERT INTO `voc_direction` (`ID`, `CODE`, `NAME`, `ACRONYM`) VALUES
(1, 'OP-DBA-01', 'Before', 'B'),
(2, 'OP-DBA-02', 'During', 'D'),
(3, 'OP-DBA-03', 'After', 'A');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_education_level`
--

CREATE TABLE `voc_education_level` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `DESCRIPTION` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_education_level`
--

INSERT INTO `voc_education_level` (`ID`, `CODE`, `NAME`, `DESCRIPTION`) VALUES
(1, 'EDU-LEV-01', 'Primary Education', 'Primary education and elementary education is typically the first stage of formal education, coming after preschool and before secondary education (The first two grades of primary school, Grades 1 and 2, are also part of early childhood education).'),
(2, 'EDU-LEV-02', 'Secondary Education', 'Secondary education covers two phases on the International Standard Classification of Education (ISCED) scale. Level 2 or lower secondary education (less common junior secondary education) is considered the second and final phase of basic education, and level 3 (upper) secondary education is the stage before tertiary education.'),
(3, 'EDU-LEV-03', 'Tertiary education', 'Tertiary education, also referred to as third stage, third level, and postsecondary education, is the educational level following the completion of a school providing a secondary education.'),
(4, 'EDU-LEV-99', 'Other Education Level', 'Another Education Level that do not belong in the three categories specified.');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_essdai_domain`
--

CREATE TABLE `voc_essdai_domain` (
  `ID` smallint(10) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `DOMAIN_NAME` varchar(100) NOT NULL,
  `DOMAIN_WEIGHT` smallint(5) UNSIGNED NOT NULL,
  `EXCLUDE_ACTIVITY_LEVEL` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='EULAR Sjögren''s syndrome disease activity index (ESSDAI)';

--
-- Άδειασμα δεδομένων του πίνακα `voc_essdai_domain`
--

INSERT INTO `voc_essdai_domain` (`ID`, `CODE`, `DOMAIN_NAME`, `DOMAIN_WEIGHT`, `EXCLUDE_ACTIVITY_LEVEL`) VALUES
(1, 'ESSDAI-01', 'Constitutional', 3, 3),
(2, 'ESSDAI-02', 'Lymphadenopathy and lymphoma', 4, NULL),
(3, 'ESSDAI-03', 'Glandular', 2, 3),
(4, 'ESSDAI-04', 'Articular', 2, NULL),
(5, 'ESSDAI-05', 'Cutaneous', 3, NULL),
(6, 'ESSDAI-06', 'Pulmonary', 5, NULL),
(7, 'ESSDAI-07', 'Renal', 5, NULL),
(8, 'ESSDAI-08', 'Muscular', 6, NULL),
(9, 'ESSDAI-09', 'Peripheral nervous system', 5, NULL),
(10, 'ESSDAI-10', 'Central nervous system', 5, 1),
(11, 'ESSDAI-11', 'Hematological', 2, NULL),
(12, 'ESSDAI-12', 'Biological', 1, 3);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_ethnicity`
--

CREATE TABLE `voc_ethnicity` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `DESCRIPTION` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_ethnicity`
--

INSERT INTO `voc_ethnicity` (`ID`, `CODE`, `NAME`, `DESCRIPTION`) VALUES
(1, 'ETHN-01', 'Latin', 'A person of Cuban, Mexican, Puerto Rican, South or Central American, or other Spanish culture or origin, regardless of race. The term, \'Spanish origin\', can be used in addition to \'Hispanic or Latino\'.'),
(2, 'ETHN-02', 'American Indian or Alaska Native', 'A person having origins in any of the original peoples of North and South America (including Central America), and who maintains tribal affiliation or community attachment.'),
(3, 'ETHN-03', 'Asian', 'A person having origins in any of the original peoples of the Far East, Southeast Asia, or the Indian subcontinent, including, for example, Cambodia, China, India, Japan, Korea, Malaysia, Pakistan, the Philippine Islands, Thailand, and Vietnam.'),
(4, 'ETHN-04', 'Black or African American', 'A person having origins in any of the black racial groups of Africa. Terms such as \'Haitian\' or \'Negro\' can be used in addition to \'Black or African American\'.'),
(5, 'ETHN-05', 'Native Hawaiian or Other Pacific Islander', 'A person having origins in any of the original peoples of Hawaii, Guam, Samoa, or other Pacific Islands.'),
(6, 'ETHN-06', 'Caucasian', 'A person having origins in any of the original peoples of Europe, the Middle East, or North Africa.');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_exam_outcome_type`
--

CREATE TABLE `voc_exam_outcome_type` (
  `ID` tinyint(3) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `NOTES` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_exam_outcome_type`
--

INSERT INTO `voc_exam_outcome_type` (`ID`, `CODE`, `NAME`, `NOTES`) VALUES
(1, 'VALUE-TYPE-01', 'Amount and/or Assessment', 'The value of a test is an amount. We would like to know both the specific amount measured (if available) and whether it was normal or not (including normal range of values, if available/applicable).'),
(2, 'VALUE-TYPE-02', 'Boolean (Yes/No)', 'The value of a test can be Yes/No. The user may still want to characterize whether the yes/no value indicates a normal condition or not. '),
(3, 'VALUE-TYPE-03', 'Assessment Only', 'The value of a test is an amount. However, we are only interested whether it was normal or not (including normal range of values, if available/applicable).'),
(4, 'VALUE-TYPE-04', 'Term', 'The value of a test is a term from a specific List (specified in another DB table).'),
(5, 'VALUE-TYPE-05', 'Number and/or Assessment', 'The value of a test is an number. We would like to know both the specific number (if available) and whether it was normal or not (including normal range of values, if available/applicable).'),
(6, 'VALUE-TYPE-06', 'Number and/or Term', 'The value of a test is an number. We would like to know both the specific number (if available) and whether it belongs to a specific class (from a predefined List of Classes/Terms).');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_ipi_risk`
--

CREATE TABLE `voc_ipi_risk` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Risk for non-Hodgkin Lymphoma';

--
-- Άδειασμα δεδομένων του πίνακα `voc_ipi_risk`
--

INSERT INTO `voc_ipi_risk` (`ID`, `CODE`, `NAME`, `DESCRIPTION`) VALUES
(1, 'IPI-RISK-1', 'Low risk', 'Low risk (0-1 points) - 5-year survival of 73%'),
(2, 'IPI-RISK-2', 'Low-intermediate risk', 'Low-intermediate risk (2 points) - 5-year survival of 51%'),
(3, 'IPI-RISK-3', 'High-intermediate risk', 'High-intermediate risk (3 points) - 5-year survival of 43%'),
(4, 'IPI-RISK-4', 'High risk', 'High risk (4-5 points) - 5-year survival of 26%');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_lab_test`
--

CREATE TABLE `voc_lab_test` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(10) NOT NULL,
  `NAME` varchar(250) NOT NULL,
  `OUTCOME_VALUE_ID` tinyint(3) UNSIGNED DEFAULT NULL,
  `UNIT_ID` smallint(3) UNSIGNED DEFAULT NULL,
  `RANGE_NOTES` varchar(200) DEFAULT NULL,
  `BROADER_TERM_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `LAB_TEST_TYPE_ID` tinyint(3) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_lab_test`
--

INSERT INTO `voc_lab_test` (`ID`, `CODE`, `NAME`, `OUTCOME_VALUE_ID`, `UNIT_ID`, `RANGE_NOTES`, `BROADER_TERM_ID`, `LAB_TEST_TYPE_ID`) VALUES
(1, 'BLOOD-100', 'Hematologic Test', NULL, NULL, NULL, NULL, 1),
(2, 'BLOOD-110', 'Hemoglobin [Mass/volume]', 1, 4, 'Anemia, \"yes\" if Hb < 12.0 g/dL females and < 13.0 g/dL males', 1, 1),
(3, 'BLOOD-120', 'Leukocytes [#/volume]', 1, 1, 'Leukopenia, \"yes\" if < 4000 /mm^3', 1, 1),
(4, 'BLOOD-130', 'Neutrophils [#/volume]', 1, 1, 'Neutropenia, \"yes\" if < 1500 /mm^3', 1, 1),
(5, 'BLOOD-140', 'Lymphocytes [#/volume]', 1, 1, 'Lymphopenia, \"yes\" if < 1000 /mm^3', 1, 1),
(6, 'BLOOD-150', 'Platelets (aka Thrombocytes)  [#/volume]', 1, 1, 'Thrombocytopenia, \"yes\" if < 150000 /mm^3', 1, 1),
(7, 'BLOOD-200', 'Biochemistry Test', NULL, NULL, NULL, NULL, 1),
(8, 'BLOOD-211', 'Albumin [Mass/volume]', 1, 4, 'Low serum albumin (Reference Range >= 3.5 g/dl)', 7, 1),
(9, 'BLOOD-212', 'Creatinine [Mass/volume]', 1, 8, NULL, 7, 1),
(10, 'BLOOD-213', 'Folate [Mass/volume]', 1, 15, NULL, 7, 1),
(11, 'BLOOD-214', 'Homocysteine Levels [Mass/volume]', 1, 17, NULL, 7, 1),
(12, 'BLOOD-215', 'Uric acid [Mass/volume]', 1, 8, 'Normal Value: < 6 mg/dL in female, < 7 mg/dL in males', 7, 1),
(13, 'BLOOD-216', 'Vitamin B12 [Mass/volume]', 1, 12, NULL, 7, 1),
(14, 'BLOOD-221', 'Alkaline phosphatase (ALP)', 3, NULL, NULL, 7, 1),
(15, 'BLOOD-222', 'Alanine aminotransferase (ALT)', 3, NULL, NULL, 7, 1),
(16, 'BLOOD-223', 'Aspartate aminotransferase (AST)', 3, NULL, NULL, 7, 1),
(17, 'BLOOD-224', 'Gamma glutamyl transferase (γ-GT)', 3, NULL, NULL, 7, 1),
(18, 'BLOOD-225', 'Lactate dehydrogenase (LDH)', 3, NULL, NULL, 7, 1),
(19, 'BLOOD-231', 'Potassium [Moles/Volume]', 1, 26, NULL, 7, 1),
(20, 'BLOOD-232', 'Sodium [Moles/Volume]', 1, 26, NULL, 7, 1),
(21, 'BLOOD-233', 'Creatine phosphokinase (CPK) Muscle Enzymes [Enzymatic activity/volume]', 1, 27, NULL, 7, 1),
(22, 'BLOOD-234', 'Calcium [Moles/Volume]', 1, 8, NULL, 7, 1),
(23, 'BLOOD-235', 'Bilirubin Total [Mass/Volume]', 1, 8, NULL, 7, 1),
(24, 'BLOOD-236', 'Bilirubin Indirect [Mass/Volume]', 1, 8, NULL, 7, 1),
(25, 'BLOOD-300', 'Serum Protein Test', NULL, NULL, NULL, NULL, 1),
(26, 'BLOOD-310', 'Cryoglobulin Test', NULL, NULL, NULL, 25, 1),
(27, 'BLOOD-311', 'Cryoglobulins [presence]', 2, NULL, NULL, 26, 1),
(28, 'BLOOD-312', 'Cryoglobulins Type [term]', 4, NULL, 'An ID from the Cryo Type (table)', 26, 1),
(29, 'BLOOD-320', 'Immunoglobulin Test', NULL, NULL, NULL, 25, 1),
(30, 'BLOOD-321', 'Immunoglobulins [Mass/volume] (in serum)', 1, 8, NULL, 29, 1),
(31, 'BLOOD-322', 'Immunoglobulin A (IgA) [Mass/volume]', 1, 5, NULL, 29, 1),
(32, 'BLOOD-323', 'Immunoglobulin G (IgG) [Mass/volume]', 1, 5, NULL, 29, 1),
(33, 'BLOOD-324', 'Immunoglobulin G4 (IgG4)', 3, NULL, NULL, 29, 1),
(34, 'BLOOD-325', 'Immunoglobulin M (IgM) [Mass/volume]', 1, 5, NULL, 29, 1),
(35, 'BLOOD-326', 'Kappa Free light chains [Mass/volume] (in Serum)', 1, 9, NULL, 29, 1),
(36, 'BLOOD-327', 'Lambda Free light chains [Mass/volume] (in Serum)', 1, 9, NULL, 29, 1),
(37, 'BLOOD-328', 'Free light (immunoglobulin) chains KAPPA/LAMBDA ratio', 1, 2, NULL, 29, 1),
(38, 'BLOOD-330', 'Monoclonal M-component [presence ] (in Serum)', 2, NULL, NULL, 25, 1),
(39, 'BLOOD-340', 'Haptoglobin', 3, NULL, NULL, 25, 1),
(40, 'BLOOD-400', 'Lipid Test', NULL, NULL, NULL, NULL, 1),
(41, 'BLOOD-410', 'Total cholesterol levels [Mass/volume]', 1, 8, NULL, 40, 1),
(42, 'BLOOD-420', 'Low-density lipoprotein (LDL) cholesterol [Mass/volume]', 1, 8, NULL, 40, 1),
(43, 'BLOOD-430', 'High-density lipoprotein (HDL) cholesterol [Mass/volume]', 1, 8, NULL, 40, 1),
(44, 'BLOOD-440', 'Triglyceride levels [Mass/volume]', 1, 8, NULL, 40, 1),
(45, 'BLOOD-500', 'Autoantibody Test', NULL, NULL, NULL, NULL, 1),
(46, 'BLOOD-511', 'Rheumatoid Factor (RF) [Units/volume]', 1, 6, NULL, 45, 1),
(47, 'BLOOD-512', 'Rheumatoid Factor (RF) titer [Units/volume]', 1, 6, NULL, 45, 1),
(48, 'BLOOD-521', 'Antinuclear Antibodies (ANA) [presence]', 2, NULL, NULL, 45, 1),
(49, 'BLOOD-522', 'Antinuclear Antibodies (ANA) titer', 5, NULL, 'The number is actually a fraction, e.g., 1/160', 45, 1),
(50, 'BLOOD-523', 'Antinuclear Antibodies (ANA) pattern [term]', 4, NULL, 'An ID from the ANA-Pattern (table)', 45, 1),
(51, 'BLOOD-530', 'Anti-Ro/SSA [presence]', 2, NULL, NULL, 45, 1),
(52, 'BLOOD-531', 'Anti-Ro52/SSA [presence]', 2, NULL, NULL, 51, 1),
(53, 'BLOOD-532', 'Anti-Ro60/SSA [presence]', 2, NULL, NULL, 51, 1),
(54, 'BLOOD-540', 'Anti-La/SSB [presence]', 2, NULL, NULL, 45, 1),
(55, 'BLOOD-550', 'Other Autoantibody Test', NULL, NULL, NULL, NULL, 1),
(56, 'BLOOD-551', 'Anti-CCP antibodies [Presence]', 2, NULL, NULL, 55, 1),
(57, 'BLOOD-552', 'Anti-dsDNA [Presence]', 2, NULL, NULL, 55, 1),
(58, 'BLOOD-553', 'Anti-Smith (Anti-Sm) antibodies [Presence]', 2, NULL, NULL, 55, 1),
(59, 'BLOOD-554', 'Anti-RNP antibodies [Presence]', 2, NULL, NULL, 55, 1),
(60, 'BLOOD-555', 'Anti-Scl-70 antibodies [Presence]', 2, NULL, NULL, 55, 1),
(61, 'BLOOD-556', 'Anti-synthetase antibodies (any) [Presence]', 2, NULL, NULL, 55, 1),
(62, 'BLOOD-557', 'Lupus Anticoagulant (LA) [Presence]', 2, NULL, NULL, 55, 1),
(63, 'BLOOD-558', 'Anti-cardiolipin (ACLA) IgG [Presence]', 2, NULL, NULL, 55, 1),
(64, 'BLOOD-559', 'Anti-cardiolipin (ACLA) IgM [Presence]', 2, NULL, NULL, 55, 1),
(65, 'BLOOD-561', 'Anti-beta 2 glycoprotein IgG [Presence]', 2, NULL, NULL, 55, 1),
(66, 'BLOOD-562', 'Anti-beta 2 glycoprotein IgM [Presence]', 2, NULL, NULL, 55, 1),
(67, 'BLOOD-563', 'Anti-neutrophil cytoplasmic (ANCA) antibodies [Presence]', 2, NULL, NULL, 55, 1),
(68, 'BLOOD-564', 'Anti-Saccharomyces cerevisiae (ASCA) IgG antibodies [Presence]', 2, NULL, NULL, 55, 1),
(69, 'BLOOD-565', 'Anti-Saccharomyces cerevisiae (ASCA) IgM antibodies [Presence]', 2, NULL, NULL, 55, 1),
(70, 'BLOOD-566', 'Anti-thyroglobulin (anti-Tg) antibody [Presence]', 2, NULL, NULL, 55, 1),
(71, 'BLOOD-567', 'Anti-thyroperoxidase (anti-TPO) antibodies [Presence]', 2, NULL, NULL, 55, 1),
(72, 'BLOOD-568', 'Anti-TSH Receptor (anti-TSHR) antibodies [Presence]', 2, NULL, NULL, 55, 1),
(73, 'BLOOD-569', 'Anti-gastric mucosa antibodies [Presence]', 2, NULL, NULL, 55, 1),
(74, 'BLOOD-571', 'Anti-transglutaminase IgA antibodies [Presence]', 2, NULL, NULL, 55, 1),
(75, 'BLOOD-572', 'Anti-transglutaminase IgG antibodies [Presence]', 2, NULL, NULL, 55, 1),
(76, 'BLOOD-573', 'Anti-LKM antibodies [Presence]', 2, NULL, NULL, 55, 1),
(77, 'BLOOD-574', 'Anti-Smooth Muscle Antibody (ASMA) [Presence]', 2, NULL, NULL, 55, 1),
(78, 'BLOOD-575', 'Coombs test direct', 2, NULL, NULL, 55, 1),
(79, 'BLOOD-576', 'Coombs test indirect', 2, NULL, NULL, 55, 1),
(80, 'BLOOD-577', 'Anti-platelets antibodies [Presence]', 2, NULL, NULL, 55, 1),
(81, 'BLOOD-578', 'Anti-centromere (any quantitative test)', 2, NULL, NULL, 55, 1),
(82, 'BLOOD-579', 'Anti-mitochondrial (any quantitative test)', 2, NULL, NULL, 55, 1),
(83, 'BLOOD-581', 'Anti-mitochondrial M2 (any quantitative test)', 2, NULL, NULL, 55, 1),
(84, 'BLOOD-582', 'Other autoantibodies [Presence]', 2, NULL, NULL, 55, 1),
(85, 'BLOOD-600', 'Complement Test', NULL, NULL, NULL, NULL, 1),
(86, 'BLOOD-610', 'C3 levels (Serum complement) [Mass/volume]', 1, 8, NULL, 85, 1),
(87, 'BLOOD-620', 'C4 levels (Serum complement) [Mass/volume]', 1, 8, NULL, 85, 1),
(88, 'BLOOD-700', 'Cluster of differentiation (CD) Test', NULL, NULL, NULL, NULL, 1),
(89, 'BLOOD-710', 'CD3 (T cells) [#/volume]', 1, 3, NULL, 88, 1),
(90, 'BLOOD-720', 'CD4 (T helper/inducer cells) [#/volume]', 1, 3, NULL, 88, 1),
(91, 'BLOOD-730', 'CD8 (T suppressor/cytotoxic cells) [#/volume]', 1, 3, NULL, 88, 1),
(92, 'BLOOD-740', 'CD19 (B-lymphocyte antigen) [#/volume]', 1, 3, NULL, 88, 1),
(93, 'BLOOD-800', 'Miscellaneous Test', NULL, NULL, NULL, NULL, 1),
(94, 'BLOOD-810', 'C reactive protein [Mass/volume]', 1, 8, 'Increased C-reactive protein, \"yes\" if > 0.5 mg/dL', 93, 1),
(95, 'BLOOD-820', 'Anti-Hepatitis C Virus (HCV) antibody [presence]', 2, NULL, NULL, 93, 1),
(96, 'BLOOD-830', 'Hepatitis B surface antigen (HBsAg) [Presence]', 2, NULL, NULL, 93, 1),
(97, 'BLOOD-840', 'Hepatitis B core antibody (HBcAb) [Presence]', 2, NULL, NULL, 93, 1),
(98, 'BLOOD-850', 'Hepatitis B surface antibody (HBsAb) [Presence]', 2, NULL, NULL, 93, 1),
(99, 'BLOOD-860', 'Hepatitis B virus (HBV) DNA [Presence]', 2, NULL, NULL, 93, 1),
(100, 'BLOOD-870', 'HIV I and II Antibodies [Presence]', 2, NULL, NULL, 93, 1),
(101, 'BLOOD-900', 'Blood Biomarker', NULL, NULL, NULL, NULL, 1),
(102, 'BLOOD-910', 'Beta-2 Microglobulin [Mass/volume] (in Serum)', 1, 16, NULL, 101, 1),
(103, 'BLOOD-920', 'B lymphocyte activating factor (BAFF) [Mass/volume] (in serum)', 1, 13, NULL, 101, 1),
(104, 'BLOOD-930', 'Chemokine Ligand 13 (CXCL13) [Mass/volume]  (in serum)', 1, 13, NULL, 101, 1),
(105, 'BLOOD-940', 'FMS-like tyrosine kinase 3 ligand (FLT3L) [Mass/volume]  (in serum)', 1, 13, NULL, 101, 1),
(106, 'BLOOD-950', 'Thymic stromal lymphopoietin (TSLP) [Mass/volume]  (in serum)', 1, 13, NULL, 101, 1),
(107, 'BLOOD-960', 'TNFAIP3 (A20) - DNA [presence]', 2, NULL, NULL, 101, 1),
(108, 'BLOOD-971', 'Interferon (IFN) signature Type I [presence] (in PBMCs)', 2, NULL, NULL, 101, 1),
(109, 'BLOOD-972', 'Interferon (IFN) signature Type II [presence] (in PBMCs)', 2, NULL, NULL, 101, 1),
(110, 'BLOOD-973', 'Interferon (IFN) signature Type I and II [presence] (in PBMCs)', 2, NULL, NULL, 101, 1),
(201, 'URINE-100', 'Urinalysis', 2, NULL, NULL, NULL, 2),
(202, 'URINE-110', 'Urine pH', 1, 14, 'High pH, Normal Value: < 6.5', 201, 2),
(203, 'URINE-120', 'Casts [Presence] in Urine', 2, NULL, 'Healthy: no', 201, 2),
(204, 'URINE-121', 'Red Blood Cell Casts [Presence] in Urine', 2, NULL, 'Healthy: no', 203, 2),
(205, 'URINE-122', 'White Blood Cell Casts [Presence] in Urine', 2, NULL, 'Healthy: no', 203, 2),
(206, 'URINE-130', 'Urine Specific Gravity', 5, 19, 'Normal Value: > 1010 and < 1030', 201, 2),
(207, 'URINE-140', 'Blood [Presence] in Urine', 2, NULL, NULL, 201, 2),
(208, 'URINE-141', 'Red Blood Cells [Presence] in Urine', 2, NULL, NULL, 207, 2),
(209, 'URINE-142', 'White Blood Cells [Presence] in Urine', 2, NULL, NULL, 207, 2),
(210, 'URINE-150', 'Protein [Presence] in Urine', 2, NULL, NULL, 201, 2),
(211, 'URINE-200', 'Proteins [Mass/time] (in 24 hour Urine)', 1, 7, 'Normal Value: < 500 mg/24h', NULL, 2),
(212, 'URINE-300', 'Bence-Jones Proteinuria [Presence]', 2, NULL, NULL, NULL, 2),
(301, 'ORAL-10', 'Saliva Flow\'s test', 1, 10, NULL, NULL, 3),
(302, 'ORAL-11', 'Unstimulated saliva flow Value', 1, 10, 'Unstimulated whole saliva test POSITIVE IF <= 1.5 mL / 15 min (cut off values)', 301, 3),
(303, 'ORAL-12', 'Stimulated saliva flow (with any stimulus)', 1, 10, NULL, 301, 3),
(401, 'OCULAR-01', 'Schirmer\'s Test (Worst Eye) Value', 3, NULL, 'Schirmer\'s test POSITIVE IF <= 5 mm in 5 min in either eye (cut off values)', NULL, 4),
(402, 'OCULAR-02', 'Rose Bengal Staining / Van Bijsterveld Score', 3, NULL, 'Rose Bengal/van Bijsterveld\'s: <= 4 (cut off values)', NULL, 4),
(403, 'OCULAR-03', 'Ocular Staining Score / Fluorescein Lissamine Green', 3, NULL, NULL, NULL, 4),
(501, 'BIOPSY-10', 'Biopsy Condition Exam', NULL, NULL, NULL, NULL, 5),
(502, 'BIOPSY-11', 'Germinal Centers [check]', 2, NULL, NULL, 501, 5),
(503, 'BIOPSY-12', 'Myoepithelial sialadenitis [check]', 2, NULL, NULL, 501, 5),
(504, 'BIOPSY-13', 'Lymphoma [check]', 2, NULL, NULL, 501, 5),
(505, 'BIOPSY-14', 'Fibrosis / AdiposeTissue / Atrophy [check]', 2, NULL, NULL, 501, 5),
(506, 'BIOPSY-20', 'Biopsy Score', NULL, NULL, NULL, NULL, 5),
(507, 'BIOPSY-21', 'Greenspan Focus Score', 5, NULL, NULL, 506, 5),
(508, 'BIOPSY-22', 'Chisholm-Mason Score', 5, NULL, NULL, 506, 5),
(509, 'BIOPSY-30', 'Tissue Biomarker', NULL, NULL, NULL, NULL, 5),
(510, 'BIOPSY-31', 'miRNA200b-5p', 1, 13, 'Normal: > 0.4156, Low: < 0.4156', 509, 5),
(511, 'BIOPSY-32', 'Interferon (IFN) signature Type I [check] (in salivary gland biopsy)', 2, NULL, NULL, 509, 5),
(512, 'BIOPSY-33', 'Interferon (IFN) signature Type II [check] (in salivary gland biopsy)', 2, NULL, NULL, 509, 5),
(513, 'BIOPSY-34', 'Interferon (IFN) signature Type I and II [check] (in salivary gland biopsy)', 2, NULL, NULL, 509, 5);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_lab_test_type`
--

CREATE TABLE `voc_lab_test_type` (
  `ID` tinyint(3) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_lab_test_type`
--

INSERT INTO `voc_lab_test_type` (`ID`, `CODE`, `NAME`) VALUES
(1, 'TEST-TYPE-01', 'Blood / Serum / Plasma Test'),
(2, 'TEST-TYPE-02', 'Urine Test'),
(3, 'TEST-TYPE-03', 'Oral Test'),
(4, 'TEST-TYPE-04', 'Ocular Test'),
(5, 'TEST-TYPE-05', 'Salivary Gland (Biopsy) / Tissue Test');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_lymphoma_organ`
--

CREATE TABLE `voc_lymphoma_organ` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `BROADER_TERM_ID` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_lymphoma_organ`
--

INSERT INTO `voc_lymphoma_organ` (`ID`, `CODE`, `NAME`, `BROADER_TERM_ID`) VALUES
(1, 'ORGAN-01', 'Salivary Gland', NULL),
(2, 'ORGAN-02', 'Lacrimal Gland', NULL),
(3, 'ORGAN-03', 'Lung', NULL),
(4, 'ORGAN-04', 'Stomach', NULL),
(5, 'ORGAN-05', 'Liver', NULL),
(6, 'ORGAN-06', 'Spleen', NULL),
(7, 'ORGAN-07', 'Lymph Nodes', NULL),
(8, 'ORGAN-08', 'Bone Marrow', NULL),
(9, 'ORGAN-90', 'Other Site', NULL),
(10, 'ORGAN-91', 'Other MALT site', 9),
(11, 'ORGAN-92', 'Other non-MALT site', 9);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_lymphoma_stage`
--

CREATE TABLE `voc_lymphoma_stage` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Lugano classification for staging of lymphomas.';

--
-- Άδειασμα δεδομένων του πίνακα `voc_lymphoma_stage`
--

INSERT INTO `voc_lymphoma_stage` (`ID`, `CODE`, `NAME`, `DESCRIPTION`) VALUES
(1, 'STAGE-01', 'Stage I', 'Involvement of a single lymph node region (eg, cervical, axillary, inguinal, mediastinal) or lymphoid structure such as the spleen, thymus, or Waldeyer\'s ring.'),
(2, 'STAGE-02', 'Stage II', 'Involvement of two or more lymph node regions or lymph node structures on the same side of the diaphragm. Hilar nodes should be considered to be \'lateralized\' and when involved on both sides, constitute stage II disease. For the purpose of defining the number of anatomic regions, all nodal disease within the mediastinum is considered to be a single lymph node region, and hilar involvement constitutes an additional site of involvement. The number of anatomic regions should be indicated by a subscript (eg, II-3).'),
(3, 'STAGE-03', 'Stage III', 'Involvement of lymph node regions or lymphoid structures on both sides of the diaphragm. This may be subdivided stage III-1 or III-2: stage III-1 is used for patients with involvement of the spleen or splenic hilar, celiac, or portal nodes; and stage III-2 is used for patients with involvement of the paraaortic, iliac, inguinal, or mesenteric nodes.'),
(4, 'STAGE-04', 'Stage IV', 'Diffuse or disseminated involvement of one or more extranodal organs or tissue beyond that designated E, with or without associated lymph node involvement.');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_medical_condition`
--

CREATE TABLE `voc_medical_condition` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  `AKA` varchar(250) DEFAULT NULL COMMENT 'Comma separated terms with the same or similar meaning.',
  `BROADER_TERM_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `FAMILY_HISTORY` char(1) DEFAULT NULL,
  `COMORBIDITIES` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Disorder / Medical Condition / Manifestation';

--
-- Άδειασμα δεδομένων του πίνακα `voc_medical_condition`
--

INSERT INTO `voc_medical_condition` (`ID`, `CODE`, `NAME`, `AKA`, `BROADER_TERM_ID`, `FAMILY_HISTORY`, `COMORBIDITIES`) VALUES
(1, 'COND-010000', 'Circulatory System Disease', NULL, NULL, NULL, 'X'),
(2, 'COND-011000', 'Coronary Artery Disease (CAD)', 'Ischemic heart disease, IHD', 1, NULL, 'X'),
(3, 'COND-011100', 'Myocardial Infarction (MI)', 'Heart attack', 2, NULL, 'X'),
(4, 'COND-011200', 'Angina', 'Angina pectoris', 2, NULL, 'X'),
(5, 'COND-011210', 'Stable Angina', NULL, 4, NULL, 'X'),
(6, 'COND-011220', 'Unstable Angina (UA)', NULL, 4, NULL, 'X'),
(7, 'COND-020000', 'Digestive System Disease', NULL, NULL, NULL, 'X'),
(8, 'COND-021000', 'Liver Disease', NULL, 7, NULL, 'X'),
(9, 'COND-021100', 'Autoimmune Hepatitis (AIH)', 'Lupoid Hepatitis', 8, NULL, 'X'),
(10, 'COND-021200', 'Primary Biliary Cholangitis (PBC)', 'Primary Biliary Cirrhosis', 8, NULL, 'X'),
(11, 'COND-022000', 'Celiac Disease', 'sprue, nontropical sprue, gluten-sensitive enteropathy', 7, NULL, 'X'),
(12, 'COND-023000', 'Autoimmune Gastritis', NULL, 7, NULL, 'X'),
(13, 'COND-030000', 'Endocrine System Disease', NULL, NULL, NULL, 'X'),
(14, 'COND-031000', 'Autoimmune Thyroid Disease', NULL, 13, NULL, 'X'),
(15, 'COND-032000', 'Autoimmune Adrenal Disease', NULL, 13, NULL, 'X'),
(16, 'COND-033000', 'Autoimmune Ovarian Disease', NULL, 13, NULL, 'X'),
(17, 'COND-040000', 'Cutaneous Disease', NULL, NULL, NULL, 'X'),
(18, 'COND-041000', 'Maculopapular Rash', NULL, 17, NULL, 'X'),
(19, 'COND-042000', 'Annular Rash', NULL, 17, NULL, 'X'),
(20, 'COND-043000', 'Erythema multiform (EM)', NULL, 17, NULL, 'X'),
(21, 'COND-044000', 'Urticarial Rash', 'Hives', 17, NULL, 'X'),
(22, 'COND-045000', 'Palpable Purpura', NULL, 17, NULL, 'X'),
(23, 'COND-046000', 'Vasculitic Ulcer', NULL, 17, NULL, 'X'),
(24, 'COND-047000', 'Photosensitivity', NULL, 17, NULL, 'X'),
(25, 'COND-050000', 'Lymphatic System Disease', NULL, NULL, NULL, 'X'),
(26, 'COND-051000', 'Regional Lymphadenopathy', NULL, 25, NULL, 'X'),
(27, 'COND-051100', 'Cervical Lymphadenopathy', NULL, 26, NULL, 'X'),
(28, 'COND-051200', 'Other Region Lymphadenopathy', NULL, 26, NULL, 'X'),
(29, 'COND-052000', 'Generalized Lymphadenopathy', NULL, 25, NULL, 'X'),
(30, 'COND-052100', 'Generalized Lymphadenopathy non-associated with Lymphoma', NULL, 29, NULL, 'X'),
(31, 'COND-052200', 'Lymphoma', NULL, 29, NULL, 'X'),
(32, 'COND-052300', 'Hodgkin\'s Lymphoma (HL)', NULL, 31, NULL, 'X'),
(33, 'COND-052400', 'Non-Hodgkin lymphoma (NHL) (NHL)', NULL, 31, NULL, 'X'),
(34, 'COND-052500', 'Mature B-cell neoplasms (MBCN)', NULL, 33, NULL, 'X'),
(35, 'COND-052510', 'B-cell Marginal Zone Lymphoma', NULL, 34, NULL, 'X'),
(36, 'COND-052511', 'B-cell Mucosa-associated Lymphoid Tissue (MALT) Lymphoma', 'MALT lymphoma', 35, NULL, 'X'),
(37, 'COND-052512', 'B-cell Nodal Marginal Zone Lymphoma (NMZL)', NULL, 35, NULL, 'X'),
(38, 'COND-052513', 'B-cell Splenic Marginal Zone Lymphoma (SMZL)', NULL, 35, NULL, 'X'),
(39, 'COND-052521', 'B-cell Small Lymphocytic Lymphoma / Chronic lymphocytic leukemia (CLL)', NULL, 34, NULL, 'X'),
(40, 'COND-052522', 'Diffuse Large B-cell Lymphoma (DLBCL)', NULL, 34, NULL, 'X'),
(41, 'COND-052523', 'Mantle B-cell Lymphoma (MCL)', NULL, 34, NULL, 'X'),
(42, 'COND-052524', 'T-Cell/Histiocyte-Rich B-Cell Lymphoma (THRLBCL)', NULL, 34, NULL, 'X'),
(43, 'COND-052525', 'Follicular B-cell Lymphoma', 'FO B cells lymphoma', 34, NULL, 'X'),
(44, 'COND-052526', 'Burkitt Lymphoma', NULL, 34, NULL, 'X'),
(45, 'COND-052527', 'Anaplastic large cell lymphoma (ALCL)', NULL, 34, NULL, 'X'),
(46, 'COND-052529', 'Monoclonal gammopathy of undetermined significance (MGUS)', NULL, 34, NULL, 'X'),
(47, 'COND-052531', 'Lymphoplasmacytic lymphoma (LPL)', NULL, 34, NULL, 'X'),
(48, 'COND-052532', 'EBV–positive Large B-cell Lymphoma (EBV–positive LBCL)', NULL, 34, NULL, 'X'),
(49, 'COND-052590', 'Multiple Myeloma (MM)', NULL, 34, NULL, 'X'),
(50, 'COND-052591', 'Other mature B-cell neoplasms', NULL, 34, NULL, 'X'),
(51, 'COND-052600', 'Mature T- and NK- cell neoplasms', NULL, 33, NULL, 'X'),
(52, 'COND-052700', 'Histiocytic and Dendritic cell neoplasms', NULL, 33, NULL, 'X'),
(53, 'COND-052800', 'Posttransplant lymphoproliferative disorder (PTLD)', NULL, 33, NULL, 'X'),
(54, 'COND-060000', 'Muscular System Disease', 'Myositis', NULL, NULL, 'X'),
(55, 'COND-061000', 'Idiopathic Inflammatory Myopathy (IIM)', NULL, 54, NULL, 'X'),
(56, 'COND-062000', 'Inclusion Body Myositis (IBM) documented with Biopsy', 'Inclusion-Body Myositis, IBM', 54, NULL, 'X'),
(57, 'COND-070000', 'Nervous System Disease', 'Neurological disorders', NULL, NULL, 'X'),
(58, 'COND-071000', 'Central Nervous System Disease (CNS)', NULL, 57, NULL, 'X'),
(59, 'COND-071100', 'Multiple Sclerosis (MS) like', NULL, 58, NULL, 'X'),
(60, 'COND-071200', 'Neuromyelitis Optica (NMO)', 'Devic\'s disease, Devic\'s syndrome', 58, NULL, 'X'),
(61, 'COND-071300', 'Transverse Myelitis (TM)', NULL, 58, NULL, 'X'),
(62, 'COND-071400', 'Cerebral Vasculitis', 'Central nervous system vasculitis, CNS vasculitis', 58, NULL, 'X'),
(63, 'COND-071500', 'Lymphocytic Meningitis', NULL, 58, NULL, 'X'),
(64, 'COND-071600', 'Cerebral Stroke', 'Cerebrovascular accident, CVA', 58, NULL, 'X'),
(65, 'COND-072000', 'Peripheral Nervous System Disease', NULL, 57, NULL, 'X'),
(66, 'COND-072100', 'Mononeuritis Multiplex', NULL, 65, NULL, 'X'),
(67, 'COND-072200', 'Sensorimotor Axonal Polyneuropathy', NULL, 65, NULL, 'X'),
(68, 'COND-072300', 'Painful Sensory Neuropathy', 'Small Fiber Disease', 65, NULL, 'X'),
(69, 'COND-072400', 'Sensory Ataxic Neuropathy and Autonomic Dysfunction', NULL, 65, NULL, 'X'),
(70, 'COND-072500', 'Cranial Nerve Neuropathy', NULL, 65, NULL, 'X'),
(71, 'COND-072600', 'Trigeminal Neuropathy', NULL, 65, NULL, 'X'),
(72, 'COND-080000', 'Renal / Urogenital System Disease', NULL, NULL, NULL, 'X'),
(73, 'COND-081000', 'Renal Disease', 'Kidney Disease', 72, NULL, 'X'),
(74, 'COND-081100', 'Glomerulopathy', NULL, 73, NULL, 'X'),
(75, 'COND-081110', 'Glomerulopathy Syndrome', NULL, 74, NULL, 'X'),
(76, 'COND-081111', 'Nephritic syndrome', NULL, 75, NULL, 'X'),
(77, 'COND-081112', 'Nephrotic syndrome', NULL, 75, NULL, 'X'),
(78, 'COND-081120', 'Kidney Biopsy Finding', NULL, 74, NULL, 'X'),
(79, 'COND-081121', 'Membranoproliferative Glomerulonephritis (MPGN)', 'mesangiocapillary glomerulonephritis', 78, NULL, 'X'),
(80, 'COND-081122', 'Mesangioproliferative Glomerulonephritis (MPGN)', NULL, 78, NULL, 'X'),
(81, 'COND-081123', 'Membranous Glomerulonephritis (MGN)', NULL, 78, NULL, 'X'),
(82, 'COND-081124', 'Other Glomerulonephritis', NULL, 78, NULL, 'X'),
(83, 'COND-081130', 'Proteinuria', NULL, 74, NULL, 'X'),
(84, 'COND-081140', 'Hematuria', NULL, 74, NULL, 'X'),
(85, 'COND-081200', 'Tubulointerstitial Nephritis', 'Interstitial Nephritis', 73, NULL, 'X'),
(86, 'COND-081210', 'Distal Renal Tubular Acidosis (dRTA)', 'Type 1 renal tubular acidosis, Type 1 RTA', 85, NULL, 'X'),
(87, 'COND-081211', 'Incomplete Distal Renal Tubular Acidosis (dRTA)', NULL, 86, NULL, 'X'),
(88, 'COND-081212', 'Complete Distal Renal Tubular Acidosis (dRTA)', NULL, 86, NULL, 'X'),
(89, 'COND-081220', 'Proximal Renal Tubular Acidosis (pRTA)', 'Type 2 renal tubular acidosis, Type 2 RTA', 85, NULL, 'X'),
(90, 'COND-081230', 'Fanconi Syndrome', NULL, 85, NULL, 'X'),
(91, 'COND-081240', 'Nephrogenic Diabetes Insidious (NDI)', 'Renal diabetes insipidus', 85, NULL, 'X'),
(92, 'COND-082000', 'Urogenital Disease', NULL, 72, NULL, 'X'),
(93, 'COND-082100', 'Interstitial Cystitis (IC)', 'bladder pain syndrome, BPS', 92, NULL, 'X'),
(94, 'COND-082200', 'Gynecologic Manifestations', NULL, 92, NULL, 'X'),
(95, 'COND-082210', 'Dyspareunia', NULL, 94, NULL, 'X'),
(96, 'COND-082220', 'Dryness', NULL, 94, NULL, 'X'),
(97, 'COND-082230', 'Vaginal Pruritus', NULL, 94, NULL, 'X'),
(98, 'COND-090000', 'Respiratory System Disease', NULL, NULL, NULL, 'X'),
(99, 'COND-091000', 'Pulmonary Disease', NULL, 98, NULL, 'X'),
(100, 'COND-091100', 'Small Airway Disease', NULL, 99, NULL, 'X'),
(101, 'COND-091200', 'Nonspecific Interstitial Pneumonia (NSIP)', NULL, 99, NULL, 'X'),
(102, 'COND-091300', 'Usual Interstitial Pneumonia (UIP)', NULL, 99, NULL, 'X'),
(103, 'COND-091400', 'Lymphocytic Interstitial Pneumonia (LIP)', NULL, 99, NULL, 'X'),
(104, 'COND-091500', 'Cryptogenic Organizing Pneumonia (COP)', NULL, 99, NULL, 'X'),
(105, 'COND-100000', 'Skeletal System Disease', NULL, NULL, NULL, 'X'),
(106, 'COND-101000', 'Arthritis', NULL, 105, NULL, 'X'),
(107, 'COND-101100', 'Non erosive Arthritis', NULL, 106, NULL, 'X'),
(108, 'COND-101110', 'Non erosive Arthritis affecting Hands/feet/wrist/ankles', NULL, 107, NULL, 'X'),
(109, 'COND-101120', 'Non erosive Arthritis affecting other joints', NULL, 107, NULL, 'X'),
(110, 'COND-101200', 'Erosive Arthritis', NULL, 106, NULL, 'X'),
(111, 'COND-101210', 'Erosive Arthritis affecting Hands/feet/wrist/ankles', NULL, 110, NULL, 'X'),
(112, 'COND-101220', 'Erosive Arthritis affecting other joints', NULL, 110, NULL, 'X'),
(113, 'COND-102000', 'Fibromyalgia (FM)', NULL, 105, NULL, 'X'),
(114, 'COND-110000', 'Hematologic Disease', NULL, NULL, NULL, 'X'),
(115, 'COND-111000', 'Anemia', NULL, 114, NULL, 'X'),
(116, 'COND-111100', 'Hemolytic Anemia', NULL, 115, NULL, 'X'),
(117, 'COND-111200', 'Anemia of Chronic Disease', NULL, 115, NULL, 'X'),
(118, 'COND-112000', 'Leukopenia', NULL, 114, NULL, 'X'),
(119, 'COND-113000', 'Neutropenia', NULL, 114, NULL, 'X'),
(120, 'COND-114000', 'Lymphopenia', NULL, 114, NULL, 'X'),
(121, 'COND-115000', 'Thrombocytopenia', NULL, 114, NULL, 'X'),
(122, 'COND-120000', 'Other Systemic Disorder', NULL, NULL, NULL, 'X'),
(123, 'COND-121000', 'Cryoglobulinemic vasculitis', NULL, 122, NULL, 'X'),
(124, 'COND-130000', 'Ocular / Oral Complication', NULL, NULL, NULL, 'X'),
(125, 'COND-131000', 'Cheilitis', NULL, 124, NULL, 'X'),
(126, 'COND-132000', 'Mycosis', NULL, 124, NULL, 'X'),
(127, 'COND-133000', 'Teeth Loss', 'Edentulism', 124, NULL, 'X'),
(128, 'COND-134000', 'Oral Ulcers', NULL, 124, NULL, 'X'),
(129, 'COND-135000', 'Burning Mouth Syndrome (BMS)', NULL, 124, NULL, 'X'),
(130, 'COND-136000', 'Corneal Ulcers', NULL, 124, NULL, 'X'),
(131, 'COND-140000', 'Cancer', NULL, NULL, 'X', NULL),
(132, 'COND-150000', 'Auto-immune Disease', NULL, NULL, 'X', 'X'),
(133, 'COND-150100', 'Sjogren\'s Syndrome (SS)', NULL, 132, 'X', NULL),
(134, 'COND-150200', 'Type 1 Diabetes Mellitus', NULL, 132, 'X', 'X'),
(135, 'COND-150300', 'Systemic lupus erythematosus (SLE)', NULL, 132, 'X', NULL),
(136, 'COND-150400', 'Systemic sclerosis', 'Diffuse scleroderma, Systemic sclerosis', 132, 'X', NULL),
(137, 'COND-150500', 'Idiopathic Inflammatory Myopathy (IIM)', NULL, 132, 'X', NULL),
(138, 'COND-150600', 'Mixed connective tissue disease (MCTD)', NULL, 132, 'X', NULL),
(139, 'COND-150700', 'Antiphospholipid syndrome (APS)', 'Hughes syndrome', 132, 'X', 'X'),
(140, 'COND-150800', 'Rheumatoid arthritis (RA)', NULL, 132, 'X', NULL),
(141, 'COND-150900', 'Autoimmune thyreopathy', NULL, 132, 'X', 'X'),
(142, 'COND-151000', 'Autoimmune gastritis', NULL, 132, 'X', 'X'),
(143, 'COND-151100', 'Autoimmune hepatitis', NULL, 132, 'X', 'X'),
(144, 'COND-151200', 'Primary biliary cirrhosis (PBC)', 'Primary biliary cholangitis', 132, 'X', 'X'),
(145, 'COND-151300', 'Sclerosing cholangitis', NULL, 132, 'X', 'X'),
(146, 'COND-151400', 'Celiac disease', NULL, 132, 'X', 'X'),
(147, 'COND-151500', 'Autoimmune haemolytic anemia (AIHA)', NULL, 132, 'X', 'X'),
(148, 'COND-151600', 'Autoimmune thrombocytopenia (AITP)', NULL, 132, 'X', 'X'),
(149, 'COND-151700', 'Common variable Immunodeficiency (CVID)', NULL, 132, 'X', 'X'),
(150, 'COND-151800', 'IgA deficiency (IGAD)', 'Immunoglobulin A deficiency', 132, 'X', 'X'),
(151, 'COND-151900', 'Other primary immunodeficiency', NULL, 132, 'X', 'X'),
(152, 'COND-152000', 'Psoriasis', NULL, 132, 'X', 'X'),
(153, 'COND-152100', 'Psoriatic arthritis', NULL, 132, 'X', 'X'),
(154, 'COND-152200', 'Inflammatory bowel disease (IBD)', NULL, 132, 'X', 'X'),
(155, 'COND-152300', 'Other autoimmune disease', NULL, 132, 'X', 'X');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_medical_imaging_test`
--

CREATE TABLE `voc_medical_imaging_test` (
  `ID` smallint(10) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  `OUTCOME_TYPE_ID` tinyint(3) UNSIGNED DEFAULT NULL,
  `CATEGORY_ID` smallint(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_medical_imaging_test`
--

INSERT INTO `voc_medical_imaging_test` (`ID`, `CODE`, `NAME`, `OUTCOME_TYPE_ID`, `CATEGORY_ID`) VALUES
(1, 'IMG-11', 'Parotid and Submandibular Gland Ultrasound', 3, 1),
(2, 'IMG-12', 'MRI Sialography', 3, 1),
(3, 'IMG-13', 'Salivary Gland Scintigraphy', 3, 1),
(4, 'IMG-14', 'MRI of Major Salivary Glands', 3, 1),
(5, 'IMG-21', 'Intima Media Thickness score in Carotid or Femoral Arteries', 3, 2),
(6, 'IMG-22', 'Carotid Plaque by Ultrasound', 3, 2),
(7, 'IMG-23', 'Femoral Plaque by Ultrasound', 3, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_performance_status`
--

CREATE TABLE `voc_performance_status` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ECOG Performance Status';

--
-- Άδειασμα δεδομένων του πίνακα `voc_performance_status`
--

INSERT INTO `voc_performance_status` (`ID`, `CODE`, `NAME`, `DESCRIPTION`) VALUES
(1, 'PERFORM-01', 'Grade 1', 'Restricted in physically strenuous activity but ambulatory and able to carry out work of a light or sedentary nature, e.g., light house work, office work'),
(2, 'PERFORM-02', 'Grade 2', 'Ambulatory and capable of all selfcare but unable to carry out any work activities; up and about more than 50% of waking hours'),
(3, 'PERFORM-03', 'Grade 3', 'Capable of only limited selfcare; confined to bed or chair more than 50% of waking hours'),
(4, 'PERFORM-04', 'Grade 4', 'Completely disabled; cannot carry on any selfcare; totally confined to bed or chair'),
(5, 'PERFORM-05', 'Grade 5', 'Dead'),
(6, 'PERFORM-00', 'Grade 0', 'Fully active, able to carry on all pre-disease performance without restriction');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_pharm_drug`
--

CREATE TABLE `voc_pharm_drug` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  `AKA` varchar(255) DEFAULT NULL COMMENT 'Comma separated terms with the same or similar meaning.',
  `DOSAGE_UNIT_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `BROADER_TERM_ID` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Pharmaceutical Drug or Substance';

--
-- Άδειασμα δεδομένων του πίνακα `voc_pharm_drug`
--

INSERT INTO `voc_pharm_drug` (`ID`, `CODE`, `NAME`, `AKA`, `DOSAGE_UNIT_ID`, `BROADER_TERM_ID`) VALUES
(1, 'CHEM-10000', 'Glucocorticoids (GCC)', NULL, NULL, NULL),
(2, 'CHEM-11000', 'Prednisone (PDN)', NULL, 21, 1),
(3, 'CHEM-20000', 'Disease-modifying antirheumatic drugs (DMARD)', NULL, NULL, NULL),
(4, 'CHEM-21000', 'Conventional Disease-modifying Antirheumatic Drug (cDMARD)', NULL, NULL, 3),
(5, 'CHEM-21010', 'Methotrexate (MTX)', 'amethopterin', 22, 4),
(6, 'CHEM-21020', 'Leflunomide', 'Arava', 21, 4),
(7, 'CHEM-21030', 'Azathioprine (AZA)', NULL, 21, 4),
(8, 'CHEM-21040', 'Hydroxychloroquine (HCQ)', 'Plaquenil', 21, 4),
(9, 'CHEM-21050', 'Chloroquine', NULL, 21, 4),
(10, 'CHEM-21060', 'Mycophenolate', 'prodrug mycophenolate mofetil, MMF', 21, 4),
(11, 'CHEM-21070', 'Cyclosporine', 'Ciclosporin, Cyclosporin', 21, 4),
(12, 'CHEM-21080', 'Sulphasalazine (SSZ)', 'Azulfidine', 21, 4),
(13, 'CHEM-21090', 'Colchicine', 'Colcrys, Mitigare', 21, 4),
(14, 'CHEM-21100', 'Tacrolimus', 'Fujimycin, FK506', 21, 4),
(15, 'CHEM-21110', 'Cyclophosphamide (CP)', 'cytophosphane', 23, 4),
(16, 'CHEM-21120', 'Intravenous Immunoglobulin (IVIG)', NULL, 20, 4),
(17, 'CHEM-21130', 'Therapeutic plasma exchange (TPE)', 'plasmapheresis', 25, 4),
(18, 'CHEM-22000', 'Biological Disease-modifying Antirheumatic Drug (bDMARD)', NULL, NULL, 3),
(19, 'CHEM-22100', 'Rituximab', 'Rituxan', 24, 18),
(20, 'CHEM-22200', 'Belimumab', 'Benlysta, LymphoStat-B', 23, 18),
(21, 'CHEM-22300', 'Abatacept', 'Orencia', NULL, 18),
(22, 'CHEM-22900', 'Other Biological Agent', NULL, NULL, 18),
(23, 'CHEM-22901', 'Ofatumumab', 'Arzerra, HuMax-CD20', NULL, 22),
(24, 'CHEM-22902', 'Anti-TNF (any)', NULL, NULL, 22),
(25, 'CHEM-22903', 'Anti-CD40', NULL, NULL, 22),
(26, 'CHEM-22904', 'IFNα/β inhibitor', NULL, NULL, 22),
(27, 'CHEM-22905', 'ICOS inhibitor', NULL, NULL, 22),
(28, 'CHEM-22906', 'IL-6 inhibitor', NULL, NULL, 22),
(29, 'CHEM-22907', 'Anakinra', 'Kineret', NULL, 22),
(30, 'CHEM-22908', 'JAK inhibitor', NULL, NULL, 22),
(31, 'CHEM-22909', 'Secukinumab', 'Cosentyx', NULL, 22),
(32, 'CHEM-22910', 'Ustekinumab', 'Stelara', NULL, 22),
(33, 'CHEM-30000', 'Nonsteroidal Anti-inflammatory Drug (NSAID)', NULL, NULL, NULL),
(34, 'CHEM-40000', 'Local / Dryness Treatment', NULL, NULL, NULL),
(35, 'CHEM-41000', 'Natural Tears', NULL, NULL, 34),
(36, 'CHEM-42000', 'Oral / Eye Moisturizing Agent', NULL, NULL, 34),
(37, 'CHEM-43000', 'Oral Pilocarpine', NULL, NULL, 34),
(38, 'CHEM-44000', 'Vaginal Moisturizing Agent', NULL, NULL, 34),
(39, 'CHEM-45000', 'Autologous Serum', NULL, NULL, 34),
(40, 'CHEM-46000', 'Cyclosporine Eye Drops', NULL, NULL, 34),
(41, 'CHEM-50000', 'SS Related / Co-morbidity Treatment', NULL, NULL, NULL),
(42, 'CHEM-50100', 'Calcium Supplements', NULL, NULL, 41),
(43, 'CHEM-50200', 'Vitamin D', NULL, NULL, 41),
(44, 'CHEM-50300', 'Bisphoshonates', NULL, NULL, 41),
(45, 'CHEM-50400', 'Denosumab', 'Prolia, Xgeva', NULL, 41),
(46, 'CHEM-50500', 'Teriparatide', NULL, NULL, 41),
(47, 'CHEM-50600', 'Antidepressants', NULL, NULL, 41),
(48, 'CHEM-50700', 'Thyroid Substitution Treatment', NULL, NULL, 41),
(49, 'CHEM-50800', 'Inhaled β2 Agonist', 'bronchodilators', NULL, 41),
(50, 'CHEM-50900', 'Inhaled Steroids', 'corticosteroids', NULL, 41),
(51, 'CHEM-51000', 'Ursodeoxycholic Acid (USAN)', 'ursodiol', NULL, 41),
(52, 'CHEM-51100', 'H1/H2 Inhibitor', NULL, NULL, 41),
(53, 'CHEM-51200', 'Vasodilators', NULL, NULL, 41),
(54, 'CHEM-51300', 'Oral Alkali Supplements with or without Potassium', NULL, NULL, 41),
(55, 'CHEM-51400', 'Anti-convulsive Treatment', 'antiepileptic drug, antiseizure drug', NULL, 41),
(56, 'CHEM-51500', 'Other Common Treatment', NULL, NULL, 41),
(57, 'CHEM-51501', 'Anti-hypertensive', NULL, NULL, 56),
(58, 'CHEM-51502', 'Antibiotic', NULL, NULL, 56),
(59, 'CHEM-51503', 'Lipid Lowering Agent', NULL, NULL, 56),
(60, 'CHEM-51504', 'Anti-diabetic Treatment', NULL, NULL, 56),
(61, 'CHEM-60000', 'Vaccination', NULL, NULL, NULL),
(62, 'CHEM-61000', 'Flu Vaccination', NULL, NULL, 61),
(63, 'CHEM-62000', 'Pneumococcus Vaccination', NULL, NULL, 61);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_pregnancy_outcome`
--

CREATE TABLE `voc_pregnancy_outcome` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  `DESCRIPTION` text,
  `BROADER_TERM_ID` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_pregnancy_outcome`
--

INSERT INTO `voc_pregnancy_outcome` (`ID`, `CODE`, `NAME`, `DESCRIPTION`, `BROADER_TERM_ID`) VALUES
(1, 'PREG-OUT-100', 'Single ', 'When a fetus, whatever its gestational age, exits the maternal body.', NULL),
(2, 'PREG-OUT-110', 'Single Live birth (full term or preterm birth)', 'A live birth occurs when a fetus, whatever its gestational age, exits the maternal body and subsequently shows any sign of life, such as voluntary movement, heartbeat, or pulsation of the umbilical cord, for however brief a time and regardless of whether the umbilical cord or placenta are intact.', 1),
(3, 'PREG-OUT-120', 'Single Stillbirth: Fetal death after 20 wk', 'Stillbirth is typically defined as fetal death at or after 20 to 28 weeks of pregnancy. It results in a baby born without signs of life.', 1),
(4, 'PREG-OUT-200', 'Twins', 'Twins are two offspring produced by the same pregnancy. Twins can be either monozygotic (\'identical\'), meaning that they develop from one zygote, which splits and forms two embryos, or dizygotic (\'fraternal\'), meaning that each twin develops from a separate egg and each egg is fertilized by its own sperm cell.', NULL),
(5, 'PREG-OUT-210', 'Twins, both live born', 'Two offspring produced by the same pregnancy, both of which shown any sign of life.', 4),
(6, 'PREG-OUT-211', 'Twins Live Born Monozygotic', 'They develop from one zygote, which splits and forms two embryos', 5),
(7, 'PREG-OUT-212', 'Twins Live Born Dizygotic', 'Each twin develops from a separate egg and each egg is fertilized by its own sperm cell.', 5),
(8, 'PREG-OUT-220', 'Twins, both stillborn', 'Two offspring produced by the same pregnancy, both of which born without any sign of life.', 4),
(9, 'PREG-OUT-221', 'Twins, both stillborn Monozygotic', NULL, 8),
(10, 'PREG-OUT-222', 'Twins, both stillborn Dizygotic', NULL, 8),
(11, 'PREG-OUT-230', 'Twins, one live born and one still born', 'Two offspring produced by the same pregnancy, one of which shown sings of life, while the other one born without any sign of life.', 4),
(12, 'PREG-OUT-231', 'Twins, one live born and one still born, Monozygotic', NULL, 11),
(13, 'PREG-OUT-232', 'Twins, one live born and one still born, Dizygotic', NULL, 11),
(14, 'PREG-OUT-300', 'Abortion', 'Abortion is the ending of pregnancy due to removing an embryo or fetus before it can survive outside the uterus. Death of the fetus or passage of products of conception (fetus and placenta) before 20 wk gestation', NULL),
(15, 'PREG-OUT-310', 'Spontaneous abortion / Miscarriage', 'Miscarriage, also known as spontaneous abortion and pregnancy loss, is when an abortion happens but it is not deliberate or mindful.', 14),
(16, 'PREG-OUT-320', 'Induced abortion', 'Termination of pregnancy for medical or elective reasons.', 14),
(17, 'PREG-OUT-900', 'Other, unspecified', 'Another pregnancy outcome, not covered by the other terms. (e.g., a multiple pregnancy involves more than two offspring)', NULL);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_questionnaire`
--

CREATE TABLE `voc_questionnaire` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  `ACRONYM` varchar(15) DEFAULT NULL,
  `SCORE_TYPE_ID` tinyint(3) UNSIGNED DEFAULT NULL,
  `SCORE_NOTES` varchar(255) DEFAULT NULL,
  `SCORE_NORMAL_RANGE` varchar(255) DEFAULT NULL,
  `BROADER_TERM_ID` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_questionnaire`
--

INSERT INTO `voc_questionnaire` (`ID`, `CODE`, `NAME`, `ACRONYM`, `SCORE_TYPE_ID`, `SCORE_NOTES`, `SCORE_NORMAL_RANGE`, `BROADER_TERM_ID`) VALUES
(1, 'QUEST-010', 'EULAR Sjogren\'s syndrome Disease Activity Index (ESSDAI) score', 'ESSDAI', 5, NULL, '0', NULL),
(2, 'QUEST-020', 'EULAR Sjogren\'s Syndrome Patient Reported Index (ESSPRI) score', 'ESSPRI', 5, NULL, '0', NULL),
(3, 'QUEST-021', 'EULAR Sjogren\'s Syndrome Patient Reported Index (ESSPRI) Pain score', 'ESSPRI Pain', 5, NULL, '0', 2),
(4, 'QUEST-022', 'EULAR Sjogren\'s Syndrome Patient Reported Index (ESSPRI) Fatigue score', 'ESSPRI Fatigue', 5, NULL, '0', 2),
(5, 'QUEST-023', 'EULAR Sjogren\'s Syndrome Patient Reported Index (ESSPRI) Dryness score', 'ESSPRI Dryness', 5, NULL, '0', 2),
(6, 'QUEST-030', 'Charlson Age-Comorbidity Index (CACI) score', 'CACI', 5, NULL, '0', NULL),
(7, 'QUEST-040', 'Short Form (36) Health Survey score', 'SF36', 5, NULL, '100', NULL),
(8, 'QUEST-050', 'Functional Assessment of Chronic Illness Therapy-Fatigue (FACIT-F) 13-item score', 'FACIT-F-13', 5, NULL, '0', NULL),
(9, 'QUEST-060', 'EuroQol Group - Five Dimensions - Three Level scale (EQ-5D-3L) Health State', 'EQ-5D-3L', 5, 'State: a Number of 5 digits in range 1-5', '11111', NULL),
(10, 'QUEST-070', 'International Prognostic Index (IPI) score', 'IPI', 6, 'An ID from table voc_ipi_risk', NULL, NULL),
(11, 'QUEST-080', 'Visual Analogue Scale (VAS) score', 'VAS score', NULL, NULL, NULL, NULL),
(12, 'QUEST-081', 'Oral Sicca Visual Analogue Scale (VAS) score', 'Oral-VAS', 5, NULL, '0', 11),
(13, 'QUEST-082', 'Ocular Sicca Visual Analogue Scale (VAS) score', 'Ocular-VAS', 5, NULL, '0', 11),
(14, 'QUEST-083', 'Vaginal sicca Visual Analogue Scale (VAS) score', 'Vaginal-VAS', 5, NULL, '0', 11),
(15, 'QUEST-084', 'Dyspareunia Visual Analogue Scale (VAS) score', 'Dyspareunia-VAS', 5, NULL, '0', 11),
(16, 'QUEST-085', 'Fatigue Visual Analogue Scale (VAS) score', 'Fatigue-VAS', 5, NULL, '0', 11),
(17, 'QUEST-086', 'EuroQol Group (EQ) Visual Analogue Scale (VAS) score', 'EQ-VAS', 5, NULL, '100', 11),
(18, 'QUEST-090', 'Eysenck Personality Questionnaire (EPQ) score', 'EPQ', 3, NULL, NULL, NULL),
(19, 'QUEST-091', 'Eysenck Personality Questionnaire (EPQ) - Extraversion/Introversion value', 'EPQ-E', 5, NULL, '=/< 9', 18),
(20, 'QUEST-092', 'Eysenck Personality Questionnaire (EPQ) - Neuroticism/Stability value', 'EPQ-N', 5, NULL, '=/< 12', 18),
(21, 'QUEST-093', 'Eysenck Personality Questionnaire (EPQ) - Psychoticism/Socialisation value', 'EPQ-P', 5, NULL, '=/< 2', 18),
(22, 'QUEST-094', 'Eysenck Personality Questionnaire (EPQ) - Lie/Social Desirability value', 'EPQ-L', 5, NULL, '=/< 12', 18),
(23, 'QUEST-100', 'State-Trait Anxiety Inventory (STAI) score', 'STAI', 3, NULL, NULL, NULL),
(24, 'QUEST-101', 'State-Trait Anxiety Inventory (STAI) - State Anxiety score', 'STAI-S', 5, NULL, '=/< 35', 23),
(25, 'QUEST-102', 'State-Trait Anxiety Inventory (STAI) - Trait Anxiety score', 'STAI-T', 5, NULL, '=/< 35', 23),
(26, 'QUEST-110', 'Hospital anxiety and depression scale (HADS) score', 'HADS', 3, NULL, '0', NULL),
(27, 'QUEST-111', 'HADS - Anxiety Score', 'HADS-A', 5, NULL, '0', 26),
(28, 'QUEST-112', 'HADS - Depression Score', 'HADS-D', 5, NULL, '0', 26),
(29, 'QUEST-120', 'Athens Insomnia Scale (AIS) score', 'AIS', 5, NULL, '=/< 6', NULL),
(30, 'QUEST-130', 'Zung Self-Rating Depression Scale (ZSDS) score', 'ZSDS', 5, NULL, '=/< 40', NULL),
(31, 'QUEST-140', 'Profile of Fatigue and Discomfort (PROFAD) score', 'PROFAD', 5, NULL, '0', NULL),
(32, 'QUEST-150', 'Sicca Symptoms Inventory (SSI) score', 'SSI', 5, NULL, '0', NULL),
(33, 'QUEST-160', 'Profile of Fatigue and Discomfort - Sicca Symptoms Inventory - Short Form (PROFAD-SSI-SF) score', 'PROFAD-SSI-SF', 5, NULL, '0', NULL);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_relative_degree`
--

CREATE TABLE `voc_relative_degree` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `VALUE` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_relative_degree`
--

INSERT INTO `voc_relative_degree` (`ID`, `CODE`, `VALUE`) VALUES
(1, 'REL-DEG-01', 'First Degree'),
(2, 'REL-DEG-02', 'Second Degree'),
(3, 'REL-DEG-03', 'Third Degree'),
(4, 'REL-DEG-99', 'Other');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_sex`
--

CREATE TABLE `voc_sex` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_sex`
--

INSERT INTO `voc_sex` (`ID`, `CODE`, `NAME`) VALUES
(1, 'SEX-01', 'Male'),
(2, 'SEX-02', 'Female'),
(3, 'SEX-99', 'Other');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_smoking_status`
--

CREATE TABLE `voc_smoking_status` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_smoking_status`
--

INSERT INTO `voc_smoking_status` (`ID`, `CODE`, `NAME`, `DESCRIPTION`) VALUES
(1, 'SMOK-STAT-01', 'Active ', 'The person is a Current smoker. Someone who has smoked greater than 100 cigarettes (including hand rolled cigarettes, cigars, cigarillos etc) in their lifetime and has smoked in the last 28 days.'),
(2, 'SMOK-STAT-02', 'Ex-Smoker ', 'The person was smoking in the past (being an ex-smoker) Ex-smoker is someone who has smoked greater than 100 cigarettes in their lifetime but has not smoked in the last 28 days.'),
(3, 'SMOK-STAT-03', 'Never Smoker', 'He/She has never smoked in his life. Never smoker is someone who has not smoked greater than 100 cigarettes in their lifetime and does not currently smoke.');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_specialist`
--

CREATE TABLE `voc_specialist` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_specialist`
--

INSERT INTO `voc_specialist` (`ID`, `CODE`, `NAME`) VALUES
(1, 'SPEC-01', 'Ophthalmologist'),
(2, 'SPEC-02', 'Oral medicine / dentist'),
(3, 'SPEC-03', 'Ear, Nose, and Throat (ENT)'),
(4, 'SPEC-04', 'Nephrologist'),
(5, 'SPEC-05', 'Pulmonologist'),
(6, 'SPEC-06', 'Hematologist'),
(7, 'SPEC-07', 'Gastroenterologist'),
(8, 'SPEC-08', 'Neurologist'),
(9, 'SPEC-09', 'Psychiatrist'),
(10, 'SPEC-10', 'Gynecologist'),
(11, 'SPEC-11', 'Endocrinologists'),
(12, 'SPEC-12', 'Dermatologist'),
(13, 'SPEC-99', 'Other specialist');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_symptom_sign`
--

CREATE TABLE `voc_symptom_sign` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `BROADER_TERM_ID` smallint(5) UNSIGNED DEFAULT NULL,
  `CATEGORY_ID` smallint(5) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Symptom / Sign';

--
-- Άδειασμα δεδομένων του πίνακα `voc_symptom_sign`
--

INSERT INTO `voc_symptom_sign` (`ID`, `CODE`, `NAME`, `BROADER_TERM_ID`, `CATEGORY_ID`) VALUES
(1, 'SYMPT-010', 'Parotid or Submandibular swelling', NULL, 1),
(2, 'SYMPT-011', 'Parotid Gland swelling', 1, 1),
(3, 'SYMPT-012', 'Submandibular salivary gland swelling', 1, 1),
(4, 'SYMPT-020', 'Dry Mouth (aka Xerostomia)', NULL, 1),
(5, 'SYMPT-030', 'Dry Eyes', NULL, 1),
(6, 'SYMPT-110', 'Dry Nose', NULL, 2),
(7, 'SYMPT-120', 'Dry Cough', NULL, 2),
(8, 'SYMPT-130', 'Dry Skin', NULL, 2),
(9, 'SYMPT-140', 'Fever', NULL, 2),
(10, 'SYMPT-141', 'Fever Low Grade', 9, 2),
(11, 'SYMPT-142', 'Fever > 38°C', 9, 2),
(12, 'SYMPT-150', 'Night Sweats', NULL, 2),
(13, 'SYMPT-160', 'Fatigue', NULL, 2),
(14, 'SYMPT-170', 'Weight Loss > 5% of Body Weight', NULL, 2),
(15, 'SYMPT-180', 'Raynaud\'s Phenomenon', NULL, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_unit`
--

CREATE TABLE `voc_unit` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(10) NOT NULL,
  `EXPRESSION` varchar(20) NOT NULL,
  `NAME` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_unit`
--

INSERT INTO `voc_unit` (`ID`, `CODE`, `EXPRESSION`, `NAME`) VALUES
(1, 'UNITEXP-01', '#/mm^3', 'Number / Cube Millimetre (mm^3)'),
(2, 'UNITEXP-02', '%', 'Percent (%)'),
(3, 'UNITEXP-03', 'cells/μL', 'Cells / microliter (μL)'),
(4, 'UNITEXP-04', 'g/dL', 'Gram (g) / Decilitre (dL)'),
(5, 'UNITEXP-05', 'g/L', 'Gram (g) / Litre (L)'),
(6, 'UNITEXP-06', 'IU/mL', 'International Unit (IU) / Millilitre (mL)'),
(7, 'UNITEXP-07', 'mg/24h', 'Milligrams (mg) / 24 hours (h)'),
(8, 'UNITEXP-08', 'mg/dL', 'Milligrams (mg) / Decilitre (dL)'),
(9, 'UNITEXP-09', 'mg/L', 'Milligrams (mg) / Litre (L)'),
(10, 'UNITEXP-10', 'mL/15min', 'Millilitre (mL) / 15 minutes (min)'),
(11, 'UNITEXP-11', 'mm/5min', 'Millimetre (mm) / 5 minutes (min)'),
(12, 'UNITEXP-12', 'ng/mL', 'Nanogram (ng) / Millilitre (mL)'),
(13, 'UNITEXP-13', 'pg/mL', 'Picogram (pg) / Millilitre (mL)'),
(14, 'UNITEXP-14', 'pH', 'pH'),
(15, 'UNITEXP-15', 'μg/L', 'Micrograms (μg) / Litre (L)'),
(16, 'UNITEXP-16', 'μg/mL', 'Micrograms (μg) / Millilitre (mL)'),
(17, 'UNITEXP-17', 'μmol/L', 'Micromole (μmol) / Litre (L)'),
(18, 'UNITEXP-18', 'cigar-pack/year', 'Packets of cigarettes / Year'),
(19, 'UNITEXP-19', '#', 'Number (#)'),
(20, 'UNITEXP-20', 'mgr/Kg of BW/month', 'Milligrams (mg) / Kilograms (Kg) of BW / month'),
(21, 'UNITEXP-21', 'mg/day', 'Milligrams (mg) / Day'),
(22, 'UNITEXP-22', 'mg/week', 'Milligrams (mg) / Week'),
(23, 'UNITEXP-23', 'mg/month', 'Milligrams (mg) / Month'),
(24, 'UNITEXP-31', 'cycle/12-month', 'Cycles / 12-months'),
(25, 'UNITEXP-32', 'session numbers', 'Session Numbers'),
(26, 'UNITEXP-33', 'mEq/L', 'Milliequivalents (mEq) / Liter (L)'),
(27, 'UNITEXP-34', 'IU/L', '');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_visit_status`
--

CREATE TABLE `voc_visit_status` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_visit_status`
--

INSERT INTO `voc_visit_status` (`ID`, `CODE`, `NAME`, `DESCRIPTION`) VALUES
(1, 'VIS-STAT-01', 'Visit \'Open\'', 'Additional information can be recoded for a specific visit.'),
(2, 'VIS-STAT-02', 'Visit \'Closed\'', 'No other information can be recorded for a patient, as part of the current visit.');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `voc_visit_type`
--

CREATE TABLE `voc_visit_type` (
  `ID` smallint(5) UNSIGNED NOT NULL,
  `CODE` varchar(15) NOT NULL,
  `NAME` varchar(150) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Άδειασμα δεδομένων του πίνακα `voc_visit_type`
--

INSERT INTO `voc_visit_type` (`ID`, `CODE`, `NAME`, `DESCRIPTION`) VALUES
(1, 'VISIT-00', 'Data Harmonization Process', NULL),
(2, 'VISIT-01', 'Cohort inclusion (punctual)', NULL),
(3, 'VISIT-02', 'Ever', 'At any time during all the period from Cohort inclusion and the last visit during the HarmonicSS study (recorded as punctual point, but this records all the data if ever occurred, or the worst one , during the whole period)'),
(4, 'VISIT-03', 'Running at any other point (punctual)', NULL),
(5, 'VISIT-04', 'Last One (punctual)', NULL);

--
-- Ευρετήρια για άχρηστους πίνακες
--

--
-- Ευρετήρια για πίνακα `cohort`
--
ALTER TABLE `cohort`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `PARAMETER` (`PARAMETER`);

--
-- Ευρετήρια για πίνακα `cond_diagnosis`
--
ALTER TABLE `cond_diagnosis`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `CONDITION_ID` (`CONDITION_ID`),
  ADD KEY `DATE_ID` (`DIAGNOSIS_DATE_ID`),
  ADD KEY `STAGE_ID` (`STAGE_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `PERFORMANCE_STATUS_ID` (`PERFORMANCE_STATUS_ID`),
  ADD KEY `STMT_ID` (`STMT_ID`);

--
-- Ευρετήρια για πίνακα `cond_diagnosis_organs`
--
ALTER TABLE `cond_diagnosis_organs`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ORGAN_ID` (`ORGAN_ID`),
  ADD KEY `DIAGNOSIS_ID` (`DIAGNOSIS_ID`);

--
-- Ευρετήρια για πίνακα `cond_link_diagnosis_lab_tests`
--
ALTER TABLE `cond_link_diagnosis_lab_tests`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `DIAGNOSIS_ID` (`DIAGNOSIS_ID`),
  ADD KEY `LAB_TEST_ID` (`LAB_TEST_ID`);

--
-- Ευρετήρια για πίνακα `cond_link_diagnosis_symptoms`
--
ALTER TABLE `cond_link_diagnosis_symptoms`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `DIAGNOSIS_ID` (`DIAGNOSIS_ID`),
  ADD KEY `SYMPTOMS_ID` (`SYMPTOMS_ID`);

--
-- Ευρετήρια για πίνακα `cond_symptom`
--
ALTER TABLE `cond_symptom`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `CONDITION_ID` (`CONDITION_ID`),
  ADD KEY `DATE_ID` (`OBSERVE_DATE_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `STMT_ID` (`STMT_ID`);

--
-- Ευρετήρια για πίνακα `demo_education_level_data`
--
ALTER TABLE `demo_education_level_data`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `EDUCATION_LEVEL_ID` (`EDUCATION_LEVEL_ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`);

--
-- Ευρετήρια για πίνακα `demo_ethnicity_data`
--
ALTER TABLE `demo_ethnicity_data`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ETHNICITY_ID` (`ETHNICITY_ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`);

--
-- Ευρετήρια για πίνακα `demo_occupation_data`
--
ALTER TABLE `demo_occupation_data`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `LOSS_OF_WORK_DUE_TO_PSS_ID` (`LOSS_OF_WORK_DUE_TO_PSS_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`);

--
-- Ευρετήρια για πίνακα `demo_pregnancy_data`
--
ALTER TABLE `demo_pregnancy_data`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `CONCEPTION_DATE_ID` (`CONCEPTION_DATE_ID`),
  ADD KEY `OUTCOME_DATE_ID` (`OUTCOME_DATE_ID`),
  ADD KEY `OUTCOME_ID` (`OUTCOME_ID`),
  ADD KEY `SS_CONCORDANT_ID` (`SS_CONCORDANT_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `STMT_ID` (`STMT_ID`);

--
-- Ευρετήρια για πίνακα `demo_sex_data`
--
ALTER TABLE `demo_sex_data`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `SEX_ID` (`SEX_ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`);

--
-- Ευρετήρια για πίνακα `demo_weight_data`
--
ALTER TABLE `demo_weight_data`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `BMI_CLASS_ID` (`BMI_CLASS_ID`);

--
-- Ευρετήρια για πίνακα `dt_amount`
--
ALTER TABLE `dt_amount`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `UNIT_ID` (`UNIT_ID`),
  ADD KEY `OP_ID` (`OP_ID`);

--
-- Ευρετήρια για πίνακα `dt_amount_range`
--
ALTER TABLE `dt_amount_range`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `UNIT_ID` (`UNIT_ID`);

--
-- Ευρετήρια για πίνακα `dt_date`
--
ALTER TABLE `dt_date`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `OP_ID` (`OP_ID`);

--
-- Ευρετήρια για πίνακα `dt_int_range`
--
ALTER TABLE `dt_int_range`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `dt_period_of_time`
--
ALTER TABLE `dt_period_of_time`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `START_DATE_ID` (`START_DATE_ID`),
  ADD KEY `END_DATE_ID` (`END_DATE_ID`),
  ADD KEY `EXACT_ID` (`EXACT_ID`),
  ADD KEY `BEFORE_PERIOD_ID` (`BEFORE_PERIOD_ID`);

--
-- Ευρετήρια για πίνακα `exam_biopsy`
--
ALTER TABLE `exam_biopsy`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `BIOSPY_ID` (`BIOPSY_ID`),
  ADD KEY `SAMPLE_DATE_ID` (`BIOPSY_DATE_ID`),
  ADD KEY `ASSESSMENT_ID` (`ASSESSMENT_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `TEST_ID` (`TEST_ID`),
  ADD KEY `OUTCOME_AMOUNT_ID` (`OUTCOME_AMOUNT_ID`),
  ADD KEY `NORMAL_RANGE_ID` (`NORMAL_RANGE_ID`),
  ADD KEY `OUTCOME_CHECK_ID` (`OUTCOME_CHECK_ID`);

--
-- Ευρετήρια για πίνακα `exam_caci_condition`
--
ALTER TABLE `exam_caci_condition`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `CACI_ID` (`CACI_ID`),
  ADD KEY `DATE_ID` (`QUESTIONNAIRE_DATE_ID`),
  ADD KEY `VALUE_ID` (`VALUE_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`);

--
-- Ευρετήρια για πίνακα `exam_essdai_domain`
--
ALTER TABLE `exam_essdai_domain`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `DOMAIN_ID` (`DOMAIN_ID`),
  ADD KEY `DATE_ID` (`QUESTIONNAIRE_DATE_ID`),
  ADD KEY `ACTIVITY_LEVEL_ID` (`ACTIVITY_LEVEL_ID`);

--
-- Ευρετήρια για πίνακα `exam_lab_test`
--
ALTER TABLE `exam_lab_test`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `SAMPLE_DATE_ID` (`SAMPLE_DATE_ID`),
  ADD KEY `ASSESSMENT_ID` (`OUTCOME_ASSESSMENT_ID`),
  ADD KEY `NORMAL_RANGE_ID` (`NORMAL_RANGE_ID`),
  ADD KEY `TEST_ID` (`TEST_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `OUTCOME_AMOUNT_ID` (`OUTCOME_AMOUNT_ID`);

--
-- Ευρετήρια για πίνακα `exam_medical_imaging_test`
--
ALTER TABLE `exam_medical_imaging_test`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `TEST_ID` (`TEST_ID`),
  ADD KEY `DATE_ID` (`TEST_DATE_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `ASSESSMENT_ID` (`ASSESSMENT_ID`);

--
-- Ευρετήρια για πίνακα `exam_questionnaire_score`
--
ALTER TABLE `exam_questionnaire_score`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `SCORE_ID` (`SCORE_ID`),
  ADD KEY `DATE_ID` (`QUESTIONNAIRE_DATE_ID`),
  ADD KEY `ASSESSMENT_ID` (`ASSESSMENT_ID`),
  ADD KEY `NORMAL_RANGE_ID` (`NORMAL_RANGE_ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`);

--
-- Ευρετήρια για πίνακα `interv_chemotherapy`
--
ALTER TABLE `interv_chemotherapy`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `PERIOD_OF_TIME_ID` (`PERIOD_ID`),
  ADD KEY `DUE_TO_PSS_ID` (`DUE_TO_PSS_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `STMT_ID` (`STMT_ID`);

--
-- Ευρετήρια για πίνακα `interv_medication`
--
ALTER TABLE `interv_medication`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `MEDICATION_ID` (`MEDICATION_ID`),
  ADD KEY `DOSAGE_ID` (`DOSAGE_ID`),
  ADD KEY `PERIOD_OF_TIME_ID` (`PERIOD_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `STMT_ID` (`STMT_ID`);

--
-- Ευρετήρια για πίνακα `interv_surgery`
--
ALTER TABLE `interv_surgery`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `DATE_ID` (`SURGERY_DATE_ID`),
  ADD KEY `DUE_TO_PSS_ID` (`DUE_TO_PSS_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `STMT_ID` (`STMT_ID`);

--
-- Ευρετήρια για πίνακα `lifestyle_smoking`
--
ALTER TABLE `lifestyle_smoking`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `STATUS_ID` (`STATUS_ID`),
  ADD KEY `AMOUNT_ID` (`AMOUNT_ID`),
  ADD KEY `PERIOD_ID` (`PERIOD_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `STMT_ID` (`STMT_ID`);

--
-- Ευρετήρια για πίνακα `other_clinical_trials`
--
ALTER TABLE `other_clinical_trials`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `PERIOD_OF_TIME` (`PERIOD_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `STMT_ID` (`STMT_ID`);

--
-- Ευρετήρια για πίνακα `other_family_history`
--
ALTER TABLE `other_family_history`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `MEDICAL_CONDITION_ID` (`MEDICAL_CONDITION_ID`),
  ADD KEY `RELATIONSHIP_DEGREE_ID` (`RELATIVE_DEGREE_ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`),
  ADD KEY `STMT_ID` (`STMT_ID`);

--
-- Ευρετήρια για πίνακα `other_healthcare_visit`
--
ALTER TABLE `other_healthcare_visit`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `VISIT_DATE_ID` (`DATE_ID`),
  ADD KEY `SPECIALIST_ID` (`SPECIALIST_ID`),
  ADD KEY `VISIT_ID` (`VISIT_ID`);

--
-- Ευρετήρια για πίνακα `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `UID_Index` (`UID`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `PSS_SYMPTOMS_ONSET_DATE_ID` (`PSS_SYMPTOMS_ONSET_DATE_ID`),
  ADD KEY `PSS_DIAGNOSIS_DATE_ID` (`PSS_DIAGNOSIS_DATE_ID`),
  ADD KEY `COHORT_INCLUSION_DATE_ID` (`COHORT_INCLUSION_DATE_ID`),
  ADD KEY `DATE_OF_BIRTH_ID` (`DATE_OF_BIRTH_ID`);

--
-- Ευρετήρια για πίνακα `patient_visit`
--
ALTER TABLE `patient_visit`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `PATIENT_ID` (`PATIENT_ID`),
  ADD KEY `VISIT_TYPE_ID` (`VISIT_TYPE_ID`),
  ADD KEY `VISIT_STATUS_ID` (`VISIT_STATUS_ID`),
  ADD KEY `VISIT_DATE_ID` (`VISIT_DATE_ID`);

--
-- Ευρετήρια για πίνακα `voc_activity_level`
--
ALTER TABLE `voc_activity_level`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_ana_pattern`
--
ALTER TABLE `voc_ana_pattern`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_assessment`
--
ALTER TABLE `voc_assessment`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `BROADER_TERM_ID` (`BROADER_TERM_ID`);

--
-- Ευρετήρια για πίνακα `voc_biopsy`
--
ALTER TABLE `voc_biopsy`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `BROADER_TERM_ID` (`BROADER_TERM_ID`);

--
-- Ευρετήρια για πίνακα `voc_bmi_class`
--
ALTER TABLE `voc_bmi_class`
  ADD PRIMARY KEY (`ID`);

--
-- Ευρετήρια για πίνακα `voc_caci_condition`
--
ALTER TABLE `voc_caci_condition`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_confirmation`
--
ALTER TABLE `voc_confirmation`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD UNIQUE KEY `Acronym_Index` (`ACRONYM`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_cryo_type`
--
ALTER TABLE `voc_cryo_type`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_direction`
--
ALTER TABLE `voc_direction`
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD UNIQUE KEY `Acronym_Index` (`ACRONYM`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_education_level`
--
ALTER TABLE `voc_education_level`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_essdai_domain`
--
ALTER TABLE `voc_essdai_domain`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `EXCLUDE_ACTIVITY_LEVEL` (`EXCLUDE_ACTIVITY_LEVEL`);

--
-- Ευρετήρια για πίνακα `voc_ethnicity`
--
ALTER TABLE `voc_ethnicity`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_exam_outcome_type`
--
ALTER TABLE `voc_exam_outcome_type`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD UNIQUE KEY `Acronym_Index` (`NOTES`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_ipi_risk`
--
ALTER TABLE `voc_ipi_risk`
  ADD PRIMARY KEY (`ID`);

--
-- Ευρετήρια για πίνακα `voc_lab_test`
--
ALTER TABLE `voc_lab_test`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `UNIT_ID` (`UNIT_ID`),
  ADD KEY `OUTCOME_VALUE_ID` (`OUTCOME_VALUE_ID`),
  ADD KEY `BROADER_TERM_ID` (`BROADER_TERM_ID`),
  ADD KEY `LAB_TEST_TYPE_ID` (`LAB_TEST_TYPE_ID`);

--
-- Ευρετήρια για πίνακα `voc_lab_test_type`
--
ALTER TABLE `voc_lab_test_type`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_lymphoma_organ`
--
ALTER TABLE `voc_lymphoma_organ`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `BROADER_TERM_ID` (`BROADER_TERM_ID`);

--
-- Ευρετήρια για πίνακα `voc_lymphoma_stage`
--
ALTER TABLE `voc_lymphoma_stage`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_medical_condition`
--
ALTER TABLE `voc_medical_condition`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `BROADER_TERM` (`BROADER_TERM_ID`);

--
-- Ευρετήρια για πίνακα `voc_medical_imaging_test`
--
ALTER TABLE `voc_medical_imaging_test`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `OUTCOME_TYPE_ID` (`OUTCOME_TYPE_ID`);

--
-- Ευρετήρια για πίνακα `voc_performance_status`
--
ALTER TABLE `voc_performance_status`
  ADD PRIMARY KEY (`ID`);

--
-- Ευρετήρια για πίνακα `voc_pharm_drug`
--
ALTER TABLE `voc_pharm_drug`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `BROADER_TERM_ID` (`BROADER_TERM_ID`),
  ADD KEY `UNIT_ID` (`DOSAGE_UNIT_ID`),
  ADD KEY `Acronym_Index` (`AKA`);

--
-- Ευρετήρια για πίνακα `voc_pregnancy_outcome`
--
ALTER TABLE `voc_pregnancy_outcome`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Iindex` (`ID`),
  ADD KEY `BROADER_TERM_ID` (`BROADER_TERM_ID`);

--
-- Ευρετήρια για πίνακα `voc_questionnaire`
--
ALTER TABLE `voc_questionnaire`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Acronym_Index` (`ACRONYM`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `BROADER_TERM_ID` (`BROADER_TERM_ID`),
  ADD KEY `SCORE_TYPE_ID` (`SCORE_TYPE_ID`);

--
-- Ευρετήρια για πίνακα `voc_relative_degree`
--
ALTER TABLE `voc_relative_degree`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_sex`
--
ALTER TABLE `voc_sex`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_smoking_status`
--
ALTER TABLE `voc_smoking_status`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_specialist`
--
ALTER TABLE `voc_specialist`
  ADD PRIMARY KEY (`ID`);

--
-- Ευρετήρια για πίνακα `voc_symptom_sign`
--
ALTER TABLE `voc_symptom_sign`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD KEY `ID_Index` (`ID`),
  ADD KEY `BROADER_TERM_ID` (`BROADER_TERM_ID`);

--
-- Ευρετήρια για πίνακα `voc_unit`
--
ALTER TABLE `voc_unit`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Code_Index` (`CODE`),
  ADD UNIQUE KEY `Expression_Index` (`EXPRESSION`),
  ADD KEY `ID_Index` (`ID`);

--
-- Ευρετήρια για πίνακα `voc_visit_status`
--
ALTER TABLE `voc_visit_status`
  ADD PRIMARY KEY (`ID`);

--
-- Ευρετήρια για πίνακα `voc_visit_type`
--
ALTER TABLE `voc_visit_type`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT για άχρηστους πίνακες
--

--
-- AUTO_INCREMENT για πίνακα `cohort`
--
ALTER TABLE `cohort`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=155;

--
-- AUTO_INCREMENT για πίνακα `cond_diagnosis`
--
ALTER TABLE `cond_diagnosis`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=158140;

--
-- AUTO_INCREMENT για πίνακα `cond_diagnosis_organs`
--
ALTER TABLE `cond_diagnosis_organs`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT για πίνακα `cond_link_diagnosis_lab_tests`
--
ALTER TABLE `cond_link_diagnosis_lab_tests`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT για πίνακα `cond_link_diagnosis_symptoms`
--
ALTER TABLE `cond_link_diagnosis_symptoms`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT για πίνακα `cond_symptom`
--
ALTER TABLE `cond_symptom`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40992;

--
-- AUTO_INCREMENT για πίνακα `demo_education_level_data`
--
ALTER TABLE `demo_education_level_data`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5826;

--
-- AUTO_INCREMENT για πίνακα `demo_ethnicity_data`
--
ALTER TABLE `demo_ethnicity_data`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7016;

--
-- AUTO_INCREMENT για πίνακα `demo_occupation_data`
--
ALTER TABLE `demo_occupation_data`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5826;

--
-- AUTO_INCREMENT για πίνακα `demo_pregnancy_data`
--
ALTER TABLE `demo_pregnancy_data`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6981;

--
-- AUTO_INCREMENT για πίνακα `demo_sex_data`
--
ALTER TABLE `demo_sex_data`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8593;

--
-- AUTO_INCREMENT για πίνακα `demo_weight_data`
--
ALTER TABLE `demo_weight_data`
  MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5071;

--
-- AUTO_INCREMENT για πίνακα `dt_amount`
--
ALTER TABLE `dt_amount`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=227718;

--
-- AUTO_INCREMENT για πίνακα `dt_amount_range`
--
ALTER TABLE `dt_amount_range`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35787;

--
-- AUTO_INCREMENT για πίνακα `dt_date`
--
ALTER TABLE `dt_date`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=860345;

--
-- AUTO_INCREMENT για πίνακα `dt_int_range`
--
ALTER TABLE `dt_int_range`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11829;

--
-- AUTO_INCREMENT για πίνακα `dt_period_of_time`
--
ALTER TABLE `dt_period_of_time`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=90316;

--
-- AUTO_INCREMENT για πίνακα `exam_biopsy`
--
ALTER TABLE `exam_biopsy`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15085;

--
-- AUTO_INCREMENT για πίνακα `exam_caci_condition`
--
ALTER TABLE `exam_caci_condition`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT για πίνακα `exam_essdai_domain`
--
ALTER TABLE `exam_essdai_domain`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61619;

--
-- AUTO_INCREMENT για πίνακα `exam_lab_test`
--
ALTER TABLE `exam_lab_test`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=365614;

--
-- AUTO_INCREMENT για πίνακα `exam_medical_imaging_test`
--
ALTER TABLE `exam_medical_imaging_test`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT για πίνακα `exam_questionnaire_score`
--
ALTER TABLE `exam_questionnaire_score`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73605;

--
-- AUTO_INCREMENT για πίνακα `interv_chemotherapy`
--
ALTER TABLE `interv_chemotherapy`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11632;

--
-- AUTO_INCREMENT για πίνακα `interv_medication`
--
ALTER TABLE `interv_medication`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81317;

--
-- AUTO_INCREMENT για πίνακα `interv_surgery`
--
ALTER TABLE `interv_surgery`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11619;

--
-- AUTO_INCREMENT για πίνακα `lifestyle_smoking`
--
ALTER TABLE `lifestyle_smoking`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6839;

--
-- AUTO_INCREMENT για πίνακα `other_clinical_trials`
--
ALTER TABLE `other_clinical_trials`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5804;

--
-- AUTO_INCREMENT για πίνακα `other_family_history`
--
ALTER TABLE `other_family_history`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT για πίνακα `other_healthcare_visit`
--
ALTER TABLE `other_healthcare_visit`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT για πίνακα `patient`
--
ALTER TABLE `patient`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8626;

--
-- AUTO_INCREMENT για πίνακα `patient_visit`
--
ALTER TABLE `patient_visit`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8620;

--
-- AUTO_INCREMENT για πίνακα `voc_activity_level`
--
ALTER TABLE `voc_activity_level`
  MODIFY `ID` smallint(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT για πίνακα `voc_ana_pattern`
--
ALTER TABLE `voc_ana_pattern`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT για πίνακα `voc_assessment`
--
ALTER TABLE `voc_assessment`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT για πίνακα `voc_biopsy`
--
ALTER TABLE `voc_biopsy`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT για πίνακα `voc_bmi_class`
--
ALTER TABLE `voc_bmi_class`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT για πίνακα `voc_caci_condition`
--
ALTER TABLE `voc_caci_condition`
  MODIFY `ID` smallint(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT για πίνακα `voc_confirmation`
--
ALTER TABLE `voc_confirmation`
  MODIFY `ID` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT για πίνακα `voc_cryo_type`
--
ALTER TABLE `voc_cryo_type`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT για πίνακα `voc_education_level`
--
ALTER TABLE `voc_education_level`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT για πίνακα `voc_essdai_domain`
--
ALTER TABLE `voc_essdai_domain`
  MODIFY `ID` smallint(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT για πίνακα `voc_ethnicity`
--
ALTER TABLE `voc_ethnicity`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT για πίνακα `voc_exam_outcome_type`
--
ALTER TABLE `voc_exam_outcome_type`
  MODIFY `ID` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT για πίνακα `voc_ipi_risk`
--
ALTER TABLE `voc_ipi_risk`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT για πίνακα `voc_lab_test`
--
ALTER TABLE `voc_lab_test`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=514;

--
-- AUTO_INCREMENT για πίνακα `voc_lab_test_type`
--
ALTER TABLE `voc_lab_test_type`
  MODIFY `ID` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT για πίνακα `voc_lymphoma_organ`
--
ALTER TABLE `voc_lymphoma_organ`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT για πίνακα `voc_lymphoma_stage`
--
ALTER TABLE `voc_lymphoma_stage`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT για πίνακα `voc_medical_condition`
--
ALTER TABLE `voc_medical_condition`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=156;

--
-- AUTO_INCREMENT για πίνακα `voc_medical_imaging_test`
--
ALTER TABLE `voc_medical_imaging_test`
  MODIFY `ID` smallint(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT για πίνακα `voc_performance_status`
--
ALTER TABLE `voc_performance_status`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT για πίνακα `voc_pharm_drug`
--
ALTER TABLE `voc_pharm_drug`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT για πίνακα `voc_pregnancy_outcome`
--
ALTER TABLE `voc_pregnancy_outcome`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT για πίνακα `voc_questionnaire`
--
ALTER TABLE `voc_questionnaire`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT για πίνακα `voc_relative_degree`
--
ALTER TABLE `voc_relative_degree`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT για πίνακα `voc_sex`
--
ALTER TABLE `voc_sex`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT για πίνακα `voc_smoking_status`
--
ALTER TABLE `voc_smoking_status`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT για πίνακα `voc_specialist`
--
ALTER TABLE `voc_specialist`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT για πίνακα `voc_symptom_sign`
--
ALTER TABLE `voc_symptom_sign`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT για πίνακα `voc_unit`
--
ALTER TABLE `voc_unit`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT για πίνακα `voc_visit_status`
--
ALTER TABLE `voc_visit_status`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT για πίνακα `voc_visit_type`
--
ALTER TABLE `voc_visit_type`
  MODIFY `ID` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Περιορισμοί για άχρηστους πίνακες
--

--
-- Περιορισμοί για πίνακα `cond_diagnosis`
--
ALTER TABLE `cond_diagnosis`
  ADD CONSTRAINT `cond_diagnosis_fk_stmt_id` FOREIGN KEY (`STMT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `cond_diagnosis_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `cond_diagnosis_ibfk_2` FOREIGN KEY (`CONDITION_ID`) REFERENCES `voc_medical_condition` (`ID`),
  ADD CONSTRAINT `cond_diagnosis_ibfk_3` FOREIGN KEY (`DIAGNOSIS_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `cond_diagnosis_ibfk_4` FOREIGN KEY (`STAGE_ID`) REFERENCES `voc_lymphoma_stage` (`ID`),
  ADD CONSTRAINT `cond_diagnosis_ibfk_5` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`),
  ADD CONSTRAINT `cond_diagnosis_ibfk_6` FOREIGN KEY (`PERFORMANCE_STATUS_ID`) REFERENCES `voc_performance_status` (`ID`);

--
-- Περιορισμοί για πίνακα `cond_diagnosis_organs`
--
ALTER TABLE `cond_diagnosis_organs`
  ADD CONSTRAINT `cond_diagnosis_organs_ibfk_1` FOREIGN KEY (`ORGAN_ID`) REFERENCES `voc_lymphoma_organ` (`ID`),
  ADD CONSTRAINT `cond_diagnosis_organs_ibfk_2` FOREIGN KEY (`DIAGNOSIS_ID`) REFERENCES `cond_diagnosis` (`ID`);

--
-- Περιορισμοί για πίνακα `cond_link_diagnosis_lab_tests`
--
ALTER TABLE `cond_link_diagnosis_lab_tests`
  ADD CONSTRAINT `cond_link_diagnosis_lab_tests_ibfk_1` FOREIGN KEY (`DIAGNOSIS_ID`) REFERENCES `cond_diagnosis` (`ID`),
  ADD CONSTRAINT `cond_link_diagnosis_lab_tests_ibfk_2` FOREIGN KEY (`LAB_TEST_ID`) REFERENCES `exam_lab_test` (`ID`);

--
-- Περιορισμοί για πίνακα `cond_link_diagnosis_symptoms`
--
ALTER TABLE `cond_link_diagnosis_symptoms`
  ADD CONSTRAINT `cond_link_diagnosis_symptoms_ibfk_1` FOREIGN KEY (`DIAGNOSIS_ID`) REFERENCES `cond_diagnosis` (`ID`),
  ADD CONSTRAINT `cond_link_diagnosis_symptoms_ibfk_2` FOREIGN KEY (`SYMPTOMS_ID`) REFERENCES `cond_symptom` (`ID`);

--
-- Περιορισμοί για πίνακα `cond_symptom`
--
ALTER TABLE `cond_symptom`
  ADD CONSTRAINT `cond_symptom_fk_stmt_id` FOREIGN KEY (`STMT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `cond_symptom_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `cond_symptom_ibfk_2` FOREIGN KEY (`CONDITION_ID`) REFERENCES `voc_symptom_sign` (`ID`),
  ADD CONSTRAINT `cond_symptom_ibfk_3` FOREIGN KEY (`OBSERVE_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `cond_symptom_ibfk_4` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `demo_education_level_data`
--
ALTER TABLE `demo_education_level_data`
  ADD CONSTRAINT `demo_education_level_data_ibfk_3` FOREIGN KEY (`EDUCATION_LEVEL_ID`) REFERENCES `voc_education_level` (`ID`),
  ADD CONSTRAINT `demo_education_level_data_ibfk_4` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `demo_education_level_data_ibfk_6` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `demo_ethnicity_data`
--
ALTER TABLE `demo_ethnicity_data`
  ADD CONSTRAINT `demo_ethnicity_data_ibfk_3` FOREIGN KEY (`ETHNICITY_ID`) REFERENCES `voc_ethnicity` (`ID`),
  ADD CONSTRAINT `demo_ethnicity_data_ibfk_4` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `demo_ethnicity_data_ibfk_6` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `demo_occupation_data`
--
ALTER TABLE `demo_occupation_data`
  ADD CONSTRAINT `demo_occupation_data_data_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `demo_occupation_data_ibfk_1` FOREIGN KEY (`LOSS_OF_WORK_DUE_TO_PSS_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `demo_occupation_data_ibfk_3` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `demo_pregnancy_data`
--
ALTER TABLE `demo_pregnancy_data`
  ADD CONSTRAINT `demo_pregnancy_data_fk_outcome_id` FOREIGN KEY (`OUTCOME_ID`) REFERENCES `voc_pregnancy_outcome` (`ID`),
  ADD CONSTRAINT `demo_pregnancy_data_fk_stmt_id` FOREIGN KEY (`STMT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `demo_pregnancy_data_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `demo_pregnancy_data_ibfk_2` FOREIGN KEY (`CONCEPTION_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `demo_pregnancy_data_ibfk_3` FOREIGN KEY (`OUTCOME_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `demo_pregnancy_data_ibfk_5` FOREIGN KEY (`SS_CONCORDANT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `demo_pregnancy_data_ibfk_6` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `demo_sex_data`
--
ALTER TABLE `demo_sex_data`
  ADD CONSTRAINT `demo_sex_data_ibfk_3` FOREIGN KEY (`SEX_ID`) REFERENCES `voc_sex` (`ID`),
  ADD CONSTRAINT `demo_sex_data_ibfk_4` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `demo_sex_data_ibfk_6` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `demo_weight_data`
--
ALTER TABLE `demo_weight_data`
  ADD CONSTRAINT `demo_weight_data_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `demo_weight_data_ibfk_2` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`),
  ADD CONSTRAINT `demo_weight_data_ibfk_3` FOREIGN KEY (`BMI_CLASS_ID`) REFERENCES `voc_bmi_class` (`ID`);

--
-- Περιορισμοί για πίνακα `dt_amount`
--
ALTER TABLE `dt_amount`
  ADD CONSTRAINT `dt_amount_fk_op_id` FOREIGN KEY (`OP_ID`) REFERENCES `voc_direction` (`ID`),
  ADD CONSTRAINT `dt_amount_ibfk_1` FOREIGN KEY (`UNIT_ID`) REFERENCES `voc_unit` (`ID`);

--
-- Περιορισμοί για πίνακα `dt_amount_range`
--
ALTER TABLE `dt_amount_range`
  ADD CONSTRAINT `dt_amount_range_ibfk_1` FOREIGN KEY (`UNIT_ID`) REFERENCES `voc_unit` (`ID`);

--
-- Περιορισμοί για πίνακα `dt_date`
--
ALTER TABLE `dt_date`
  ADD CONSTRAINT `dt_date_ibfk_1` FOREIGN KEY (`OP_ID`) REFERENCES `voc_direction` (`ID`);

--
-- Περιορισμοί για πίνακα `dt_period_of_time`
--
ALTER TABLE `dt_period_of_time`
  ADD CONSTRAINT `dt_period_of_time_ibfk_1` FOREIGN KEY (`START_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `dt_period_of_time_ibfk_2` FOREIGN KEY (`END_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `dt_period_of_time_ibfk_3` FOREIGN KEY (`EXACT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `dt_period_of_time_ibfk_4` FOREIGN KEY (`BEFORE_PERIOD_ID`) REFERENCES `dt_period_of_time` (`ID`);

--
-- Περιορισμοί για πίνακα `exam_biopsy`
--
ALTER TABLE `exam_biopsy`
  ADD CONSTRAINT `exam_biopsy_fk_biopsy_id` FOREIGN KEY (`BIOPSY_ID`) REFERENCES `voc_biopsy` (`ID`),
  ADD CONSTRAINT `exam_biopsy_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `exam_biopsy_ibfk_10` FOREIGN KEY (`OUTCOME_AMOUNT_ID`) REFERENCES `dt_amount` (`ID`),
  ADD CONSTRAINT `exam_biopsy_ibfk_11` FOREIGN KEY (`NORMAL_RANGE_ID`) REFERENCES `dt_amount_range` (`ID`),
  ADD CONSTRAINT `exam_biopsy_ibfk_12` FOREIGN KEY (`OUTCOME_CHECK_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `exam_biopsy_ibfk_3` FOREIGN KEY (`BIOPSY_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `exam_biopsy_ibfk_7` FOREIGN KEY (`ASSESSMENT_ID`) REFERENCES `voc_assessment` (`ID`),
  ADD CONSTRAINT `exam_biopsy_ibfk_8` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`),
  ADD CONSTRAINT `exam_biopsy_ibfk_9` FOREIGN KEY (`TEST_ID`) REFERENCES `voc_lab_test` (`ID`);

--
-- Περιορισμοί για πίνακα `exam_caci_condition`
--
ALTER TABLE `exam_caci_condition`
  ADD CONSTRAINT `exam_caci_condition_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `exam_caci_condition_ibfk_2` FOREIGN KEY (`CACI_ID`) REFERENCES `voc_caci_condition` (`ID`),
  ADD CONSTRAINT `exam_caci_condition_ibfk_3` FOREIGN KEY (`QUESTIONNAIRE_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `exam_caci_condition_ibfk_4` FOREIGN KEY (`VALUE_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `exam_caci_condition_ibfk_5` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `exam_essdai_domain`
--
ALTER TABLE `exam_essdai_domain`
  ADD CONSTRAINT `exam_essdai_domain_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `exam_essdai_domain_ibfk_2` FOREIGN KEY (`DOMAIN_ID`) REFERENCES `voc_essdai_domain` (`ID`),
  ADD CONSTRAINT `exam_essdai_domain_ibfk_3` FOREIGN KEY (`QUESTIONNAIRE_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `exam_essdai_domain_ibfk_4` FOREIGN KEY (`ACTIVITY_LEVEL_ID`) REFERENCES `voc_activity_level` (`ID`);

--
-- Περιορισμοί για πίνακα `exam_lab_test`
--
ALTER TABLE `exam_lab_test`
  ADD CONSTRAINT `exam_lab_test_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `exam_lab_test_ibfk_3` FOREIGN KEY (`SAMPLE_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `exam_lab_test_ibfk_5` FOREIGN KEY (`OUTCOME_ASSESSMENT_ID`) REFERENCES `voc_assessment` (`ID`),
  ADD CONSTRAINT `exam_lab_test_ibfk_6` FOREIGN KEY (`NORMAL_RANGE_ID`) REFERENCES `dt_amount_range` (`ID`),
  ADD CONSTRAINT `exam_lab_test_ibfk_7` FOREIGN KEY (`TEST_ID`) REFERENCES `voc_lab_test` (`ID`),
  ADD CONSTRAINT `exam_lab_test_ibfk_8` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`),
  ADD CONSTRAINT `exam_lab_test_ibfk_9` FOREIGN KEY (`OUTCOME_AMOUNT_ID`) REFERENCES `dt_amount` (`ID`);

--
-- Περιορισμοί για πίνακα `exam_medical_imaging_test`
--
ALTER TABLE `exam_medical_imaging_test`
  ADD CONSTRAINT `exam_medical_imaging_test_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `exam_medical_imaging_test_ibfk_2` FOREIGN KEY (`TEST_ID`) REFERENCES `voc_medical_imaging_test` (`ID`),
  ADD CONSTRAINT `exam_medical_imaging_test_ibfk_3` FOREIGN KEY (`TEST_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `exam_medical_imaging_test_ibfk_5` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`),
  ADD CONSTRAINT `exam_medical_imaging_test_ibfk_6` FOREIGN KEY (`ASSESSMENT_ID`) REFERENCES `voc_assessment` (`ID`);

--
-- Περιορισμοί για πίνακα `exam_questionnaire_score`
--
ALTER TABLE `exam_questionnaire_score`
  ADD CONSTRAINT `exam_questionnaire_score_ibfk_2` FOREIGN KEY (`SCORE_ID`) REFERENCES `voc_questionnaire` (`ID`),
  ADD CONSTRAINT `exam_questionnaire_score_ibfk_3` FOREIGN KEY (`QUESTIONNAIRE_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `exam_questionnaire_score_ibfk_4` FOREIGN KEY (`ASSESSMENT_ID`) REFERENCES `voc_assessment` (`ID`),
  ADD CONSTRAINT `exam_questionnaire_score_ibfk_5` FOREIGN KEY (`NORMAL_RANGE_ID`) REFERENCES `dt_int_range` (`ID`),
  ADD CONSTRAINT `exam_questionnaire_score_ibfk_6` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `exam_questionnaire_score_ibfk_7` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `interv_chemotherapy`
--
ALTER TABLE `interv_chemotherapy`
  ADD CONSTRAINT `interv_chemotherapy_fk_stmt_id` FOREIGN KEY (`STMT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `interv_chemotherapy_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `interv_chemotherapy_ibfk_2` FOREIGN KEY (`PERIOD_ID`) REFERENCES `dt_period_of_time` (`ID`),
  ADD CONSTRAINT `interv_chemotherapy_ibfk_3` FOREIGN KEY (`DUE_TO_PSS_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `interv_chemotherapy_ibfk_4` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `interv_medication`
--
ALTER TABLE `interv_medication`
  ADD CONSTRAINT `interv_medication_fk_stmt_id` FOREIGN KEY (`STMT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `interv_medication_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `interv_medication_ibfk_2` FOREIGN KEY (`MEDICATION_ID`) REFERENCES `voc_pharm_drug` (`ID`),
  ADD CONSTRAINT `interv_medication_ibfk_3` FOREIGN KEY (`DOSAGE_ID`) REFERENCES `dt_amount` (`ID`),
  ADD CONSTRAINT `interv_medication_ibfk_4` FOREIGN KEY (`PERIOD_ID`) REFERENCES `dt_period_of_time` (`ID`),
  ADD CONSTRAINT `interv_medication_ibfk_5` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `interv_surgery`
--
ALTER TABLE `interv_surgery`
  ADD CONSTRAINT `interv_surgery_fk_stmt_id` FOREIGN KEY (`STMT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `interv_surgery_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `interv_surgery_ibfk_2` FOREIGN KEY (`SURGERY_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `interv_surgery_ibfk_3` FOREIGN KEY (`DUE_TO_PSS_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `interv_surgery_ibfk_4` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `lifestyle_smoking`
--
ALTER TABLE `lifestyle_smoking`
  ADD CONSTRAINT `lifestyle_smoking_fk_stmt_id` FOREIGN KEY (`STMT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `lifestyle_smoking_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `lifestyle_smoking_ibfk_2` FOREIGN KEY (`STATUS_ID`) REFERENCES `voc_smoking_status` (`ID`),
  ADD CONSTRAINT `lifestyle_smoking_ibfk_3` FOREIGN KEY (`AMOUNT_ID`) REFERENCES `dt_amount` (`ID`),
  ADD CONSTRAINT `lifestyle_smoking_ibfk_5` FOREIGN KEY (`PERIOD_ID`) REFERENCES `dt_period_of_time` (`ID`),
  ADD CONSTRAINT `lifestyle_smoking_ibfk_6` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `other_clinical_trials`
--
ALTER TABLE `other_clinical_trials`
  ADD CONSTRAINT `other_clinical_trials_fk_stmt_id` FOREIGN KEY (`STMT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `other_clinical_trials_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `other_clinical_trials_ibfk_2` FOREIGN KEY (`PERIOD_ID`) REFERENCES `dt_period_of_time` (`ID`),
  ADD CONSTRAINT `other_clinical_trials_ibfk_3` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `other_family_history`
--
ALTER TABLE `other_family_history`
  ADD CONSTRAINT `other_family_history_fk_stmt_id` FOREIGN KEY (`STMT_ID`) REFERENCES `voc_confirmation` (`ID`),
  ADD CONSTRAINT `other_family_history_ibfk_1` FOREIGN KEY (`MEDICAL_CONDITION_ID`) REFERENCES `voc_medical_condition` (`ID`),
  ADD CONSTRAINT `other_family_history_ibfk_2` FOREIGN KEY (`RELATIVE_DEGREE_ID`) REFERENCES `voc_relative_degree` (`ID`),
  ADD CONSTRAINT `other_family_history_ibfk_4` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `other_family_history_ibfk_5` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `other_healthcare_visit`
--
ALTER TABLE `other_healthcare_visit`
  ADD CONSTRAINT `other_healthcare_visit_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `other_healthcare_visit_ibfk_2` FOREIGN KEY (`DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `other_healthcare_visit_ibfk_3` FOREIGN KEY (`SPECIALIST_ID`) REFERENCES `voc_specialist` (`ID`),
  ADD CONSTRAINT `other_healthcare_visit_ibfk_4` FOREIGN KEY (`VISIT_ID`) REFERENCES `patient_visit` (`ID`);

--
-- Περιορισμοί για πίνακα `patient`
--
ALTER TABLE `patient`
  ADD CONSTRAINT `patient_ibfk_1` FOREIGN KEY (`DATE_OF_BIRTH_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `patient_ibfk_2` FOREIGN KEY (`PSS_SYMPTOMS_ONSET_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `patient_ibfk_3` FOREIGN KEY (`PSS_DIAGNOSIS_DATE_ID`) REFERENCES `dt_date` (`ID`),
  ADD CONSTRAINT `patient_ibfk_4` FOREIGN KEY (`COHORT_INCLUSION_DATE_ID`) REFERENCES `dt_date` (`ID`);

--
-- Περιορισμοί για πίνακα `patient_visit`
--
ALTER TABLE `patient_visit`
  ADD CONSTRAINT `patient_visit_ibfk_1` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`),
  ADD CONSTRAINT `patient_visit_ibfk_2` FOREIGN KEY (`VISIT_TYPE_ID`) REFERENCES `voc_visit_type` (`ID`),
  ADD CONSTRAINT `patient_visit_ibfk_3` FOREIGN KEY (`VISIT_STATUS_ID`) REFERENCES `voc_visit_status` (`ID`),
  ADD CONSTRAINT `patient_visit_ibfk_4` FOREIGN KEY (`VISIT_DATE_ID`) REFERENCES `dt_date` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_assessment`
--
ALTER TABLE `voc_assessment`
  ADD CONSTRAINT `voc_assessment_ibfk_1` FOREIGN KEY (`BROADER_TERM_ID`) REFERENCES `voc_assessment` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_biopsy`
--
ALTER TABLE `voc_biopsy`
  ADD CONSTRAINT `voc_biopsy_ibfk_1` FOREIGN KEY (`BROADER_TERM_ID`) REFERENCES `voc_biopsy` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_essdai_domain`
--
ALTER TABLE `voc_essdai_domain`
  ADD CONSTRAINT `voc_essdai_domain_ibfk_1` FOREIGN KEY (`EXCLUDE_ACTIVITY_LEVEL`) REFERENCES `voc_activity_level` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_lab_test`
--
ALTER TABLE `voc_lab_test`
  ADD CONSTRAINT `voc_lab_test_ibfk_3` FOREIGN KEY (`UNIT_ID`) REFERENCES `voc_unit` (`ID`),
  ADD CONSTRAINT `voc_lab_test_ibfk_5` FOREIGN KEY (`OUTCOME_VALUE_ID`) REFERENCES `voc_exam_outcome_type` (`ID`),
  ADD CONSTRAINT `voc_lab_test_ibfk_6` FOREIGN KEY (`BROADER_TERM_ID`) REFERENCES `voc_lab_test` (`ID`),
  ADD CONSTRAINT `voc_lab_test_ibfk_7` FOREIGN KEY (`LAB_TEST_TYPE_ID`) REFERENCES `voc_lab_test_type` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_lymphoma_organ`
--
ALTER TABLE `voc_lymphoma_organ`
  ADD CONSTRAINT `voc_lymphoma_organ_ibfk_1` FOREIGN KEY (`BROADER_TERM_ID`) REFERENCES `voc_lymphoma_organ` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_medical_condition`
--
ALTER TABLE `voc_medical_condition`
  ADD CONSTRAINT `voc_medical_condition_ibfk_1` FOREIGN KEY (`BROADER_TERM_ID`) REFERENCES `voc_medical_condition` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_medical_imaging_test`
--
ALTER TABLE `voc_medical_imaging_test`
  ADD CONSTRAINT `voc_medical_imaging_test_ibfk_1` FOREIGN KEY (`OUTCOME_TYPE_ID`) REFERENCES `voc_exam_outcome_type` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_pharm_drug`
--
ALTER TABLE `voc_pharm_drug`
  ADD CONSTRAINT `voc_pharm_drug_ibfk_1` FOREIGN KEY (`BROADER_TERM_ID`) REFERENCES `voc_pharm_drug` (`ID`),
  ADD CONSTRAINT `voc_pharm_drug_ibfk_2` FOREIGN KEY (`DOSAGE_UNIT_ID`) REFERENCES `voc_unit` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_pregnancy_outcome`
--
ALTER TABLE `voc_pregnancy_outcome`
  ADD CONSTRAINT `voc_pregnancy_outcome_ibfk_1` FOREIGN KEY (`BROADER_TERM_ID`) REFERENCES `voc_pregnancy_outcome` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_questionnaire`
--
ALTER TABLE `voc_questionnaire`
  ADD CONSTRAINT `voc_questionnaire_ibfk_1` FOREIGN KEY (`BROADER_TERM_ID`) REFERENCES `voc_questionnaire` (`ID`),
  ADD CONSTRAINT `voc_questionnaire_ibfk_2` FOREIGN KEY (`SCORE_TYPE_ID`) REFERENCES `voc_exam_outcome_type` (`ID`);

--
-- Περιορισμοί για πίνακα `voc_symptom_sign`
--
ALTER TABLE `voc_symptom_sign`
  ADD CONSTRAINT `voc_symptom_sign_ibfk_1` FOREIGN KEY (`BROADER_TERM_ID`) REFERENCES `voc_symptom_sign` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
