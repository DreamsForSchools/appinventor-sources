for ARGUMENT in "$@"
do
   KEY=$(echo $ARGUMENT | cut -f1 -d=)

   KEY_LENGTH=${#KEY}
   VALUE="${ARGUMENT:$KEY_LENGTH+1}"

   export "$KEY"="$VALUE"
done

if test -n "$BS_ADDR"; then
  sed -i "s/localhost:9990/$BS_ADDR/g" ./appinventor/appengine/build/war/WEB-INF/appengine-web.xml
  echo "Build server address changed"
else
  echo "Build server address value not provided (e.g. bash script.sh BS_ADDR=\"1.2.3.4\")"
  exit 0
fi
