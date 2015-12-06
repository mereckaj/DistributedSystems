#! /bin/bash

printf "LEAVE_CHATROOM:$1\nJOIN_ID:$2\nCLIENT_NAME:$3" | ncat 127.0.0.1 8000
