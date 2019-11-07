## WebSiteParser

## Description
Web site parser extracts needed data from Product pages and convert them 
into a valid JSON file. Program could avoid bot detection mechanism by leaving cookies, userAgent data.
Parser doesn't use any Frameworks, only such libraries: JSOUP(for parsing), 
Jackson(for JSON conversion) with Maven management.

## Notes
- If you got exception "Can't connect to main page"(usually that can happen
 when you are connecting to site for first time), execute program few times 
 again and you'll get a **stable connection** for all future executions.
 I think this caused by connection to site with UK ip-address 
 (for example with Singapore IP it's blocked at all);
- You can [check](https://jsonformatter.curiousconcept.com/) validation of JSON file too;
