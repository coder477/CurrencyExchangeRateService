Exchange Rate Service:
A scalable service that will display currency exchange rates for a date and persist the data to DB. The service receives that will give exchange rates take three parameters: two currencies and a date using the URL path.
GET /api/exchange-rate/{date}/{baseCurrency}/{targetCurrency}
Ex: http://localhost:8080/api/exchange-rate/2019-08-30/USD/EUR
Output :
{
"exchangeRate":0.8996041742,
 "averageRate":0.90235358342, 
"exchangeRateTrend":"ascending"
 }
The exchange rate between base and target currencies will be given as below.The output consists of exachange rate along with the average of the five days before the requested date (excluding Saturday and Sunday) and the exchange rate trend.
Historic Data services:
With the persisted data, the following needs to be retreived: historic data datewise and historic data monthwise.
1. GET /api/exchange-rate//history/monthly/{yyyy}/{MM}
Ex: http://localhost:8080//api/exchange-rate/history/monthly/2019/08
Output:
[ {"baseCurrency":"USD","targetCurrency":"EUR","exchangeRate":"0.8996041742","cumulativeExchangeRateAverage":"0.90235358342","exchangeTrend":"ASCENDING","date":"2019-08-30"},{"baseCurrency":"USD","targetCurrency":"EUR","exchangeRate":"0.9037505648","cumulativeExchangeRateAverage":"0.90187861482","exchangeTrend":"UNDEFINED","date":"2019-08-29"}]
2. GET /api/exchange-rate//history/daily/{year}/{month}/{day}
Ex: http://localhost:8080//api/exchange-rate/history/daily/2019/08/30
Output:
[ {"baseCurrency":"USD","targetCurrency":"EUR","exchangeRate":"0.8996041742","cumulativeExchangeRateAverage":"0.90235358342","exchangeTrend":"ASCENDING","date":"2019-08-30"}]

PreRequisites:
Java 8
Maven with Springboot (or IDE that supports these)
Constraints:
Should use https://exchangeratesapi.io/ as source of data should allow dates between 2000-01-01 and yesterday only. 
Should return errors incase of in correct input parameters. 
All successful queries should be persisted in the DB for historic data retreival.
Running the application locally:
Run Integration test with mvn -Dtest=ExchangeRateIntegrationTest test 
Run application with mvn spring-boot:run
Approach and Details: 
H2(with JPA) database is used for DB persistence as it will be ideal for test applications. 
what happens on querying exchange rate with a given date?
1. Date will be validated to find saturday/sunday
2. Successful requests are stored in DB with Date as primary key.
3. Incorrect input paramaters are handled as Bad Requests. Exceptions from external api are also handled.
what happens on querying monthly/daily historic data?
1. Data is queried on the date in DB with reference to the month/date given in the query.
2. The resultant rows are given as a list in output.
Assumptions: 
If exchange rate has been queried for a saturday/sunday, the result of previous valid date will be given and stored in DB. 
Results in case of any error from API will be considered as external API errors and the same error message is used. 
For storing historic data, the query date is considered as the date basis on which the monthly/daily exchange rates are given. 
Exchange Rate historic data for a non existing time period is returned as empty list.
Improvements:
The built application structure is more suitable for test applications.
The application needs additional test cases and can be added in a CI/CG pipeline for handling devops while staging the application
The H2 database should be replacaed with Databses like postgresql/mysql in order to make it production ready.

