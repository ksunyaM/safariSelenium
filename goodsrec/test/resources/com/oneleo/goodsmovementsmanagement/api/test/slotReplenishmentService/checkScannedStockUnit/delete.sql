begin
delete REPLENISHMENT_ITEM where ID_REPLENISHMENT_TASK in (select id from REPLENISHMENT_TASK where code='${code}');
delete REPLENISHMENT_LOCATION_LNK where ID_REPLENISHMENT_TASK in (select id from REPLENISHMENT_TASK where code='${code}');
delete REPLENISHMENT_TASK where code='${code}';
commit;
end;