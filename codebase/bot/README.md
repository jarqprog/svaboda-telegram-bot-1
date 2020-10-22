# telegram-bot

## Configuration variables
|Key|Description|Example|Mandatory|
|---|---|---|---|
|PORT|TCP port on which application will listen incoming http requests|8080|Yes
|BOT_NAME|telegram bot name|any-bot-name|Yes
|BOT_TOKEN|token to access telegram bot api|some-secret|Yes

## TBD
    Add authorization for deletion stats
    Consider using queue to push stats instead exposing rest endpoint
    Investigate failing test: CachedStatisticsTest > should return proper statistics when there were multiple calls against commands() 

