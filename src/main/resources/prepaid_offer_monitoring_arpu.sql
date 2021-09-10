create or replace PROCEDURE prepaid_evaluation_monitoring_arpu_policy
(
    in_request_id in VARCHAR2,
    in_instance_id IN VARCHAR2,
    in_offer_config_id IN VARCHAR2,
    in_provision_type IN VARCHAR2,
    in_event_type IN VARCHAR2,
    in_msisdn IN NUMBER,
    out_result OUT INTEGER
)
IS
    cursor cursor_prepaid_cx_offer_monitoring is
   SELECT event_type, 
        arpu_op, arpu_value, 
        
        top_up_transaction_value
    FROM prepaid_cx_offer_monitoring
    WHERE offer_config_id = in_offer_config_id;

 var_data_trx varchar2(1000);
 var_data_config varchar2(1000);
 var_status varchar2(10);
 topup_msisdn f_tbl_topup.msisdn%type;
 topup_transaction f_tbl_topup.transaction_value%type;
 topup_operator_id f_tbl_topup.operator_id%type;
 
 
BEGIN
    var_status := 'FAILED';
    out_result := 2;

    FOR config IN cursor_prepaid_cx_offer_monitoring
        LOOP
            var_data_config := 'top_up_transaction_value|'||config.top_up_transaction_value;
               
             select msisdn, operator_id, sum(transaction_value) as transaction_value 
                into topup_msisdn, topup_operator_id, topup_transaction
             from f_tbl_topup 
                where msisdn = in_msisdn    -- ='93975664'
                --and operator_id = 133 
                --and tansaction_timestamp = ''
                group by msisdn, operator_id;
                 
                IF config.event_type ='ARPU' THEN
                     IF config.arpu_op = 'Less Than' THEN
                        IF config.arpu_value < topup_transaction THEN
                              out_result :=1;                  
                        END IF;
                     ELSIF config.arpu_op = 'More Than' THEN
                         IF config.arpu_value > topup_transaction THEN
                                out_result :=1;                  
                         END IF;
                     ELSIF config.arpu_op = 'Equal to' THEN
                         IF config.arpu_value = topup_transaction THEN
                                out_result :=1;                  
                         END IF;
                     ELSIF config.arpu_op = 'Less Than or Equal to' THEN
                         IF config.arpu_value <= topup_transaction THEN
                                out_result :=1;                  
                         END IF;
                     ELSIF config.arpu_op = 'More Than or Equal to' THEN
                         IF config.arpu_value >= topup_transaction THEN
                                out_result :=1;                  
                         END IF; 
                     END IF;
                END IF;    
            
                IF out_result = 1 THEN
                    var_status := 'SUCCESS';
                END IF;

              insert into
              prepaid_evaluation_log(request_id, msisdn, instance_id, offer_config_id, provision_type, event_type, status, status_code, data_trx, data_config)
              values(in_request_id, in_msisdn, in_instance_id, in_offer_config_id, in_provision_type, in_event_type, var_status, out_result, var_data_trx, var_data_config);    

        END LOOP;

   

EXCEPTION
   WHEN OTHERS THEN
      dbms_output.put_line( SQLERRM );


END;