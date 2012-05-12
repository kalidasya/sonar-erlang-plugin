-module(erlcount_sup).
-behaviour(supervisor).
-export([start_link/0, init/1]).

%%------------------
%% VERY IMPORTANT COMMENT
%%===================
start_link() ->
    supervisor:start_link(?MODULE, []).

%Simple comment
init([]) ->
    MaxRestart = 5, %inline comment
    MaxTime = 100,
	%%Another simple comment
    {ok, {{one_for_one, MaxRestart, MaxTime},
     [{dispatch,
       {erlcount_dispatch, start_link, []}, %%another inline comment
        transient,
        60000,
        worker,
        [erlcount_dispatch]}]}}.
