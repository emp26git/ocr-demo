# ocr-demo
This is an application that accepts a single String of data input which is meant to represent an OCR scan of a business card. The application will process the specified input String and attempt to find: (1) the person's name, (2) the contact phone number, and (3) the contact email address. This information is maintained in the ContactInfo.

In order to support user input, I created a Java Swing UI with a TextArea component where the contact card data can be entered. After the data has been entered, clicking the Scan button will trigger the processing of the data by the BusinessCardParser class. Once the data has been processed, a notification dialog is displayed to show the processing results for the Name, Phone, and Email.

The application is a stand-alone Java Jar file that can be built with Maven

There is a set of unit tests for the main BusinessCardParser class. These tests can be executed via Maven.
