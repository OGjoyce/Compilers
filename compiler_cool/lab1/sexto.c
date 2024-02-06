class Main inherits IO{
	s1 : String <- new String;
	s2 : String <- new String;
	s1L : Int <- new Int;
	s2L : Int <- new Int;
	cs : String <- new String;
	cont : Int <- new Int;
	aux : Int <- new Int;
	main(): Object{
		{
		aux <- 0;
		out_string("type anything... \n");
		s1 <- in_string();
		out_string("type anything again... \n");
		s2 <- in_string();
		s1L <- s1.length();
		s2L <- s2.length();	
		while (cont <= s1L-1) loop{
		if (s1.substr(0, 1) = s2.substr(0, cont)) then "se encontro un caracter valido" else "no se encontro"
			fi;
		cont <- aux + 1;
	}
	pool;
	
		}
};
};