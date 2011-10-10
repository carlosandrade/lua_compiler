//chunk::= (stat (';'|&) )* (laststat (';'|&)|&)
//--> s(stat) = {Name,'(', do, while, repeat, if, for, function,local}
private void parseChunck(){
    while((currentToken.kind == Token.NAME) || (currentToken.kind == Token.LPAREN)
    || (currentToken.kind == Token.DO) || (currentToken.kind == Token.WHILE)
    || (currentToken.kind == Token.REPEAT) || (currentToken.kind == Token.IF)
    || (currentToken.kind == Token.IF) || (currentToken.kind == Token.FOR)
    || (currentToken.kind == Token.FUNCTION) || (currentToken.kind == Token.LOCAL))
    {
        parseStat();
        if(currentToken.kind == Token.SEMICOLON)
            acceptIt();
        else
            dummy();
    }
    if((currentToken.kind == Token.RETURN) || (currentToken.kind == Token.BREAK))
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