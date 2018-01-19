# Salesforce-Data-Management-Tool
A Java tool to manage accounts and authorize permissions, import and export data from Salesforce system. 

## Login Page
Administrators can login with their production salesforce account or their sandbox salesforce account. 

The login button will change color and content according to the content of the username to remind users if it is production account or sandbox account.
The username and password will be stored in the java.util.prefs.Preferences if the "Save credential" is checked.

![login](https://user-images.githubusercontent.com/23087939/31187332-6fc076cc-a8e6-11e7-983e-e344c43d70d0.PNG)

## Main Page
Users can manage action items:
* View action items and details, 
* Assign action items to specific administrator,
* Change the status of action items,
* Auto Complete action items.

![salesforce tool](https://user-images.githubusercontent.com/23087939/35135625-4684039a-fc93-11e7-8a30-85b350903fd8.PNG)
