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


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int YYEOFER = 5;
	private final int YYCM = 1;
	private final int YYEST = 2;
	private final int YYSTRNULLERR = 4;
	private final int YYSTRNERR = 3;
	private final int yy_state_dtrans[] = {
		0,
		59,
		65,
		252,
		253,
		252
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NOT_ACCEPT,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NOT_ACCEPT,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NOT_ACCEPT,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NOT_ACCEPT,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NOT_ACCEPT,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NOT_ACCEPT,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NOT_ACCEPT,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NOT_ACCEPT,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NOT_ACCEPT,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NOT_ACCEPT,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NOT_ACCEPT,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NOT_ACCEPT,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NOT_ACCEPT,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NO_ANCHOR,
		/* 186 */ YY_NOT_ACCEPT,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NOT_ACCEPT,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NO_ANCHOR,
		/* 192 */ YY_NOT_ACCEPT,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NO_ANCHOR,
		/* 195 */ YY_NOT_ACCEPT,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NO_ANCHOR,
		/* 198 */ YY_NOT_ACCEPT,
		/* 199 */ YY_NO_ANCHOR,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NOT_ACCEPT,
		/* 202 */ YY_NO_ANCHOR,
		/* 203 */ YY_NO_ANCHOR,
		/* 204 */ YY_NOT_ACCEPT,
		/* 205 */ YY_NO_ANCHOR,
		/* 206 */ YY_NO_ANCHOR,
		/* 207 */ YY_NOT_ACCEPT,
		/* 208 */ YY_NO_ANCHOR,
		/* 209 */ YY_NO_ANCHOR,
		/* 210 */ YY_NOT_ACCEPT,
		/* 211 */ YY_NO_ANCHOR,
		/* 212 */ YY_NO_ANCHOR,
		/* 213 */ YY_NOT_ACCEPT,
		/* 214 */ YY_NO_ANCHOR,
		/* 215 */ YY_NO_ANCHOR,
		/* 216 */ YY_NOT_ACCEPT,
		/* 217 */ YY_NO_ANCHOR,
		/* 218 */ YY_NO_ANCHOR,
		/* 219 */ YY_NOT_ACCEPT,
		/* 220 */ YY_NO_ANCHOR,
		/* 221 */ YY_NO_ANCHOR,
		/* 222 */ YY_NOT_ACCEPT,
		/* 223 */ YY_NO_ANCHOR,
		/* 224 */ YY_NO_ANCHOR,
		/* 225 */ YY_NOT_ACCEPT,
		/* 226 */ YY_NO_ANCHOR,
		/* 227 */ YY_NO_ANCHOR,
		/* 228 */ YY_NOT_ACCEPT,
		/* 229 */ YY_NO_ANCHOR,
		/* 230 */ YY_NOT_ACCEPT,
		/* 231 */ YY_NO_ANCHOR,
		/* 232 */ YY_NOT_ACCEPT,
		/* 233 */ YY_NO_ANCHOR,
		/* 234 */ YY_NOT_ACCEPT,
		/* 235 */ YY_NO_ANCHOR,
		/* 236 */ YY_NOT_ACCEPT,
		/* 237 */ YY_NO_ANCHOR,
		/* 238 */ YY_NOT_ACCEPT,
		/* 239 */ YY_NO_ANCHOR,
		/* 240 */ YY_NOT_ACCEPT,
		/* 241 */ YY_NO_ANCHOR,
		/* 242 */ YY_NOT_ACCEPT,
		/* 243 */ YY_NO_ANCHOR,
		/* 244 */ YY_NOT_ACCEPT,
		/* 245 */ YY_NO_ANCHOR,
		/* 246 */ YY_NOT_ACCEPT,
		/* 247 */ YY_NO_ANCHOR,
		/* 248 */ YY_NOT_ACCEPT,
		/* 249 */ YY_NO_ANCHOR,
		/* 250 */ YY_NOT_ACCEPT,
		/* 251 */ YY_NOT_ACCEPT,
		/* 252 */ YY_NOT_ACCEPT,
		/* 253 */ YY_NOT_ACCEPT,
		/* 254 */ YY_NO_ANCHOR,
		/* 255 */ YY_NOT_ACCEPT,
		/* 256 */ YY_NO_ANCHOR,
		/* 257 */ YY_NOT_ACCEPT,
		/* 258 */ YY_NO_ANCHOR,
		/* 259 */ YY_NO_ANCHOR,
		/* 260 */ YY_NOT_ACCEPT,
		/* 261 */ YY_NO_ANCHOR,
		/* 262 */ YY_NO_ANCHOR,
		/* 263 */ YY_NO_ANCHOR,
		/* 264 */ YY_NO_ANCHOR,
		/* 265 */ YY_NOT_ACCEPT,
		/* 266 */ YY_NO_ANCHOR,
		/* 267 */ YY_NOT_ACCEPT,
		/* 268 */ YY_NOT_ACCEPT,
		/* 269 */ YY_NO_ANCHOR,
		/* 270 */ YY_NO_ANCHOR,
		/* 271 */ YY_NO_ANCHOR,
		/* 272 */ YY_NO_ANCHOR,
		/* 273 */ YY_NO_ANCHOR,
		/* 274 */ YY_NO_ANCHOR,
		/* 275 */ YY_NO_ANCHOR,
		/* 276 */ YY_NO_ANCHOR,
		/* 277 */ YY_NO_ANCHOR,
		/* 278 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"18,9:8,43,3,17,42,10,9:18,19,9,11,9:5,4,6,5,46,41,20,54,47,8:10,53,52,45,1," +
"2,9,51,38,56,27,57,58,23,56,59,60,56:2,61,56,29,62,63,56,64,65,32,66,33,67," +
"56:3,69,12,69,68,55,68,28,14,39,35,24,13,7,30,21,7:2,25,7,16,34,36,7,31,26," +
"15,40,44,37,7:3,49,22,50,48,9,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,279,
"0,1,2,3,1,4,5,1,6,7,1:5,8,9,1:3,6,10,1:15,6,11,6,12,6,1:7,13,14,1:3,15,1,16" +
",1:2,17,1:2,18,1:2,19,1:3,20,1:6,21,1:8,22,23,24,25,26,27,28,1,6:2,29,6:2,3" +
"0,1,31,6,32,33,6,1,34,6,35,36,37,38,39,40,32,1:2,41,32,42,32,43,14,1,6,44,3" +
"2,31,32:2,6,32,45,46,47,48,49,32,50,32,6,32,31,32,6:3,32,51,52,53,32:2,6,32" +
",54,55,56,32,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77" +
",78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101," +
"102,103,104,42,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119," +
"120,121,122,123,124,43,125,126,127,128,129,15,33,30,130,131,132,44,133,14,1" +
"34,31,135,136,137,16,138,34,139,140,141,142,143,144,145,146,147,148,149,150" +
",151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166")[0];

	private int yy_nxt[][] = unpackFromString(167,70,
"1,2,3,4,5,6,7,8,9,10,11,12,10,86,8,113,135,13,10,14,15,149,87,16,156,160,8," +
"88,8,254,8:2,266,114,163,8,166,169,273,172,8,17,18,19,20,21,22,23,24,25,26," +
"27,28,29,30,31,114:2,275,114,136,276,150,277,114:3,278,10,32,-1:72,33,-1:68" +
",34,-1:73,35,-1:70,36,-1:70,8:2,-1:3,8:5,-1:4,8,-1,8:18,-1:3,8,-1:10,8:15,-" +
"1:8,9,-1:81,38,-1:56,114:2,-1:3,114:5,-1:4,137,115,114:18,-1:3,114,-1:10,11" +
"4:5,137,114:9,-1,42,-1:18,43,-1:50,38:2,-1,38:66,-1:7,8:2,-1:3,8:5,-1:4,8,1" +
"92,8:7,214,8:10,-1:3,8,-1:10,8:4,214,8:10,-1:21,246,56,-1,55,-1,105,-1:4,26" +
"5,-1:26,55,-1,246,-1:3,265,105,-1:26,55,-1,55,-1:33,55,-1:33,54,-1,54,-1:33" +
",54,-1:32,250,106,-1:12,57,-1:21,57,-1:2,250,-1:9,1,60:2,61,109,132,60,108," +
"62,60:4,108:4,60:4,108,60,108:18,60:3,108,60:11,108:12,60:2,-1:8,62,-1:61,1" +
",66:2,67,111,133,66:5,68,69,66:4,70,71,66:51,-1:3,74,-1:7,75,76,77,78,79,80" +
",-1:65,110,-1:3,81,-1:75,183,-1,186,-1:15,189,-1:17,186,-1:7,189,-1:10,8:2," +
"-1:3,8:5,-1:4,37,89,8:5,175,8:9,175,8:2,-1:3,8,-1:10,8:5,37,8:9,-1:13,116,-" +
"1:2,91,-1:4,115,138,116,134,148,155,-1,159,91,162,-1:3,165,-1:3,159,-1:19,1" +
"34,162,115,148,165,-1:2,155,-1:11,114:2,-1:3,114:5,-1:4,114,171,114:2,157,1" +
"14:15,-1:3,114,-1:10,114:6,157,114:8,-1:22,257,-1:2,257,-1:35,257,-1:30,195" +
",-1:7,192,-1:2,260,-1:10,260,-1:14,192,-1:32,192,-1:7,192,-1:28,192,-1:26,4" +
"7,-1:5,49,-1,98,50,244,51,-1,47,-1:4,240,-1,52,-1:2,51,-1:18,98,-1:2,50,240" +
",52,-1,244,-1:26,265,-1:8,265,-1:32,265,-1:27,105,-1:3,105,-1:38,105,-1:11," +
"114:2,-1:3,114:5,-1:4,114,-1,114:18,-1:3,114,-1:10,114:15,-1:7,8:2,-1:3,8:5" +
",-1:4,8,54,8,103,8:16,-1:3,8,-1:10,8:3,103,8:11,-1:15,251,-1:6,251,-1:9,251" +
",-1:44,108,-1:5,108:4,-1:4,108,-1,108:18,-1:3,108,-1:11,108:12,-1:7,63,-1:7" +
"6,110,-1:62,72,-1:79,44,-1:6,45,-1:9,44,-1:4,45,-1:29,45,-1:9,8:2,-1:3,8:5," +
"-1:4,8,85,8:7,178,181,8:9,-1:3,8,-1:10,8:4,178,8:4,181,8:5,-1:7,114:2,-1:3," +
"114:5,-1:4,114,192,114:7,197,114:10,-1:3,114,-1:10,114:4,197,114:10,-1:22,5" +
"2,-1:2,99,-1:10,52,-1:24,99,-1,52,-1:28,52,-1:13,52,-1:26,52,-1:27,246:2,-1" +
":37,246,-1:15,64,-1:69,73,-1:78,46,-1:6,46,-1:9,46,-1:4,45,-1:29,45,-1:9,8:" +
"2,-1:3,8:5,-1:4,8,112,8,184,8:9,187,8:6,-1:3,8,-1:10,8:3,184,8:3,187,8:7,-1" +
":7,114:2,-1:3,114,151,114:2,117,-1:4,114,90,151,114:2,173,114:2,117,114:11," +
"-1:3,114,-1:10,114:10,173,114:4,-1:15,46,-1:5,198,95,-1,186,-1,201,-1,204,-" +
"1,192,-1,46,260,207,-1:2,45,204,-1:5,260,-1:13,186,192,198,-1,207,-1:2,201," +
"-1,45,-1:24,210,-1:3,201,-1,268,-1:9,268,-1:26,201,-1:11,8:2,-1:3,8,39,8:2," +
"40,-1:4,8,90,39,8:2,190,8:2,40,8:11,-1:3,8,-1:10,8:10,190,8:4,-1:7,114:2,-1" +
":3,114,118,114:3,-1:4,114,92,118,114:17,-1:3,114,-1:10,114:15,-1:22,213,-1:" +
"5,216,-1:4,260,-1:4,216,-1:5,260,-1:32,8:2,-1:3,8:5,-1:4,8,255,8:2,193,196," +
"8:14,-1:3,8,-1:10,8:6,193,8:3,196,8:4,-1:7,114:2,-1:3,114:5,-1:4,114,268,11" +
"4:5,270,114:9,270,114:2,-1:3,114,-1:10,114:15,-1:22,219,-1:3,219,-1:38,219," +
"-1:11,8:2,-1:3,8:5,-1:4,8,168,8,199,8:9,258,8:6,-1:3,8,-1:10,8:3,199,8:3,25" +
"8,8:7,-1:7,114:2,-1:3,114:5,-1:4,114,45,114:14,120,114:3,-1:3,114,-1:10,114" +
":12,120,114:2,-1:21,198,222,-1,186,-1:33,186,-1,198,-1:16,8:2,-1:3,8,41,8:3" +
",-1:4,8,92,41,8:17,-1:3,8,-1:10,8:15,-1:7,114:2,-1:3,114:3,139,114,-1:4,114" +
",44,114:9,139,114:8,-1:3,114,-1:10,114:15,-1:15,44,-1:6,119,-1:9,44,-1,207," +
"-1:27,207,-1:14,8:2,-1:3,8:5,-1:4,8,177,8:11,269,8:6,-1:3,8,-1:10,8:7,269,8" +
":7,-1:7,114:2,-1:3,114:5,-1:4,114,186,114,185,114:16,-1:3,114,-1:10,114:3,1" +
"85,114:11,-1:15,46,-1:6,121,-1:9,46,-1,228,-1:27,228,-1:14,8:2,-1:3,8:5,-1:" +
"4,8,180,8:7,202,8:10,-1:3,8,-1:10,8:4,202,8:10,-1:7,114:2,-1:3,114:5,-1:4,1" +
"14,219,114:3,188,114:14,-1:3,114,-1:10,114:10,188,114:4,-1:22,268,-1:5,268," +
"-1:9,268,-1:38,8:2,-1:3,8:5,-1:4,8,267,8:2,205,8:2,262,8:9,262,8:2,-1:3,8,-" +
"1:10,8:6,205,8:8,-1:7,114:2,-1:3,114:5,-1:4,114,260,114:10,271,114:7,-1:3,2" +
"71,-1:10,114:15,-1:22,186,-1,186,-1:33,186,-1:18,8:2,-1:3,8:5,-1:4,8,257,8:" +
"2,272,8:15,-1:3,8,-1:10,8:6,272,8:8,-1:7,114:2,-1:3,114:3,152,114,-1:4,114," +
"46,114:9,152,114:8,-1:3,114,-1:10,114:15,-1:22,230,-1:11,230,-1:27,230,-1:1" +
"4,8:2,-1:3,8:5,-1:4,8,186,8,208,8:16,-1:3,8,-1:10,8:3,208,8:11,-1:7,114:2,-" +
"1:3,114:5,-1:4,114,228,114:11,200,114:6,-1:3,114,-1:10,114:7,200,114:7,-1:2" +
"1,198:2,-1:37,198,-1:16,8:2,-1:3,8:5,-1:4,8,189,8:17,211,-1:3,8,-1:10,8:11," +
"211,8:3,-1:7,114:2,-1:3,114:5,-1:4,206,198,114:18,-1:3,114,-1:10,114:5,206," +
"114:9,-1:16,47,-1:5,47,-1,48,-1:4,47,-1:28,48,-1:18,8:2,-1:3,8:5,-1:4,8,45," +
"8:14,94,8:3,-1:3,8,-1:10,8:12,94,8:2,-1:7,114:2,-1:3,114:4,141,-1:4,114,47," +
"114:6,141,114:11,-1:3,114,-1:10,114:15,-1:16,47,-1:5,47,-1:6,47,-1:47,8:2,-" +
"1:3,8:3,93,8,-1:4,8,44,8:9,93,8:8,-1:3,8,-1:10,8:15,-1:7,114:2,-1:3,114:5,-" +
"1:4,114,53,114,102,114:16,-1:3,114,-1:10,114:3,102,114:11,-1:22,48,-1,48,-1" +
":33,48,-1:18,8:2,-1:3,8:5,-1:4,8,260,8:10,274,8:7,-1:3,274,-1:10,8:15,-1:7," +
"114:2,-1:3,114:5,-1:4,114,123,114,158,114:16,-1:3,114,-1:10,114:3,158,114:1" +
"1,-1:22,236,-1,236,-1:33,236,-1:18,8:2,-1:3,8:5,-1:4,8,201,8:3,217,8:14,-1:" +
"3,8,-1:10,8:10,217,8:4,-1:7,114:2,-1:3,114:5,-1:4,114,51,114:4,154,114:11,1" +
"54,114,-1:3,114,-1:10,114:15,-1:22,238,-1,236,-1:9,240,-1:23,236,-1:3,240,-" +
"1:14,8:2,-1:3,8:5,-1:4,8,216,8:5,220,8:9,220,8:2,-1:3,8,-1:10,8:15,-1:7,114" +
":2,-1:3,114:5,-1:4,114,236,114,212,114:16,-1:3,114,-1:10,114:3,212,114:11,-" +
"1:22,242,-1:2,242,-1:35,242,-1:15,8:2,-1:3,8:3,140,8,-1:4,8,46,8:9,140,8:8," +
"-1:3,8,-1:10,8:15,-1:7,114:2,-1:3,114:5,-1:4,114,52,114:13,126,114:4,-1:3,1" +
"14,-1:10,114:8,126,114:6,-1:22,123,-1,123,-1:33,123,-1:18,8:2,-1:3,8:5,-1:4" +
",229,198,8:18,-1:3,8,-1:10,8:5,229,8:9,-1:7,114:2,-1:3,114:5,-1:4,114,99,11" +
"4:2,143,114:15,-1:3,114,-1:10,114:6,143,114:8,-1:22,100,-1:3,244,51,-1:11,5" +
"1,-1:25,244,-1:11,8:2,-1:3,8:5,-1:4,8,268,8:5,231,8:9,231,8:2,-1:3,8,-1:10," +
"8:15,-1:7,114:2,-1:3,114:5,-1:4,114,242,114:2,218,114:15,-1:3,114,-1:10,114" +
":6,218,114:8,-1:7,8:2,-1:3,8:4,96,-1:4,8,47,8:6,96,8:11,-1:3,8,-1:10,8:15,-" +
"1:7,114:2,-1:3,114:5,-1:4,114,105,114:3,129,114:14,-1:3,114,-1:10,114:10,12" +
"9,114:4,-1:22,142,-1,123,-1,244,-1:31,123,-1:6,244,-1:11,8:2,-1:3,8:5,-1:4," +
"8,48,8,97,8:16,-1:3,8,-1:10,8:3,97,8:11,-1:7,114:2,-1:3,114:5,-1:4,114,265," +
"114:8,263,114:9,-1:3,114,-1:10,114:9,263,114:5,-1:22,125,-1:4,51,-1:6,240,-" +
"1:4,51,-1:22,240,-1:14,8:2,-1:3,8:5,-1:4,8,236,8,237,8:16,-1:3,8,-1:10,8:3," +
"237,8:11,-1:7,114:2,-1:3,114:5,-1:4,221,246,114:18,-1:3,114,-1:10,114:5,221" +
",114:9,-1:22,51,-1:4,51,-1:11,51,-1:37,8:2,-1:3,8:5,-1:4,8,123,8,153,8:16,-" +
"1:3,8,-1:10,8:3,153,8:11,-1:7,114:2,-1:3,114:5,-1:4,114,55,114,128,114:16,-" +
"1:3,114,-1:10,114:3,128,114:11,-1:22,53,-1,53,-1:33,53,-1:18,8:2,-1:3,8:5,-" +
"1:4,8,51,8:4,144,8:11,144,8,-1:3,8,-1:10,8:15,-1:7,114:2,-1:3,114:5,-1:4,11" +
"4,57,114:12,147,114:5,-1:3,114,-1:10,114:2,147,114:12,-1:16,47,-1:5,122,-1:" +
"2,242,-1:3,47,-1:31,242,-1:15,8:2,-1:3,8:5,-1:4,8,52,8:13,101,8:4,-1:3,8,-1" +
":10,8:8,101,8:6,-1:7,114:2,-1:3,114:3,227,114,-1:4,114,251,114:9,227,114:8," +
"-1:3,114,-1:10,114:15,-1:22,123,-1,123,-1:2,51,-1:11,51,-1:18,123,-1:18,8:2" +
",-1:3,8:5,-1:4,8,99,8:2,124,8:15,-1:3,8,-1:10,8:6,124,8:8,-1:7,114:2,-1:3,1" +
"14:5,-1:4,114,58,114:3,131,114:14,-1:3,114,-1:10,114:10,131,114:4,-1:7,8:2," +
"-1:3,8:5,-1:4,8,242,8:2,241,8:15,-1:3,8,-1:10,8:6,241,8:8,-1:22,99,-1:2,99," +
"-1:35,99,-1:15,8:2,-1:3,8:5,-1:4,8,244,8:3,243,8:14,-1:3,8,-1:10,8:10,243,8" +
":4,-1:22,127,-1,53,-1,244,-1:31,53,-1:6,244,-1:11,8:2,-1:3,8:5,-1:4,8,53,8," +
"145,8:16,-1:3,8,-1:10,8:3,145,8:11,-1:7,8:2,-1:3,8:5,-1:4,8,265,8:8,264,8:9" +
",-1:3,8,-1:10,8:9,264,8:5,-1:21,246,248,-1:8,265,-1:28,246,-1:3,265,-1:12,8" +
":2,-1:3,8:5,-1:4,245,246,8:18,-1:3,8,-1:10,8:5,245,8:9,-1:7,8:2,-1:3,8:5,-1" +
":4,8,55,8,104,8:16,-1:3,8,-1:10,8:3,104,8:11,-1:7,8:2,-1:3,8:5,-1:4,8,105,8" +
":3,146,8:14,-1:3,8,-1:10,8:10,146,8:4,-1:7,8:2,-1:3,8:5,-1:4,8,57,8:12,130," +
"8:5,-1:3,8,-1:10,8:2,130,8:12,-1:22,57,-1:12,57,-1:21,57,-1:19,8:2,-1:3,8:3" +
",249,8,-1:4,8,251,8:9,249,8:8,-1:3,8,-1:10,8:15,-1:7,8:2,-1:3,8:5,-1:4,8,58" +
",8:3,107,8:14,-1:3,8,-1:10,8:10,107,8:4,-1:22,58,-1:3,58,-1:38,58,-1:4,1,10" +
":9,-1,10:59,1,82:2,83,82:6,-1,84,82:58,-1:7,114:2,-1:3,114:5,-1:4,114,112,1" +
"14,161,114:9,164,114:6,-1:3,114,-1:10,114:3,161,114:3,164,114:7,-1:22,225,-" +
"1:3,201,-1,216,-1:9,216,-1:26,201,-1:11,114:2,-1:3,114:5,-1:4,114,216,114:5" +
",194,114:9,194,114:2,-1:3,114,-1:10,114:15,-1:22,234,-1:3,234,-1:38,234,-1:" +
"11,8:2,-1:3,8:5,-1:4,8,228,8:11,223,8:6,-1:3,8,-1:10,8:7,223,8:7,-1:7,114:2" +
",-1:3,114:5,-1:4,114,201,114:3,191,114:14,-1:3,114,-1:10,114:10,191,114:4,-" +
"1:22,240,-1:11,240,-1:27,240,-1:14,114:2,-1:3,114:5,-1:4,114,230,114:11,203" +
",114:6,-1:3,114,-1:10,114:7,203,114:7,-1:7,8:2,-1:3,8:5,-1:4,8,219,8:3,233," +
"8:14,-1:3,8,-1:10,8:10,233,8:4,-1:7,114:2,-1:3,114:5,-1:4,224,250,114:18,-1" +
":3,114,-1:10,114:5,224,114:9,-1:7,8:2,-1:3,8:5,-1:4,247,250,8:18,-1:3,8,-1:" +
"10,8:5,247,8:9,-1:21,250:2,-1:37,250,-1:16,114:2,-1:3,114:5,-1:4,114,174,11" +
"4:7,167,114:10,-1:3,114,-1:10,114:4,167,114:10,-1:22,232,-1:3,219,-1,268,-1" +
":9,268,-1:26,219,-1:26,244,-1:3,244,-1:38,244,-1:11,8:2,-1:3,8:5,-1:4,8,230" +
",8:11,226,8:6,-1:3,8,-1:10,8:7,226,8:7,-1:7,114:2,-1:3,114:5,-1:4,114,244,1" +
"14:3,209,114:14,-1:3,114,-1:10,114:10,209,114:4,-1:7,114:2,-1:3,114:5,-1:4," +
"114,240,114:11,215,114:6,-1:3,114,-1:10,114:7,215,114:7,-1:7,8:2,-1:3,8:5,-" +
"1:4,8,234,8:3,235,8:14,-1:3,8,-1:10,8:10,235,8:4,-1:7,114:2,-1:3,114:5,-1:4" +
",114,159,114:5,170,114:9,170,114:2,-1:3,114,-1:10,114:15,-1:7,8:2,-1:3,8:5," +
"-1:4,8,240,8:11,239,8:6,-1:3,8,-1:10,8:7,239,8:7,-1:7,114:2,-1:3,114:5,-1:4" +
",114,255,114:2,259,256,114:14,-1:3,114,-1:10,114:6,259,114:3,256,114:4,-1:7" +
",114:2,-1:3,114:5,-1:4,114,168,114,176,114:9,179,114:6,-1:3,114,-1:10,114:3" +
",176,114:3,179,114:7,-1:7,114:2,-1:3,114:5,-1:4,114,177,114:11,261,114:6,-1" +
":3,114,-1:10,114:7,261,114:7,-1:7,114:2,-1:3,114:5,-1:4,114,180,114:7,182,1" +
"14:10,-1:3,114,-1:10,114:4,182,114:10");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{return new Symbol(TokenConstants.EQ);}
					case -3:
						break;
					case 3:
						{return new Symbol(TokenConstants.ERROR);}
					case -4:
						break;
					case 4:
						{curr_lineno++;}
					case -5:
						break;
					case 5:
						{return new Symbol(TokenConstants.LPAREN);}
					case -6:
						break;
					case 6:
						{return new Symbol(TokenConstants.MULT);}
					case -7:
						break;
					case 7:
						{return new Symbol(TokenConstants.RPAREN);}
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.INT_CONST,
                                         new IntSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -10:
						break;
					case 10:
						{System.err.println("no hizo ningun match: " + yytext());}
					case -11:
						break;
					case 11:
						{;}
					case -12:
						break;
					case 12:
						{ yybegin(YYEST); }
					case -13:
						break;
					case 13:
						{;}
					case -14:
						break;
					case 14:
						{;}
					case -15:
						break;
					case 15:
						{return new Symbol(TokenConstants.MINUS);}
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -17:
						break;
					case 17:
						{return new Symbol(TokenConstants.COMMA);}
					case -18:
						break;
					case 18:
						{;}
					case -19:
						break;
					case 19:
						{;}
					case -20:
						break;
					case 20:
						{;}
					case -21:
						break;
					case 21:
						{return new Symbol(TokenConstants.LT);}
					case -22:
						break;
					case 22:
						{return new Symbol(TokenConstants.PLUS);}
					case -23:
						break;
					case 23:
						{return new Symbol(TokenConstants.DIV);}
					case -24:
						break;
					case 24:
						{return new Symbol(TokenConstants.NEG);}
					case -25:
						break;
					case 25:
						{return new Symbol(TokenConstants.LBRACE);}
					case -26:
						break;
					case 26:
						{return new Symbol(TokenConstants.RBRACE);}
					case -27:
						break;
					case 27:
						{return new Symbol(TokenConstants.AT);}
					case -28:
						break;
					case 28:
						{return new Symbol(TokenConstants.SEMI);}
					case -29:
						break;
					case 29:
						{return new Symbol(TokenConstants.COLON);}
					case -30:
						break;
					case 30:
						{return new Symbol(TokenConstants.DOT);}
					case -31:
						break;
					case 31:
						{return new Symbol(TokenConstants.ERROR, yytext());}
					case -32:
						break;
					case 32:
						{;}
					case -33:
						break;
					case 33:
						{return new Symbol(TokenConstants.DARROW); }
					case -34:
						break;
					case 34:
						{return new Symbol(TokenConstants.ERROR);}
					case -35:
						break;
					case 35:
						{ yybegin(YYCM); pro++; }
					case -36:
						break;
					case 36:
						{return new Symbol(TokenConstants.ERROR, "end of comment without start");}
					case -37:
						break;
					case 37:
						{return new Symbol(TokenConstants.FI);}
					case -38:
						break;
					case 38:
						{;}
					case -39:
						break;
					case 39:
						{return new Symbol(TokenConstants.IF);}
					case -40:
						break;
					case 40:
						{return new Symbol(TokenConstants.IN);}
					case -41:
						break;
					case 41:
						{return new Symbol(TokenConstants.OF);}
					case -42:
						break;
					case 42:
						{return new Symbol(TokenConstants.LE);}
					case -43:
						break;
					case 43:
						{return new Symbol(TokenConstants.ASSIGN);}
					case -44:
						break;
					case 44:
						{return new Symbol(TokenConstants.NOT);}
					case -45:
						break;
					case 45:
						{return new Symbol(TokenConstants.NEW);}
					case -46:
						break;
					case 46:
						{return new Symbol(TokenConstants.LET);}
					case -47:
						break;
					case 47:
						{return new Symbol(TokenConstants.THEN);}
					case -48:
						break;
					case 48:
						{return new Symbol(TokenConstants.BOOL_CONST,true);}
					case -49:
						break;
					case 49:
						{return new Symbol(TokenConstants.ELSE);}
					case -50:
						break;
					case 50:
						{return new Symbol(TokenConstants.POOL);}
					case -51:
						break;
					case 51:
						{return new Symbol(TokenConstants.ESAC);}
					case -52:
						break;
					case 52:
						{return new Symbol(TokenConstants.LOOP);}
					case -53:
						break;
					case 53:
						{return new Symbol(TokenConstants.CASE);}
					case -54:
						break;
					case 54:
						{return new Symbol(TokenConstants.BOOL_CONST,false);}
					case -55:
						break;
					case 55:
						{return new Symbol(TokenConstants.WHILE);}
					case -56:
						break;
					case 56:
						{return new Symbol(TokenConstants.CLASS);}
					case -57:
						break;
					case 57:
						{return new Symbol(TokenConstants.ISVOID);}
					case -58:
						break;
					case 58:
						{return new Symbol(TokenConstants.INHERITS);}
					case -59:
						break;
					case 59:
						{;}
					case -60:
						break;
					case 60:
						{string_buf.append(yytext());}
					case -61:
						break;
					case 61:
						{curr_lineno++;}
					case -62:
						break;
					case 62:
						{;}
					case -63:
						break;
					case 63:
						{pro++;}
					case -64:
						break;
					case 64:
						{pro--; if(pro == 0){yybegin(YYINITIAL);}}
					case -65:
						break;
					case 65:
						{string_buf.append(yytext());}
					case -66:
						break;
					case 66:
						{string_buf.append(yytext());}
					case -67:
						break;
					case 67:
						{string_buf.setLength(0);
                            yybegin(YYINITIAL);
                            return new Symbol(TokenConstants.ERROR, "String not defined..."); }
					case -68:
						break;
					case 68:
						{ yybegin(YYINITIAL);
                                      String xxzzxxzpape = string_buf.toString();
                                      if(xxzzxxzpape.length() >= 1025) {
                                            return new Symbol(TokenConstants.ERROR, "String too long, (s.length() <= 1024)....");
                                      } else {
                                            return new Symbol(TokenConstants.STR_CONST,
                                                new StringSymbol(xxzzxxzpape, xxzzxxzpape.length(), xxzzxxzpape.hashCode()));
                                      }
                                  }
					case -69:
						break;
					case 69:
						{;}
					case -70:
						break;
					case 70:
						{string_buf.append("\u000b");}
					case -71:
						break;
					case 71:
						{ yybegin(YYSTRNULLERR);
                                           return new Symbol(TokenConstants.ERROR, "String has an invalid character(s)."); }
					case -72:
						break;
					case 72:
						{string_buf.append(yytext());}
					case -73:
						break;
					case 73:
						{string_buf.append(yytext());}
					case -74:
						break;
					case 74:
						{ string_buf.append("\n"); }
					case -75:
						break;
					case 75:
						{ string_buf.append("\""); }
					case -76:
						break;
					case 76:
						{ string_buf.append("\\"); }
					case -77:
						break;
					case 77:
						{ string_buf.append("\f"); }
					case -78:
						break;
					case 78:
						{ string_buf.append("\b"); }
					case -79:
						break;
					case 79:
						{ string_buf.append("\t"); }
					case -80:
						break;
					case 80:
						{ string_buf.append("\n"); }
					case -81:
						break;
					case 81:
						{ string_buf.append("\\n"); }
					case -82:
						break;
					case 82:
						{ ; }
					case -83:
						break;
					case 83:
						{ yybegin(YYINITIAL); }
					case -84:
						break;
					case 84:
						{ yybegin(YYINITIAL); }
					case -85:
						break;
					case 86:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -86:
						break;
					case 87:
						{System.err.println("no hizo ningun match: " + yytext());}
					case -87:
						break;
					case 88:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -88:
						break;
					case 89:
						{return new Symbol(TokenConstants.FI);}
					case -89:
						break;
					case 90:
						{return new Symbol(TokenConstants.IF);}
					case -90:
						break;
					case 91:
						{return new Symbol(TokenConstants.IN);}
					case -91:
						break;
					case 92:
						{return new Symbol(TokenConstants.OF);}
					case -92:
						break;
					case 93:
						{return new Symbol(TokenConstants.NOT);}
					case -93:
						break;
					case 94:
						{return new Symbol(TokenConstants.NEW);}
					case -94:
						break;
					case 95:
						{return new Symbol(TokenConstants.LET);}
					case -95:
						break;
					case 96:
						{return new Symbol(TokenConstants.THEN);}
					case -96:
						break;
					case 97:
						{return new Symbol(TokenConstants.BOOL_CONST,true);}
					case -97:
						break;
					case 98:
						{return new Symbol(TokenConstants.ELSE);}
					case -98:
						break;
					case 99:
						{return new Symbol(TokenConstants.POOL);}
					case -99:
						break;
					case 100:
						{return new Symbol(TokenConstants.ESAC);}
					case -100:
						break;
					case 101:
						{return new Symbol(TokenConstants.LOOP);}
					case -101:
						break;
					case 102:
						{return new Symbol(TokenConstants.CASE);}
					case -102:
						break;
					case 103:
						{return new Symbol(TokenConstants.BOOL_CONST,false);}
					case -103:
						break;
					case 104:
						{return new Symbol(TokenConstants.WHILE);}
					case -104:
						break;
					case 105:
						{return new Symbol(TokenConstants.CLASS);}
					case -105:
						break;
					case 106:
						{return new Symbol(TokenConstants.ISVOID);}
					case -106:
						break;
					case 107:
						{return new Symbol(TokenConstants.INHERITS);}
					case -107:
						break;
					case 108:
						{;}
					case -108:
						break;
					case 109:
						{string_buf.append(yytext());}
					case -109:
						break;
					case 110:
						{string_buf.append(yytext());}
					case -110:
						break;
					case 111:
						{string_buf.append(yytext());}
					case -111:
						break;
					case 113:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -112:
						break;
					case 114:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -113:
						break;
					case 115:
						{return new Symbol(TokenConstants.FI);}
					case -114:
						break;
					case 116:
						{return new Symbol(TokenConstants.IF);}
					case -115:
						break;
					case 117:
						{return new Symbol(TokenConstants.IN);}
					case -116:
						break;
					case 118:
						{return new Symbol(TokenConstants.OF);}
					case -117:
						break;
					case 119:
						{return new Symbol(TokenConstants.NOT);}
					case -118:
						break;
					case 120:
						{return new Symbol(TokenConstants.NEW);}
					case -119:
						break;
					case 121:
						{return new Symbol(TokenConstants.LET);}
					case -120:
						break;
					case 122:
						{return new Symbol(TokenConstants.THEN);}
					case -121:
						break;
					case 123:
						{return new Symbol(TokenConstants.ELSE);}
					case -122:
						break;
					case 124:
						{return new Symbol(TokenConstants.POOL);}
					case -123:
						break;
					case 125:
						{return new Symbol(TokenConstants.ESAC);}
					case -124:
						break;
					case 126:
						{return new Symbol(TokenConstants.LOOP);}
					case -125:
						break;
					case 127:
						{return new Symbol(TokenConstants.CASE);}
					case -126:
						break;
					case 128:
						{return new Symbol(TokenConstants.WHILE);}
					case -127:
						break;
					case 129:
						{return new Symbol(TokenConstants.CLASS);}
					case -128:
						break;
					case 130:
						{return new Symbol(TokenConstants.ISVOID);}
					case -129:
						break;
					case 131:
						{return new Symbol(TokenConstants.INHERITS);}
					case -130:
						break;
					case 132:
						{string_buf.append(yytext());}
					case -131:
						break;
					case 133:
						{string_buf.append(yytext());}
					case -132:
						break;
					case 135:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -133:
						break;
					case 136:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -134:
						break;
					case 137:
						{return new Symbol(TokenConstants.FI);}
					case -135:
						break;
					case 138:
						{return new Symbol(TokenConstants.IF);}
					case -136:
						break;
					case 139:
						{return new Symbol(TokenConstants.NOT);}
					case -137:
						break;
					case 140:
						{return new Symbol(TokenConstants.LET);}
					case -138:
						break;
					case 141:
						{return new Symbol(TokenConstants.THEN);}
					case -139:
						break;
					case 142:
						{return new Symbol(TokenConstants.ELSE);}
					case -140:
						break;
					case 143:
						{return new Symbol(TokenConstants.POOL);}
					case -141:
						break;
					case 144:
						{return new Symbol(TokenConstants.ESAC);}
					case -142:
						break;
					case 145:
						{return new Symbol(TokenConstants.CASE);}
					case -143:
						break;
					case 146:
						{return new Symbol(TokenConstants.CLASS);}
					case -144:
						break;
					case 147:
						{return new Symbol(TokenConstants.ISVOID);}
					case -145:
						break;
					case 149:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -146:
						break;
					case 150:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -147:
						break;
					case 151:
						{return new Symbol(TokenConstants.IF);}
					case -148:
						break;
					case 152:
						{return new Symbol(TokenConstants.LET);}
					case -149:
						break;
					case 153:
						{return new Symbol(TokenConstants.ELSE);}
					case -150:
						break;
					case 154:
						{return new Symbol(TokenConstants.ESAC);}
					case -151:
						break;
					case 156:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -152:
						break;
					case 157:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -153:
						break;
					case 158:
						{return new Symbol(TokenConstants.ELSE);}
					case -154:
						break;
					case 160:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -155:
						break;
					case 161:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -156:
						break;
					case 163:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -157:
						break;
					case 164:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -158:
						break;
					case 166:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -159:
						break;
					case 167:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -160:
						break;
					case 169:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -161:
						break;
					case 170:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -162:
						break;
					case 172:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -163:
						break;
					case 173:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -164:
						break;
					case 175:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -165:
						break;
					case 176:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -166:
						break;
					case 178:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -167:
						break;
					case 179:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -168:
						break;
					case 181:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -169:
						break;
					case 182:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -170:
						break;
					case 184:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -171:
						break;
					case 185:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -172:
						break;
					case 187:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -173:
						break;
					case 188:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -174:
						break;
					case 190:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -175:
						break;
					case 191:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -176:
						break;
					case 193:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -177:
						break;
					case 194:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -178:
						break;
					case 196:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -179:
						break;
					case 197:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -180:
						break;
					case 199:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -181:
						break;
					case 200:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -182:
						break;
					case 202:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -183:
						break;
					case 203:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -184:
						break;
					case 205:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -185:
						break;
					case 206:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -186:
						break;
					case 208:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -187:
						break;
					case 209:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -188:
						break;
					case 211:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -189:
						break;
					case 212:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -190:
						break;
					case 214:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -191:
						break;
					case 215:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -192:
						break;
					case 217:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -193:
						break;
					case 218:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -194:
						break;
					case 220:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -195:
						break;
					case 221:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -196:
						break;
					case 223:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -197:
						break;
					case 224:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -198:
						break;
					case 226:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -199:
						break;
					case 227:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -200:
						break;
					case 229:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -201:
						break;
					case 231:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -202:
						break;
					case 233:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -203:
						break;
					case 235:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -204:
						break;
					case 237:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -205:
						break;
					case 239:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -206:
						break;
					case 241:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -207:
						break;
					case 243:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -208:
						break;
					case 245:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -209:
						break;
					case 247:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -210:
						break;
					case 249:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -211:
						break;
					case 254:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -212:
						break;
					case 256:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -213:
						break;
					case 258:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -214:
						break;
					case 259:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -215:
						break;
					case 261:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -216:
						break;
					case 262:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -217:
						break;
					case 263:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -218:
						break;
					case 264:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -219:
						break;
					case 266:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -220:
						break;
					case 269:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -221:
						break;
					case 270:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -222:
						break;
					case 271:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -223:
						break;
					case 272:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -224:
						break;
					case 273:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -225:
						break;
					case 274:
						{ return new Symbol(TokenConstants.OBJECTID,
                                           new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -226:
						break;
					case 275:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -227:
						break;
					case 276:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -228:
						break;
					case 277:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -229:
						break;
					case 278:
						{ return new Symbol(TokenConstants.TYPEID,
                        new IdSymbol(yytext(), yytext().length(), yytext().hashCode())); }
					case -230:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
