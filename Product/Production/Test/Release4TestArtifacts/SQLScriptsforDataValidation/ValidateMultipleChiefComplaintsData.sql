# Validate 2 records were inserted into the chief complaint table:
select count(*) from schiepph.chief_complaint where ss_id =
( select ss_id from schiepph.syndromic_surveillance where facility_name ='MultipleCComplaints');

# Validate there is an entry for the first complaint:
select * from schiepph.chief_complaint where free_text ='STOMACH ACHE' and result_status='F' and timestamp='201102171531'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='MultipleCComplaints');

# Validate there is an entry for the second complaint:
select * from schiepph.chief_complaint where free_text ='DIZZY' and result_status='F' and timestamp='201102171531'
and ss_id=( select ss_id from schiepph.syndromic_surveillance where facility_name ='MultipleCComplaints');
