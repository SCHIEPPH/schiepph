# Validate 2 records were inserted into the patient_race table:
select count(*) from schiepph.patient_race where ss_id =
( select ss_id from schiepph.syndromic_surveillance where facility_name ='TestMultipleRace');

# Validate there is an entry for the first race:
select * from schiepph.patient_race where concept_name ='Race1-White' and concept_code='2106-3' and coding_system='CDCREC'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='TestMultipleRace');

# Validate there is an entry for the second race:
select * from schiepph.patient_race where concept_name ='Race2-Asian' and concept_code='2028-9' and coding_system='CDCREC'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='TestMultipleRace');