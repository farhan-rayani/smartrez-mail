#Smartrez-mail

###About
Smartrez email API is a simple Java API that uses Sun’s JavaMail API to send e-mail messages.

This is a project to Expose Mail API usage (Fluent) through REST Web Service (& Client). The API consists in an internal DSL to send e-mail messages that is designed to be simple to use.

```java
EmailServiceClient client = EmailServiceClient.getInstance();
client.sendMail(
	new EmailMessage()
	    .from("farhan.rayani@flydubai.com")
	    .to("biju.abraham@flydubai.com")
	    .withSubject("SmartRez Mail")
	    .withBody("Demo message")
	    .asHtml()
	    .send()
	    );
```
### Usage

There are two ways to Use the Mail API as stated below.
 1. Send Mail using REST through Web - Service Endpoint & Definition (Sample) mentioned below.
 2. Send Mail using Rest-Client (jar).

### Features
-   Simple & human Readable API.
-   Send e-mail to, cc or bcc with multiple addresses.
-   Send e-mail with **attachments**.
-   Send e-mail with **pre-defined templates** available for use (e.g. Alert/Info/General/Log)

### Maven Dependency
```xml
<dependency>
  <groupId>com.flydubai</groupId>
  <artifactId>smartrezmail-client</artifactId>
  <version>1.0</version>
</dependency>
```

### Service Endpoint

This is subject to Change as per usage of flydubai IT - Team SmartRez.

##### Development
*   http://{IP}:{PORT}/smartrezmail-server/mail

##### Production
*   http://{IP}:{PORT}/smartrezmail/mail

##### Http Protocol - **POST**
**Json** - Email with Body
```java
{  
    "fromAddress":"farhan.rayani@flydubai.com",
    "toAddresses":[  
        "farhan.rayani@flydubai.com"
    ],
    "ccAddresses":[],
    "bccAddresses":[],
    "attachments":[],
    "subject":"Smartrez Mail",
    "body":"<p>Dear Team,<\/p><p>This is an auto generated alert for Application {app}.<\/p><p>Click <ahref='path_to_file'>here<\/a> to view the logs<\/p><p>Regards,<\/p><p>flydubai IT (SmartRez)<\/p>",
    "asHtml":true
}
```

**Json** - Email with Body from Template
```java
{
	"fromAddress": "farhan.rayani@flydubai.com",
	"toAddresses": ["farhan.rayani@flydubai.com"],
	"ccAddresses": [],
	"bccAddresses": [],
	"attachments": [],
	"template": "ALERT_TEMPLATE",
	"subject": "Smartrez Mail - Test",
	"body": "Hello!",
	"asHtml": true,
	"templateArgsMap": {
		"var2": "C:\\Users\\amit.lulla\\Desktop\\pnl.log",
		"var1": "Test-Application"
	}
}
```
_Note_: if body has some value & template is also provided, Email would be sent using the String passed in Body{} & not the template.

### Template File Example
```html
<html>
    <body>
        Dear Team,
        <p>
            This is a System generated <font color="red"><b>ALERT</b></font> for application <font color="red">${var1}</font>
        </p>
        <p>
			Kindly click <a href="${var2}">here</a> for logs
		</p>
		
		Regards,<br>
		flydubai IT
    </body>
</html>
```
In code above, ${var1} would be rendered from Arguments map.

**Arguments Map**

| Key        	| Value						|
| ------------- | -------------					|
| var1		| "Test-Application" 				|
| var2		| "C:\\Users\\amit.lulla\\Desktop\\pnl.log"	|

**Json Schema**
```java
{
  "id": "http://localhost:8080/smartrezmail-server/mail",
  "type": "object",
  "properties": {
    "fromAddress": {
      "id": "http://localhost:8080/smartrezmail-server/mail/fromAddress",
      "type": "string"
    },
    "toAddresses": {
      "id": "http://localhost:8080/smartrezmail-server/mail/toAddresses",
      "type": "array",
      "items": {
        "id": "http://localhost:8080/smartrezmail-server/mail/toAddresses/0",
        "type": "string"
      }
    },
    "ccAddresses": {
      "id": "http://localhost:8080/smartrezmail-server/mail/ccAddresses",
      "type": "array",
      "items": []
    },
    "bccAddresses": {
      "id": "http://localhost:8080/smartrezmail-server/mail/bccAddresses",
      "type": "array",
      "items": []
    },
    "attachments": {
      "id": "http://localhost:8080/smartrezmail-server/mail/attachments",
      "type": "array",
      "items": []
    },
    "template": {
      "id": "http://localhost:8080/smartrezmail-server/mail/template",
      "type": "string"
    },
    "subject": {
      "id": "http://localhost:8080/smartrezmail-server/mail/subject",
      "type": "string"
    },
    "body": {
      "id": "http://localhost:8080/smartrezmail-server/mail/body",
      "type": "string"
    },
    "asHtml": {
      "id": "http://localhost:8080/smartrezmail-server/mail/asHtml",
      "type": "boolean"
    },
    "templateArgsMap": {
      "id": "http://localhost:8080/smartrezmail-server/mail/templateArgsMap",
      "type": "object",
      "properties": {
        "var2": {
          "id": "http://localhost:8080/smartrezmail-server/mail/templateArgsMap/var2",
          "type": "string"
        },
        "var1": {
          "id": "http://localhost:8080/smartrezmail-server/mail/templateArgsMap/var1",
          "type": "string"
        }
      }
    }
  },
  "required": [
    "fromAddress",
    "toAddresses",
    "ccAddresses",
    "bccAddresses",
    "attachments",
    "template",
    "subject",
    "body",
    "asHtml",
    "templateArgsMap"
  ]
}
```
### EMAIL SESSION REFRESH

If any email configurations are changing in configurator , email session map need to refresh using the url
http://<IP>:<port>/smartrezmail/mail/refresh
@POST

### Author
**_Amit Lulla_** [farhan.rayani@flydubai.com]
