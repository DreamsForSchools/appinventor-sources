# Define required macros here
SHELL = /bin/sh

init:
	.circleci/gcloud-sdk.sh 
	# cp sample-.gitignore .gitignore
	# git submodule update --init
	# cd appinventor
	# ant MakeAuthKey
	# ant

dev-mode:
	cd appinventor
	ant devmode

run-main:
	/Users/dos/google-cloud-sdk/bin/java_dev_appserver.sh --port=8888 --address=0.0.0.0 appinventor/appengine/build/war/

run-build-server:
	cd appinventor/buildserver
	ant RunLocalBuildServer

test:
	ant tests

build-gae:
	sudo apt-get install libc6:i386 zlib1g:i386 libstdc++6:i386

test:
	echo "Hello"