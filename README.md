# Athento Report Engine

This addon for Nuxeo DM define report managers and contributions. 

- JasperReport manager is a contribution to generate reports with this engine.

## Jasper Report Engine

###How to use###

You can access to your report via REST API:

```
http://${HOST}/nuxeo/restAPI/jr/{alias}/report?output={output}&param1=...&param2=...
```

where,

* jr: is the JasperReport engine.
* {alias}: is your report alias defined in Report Service contribution.
* {output}: pdf, xls, html are the default outputs.
* param#: are params for your report




