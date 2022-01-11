create or replace PROCEDURE prepaid_evaluation_monitoring_topup_policy
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
   SELECT *
    FROM prepaid_cx_offer_monitoring
    WHERE offer_config_id = in_offer_config_id;

 var_data_trx varchar2(1000);
 var_data_config varchar2(1000);
 var_status varchar2(10);
 topup f_tbl_topup%rowtype;
 b_account_balance boolean;
 b_account_balance_before boolean;
 b_topup_value boolean;
 b_da_id boolean;
 b_da_balance boolean;
 
BEGIN
    var_status := 'FAILED';
    out_result := 2;
    

    FOR config IN cursor_prepaid_cx_offer_monitoring
        LOOP
            var_data_config := 'top_up_transaction_value|'||config.top_up_transaction_value;
               
             select * 
                into topup
             from f_tbl_topup 
                where msisdn = in_msisdn    -- ='93975664'
                and operator_id = config.operator_id 
                and transaction_timestamp between config.period_start_date and config.period_end_date
              ;
                 
                IF (config.event_type ='Topup' AND config.operator_id = topup.operator_id AND config.usage_service_type = topup.usage_service_type) THEN
                   --current before
                    IF config.top_up_cur_balance_op is NULL THEN
                        b_account_balance := true;
                    ELSE     
                        IF config.top_up_cur_balance_op = 'Less Than' THEN
                            IF config.top_up_cur_balance_value < topup.current_balance THEN
                                 b_account_balance := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'More Than' THEN
                            IF config.top_up_cur_balance_value < topup.current_balance THEN
                                 b_account_balance := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'Equal to' THEN
                            IF config.top_up_cur_balance_value < topup.current_balance THEN
                                 b_account_balance := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'Less Than or Equal to' THEN
                            IF config.top_up_cur_balance_value < topup.current_balance THEN
                                 b_account_balance := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'More Than or Equal to' THEN
                            IF config.top_up_cur_balance_value < topup.current_balance THEN
                                 b_account_balance := true;
                            END IF;
                        END IF;
                    END IF;
                    -- end current balance
                   
                   --balance before
                    IF config.top_up_acc_balance_before_op is NULL THEN
                        b_account_balance_before := true;
                    ELSE     
                        IF config.top_up_acc_balance_before_op = 'Less Than' THEN
                            IF config.top_up_acc_balance_before_value < topup.account_balance_before THEN
                                 b_account_balance_before := true;
                            END IF;
                        ELSIF config.top_up_acc_balance_before_op = 'More Than' THEN
                            IF config.top_up_acc_balance_before_value < topup.account_balance_before THEN
                                 b_account_balance_before := true;
                            END IF;
                        ELSIF config.top_up_acc_balance_before_op = 'Equal to' THEN
                            IF config.top_up_acc_balance_before_value < topup.account_balance_before THEN
                                 b_account_balance_before := true;
                            END IF;
                        ELSIF config.top_up_acc_balance_before_op = 'Less Than or Equal to' THEN
                            IF config.top_up_acc_balance_before_value < topup.account_balance_before THEN
                                 b_account_balance_before := true;
                            END IF;
                        ELSIF config.top_up_acc_balance_before_op = 'More Than or Equal to' THEN
                            IF config.top_up_acc_balance_before_value < topup.account_balance_before THEN
                                 b_account_balance_before := true;
                            END IF;
                        END IF;
                    END IF;
                    -- end balance before
                    
                      --topup value 
                    IF config.top_up_cur_balance_op is NULL THEN
                        b_topup_value := true;
                    ELSE     
                        IF config.top_up_cur_balance_op = 'Less Than' THEN
                            IF config.top_up_cur_balance_value < topup.paid_topup THEN
                                 b_topup_value := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'More Than' THEN
                            IF config.top_up_cur_balance_value < topup.paid_topup THEN
                                 b_topup_value := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'Equal to' THEN
                            IF config.top_up_cur_balance_value < topup.paid_topup THEN
                                 b_topup_value := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'Less Than or Equal to' THEN
                            IF config.top_up_cur_balance_value < topup.paid_topup THEN
                                 b_topup_value := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'More Than or Equal to' THEN
                            IF config.top_up_cur_balance_value < topup.paid_topup THEN
                                 b_topup_value := true;
                            END IF;
                        END IF;
                    END IF;
                    -- end topup value 
                    
                      --da balance 
                    IF config.top_up_cur_balance_op is NULL THEN
                        b_da_balance := true;
                    ELSE     
                        IF config.top_up_cur_balance_op = 'Less Than' THEN
                            IF config.top_up_cur_balance_value < topup.da_balance THEN
                                 b_da_balance := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'More Than' THEN
                            IF config.top_up_cur_balance_value < topup.da_balance THEN
                                 b_da_balance := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'Equal to' THEN
                            IF config.top_up_cur_balance_value < topup.da_balance THEN
                                 b_da_balance := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'Less Than or Equal to' THEN
                            IF config.top_up_cur_balance_value < topup.da_balance THEN
                                 b_da_balance := true;
                            END IF;
                        ELSIF config.top_up_cur_balance_op = 'More Than or Equal to' THEN
                            IF config.top_up_cur_balance_value < topup.da_balance THEN
                                 b_da_balance := true;
                            END IF;
                        END IF;
                    END IF;
                    -- end da balance 
                IF  (b_da_balance AND  b_topup_value AND  b_account_balance_before AND b_account_balance) THEN
                    out_result := 1;
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