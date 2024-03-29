/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.
Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.
IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */

// This is a project skeleton file

import java.io.PrintStream;

/**
 * This class aggregates all kinds of support routines and constants for the
 * code generator; all routines are statics, so no instance of this class is
 * even created.
 */
class CgenSupport {
    /** Runtime constants for controlling the garbage collector. */
    final static String[] gcInitNames = { "_NoGC_Init", "_GenGC_Init",
            "_ScnGC_Init" };

    /** Runtime constants for controlling the garbage collector. */
    final static String[] gcCollectNames = { "_NoGC_Collect", "_GenGC_Collect",
            "_ScnGC_Collect" };

    final static int MAXINT = 100000000;
    final static int WORD_SIZE = 4;
    final static int LOG_WORD_SIZE = 2; // for logical shifts

    // Global names
    final static String CLASSNAMETAB = "class_nameTab";
    final static String CLASSOBJTAB = "class_objTab";
    final static String INTTAG = "_int_tag";
    final static String BOOLTAG = "_bool_tag";
    final static String STRINGTAG = "_string_tag";
    final static String HEAP_START = "heap_start";

    // Naming conventions
    final static String DISPTAB_SUFFIX = "_dispTab";
    final static String METHOD_SEP = ".";
    final static String CLASSINIT_SUFFIX = "_init";
    final static String PROTOBJ_SUFFIX = "_protObj";
    final static String OBJECTPROTOBJ = "Object" + PROTOBJ_SUFFIX;
    final static String INTCONST_PREFIX = "int_const";
    final static String STRCONST_PREFIX = "str_const";
    final static String BOOLCONST_PREFIX = "bool_const";

    final static int EMPTYSLOT = 0;
    final static String LABEL = ":\n";

    // information about object headers
    final static int DEFAULT_OBJFIELDS = 3;
    final static int TAG_OFFSET = 0;
    final static int SIZE_OFFSET = 1;
    final static int DISPTABLE_OFFSET = 2;

    final static int STRING_SLOTS = 1;
    final static int INT_SLOTS = 1;
    final static int BOOL_SLOTS = 1;

    final static String GLOBAL = "\t.globl\t";
    final static String ALIGN = "\t.align\t2\n";
    final static String WORD = "\t.word\t";

    // register names,
    final static String ZERO = "$zero"; // Zero register
    final static String ACC = "$a0"; // Accumulator
    final static String A1 = "$a1"; // For arguments to prim funcs
    final static String SELF = "$s0"; // Ptr to self (callee saves)
    final static String T1 = "$t1"; // Temporary 1
    final static String T2 = "$t2"; // Temporary 2
    final static String T3 = "$t3"; // Temporary 3
    final static String SP = "$sp"; // Stack pointer
    final static String FP = "$fp"; // Frame pointer
    final static String RA = "$ra"; // Return address

    // Opcodes
    final static String JALR = "\tjalr\t";
    final static String JAL = "\tjal\t";
    final static String RET = "\tjr\t" + RA;

    final static String SW = "\tsw\t";
    final static String LW = "\tlw\t";
    final static String LI = "\tli\t";
    final static String LA = "\tla\t";

    final static String MOVE = "\tmove\t";
    final static String NEG = "\tneg\t";
    final static String ADD = "\tadd\t";
    final static String ADDI = "\taddi\t";
    final static String ADDU = "\taddu\t";
    final static String ADDIU = "\taddiu\t";
    final static String DIV = "\tdiv\t";
    final static String MUL = "\tmul\t";
    final static String SUB = "\tsub\t";
    final static String SLL = "\tsll\t";
    final static String BEQZ = "\tbeqz\t";
    final static String BRANCH = "\tb\t";
    final static String BEQ = "\tbeq\t";
    final static String BNE = "\tbne\t";
    final static String BLEQ = "\tble\t";
    final static String BLT = "\tblt\t";
    final static String BGT = "\tbgt\t";

 
    static void emitLoad(String dest_reg, int offset, String source_reg,
            PrintStream s) {
        s.println(LW + dest_reg + " " + offset * WORD_SIZE + "(" + source_reg
                + ")");
    }

  
    static void emitStore(String source_reg, int offset, String dest_reg,
            PrintStream s) {
        s.println(SW + source_reg + " " + offset * WORD_SIZE + "(" + dest_reg
                + ")");
    }

   
    static void emitLoadImm(String dest_reg, int val, PrintStream s) {
        s.println(LI + dest_reg + " " + val);
    }

   
    static void emitLoadAddress(String dest_reg, String address, PrintStream s) {
        s.println(LA + dest_reg + " " + address);
    }


    static void emitPartialLoadAddress(String dest_reg, PrintStream s) {
        s.print(LA + dest_reg + " ");
    }


    static void emitLoadBool(String dest_reg, BoolConst b, PrintStream s) {
        emitPartialLoadAddress(dest_reg, s);
        b.codeRef(s);
        s.println("");
    }


    static void emitLoadString(String dest_reg, StringSymbol str, PrintStream s) {
        emitPartialLoadAddress(dest_reg, s);
        str.codeRef(s);
        s.println("");
    }

    static void emitLoadInt(String dest_reg, IntSymbol i, PrintStream s) {
        emitPartialLoadAddress(dest_reg, s);
        i.codeRef(s);
        s.println("");
    }


    static void emitMove(String dest_reg, String source_reg, PrintStream s) {
        s.println(MOVE + dest_reg + " " + source_reg);
    }


    static void emitNeg(String dest_reg, String source_reg, PrintStream s) {
        s.println(NEG + dest_reg + " " + source_reg);
    }

   
    static void emitAdd(String dest_reg, String src1, String src2, PrintStream s) {
        s.println(ADD + dest_reg + " " + src1 + " " + src2);
    }

   
    static void emitAddu(String dest_reg, String src1, String src2,
            PrintStream s) {
        s.println(ADDU + dest_reg + " " + src1 + " " + src2);
    }


    static void emitAddiu(String dest_reg, String src, int imm, PrintStream s) {
        s.println(ADDIU + dest_reg + " " + src + " " + imm);
    }


    static void emitDiv(String dest_reg, String src1, String src2, PrintStream s) {
        s.println(DIV + dest_reg + " " + src1 + " " + src2);
    }

    static void emitMul(String dest_reg, String src1, String src2, PrintStream s) {
        s.println(MUL + dest_reg + " " + src1 + " " + src2);
    }

 
    static void emitSub(String dest_reg, String src1, String src2, PrintStream s) {
        s.println(SUB + dest_reg + " " + src1 + " " + src2);
    }

    
    static void emitSll(String dest_reg, String src1, int num, PrintStream s) {
        s.println(SLL + dest_reg + " " + src1 + " " + num);
    }


    static void emitJalr(String dest_reg, PrintStream s) {
        s.println(JALR + dest_reg);
    }

    static void emitJal(String dest, PrintStream s) {
        s.println(JAL + dest);
    }

    static void emitReturn(PrintStream s) {
        s.println(RET);
    }

    static void emitGCAssign(PrintStream s) {
        s.println(JAL + "_GenGC_Assign");
    }

 
    static void emitDispTableRef(AbstractSymbol sym, PrintStream s) {
        s.print(sym + DISPTAB_SUFFIX);
    }


    static void emitInitRef(AbstractSymbol sym, PrintStream s) {
        s.print(sym + CLASSINIT_SUFFIX);
    }

  
    static void emitProtObjRef(AbstractSymbol sym, PrintStream s) {
        s.print(sym + PROTOBJ_SUFFIX);
    }

    static void emitMethodRef(AbstractSymbol classname,
            AbstractSymbol methodname, PrintStream s) {
        s.print(classname + METHOD_SEP + methodname);
    }

    static void emitLabelRef(int label, PrintStream s) {
        s.print("label" + label);
    }


    static void emitLabelDef(int label, PrintStream s) {
        emitLabelRef(label, s);
        s.println(":");
    }


    static void emitBeqz(String src, int label, PrintStream s) {
        s.print(BEQZ + src + " ");
        emitLabelRef(label, s);
        s.println("");
    }

   
    static void emitBeq(String src1, String src2, int label, PrintStream s) {
        s.print(BEQ + src1 + " " + src2 + " ");
        emitLabelRef(label, s);
        s.println("");
    }

   
    static void emitBne(String src1, String src2, int label, PrintStream s) {
        s.print(BNE + src1 + " " + src2 + " ");
        emitLabelRef(label, s);
        s.println("");
    }


    static void emitBleq(String src1, String src2, int label, PrintStream s) {
        s.print(BLEQ + src1 + " " + src2 + " ");
        emitLabelRef(label, s);
        s.println("");
    }


    static void emitBlt(String src1, String src2, int label, PrintStream s) {
        s.print(BLT + src1 + " " + src2 + " ");
        emitLabelRef(label, s);
        s.println("");
    }

    
    static void emitBlti(String src, int imm, int label, PrintStream s) {
        s.print(BLT + src + " " + imm + " ");
        emitLabelRef(label, s);
        s.println("");
    }

  
    static void emitBgti(String src, int imm, int label, PrintStream s) {
        s.print(BGT + src + " " + imm + " ");
        emitLabelRef(label, s);
        s.println("");
    }

   
    static void emitBranch(int label, PrintStream s) {
        s.print(BRANCH);
        emitLabelRef(label, s);
        s.println("");
    }


    static void emitPush(String reg, PrintStream s) {
        emitStore(reg, 0, SP, s);
        emitAddiu(SP, SP, -WORD_SIZE, s);
    }


    static void emitPop(String reg, PrintStream s) {
        
        emitLoad(reg, 1, SP, s);
        emitAddiu(SP, SP, WORD_SIZE, s);
    }

    static void emitFetchInt(String dest, String source, PrintStream s) {
        emitLoad(dest, DEFAULT_OBJFIELDS, source, s);
    }

   
    static void emitStoreInt(String source, String dest, PrintStream s) {
        emitStore(source, DEFAULT_OBJFIELDS, dest, s);
    }

   
    static void emitTestCollector(PrintStream s) {
        emitPush(ACC, s);
        emitMove(ACC, SP, s);
        emitMove(A1, ZERO, s);
        s.println(JAL + gcCollectNames[Flags.cgen_Memmgr]);
        emitAddiu(SP, SP, 4, s);
        emitLoad(ACC, 0, SP, s);
    }


    static void emitGCCheck(String source, PrintStream s) {
        if (source != A1)
            emitMove(A1, source, s);
        s.println(JAL + "_gc_check");
    }

    private static boolean ascii = false;
    static void asciiMode(PrintStream s) {
        if (!ascii) {
            s.print("\t.ascii\t\"");
            ascii = true;
        }
    }


    static void byteMode(PrintStream s) {
        if (ascii) {
            s.println("\"");
            ascii = false;
        }
    }


    static void emitStringConstant(String str, PrintStream s) {
        ascii = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            switch (c) {
            case '\n':
                asciiMode(s);
                s.print("\\n");
                break;
            case '\t':
                asciiMode(s);
                s.print("\\t");
                break;
            case '\\':
                byteMode(s);
                s.println("\t.byte\t" + (byte) '\\');
                break;
            case '"':
                asciiMode(s);
                s.print("\\\"");
                break;
            default:
                if (c >= 0x20 && c <= 0x7f) {
                    asciiMode(s);
                    s.print(c);
                } else {
                    byteMode(s);
                    s.println("\t.byte\t" + (byte) c);
                }
            }
        }
        byteMode(s);
        s.println("\t.byte\t0\t");
    }
}