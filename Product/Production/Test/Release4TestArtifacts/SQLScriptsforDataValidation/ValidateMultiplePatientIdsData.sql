# Validate 2 records were inserted into the patient identifier table:
select count(*) from schiepph.unique_patient_identifier where ss_id =
( select ss_id from schiepph.syndromic_surveillance where facility_name ='MultiplePatientIds');

# Validate there is an entry for the first patient identifier:
select * from schiepph.unique_patient_identifier where identifier_Type_code ='PI' and identifier='95101100001'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='MultiplePatientIds');

# Validate there is an entry for the second patient identifier:
select * from schiepph.unique_patient_identifier where identifier_Type_code ='PT' and identifier='E95101100001'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='MultiplePatientIds');
