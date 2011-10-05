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
    
    

*/

class Token extends Object{
    public byte kind;
    public String spelling;
    
    public Token (byte kind, String spelling)
    {
        this.kind = kind;
        this.spelling = spelling;
        //If kind is NAME(IDENTIFIER) and spelling matches one of the keywords,
        //change the token's kind accordingly:
    }
}