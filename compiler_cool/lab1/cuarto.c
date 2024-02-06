class Main inherits IO{
	numero : Int <- new Int;
	main(): Object{
		{
		numero <- 0 - 50;
		while (numero < 150) loop{
			out_int(numero);
			out_string("--");
			out_int(function(numero));
			out_string("\n");
			numero <- numero + 10;
		}
		pool;
	}
};
	function(numero : Int): Int{
		(numero * 18) / 10 + 32
	};

};