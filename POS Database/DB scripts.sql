CREATE DATABASE IF NOT EXISTS POS;
USE POS;

/*	
	drop database POS;
	drop table users;
*/
/*************************** TABLES *******************************/

CREATE TABLE Employee(
	employeeID			int auto_increment,
    employeeFirstName 	varchar(30),
    employeeMiddleName	varchar(30),
    employeeLastName 	varchar(30),
    jobPosition			varchar(20),
    phoneNumber			varchar(10),
    address				varchar(100),
    city				varchar(25),
    state				varchar(2),
    zipCode				varchar(5),
    dateHired			int,
    ssn					varchar(9),
    wage				numeric(6, 2),
    isSalary			boolean,		-- hourly=false, salary=true
    primary key (employeeID)
);

CREATE TABLE Users(
	userID 			varchar(20) not null,
    userPassword 	varchar(20) not null,
    employeeID		int,
    isActive		boolean,
    primary key (userID),
    constraint fk_eID foreign key (employeeID) references Employee(employeeID)
		on delete restrict on update cascade
);

CREATE TABLE Category(
	categoryID			varchar(5) not null,
    categoryName		varchar(50) not null,
    categoryDescription	varchar(100),
    discontinued		boolean,
    primary key (categoryID)
);

CREATE TABLE Product(
	productID			varchar(10) not null,
    productName			varchar(50) not null,
    productDescription	varchar(100),
    categoryID			varchar(5) not null,
    unitPrice			numeric(8, 2),
    stock				int,
    discontinued		boolean,
    primary key (productID),
    constraint fk_cID foreign key (categoryID) references Category(categoryID)
		on delete restrict on update cascade
);

CREATE TABLE Orders(
	orderID				varchar(12) not null,
    orderDate			int not null,
	userID				varchar(20) not null,
    salesTax			numeric(8, 2),
    totalCash			numeric(8, 2),
    totalCreditCard		numeric(8, 2),
    orderComment		varchar(200),
    primary key (orderID),
    constraint fk_uID foreign key (userID) references Users(userID)
		on delete restrict on update cascade
);

CREATE TABLE todaySalesCounter(num int primary key);	-- helps to generate OrderID
insert into todaySalesCounter values(0);
CREATE TABLE currentLanguage(lang varchar(2), country varchar(2));	-- saves language preference of user
insert into currentLanguage values("en", "US");		-- default values

/* 
drop table todaySalesCounter;
drop table OrderDetails;
drop table Orders;
*/

CREATE TABLE OrderDetails(
    orderID				varchar(12) not null,
    productID			varchar(10) not null,
    quantity			int,
    unitPrice			numeric(8, 2),
    discount			numeric(8, 2),
    -- primary key (orderDetailID),
    constraint fk_oID foreign key (orderID) references Orders(orderID)
		on delete restrict on update cascade,
	constraint fk_pID foreign key (productID) references Product(productID)
		on delete restrict on update cascade
);

/*********************** DATA TEST *********************************/
insert into Employee values (1, "Kelsey", null,"Hansen","Cashier", "8189567412","18652 Westgate Ave.", "West LA", "CA", "90025",1417428000, "123456789", 10, false);
insert into Employee values (2, "Richard", null, "Galdon", "Chef", "4248567472", "5234 Tujunga Blvd.", "North Hollywood", "CA", "91505", 1417498200, "666554321", 2000, true);
insert into Employee values (3, "Karina", null, "Robles", "Cashier", "3102527898", "19230 Culver Blvd.", "Culver City", "CA", "92323", 1417500000, "683711299", 10, false);

insert into Users values ("kelsey", "123456", 1, true);
insert into Users values ("galdochef", "123456", 2, false);
insert into Users values ("karina", "qwerty", 3, true);

insert into Category values ("CA001","Bakery goods","bread, cake, etc", false);
insert into Category values ("CA002","Vegetables","vegetables", false);
insert into Category values ("CA003","Beverages","all type of beverages", false);
insert into Category values ("CA004","Snacks", "chips", false);
insert into Category values ("CA005","Meats & Fish","fresh meat", false);

insert into Product values ("P000000001","Tomatoes","price per each", "CA002", 0.50, 20, false);
insert into Product values ("P000000002","Coca-Cola Soda 2.0 L","2 liters", "CA003", 4.00, 20, false);
insert into Product values ("P000000003","Coca-Cola Soda 1.0 L","1 liter", "CA003", 2.20, 30, false);
insert into Product values ("P000000004","Tampico 1 gallon","orange juice", "CA003", 4.70, 15, false);
insert into Product values ("P000000005","Lays 10 OZ","potatoe chips", "CA004", 1.50, 50, false);
insert into Product values ("P000000006","Ritz Original 200 g","only sold by carton", "CA004", 2.00, 30, false);
insert into Product values ("P000000007","Pringles Chedar Cheese 6 OZ","", "CA004", 3.20, 25, false);
insert into Product values ("P000000008","French Bread","sold by unit", "CA001", 0.50, 50, false);
insert into Product values ("P000000010","Sweet Bread","sold by unit", "CA001", 0.50, 40, false);
insert into Product values ("P000000011","Muffin","sold by unit", "CA001", 1.0, 20,false);
insert into Product values ("P000000012","Avocado","sold by unit", "CA002", 1.70, 16,false);
insert into Product values ("P000000013","Fanta Soda 2.0 L","2 litters", "CA003", 4.00, 15, false);
insert into Product values ("P000000014","Chicken Meat","sold by pound", "CA005", 3.50, 40, false);
insert into Product values ("P000000015","Fish Meat","sold by pound", "CA005", 4.50, 40, false);
insert into Product values ("P000000016","Sausage","sold by pound", "CA005", 4.50, 40, false);

select * from product

/********************** PROCEDURES *********************************/
delimiter $$
create procedure spViewUsers ()
begin
	select userID, userPassword from Users;
end$$
delimiter $$

delimiter $$
create procedure spSaveOrder (IN uID varchar(20),
							IN tax numeric(8, 2),
                            IN cash numeric(8, 2),
                            IN creditCard numeric(8, 2),
                            IN uComment varchar(200), 
                            OUT orderID varchar(12))
begin
-- generates the orderID
	set @oldNum := (select num from todaySalesCounter);
	update todaySalesCounter set num = @oldNum + 1 where num = @oldNum;
    set @numSale := (select lpad(@oldNum + 1, 4, '0') from todaySalesCounter); 
    set @dateSale := (select curdate() + 0);
    set orderID := (select concat(@dateSale, @numSale));
    
-- generates todays' date in unix timestamp
	set @unixDate := (select unix_timestamp(now()));
-- inserts data into Orders table
	insert into Orders values(orderID, @unixDate, uID, tax, cash, creditCard, uComment);
end$$
delimiter ;

delimiter $$
create procedure spSaveOrderDetails (IN orderID varchar(12), 
									 IN prodID varchar(10),
                                     IN quantity int,
                                     IN price numeric(8, 2),
                                     IN disc numeric(8, 2))
begin
    insert into OrderDetails value(orderID, prodID, quantity, price, disc);
end$$
delimiter ;

-- SPs for CATEGORIES ------------------------------------------------------------------------
delimiter $$
create procedure spAllCategories ()
begin
	select * from Category;
end$$
delimiter ;

delimiter $$
create procedure spCategoryNameLike (IN catPartialName varchar(50))
begin
	select * from Category where categoryName like concat('%', catPartialName, '%');
end$$
delimiter ;

-- generates Category ID
delimiter $$
create procedure spCreateCatID(OUT catID varchar(5))
begin
    set @catNum := (select max(categoryID) from category);
    set @catNum := (select substring(@catNum, 3, 3));
    set @catNum := (select lpad(@catNum + 1, 3, 0));	-- fills with leading zeros 
    set catID := (select concat('CA', @catNum));		-- concatenates CA###
end$$
delimiter ;

delimiter $$
create procedure spNewCategory(IN catID varchar(5), IN catName varchar(50), IN catDescription varchar(100), IN discontinued boolean)
begin
	insert into category value(catID, catName, catDescription, discontinued);
end$$
delimiter ;

delimiter $$
create procedure spUpdateCategory(IN catID varchar(5), IN catName varchar(50), IN catDescription varchar(100), IN disc boolean)
begin
	update category set categoryName = catName, categoryDescription = catDescription, discontinued = disc
    where categoryID = catID;
end$$
delimiter ;

-- SPs for EMPLOYEES -----------------------------------------------------
delimiter $$
create procedure spAllEmployees()
begin
	select * from employee;
end$$
delimiter ;

-- SPs for PRODUCTS ------------------------------------------------------
delimiter $$
create procedure spAllProducts()
begin
	select * from product;
end$$
delimiter ;

delimiter $$
create procedure spProductsByCategoryID (IN catID varchar(5))
begin
	select * from Product where categoryID = catID;
end$$
delimiter ;

delimiter $$
create procedure spProductsByID (IN prodID varchar(10))
begin
	select unitPrice from Product where productID = prodID;
end$$
delimiter ;

delimiter $$
create procedure spProductNameLike (IN prodPartialName varchar(50))
begin
	select * from Product where productName like concat('%', prodPartialName, '%');
end$$
delimiter ;

-- generates Product ID
delimiter $$
create procedure spCreateProdID(OUT prodID varchar(10))
begin
    set @prodNum := (select max(productID) from product);
    set @prodNum := (select substring(@prodNum, 2, 9));	-- (str, pos, len)
    set @prodNum := (select lpad(@prodNum + 1, 9, 0));	-- fills with leading zeros 
    set prodID := (select concat('P', @prodNum));		-- concatenates P###
end$$
delimiter ;

delimiter $$
create procedure spNewProduct(IN prodID varchar(10),
								IN prodName varchar(50),
								IN description varchar(100),
                                IN categoryID varchar(5),
                                IN unitPrice decimal(8, 2),
                                IN stock int,
                                IN discontinued boolean)
begin
	insert into product value(prodID, prodName, description, categoryID, unitPrice, stock, discontinued);
end$$
delimiter $$

delimiter $$
create procedure spUpdateProduct(IN prodID varchar(10),
								IN prodName varchar(50),
								IN description varchar(100),
                                IN categoryID varchar(5),
                                IN unitPrice decimal(8, 2),
                                IN stock int,
                                IN discontinued boolean)
begin
	update product set productName = prodName, 
						productDescription = description, 
                        categoryID = categoryID,
                        unitPrice = unitPrice,
                        stock = stock,
                        discontinued = discontinued
	where productID = prodID;
end$$
delimiter ;

delimiter $$
create procedure spChangeLanguage(IN lang varchar(2), IN country varchar(2))
begin 
	update currentLanguage set lang = lang, country = country;
end$$
delimiter ;

-- call spChangeLanguage("en","US");

delimiter $$
create procedure spSelectLanguage()
begin
	select * from currentLanguage;
end$$
delimiter ;


/*
call spSaveOrder("admin", 5.65, "blah blah", @ordID);
select @ordID;
call spSaveOrderDetails(@ordID, "NNNNNnnnnn", 2, 8.50, 0.00);

select * from Orders;
select * from OrderDetails;
select * from Product;
*/

/**************************************** TRIGGERS ***************************************************/

create trigger updateStock after insert on OrderDetails
for each row
	update Product set stock = stock - new.quantity where productID = new.productID;
    
/************************************ OPERATIONS FOR REPORTS ************************************************/
create view todaysales as
	select  h.orderID, h.salesTax, sum(d.unitPrice*d.quantity-d.discount) as total 
	from Orders h left join OrderDetails d
    on h.orderID = d.orderID
    where date(from_unixtime(h.OrderDate)) = curdate()
    group by h.orderID;

create view todaySalesByProduct as
	select p.productName, sum(d.quantity) as ItemsSold, sum(d.quantity*d.unitPrice-d.discount) as Total
    from OrderDetails d left join Product p
    on d.productID = p.productID
    left join Orders h
    on h.orderID = d.orderID
	where date(from_unixtime(h.OrderDate)) = curdate()
	group by d.productID
    order by ItemsSold desc, Total desc;
    
/* find this query directly in jasper report file
delimiter $$
create procedure TopSoldProducts (IN date1 int, IN date2 int)
begin
	SELECT p.productName, sum(od.quantity) as TotalQuantity, sum(od.quantity*od.unitPrice-od.discount) as Total
	FROM OrderDetails od 
	INNER JOIN Orders o ON od.orderID = o.orderID
	INNER JOIN Product p ON od.productID = p.productID
    WHERE o.orderDate >= date1 AND o.OrderDate <= date2
	GROUP BY p.productName
	ORDER BY TotalQuantity DESC, Total DESC;
end$$
delimiter ;
*/
