!/bin/bash

#export REFERL_BASE=/home/tkende/dev/tools/refactorerl-0.9.12.05/
export PATH=$REFERL_BASE/bin:$PATH
METRICS=( "mods.funs.mcCabe:RE-mcCabe.txt"
	"mods.funs.max_depth_of_calling:RE-max_depth_of_calling.txt"
	"mods.cohesion:RE-cohesion.txt"
	"mods.funs.max_depth_of_cases:RE-max_depth_of_cases.txt"
	"mods.funs.max_depth_of_structs:RE-max_depth_of_structs.txt"
	"mods.funs.number_of_funclauses:RE-number_of_funclauses.txt"
	"mods.funs.branches_of_recursion:RE-branches_of_recursion.txt"
	"mods.funs.no_space_after_comma:RE-no_space_after_comma.txt"
	"mods.funs.is_tail_recursive:RE-is_tail_recursive.txt"
	"mods.imported_modules:CRE-imported_modules.txt"
	"mods.function_calls_in:CRE-function_calls_in-txt"
	"mods.function_calls_out:CRE-function_calls_out"
)

main() {
   if [ $# -ne 2 ]; then
      echo "Usage: runAnalysis.sh appbase application"
      exit 65
   fi
   appbase=$1
   application=$2
   dir=${appbase}
   get_home_dir
   setup_referl
   get_metrics
   clear_referl

}

get_home_dir(){
   dir=${dir#[\\/]};
   dir=${dir%%/*}
}

setup_referl(){
   echo ---------------START UP SERVER
   RefactorErl start nif
   sleep 1
   echo ---------------SET parent folder ${appbase}
   RefactorErl ri delenv appbase >> /dev/null
   #RefactorErl ri setenv appbase
   sleep 1 
   RefactorErl ri addenv appbase "${appbase}"
   sleep 1
   RefactorErl ri addenv deps "${appbase}/deps"
   #RefactorErl ri setenv appbase "${appbase}"
   sleep 1 
   RefactorErl ri reset
   sleep 1 
   echo ---------------Add project ${dir} ${application}

   #RefactorErl ri add "${appbase}/${application}" 
   RefactorErl ri add ${dir} ${application}
   sleep 1

}

get_metrics(){
   if [ ! -d "${appbase}/${application}/.eunit" ]; then
      mkdir ${appbase}/${application}/.eunit
   fi
   for metric in ${METRICS[@]} ; do
      KEY=${metric%%:*}
      VALUE=${metric##*:}

      echo Add ${KEY} file to: ${appbase}/${application}/.eunit/${VALUE}
      RefactorErl ri q "${KEY}" "[linenum,{out,'${appbase}/${application}/.eunit/${VALUE}'}]"
      sleep 1
   done
}

clear_referl(){
   RefactorErl ri delenv appbase >> /dev/null
   wait
   RefactorErl stop #>> /dev/null
   wait
}
main $@
