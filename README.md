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

`gradle(w) clean build`
## Running application
To run application using gradle:

`gradle(w) -Dlog4j.configurationFile=.\log4j2.properties run`

where log4j file properties are specified using system variable `log4j.configurationFile`.

## Running tests 
To run application tests using gradle:

`gradle(w) test`

## REST Api
REST Api can be accessed using this url:

GET `http:\\localhost:<port>\items` - gets all items<br/>
GET `http:\\localhost:<port>\items\{id}` - gets item by id<br/>
DELETE `http:\\localhost:<port>\items\{id}` - deletes item by id<br/>
POST `http:\\localhost:<port>\items` - adds new item<br/>
PATCH `http:\\localhost:<port>\items\{id}` - update existing item<br/>

for now port is hardcoded and is `9081` 
