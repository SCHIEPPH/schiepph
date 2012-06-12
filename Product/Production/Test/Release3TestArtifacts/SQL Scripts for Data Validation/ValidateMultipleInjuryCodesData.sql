# Validate 3 records were inserted into the injury codes table:
select count(*) from schiepph.injury_codes where ss_id =
( select ss_id from schiepph.syndromic_surveillance where facility_name ='TestMultipleInjuryCodes');

# Validate there is an entry for the first injury code:
select * from schiepph.injury_codes where identifier ='E8809' and text='FALL ON STAIR/STEP NEC' and coding_system='I9CDX' and dx_type='A'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='TestMultipleInjuryCodes');

# Validate there is an entry for the second injury code:
select * from schiepph.injury_codes where identifier ='4739' and text='CHRONIC SINUSITIS NOS' and coding_system='I9CDX' and dx_type='A'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='TestMultipleInjuryCodes');

# Validate there is an entry for the third injury code:
select * from schiepph.injury_codes where identifier ='04100' and text='STREPTOCOCCUS UNSPECF' and coding_system='I9CDX' and dx_type='F'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='TestMultipleInjuryCodes');