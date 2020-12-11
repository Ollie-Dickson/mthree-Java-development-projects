-- Ollie Dickson
-- 03/12/2020

DROP DATABASE IF EXISTS hotel;
CREATE DATABASE hotel;
USE hotel;

CREATE TABLE ROOM (
    roomID INT PRIMARY KEY AUTO_INCREMENT,
    roomType VARCHAR(10) NOT NULL,
    hasMicrowave TINYINT NOT NULL,
    hasJacuzzi TINYINT NOT NULL,
    hasRefrigerator TINYINT NOT NULL,
    hasOven TINYINT NOT NULL,
    AdaAccessible TINYINT NOT NULL,
    standardOccupancy TINYINT NOT NULL,
    maximumOccupancy TINYINT NOT NULL,
    basePrice DECIMAL(6,2) NOT NULL,
    extraPerson DECIMAL(4,2)
);

CREATE TABLE GUEST (
	guestID INT PRIMARY KEY AUTO_INCREMENT,
	firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    address VARCHAR(300) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip CHAR(5) NOT NULL,
    phone VARCHAR(16) NOT NULL
);

CREATE TABLE RESERVATION (
	reservationID INT PRIMARY KEY AUTO_INCREMENT,
	adults TINYINT NOT NULL,
    children TINYINT NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    totalCost DECIMAL(6,2) NOT NULL
);

CREATE TABLE ROOM_GUEST_RESERVATION (
    roomID INT NOT NULL,
    guestID INT NOT NULL,
    reservationID INT NOT NULL,
    PRIMARY KEY pk_room_guest_reservation (roomID, guestID, reservationID),
    FOREIGN KEY pk_room_guest_reservation_room (roomID)
        REFERENCES ROOM(roomID),
    FOREIGN KEY pk_room_guest_reservation_guest (guestID)
        REFERENCES GUEST(guestID),
	FOREIGN KEY pk_room_guest_reservation_reservation (reservationID)
        REFERENCES RESERVATION(reservationID)
);