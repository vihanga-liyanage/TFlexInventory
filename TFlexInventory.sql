DROP TABLE IF EXISTS "category";
CREATE TABLE "category" (
	"cid" INTEGER PRIMARY KEY NOT NULL UNIQUE, 
	"cname" VARCHAR NOT NULL 
	);
INSERT INTO "category" VALUES(1,'Tea');
INSERT INTO "category" VALUES(2,'Flavour');
INSERT INTO "category" VALUES(3,'Herbs');
INSERT INTO "category" VALUES(4,'Flowers');
INSERT INTO "category" VALUES(5,'Fruits');
INSERT INTO "category" VALUES(6,'Leaves');
INSERT INTO "category" VALUES(7,'Other');
DROP TABLE IF EXISTS "item";
CREATE TABLE "item" (
	"code" INTEGER PRIMARY KEY NOT NULL UNIQUE, 
	"itemname" VARCHAR NOT NULL UNIQUE, 
	"category" INTEGER NOT NULL REFERENCES `category` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE, 
	"currentstock" INTEGER NOT NULL, 
	"price" FLOAT
	);
INSERT INTO "item" VALUES(1,'Aloe Vera C/cut',3,100,0);
INSERT INTO "item" VALUES(2,'Cardamom C/C',3,10,0);
INSERT INTO "item" VALUES(3,'Cardamom Pods',3,20,0);
INSERT INTO "item" VALUES(4,'Chai F/C',3,0,0);
INSERT INTO "item" VALUES(5,'Chai C/C',3,100,0);
INSERT INTO "item" VALUES(6,'Cinnamon C/C',3,0,0);
INSERT INTO "item" VALUES(7,'Clove C/cut ',3,0,0);
INSERT INTO "item" VALUES(8,'Ginger C/cut',3,0,0);
INSERT INTO "item" VALUES(9,'Gotukola C/cut',3,0,0);
INSERT INTO "item" VALUES(10,'Lemongrass C/cut',3,0,0);
INSERT INTO "item" VALUES(11,'Pink Pepper Corns',3,0,0);
INSERT INTO "item" VALUES(12,'Chilli Flakes',3,0,0);
INSERT INTO "item" VALUES(13,'Iramusu C/cut',3,0,0);
INSERT INTO "item" VALUES(14,'Liqourice  C/cut',3,0,0);
INSERT INTO "item" VALUES(15,'Strar Anise',3,0,0);
INSERT INTO "item" VALUES(16,'Rooibos C/cut ',3,0,0);
INSERT INTO "item" VALUES(17,'Polpala C/cut',3,0,0);
INSERT INTO "item" VALUES(18,'Valerian Root C/cut ',3,0,0);
INSERT INTO "item" VALUES(19,'Vanilla Pod /Chunk ',3,0,0);
INSERT INTO "item" VALUES(20,'Cornflowers ',4,0,0);
INSERT INTO "item" VALUES(21,'Chamomile ',4,0,0);
INSERT INTO "item" VALUES(22,'Sunflower C/cut ',4,0,0);
INSERT INTO "item" VALUES(23,'Linden Flower ',4,0,0);
INSERT INTO "item" VALUES(24,'Lavender ',4,0,0);
INSERT INTO "item" VALUES(25,'Marigold ',4,0,0);
INSERT INTO "item" VALUES(26,'Mallow Flower ',4,0,0);
INSERT INTO "item" VALUES(27,'Rose Petals ',4,0,0);
INSERT INTO "item" VALUES(28,'Rose F/Cut ',4,0,0);
INSERT INTO "item" VALUES(29,'Jasmine Bud ',4,0,0);
INSERT INTO "item" VALUES(30,'Jasmine Flower ',4,0,0);
INSERT INTO "item" VALUES(31,'Hibiscus C/Cut ',4,0,0);
INSERT INTO "item" VALUES(32,'Hibiscus F/Cut ',4,0,0);
INSERT INTO "item" VALUES(33,'Ranawara C/cut ',4,0,0);
INSERT INTO "item" VALUES(34,'Apple C/cut ',5,0,0);
INSERT INTO "item" VALUES(35,'Cranberry C/cut ',5,0,0);
INSERT INTO "item" VALUES(36,'Lemon Peel C/cut ',5,0,0);
INSERT INTO "item" VALUES(37,'Orange Peel C/cut ',5,0,0);
INSERT INTO "item" VALUES(38,'Passion Fruit C/cut ',5,0,0);
INSERT INTO "item" VALUES(39,'Pineapple C/cut ',5,0,0);
INSERT INTO "item" VALUES(40,'Raisins C/cut ',5,0,0);
INSERT INTO "item" VALUES(41,'Raspberry C/cut ',5,0,0);
INSERT INTO "item" VALUES(42,'Rosehip C/cut ',5,0,0);
INSERT INTO "item" VALUES(43,'Strawberry C/cut ',5,0,0);
INSERT INTO "item" VALUES(44,'Sour Cherry C/cut ',5,0,0);
INSERT INTO "item" VALUES(45,'Pepper mint leaf C/cut ',6,0,0);
INSERT INTO "item" VALUES(46,'Spearmint Leaf C/cut ',6,0,0);
INSERT INTO "item" VALUES(47,'Black Berry Leaves C/cut ',6,0,0);
INSERT INTO "item" VALUES(48,'Lemon Balm C/cut ',6,0,0);
INSERT INTO "item" VALUES(49,'Lemon Verbena C/cut ',6,0,0);
INSERT INTO "item" VALUES(50,'Lime Tree Leaves C/cut ',6,0,0);
INSERT INTO "item" VALUES(51,'Milk Chocolate ',7,0,0);
INSERT INTO "item" VALUES(52,'White Chocolate ',7,0,0);
INSERT INTO "item" VALUES(53,'Coconut Chips ',7,0,0);
INSERT INTO "item" VALUES(54,'Almond C/cut ',7,50,0);
INSERT INTO "item" VALUES(55,'Hazlenut C/Cut ',7,0,0);
INSERT INTO "item" VALUES(56,'Toffee C/Cut ',7,0,0);
INSERT INTO "item" VALUES(57,'Cocoa Chips C/cut ',7,0,0);
INSERT INTO "item" VALUES(58,'Gurana Seed C/cut ',7,0,0);
INSERT INTO "item" VALUES(59,'B.O.P.F - Gordon Estate ',1,0,0);
INSERT INTO "item" VALUES(60,'F.B.O.P Black Tea - Shawlands Estate ',1,0,0);
INSERT INTO "item" VALUES(61,'G.P.S.P Green Tea  - Radella Estate ',1,0,0);
INSERT INTO "item" VALUES(62,'Golden Tips - Pothatuwa Estate ',1,0,0);
INSERT INTO "item" VALUES(63,'Organic O.P Black Tea- Green Field Estate ',1,0,0);
INSERT INTO "item" VALUES(64,'O.P.1 Black Tea - Shawlands Estate  ',1,0,0);
INSERT INTO "item" VALUES(65,'O.P  Black Tea - Shawlands Estate  ',1,0,0);
INSERT INTO "item" VALUES(66,'Pekoe 01 Black Tea - Courtlogde Estate ',1,0,0);
INSERT INTO "item" VALUES(67,'Silver Tips - St. Coombs Estate ',1,0,0);
INSERT INTO "item" VALUES(68,'Silver Tips - Hapugastenne Estate ',1,0,0);
INSERT INTO "item" VALUES(69,'Y.H.S.P Green Tea - Ceylon Comeney Estate ',1,0,0);
INSERT INTO "item" VALUES(70,'Almond Liquid Flavour',2,0,0);
INSERT INTO "item" VALUES(71,'Apple  Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(72,'Bergamot  Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(73,'Blood Orange  Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(74,'Blueberry  Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(75,'Blueberry Flavour granules ',2,0,0);
INSERT INTO "item" VALUES(76,'Cinnamon  Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(77,'Cinnamon  Flavour granules ',2,0,0);
INSERT INTO "item" VALUES(78,'Cherry  Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(79,'Cardamom  Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(80,'Coffee Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(81,'Caramel Liquid Flavour ',2,50,0);
INSERT INTO "item" VALUES(82,'Chocolate Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(83,'Coconut Liquid Flavour  ',2,0,0);
INSERT INTO "item" VALUES(84,'Jasmine Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(85,'Honey Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(86,'Lemon Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(87,'Lime Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(88,'Orange Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(89,'Peppermint Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(90,'Peach Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(91,'Passionfruit Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(92,'Passionfruit Flavour granules ',2,0,0);
INSERT INTO "item" VALUES(93,'Pineapple Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(94,'Pineapple Flavour Granules ',2,0,0);
INSERT INTO "item" VALUES(95,'Rose Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(96,'Raspberry Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(97,'Strawberry & Cream Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(98,'Strawberry Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(99,'Strawberry Flavour granules ',2,0,0);
INSERT INTO "item" VALUES(100,'Vanilla Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(101,'new ing',3,0,0);
INSERT INTO "item" VALUES(102,'B.O.P.F Black Tea',3,0,0);
INSERT INTO "item" VALUES(103,'Red Pepper corns',3,0,0);
INSERT INTO "item" VALUES(105,'Chai Liquid Flavour',2,0,0);
INSERT INTO "item" VALUES(106,'Grapefruit Liquid Flavour ',2,0,0);
INSERT INTO "item" VALUES(107,'Lavendor Liquid Flavour',2,0,0);
INSERT INTO "item" VALUES(108,'vdxbgfbgxd',3,0,0);
INSERT INTO "item" VALUES(109,'Soursop Cut ',3,0,0);
INSERT INTO "item" VALUES(110,'Soursop Liquid Flavour',2,0,0);
DROP TABLE IF EXISTS "transaction";
CREATE TABLE "transaction" (
	"tid" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, 
	"date" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	"isout" BOOL DEFAULT 0,
	"user" INTEGER NOT NULL REFERENCES `user` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT 
	);
INSERT INTO "transaction" VALUES(1,'2017-05-01 19:43:15',0,1);
INSERT INTO "transaction" VALUES(2,'2017-05-01 19:47:24',0,1);
INSERT INTO "transaction" VALUES(3,'2017-05-02 14:50:50',1,1);
INSERT INTO "transaction" VALUES(4,'2017-05-04 08:51:46',0,1);
INSERT INTO "transaction" VALUES(5,'2017-05-04 08:51:59',1,1);
DROP TABLE IF EXISTS "transactionitem";
CREATE TABLE "transactionitem" (
	"transaction" INTEGER NOT NULL REFERENCES `transaction` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE, 
	"item" INTEGER NOT NULL REFERENCES `item` (`code`) ON DELETE CASCADE ON UPDATE CASCADE, 
	"qty" DOUBLE NOT NULL 	
	);
INSERT INTO "transactionitem" VALUES(1,54,100);
INSERT INTO "transactionitem" VALUES(1,81,50);
INSERT INTO "transactionitem" VALUES(1,1,350);
INSERT INTO "transactionitem" VALUES(2,5,100);
INSERT INTO "transactionitem" VALUES(3,1,300);
INSERT INTO "transactionitem" VALUES(3,54,50);
INSERT INTO "transactionitem" VALUES(4,54,10);
INSERT INTO "transactionitem" VALUES(5,54,10);
DROP TABLE IF EXISTS "user";
CREATE TABLE "user" (
	"uid" INTEGER PRIMARY KEY NOT NULL UNIQUE, 
	"firstname" VARCHAR NOT NULL,
	"lastname" VARCHAR NOT NULL,
	"username" VARCHAR NOT NULL UNIQUE, 
	"password" VARCHAR NOT NULL, 
	"level" INTEGER NOT NULL DEFAULT 0
	);
INSERT INTO "user" VALUES(1,'Dushyantha','De Silva','dush','F7C3BC1D808E04732ADF679965CCC34CA7AE3441',0);
