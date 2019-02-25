CREATE TABLE IF NOT EXISTS Items
(
    id IDENTITY,
    title varchar(300) NOT NULL,
    description varchar,
    price real NOT NULL
);

CREATE TABLE IF NOT EXISTS Locations
(
    id IDENTITY,
    country varchar(100) NOT NULL,
    city varchar(100) NOT NULL,
    street varchar(200) NOT NULL,
    log real,
    lat real,
);

CREATE TABLE IF NOT EXISTS Items_Locations
(
    item_id BIGINT NOT NULL,
    location_id INT NOT NULL,
    stock INT,
);
ALTER TABLE Items_Locations ADD FOREIGN KEY (item_id) REFERENCES Items(id) ON DELETE CASCADE;
ALTER TABLE Items_Locations ADD FOREIGN KEY (location_id) REFERENCES Locations(id) ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS idx_items_location_id ON Items_Locations (item_id, location_id);

INSERT INTO Locations VALUES(1, 'UK', 'London', 'Victoria str. 22', 22.456, 18.3654);
INSERT INTO Locations VALUES(2, 'US', 'New York', 'Wall St. 10', 32.456, 74.3654);
INSERT INTO Locations VALUES(3, 'Germany', 'Munich', 'Waldheimplatz 40', 40.456, 22.3654);

INSERT INTO Items VALUES(1, 'Dremel Digilab 3D20 3D Printer', 'Countless hours of high quality printing with the most reliable low-cost 3D printer
                                                                         Quickly and easily make your 1st print out of the box with completely pre-assembled printer, included 0.5kg spool of filament and clear setup instructions.Maximum Build Depth (inches): 9 Maximum Build Height (inches): 5.9 Maximum Build Width (inches): 5.5
                                                                         Far superior safety compared to competition with fully enclosed design, non-heated build plate, PLA-only printing and 3rd party UL safety approval
                                                                         Coming Soon! Compatibility with brand new Dremel Digilab 3D Slicer based on Cura, the industry leading open source 3D printing software and .gcode files so you can use your favorite slicer
                                                                         Accompanied by unparalleled local Dremel customer service support and industry’s best 1-year warranty.Extruder temperature: Up to 230°C /397°F', 599);
INSERT INTO Items VALUES(2, 'Comgrow Creality Ender 3 3D Printer', 'Resume Print: Ender 3 has the ability to resume printing even after a power outage or lapse occurs.
                                                                              Easy and Qucik Assembly: It comes with several assembled parts, you only need about 2 hours to assemble 20 nuts well.
                                                                              Advanced Technology: Upgraded extruder greatly reduces plugging risk and bad extrustion; V-Slot with POM wheels make it move noiseless, smoothly and durable.
                                                                              Safety protected power supply; Only needs 5 minutes for hot bed to reach 110℃
                                                                              Strict Test: Strict testing for key components before delivery and life-time technical supports.', 299.99);
INSERT INTO Items VALUES(3, 'Monoprice Maker Select 3D Printer', 'Includes Heated Build Plate, 2 Gb Micro Sd Card and Sample PLA Filament. Compatible softwares: Such as Cure, Repeater, or Simplify 3D, the Maker Select is compatible with Windows, Mac OS X, and Linux
                                                                            Quality Assurance/Technical Assistance: Live chat at Monoprice.com Mon through Fri 6am – 6pm PST | Email at tech@monoprice.com | Phone at 877-271-2592 Mon through Fri 6am – 5pm PST.
                                                                            Large volume: The large 8 x 8 inches build plate and generous 7 inches vertical spacing means that you can print larger, more complex models. Note: Ensure that the nozzle temperature is set to the correct value for the type of material being printed.
                                                                            Check that the GCODE file is complete and not corrupted. Check the original model file. Heated build plate: The heated build plate allows you to print slower cooling materials, such as ABS and modified ABS, as well as PLA and PLA blends
                                                                            Micro SD card slot: Use a micro SD card to store sliced G Code files, then plug the card into the micro SD card slot to print without the need to connect to a Windows or Mac PC. Printable Materials- ABS, PLA, XT Copolyester, PET, TPU, TPC, FPE, PVA, HIPS, Jelly, Foam, Felty. Max. Extruder Temp- 500°F (260°C)', 239.99);
INSERT INTO Items VALUES(4, 'LulzBot Mini Desktop 3D Printer', 'Enjoy awesome ease-of-use features including auto-bed leveling, auto-nozzle cleaning, an easy carry handle, new Cura software, and a low maintenance PEI print surface
                                                                          Print Volume: 6in x 6in x 6.2in (152mm x 152mm x 158mm). Layer resolution as fine as 50 micron and as coarse as 500 micron, depending on part geometry and speed/finish requirements
                                                                          Maximum temperatures of 300°C (572°F) for the hot end and 120°C (248°F) open a world of material possibilities. Top Print Speed: 275mm/sec (10.8 in/sec) at 0.18 mm layer height
                                                                          Modular tool head carriage design allows plug-and-play with different print heads for growing selection of 3D printing filament materials
                                                                          Freedom to use any number of 3D printing software programs, Cura LulzBot Edition comes standard. Other compatible software includes OctoPrint, BotQueue, Slic3r, Printrun, MatterControl, and more', 1513.31);
INSERT INTO Items VALUES(5, 'MakerBot Replicator+ 3D Printer', 'Camera resolution : 640 x 480. ENGINEERED AND EXTENSIVELY TESTED FOR RELIABLE, FASTER 3D PRINTING: The MakerBot Replicator+ 3D Printer is consistently rated the best desktop 3D printer for professionals, educators, and hobbyists.
                                                                          AN INTELLIGENT 3D PRINTING WORKFLOW: The MakerBot Replicator+ 3D Printer includes streamlined workflow features including the Smart Extruder+, a full-color LCD display, Wi-Fi connectivity, and an on-board camera for remote monitoring via MakerBot Mobile.
                                                                          QUICK-START WIRELESS SETUP: Get started 3D printing right out of the box with a pre-leveled build plate and a guided wireless setup via MakerBot Mobile. Stepper Motors : 1.8° step angle with 1/16 micro-stepping
                                                                          FASTER PRINT TIMES AND LARGER BUILD VOLUME: The MakerBot Replicator+ 3D Printer is approximately 30 percent faster than the MakerBot Replicator 5th Generation Desktop 3D Printer and features a 25 percent larger build volume..Power Requirements: 100-­240 V, 50-60 HZ,0.76-0.43 A
                                                                          ADVANCED MAKERBOT PRINT 3D PRINTING SOFTWARE INCLUDED: Store, organize, and access 3D design files in your Cloud-enabled library before preparing for print. MakerBot Print is compatible with Mac and Windows computers and guides the 3D printing workflow from start to finish.
                                                                          MAKERBOT THINGIVERSE: Access millions of free downloadable files for printing with your MakerBot Replicator+ 3D Printer at the world''s largest 3D printing marketplace.', 2799.00);

INSERT INTO Items_Locations VALUES(1, 1, 50);
INSERT INTO Items_Locations VALUES(1, 2, 30);
INSERT INTO Items_Locations VALUES(1, 3, 10);

INSERT INTO Items_Locations VALUES(2, 1, 5);
INSERT INTO Items_Locations VALUES(2, 2, 3);

INSERT INTO Items_Locations VALUES(3, 3, 10);

INSERT INTO Items_Locations VALUES(4, 1, 15);
INSERT INTO Items_Locations VALUES(4, 2, 100);
INSERT INTO Items_Locations VALUES(4, 3, 50);

INSERT INTO Items_Locations VALUES(5, 2, 100);
INSERT INTO Items_Locations VALUES(4, 3, 200);
