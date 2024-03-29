#!/bin/bash

set -e

GCLOUD_PROJECT=appjam-265500
# expecting the install directly in the home directory
GCLOUD=${HOME}/google-cloud-sdk/bin/gcloud

echo ${GCLOUD_SERVICE_KEY} | base64 --decode > ${HOME}/gcloud-service-key.json

if ${GCLOUD} version 2>&1 >> /dev/null; then
    echo "Cloud SDK is already installed"
else
    SDK_URL=https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-190.0.1-linux-x86_64.tar.gz
    INSTALL_DIR=${HOME}

    cd ${INSTALL_DIR}
    wget ${SDK_URL} -O cloud-sdk.tar.gz
    tar -xzvf cloud-sdk.tar.gz

    ${GCLOUD} components install app-engine-java
fi

${GCLOUD} auth activate-service-account --key-file ${HOME}/gcloud-service-key.json
${GCLOUD} config set project ${GCLOUD_PROJECT}

# installs uglifyjs
sudo npm install uglify-js -g

# install java
sudo apt-get install openjdk-8-jdk

# installs ant	
sudo apt-get update
sudo apt-get install ant
