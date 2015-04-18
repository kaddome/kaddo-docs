README for hoozad
==========================

gradle installGrunt grunt_build bootRun
npm -g install karma karma-jasmine karma-chrome-launcher --save-dev
http://karma-runner.github.io/0.12/intro/installation.html



Heroku integration
==========================

```bash
heroku apps:create hoozad-pilot
heroku config:set BUILDPACK_URL=https://github.com/heroku/heroku-buildpack-gradle --app hoozad-pilot
git push heroku master  
```
