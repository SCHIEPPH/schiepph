# Validate 2 records were inserted into the patient_ethnicity table:
select count(*) from schiepph.patient_ethnicity where ss_id =
( select ss_id from schiepph.syndromic_surveillance where facility_name ='MultipleEthnicity');

# Validate there is an entry for the first ethnicity:
select * from schiepph.patient_ethnicity where concept_name ='1-Hispanic or Latino' and concept_code='2135-2' and coding_system='CDCREC'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='MultipleEthnicity');

# Validate there is an entry for the second ethnicity:
select * from schiepph.patient_ethnicity where concept_name ='2-Not Hispanic or Latino' and concept_code='2186-5' and coding_system='CDCREC'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='MultipleEthnicity');
