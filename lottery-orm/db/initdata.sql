
delete from lottery_round_item;
delete FROM lottery.lottery_round;
delete from lottery_order_detail;
delete from lottery_order;
delete from trade_info;
update lottery.account_detail set money=0.0 ;