cat *.xml | xml2json | jq '.testsuite[].testcase | if type == "object" then . else .[]? end' | jq -s . > tests.json
