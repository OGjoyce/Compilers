class Main inherits IO{
	s : String <- new String;
	foo1 : Int <- new Int;
	foo2 : Int <- new Int;
	primer : Int <- new Int;
	segundo : Int <- new Int;
	main(): Object{
		{
		out_string("Insert arithmetic operation: \n");
		s <- in_string();
		primer <- Sumfunction(s);
		segundo <- SumfunctionDos(s);
		if (s.substr(1, 2) = "+") then primer;
		if (s.substr(1, 2) = "+") then segundo;
		out_int(primer + segundo);
	}
	};
	Sumfunction(s : string): Int{
		foo1 <- s.substr(0, 1);
		if foo1 = "0" then 0 else
		if foo1 = "1" then 1 else
		if foo1 = "2" then 2 else
        if foo1 = "3" then 3 else
        if foo1 = "4" then 4 else
        if foo1 = "5" then 5 else
        if foo1 = "6" then 6 else
        if foo1 = "7" then 7 else
        if foo1 = "8" then 8 else
        if foo1 = "9" then 9 else


	};

	SumfunctionDos(s : string): Int{
		foo2 <- s.substr(3, 4);
		if foo2 = "0" then 0 else
		if foo2 = "1" then 1 else
		if foo2 = "2" then 2 else
        if foo2 = "3" then 3 else
        if foo2 = "4" then 4 else
        if foo2 = "5" then 5 else
        if foo2 = "6" then 6 else
        if foo2 = "7" then 7 else
        if foo2 = "8" then 8 else
        if foo2 = "9" then 9 else


	};
};