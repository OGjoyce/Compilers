/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser
*/

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    private int[] arregloPre = {1,2,3};
    // Stacks para evaluar online
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();
        while(S()) {System.out.println(this.operandos.peek());}
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            this.next++;
            return true;
        }
        return false;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */ //NO ENTENDI QUE HACER
        if(op = Token.LPAREN){
            return 1;
            if(op = Token.RPAREN){
                return 2;
                if(op = Token.EXP){
                    return 3;
                    if(op = Token.MULT || Token.DIV || Token.MOD){
                        return 4;
                        if(op=Token.UNARY){
                            return 5;
                            if(op=Token.PLUS || Token.MINUS){
                                return 6;
                            }
                        }
                    }
                }
            }
        }


    }

    private void popOp() {
        Token op = this.operadores.pop();
        if(op.equals(Token.UNARY)) {
            /* TODO: Su codigo aqui */ //NO ENTENDI QUE HACER
        } else {
            /* TODO: Su codigo aqui */ // NO ENTENDI QUE HACER
        }
    }

    private void pushOp(Token op) {
        /* TODO: Su codigo aqui */
      //  while lo que esta en el top del stack tenga mayor precedencia a lo que viene ver no entendi que hacer
        
    }

    private boolean S() {
        return E() && term(Token.SEMI);
    }
/* S=E;
E=PE1
P=(E)|UP|NUMBER
E1= BPE1|LAMBDA
B=OPBINARIA
U=-
*/
    private boolean E() {
        return P() && M();
    }

    /* TODO: sus otras funciones aqui */
    private boolean M(){
        return B() && P() && M() || lambda();
    }
    private boolean P(){
        return EP() || U() && P() || N();
    }
    private boolean EP(){
        return term(Token.LPAREN) && E() && term(Token.RPAREN);
    }
    private boolean U(){
        return term(Token.MINUS);
    }
    private boolean N(){
        return term(Token.NUMBER);
    }
    private boolean B(){
        return SUMA() || RESTA() || MULTIP() || DIVISION() || MODULO() || EXPON();
    }
    // operaciones binarias
    private boolean SUMA(){
        return term(Token.PLUS);
    }
    private boolean RESTA(){
        return term(Token.MINUS);
    }
    private boolean MULTIP(){
        return term(Token.MULT);
    }
    private boolean DIVISION(){
        return term(Token.DIV);
    }
    private boolean MODULO(){
        return term(Token.MOD);
    }
    private boolean EXPON(){
        return term(Token.EXP);
    }
    private boolean lambda(){
        return true;
    }
}
