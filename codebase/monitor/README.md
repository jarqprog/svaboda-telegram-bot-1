# monitor service
Service to execute diagnostic http calls against other services in the system.

## Configuration variables
|Key|Description|Example|Mandatory|
|---|---|---|---|
|PORT|TCP port on which application will listen incoming http requests|8080|Yes
|INTERVAL_SEC|time interval in seconds between diagnostic calls|60|Yes
|SERVICES_BASE_URLS|coma separated list of services to monitor|some_service.com,other_service.com|Yes
|DIAGNOSTIC_ENDPOINT|common diagnostic endpoint exposed in services|_someEndpoint|Yes
|DB_URL|database url|mongodb+srv://{user}:{password}@any.any.any.net/<dbname>|Yes

## TBD
Add tests