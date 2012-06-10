-module(erlcount_sup).
-behaviour(supervisor).
-export_all().

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
        
%% Specification defined, but it is also commented
-spec start_link(X) -> Y.
start_link([]) ->
    supervisor:start_link(?MODULE, []).

-spec start_link(X,Z) -> Y.
start_link([], []) ->
    supervisor:start_link(?MODULE, []).    