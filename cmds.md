
# Switch Java version
/usr/libexec/java_home -V
export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

# Build instance
ant

# Run instance locally
~/google-cloud-sdk/bin/java_dev_appserver.sh --port=8888 --address=0.0.0.0 appengine/build/war/

# Run instance and log
nohup ~/google-cloud-sdk/bin/java_dev_appserver.sh --port=8888 --address=0.0.0.0 appengine/build/war/ > serverlog.out &

# Build && Run instance
ant && ~/google-cloud-sdk/bin/java_dev_appserver.sh --port=8888 --address=0.0.0.0 appengine/build/war/

# Run build server
cd appinventor-sources/appinventor/buildserver
ant RunLocalBuildServer

# Build to deploy app inventor

This following command works consistently
`gcloud app deploy --project=appjam-265500 --version=1 appengine/build/war/WEB-INF/appengine-web.xml`

python ~/google-cloud-sdk/platform/google_appengine/appcfg.py -A appjam-265500 --oauth2 update appengine/build/war

~/google-cloud-sdk/bin/appcfg.sh -A appjam-265500 --oauth2 update appengine/build/war

gcloud app deploy appengine/build/war/WEB-INF/appengine-web.xml

python ~/google-cloud-sdk/platform/google_appengine/appcfg.py -A appjam-265500 --oauth2 update appengine/build/war


gcloud components update --version 159.0.0
gcloud components update --version 276.0.0

gcloud app deploy appengine/war/WEB-INF/appengine-web.xml
python ~/google-cloud-sdk/platform/google_appengine/appcfg.py -A appjam-265500 --oauth2 update appengine/war


us.gcr.io/appjam-265500/rendezvous .
docker build -t us.gcr.io/appjam-265500/rendezvous .
docker push us.gcr.io/appjam-265500/rendezvous

docker build -t us.gcr.io/appjam-265500/turn-server .
docker push us.gcr.io/appjam-265500/turn-server