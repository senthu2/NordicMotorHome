Drop database if exists NMP;
create database NMP;

use NMP;

create table cars
(
	car_id			INT 	PRIMARY KEY 	NOT NULL 	AUTO_INCREMENT,
    brand 			VARCHAR(45),
    model 			VARCHAR(45),
    beds			INT
);  

create table documentation
(
	documentation_id	INT 	PRIMARY KEY 	NOT NULL 	AUTO_INCREMENT,
    car_status			VARCHAR(50),
    car_registration	INT,
    mileage				INT,
    price_group			VARCHAR(1),
    car_id				INT,
    -- CONSTRAINT documentation_fk_cars
	FOREIGN KEY (car_id)
    REFERENCES cars (car_id)
);

CREATE TABLE location
(
location_id			INT 	PRIMARY KEY 	NOT NULL 	AUTO_INCREMENT,
city				VARCHAR(30),
zip_code			INT
);

CREATE TABLE address
(
address_id 			INT 	PRIMARY KEY 	NOT NULL 	AUTO_INCREMENT,
-- location_id 		int,
street				VARCHAR(30),
house_num			INT,
location_id			INT,
-- CONSTRAINT address_fk_location
	FOREIGN KEY (location_id) 
    REFERENCES location(location_id)
);

CREATE TABLE customer
(
	customer_id			INT 	PRIMARY KEY 	NOT NULL 	AUTO_INCREMENT,
    -- address_id   		int,
	cus_first_name		VARCHAR(50),
	cus_last_name 		VARCHAR(50),
	driver_licence		INT 			UNIQUE,
	address_id			INT,
-- CONSTRAINT customer_fk_address
	FOREIGN KEY (address_id)
    REFERENCES address(address_id)
);

create table Rentals
(
	rental_id int   primary key   not null  auto_increment    unique,
    documentation_id int, 
    customer_id int, 
    from_Date date,
    to_Date date, 
    days_of_rental int,
    total_price double,
    rent_table  boolean  default false,
	rent_chairs  boolean  default false,
	rent_car_seat  boolean  default false,
	rent_bike_rack  boolean  default false,
	rent_bed_linnen  boolean  default false,
    tank_Filled boolean default false, 
    overDriven int default 0, 

	foreign key (documentation_id) REFERENCES documentation(documentation_id),
    foreign key (customer_id) references customer(customer_id)
    
    );

create table cancel
(
		 cancel_id    int primary key   not null   unique   auto_increment,
         cancel_price  int,
         cancel_days  int
);

CREATE TABLE staff
(
staff_id			INT 	PRIMARY KEY 	NOT NULL 	AUTO_INCREMENT,
staff_first_name		VARCHAR(50),
staff_last_name 		VARCHAR(50),
address_id			INT,
-- CONSTRAINT staff_fk_address
	FOREIGN KEY (address_id)
    REFERENCES address(address_id)
);

create table PickUpPoints
(
	PickUP_id   int   primary key   not null  auto_increment,
    place  varchar(45),
    price  double
);

create table price_group
(
	price_id int auto_increment not null primary key,
    price_group varchar(1),
    price double,
    FOREIGN KEY (price_group)
    REFERENCES documentation(price_group)
    
);

create table seasons 
(
	season_id int  not null primary key,
    from_date date,
    to_date date,
    price_factor double
);

insert into seasons values
(1, '2020-04-01', '2020-06-30', 1.30),
(2, '2020-7-01', '2020-08-15', 1.60),
(3, '2020-08-16', '2020-10-31', 1.30),
(4, '2020-11-01', '2021-03-30', 1.0);

insert into price_group values
(1, 'A', 110),
(2, 'B', 134),
(3, 'C', 160);


insert into PickUpPoints values
(1, 'KBH lufthavn', 10.5),
(2, 'Helsingør færgehavn', 30),
(3, 'SCANDIC PALACE HOTEL', 3),
(4, 'SCANDIC KØDBYEN HOTEL', 3.5);

INSERT INTO cars VALUES
(1, 'Ford', 'Focus', 4),
(2, 'Ferrari','Campero', 4),
(3, 'Mercedes', 'Camp', 3),
(4, 'WW', 'Wroom', 3),
(5, 'Fiat', 'Voyager', 6);

INSERT INTO documentation VALUES
(1, 'Available', 2371227, 400, l, 1),
(2, 'Reperation', 4258431, 1000, l,  2),
(3, 'Available', 396982, 6000, m, 3),
(4, 'Unavailable', 32991436, 5040, m, 4),
(5, 'Unavailable', 23627373, 100000, h, 5);

INSERT INTO location VALUES
(1, 'Roskilde', 4000),
(2, 'CPH', 2200),
(3, 'Mediolan', 3),
(4, 'Berlin', 484),
(5, 'Palermo', 50);

INSERT INTO address VALUES
(1, 'Arturrovej',22 , 1),
(2, 'Vejgade', 65, 2),
(3, 'Gadevej',89 , 3),
(4, 'Stivej', 23, 4),
(5, 'Stigade', 125, 5),
(6, 'Københavnsvej',22 , 2),
(7, 'Gadegade', 65, 2),
(8, 'Algade',89 , 2),
(9, 'Dørvej', 23, 2),
(10, 'Yes', 125, 5);

INSERT INTO customer VALUES
(1, 'Artur','Radzi',27899855, 1),
(2, 'John','Mazda',23456789, 2),
(3, 'Senthu','Toyota',987654, 3),
(4, 'Andrew','Nissan',678234, 4),
(5, 'Kate','Renault',76543098, 5);

INSERT INTO staff VALUES
(1, 'Tony','Boi', 6),
(2, 'Anthony','Hopkins', 7),
(3, 'Mark','Dark', 8),
(4, 'Marie','Marieson', 9),
(5, 'Ann','Olsen', 10);

INSERT INTO Rentals (rental_id, documentation_id, customer_id, from_Date, to_Date, days_of_rental, total_price, rent_table, rent_chairs, rent_car_seat, rent_bike_rack, rent_bed_linnen)
VALUES 
(1, 4, 2, '2020-05-24', '2020-05-30', 6,3000, false, true, false, true, false),
(5, 5, 5, '2020-06-21','2020-07-21', 30,5000, false, false, false, false, false);


insert into cancel values 
(1, 20, 50),
(2, 50, 49),
(3, 80, 15),
(4, 95, 0);