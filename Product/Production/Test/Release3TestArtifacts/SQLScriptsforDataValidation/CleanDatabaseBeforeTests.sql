# Delete data from all the tables before the tests run

delete from schiepph.chief_complaint;
delete from schiepph.injury_codes;
delete from schiepph.patient_ethnicity;
delete from schiepph.patient_race;
delete from schiepph.unique_patient_identifier;
delete from schiepph.syndromic_surveillance;

commit;