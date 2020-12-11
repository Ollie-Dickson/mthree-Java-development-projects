-- Ollie Dickson
-- 03/12/2020

-- ./mysqldump -u root -p hotel room > /Users/olliedickson/Desktop/mthree/relational-database-project-Ollie-Dickson/mysql_dump_data/room.sql

USE hotel;

DELETE FROM ROOM WHERE roomID < 1000;
INSERT INTO `ROOM` VALUES (201,'Double',1,1,0,0,0,2,4,199.99,10.00),(202,'Double',0,0,1,0,1,2,4,174.99,10.00),
(203,'Double',1,1,0,0,0,2,4,199.99,10.00),(204,'Double',0,0,1,0,1,2,4,174.99,10.00),(205,'Single',1,1,1,0,0,2,2,174.99,NULL),
(206,'Single',1,0,1,0,1,2,2,149.99,NULL),(207,'Single',1,1,1,0,0,2,2,174.99,NULL),(208,'Single',1,0,1,0,1,2,2,149.99,NULL),
(301,'Double',1,1,0,0,0,2,4,199.99,10.00),(302,'Double',0,0,1,0,1,2,4,174.99,10.00),(303,'Double',1,1,0,0,0,2,4,199.99,10.00),
(304,'Double',0,0,1,0,1,2,4,174.99,10.00),(305,'Single',1,1,1,0,0,2,2,174.99,NULL),(306,'Single',1,0,1,0,1,2,2,149.99,NULL),
(307,'Single',1,1,1,0,0,2,2,174.99,NULL),(308,'Single',1,0,1,0,1,2,2,149.99,NULL),(401,'Suite',1,0,1,1,1,3,8,399.99,20.00),
(402,'Suite',1,0,1,1,1,3,8,399.99,20.00);

DELETE FROM GUEST WHERE guestID < 1000;
INSERT INTO `GUEST` VALUES 
(15,'Ollie','Dickson','123 Fake Street','The Big City','AL','69451','(999) 620-2411'),
(16,'Mack','Simmer','379 Old Shore Street','Council Bluffs','IA','51501','(291) 553-0508'),
(17,'Bettyann','Seery','750 Wintergreen Dr.','Wasilla','AK','99654','(478) 277-9632'),
(18,'Duane','Cullison','9662 Foxrun Lane','Harlingen','TX','78552','(308) 494-0198'),
(19,'Karie','Yang','9378 W. Augusta Ave.','West Deptford','NJ','08096','(214) 730-0298'),
(20,'Aurore','Lipton','762 Wild Rose Street','Saginaw','MI','48601','(377) 507-0974'),
(21,'Zachery','Luechtefeld','7 Poplar Dr.','Arvada','CO','80003','(814) 485-2615'),
(22,'Jeremiah','Pendergrass','70 Oakwood St.','Zion','IL','60099','(279) 491-0960'),
(23,'Walter','Holaway','7556 Arrowhead St.','Cumberland','RI','02864','(446) 396-6785'),
(24,'Wilfred','Vise','77 West Surrey Street','Oswego','NY','13126','(834) 727-1001'),
(25,'Maritza','Tilton','939 Linda Rd.','Burke','VA','22015','(446) 351-6860'),
(26,'Joleen','Tison','87 Queen St.','Drexel Hill','PA','19026','(231) 893-2755');

DELETE FROM RESERVATION WHERE reservationID < 1000;
INSERT INTO `RESERVATION` VALUES (1,1,0,'2023-02-02','2023-02-04',299.98),(2,2,1,'2023-02-05','2023-02-10',999.95),
(3,2,0,'2023-02-22','2023-02-24',349.98),(4,2,2,'2023-03-06','2023-03-07',199.99),(5,1,1,'2023-03-17','2023-03-20',524.97),
(6,3,0,'2023-03-18','2023-03-23',924.95),(7,2,2,'2023-03-29','3023-03-31',349.98),(8,2,0,'2023-03-31','2023-04-05',874.95),
(9,1,0,'2023-04-09','2023-04-13',799.96),(10,1,1,'2023-04-23','2023-04-24',174.99),(11,2,4,'2023-05-30','2023-06-02',1199.97),
(12,2,0,'2023-06-10','2023-06-14',599.96),(13,1,0,'2023-06-10','2023-06-14',599.96),(14,3,0,'2023-06-17','2023-06-18',184.99),
(15,2,0,'2023-06-28','2023-07-02',699.96),(16,3,1,'2023-07-13','2023-07-14',184.99),(17,4,2,'2023-07-18','2023-07-21',1259.97),
(18,2,1,'2023-07-28','2023-07-29',199.99),(19,1,0,'2023-08-30','2023-09-01',349.98),(20,2,0,'2023-09-16','2023-09-17',149.99),
(21,2,2,'2023-09-13','2023-09-15',399.98),(22,2,2,'2023-11-22','2023-11-25',1199.97),(23,2,0,'2023-11-22','2023-11-25',449.97),
(24,2,2,'2023-11-22','2023-11-25',599.97),(25,2,0,'2023-12-24','2023-12-28',699.96);

DELETE FROM ROOM_GUEST_RESERVATION WHERE reservationID < 1000;
INSERT INTO `ROOM_GUEST_RESERVATION` VALUES (205,15,15),(307,15,5),(206,16,23),(208,16,20),(301,16,24),(308,16,1),(203,17,2),
(303,17,18),(305,17,19),(305,18,3),(401,18,22),(201,19,4),(203,19,21),(302,20,6),(304,20,14),(202,21,7),(304,22,8),(204,23,16),
(301,23,9),(207,24,10),(401,24,17),(302,25,25),(401,25,11),(206,26,12),(208,26,13);

DELETE FROM ROOM_GUEST_RESERVATION WHERE guestID = 22;
DELETE FROM RESERVATION WHERE reservationID = 8;
DELETE FROM GUEST WHERE guestID = 22;


-- LOAD DATA LOCAL INFILE '/Users/olliedickson/Desktop/mthree/relational-database-project-Ollie-Dickson/guests.csv'
-- INTO TABLE hotel.guest
-- FIELDS TERMINATED BY ',';