# ocr-demo
This is an application that accepts a single String of data input which is meant to represent an OCR scan of a business card. 
The application will process the specified input String and attempt to find: (1) the person's name, (2) the contact phone number, 
and (3) the contact email address. This information is maintained in the ContactInfo.

In order to support user input, I created a Java Swing UI with a JTextArea component where the contact card data can be entered. 
After the data has been entered, clicking the Scan button will trigger the processing of the data by the BusinessCardParser class. 
Once the data has been processed, a notification dialog is displayed to show the processing results for the Name, Phone, and Email.

To access the application source, clone the Git repository to a directory on your local machine:
```
% cd folder/to/clone-into/
% git clone https://github.com/emp26git/ocr-demo.git
```

After cloning the repository, the application can be built using Maven:
```
% cd ocr-demo/ocr-demo
% mvn clean package
```

This should build the executable jar file:
```
target/ocr-demo-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

To execute the application, "cd" to the target directory and run the executable jar:
```
% cd target
% java -jar ocr-demo-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

There is a set of unit tests for the main BusinessCardParser class. These tests can be executed via Maven:
```
% mvn test
```

This application requires:
- Java JDK 8 update 212 or higher
- Maven 3.6.1 or higher
- JUnit 5.4.2 (Jupiter) or higher
- Apache OpenNLP 1.9.1
- Log4j 2.11.2
- Apache Commons-Lang3 3.9
- Apache Commons-Validator 1.6

