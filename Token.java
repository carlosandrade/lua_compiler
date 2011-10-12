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
    
    //literals, identifiers, operators..
    INTLITERAL  =   0,
    CHARLITERAL =   1,
    NAME        =   2,
    OPERATOR    =   3,
    
    MINUS //-
    VELHA //#
    
    // reserved words - must be in alphabetical order...
    AND         =   4,
    BREAK       =   5,
    DO          =   6,
    ELSE        =   7,
    ELSEIF      =   8,
    END         =   9,
    FALSE       =   10,
    FOR         =   11,
    FUNCTION    =   12,
    IF          =   13,
    IN          =   14,
    LOCAL       =   15,
    NIL         =   16,
    NOT         =   17,
    OR          =   18,
    REPEAT      =   19,
    RETURN      =   20,
    THEN        =   21,
    TRUE        =   22,
    UNTIL       =   23,
    WHILE       =   24,
    
    // punctuation...
    DOT         =   25,
    COLON       =   26,
    SEMICOLON   =   27,
    COMMA       =   28,
    BECOMES     =   29,
    TWODOTES    =   30,
    THREEDOTES  =   31,
    
    // brackets...
    LPAREN      =   32,
    RPAREN      =   33,
    LBRACKET    =   34,
    RBRACKET    =   35,
    LCURLY      =   36,   
    RCURLY      =   37,
    
    // special tokens...
    EOT         =   38,
    ERROR       =   39;
    NORMALSTRING = 40;
    CHARSTRING = 41;
    
    
    // Spellings of different kinds of 
    //token (must correspond to the token kinds above):
    
    private final static String[] spellings = {
        "<int>",
        "<char>",
        "<name>",
        "<operator>",
        
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
        
        ".",
        ":",
        ";",
        ",",
        "=",
        "..",
        "...",
        
        "(",
         ")",
         "[",
         "]",
         "{",
         "}",
         
         "",

         "<error>"
     };      
}