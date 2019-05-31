package exam;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Lexer {

    private int pos=0;
    private String input = "";

    private List<Lexem> lexems = new ArrayList<Lexem>();//list c лексемами

    Lexer()//добавляет лексемы в список
    {
        lexems.add(new Lexem(Pattern.compile("^int$"),"INIT"));
        lexems.add(new Lexem(Pattern.compile("^for$"),"FOR"));
        lexems.add(new Lexem(Pattern.compile("^while$"),"WHILE"));
        lexems.add(new Lexem(Pattern.compile("^if$"),"IF"));
        lexems.add(new Lexem(Pattern.compile("^print$"),"PRINT"));
        lexems.add(new Lexem(Pattern.compile("^!F$"),"NF"));
        lexems.add(new Lexem(Pattern.compile("^!$"),"F"));
        lexems.add(new Lexem(Pattern.compile("^list$"),"LIST"));
        lexems.add(new Lexem(Pattern.compile("^set$"),"SET"));
        lexems.add(new Lexem(Pattern.compile("^add|get|remove|size$"),"COL_OP"));

        lexems.add(new Lexem(Pattern.compile("^[a-z]+$"),"VAR"));
        lexems.add(new Lexem(Pattern.compile("^(0|[1-9][0-9]*)$"),"NUMBER"));
        lexems.add(new Lexem(Pattern.compile("^=$"),"ASSIGN_OP"));
        lexems.add(new Lexem(Pattern.compile("^<|>|<=|>=|!=|==$"),"LOG_OP"));
        lexems.add(new Lexem(Pattern.compile("^\\+|\\-|\\*|\\/$"),"OP"));
        lexems.add(new Lexem(Pattern.compile("^;$"),"DOT-ZAP"));
        lexems.add(new Lexem(Pattern.compile("^\\s+$"),"WS"));

        lexems.add(new Lexem(Pattern.compile("^\\($"),"LP"));
        lexems.add(new Lexem(Pattern.compile("^\\)$"),"RP"));
        lexems.add(new Lexem(Pattern.compile("^\\{$"),"LP_F"));
        lexems.add(new Lexem(Pattern.compile("^\\}$"),"RP_F"));
    }

    public List<Token> analyze(String input)//загружаем строку на "твоем" языке и возвращаем список токенов, который формируется в processTokens()
    {
        this.input = input;
        System.out.println("You insert : " + input );
        return processTokens();
    }

    boolean isMatches ( String ss )
    {
        boolean result = false;//флаг

        for ( Lexem e: lexems )
        {
            if( e.getPattern().matcher(ss).matches() )
            {
                result = true;//если строка переданная в метод подходит к одной из лексем, флаг = тру иначе остается фолз
            }
        }

        return result;//возвращаем флаг
    }

    private Lexem lexemMatch( String ss )
    {
        for ( Lexem e: lexems )
        {
            if( e.getPattern().matcher(ss).matches() )
            {
                return e;//возвращаем тип лексемы, которой соответствует строка переданная в метод
            }
        }

        return null;//иначе фолз
    }

    public List<Token> processTokens()
    {
        String acc = "";//аккумулятор, тут собирается значение лексемы
        List <Token> tokens = new ArrayList<Token>();//список токенов
        boolean isGood = false;

        while( pos <= input.length() - 1 )//до тех пор, пока позиция меньше длины входной строки
        {
            String newAcc = acc + input.charAt(pos);//буфферная строка( если она тру, то в асс вводится ее значение)

            if ( isMatches( newAcc ) == false )
            {
                Lexem currentLexem  = lexemMatch( acc );//ищем лексему соответствующую строке

                if ( currentLexem != null ) {//если лексемы нет, то... ошибка?

                    Token token = new Token(currentLexem.name, acc);//создаем новый токен

                    tokens.add(token);//добавляем токен в список токенов

                    acc = ""+ input.charAt(pos);
                }
            }
            else {
                acc = newAcc;
            }

            pos++;
        }

        tokens.add(new Token(lexemMatch( acc ).name, acc));

        return tokens;
    }

}
