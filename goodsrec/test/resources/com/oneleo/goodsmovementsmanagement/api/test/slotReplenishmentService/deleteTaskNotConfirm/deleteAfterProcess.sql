begin
delete REPLENISHMENT_LOCATION_LNK where ID_REPLENISHMENT_TASK in (select id from REPLENISHMENT_TASK where code='test1');
delete REPLENISHMENT_TASK where code='test1';
commit;
end;