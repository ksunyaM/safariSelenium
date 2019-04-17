begin
delete REPLENISHMENT_LOCATION_LNK where ID_REPLENISHMENT_TASK in (select id from REPLENISHMENT_TASK where code='TESTALFO');
delete REPLENISHMENT_TASK where code='TESTALFO';
commit;
end;