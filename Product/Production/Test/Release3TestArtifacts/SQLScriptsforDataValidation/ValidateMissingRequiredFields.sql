
# Validate no data in inserted into the database when a file with a missing required field is processed
select count(*) from schiepph.syndromic_surveillance where facility_name in
('TestMissingAge','TestMissingVisitingId','TestMissingVisitDateTime','TestMissingReportDateTime','TestMissingPatientId','TestMissingFacilityId231','TestMissingFacilityId251','TestMissingDiagnosisType','TestMissingAgeUnits','TestMissingVisitType','TestInvalidAdt','TestInvalidhl7','TestMultipleMissingR')