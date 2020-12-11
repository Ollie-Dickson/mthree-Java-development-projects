-- Ollie Dickson
-- 04/12/2020

USE hotel;

-- 1. Write a query that returns a list of reservations that end in July 2023, including the name of the guest, 
--    the room number(s), and the reservation dates.
SELECT bridge.roomID, 
	GUEST.firstName, GUEST.lastName, 
	RESERVATION.startDate, RESERVATION.endDate
FROM RESERVATION
INNER JOIN ROOM_GUEST_RESERVATION bridge ON RESERVATION.reservationID = bridge.reservationID
INNER JOIN GUEST ON bridge.guestID = GUEST.guestID
WHERE RESERVATION.endDate BETWEEN '2023-7-1' AND '2023-7-31';
-- '205','Ollie','Dickson','2023-06-28','2023-07-02'
-- '204','Walter','Holaway','2023-07-13','2023-07-14'
-- '401','Wilfred','Vise','2023-07-18','2023-07-21'
-- '303','Bettyann','Seery','2023-07-28','2023-07-29'


-- 2. Write a query that returns a list of all reservations for rooms with a jacuzzi, displaying the guest's name, 
--    the room number, and the dates of the reservation.
SELECT bridge.roomID, 
	GUEST.firstName, GUEST.lastName, 
	RESERVATION.startDate, RESERVATION.endDate
FROM RESERVATION
INNER JOIN ROOM_GUEST_RESERVATION bridge ON RESERVATION.reservationID = bridge.reservationID
INNER JOIN GUEST ON bridge.guestID = GUEST.guestID
INNER JOIN ROOM ON bridge.roomID = ROOM.roomID
WHERE ROOM.hasJacuzzi = 1
ORDER BY RESERVATION.startDate;
-- '203','Bettyann','Seery','2023-02-05','2023-02-10'
-- '305','Duane','Cullison','2023-02-22','2023-02-24'
-- '201','Karie','Yang','2023-03-06','2023-03-07'
-- '307','Ollie','Dickson','2023-03-17','2023-03-20'
-- '301','Walter','Holaway','2023-04-09','2023-04-13'
-- '207','Wilfred','Vise','2023-04-23','2023-04-24'
-- '205','Ollie','Dickson','2023-06-28','2023-07-02'
-- '303','Bettyann','Seery','2023-07-28','2023-07-29'
-- '305','Bettyann','Seery','2023-08-30','2023-09-01'
-- '203','Karie','Yang','2023-09-13','2023-09-15'
-- '301','Mack','Simmer','2023-11-22','2023-11-25'



--  3. Write a query that returns all the rooms reserved for a specific guest, including the guest's name, the room(s) 
--     reserved, the starting date of the reservation, and how many people were included in the reservation.
SELECT bridge.roomID,
	GUEST.firstName, GUEST.lastName, 
    RESERVATION.startDate, RESERVATION.adults, RESERVATION.children
FROM RESERVATION
INNER JOIN ROOM_GUEST_RESERVATION bridge ON RESERVATION.reservationID = bridge.reservationID
INNER JOIN GUEST ON bridge.guestID = GUEST.guestID
WHERE GUEST.firstName = 'Duane' AND GUEST.lastName = 'Cullison';
-- '305','Duane','Cullison','2023-02-22','2','0'
-- '401','Duane','Cullison','2023-11-22','2','2'


-- 4. Write a query that returns a list of rooms, reservation ID, and per-room cost for each reservation. 
--    The results should include all rooms, whether or not there is a reservation associated with the room.
SELECT ROOM.roomID AS 'Room ID',
	IFNULL(bridge.reservationID,'No Reservations') AS 'Reservation ID',
    IFNULL(CONCAT('$',RESERVATION.totalCost) ,'No Reservations') AS 'Total Cost'
FROM ROOM
LEFT OUTER JOIN ROOM_GUEST_RESERVATION bridge ON ROOM.roomID = bridge.roomID
LEFT OUTER JOIN RESERVATION ON bridge.reservationID = RESERVATION.reservationID;
-- '201','4','$199.99'
-- '202','7','$349.98'
-- '203','2','$999.95'
-- '203','21','$399.98'
-- '204','16','$184.99'
-- '205','15','$699.96'
-- '206','23','$449.97'
-- '206','12','$599.96'
-- '207','10','$174.99'
-- '208','20','$149.99'
-- '208','13','$599.96'
-- '301','24','$599.97'
-- '301','9','$799.96'
-- '302','6','$924.95'
-- '302','25','$699.96'
-- '303','18','$199.99'
-- '304','14','$184.99'
-- '305','19','$349.98'
-- '305','3','$349.98'
-- '306','No Reservations','No Reservations'
-- '307','5','$524.97'
-- '308','1','$299.98'
-- '401','22','$1199.97'
-- '401','17','$1259.97'
-- '401','11','$1199.97'
-- '402','No Reservations','No Reservations'



-- 5. Write a query that returns all rooms with a capacity of three or more and 
--    that are reserved on any date in April 2023.
SELECT ROOM.roomID, ROOM.maximumOccupancy,
	RESERVATION.startDate, RESERVATION.endDate
FROM RESERVATION
INNER JOIN ROOM_GUEST_RESERVATION ON RESERVATION.reservationID = room_guest_reservation.reservationID
INNER JOIN ROOM ON room_guest_reservation.roomID = ROOM.roomID
WHERE ROOM.maximumOccupancy > 2 AND ((RESERVATION.startDate BETWEEN '2023-4-1' AND '2023-4-30') OR (RESERVATION.endDate BETWEEN '2023-4-1' AND '2023-4-30'));
-- '301','4','2023-04-09','2023-04-13'


-- 6. Write a query that returns a list of all guest names and the number of reservations per guest, 
--    sorted starting with the guest with the most reservations and then by the guest's last name.
SELECT GUEST.firstName, GUEST.lastName,
	COUNT(bridge.reservationID) AS NumberOfReservations
FROM RESERVATION
INNER JOIN ROOM_GUEST_RESERVATION bridge ON RESERVATION.reservationID = bridge.reservationID
INNER JOIN GUEST ON bridge.guestID = GUEST.guestID
GROUP BY bridge.guestID
ORDER BY NumberOfReservations DESC, GUEST.lastName;
-- 'Mack','Simmer','4'
-- 'Bettyann','Seery','3'
-- 'Duane','Cullison','2'
-- 'Ollie','Dickson','2'
-- 'Walter','Holaway','2'
-- 'Aurore','Lipton','2'
-- 'Maritza','Tilton','2'
-- 'Joleen','Tison','2'
-- 'Wilfred','Vise','2'
-- 'Karie','Yang','2'
-- 'Zachery','Luechtefeld','1'


-- 7. Write a query that displays the name, address, and phone number of a guest based on their phone number. 
SELECT GUEST.firstName, GUEST.lastName, GUEST.address, GUEST.phone
FROM GUEST
WHERE GUEST.phone = '(214) 730-0298';
-- 'Karie','Yang','9378 W. Augusta Ave.','(214) 730-0298'


SELECT GUEST.firstName, GUEST.lastName, GUEST.address, GUEST.phone
FROM GUEST
ORDER BY GUEST.phone;
-- 'Karie','Yang','9378 W. Augusta Ave.','(214) 730-0298'
-- 'Joleen','Tison','87 Queen St.','(231) 893-2755'
-- 'Mack','Simmer','379 Old Shore Street','(291) 553-0508'
-- 'Duane','Cullison','9662 Foxrun Lane','(308) 494-0198'
-- 'Aurore','Lipton','762 Wild Rose Street','(377) 507-0974'
-- 'Maritza','Tilton','939 Linda Rd.','(446) 351-6860'
-- 'Walter','Holaway','7556 Arrowhead St.','(446) 396-6785'
-- 'Bettyann','Seery','750 Wintergreen Dr.','(478) 277-9632'
-- 'Zachery','Luechtefeld','7 Poplar Dr.','(814) 485-2615'
-- 'Wilfred','Vise','77 West Surrey Street','(834) 727-1001'
-- 'Ollie','Dickson','123 Fake Street','(999) 620-2411'