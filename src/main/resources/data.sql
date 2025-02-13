INSERT INTO roles(name) SELECT 'ROLE_USER' WHERE NOT EXISTS (SELECT name FROM roles WHERE name = 'ROLE_USER');
INSERT INTO roles(name) SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT name FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO users (email, name, surname, password, account_verified)
VALUES
	('andrejs@ss.com', 'Andrejs', 'Matvejevs', 'am', true),
	('ruslan@uni.lv', 'Ruslan', 'Dzhubuev', 'rs', false),
	('anton@work.lt', 'Anton', 'Den', 'ad', true),
	('lelde@gmail.com', 'Lelde', 'Brosova', 'lb', true),
	('ieva@gmail.com', 'Ieva', 'Krigere', 'ik', true),
	('janis@ss.lv', 'janis', 'berzins', 'jb', false),
	('peusersteris@sludinajumi.lv', 'peteris', 'kalnins', 'pk', true);

INSERT INTO categories (type, name, description)
VALUES
	('EMPLOYMENT', 'Vacancy', 'Job offers'),
	('EMPLOYMENT', 'Training course', 'All types of courses, including bootcamps'),
	('CONSTRUCTION', 'Construction material', 'All kinds of materials: from bricks to lumber to roof tiles'),
	('CONSTRUCTION', 'Service', 'From architecture to demolition'),
	('HOBBY', 'Book', 'From fiction to dictionary'),
	('HOBBY', 'Fishing', 'Fishing equipment, company etc.'),
	('TRANSPORT', 'Car', 'for moving people'),
	('TRANSPORT', 'Truck', 'for moving goods'),
	('REAL_ESTATE', 'House', 'Detatched and semi-detatched habitable housing units'),
	('REAL_ESTATE', 'Apartment', 'Single-family housing units in multi-apartment buildings'),
	('CLOTHING', 'Footware', 'All you can wear on your feet'),
	('CLOTHING', 'Accessory', 'From bags to scarves to hats'),
    ('ANIMALS', 'Pet', 'from fish to cat to horse'),
    ('ANIMALS', 'Pet food', 'food for pets'),
    ('ELECTRONICS', 'TV', 'TV sets of all shapes and sizes'),
   	('ELECTRONICS', 'Computer', 'From laptops to gaming machines'),
   	('FOR_HOME', 'Home appliance', 'Hairdriers, microwave ovens, washing machines etc.'),
   	('FOR_HOME', 'Furniture', 'Tables, chairs, wardrobes etc.'),
   	('MANUFACTURING', 'Manufacturing material', 'Production materials'),
   	('MANUFACTURING', 'Machinery', 'Industrial manufacturing equipment'),
   	('AGRICULTURE', 'Equipment', 'Tractors, harvesters etc.'),
   	('AGRICULTURE', 'Chemical', 'Fertilisers etc.'),
   	('OTHER', 'other', 'Everything else not listed in other categories');