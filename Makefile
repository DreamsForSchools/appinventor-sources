# Define required macros here
SHELL = /bin/sh

init:
	# .circleci/install-deps.sh 
	cp sample-.gitignore .gitignore
	git submodule update --init
	cd appinventor && ant
	# cd appinventor && ant MakeAuthKey && ant
	# minify large .js file
	find appinventor/appengine/build/war/ode -maxdepth 1 -iname "*.cache.js" -exec uglifyjs {} -o {} -m \;

build:
	cd appinventor && ant

dev-mode:
	cd appinventor && ant devmode

run-main:
	/Users/dos/google-cloud-sdk/bin/java_dev_appserver.sh --port=8888 --address=0.0.0.0 appinventor/appengine/build/war/

build-server:
	cd appinventor && cd buildserver && ant BuildDeploymentTar

run-build-server:
	cd appinventor/buildserver && ant RunLocalBuildServer

build-apk:
	cd appinventor && ant PlayApp
	# APK found in ./appinventor/build/buildserver/MIT AI2 Companion.apk
	# Android project files found in
	# ./appinventor/aiplayapp/youngandroidproject/project.properties

sign-apk:
	# remove signing key
	zip -d appinventor/build/buildserver/"DFS AppMaker.apk" META-INF/\*
	jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore my-release-key.keystore.old appinventor/build/buildserver/"DFS AppMaker.apk" alias_name
	~/Library/Android/sdk/build-tools/29.0.2/zipalign -f -v 4 appinventor/build/buildserver/"DFS AppMaker.apk" appinventor/build/buildserver/DFSAppMaker-signed.apk

test:
	ant tests

deploy:
	gcloud -q app deploy --project=appjam-265500 appinventor/appengine/build/war/WEB-INF/appengine-web.xml