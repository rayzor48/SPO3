package exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Poliz {
    private List<Token> tokens, poliz;
    private Stack<Token> stack = new Stack<>();
    private Stack<Integer> startsBuffet = new Stack<>();
    private int position = 0, start;

    public Poliz(List<Token> tokens){
        this.tokens = tokens;
        poliz = new ArrayList<>();
        createPoliz();
    }

    public List<Token> getPoliz(){
        return poliz;
    }

    private void createPoliz(){
        Token t = null;

        while ( position < tokens.size()){
            switch (getToken().getType()){
                case "INIT":
                    poliz.add(getToken());
                    break;
                case "VAR":
                    poliz.add(getToken());
                    break;
                case "LIST":
                    poliz.add(getToken());
                    break;
                case "SET":
                    poliz.add(getToken());
                    break;
                case "NUMBER":
                    poliz.add(getToken());
                    break;
                case "OP":
                    if(getPriority(getToken()) > getPriority(stack.peek())){
                        stack.push(getToken());
                    } else {
                        while (stack.size() > 1 && !stack.peek().getType().equals("LP")) {
                            if(getPriority(getToken()) <= getPriority(stack.peek())) {
                                poliz.add(stack.pop());
                            } else break;
                        }
                        stack.push(getToken());
                    }
                    break;
                case "ASSIGN_OP":
                    stack.add(getToken());
                    break;
                case "LOG_OP":
                    stack.push(getToken());
                    start = poliz.size() - 1;
                    startsBuffet.push(start);
                    break;
                case "COL_OP":
                    stack.add(getToken());
                    break;
                case "LP":
                    stack.push(getToken());
                    break;
                case "RP":
                    while (!stack.peek().getType().equals("LP")) {
                        poliz.add(stack.pop());
                    }
                    stack.pop();
                    break;
                case "LP_F":
                    poliz.add(start + 3, t);
                    poliz.add(start + 4, new Token("NF", "!f"));
                    break;
                case "RP_F":
                    start = startsBuffet.pop();
                    if( poliz.get(start + 3).getValue().equals("if")) {
                        poliz.get(start + 3).setValue(String.valueOf(poliz.size()));
                    } else {
                        poliz.get(start + 3).setValue(String.valueOf(poliz.size() + 2));
                        poliz.add(new Token("NUMBER", String.valueOf(start)));
                        poliz.add(new Token("F", "!"));
                    }
                    break;
                case "DOT-ZAP":
                    while (!stack.empty() && !stack.peek().getType().equals("LP")){
                        poliz.add(stack.pop());
                    }
                    break;
                case "FOR":
                    t = new Token("NUMBER", "cicle");
                    break;
                case "WHILE":
                    t = new Token("NUMBER", "cicle");
                    break;
                case "IF":
                    t = new Token("NUMBER", "if");
                    break;
                case "PRINT":
                    poliz.add(getToken());
                    break;
            }

            position++;
        }
    }

    private int getPriority(Token token){//получение приоритета операции (+|-) - приоритет 1, иначе (*|/) - приоритет 2
        int priority = 0;

        if(token.getValue().equals("-") || token.getValue().equals("+")){
            priority = 1;
        } else if(token.getValue().equals("/") || token.getValue().equals("*")){
            priority = 2;
        }

        return priority;
    }

    private Token getToken(){
        return tokens.get(position);
    }
}
