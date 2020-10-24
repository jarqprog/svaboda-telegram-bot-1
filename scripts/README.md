# HOW TO

## Prerequisites
Before using scripts one must specify SERVICE_NAME variable, ex: `export SERVICE_NAME=bot`
Env vars specific for given service must be provided (please find README file for given service - 'Configuration variables' section).

## Run locally
- with gradle (gradle required)
```
./run_locally.sh
```
- with Docker ([Docker](https://docs.docker.com/get-docker/) required)
```
./run_docker_locally.sh
```

## Deploy to Heroku cloud with container registry
Important: for this kind of build/deployment there is no need to set up env vars. Requires access to our [Heroku](https://dashboard.heroku.com/apps) and [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) installed):

### Prerequisites
Heroku application name and process type [web or worker](https://devcenter.heroku.com/articles/background-jobs-queueing) 
must be set (service name - see above - also):
```
export HEROKU_APP_NAME=bot-monitoring1
export PROCESS_TYPE=worker
```

`worker` is valid option when service is not exposing http endpoints

Before any action one need to login: `heroku login`.

To build and push image: `./heroku_build.sh`
To deploy: `./heroku_deploy.sh`

One can find more on the Heroku [website](https://devcenter.heroku.com/articles/container-registry-and-runtime)

### Useful scripts
To scale:
```
numberOfInstances=1
heroku ps:scale "$PROCESS_TYPE"=${numberOfInstances} --app "$HEROKU_APP_NAME"
```

Check logs:
`heroku logs --tail`