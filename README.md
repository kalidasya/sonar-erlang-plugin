sonar-erlang-plugin
===================

This is the first beta version of the sonar erlang plugin. For the official one pls check out: https://github.com/SonarCommunity/sonar-erlang
---------------------------------------------------------------------------------------------------------------------------------------------

An Erlang plugin for sonar to make it possible to analyse Erlang projects as well

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
* based on rebar.config all dependencies are published on the libraries page (recursive)
* count undocumented public apis (exported APIs which has no text comment before) included published by export_all
* number of statements (number of statements = (covered+uncovered lines))
* fix -spec issue in undocumented public API counter, but multiline -spec still not supported
* Two RefactorErl rules introduced (as a POC)
* Based on RefactorErl cyclomatic complexity implemented
* New RefacotrErl metrics as violation


BUGS:
* on the components view there are "[root]" and "package" components the "package contains the information, the root is empty, but it seems it is related to the violations --> temporary fix done, it needs to be reviewed

PROBLEMS:
* now the RuleHandler use a SAXParser because I cannot make that SMHierarchicCursor to work.. after getting the name and I tried to get the descendant text and it always thrown an exception that a child cursor is open...

MISSING FEATURES to reach milestone 3:
* publish it to the sonar dev community
* refactor/optimize/clear
* inlucde other RefactorErl metrics as Erlang specific metrics and create a widget to display it
* add todo counter (a simple regex based violation)

Areas of improvement:
	* anonym method counter
	
