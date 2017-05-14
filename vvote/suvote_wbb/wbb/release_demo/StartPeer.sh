#!/bin/bash
cd /usr/local/suvote_wbb
mongod & java -Dlogback.configurationFile=./release_demo/logback-demo.xml -jar ./release/WBBPeer.jar ./release_demo/$PEERNAME/wbbconfig$PEERNAME.json 
