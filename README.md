This is a solution to https://github.com/yurachud/homework.

Prerequisites to start the application:

* Java (developed with JDK 1.8)
* Maven (developed with 3.2.5)
    
    
To run the application on the embedded jetty server:
```
    mvn jetty:run
```

Optional parameters:
* `max.applications` - maximum amount of accepted loan applications from one country per time frame (default value is 5)
* `timeframe.seconds` - size of the timeframe in seconds (default value is 3)
    
For example, the app started with the following command
```
	mvn jetty:run -Dmax.applications=1 -Dtimeframe.seconds=10 
```
will not accept more than 1 loan application from one country per 10 seconds.

To run tests:
```
	mvn test
```

***Provided RESTful services***
----

**Apply for loan**

* **URL**
  /loan

* **Method:**
  `POST`
  
* **Headers:**
  `Content-Type: application/x-www-form-urlencoded`
  
*  **URL Params**

   None

* **Data Params**

  *Required:*
  
    ```
    amount: amount of money loaned [double]
    personalId: personal ID of a borrower [string]
    term: loan term [date, format - dd-MM-yyyy]
    name: person's name [string]
    surname: person's surname [string]
    ```

* **Success Response:**

  * **Code:** 201
 
* **Error Response:**

  * **Code:** 400

* **Comment**

  The user is looked up in the DB by `personalId`. If the user does not exist, he is being created with the provided params - `name`, `surname` and `personalId`.

----

**List all approved loans**

* **URL**
  /loan/all
* **Method:**
  `GET`
*  **URL Params**

   None

* **Data Params**

   None

* **Success Response:**

  * **Code:** 200
 
* **Error Response:**

  * **Code:** 400

----

**List all approved loans by user**

* **URL**
  /loan
* **Method:**
  `GET`
*  **URL Params**

   *Required:*

   ```
    	personalId: personal ID of a borrower [string]
   ```

* **Data Params**

   None

* **Success Response:**

  * **Code:** 200
 
* **Error Response:**

  * **Code:** 400

----

**Add user to blacklist**

* **URL**
  /blacklist
* **Method:**
  `POST`
* **Headers:**
  `Content-Type: application/x-www-form-urlencoded`
*  **URL Params**

   None

* **Data Params**

   *Required:*

   ```
    	personalId: personal ID of a borrower [string]
    ```

* **Success Response:**

  * **Code:** 200
 
* **Error Response:**

  * **Code:** 400

----

**Remove user from blacklist**

* **URL**
  /blacklist
* **Method:**
  `DELETE`
*  **URL Params**

   *Required:*

   ```
    	personalId: personal ID of a borrower [string]
   ```

* **Data Params**

   None

* **Success Response:**

  * **Code:** 200
 
* **Error Response:**

  * **Code:** 400
