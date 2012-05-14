sonar-erlang-plugin
===================

An Erlang plugin for sonar to make it possibly to analyse Erlang projects as well

It is based on other plugins like scala, javascript, python.

The current metrics in nutshell:
* number of classes = files
* number of packages = folders
* number of functions
* number of comments
* comments density
* duplications --> need to be optimized now it uses AnyTokenizer
* basic erlang profile with dialyzer rules
* colorization
* code coverage
* based on dialyzer output rule violations are marked

BUGS:
* on the components view there are "[root]" and "package" components the "package contains the information, the root is empty, but it seems it is related to the violations --> temporary fix done, it needs to be reviewed

PROBLEMS:
* now the RuleHandler use a SAXParser because I cannot make that SMHierarchicCursor to work.. after getting the name and I tried to get the descendant text and it always thrown an exception that a child cursor is open...

MISSING FEATURES to reach milestone 2:
* publish it to the sonar dev community
* refactor/optimize/clear
* find other areas of improving
	* implement export_all handling
	* implement internal method counter
	* anonym method counter
	* used libraries?
	* inner dependencies?? -> does it make sense?
