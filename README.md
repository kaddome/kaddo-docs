## README for hoozad

### Requirements
* MongoDB shell version: 3.0.2
* Java 8


### Commands

gradle installGrunt grunt_build bootRun
npm -g install karma karma-jasmine karma-chrome-launcher --save-dev
http://karma-runner.github.io/0.12/intro/installation.html

#### UI

#### Heroku integration

* heroku-toolbelt/3.31

```shell
hoozad$ heroku apps:create hoozad-pilot
hoozad$ heroku buildpack:set https://github.com/heroku/heroku-buildpack-gradle.git -a hoozad-pilot
hoozad$ heroku domains:add int.hoozad.com
hoozad$ git push heroku master  
```
