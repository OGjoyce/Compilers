class Main inherits Object {
	io : IO <- new IO;
	io1 : IO <- new IO;
	io3 : IO <- new IO;
	input : String <- new String;
	edad : Int <- new Int;
	main(): Object {{
	io.out_string("Ingrese su nombre :\n");
	input <- io.in_string(); 
	io.out_string("Ingrese su edad :\n");
	edad <- io.in_int();
	io.out_string(input); 
	io.out_string("\n"); 
	io.out_int(edad + 4);
	io.out_string("\n"); 
}};
};