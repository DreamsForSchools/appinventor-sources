for ARGUMENT in "$@"
do
   KEY=$(echo $ARGUMENT | cut -f1 -d=)

   KEY_LENGTH=${#KEY}
   VALUE="${ARGUMENT:$KEY_LENGTH+1}"

   export "$KEY"="$VALUE"
done

if test -n "$CS_ADDR"; then
  sed -i "s/localhost:3000/$CS_ADDR/g" ./appinventor/appengine/build/war/WEB-INF/appengine-web.xml
  echo "Collab server address changed"
else
  echo "Collab server address value not provided (e.g. bash script.sh CS_ADDR=\"1.2.3.4\")"
  exit 0
fi
