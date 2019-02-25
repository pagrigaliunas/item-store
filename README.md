# item-store
Create a REST API for imaginary "sales" platform.
It should be a simple CRUD api that allows to manage one resource - Item
API should be able to create item, update item, get a list of items, get singe item by id, delete an item. 
Item should contain this information: 
 - Title
 - Description
 - Price
 - Stock
 - Location (Country, City, Street and GPS coordinates)
Language: JAVA 8
Frameworks: candidates choice
Data base: candidates choice (preferably in memory, so we don't have to install a db instance to run a project)
Optional: 
 - Search items by title, price range, etc; Sortable by column
 - Add comment list for an item (Multiple users should be able to add comments on item)
Preferably shared via public git repo.
Project should contain instructions how to build and use the project.
## Building application
To build application, simply type:

`gradlew clean build`
## Running application
To run application using gradle:

`gradlew -Dlog4j.configurationFile=.\log4j2.properties run`

where log4j file properties are specified using system variable `log4j.configurationFile`.
## Running tests 
