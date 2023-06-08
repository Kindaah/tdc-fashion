--
-- Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
-- This software is the confidential and proprietary information of
-- The Dress Club. ("Confidential Information").
-- You may not disclose such Confidential Information, and may only
-- use such Confidential Information in accordance with the terms of
-- the license agreement you entered into with The Dress Club.
--

update feature f1 set order_in_product = f2.rowNumber - 1
from (select f2.id_product, f2.id, row_number() over () as rowNumber from feature f2 order by id_product, id) f2
where f2.id = f1.id;