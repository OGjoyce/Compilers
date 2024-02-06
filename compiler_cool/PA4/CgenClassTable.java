/*CgenClassTable.java (esqueleto) 

*/


import java.util.Vector;
import java.util.Stack;
import java.util.Enumeration;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.ArrayList;


class CgenClassTable extends SymbolTable {

    /** Vector de mucha utilidad*/
    private Vector nds;

    /** string de outputs mips */
    private PrintStream str;
    /* tags */
    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;

    // por si se usa el gc.
    public boolean gcUsed; 
    
    public HashMap<AbstractSymbol, HashMap<AbstractSymbol, Integer>> metodos_y_offset;
    // contador de etiquetas.
    public int labelCount;
    // guardamos clases y metodos
    public HashMap<AbstractSymbol, ArrayList<AbstractSymbol>> methodMap;
    //hashmaps <atributos, offset>.... <method, offset>
    public HashMap<AbstractSymbol, HashMap<AbstractSymbol, Integer>> atributos_y_offset;

    public HashMap<AbstractSymbol, ArrayList<AbstractSymbol>> classMap;

    //  guardamos el height de clasee en inheritance tree
    private HashMap<AbstractSymbol, Integer> heightMap;
    private HashMap<AbstractSymbol, int[]> mapofRange;

    public int getHeight(AbstractSymbol className) { 
        return heightMap.get(className);
    }

    public int[] getChildrenRange(AbstractSymbol className) {
        return mapofRange.get(className);
    }

 

    // Clasificar nds de acuerdo  a la primera búsqueda en el tree
    private void sortNds_InitMap() {
        nds.clear();
        heightMap = new HashMap<AbstractSymbol, Integer>();
        mapofRange = new HashMap<AbstractSymbol, int[]>();
        CgenNode root = root();
        heightMap.put(root.name, 0);
        xxxiiijjj(root);
    }

 //el siguiente metodo emite codigo para const local y global declarations

 
    private void codeGlobalData() {
    

        str.print("\t.data\n" + CgenSupport.ALIGN);
        str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitProtObjRef(TreeConstants.Main, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitProtObjRef(TreeConstants.Int, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitProtObjRef(TreeConstants.Str, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        BoolConst.falsebool.codeRef(str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        BoolConst.truebool.codeRef(str);
        str.println("");
        str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
        str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
        str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

        //aqui sabemos el numero de tag (importante para asignar el numero a la hora de emitir code)

        str.println(CgenSupport.INTTAG + CgenSupport.LABEL + CgenSupport.WORD
                + intclasstag);
        str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL + CgenSupport.WORD
                + boolclasstag);
        str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL
                + CgenSupport.WORD + stringclasstag);

    }

    /**
     * Emits code to start the .text segment and to declare the global names.
     * */
    private void codeGlobalText() {
        str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
        str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
        str.println(CgenSupport.WORD + 0);
        str.println("\t.text");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(TreeConstants.Main, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(TreeConstants.Int, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(TreeConstants.Str, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(TreeConstants.Bool, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth,
                str);
        str.println("");
    }

    //por orden se puso asi
    private void codeBools(int classtag) {
        BoolConst.falsebool.codeDef(classtag, str);
        BoolConst.truebool.codeDef(classtag, str);
    }

    /**  genera las constantes para GC apunta al as funciones de GC */
    private void codeSelectGc() {
        /*
            Esto genera el codigo para el Garbage Collector, que son los
            punteros a las funciones del GC.

            todo este codigo representa lo siguiente:

                .globl    _MemMgr_INITIALIZER
            _MemMgr_INITIALIZER:
                .word    _NoGC_Init
                .globl    _MemMgr_COLLECTOR
            _MemMgr_COLLECTOR:
                .word    _NoGC_Collect
                .globl    _MemMgr_TEST
            _MemMgr_TEST:
                .word    0
        */
        str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
        str.println("_MemMgr_INITIALIZER:");
        str.println(CgenSupport.WORD
                + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

        str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
        str.println("_MemMgr_COLLECTOR:");
        str.println(CgenSupport.WORD
                + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

        str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
        str.println("_MemMgr_TEST:");
        str.println(CgenSupport.WORD
                + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /**
     * emite codigo para reservar espacio inicializando todas las constantes.
     * Class names se agregan a la string table, (cuando se construye el inheritance graph)
     * length de la integer table, las constantes son emitidas cuando recorre
     * la stringttable y la inttable y produce codigo para cada entrada :)
     */
    private void codeConstants() {
        //nuestras constantes que nos van a servir para code generator
        AbstractTable.stringtable.addString("");
        AbstractTable.inttable.addString("0");

        AbstractTable.stringtable.codeStringTable(stringclasstag, str);
        AbstractTable.inttable.codeStringTable(intclasstag, str);
        codeBools(boolclasstag);
    }

    private void classNameT() {
        str.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);//imprimimos
        for (int i = 0; i < nds.size(); i++) {//recorremos el nds
            str.print(CgenSupport.WORD);//.WORD
            AbstractSymbol name = ((CgenNode) nds.get(i)).name;
            ((StringSymbol) AbstractTable.stringtable
                    .addString(name.toString())).codeRef(str); //agregamos a nuestra stringtable el nombre del string y el numero
            str.println("");
        }
    }

    
    private void codeClassObjTab() {
        str.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
        for (int i = 0; i < nds.size(); i++) {
            AbstractSymbol name = ((CgenNode) nds.get(i)).name;
            str.print(CgenSupport.WORD);
            CgenSupport.emitProtObjRef(name, str); //emit proto
            str.println("");

            str.print(CgenSupport.WORD);
            CgenSupport.emitInitRef(name, str); //emit init
            str.println("");
        }
    }

    private void dispatchTables() {
       
        classMap = new HashMap<AbstractSymbol, ArrayList<AbstractSymbol>>();
        methodMap = new HashMap<AbstractSymbol, ArrayList<AbstractSymbol>>();

        classMap.put(TreeConstants.No_class, new ArrayList<AbstractSymbol>());
        methodMap.put(TreeConstants.No_class, new ArrayList<AbstractSymbol>());

        Stack<CgenNode> todoList = new Stack<CgenNode>();
        todoList.add((CgenNode) probe(TreeConstants.Object_));

        while (!todoList.isEmpty()) {
            CgenNode nd = todoList.pop();//pop
            AbstractSymbol className = nd.name; //nombre

            ArrayList<AbstractSymbol> classList = new ArrayList<AbstractSymbol>(
                    classMap.get(nd.getParent())); //padres
            ArrayList<AbstractSymbol> methodList = new ArrayList<AbstractSymbol>(
                    methodMap.get(nd.getParent())); //metodos

            for (Enumeration e = nd.features.getElements(); e.hasMoreElements();) {
                Object ft = e.nextElement();
                if (ft instanceof method) {
                    AbstractSymbol methodName = ((method) ft).name;

                    int index = methodList.indexOf(methodName);
                    if (index == -1) {
                        classList.add(className);
                        methodList.add(methodName);
                    } else {
                        classList.set(index, className);
                    }
                }
            }

            CgenSupport.emitDispTableRef(className, str);
            str.print(CgenSupport.LABEL);

            for (int i = 0; i < methodList.size(); i++) {
                str.print(CgenSupport.WORD);
                CgenSupport.emitMethodRef(classList.get(i), methodList.get(i),
                        str);
                str.println("");
            }

            classMap.put(className, classList);
            methodMap.put(className, methodList);

            HashMap<AbstractSymbol, Integer> methodOffset = new HashMap<AbstractSymbol, Integer>();
            for (int i = 0; i < methodList.size(); i++) {
                methodOffset.put(methodList.get(i), i);
            }
            metodos_y_offset.put(className, methodOffset);

            for (Enumeration e = nd.getChildren(); e.hasMoreElements();) {
                todoList.push((CgenNode) e.nextElement());
            }
        }
    }

    private void protos() {

        // guardamos atributos de cada clase. mapa_de_attrName
        HashMap<AbstractSymbol, ArrayList<AbstractSymbol>> mapa_de_attrName = new HashMap<AbstractSymbol, ArrayList<AbstractSymbol>>();
        HashMap<AbstractSymbol, ArrayList<AbstractSymbol>> type_attr = new HashMap<AbstractSymbol, ArrayList<AbstractSymbol>>();

        mapa_de_attrName
                .put(TreeConstants.No_class, new ArrayList<AbstractSymbol>()); //put(no class, [])
        type_attr
                .put(TreeConstants.No_class, new ArrayList<AbstractSymbol>()); //put(no class, [])

        Stack<CgenNode> todoList = new Stack<CgenNode>();
        todoList.add((CgenNode) probe(TreeConstants.Object_)); //[[stack]].add(Object) para que este en el top del stack

        while (!todoList.isEmpty()) { //como nunca va a estar vacio
            CgenNode nd = todoList.pop(); //pop
            AbstractSymbol className = nd.name; //sacamos el nombre de la clase.

            ArrayList<AbstractSymbol> attrNameList = new ArrayList<AbstractSymbol>( 
                    mapa_de_attrName.get(nd.getParent())); //attrNamelist = [[[parent de la clase]]]
            ArrayList<AbstractSymbol> attrTypeList = new ArrayList<AbstractSymbol>(
                    type_attr.get(nd.getParent())); // [[parent del atriibuto con su respectivo tipo]]

            for (Enumeration e = nd.features.getElements(); e.hasMoreElements();) { //las features de la clase 
                Object ft = e.nextElement(); // feature de cada elemento
                if (ft instanceof attr) { 
                    attr a = (attr) ft; //cast a attr
                    attrNameList.add(a.name); //agregamos a nuestro arraylist el nombre
                    attrTypeList.add(a.type_decl); // agregamos a nuestro arraylist el tipo
                }
            }

            str.println(CgenSupport.WORD + "-1"); //eyecatcher
            CgenSupport.emitProtObjRef(className, str);//protos
            str.print(CgenSupport.LABEL);

            int classTag = nds.indexOf(nd); //clastag = index de la clase
            str.println(CgenSupport.WORD + classTag);//word #

            int objectSize = attrNameList.size() //size del arraylist 
                    ;
            str.println(CgenSupport.WORD + objectSize + CgenSupport.DEFAULT_OBJFIELDS); 

            str.print(CgenSupport.WORD);
            CgenSupport.emitDispTableRef(className, str);
            str.println("");

            for (int i = 0; i < attrTypeList.size(); i++) { //recorremos nuestra lista de tipos de atributo
                str.print(CgenSupport.WORD);//por cada uno un WORD

                AbstractSymbol type = attrTypeList.get(i); //agarramos el tipo de cada uno
                if (type == TreeConstants.Int || type == TreeConstants.Str
                        || type == TreeConstants.Bool) { //comparaciones de int, str y bool
                    CgenSupport.emitProtObjRef(type, str);
                } else {
                    str.print(0);
                }

                str.println("");
            }

            mapa_de_attrName.put(className, attrNameList); //<classname, lista de los nombres de los atributos>
            type_attr.put(className, attrTypeList);

            HashMap<AbstractSymbol, Integer> attrOffset = new HashMap<AbstractSymbol, Integer>(); //attrpffset<As, #>
            for (int i = 0; i < attrNameList.size(); i++) {//lo recorremos
                attrOffset.put(attrNameList.get(i), //le ponemos el nombre del atributo, el default + i
                        CgenSupport.DEFAULT_OBJFIELDS + i);
            }
            atributos_y_offset.put(className, attrOffset);//<classname, <AS, #>>

            for (Enumeration e = nd.getChildren(); e.hasMoreElements();) {
                todoList.push((CgenNode) e.nextElement());
            }
        }
    }

    //fucking inits

    private void Initializer() {
        for (Enumeration e1 = nds.elements(); e1.hasMoreElements();) {
            CgenNode nd = (CgenNode) e1.nextElement();

            CgenSupport.emitInitRef(nd.name, str);
            str.print(CgenSupport.LABEL);

            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -3
                    * CgenSupport.WORD_SIZE, str);
            CgenSupport.emitStore(CgenSupport.FP, 3, CgenSupport.SP, str);
            CgenSupport.emitStore(CgenSupport.SELF, 2, CgenSupport.SP, str);
            CgenSupport.emitStore(CgenSupport.RA, 1, CgenSupport.SP, str);

            CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP,
                    CgenSupport.WORD_SIZE * 3, str);
            CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str);

            if (nd.getParent() != TreeConstants.No_class) {
                str.print(CgenSupport.JAL);
                CgenSupport.emitInitRef(nd.getParent(), str);
                str.println();
            }

            HashMap<AbstractSymbol, Integer> offsetMap = atributos_y_offset
                    .get(nd.name);
            for (Enumeration e2 = nd.features.getElements(); e2
                    .hasMoreElements();) {
                Object ft = e2.nextElement();
                if (ft instanceof attr) {
                    attr a = (attr) ft;
                    if (!(a.init instanceof no_expr)) {
                        SymbolTable env = new SymbolTable();
                        env.enterScope();
                        a.init.code(str, nd.name, this, env, 3);
                        int offset = offsetMap.get(a.name);
                        CgenSupport.emitStore(CgenSupport.ACC, offset,
                                CgenSupport.SELF, str);
                        env.exitScope();
                        
                        if (gcUsed) {
                            // _GenGC_Assign saves $a0, so no need to save here
                            CgenSupport.emitAddiu(CgenSupport.A1, CgenSupport.SELF, offset * CgenSupport.WORD_SIZE, str);
                            CgenSupport.emitJal("_GenGC_Assign", str);
                        }
                    }
                }
            }

            CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, str);
            CgenSupport.emitLoad(CgenSupport.FP, 3, CgenSupport.SP, str);
            CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP, str);
            CgenSupport.emitLoad(CgenSupport.RA, 1, CgenSupport.SP, str);
            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP,
                    3 * CgenSupport.WORD_SIZE, str);
            CgenSupport.emitReturn(str);
        }
    }

    private void ClassMethods() {
        for (Enumeration e1 = nds.elements(); e1.hasMoreElements();) {
            CgenNode nd = (CgenNode) e1.nextElement();
            if (nd.basic()) {
                continue;
            }

            for (Enumeration e2 = nd.features.getElements(); e2
                    .hasMoreElements();) {
                Object ft = e2.nextElement();
                if (ft instanceof method) {
                    method m = (method) ft;
                    CgenSupport.emitMethodRef(nd.name, m.name, str);
                    str.print(CgenSupport.LABEL);

                    CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -3
                            * CgenSupport.WORD_SIZE, str);
                    CgenSupport.emitStore(CgenSupport.FP, 3, CgenSupport.SP,
                            str);
                    CgenSupport.emitStore(CgenSupport.SELF, 2, CgenSupport.SP,
                            str);
                    CgenSupport.emitStore(CgenSupport.RA, 1, CgenSupport.SP,
                            str);

                    CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP,
                            CgenSupport.WORD_SIZE * 3, str);
                    CgenSupport
                            .emitMove(CgenSupport.SELF, CgenSupport.ACC, str);

                    SymbolTable env = new SymbolTable();
                    env.enterScope();
                    for (int i = 0; i < m.formals.getLength(); i++) {
                        env.addId(((formal) m.formals.getNth(i)).name,
                                m.formals.getLength() - i);
                    }

                    m.expr.code(str, nd.name, this, env, 3);

                    CgenSupport
                            .emitLoad(CgenSupport.FP, 3, CgenSupport.SP, str);
                    CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP,
                            str);
                    CgenSupport
                            .emitLoad(CgenSupport.RA, 1, CgenSupport.SP, str);
                    CgenSupport
                            .emitAddiu(CgenSupport.SP, CgenSupport.SP,
                                    (3 + m.formals.getLength())
                                            * CgenSupport.WORD_SIZE, str);
                    CgenSupport.emitReturn(str);
                }
            }
        }
    }


    private void installBasicClasses() {
        AbstractSymbol filename = AbstractTable.stringtable
                .addString("<basic class>");


        addId(TreeConstants.No_class, new CgenNode(new class_(0, TreeConstants.No_class, TreeConstants.No_class,
                new Features(0), filename), CgenNode.Basic, this));

        addId(TreeConstants.SELF_TYPE, new CgenNode(new class_(0,
                TreeConstants.SELF_TYPE, TreeConstants.No_class,
                new Features(0), filename), CgenNode.Basic, this));

        addId(TreeConstants.prim_slot, new CgenNode(new class_(0,
                TreeConstants.prim_slot, TreeConstants.No_class,
                new Features(0), filename), CgenNode.Basic, this));


        /* la clase object no tiene parent, sus metodos son:
            cool_abort(), type_name -> string, copy() es para el selftype
        */

        class_ Object_class = new class_(
                0,
                TreeConstants.Object_,
                TreeConstants.No_class,
                new Features(0)
                        .appendElement(
                                new method(0, TreeConstants.cool_abort,
                                        new Formals(0), TreeConstants.Object_,
                                        new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.type_name,
                                        new Formals(0), TreeConstants.Str,
                                        new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.copy,
                                        new Formals(0),
                                        TreeConstants.SELF_TYPE, new no_expr(0))),
                filename);

        installClass(new CgenNode(Object_class, CgenNode.Basic, this));


        // in_int() : Int "   an int     " "     "
        /* los metodos de io son los de object, out_string(str), out_int(int) selftype y #
            in_string(): lee un string del output
            in_int(): Int"un entero"
        */

        class_ IO_class = new class_(
                0,
                TreeConstants.IO,
                TreeConstants.Object_,
                new Features(0)
                        .appendElement(
                                new method(0, TreeConstants.out_string,
                                        new Formals(0)
                                                .appendElement(new formal(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Str)),
                                        TreeConstants.SELF_TYPE, new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.out_int,
                                        new Formals(0)
                                                .appendElement(new formal(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Int)),
                                        TreeConstants.SELF_TYPE, new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.in_string,
                                        new Formals(0), TreeConstants.Str,
                                        new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.in_int,
                                        new Formals(0), TreeConstants.Int,
                                        new no_expr(0))), filename);

        installClass(new CgenNode(IO_class, CgenNode.Basic, this));

        //"val" = integer

        class_ Int_class = new class_(0, TreeConstants.Int,
                TreeConstants.Object_, new Features(0).appendElement(new attr(
                        0, TreeConstants.val, TreeConstants.prim_slot,
                        new no_expr(0))), filename);

        installClass(new CgenNode(Int_class, CgenNode.Basic, this));

        // Bool also has only the "val" slot.
        class_ Bool_class = new class_(0, TreeConstants.Bool,
                TreeConstants.Object_, new Features(0).appendElement(new attr(
                        0, TreeConstants.val, TreeConstants.prim_slot,
                        new no_expr(0))), filename);

        installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

        //
        // val = length
        // str_field el string
        // length() : int
        // concat() : 
        // substr():

        class_ Str_class = new class_(
                0,
                TreeConstants.Str,
                TreeConstants.Object_,
                new Features(0)
                        .appendElement(
                                new attr(0, TreeConstants.val,
                                        TreeConstants.Int, new no_expr(0)))
                        .appendElement(
                                new attr(0, TreeConstants.str_field,
                                        TreeConstants.prim_slot, new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.length,
                                        new Formals(0), TreeConstants.Int,
                                        new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.concat,
                                        new Formals(0)
                                                .appendElement(new formal(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Str)),
                                        TreeConstants.Str, new no_expr(0)))
                        .appendElement(
                                new method(
                                        0,
                                        TreeConstants.substr,
                                        new Formals(0)
                                                .appendElement(
                                                        new formal(
                                                                0,
                                                                TreeConstants.arg,
                                                                TreeConstants.Int))
                                                .appendElement(
                                                        new formal(
                                                                0,
                                                                TreeConstants.arg2,
                                                                TreeConstants.Int)),
                                        TreeConstants.Str, new no_expr(0))),
                filename);

        installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }

 


    private void installClasses(Classes cs) {
        for (Enumeration e = cs.getElements(); e.hasMoreElements();) {
            installClass(new CgenNode((Class_) e.nextElement(),
                    CgenNode.NotBasic, this));
        }
    }
    private void installClass(CgenNode nd) {
        AbstractSymbol name = nd.getName();
        if (probe(name) != null)
            return;
        nds.addElement(nd);
        addId(name, nd);
    }

    private void buildInheritanceTree() {
        for (Enumeration e = nds.elements(); e.hasMoreElements();) {
            setRelations((CgenNode) e.nextElement());
        }
    }

    private void setRelations(CgenNode nd) {
        CgenNode parent = (CgenNode) probe(nd.getParent());
        nd.setParentNd(parent);
        parent.addChild(nd);
    }

    /*invoka el code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
    this.str = str;
        nds = new Vector();
        labelCount = 0;
        atributos_y_offset = new HashMap<AbstractSymbol, HashMap<AbstractSymbol, Integer>>();
        metodos_y_offset = new HashMap<AbstractSymbol, HashMap<AbstractSymbol, Integer>>();

        enterScope();
        if (Flags.cgen_debug)
            System.out.println("Building CgenClassTable");

        installBasicClasses();
        installClasses(cls);
        buildInheritanceTree();
        sortNds_InitMap();

        stringclasstag = nds.indexOf(probe(TreeConstants.Str));
        intclasstag = nds.indexOf(probe(TreeConstants.Int));
        boolclasstag = nds.indexOf(probe(TreeConstants.Bool));

        code();

        exitScope();
    }

    /**
     * This method is the meat of the code generator. It is to be filled in
     * programming assignment 5
     */
    public void code() {
        if (Flags.cgen_debug)
            System.out.println("coding global data");
        codeGlobalData();

        if (Flags.cgen_debug)
            System.out.println("choosing gc");
        codeSelectGc();

        if (Flags.cgen_debug)
            System.out.println("coding constants");
        codeConstants();

        
        if (Flags.cgen_debug) {
            System.out.println("coding class_nameTab");
        }
        classNameT();

       
        if (Flags.cgen_debug) {
            System.out.println("coding class_objTab");
        }
        codeClassObjTab();

        if (Flags.cgen_debug) {
            System.out.println("coding dispatch tables");
        }
        dispatchTables();

      
        if (Flags.cgen_debug) {
            System.out.println("coding prototype objects");
        }
        protos();

        if (Flags.cgen_debug)
            System.out.println("coding global text");
        codeGlobalText();

        if (Flags.cgen_debug)
            System.out.println("coding object initializer");
        Initializer();


        if (Flags.cgen_debug)
            System.out.println("coding class methods");
        ClassMethods();
    }
    // Para obtener los parents de algun nodo
    // si reverse = true devuelve los padres desde el mas lejano al mas cercano
    // de lo contrario devuelve los padres desde el mas cercano al mas lejano
    public Vector<CgenNode> getParents(CgenNode nd, boolean reverse) {
        if(nd.name == TreeConstants.Object_) {
            return new Vector<CgenNode>();
        } else {
            Vector<CgenNode> parents = new Vector<CgenNode>();//creamos elvector
            CgenNode parent = (CgenNode) nd.getParentNd();//agarra el parent del node
            while(parent.name != TreeConstants.Object_) { //mientras el nombre del parent sea difertente de object
                if(reverse) //si reversa esta en true
                    parents.add(0, parent); //vector parents .add en el 0 agregamos el padre del node.
                else
                    parents.add(parent); //agregamos al object

                parent = parent.getParentNd(); //padre va a ser el parent deobject
            }
            if(reverse)
                parents.add(0, parent);
            else
                parents.add(parent);

            return parents;
        }
    }
       // use de sort similar al getparents.
    private void xxxiiijjj(CgenNode nd) {
        nds.add(nd); //agregamos el cgennodo
        int height = heightMap.get(nd.name); //height
        int min = nds.indexOf(nd); //minimo
        int max = min;

        for (Enumeration e = nd.getChildren(); e.hasMoreElements();) {
            CgenNode childNode = (CgenNode) e.nextElement();
            heightMap.put(childNode.name, height + 1);
            xxxiiijjj(childNode);
            max = mapofRange.get(childNode.name)[1];
        }
mapofRange.put(nd.name, new int[] { min, max });
    }


    /** Gets the root of the inheritance tree */
    public CgenNode root() {
        return (CgenNode) probe(TreeConstants.Object_);
    }

}