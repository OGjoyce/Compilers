import java.io.PrintStream;
import java.util.*;

class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;
    // protected para que los podamos acceder en el paquete
    protected Hashtable<AbstractSymbol, AbstractSymbol> IG;
    protected Hashtable<AbstractSymbol, SymbolTable> O;
    protected Hashtable<AbstractSymbol, Hashtable<AbstractSymbol, method>> M;
    protected AbstractSymbol C;
    protected AbstractSymbol F;
    protected AbstractSymbol filename;

    // Constructor de classtable recibe classes que son las que usan
    // para hacer el analisis de la primera vuelta
    public ClassTable(Classes cls) {
        this.semantErrors = 0;
        this.errorStream = System.err;
        // Inicializamos las hashtables para poder utilizarlas despues
        this.IG = new Hashtable<AbstractSymbol, AbstractSymbol>();
        this.O = new Hashtable<AbstractSymbol, SymbolTable>();
        this.M = new Hashtable<AbstractSymbol, Hashtable<AbstractSymbol, method>>();
        // Por el momento digamos que el currentClass es Object aunque ni lo usemos
        this.C = TreeConstants.Object_;
        // y pongamos tambien un filename asi dummy (el de object)
        this.filename = TreeConstants.filename;
        // mandamos a llamar a la primera vuelta "init"
        this.init(cls);
    }

    /*
     *----------------------------PRIMERA VUELTA ------------------------------
     */
    private void init(Classes cls) {
        // 1 instalamos las clases basicas en el grafo de herencia
        this.installBasicClasses();
        // 2 vericamos que no se redefinan clases basicas, ni clases
        // construimos el grafo de herencia hijo->padre
        for(int i=0; i < cls.getLength(); i++) {
            class_c cl = (class_c) cls.getNth(i);
            if(this.basicClass(cl.name)) {
                SemantErrors.basicClassRedefined(cl.name, this.semantError(cl));
                this.exit();
            } else if (this.IG.containsKey(cl.name)) {
                SemantErrors.classPreviouslyDefined(cl.name, this.semantError(cl));
                this.exit();
            } else {
                this.IG.put(cl.name, cl.parent);
            }
        }
        // 3 verificamos que el parent de las clases exista y si existe
        // que no sea un parent basico (Int, Bool, String, SELF_TYPE)
        for(int i=0; i < cls.getLength(); i++) {
            class_c cl = (class_c) cls.getNth(i);
            if(basicParent(cl.parent)) {
                SemantErrors.cannotInheritClass(cl.name, cl.parent, this.semantError(cl));
                this.exit();
            } else if(!this.IG.containsKey(cl.parent)) {
                SemantErrors.inheritsFromAnUndefinedClass(cl.name, cl.parent, this.semantError(cl));
                this.exit();
            }
        }
        // 4 Verificamos que no hayan ciclos en el grafo de herencia
        Vector<AbstractSymbol> V = new Vector<AbstractSymbol>();
        for(int i= cls.getLength() - 1; i >= 0; i--) {
            class_c cl = (class_c) cls.getNth(i);
            if(hasCycles(cl.name, V)) {
                SemantErrors.inheritanceCycle(cl.name, this.semantError(cl));
            }
            V.clear();
        }
        // Si despues de verificar los ciclos, hay algun error entonces
        // nos salimos (compilation halted bla bla bla) ...
        if(this.errors()) {
            this.exit(); // exit tiene eso de compilation halted ....
        }
        // **A partir de aqui ya no utilizamos exit hasta que terminemos de
        // anotar todo el arbol

        // 5 por cada clase que exista en el grafo de herencia, creamos un Scope
        // global (donde estan los atributos) e instanciamos una hashtable por
        // clase que va a tener <NombreMetodo, firmaMetodo>
        Enumeration<AbstractSymbol> classes = this.IG.keys();
        while(classes.hasMoreElements()) {
            AbstractSymbol cl = classes.nextElement();
            SymbolTable st = new SymbolTable();
            st.enterScope(); // Scope global
            this.O.put(cl, st);
            this.M.put(cl, new Hashtable<AbstractSymbol, method>());
        }
        // 6 instalamos los atributos y metodos de las clases basicas
        this.installBasicClassesMA();
        // 7 por cada clase restante en el archivo instalamos sus metodos
        // y sus atributos, verificamos que no se vuelvan a redefinir
        for(int i=0; i < cls.getLength(); i++) {
            // este metodo los guarda y verifica que ya existan para mandar error
            this.installMA((class_c) cls.getNth(i));
        }
        // 8 Si la clase Main no existe ni su metodo main es un error ,porque
        //todos los programas empiezan en la clase Main y con el metodo main
        if(this.IG.containsKey(TreeConstants.Main)) {
            if(!this.M.get(TreeConstants.Main).containsKey(TreeConstants.main_meth)) {
                SemantErrors.noMainMethodInMainClass(this.semantError());
            }
        } else {
            SemantErrors.noClassMain(this.semantError());
        }
    }

    // Instala las clases basicas
    private void installBasicClasses() {
        this.IG.put(TreeConstants.Object_, TreeConstants.No_class);
        this.IG.put(TreeConstants.IO, TreeConstants.Object_);
        this.IG.put(TreeConstants.Int, TreeConstants.Object_);
        this.IG.put(TreeConstants.Str, TreeConstants.Object_);
        this.IG.put(TreeConstants.Bool, TreeConstants.Object_);
    }

    // Verifica que sea una clase basica, incluyendo SELF_TYPE
    private boolean basicClass(AbstractSymbol name) {
        boolean flg = name == TreeConstants.Object_ || name == TreeConstants.IO
                   || name == TreeConstants.Int     || name == TreeConstants.Str
                   || name == TreeConstants.Bool    || name == TreeConstants.SELF_TYPE;
        return flg;
    }

    // Verifica que sea parent basico, incluyendo SELF_TYPE
    private boolean basicParent(AbstractSymbol name) {
        boolean flg = name == TreeConstants.Int  || name == TreeConstants.Str
                   || name == TreeConstants.Bool || name == TreeConstants.SELF_TYPE;
        return flg;
    }

    // Verifica si hay ciclos en el grafo de herencia de alguna clase, COMO el lab
    private boolean hasCycles(AbstractSymbol name, Vector<AbstractSymbol> V) {
        if(name == TreeConstants.Object_) {
            return false;
        } else if (V.contains(name)) {
            return true;
        } else {
            V.addElement(name);
            return hasCycles(this.IG.get(name), V);
        }
    }

    // Instala los metodos y atributos de las clases basicas, utiliza installMA
    private void installBasicClassesMA() {
        this.installMA(TreeConstants.Object_class);
        this.installMA(TreeConstants.IO_class);
        this.installMA(TreeConstants.Int_class);
        this.installMA(TreeConstants.Bool_class);
        this.installMA(TreeConstants.Str_class);
    }

    // Este metodo recorre los features de una clase (cl) y los instala en sus
    // respectivos environments, si se redefinen en la misma clase hay un
    // error semantico, como indica cool
    private void installMA(class_c cl) {
        for(int i=0; i < cl.features.getLength(); i++) {
            Feature f = (Feature) cl.features.getNth(i);
            if(f instanceof method) {
                method m = (method) f;
                if(this.M.get(cl.name).containsKey(m.name)) {
                    SemantErrors.methodMultiplyDefined(m.name, this.semantError(cl));
                } else {
                    this.M.get(cl.name).put(m.name, m);
                }
            } else {
                attr a = (attr) f;
                if(this.O.get(cl.name).probe(a.name) != null) {
                    SemantErrors.attrMultiplyDefined(a.name, this.semantError(cl));
                } else {
                    this.O.get(cl.name).addId(a.name, a.type_decl);
                }
            }
        }
    }

    /*
     *--------------- COSAS UTILES PARA RESTO ANALISIS -------------------------
     * todo lo que esta aqui puede servir para despues, ustedes miran si lo usan
     * o no
     */

    // Esto cambia la current class al name dado
    public void setCurrentClass(AbstractSymbol name) {
        this.C = name;
    }

    // Esto cambia el filename al filename dado
    public void setFilename(AbstractSymbol filename) {
        this.filename = filename;
    }

    // Devuelve true si la current class es Main
    public boolean mainClass() {
        return this.C == TreeConstants.Main;
    }

    // Devuelve los parents dado un nombre de alguna clase
    public Vector<AbstractSymbol> getParents(AbstractSymbol name) {
        /*
            COMPLETAR ESTO SI LES SIRVE
        */
        return null;
    }

    // Verifica si una clase existe en el grafo de herencia
    public boolean classExists(AbstractSymbol name) {
        return name == TreeConstants.SELF_TYPE || this.IG.containsKey(name);
    }

    // devuleve true si t1 <= t2
    public boolean LE(AbstractSymbol t1, AbstractSymbol t2) {
        /*
            Completar esta onda si les sirve
        */
        return false;
    }

    // devuelve el LUB entre dos tipos
    public AbstractSymbol LUB(AbstractSymbol t1, AbstractSymbol t2) {
        /*
            Completar esta onda si les sirve
        */
        return null;
    }

    // Manejo de errores
    public PrintStream semantError(class_c c) {
        return this.semantError(c.getFilename(), c);
    }

    /*
        Este metodo es el que tienen que utilizar dentro de cool-tree.java
        por ejemplo
        OMC.semantError(this); estando en algun nodo
    */
    public PrintStream semantError(TreeNode t) {
        return this.semantError(this.filename, t);
    }

    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
        this.errorStream.print(filename + ":" + t.getLineNumber() + ": ");
        return this.semantError();
    }

    public PrintStream semantError() {
        this.semantErrors++;
        return this.errorStream;
    }

    public boolean errors() {
        return this.semantErrors != 0;
    }

    // para salir por algun error semantico
    public void exit() {
        System.err.println("Compilation halted due to static semantic errors.");
        System.exit(1);
    }
}
