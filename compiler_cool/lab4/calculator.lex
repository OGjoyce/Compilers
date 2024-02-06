package Example;

import java_cup.runtime.Symbol;
%%
%cup
%%
";" { return new Symbol(sym.SEMI); }
"+" { return new Symbol(sym.PLUS); }
"-" { return new Symbol(sym.MINUS);}
"*"	{ return new Symbol(sym.TIMES);}
"/" { return new Symbol(sym.DIVI);}
"(" { return new Symbol(sym.LPAREN);}
")"	{ return new Symbol(sym.RPAREN);}
"^" { return new Symbol(sym.POW);}
"%" { return new Symbol(sym.REM);}
[sS][iI][nN] {return new Symbol(sym.SIN);}
[cC][oO][sS] {return new Symbol(sym.COS);}
[tT][aA][nN] {return new Symbol(sym.TAN);}
[0-9]+ {return new Symbol(sym.INTEGER, new Integer(yytext()));}
"-"[0-9]+ {return new Symbol(sym.INTEGER, new Integer(yytext()));}
[0-9]+((".")?[0-9]*)?([eE][+-]?[0-9]+)? {return new Symbol(sym.FLOAT, new Float(yytext()));}
"-"[0-9]+((".")?[0-9]*)?([eE][+-]?[0-9]+)? {return new Symbol(sym.FLOAT, new Float(yytext()));}
[\t] { /* ignorar espacios en blanco. */ }
" " {}
[\n] {;}
. { System.err.println("Illegal character: "+yytext()); }