# Define required macros here
SHELL = /bin/sh

init:
	.circleci/install-deps.sh 
	cp sample-.gitignore .gitignore
	git submodule update --init
	cd appinventor && ant MakeAuthKey && ant
	find appinventor/appengine/build/war/ode -maxdepth 1 -iname "*.cache.js" -exec uglifyjs -o {} -- {} \;

dev-mode:
	cd appinventor && ant devmode

run-main:
	/Users/dos/google-cloud-sdk/bin/java_dev_appserver.sh --port=8888 --address=0.0.0.0 appinventor/appengine/build/war/

run-build-server:
	cd appinventor/buildserver && ant RunLocalBuildServer

test:
	ant tests

deploy:
	gcloud -q app deploy appinventor/appengine/build/war/WEB-INF/appengine-web.xml