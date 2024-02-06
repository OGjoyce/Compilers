/*
 *  The scanner definition for COOL.
 */
/*
@author Carlos Montiel
15000552
Galileo University
Phase 1/4 
curent perl score: 3/63;
Implementation (steps):
make a table "test.cl" test.cl is a testing based on .txt format that contains the characters that are used for testing our
lexical analyzer code that has to match with each TokenConstants of TokenConstants.java, while using
jlex syntax.

KEYWORDS have the form:
<YYINITIAL>[i][f]         {return new Symbol(TokenConstants.IF);}

OPERATIONS have the form:
<YYINITIAL>"-"          {return new Symbol(TokenConstants.MINUS);}

INTEGERS have the form:
<YYINITIAL>[0-9]+       {return new Symbol(TokenConstants.INT_CONST, new IdSymbol(yytext(), yytext().length(),ci++));}
so,
yytext() =  test.cl table.
yytext().length = test.cl strings based on position of our lexer pointer.
ci++ = parameter for newline counter

IDENTIFIERS have the form:
<YYINITIAL>\[a-zA-Z | 0-9]+ {return new Symbol(TokenConstants.STR_CONST, new IdSymbol(yytext(), yytext().length(),cs++));}
as you can see the regular expression has a complex form than integers, these has to match with every test.cl
string on table.
the identifiers parameters ate the same as integers parameters (read above).

TokensConstants.java has one token that is not used until PA3 (no need implementation for this):
public static final int LET_STMT = 9;
public static final int error = 1;

The next parts of project will be written on each source code that needs to be modified.

WARNING,
Comments on code will not be added 'cause you have to read the Implementation to understand.
 */
import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int ci = 0;
    int cs = 0;
    int vars = 0;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }
    int pro = 0;
    
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
    case YYCM:
	yybegin(YYEOFER);
    return new Symbol(TokenConstants.ERROR, "Error at line: "+curr_lineno);
    case YYEST:
    yybegin(YYEOFER);
    return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    case YYEOFER:
    break;

    }
    return new Symbol(TokenConstants.EOF);
%eofval}


%class CoolLexer
%cup
%state YYCM, YYEST, YYSTRNERR, YYSTRNULLERR, YYEOFER
INECOMMENT  = --[^\n]*
COMMENTI = \(\*
COMMENTF   = \*\)
ASTERISCO =\*
STRINI  = \"
STRINGC  = .[^\"\0\n\\]+
STRINGF  = \"
TYPEID       = [A-Z][A-z0-9_]* | [A-Z][_][A-z0-9_]*
OBJECTID     = [a-z][A-z0-9_]*
CUALQUIER = .|\r
CHARS = [^\n\0\\\"]
DIGITOS = [\0-\9]
BACKSLASH = [\\]
%%  
<YYINITIAL>"=>"         {return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>\n           {curr_lineno++;}

<YYINITIAL>{COMMENTI}  { yybegin(YYCM); pro++; }
<YYCM>{COMMENTF}            {pro--; if(pro == 0){yybegin(YYINITIAL);}}
<YYCM>\n {curr_lineno++;}
<YYCM>[a-zA-Z]*         {;}
<YYCM>[0-9]*            {;}
<YYCM>{CUALQUIER}               {string_buf.append(yytext());}
<YYCM>{ASTERISCO}  {curr_lineno++;}
<YYCM>"*"              {curr_lineno++;}
<YYCM>{STRINI}   {;}
<YYCM>{STRINGF} {;}
<YYCM>{COMMENTI} {pro++;}
<YYCM>{BACKSLASH} {;}
<YYEST>{COMMENTI}       {string_buf.append(yytext());}
<YYEST>{COMMENTF}       {string_buf.append(yytext());}
<YYEST>\\\"                       { string_buf.append("\""); }
<YYEST>\\f                        { string_buf.append("\f"); }
<YYEST>\\b                        { string_buf.append("\b"); }
<YYEST>\\t                        { string_buf.append("\t"); }
<YYEST>\\n                        { string_buf.append("\n"); }
<YYEST>\\\\n                      { string_buf.append("\\n"); }
<YYEST>\\\n                       { string_buf.append("\n"); }
<YYEST>\\\\                       { string_buf.append("\\"); }
<YYEST>\\                         {;}
<YYEST>[\\]*                       {string_buf.append(yytext());}
<YYEST>\u000b {string_buf.append("\u000b");}
<YYEST>\n                         {string_buf.setLength(0);
                            yybegin(YYINITIAL);
                            return new Symbol(TokenConstants.ERROR, "String not defined..."); }
<YYEST>\x00              { yybegin(YYSTRNULLERR);
                                           return new Symbol(TokenConstants.ERROR, "String has an invalid character(s)."); }
<YYEST>{STRINGF}       { yybegin(YYINITIAL);
                                      String xxzzxxzpape = string_buf.toString();
                                      if(xxzzxxzpape.length() >= 1025) {
                                            return new Symbol(TokenConstants.ERROR, "String too long, (s.length() <= 1024)....");
                                      } else {
                                            return new Symbol(TokenConstants.STR_CONST,
                                                new StringSymbol(xxzzxxzpape, xxzzxxzpape.length(), xxzzxxzpape.hashCode()));

                                      }
                                  }
<YYEST>{CUALQUIER}      {string_buf.append(yytext());}
<YYEST>{DIGITOS}    {string_buf.append(yytext());}
<YYEST>" "      {;}
<YYINITIAL>--[^\n]* {;}
<YYINITIAL>[i|I][f|F]         {return new Symbol(TokenConstants.IF);}
<YYINITIAL>[f|F][i|I]         {return new Symbol(TokenConstants.FI);}   
<YYINITIAL>[e|E][l|L][s|S][e|E]         {return new Symbol(TokenConstants.ELSE);}
<YYINITIAL>[c|C][l|L][a|A][s|S][s|S]         {return new Symbol(TokenConstants.CLASS);}
<YYINITIAL>[i|I][n|N]         {return new Symbol(TokenConstants.IN);}
<YYINITIAL>[I|i][N|n][H|h][E|e][R|r][I|i][T|t][S|s]         {return new Symbol(TokenConstants.INHERITS);}
<YYINITIAL>[I|i][S|s][V|v][O|o][I|i][D|d]         {return new Symbol(TokenConstants.ISVOID);}
<YYINITIAL>[L|l][E|e][T|t]         {return new Symbol(TokenConstants.LET);}
<YYINITIAL>[L|l][O|o][O|o][P|p]         {return new Symbol(TokenConstants.LOOP);}
<YYINITIAL>[P|p][O|o][O|o][L|l]        {return new Symbol(TokenConstants.POOL);}
<YYINITIAL>[T|t][H|h][E|e][N|n]         {return new Symbol(TokenConstants.THEN);}
<YYINITIAL>[w|W][H|h][I|i][L|l][E|e]         {return new Symbol(TokenConstants.WHILE);}
<YYINITIAL>[c|A][A|a][S|s][E|e]         {return new Symbol(TokenConstants.CASE);}
<YYINITIAL>[e|E][s|S][a|A][c|C]         {return new Symbol(TokenConstants.ESAC);}
<YYINITIAL>[n|N][e|E][w|W]         {return new Symbol(TokenConstants.NEW);}
<YYINITIAL>[o|O][f|F]         {return new Symbol(TokenConstants.OF);}
<YYINITIAL>[n|N][o|O][t|T]         {return new Symbol(TokenConstants.NOT);}
<YYINITIAL>[t][r|R][u|U][e|E] {return new Symbol(TokenConstants.BOOL_CONST,true);}
<YYINITIAL>[f][a|A][l|L][s|S][e|E]+ {return new Symbol(TokenConstants.BOOL_CONST,false);}
<YYINITIAL>" " {;}
<YYINITIAL>","          {return new Symbol(TokenConstants.COMMA);}
<YYINITIAL>\f {;} 
<YYINITIAL>\r {;}
<YYINITIAL>\t {;}
<YYINITIAL>\v {;}
<YYINITIAL>\u000b {;}
<YYINITIAL>"<-" {return new Symbol(TokenConstants.ASSIGN);}
<YYINITIAL>"-"          {return new Symbol(TokenConstants.MINUS);}
<YYINITIAL>"+"          {return new Symbol(TokenConstants.PLUS);}
<YYINITIAL>"*"          {return new Symbol(TokenConstants.MULT);}
<YYINITIAL>"="          {return new Symbol(TokenConstants.EQ);}
<YYINITIAL>"/"          {return new Symbol(TokenConstants.DIV);}
<YYINITIAL>"~"          {return new Symbol(TokenConstants.NEG);}
<YYINITIAL>"{"          {return new Symbol(TokenConstants.LBRACE);}
<YYINITIAL>"}"          {return new Symbol(TokenConstants.RBRACE);}
<YYINITIAL>")"          {return new Symbol(TokenConstants.RPAREN);}
<YYINITIAL>"("          {return new Symbol(TokenConstants.LPAREN);}
<YYINITIAL>"@"          {return new Symbol(TokenConstants.AT);}
<YYINITIAL>";"          {return new Symbol(TokenConstants.SEMI);}
<YYINITIAL>":"          {return new Symbol(TokenConstants.COLON);}
<YYINITIAL>"."          {return new Symbol(TokenConstants.DOT);}
<YYINITIAL>"<="         {return new Symbol(TokenConstants.LE);}
<YYINITIAL>"<"          {return new Symbol(TokenConstants.LT);}
<YYINITIAL>"_"         {return new Symbol(TokenConstants.ERROR, yytext());}
<YYINITIAL>{TYPEID}  { return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
<YYINITIAL>">"          {return new Symbol(TokenConstants.ERROR);}
<YYINITIAL>">="         {return new Symbol(TokenConstants.ERROR);}
<YYINITIAL>{STRINI} { yybegin(YYEST); }
<YYINITIAL>"*)"         {return new Symbol(TokenConstants.ERROR, "end of comment without start");}
<YYSTRNULLERR>\n               { yybegin(YYINITIAL); }
<YYSTRNULLERR>{STRINI}               { yybegin(YYINITIAL); }
<YYSTRNULLERR>.                { ; }

<YYINITIAL>[0-9]+       { return new Symbol(TokenConstants.INT_CONST,
                                         new IntSymbol(yytext(), yytext().length(), yytext().hashCode())); }
<YYINITIAL>{OBJECTID}        { return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
<YYINITIAL>[\][n]   {;}
<YYINITIAL>[\][t] {;}
.|\n                       {System.err.println("no hizo ningun match: " + yytext());}
