
# parking_spring_app
## Steps to run this app:



**Step #1 (Configurations):**
 - Run a mysql server on localhost at 3306 port and create user **root** with password **root**
 - create databse into it by command `create database parkingApp;` 	
 - Run following DDL.
  
 ```CREATE TABLE `parking_ticket` (
  `slot_no` int(11) DEFAULT NULL,
  `driver_age` int(11) DEFAULT NULL,
  `vehicle_reg_no` varchar(100) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
); ```


 **Note: You can check application.properties file for conf related changes

**Step #2 (Run the app):**
 - Run ParkingApplication.java
 -  Open browser tab and type 'localhost:9090/parking?filepath=<input.txt file path>' where provide filepath of the input.txt file. eg: `http://localhost:9090/parking?filepath=/home/ss/me/parking/input.txt`
 - Sample input file: [https://gist.github.com/tarungarg546/6200f936f2208bad5d9d0e053d773489](https://gist.github.com/tarungarg546/6200f936f2208bad5d9d0e053d773489)

  
**Step #3 (Result):**
 - Results are store in output.txt file and get print on the terminal also.
 - Result of sample input file: [https://gist.github.com/tarungarg546/697334d7990f758ac77a8accdf3efd98](https://gist.github.com/tarungarg546/697334d7990f758ac77a8accdf3efd98)

