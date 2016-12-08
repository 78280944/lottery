DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `GetParentAccount`(IN startParent VARCHAR(20))
BEGIN
	SELECT Accountid, UserID, UserName, Limited, Ratio, Percentage, State, T2.SupUserName, Level, 
    OffType, Money, Attribute1, Attribute2
FROM (
    SELECT 
        @r AS _id,
        (SELECT @r := SupUserName FROM account_detail WHERE username = _id order by Level) AS SupUserName,
        @l := @l + 1 AS lvl
    FROM
        (SELECT @r := startParent, @l := 0) vars,
        account_detail h
    WHERE @r <> 'system'

    ) T1
JOIN account_detail T2
ON T1._id = T2.username
ORDER BY T1.lvl DESC;

END$$
DELIMITER ;