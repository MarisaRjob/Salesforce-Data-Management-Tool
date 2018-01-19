# Salesforce-Data-Management-Tool
A Java tool to manage accounts and authorize permissions, import and export data from Salesforce system. 

## Login Page
Administrators can login with their production salesforce account or their sandbox salesforce account. 

The login button will change color and content according to the content of the username to remind users if it is production account or sandbox account.

The username and password will be stored in the java.util.prefs.Preferences if the "Save credential" is checked.

![1](https://user-images.githubusercontent.com/23087939/35136937-76b667d0-fc9b-11e7-8e17-75cf3c1f243e.PNG)

## Main Page
Users can manage Salesforce Action items:
* View action items and details
* Assign action items to specific administrator
* Change the status of action items
* Auto Complete action items

![salesforce tool](https://user-images.githubusercontent.com/23087939/35135625-4684039a-fc93-11e7-8a30-85b350903fd8.PNG)

## AssignTo Page
Assign action items to specific administrator

![assignto](https://user-images.githubusercontent.com/23087939/35137177-d84b23f4-fc9c-11e7-8743-3b0c18bc9653.PNG)

## Change Status Page
Change the status of action items.

#### Status List: New, Open/Review, In Process, Cancelled, Completed, Deferred, Duplicate, Awaiting Response, Sent to Development, Deployment Pending, Ready for User Sign-off, User Signed off.
![3](https://user-images.githubusercontent.com/23087939/35137461-5bb643c6-fc9e-11e7-91da-5311ca9639f1.PNG)
