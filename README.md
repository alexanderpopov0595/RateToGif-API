# RateToGif API

### Description

Task for junior position at Alpha Bank.

This service takes a currency code as parameter and finds out, has the rubles rate increased comparing to the previous date or not. If it did - service returns success GIF image, if not - failure GIF image.

### Usage

Use simple GET request to retrieve GIF image:

```
GET /rate/{currency}
```  

where currency - is a 3-letters code of requested currency. Example:

```
GET /rate/EUR
``` 

will request GIF for rate between RUB and EUR.

### How to run

To run the application via Docker just run the run.sh bash-script:

```
./run.sh
```