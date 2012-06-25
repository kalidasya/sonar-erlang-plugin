-module(refactorerl_issues).
-behaviour(supervisor).

mcCabeIssue() ->
X=1.0,
Y=2.0,
Z=1.5,
    case {X, Y, Z} of
        {0, 0, 0} -> empty_vector;    % empty_vector is an atom
        {A, A, A} -> all_the_same;    % so is all_the_same
        {X1, Y1, Z1} -> {X1 + 1, Y1 + 1, Z1 +1};
	{1, 0, 0} -> empty_vector;
	{0, 1, 0} -> empty_vector;
	{0, 0, 1} -> empty_vector;
	{1, 1, 0} -> empty_vector;
	{1, 0, 1} -> empty_vector;
	{0, 1, 1} -> empty_vector;
	{1, 1, 1} -> empty_vector;
	{1, 2, 4} -> empty_vector
    end.
