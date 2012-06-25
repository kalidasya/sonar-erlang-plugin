#!/bin/bash
ref=/home/tkende/dev/tools/refactorerl-0.9.12.04
if [ $# -ne 2 ]
then
  echo "Usage: runAnalysis.sh appbase application
like: ./runRefactorErlAnalysis.sh ~/dev/projects/sonar-erlang-plugin/src/test/resources/org/sonar/plugins/erlang erlcount

If it failes, run it twice... dunno why...
At the end you will have a file called refactorerl.log in your .eunit folder"
  exit 65
fi

appbase=$1
application=$2
cd $ref
echo $(pwd)
echo ---------------START UP SERVER
#bin/RefactorErl stop >> /dev/null 	
bin/RefactorErl start
wait $!
sleep 2
echo ---------------SET parent folder
bin/RefactorErl ri delenv appbase 
wait $!
sleep 2
bin/RefactorErl ri addenv appbase "${appbase}"
wait $!
sleep 2
bin/RefactorErl ri reset 
wait $!
sleep 2
#set +v 
echo ---------------Add project
bin/RefactorErl ri add home "${application}" 
wait $!
sleep 2
echo Add mcCabe file to: ${appbase}/${application}/mcCabe.txt
bin/RefactorErl ri q "mods.funs.mcCabe" "[linenum,{out,'${appbase}/${application}/mcCabe.txt'}]"
sleep 2
echo Add fun_call_in file to: ${appbase}/${application}/func_call_in.txt
bin/RefactorErl ri q "mods.function_calls_in" "[linenum,{out,'${appbase}/${application}/func_call_in.txt'}]"
sleep 2

bin/RefactorErl ri delenv appbase

bin/RefactorErl stop #>> /dev/null

mv ${appbase}/${application}/mcCabe.txt ${appbase}/${application}/.eunit/refactorerl.log
