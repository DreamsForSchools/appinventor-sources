#!/bin/bash

/usr/bin/redis-server &
sleep 5
node ./index.js 