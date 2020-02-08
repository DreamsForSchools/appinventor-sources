# Define required macros here
SHELL = /bin/sh

init:
	# .circleci/install-deps.sh 
	cp sample-.gitignore .gitignore
	git submodule update --init
	cd appinventor && ant MakeAuthKey && ant
	# minify large .js file
	find appinventor/appengine/build/war/ode -maxdepth 1 -iname "*.cache.js" -exec uglifyjs {} -o {} -m \;

dev-mode:
	cd appinventor && ant devmode

run-main:
	/Users/dos/google-cloud-sdk/bin/java_dev_appserver.sh --port=8888 --address=0.0.0.0 appinventor/appengine/build/war/

run-build-server:
	cd appinventor/buildserver && ant RunLocalBuildServer

build-apk:
	cd appinventor && ant PlayApp
	# APK found in ./appinventor/build/buildserver/MIT AI2 Companion.apk
	# Android project files found in
	# ./appinventor/aiplayapp/youngandroidproject/project.properties

test:
	ant tests

deploy:
	gcloud -q app deploy appinventor/appengine/build/war/WEB-INF/appengine-web.xml