#! /bin/bash

printf "CHAT:$1\nJOIN_ID:$2\nCLIENT_NAME:$3\nMESSAGE:$4" | ncat 127.0.0.1 8000
