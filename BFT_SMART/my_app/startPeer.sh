#!/bin/bash
cd /usr/local/my_app
mongod & java -cp ../bft_smart_library/bin/*:../bft_smart_library/lib/*:out/* pagel.thesis.Server $PEERID