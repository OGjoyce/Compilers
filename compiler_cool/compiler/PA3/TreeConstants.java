/** For convenience, this clas defines a large number of symbols.  These
 * symbols include the primitive type and method names, as well as fixed
 * names used by the runtime system.  */
class TreeConstants {

	public static final	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");

    public static final AbstractSymbol arg 
	= AbstractTable.idtable.addString("arg");
    
    public static final AbstractSymbol arg2 
	= AbstractTable.idtable.addString("arg2");
    
    public static final AbstractSymbol Bool 
	= AbstractTable.idtable.addString("Bool");
    
    public static final AbstractSymbol concat 
	= AbstractTable.idtable.addString("concat");

    public static final AbstractSymbol cool_abort 
	= AbstractTable.idtable.addString("abort");

    public static final AbstractSymbol copy 
	= AbstractTable.idtable.addString("copy");

    public static final AbstractSymbol Int 
	= AbstractTable.idtable.addString("Int");

    public static final AbstractSymbol in_int 
	= AbstractTable.idtable.addString("in_int");
    
    public static final AbstractSymbol in_string 
	= AbstractTable.idtable.addString("in_string");

    public static final AbstractSymbol IO 
	= AbstractTable.idtable.addString("IO");

    public static final AbstractSymbol length 
	= AbstractTable.idtable.addString("length");

    public static final AbstractSymbol Main 
	= AbstractTable.idtable.addString("Main");

    public static final AbstractSymbol main_meth 
	= AbstractTable.idtable.addString("main");
    
    public static final AbstractSymbol No_class 
	= AbstractTable.idtable.addString("_no_class");

    public static final AbstractSymbol No_type 
	= AbstractTable.idtable.addString("_no_type");

    public static final AbstractSymbol Object_ = 
	AbstractTable.idtable.addString("Object");
    
    public static final AbstractSymbol out_int 
	= AbstractTable.idtable.addString("out_int");

    public static final AbstractSymbol out_string 
	= AbstractTable.idtable.addString("out_string");

    public static final AbstractSymbol prim_slot 
	= AbstractTable.idtable.addString("_prim_slot");

    public static final AbstractSymbol self 
	= AbstractTable.idtable.addString("self");

    public static final AbstractSymbol SELF_TYPE 
	= AbstractTable.idtable.addString("SELF_TYPE");

    public static final AbstractSymbol Str 
	= AbstractTable.idtable.addString("String");

    public static final AbstractSymbol str_field 
	= AbstractTable.idtable.addString("_str_field");

    public static final AbstractSymbol substr = 
	AbstractTable.idtable.addString("substr");

    public static final AbstractSymbol type_name = 
	AbstractTable.idtable.addString("type_name");

    public static final AbstractSymbol val = 
	AbstractTable.idtable.addString("_val");

	public static final class_c Object_class = 
	    new class_c(0, 
		       TreeConstants.Object_, 
		       TreeConstants.No_class,
		       new Features(0)
			   .appendElement(new method(0, 
					      TreeConstants.cool_abort, 
					      new Formals(0), 
					      TreeConstants.Object_, 
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.type_name,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.copy,
					      new Formals(0),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0))),
		       filename);
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	public static final class_c IO_class = 
	    new class_c(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_string,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_int,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0))),
		       filename);

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	public static final class_c Int_class = 
	    new class_c(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// Bool also has only the "val" slot.
	public static final class_c Bool_class = 
	    new class_c(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	public static final class_c Str_class =
	    new class_c(0,
		       TreeConstants.Str,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.Int,
					    new no_expr(0)))
			   .appendElement(new attr(0,
					    TreeConstants.str_field,
					    TreeConstants.prim_slot,
					    new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.length,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.concat,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formalc(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);
}
