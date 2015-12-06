#! /bin/bash

printf "DISCONNECT:0\nPORT: 0\nCLIENT_NAME: $1" | ncat 127.0.0.1 8000
