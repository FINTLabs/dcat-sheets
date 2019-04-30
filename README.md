# dcat-sheets

Web servlet for supplying [DCAT-AP-NO](https://doc.difi.no/dcat-ap-no/) resources to https://fellesdatakatalog.brreg.no/
based on information from a Google Sheet.

## Getting started

1. Use https://sheets.new to create a spreadsheet containing your datasets.
It currently needs the following columns (in order).  
See [source code](https://github.com/FINTLabs/dkat-sheets/blob/6c60c40e7e0e6a306dc9ef47303f2ff554356e49/src/main/java/no/fint/fdk/dkat/dkatsheets/ModelService.java#L89-L96)
for definition of the various attributes.
    - name
    - title
    - description
    - type
    - theme
    - accrual
    - spatial
    - access
1. Use https://developers.google.com/sheets/api/quickstart/java (step 1) 
to get credentials for accessing your spreadsheet.
1. Configure `application.yml` or environment variables with the properties
from the table below.
1. Start the application, either locally or deployed on the platform of your liking.
1. If running locally, use http://localhost:8080/catalogs/fdk to retrieve the full catalog.

## Configuration

| property                            | usage                                                                | 
|-------------------------------------|----------------------------------------------------------------------|
| `fint.fdk.dkat.organisasjonsnavn`   | Name of the organization you are publishing datasets for.            |
| `fint.fdk.dkat.organisasjonsnummer` | Business unit number (from Enhetsregisteret) for the organization.   |
| `fint.fdk.dkat.spreadsheetId`       | ID of Google Sheet to use (see the spreadsheet URI to obtain it).    |
| `fint.fdk.dkat.range`               | Cell range to obtain information from, in the form `Sheet!Cell:Cell` |
