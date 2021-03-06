
# Validate data makes it to the schiepph.syndromic_surveillance table with missing Race data:
select * from schiepph.syndromic_surveillance 
where facility_identifier = '1234567890'
and facility_name = 'TestMissingRace'
and facility_street_address = ''
and facility_city = ''
and facility_county = ''
and facility_state = ''
and facility_visit_type = '170300000X'
and medical_record_num = ''
and age = '20'
and age_units = 'a'
and gender = 'M'
and patient_city = ''
and patient_zip = '21502'
and patient_state = '24'
and patient_country = 'USA'
and unique_visiting_id = 'AN'
and visit_date_time = '20111009025915-0500'
and onset_date = '20110215'
and patient_class = 'I'
and triage_notes = ''
and clinical_impression = ''
and disposition_datetime = '20110113164512-0500'
and initial_temp = '100.1'
and initial_temp_units = '[degF]'
and initial_pulse_oximetry ='95'
and initial_pulse_oximetry_units = '%'
and report_date_time = '201110090314-0500'
and discharge_disposition = '04';

# Validate data makes it to the schiepph.patient_ethnicity table with missing Race data:
select * from schiepph.patient_ethnicity 
where ss_id = ( select ss_id from schiepph.syndromic_surveillance where facility_name='TestMissingRace' )
and concept_code = '2135-2'
and concept_name = 'Hispanic or Latino'
and coding_system = 'CDCREC';

# Validate data makes it to the schiepph.injury_codes table with missing Race data:
select * from schiepph.injury_codes 
where ss_id = ( select ss_id from schiepph.syndromic_surveillance where facility_name='TestMissingRace' )
and identifier = 'E8809'
and text = 'FALL ON STAIR/STEP NEC'
and coding_system = 'I9CDX'
and dx_type = 'A';

# Validate data makes it to the schiepph.chief_complaint table with missing Race data:
select * from schiepph.chief_complaint 
where ss_id = ( select ss_id from schiepph.syndromic_surveillance where facility_name='TestMissingRace' )
and free_text = 'STOMACH ACHE'
and result_status = 'F'
and timestamp = '201102171531';

# Validate data makes it to the schiepph.unique_patient_identifier table with missing Race data:
select * from schiepph.unique_patient_identifier
where ss_id = ( select ss_id from schiepph.syndromic_surveillance where facility_name='TestMissingRace' )
and identifier_type_code = 'PI'
and identifier = '95101100001';

# Validate NO data makes it to schiepph.patient_race table with missing Race data:
select count(*) from schiepph.patient_race 
where ss_id = ( select ss_id from schiepph.syndromic_surveillance where facility_name='TestMissingRace' );
