class Main inherits IO{
		temperature: Int <- new Int;
		perro: IO <- new IO;
		main(): Object{{
			out_string("input temperature (celsius) \n"); 
			temperature <- in_int(); out_string("from celsius to farenheit : \n"); 
			out_int(function(temperature));
		}

		};
		function(temperature : Int): Int{
			(temperature * 18) / 10 + 32
		};

};