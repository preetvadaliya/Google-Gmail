<div align = center>
<img src = "https://img.icons8.com/color/128/000000/gmail-new.png">
<h1>Google Gmail Extension</h1>
</div>

**Extension Name:** Google Gmail
**Description:** A Non-Visible component that helps you to send mail from your App Inventor App using **Google App Script**.
**Date Buit:** [date=2022-01-19 timezone="Asia/Kolkata"]
**Requirements:** Google Account, App Script deployment key


## Blocks


### Error Occurred

![component_event(1)|616x170, 50%](https://community.appinventor.mit.edu/uploads/default/original/3X/5/9/59f62a0e1d60deb70534df7e0cbb990b17e01a6d.png)

**Description:** Event raised when the error occurred from network connectivity.
**Params:** errorMessage(String message generated when the error occurred.)

### Mail Send

![component_event(2)|546x170, 50%](https://community.appinventor.mit.edu/uploads/default/original/3X/c/7/c7c41877c2a40b966bf6442df4dca6a39238e000.png)

**Description:** Event raised when code execution completed from **Google App Script**, if mail not sent then also this event raised with appropriate status and message.
**Params:** status("ok" if mail sent successfully), message("mail sent successfully" if mail sent successfully else error message)

### Send Mail

![component_method(1)|532x208, 50%](https://community.appinventor.mit.edu/uploads/default/original/3X/d/6/d60bf56746edceef05ca26bec314cd41fc859258.png)

**Description:** Call App Script function to send message to given **mailId** with given **subject** and **body**.
**Params:** mailId(Email Address of the recipients), subject(Subject of the mail), body(Body text of the mail).

### Deployment Key

![component_set_get|690x52, 50%](https://community.appinventor.mit.edu/uploads/default/original/3X/3/7/3708c1e06e1db3b59fb9fa95a70866eda479cc09.png)

![component_set_get(1)|596x52, 50%](https://community.appinventor.mit.edu/uploads/default/original/3X/6/a/6afd1a7c04a929cf6d261d54c621a0f0d667b567.png)

**Description:** Set your App Script deployment key.


## Google App Script

``` javascript

function doPost(request)  {
  let emailId = request.parameters.mail_Id;
  let emailBody = request.parameters.body;
  let emailSub = request.parameters.sub;
  const output = {};
  try {
    GmailApp.sendEmail(emailId, emailSub, emailBody);
    output['status'] = 'ok';
    output['msg'] = 'mail sent successfully';
  } catch (err) {
    output['status'] = 'error';
    output['msg'] = err;
  }
  return ContentService.createTextOutput(JSON.stringify(output)).setMimeType(ContentService.MimeType.JSON);
}

```

## Notes
1. At the deployment time it may ask you to grant permission to access the data related to Gmail.
2. Don't share your deployment key with others(Even me also).
3. Deployment Configurations (Execute as: Me (Your Mail Id), Who has access: Anyone)


## Download Link
[com.google.preet.aix](https://community.appinventor.mit.edu/uploads/short-url/86eoaAsiTZGWgSpdAY8lGHDJOd7.aix) (10.8 KB)

<br>

Happy App Inventing   
Preet P. Vadaliya
