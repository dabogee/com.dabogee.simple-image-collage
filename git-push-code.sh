#!/bin/bash

# How to use:
# -m "Commit message" - define a message
# -p - means do a push or not.
#
# sh git-push-code.sh -p -m "comment text"
#
# Or:
# sh git-push-code.sh
# Push a code with default message "Hold changes at Dec 01, 2019 22:48:49"

# Example: https://stackoverflow.com/questions/192249/how-do-i-parse-command-line-arguments-in-bash
while [[ $# -gt 0 ]]
do
	key="$1"
	case $key in
		-m|--message)
		COMMENT="$2"
		shift # past argument
		shift # past value
		;;
		-p|--push)
		DO_PUSH="$1"
		shift # past argument
		;;
	esac
done

if [ -z "$COMMENT" ]; then
	COMMENT="Hold changes at $(date '+%b %d, %Y %H:%M:%S')"
fi

git add . && git commit -m "$COMMENT"
if [ ! -z "$DO_PUSH" ]; then
	git push	
fi