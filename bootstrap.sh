#!/usr/bin/env bash

dpkg --add-architecture i386

# Install dependencies
apt-get update
apt-get upgrade -y
apt-get install -y zip unzip ant phantomjs

# Install App Engine
mkdir -p /opt/appengine
cd /opt/appengine
wget --no-verbose -O /tmp/appengine.zip https://storage.googleapis.com/appengine-sdks/featured/appengine-java-sdk-1.9.68.zip
unzip -o /tmp/appengine.zip

# Configure shell
echo "export PATH=$PATH:/opt/appengine/appengine-java-sdk-1.9.68/bin" >> /home/vagrant/.bashrc
echo "cd /vagrant/appinventor" >> /home/vagrant/.bashrc

# Configure java
update-java-alternatives -s java-1.8.0-openjdk-amd64

# Make the auth key in advance
cd /vagrant/appinventor
sudo -u vagrant ant MakeAuthKey

# Helper script for starting App Inventor dev server
cat <<EOF > /usr/local/bin/start_appinventor
ant RunLocalBuildServer &> buildserver.log &
BUILDSERVER=$!
dev_appserver.sh -p 8888 -a 0.0.0.0 appengine/build/war
kill -9 -- -$BUILDSERVER
EOF
chmod +x /usr/local/bin/start_appinventor
