#! /bin/bash

printf "KILL_SERVICE" | ncat 127.0.0.1 8000
