SELECT i.item_id, i.title, i.item_condition, i.description
FROM swap_db.items i
         JOIN swap_db.users u ON i.owner_user_id = u.user_id
WHERE u.username = 'demouser3';

SELECT i.item_id, i.title, i.description, i.item_condition
FROM swap_db.items i
         JOIN swap_db.users u ON i.owner_user_id = u.user_id
         LEFT JOIN swap_db.books b ON i.item_id = b.item_id
         LEFT JOIN swap_db.clothings c ON i.item_id = c.item_id
WHERE u.username <> 'demouser1'
ORDER BY RAND()
LIMIT 1;