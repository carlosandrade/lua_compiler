/* ver pag 119 do livro e secao 2.1 do manual de lua para mais detalhes
    No manual, a producao de Name e descrita em linguagem natural. 
    Lembrar principalmente que nao se comeca por digito, e que Lua
    permite o uso de underscore, o que nao e definido na producao
    do livro, inclusive da a entender que pode se comecar o identificador
    por underscore (ou pelo menos nada fala contra tal uso como fala para
    digito).
    
    Nada e mencionado sobre uma variavel ser chamada, portanto de score.
    
    Uma vez que as regras de Underscore sao as mesmas que para um digito,
    tal simbolo foi adicionado na producao Letter.
    
    Name::= Letter | Name Letter | Name Digit
    Letter::= a|b|c|...|z|_
    Digit::= 0|1|2|...|9
    Token::=
        +   |  -  |   *  |   /  |   %  |   ^   |  # |
        == |   ~=  |  <=  |  >=  |  <   |  >  |   = |
        (     )     {     }     [     ]
        ;  |   :   |  ,   |  .   |  .. |   ... |
        
    LiteralStrings::='String'|"String"|
                    [[LiteralStringsBrackets]]
    LiteralStringsBrackets::=  =LiteralStringsBrackets=|String
    String::= qualquer simbolo
    
        
    Name|Integer-Literal|Operator| Preciso confirmar se essa ultima linha 
    procede 
        
        
    Keywords sao definidas tambem por Name, conforme sugerido pelo livro.
    Ver pag 120. Em particular, elas sao definidas explicitamente nesta classe.
    Operators::=
    
    A secao de quebra de linha e definida diretamente no codigo tambem porem
    na classe Scanner.Java
    
    
    Duvida:
    
    Deve-se aplicar restricao para _VERSION e variaveis reservadas para
    linguagem?
    
    ((((((((a\)\)\)\)\)\)\))
    [[[[[[[a\]\]\]\]\]\]\]
    
    [( \[ )* a (  \] )* ]
    [ [ [ ... a ]
    
    "(\")* a (\")*"

"" ""

*/

class Token extends Object{
    public int kind;
    public String spelling;
     
    public Token (int kind, String spelling)
    {
        this.kind = kind;
        this.spelling = spelling;
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
    EXP               =   5,
    HEX               =   3,
    NORMALSTRING      =   4,
    CHARSTRING        =   5,
    LONGSTRING        =   6,

    //operadores    
    MINUS             =  7,
    VELHA             =  8,
    PLUS              =  9,
    TIMES             =  10,
    RBAR              =  11,
    PERCENT           =  12,
    CHAPEU            =  13,
    EQUALS            =  14,
    BECOMES           =  15,
    NOTEQUALS         =  16,
    LESSOREQUALTHAN   =  17,
    LESSTHAN          =  18,
    MOREOREQUALTHAN   =  19,
    MORETHAN          =  20,
    LPAREN            =  21,
    RPAREN            =  22,
    LCURLY            =  23,
    RCURLY            =  24,
    LBRACKET          =  25,
    RBRACKET          =  26,
    DOT               =  27,
    COLON             =  28,
    SEMICOLON         =  29,
    COMMA             =  30,
    TWODOTES          =  31,
    THREEDOTES        =  32,
    OK                =  33,
    
    
    // reserved words - must be in alphabetical order...
    AND               =  34,
    BREAK             =  35,
    DO                =  36,
    ELSE              =  37,
    ELSEIF            =  38,
    END               =  39,
    FALSE             =  40,
    FOR               =  41,
    FUNCTION          =  42,
    IF                =  43,
    IN                =  44,
    LOCAL             =  45,
    NIL               =  46,
    NOT               =  47,
    OR                =  48,
    REPEAT            =  49,
    RETURN            =  50,
    THEN              =  51,
    TRUE              =  52,
    UNTIL             =  53,
    WHILE             =  54,
        
    // special tokens...
    EOT         =   56,
    ERROR       =   57;
    
    //
    
    // Spellings of different kinds of 
    //token (must correspond to the token kinds above):
    
    private final static String[] spellings = {
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
}
