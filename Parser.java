/*
Verifica se um dado token pertence aos starters de um não terminal
*/

//--> s(explist) = s(exp) = {nil, false, true, Number, String, '...', function, Name, '(', '{', '-', not, '#'}
private boolean isExplistStarter(Token currentToken) {return isExpStarter()}


//--> s(exp) = {nil, false, true, Number, String, '...', function, Name, '(', '{', '-', not, '#'}
private boolean isExpStarter(Token currentToken)
{
    if((currentToken.kind == Token.NIL) || (currentToken.kind == Token.FALSE)
    || (currentToken.kind == Token.TRUE) || (currentToken.kind == Token.NUMBER)
    || (currentToken.kind == Token.STRING) ||(currentToken.kind == Token.THREEDOTES)
    || (currentToken.kind == Token.FUNCTION) || (currentToken.kind == Token.NAME)
    || (currentToken.kind == Token.LPAREN) || (currentToken.kind == Token.LCURLY)
    || (currentToken.kind == Token.MINUS) || (currentToken.kind == Token.NOT)
    || (currentToken.kind == Token.VELHA))
    {return true}
    else return false
    
}


//--> s(laststat) ={return,break}
private boolean isLaststatStarter(Token currentToken)
{
    if((currentToken.kind == Token.RETURN) || (currentToken.kind == Token.BREAK))
    {return true}
    else return false
}

//--> s(stat) = {Name,'(', do, while, repeat, if, for, function,local}
private boolean isStatStarter(Token currentToken)
{
    if((currentToken.kind == Token.NAME) || (currentToken.kind == Token.LPAREN)
     || (currentToken.kind == Token.DO) || (currentToken.kind == Token.WHILE)
     || (currentToken.kind == Token.REPEAT) || (currentToken.kind == Token.IF)
     || (currentToken.kind == Token.IF) || (currentToken.kind == Token.FOR)
     || (currentToken.kind == Token.FUNCTION) || (currentToken.kind == Token.LOCAL))
     {return true}
     else return false
}

//--> s(prefixexp1): {':', s(args), &} = {':','(','{', String,&}
//Se cadeia vazia refletir em true dentro do metodo a condicional
//no codigo nao funciona! No parserX() que ele deve ser refletido como Dummy. Verificar isso.
private boolean isStarterPrefixexp1(Token currentToken)
{
    if((currentToken.kind == Token.COLON) || isStartersArgs())
     {return true}
     else return false
}
//--> s(prefixexp2):  { '[', '.', &}
private boolean isStarterPrefixexp2(Token currentToken)
{
    if((currentToken.kind == Token.LBRACKET) || (currentToken.kind == Token.DOT))
     {return true}
     else return false
}
//--> s(args) = {'(','{', String}
private boolean isStarterArgs(Token currentToken)
{
    if((currentToken.kind == Token.LPAREN) || (currentToken.kind == Token.LCURLY)
    || (currentToken.kind == Token.STRING))
     {return true}
     else return false    
}

/*****************************************************************/

//chunk::= (stat (';'|&) )* (laststat (';'|&)|&)
private void parseChunck(){
    while(isStatStarter(currentToken))
    {
        parseStat();
        if(currentToken.kind == Token.SEMICOLON)
            acceptIt();
        else
            dummy();
    }
    if(isLaststatStarter(currentToken))
    {
        parseLaststat();
        if(currentToken.kind == Token.SEMICOLON)
        {
           acceptIt();
        }
        else 
            dummy();
    }
    else
        dummy();
}


//laststat::= return (explist|&) | break
private void parseLaststat(){
    switch(currentToken.kind)
    {
        case Token.RETURN:
        {
            acceptIt();
            if(isExplistStarter(currentToken))
                parseExplist();
            else
                dummy();
        }
        case Token.BREAK:
        {
            acceptIt();
        }
    }
}

//funcname ::= Name (`.` Name)* ((`:` Name)|&)
private void parseFuncname(){
    accept(Token.NAME);
    while(currentToken.kind == Token.DOT)
    {
        acceptIt();
        accept(Token.NAME);
    }
    if(currentToken.kind == Token.COLON)
    {
        acceptIt();
        accept(Token.NAME);
    }
    else
        dummy();    
}

//var::= Name (prefixexp1 prefixexp2(`[` exp `]` |  `.` Name)| &) | '(' exp ')'  prefixexp1 prefixexp2 (`[` exp `]` |  `.` Name)

private void parseVar()
{
    switch(currentToken.kind)
    {
        case Token.NAME:
        {
            acceptIt();
            if(isStarterPrefixexp1() || isStarterPrefixexp2() || currentToken.kind == Token.LBRACKET)
            {
                parsePrefixexp1();
                parsePrefixexp2();
                switch(currentToken.kind)
                {
                    case Token.LBRACKET:
                    {
                        acceptIt();
                        parseExp();
                        accept(Token.RBRACKET);
                    }
                    case Token.DOT:
                    {
                        acceptIt();
                        parseName();
                    }
                }
            }
            else
                dummy();
        }
        case Token.LPAREN:
        {
            acceptIt();
            parseExp();
            accept(Token.RPAREN);
            parsePrefixexp1();
            parsePrefixexp2();
            switch(currentToken.kind)
            {
                case Token.LBRACKET:
                {
                    acceptIt();
                    parseExp();
                    accept(Token.RBRACKET);
                }
                case Token.DOT:
                {
                    acceptIt();
                    parseName();
                }
            }
        }
    }
    
}
//namelist::=Name (`,` Name)*
private void parseNamelist()
{
    accept(Token.NAME);
    while(currentToken.kind == Token.COMMA)
    {
        acceptIt();
        parseExp();
    }
}
//explist::= exp (`,` exp)* 
private void parseExplist()
{
    parseExp();
    while(currentToken.kind == Token.COMMA)
    {
        acceptIt();
        parseExp();
    }
}
//prefixexp::=  (Name| '(' exp ')' ) prefixexp1 prefixexp2
public void parsePrefixexp()
{
    switch(currentToken.kind)
    {
        case Token.NAME:
        {
            acceptIt();
        }
        case Token.LPAREN:
        {
            acceptIt();
            parseExp();
            accept(Token.RPAREN);
        }
    }
    parsePrefixexp1();
    parsePrefixexp2();
}

//prefixexp1::=  ( (`:` Name|&) args)  prefixexp1| &	
public void parsePrefixexp1()
{
    if(currentToken.kind == Token.COLON || isArgsStarters())
    {
        if(currentToken.kind == Token.COLON)
        {
            acceptIt();
            accept(Token.NAME);
        }
        parseArgs();
        parsePrefix1();
        
    }
    else
        dummy();
}

//prefixexp2::= ((`[` exp `]` |  `.` Name )  prefixexp1) prefixexp2| &
public void parsePrefixexp2()
{
    if(currentToken.kind == Token.LBRACKET || currentToken.kind == Token.RBRACKET)
    {
        switch(currentToken.kind)
        {
            case Token.LBRACKET:
            {
                acceptIt();
                parseExp();
                accept(Token.RBRACKET);
            }
            case Token.RBRACKET:
            {
                acceptIt();
                accept(Token.NAME);
            }
        }
        parsePrefixexp1();
        parsePrefixexp2();
    }
    else 
        dummy();
}

public void parseExp()
{
    boolean isFunctionStarter = isFunctionStarter();
    boolean isPrefixexpStarter = isPrefixexpStarter();
    boolean isTableconstructorStarter = isTableconstructorStarter();
    boolean isUnopStarter = isUnopStarter();
    
    switch(currentToken)
    {
        case Token.NIL:
            acceptIt();
        case Token.FALSE:
            acceptIt();
        case Token.TRUE:
            acceptIt();
        case Token.NUMBER:
            acceptIt();
        case Token.STRING:
            acceptIt();
        case Token.THREEDOTES:
            acceptIt();
        case Token.FUNCTION:
            acceptIt();
        case isFunctionStarter:
            parseFunction();
        case isPrefixexpStarter:
            parsePrefixexp();
        case isTableconstructorStarter:
            parseTableConstructor();
        case isUnopStarter:
        {
            parseUnopStarter();
            parseExp();
        }
        default:
            //error
    }
    parseExp1();
}


/* FALTA CONSERTAR ESSE BAGULHO MAS DA PRA APROVEITAR BOA PARTE DO CODIGO!
stat::= varlist `=` explist |
		functioncall |
		do block end |
		while exp do block end |
		repeat block until exp |
		if exp then block (elseif exp then block)* ((else block) | & ) end|
for (Name `=` exp `,` exp ((`,` exp) | & ) do block end | 
namelist in explist do block end)|
function funcname funcbody |
local (function Name funcbody |
namelist ((`=` explist) | & ))
*/
private void parseStat()
{
    switch(currentToken.kind)
    {
        case Token.VARLIST:
        {
            parseVarlist();
            accept(Token.BECOMES);
            parseExpList();
            
        }
        case Token.FUNCTIONCALL:
        {
            parseFunctioncall();
        }
        case Token.DO:
        {
            acceptIt();
            parseBlock();
            accept(Token.END);
        }
        case Token.WHILE:
        {
            acceptIt();
            parseExp();
            accept(Token.DO);
            parseBlock();
            accept(Token.END);
        }
        case Token.REPEAT:
        {
            acceptIt();
            parseBlock();
            accept(Token.UNTIL);
            parseExp();
        }
        case Token.IF:
        {
            acceptIt();
            parseExp();
            accept(Token.THEN);
            parseBlock();
            while(currentToken.kind == Token.ELSEIF)
            {
                acceptIt();
                parseExp();
                accept(Token.THEN);
                parseBlock();
            }
            if(currentToken.kind == Token.ELSE)
            {
                acceptIt();
                accept(Token.BLOCK);
            }
            accept(Token.END);
        }
        case Token.FOR:
        {
            acceptIt();
            switch(currentToken.kind)
            {
                case Token.NAME:
                {
                    acceptIt();
                    parseName();
                    accept(Token.BECOMES);
                    parseExp();
                    accept(Token.COMMA);
                    parseExp();
                    if(currentToken.kind == Token.COMMA)
                    {
                        acceptIt();
                        parseExp();
                    }
                    accept(Token.DO);
                    parseBlock();
                    accept(Token.END);
                }
                case Token.NAMELIST:
                {
                    parseNamelist();
                    accept(Token.IN);
                    parseExplist();
                    accept(Token.DO);
                    parseBlock();
                    accept(Token.END);
                }
            }
        }
        case Token.FUNCTION:
        {
            parseFuncname();
            parseFuncbody();
        }
        case Token.LOCAL:
        {
            acceptIt();
            switch(currentToken.kind)
            {
                case Token.FUNCTION:
                {
                    acceptIt();
                    parseName();
                    parseFuncbody();
                }
                case Token.NAMELIST:
                {
                    parseNamelist();
                    if(currentToken.kind == Token.BECOMES)
                    {
                        acceptIt();
                        parseExplist();
                    }
                    else
                        dummy();
                }
            }
        }
    }
}

private void dummy() {}