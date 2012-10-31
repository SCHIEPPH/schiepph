
# Validate no data in inserted into the database when a file with a missing required field is processed
select count(*) from schiepph.syndromic_surveillance where facility_name in
('TestMissingAge','MissingVisitingId','MissVisitDateTime','ReportDateTime','TestMissingPatientId','MissingFacilityId231','MissingFacilityId251','MissDiagnosisType','TestMissingAgeUnits','TestMissingVisitType','InvalidADT','InvalidHL7','TestMultipleMissingR');