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

    NAME              =   2,

    
    //especializa√ß√µes dos de cima
    INT               =   3,
    FLOAT             =   4,
    EXP               =   5,
    HEX               =   6,
    NORMALSTRING      =   7,
    CHARSTRING        =   8,
    LONGSTRING        =   9,

    //operadores    
    MINUS             =  10,
    VELHA             =  11,
    PLUS              =  12,
    TIMES             =  13,
    RBAR              =  14,
    PERCENT           =  15,
    CHAPEU            =  16,
    EQUALS            =  17,
    BECOMES           =  18,
    NOTEQUALS         =  19,
    LESSOREQUALTHAN   =  20,
    LESSTHAN          =  21,
    MOREOREQUALTHAN   =  22,
    MORETHAN          =  23,
    LPAREN            =  24,
    RPAREN            =  25,
    LCURLY            =  26,
    RCURLY            =  27,
    LBRACKET          =  28,
    RBRACKET          =  29,
    DOT               =  30,
    COLON             =  31,
    SEMICOLON         =  32,
    COMMA             =  33,
    TWODOTES          =  34,
    THREEDOTES        =  35,
    OK                =  36,
    COMMENT           =  37,
    
    
    // reserved words - must be in alphabetical order...
    AND               =  38,
    BREAK             =  39,
    DO                =  40,
    ELSE              =  42,
    ELSEIF            =  43,
    END               =  43,
    FALSE             =  44,
    FOR               =  45,
    FUNCTION          =  46,
    IF                =  47,
    IN                =  48,
    LOCAL             =  49,
    NIL               =  50,
    NOT               =  51,
    OR                =  52,
    REPEAT            =  53,
    RETURN            =  54,
    THEN              =  55,
    TRUE              =  56,
    UNTIL             =  57,
    WHILE             =  58,
        
    // special tokens...
    EOT         =   38,
    ERROR       =   39;
    
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
        "=",
        "..",
        "...",
        
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

         "<error>"
     };      
}
