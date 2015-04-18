## README for hoozad

### Requirements
* MongoDB
* Java 8


### Commands

gradle installGrunt grunt_build bootRun
npm -g install karma karma-jasmine karma-chrome-launcher --save-dev
http://karma-runner.github.io/0.12/intro/installation.html

#### UI

#### Heroku integration

```bash
heroku apps:create hoozad-pilot
heroku buildpack:set https://github.com/heroku/heroku-buildpack-gradle.git -a hoozad-pilot
git push heroku master  
```
