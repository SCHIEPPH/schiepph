SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `schiepph` ;
CREATE SCHEMA IF NOT EXISTS `schiepph` DEFAULT CHARACTER SET latin1 ;
USE `schiepph` ;

-- -----------------------------------------------------
-- Table `schiepph`.`syndromic_surveillance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `schiepph`.`syndromic_surveillance` ;

CREATE  TABLE IF NOT EXISTS `schiepph`.`syndromic_surveillance` (
  `ss_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `facility_identifier` VARCHAR(50) NULL DEFAULT '' ,
  `facility_name` VARCHAR(50) NULL DEFAULT '' ,
  `facility_street_address` VARCHAR(50) NULL DEFAULT '' ,
  `facility_city` VARCHAR(50) NULL DEFAULT '' ,
  `facility_county` VARCHAR(50) NULL DEFAULT '' ,
  `facility_state` VARCHAR(50) NULL DEFAULT '' ,
  `facility_visit_type` VARCHAR(50) NULL DEFAULT '' ,
  `medical_record_num` VARCHAR(50) NULL DEFAULT '' ,
  `age` VARCHAR(50) NULL DEFAULT NULL ,
  `age_units` VARCHAR(50) NULL DEFAULT '' ,
  `gender` VARCHAR(50) NULL DEFAULT '' ,
  `patient_city` VARCHAR(50) NULL DEFAULT '' ,
  `patient_zip` VARCHAR(50) NULL DEFAULT '' ,
  `patient_state` VARCHAR(50) NULL DEFAULT '' ,
  `patient_country` VARCHAR(50) NULL DEFAULT '' ,
  `unique_visiting_id` VARCHAR(50) NULL DEFAULT '' ,
  `visit_date_time` VARCHAR(50) NULL DEFAULT '' ,
  `onset_date` VARCHAR(50) NULL DEFAULT '' ,
  `patient_class` VARCHAR(50) NULL DEFAULT '' ,
  `triage_notes` VARCHAR(500) NULL DEFAULT '' ,
  `clinical_impression` VARCHAR(500) NULL DEFAULT '' ,
  `disposition_datetime` VARCHAR(50) NULL DEFAULT '' ,
  `initial_temp` VARCHAR(50) NULL DEFAULT '' ,
  `initial_temp_units` VARCHAR(50) NULL DEFAULT '' ,
  `initial_pulse_oximetry` VARCHAR(50) NULL DEFAULT '' ,
  `initial_pulse_oximetry_units` VARCHAR(50) NULL DEFAULT '' ,
  `report_date_time` VARCHAR(50) NULL DEFAULT '' ,
  `discharge_disposition` VARCHAR(50) NULL DEFAULT '' ,
  PRIMARY KEY (`ss_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 110
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `schiepph`.`chief_complaint`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `schiepph`.`chief_complaint` ;

CREATE  TABLE IF NOT EXISTS `schiepph`.`chief_complaint` (
  `cc_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `ss_id` INT(11) NOT NULL ,
  `free_text` VARCHAR(50) NULL DEFAULT '' ,
  `result_status` VARCHAR(5) NULL DEFAULT '' ,
  `timestamp` VARCHAR(45) NULL DEFAULT '' ,
  PRIMARY KEY (`cc_id`) ,
  CONSTRAINT `cc_ss`
    FOREIGN KEY (`ss_id` )
    REFERENCES `schiepph`.`syndromic_surveillance` (`ss_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `ss_id` ON `schiepph`.`chief_complaint` (`ss_id` ASC) ;

CREATE INDEX `cc_ss` ON `schiepph`.`chief_complaint` (`ss_id` ASC) ;


-- -----------------------------------------------------
-- Table `schiepph`.`injury_codes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `schiepph`.`injury_codes` ;

CREATE  TABLE IF NOT EXISTS `schiepph`.`injury_codes` (
  `code_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `ss_id` INT(11) NOT NULL ,
  `identifier` VARCHAR(20) NULL DEFAULT '' ,
  `text` VARCHAR(50) NULL DEFAULT '' ,
  `coding_system` VARCHAR(20) NULL DEFAULT '' ,
  `dx_type` VARCHAR(5) NULL DEFAULT '' ,
  PRIMARY KEY (`code_id`) ,
  CONSTRAINT `ic_ss`
    FOREIGN KEY (`ss_id` )
    REFERENCES `schiepph`.`syndromic_surveillance` (`ss_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `ss_id` ON `schiepph`.`injury_codes` (`ss_id` ASC) ;

CREATE INDEX `ic_ss` ON `schiepph`.`injury_codes` (`ss_id` ASC) ;


-- -----------------------------------------------------
-- Table `schiepph`.`patient_ethnicity`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `schiepph`.`patient_ethnicity` ;

CREATE  TABLE IF NOT EXISTS `schiepph`.`patient_ethnicity` (
  `pe_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `concept_code` VARCHAR(10) NULL DEFAULT '' ,
  `concept_name` VARCHAR(50) NULL DEFAULT '' ,
  `coding_system` VARCHAR(20) NULL DEFAULT '' ,
  `ss_id` INT(11) NOT NULL ,
  PRIMARY KEY (`pe_id`) ,
  CONSTRAINT `pe_ss`
    FOREIGN KEY (`ss_id` )
    REFERENCES `schiepph`.`syndromic_surveillance` (`ss_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 15
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `ss_id` ON `schiepph`.`patient_ethnicity` (`ss_id` ASC) ;

CREATE INDEX `pe_ss` ON `schiepph`.`patient_ethnicity` (`ss_id` ASC) ;


-- -----------------------------------------------------
-- Table `schiepph`.`patient_race`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `schiepph`.`patient_race` ;

CREATE  TABLE IF NOT EXISTS `schiepph`.`patient_race` (
  `pr_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `ss_id` INT(11) NOT NULL ,
  `concept_code` VARCHAR(20) NULL DEFAULT '' ,
  `concept_name` VARCHAR(199) NULL DEFAULT '' ,
  `coding_system` VARCHAR(20) NULL DEFAULT '' ,
  PRIMARY KEY (`pr_id`) ,
  CONSTRAINT `pr_ss`
    FOREIGN KEY (`ss_id` )
    REFERENCES `schiepph`.`syndromic_surveillance` (`ss_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `ss_id` ON `schiepph`.`patient_race` (`ss_id` ASC) ;

CREATE INDEX `pr_ss` ON `schiepph`.`patient_race` (`ss_id` ASC) ;


-- -----------------------------------------------------
-- Table `schiepph`.`syndromic_surveillance_transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `schiepph`.`syndromic_surveillance_transaction` ;

CREATE  TABLE IF NOT EXISTS `schiepph`.`syndromic_surveillance_transaction` (
  `ss_id` INT(11) NOT NULL ,
  `timestamp` DATETIME NOT NULL ,
  `processed` INT(11) NULL DEFAULT NULL ,
  `result` VARCHAR(45) NULL DEFAULT NULL ,
  `response` BLOB NULL DEFAULT NULL ,
  PRIMARY KEY (`ss_id`, `timestamp`) ,
  CONSTRAINT `sst_ss`
    FOREIGN KEY (`ss_id` )
    REFERENCES `schiepph`.`syndromic_surveillance` (`ss_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `ss_id` ON `schiepph`.`syndromic_surveillance_transaction` (`ss_id` ASC) ;

CREATE INDEX `sst_ss` ON `schiepph`.`syndromic_surveillance_transaction` (`ss_id` ASC) ;


-- -----------------------------------------------------
-- Table `schiepph`.`unique_patient_identifier`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `schiepph`.`unique_patient_identifier` ;

CREATE  TABLE IF NOT EXISTS `schiepph`.`unique_patient_identifier` (
  `upi_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `identifier_type_code` VARCHAR(5) NULL DEFAULT '' ,
  `identifier` VARCHAR(50) NULL DEFAULT '' ,
  `ss_id` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`upi_id`) ,
  CONSTRAINT `ss_id`
    FOREIGN KEY (`ss_id` )
    REFERENCES `schiepph`.`syndromic_surveillance` (`ss_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 25
DEFAULT CHARACTER SET = latin1;

CREATE INDEX `ss_id` ON `schiepph`.`unique_patient_identifier` (`ss_id` ASC) ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
