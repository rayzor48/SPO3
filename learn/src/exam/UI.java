package exam;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class UI {
    public static void main (String [] args){
        Lexer lexer = new Lexer();
        String input = "set a; a add (2312 *23); int b = 0; a add b; b = a size; print;";//int pig=((1 +5)+(7 + (3)));    while(a < df){if(a < df){i = i +1 +(123-4 + sdf)/2213;}} //while(a < df){if(a < df){while(a < df){if(a < df){i = i +1 +(123-4 + sdf)/2213;}}}}
        //a=pig-2; popa = denis + pig*masha+ehidnost; a=3-1;
        //for(int a = 0; a < df; a = a + 1){dota = nastya * 1 + denis * 1};
        List<Token> tokens = lexer.analyze(input);

        //System.out .println("Your tokens : ");

        for ( Token t : tokens )
        {
            //System.out.println( t.toString() );
        }

        Parser parser = new Parser(tokens);
        tokens = parser.result();

        System.out.print("Poliz : ");

        Poliz poliz = new Poliz(tokens);

        for(Token t : poliz.getPoliz()){
            System.out.print(t.getValue() + ", ");
        }

        System.out.print("EOF");

        Go go = new Go(poliz.getPoliz());
        go.interpretation();
        //System.out.println(tokens.toString());

    }
}
