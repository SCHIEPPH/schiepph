
# Validate all the data make it to the schiepph.syndromic_surveillance table:
select * from schiepph.syndromic_surveillance 
where facility_identifier = '1234567890'
and facility_name = 'TestAllData2.3.1'
and facility_street_address = '1234'
and facility_city = 'Chantilly'
and facility_county = 'County'
and facility_state = 'VA'
and facility_visit_type = '1108-0'
and medical_record_num = 'MR01234567'
and age = '27'
and age_units = 'YEAR'
and gender = 'M'
and patient_city = 'Fairfax'
and patient_zip = '21502'
and patient_state = '24'
and patient_country = 'USA'
and unique_visiting_id = 'VN101100001'
and visit_date_time = '20111009025915-0500'
and onset_date = '20110215'
and patient_class = 'I'
and triage_notes = 'Pain a recurrent cramping sensation.'
and clinical_impression = 'Pain consist with appendicitis'
and disposition_datetime = '20110113164512-0500'
and initial_temp = '100.1'
and initial_temp_units = 'FARENHEIT'
and initial_pulse_oximetry ='95'
and initial_pulse_oximetry_units = 'PERCENT'
and report_date_time = '201110090314-0500'
and discharge_disposition = '04';

# Validate all the data make it to the schiepph.patient_race table :
select * from schiepph.patient_race 
where ss_id = ( select ss_id from schiepph.syndromic_surveillance where facility_name='TestAllData2.3.1' )
and concept_code = '2106-3'
and concept_name = 'White'
and coding_system = 'CDCREC';

# Validate all the data make it to the schiepph.patient_ethnicity table :
select * from schiepph.patient_ethnicity 
where ss_id = ( select ss_id from schiepph.syndromic_surveillance where facility_name='TestAllData2.3.1' )
and concept_code = '2135-2'
and concept_name = 'Hispanic or Latino'
and coding_system = 'CDCREC';

# Validate all the data make it to the schiepph.injury_codes table :
select * from schiepph.injury_codes 
where ss_id = ( select ss_id from schiepph.syndromic_surveillance where facility_name='TestAllData2.3.1' )
and identifier = 'E8809'
and text = 'FALL ON STAIR/STEP NEC'
and coding_system = 'I9CDX'
and dx_type = 'A';

# Validate all the data make it to the schiepph.chief_complaint table :
select * from schiepph.chief_complaint 
where ss_id = ( select ss_id from schiepph.syndromic_surveillance where facility_name='TestAllData2.3.1' )
and free_text = 'STOMACH ACHE'
and result_status = 'F'
and timestamp = '201102171531';

# Validate all the data make it to the schiepph.unique_patient_identifier table :
SELECT schiepph.unique_patient_identifier.*
FROM schiepph.syndromic_surveillance
INNER JOIN schiepph.unique_patient_identifier
ON schiepph.unique_patient_identifier.ss_id = schiepph.syndromic_surveillance.ss_id 
and schiepph.syndromic_surveillance.facility_name='TestAllData2.3.1'
and schiepph.unique_patient_identifier.identifier_type_code in ('PI','MR')
and schiepph.unique_patient_identifier.identifier in ('95101100001','MR01234567');
