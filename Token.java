class Token extends Object{
    public int kind;
    public String spelling;
    public SourcePosition position;
     
    public Token (int kind, String spelling, SourcePosition position)
    {
        this.kind = kind;
        this.spelling = spelling;
        this.position = position;
        //If kind is NAME(IDENTIFIER) and spelling matches one of the keywords,
        //change the token's kind accordingly:


        if(kind == NAME)
        {
            for(int k = AND ; k <= WHILE ; k++){
                if (spelling.equals(spellings[k]))
                {
                    this.kind = k;
                    break;
                }
            }
        }
    }
    //Constants dnoting different kinds of token:
    public final static int 
    //name
    NAME              =   0,
  
    //especializa√ß√µes dos de cima
    INT               =   1,
    FLOAT             =   2,
    EXP               =   3,
    HEX               =   4,
    NORMALSTRING      =   5,
    CHARSTRING        =   6,
    LONGSTRING        =   7,

    //operadores    
    MINUS             =  8,
    VELHA             =  9,
    PLUS              =  10,
    TIMES             =  11,
    RBAR              =  12,
    PERCENT           =  13,
    CHAPEU            =  14,
    EQUALS            =  15,
    BECOMES           =  16,
    NOTEQUALS         =  17,
    LESSOREQUALTHAN   =  18,
    LESSTHAN          =  19,
    MOREOREQUALTHAN   =  20,
    MORETHAN          =  21,
    LPAREN            =  22,
    RPAREN            =  23,
    LCURLY            =  24,
    RCURLY            =  25,
    LBRACKET          =  26,
    RBRACKET          =  27,
    DOT               =  28,
    COLON             =  29,
    SEMICOLON         =  30,
    COMMA             =  31,
    TWODOTES          =  32,
    THREEDOTES        =  33,
    OK                =  34,
    
    
    // reserved words - must be in alphabetical order...
    AND               =  35,
    BREAK             =  36,
    DO                =  37,
    ELSE              =  38,
    ELSEIF            =  39,
    END               =  40,
    FALSE             =  41,
    FOR               =  42,
    FUNCTION          =  43,
    IF                =  44,
    IN                =  45,
    LOCAL             =  46,
    NIL               =  47,
    NOT               =  48,
    OR                =  49,
    REPEAT            =  50,
    RETURN            =  51,
    THEN              =  52,
    TRUE              =  53,
    UNTIL             =  54,
    WHILE             =  55,
    
    // special tokens...
    EOT         =   56,
    ERROR       =   57;
    
    //
    
    // Spellings of different kinds of 
    //token (must correspond to the token kinds above):
    
    public final static String[] spellings = { //depois mudar esse metodo para private
        "<name>",
        "<int>",
        "<float>",
        "<exp>",
        "<hex>",
        "<normalstring>",
        "<charstring>",
        "<longstring>",

        "-",
        "#",
        "+",
        "*",
        "/",
        "%",
        "^",
        "==",
        "=",
        "~=",
        "<=",
        "<",
        ">=",
        ">",
        "(",
        ")",  
        "{",
        "}", 
        "[",
        "]", 
        ".",
        ":",
        ";",
        ",",
        "..",
        "...",
        "ok",
        
        
        "and",
        "break",
        "do",
        "else",
        "elseif",
        "end",
        "false",
        "for",
        "function",
        "if",
        "in",
        "local",
        "nil",
        "not",
        "or",
        "repeat",
        "return",
        "then",
        "true",
        "until",
        "while",
        "",
         "<error>"
     }; 
     
     public String toString() {
       return " Kind=" + spellings[kind] + "\t spelling=" + spelling +
         "\t position=" + position+"\n";
     }        
}