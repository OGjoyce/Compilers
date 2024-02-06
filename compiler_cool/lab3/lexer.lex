/*

    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    En este archivo ustedes tienen que crear un lexer que sea capaz de reconocer
    los tokens de la siguiente gramática:

    S ::= E;
    E ::= E + E
        | E - E
        | E * E
        | E / E
        | E % E
        | E ^ E
        | - E
        | (E)
        | number

    **** Cosas Importantes ****:

    1. Lo que está en minúscula son terminales
    2. Lo que está en mayúscula son no terminales
    3. number son números double de java que siguen el siguiente formato:

        signo   = [+-]
        digitos = [0-9]
        punto = .
        exponente = [eE]
        (digitos)+((punto)?(digitos)*)?((exponente)(signo)?(digitos)+)?

        donde '+' es uno o más veces
        donde '*' es cero o más veces
        donde '?' es cero o una vez

    4. Cuando hacemos match con un token devolvemos un objeto de tipo Token
        4.1 Ver clase Token
        4.2 Si encuentran un numero el constructor que tienen que usar es:
            Token(id, val)
        4.3 En la clase token hay constantes que definen el id de cada token
*/

import java.io.StringReader;
import java.io.IOException;

%%

%{

    public static void main(String[] args) throws IOException {
        String input = "";
        for(int i=0; i < args.length; i++) {
            input += args[i];
        }
        Lexer lexer = new Lexer(new StringReader(input));
        Token token;
        while((token = lexer.nextToken()) != null) {
            System.out.println(token);
        }
    }


    /*

        ****************** LEER ********************

        - %public es para que la clase sea publica y se pueda utilizar en otros paquetes
        - %class Lexer es para que la clase generada se llame "Lexer"
        - %function nextToken el lexer generado tendra una funcion nextToken() para obtener
           el siguiente token del input
        - %type Token es para que la clase tome en cuenta que vamos a devolver un objeto Token

        todo esto no se modifica por ningun motivo :)

        *** Despues de "%type Token" pueden definir sus ER o tokens, van a encontrar
        el ejemplo para SEMI (";") y para WHITESPACE
    */
%}

%public
%class Lexer
%function nextToken
%type Token

SEMI = ";" // Definan aqui sus Tokens/ER por ejemplo: "el token SEMI"
WHITE = (" "|\t|\n)
SIGNOMAS  = "+"
SIGNOMENOS = "-"
POR = "*"
SLASH = "/"
DIVI = "%"
EXPE = "^"
PA = "("
PC = ")"
ERROR = "ERROR"
DIGITOS = [0-9]
PUNTO = .
EXPONENTE = [eE](digitos)+((punto)?(digitos)*)?((exponente)(signo)?(digitos)+)?
DOUBLE = [0-9]+((".")?[0-9]*)?([eE][+-]?[0-9]+)?

%%

<YYINITIAL>{SEMI}   { return new Token(Token.SEMI);   }
<YYINITIAL>{SIGNOMAS}    {return new Token(Token.PLUS);}
<YYINITIAL>{SIGNOMENOS}     {return new Token(Token.MINUS);}
<YYINITIAL>{POR}        {return new Token(Token.MULT);}
<YYINITIAL>" "        {}
<YYINITIAL>{DIVI}   {return new Token(Token.DIV);}
<YYINITIAL>{PA}       {return new Token(Token.LPAREN);}
<YYINITIAL>{PC}     {return new Token(Token.RPAREN);}
<YYINITIAL>{DOUBLE} {return new Token(Token.NUMBER, yytext());}
<YYINITIAL>{WHITE}  { /* NO HACER NADA */             }
<YYINITIAL>.        { return new Token(Token.ERROR);
                      /* todo lo demas es ERROR */ }
