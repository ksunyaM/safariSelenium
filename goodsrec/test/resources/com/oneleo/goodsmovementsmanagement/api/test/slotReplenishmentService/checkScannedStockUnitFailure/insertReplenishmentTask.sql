BEGIN
Insert into REPLENISHMENT_TASK (ID,CODE,INTERNAL_CODE,PRIORITY,QUANTITY,EFFECTIVE_QUANTITY,END_DATE,START_DATE,STATUS,USERNAME,WAREHOUSE_CODE,TRACEABLE,TYPE,DELETED,INSTANCE_KEY,REFERENCE_CODE,CURRENT_STATE,COMPANY,CREATION_DATE,SUB_TYPE,VERSION,LAST_UPDATE_DATE) 
values (REPLENISHMENT_TASK_SEQ.nextval,'${code}','000000000010000561',null,'40',null,null,null,'Open',null,'3701','1','Automatic Generation',null,null,null,null,null,to_timestamp('07/07/2017 12:23:53','MM/DD/YYYY HH24:MI:SS'),null,'0',null);
Insert into REPLENISHMENT_LOCATION_LNK (ID,SOURCE_LOCATION,PROPOSED_SEQUENCE,TARGET_LOCATION,DELETED,ID_REPLENISHMENT_TASK,TARGET_PRIORITY) 
values (REPLENISHMENT_LOCATION_LNK_SEQ.nextval,'Y052A21',null,'Y994A41',null,(select ID from REPLENISHMENT_TASK where code='${code}'),null);
Insert into REPLENISHMENT_LOCATION_LNK (ID,SOURCE_LOCATION,PROPOSED_SEQUENCE,TARGET_LOCATION,DELETED,ID_REPLENISHMENT_TASK,TARGET_PRIORITY) 
values (REPLENISHMENT_LOCATION_LNK_SEQ.nextval,'Y052A23',null,'Y994A41',null,(select ID from REPLENISHMENT_TASK where code='${code}'),null);
Insert into REPLENISHMENT_LOCATION_LNK (ID,SOURCE_LOCATION,PROPOSED_SEQUENCE,TARGET_LOCATION,DELETED,ID_REPLENISHMENT_TASK,TARGET_PRIORITY) 
values (REPLENISHMENT_LOCATION_LNK_SEQ.nextval,'Y052A22',null,'Y994A41',null,(select ID from REPLENISHMENT_TASK where code='${code}'),null);
COMMIT;
END;