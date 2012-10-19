-module(erlcount_counter).
-behaviour(gen_server).
-export([start_link/4]).
-export([init/1, handle_call/3, 'handle/cast'/2, handle_info/2]).
-export(['terminate'/2, 
	code_change/3]).
-ifdef('TEST').
-export([
	test_1/2,
	test_2/0
	]).
-endif.

-ifndef('WER'). -export([test_3/0]). -endif.
-export([]).

-record(state, {dispatcher, ref, file, re}).

start_link(DispatcherPid, Ref, FileName, Regex) ->
    gen_server:start_link(?MODULE, [DispatcherPid, Ref, FileName, Regex], []).

%%============================================
%Comment 0
%%--------------------------------------------
init([DispatcherPid, Ref, FileName, Regex]) ->
    self() ! start,
    {ok, #state{dispatcher=DispatcherPid,
                ref = Ref,
                file = FileName,
                re = Regex}}.

%%-------------------------------------
handle_call(_Msg, _From, State) ->
    {noreply, State}.
%%======================================
'handle/cast'(_Msg, State) ->
    {noreply, State}.

%%======================================
%Comment
handle_info(start, S = #state{re=Re, ref=Ref}) ->
    {ok, Bin} = file:read_file(S#state.file),
    Count = erlcount_lib:regex_count(Re, Bin),
    erlcount_dispatch:complete(S#state.dispatcher, Re, Ref, Count),
    {stop, normal, S}.

%%Comment2
%---------------------------------------
terminate(_Reason, _State) ->
    ok.
	
%%Comment3
code_change(_OldVsn, State, _Extra) ->
    {ok, State}.

test_1(_A, _B) ->
    {test, 1}.

test_2() ->
    {test, 1}.

test_3() ->
    {test, 1}.
