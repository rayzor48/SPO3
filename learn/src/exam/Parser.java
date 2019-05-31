package exam;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private ArrayList<Token> tokens;
    private int position = 0;

    public Parser(List<Token> tokens){
        this.tokens = new ArrayList<Token>();
        for (Token token : tokens){
            if(!token.getType().equals("WS")){
                this.tokens.add(token);
            }
        }
    }

    public ArrayList<Token> result(){
        System.out.println("Parser : " + lang());
        return tokens;
    }

    private boolean lang(){

        while(position <= tokens.size() - 1){
            if(!expr()){
                System.out.println("Error in : " + tokens.get(position).value + " position : " + position);
                return false;
            }
        }

        return true;
    }

    private boolean expr(){
        boolean expr = false;

        if(init() || assign(true) || myFor() || myWhile() || myIf() || myList() || mySet() || collectionsOperations() || print()){
            expr = true;
        }

        return expr;
    }

    private boolean init(){
        int old_position = position;
        boolean init = false;

        if(getType().equals("INIT")){
            init = assign(true);
        }

        position = init ? position : old_position;
        return init;
    }

    private boolean assign(boolean flag) {
        int old_position = position;
        boolean assign = false;

        if(getType().equals("VAR")){
            assign = assignOp(flag);
        }

        position = assign ? position : old_position;
        return assign;
    }

    private boolean assignOp(boolean flag) {
        int old_position = position;
        boolean assignOp = false;

        if(getType().equals("ASSIGN_OP")){
            assignOp = valueOp(flag);
        }

        position = assignOp ? position : old_position;
        return assignOp;
    }

    private boolean valueOp(boolean flag){
        int old_position = position;
        boolean valueOp = false;

        valueOp = collectionsOperations();

        position = valueOp ? position : old_position;

        if(!valueOp) {
            if (value()) {
                if (op()) {
                    valueOp = valueOp(flag);
                } else {
                    if (flag) {
                        return getType().equals("DOT-ZAP");
                    } else {
                        return getType().equals("RP");
                    }
                }
            }
        }
        position = valueOp ? position : old_position;
        return valueOp;
    }

    private boolean op(){
        int old_position = position;
        boolean op = getType().equals("OP");

        position = op ? position : old_position;
        return op;
    }

    private boolean value(){
        int old_position = position;
        boolean value = false;

        switch (getType()){
            case "NUMBER":
                value = true;
                break;
            case "VAR":
                value = true;
                break;
            case "LP":
                value = assignInBrackets();
                break;
        }

        position = value ? position : old_position;
        return value;
    }

    private boolean assignInBrackets(){
        return valueOp(false);
    }

    private boolean myFor(){
        int old_position = position;
        boolean myFor = false;

        if(getType().equals("FOR")){
            if(for_conditional()){
                myFor = body();
            }
        }

        position = myFor ? position : old_position;
        return myFor;
    }

    private boolean for_conditional(){
        int old_position = position;
        boolean for_conditional = false;

        if(getType().equals("LP")){
            if(init()){
                if(logOp() && getType().equals("DOT-ZAP")){
                    for_conditional = assign(false);
                }
            }
        }

        position = for_conditional ? position : old_position;
        return for_conditional;
    }

    private boolean myWhile(){
        int old_position = position;
        boolean myWhile = false;

        if(getType().equals("WHILE")){
            if(getType().equals("LP")){
                if(logOp() && getType().equals("RP")){
                    myWhile = body();
                }
            }
        }

        position = myWhile ? position : old_position;
        return myWhile;
    }

    private boolean logOp(){//while_Conditional
        int old_position = position;
        boolean logOp = false;

        if(getType().equals("VAR")){
            if(getType().equals("LOG_OP")){
                if(tokens.get(position).getType().equals("NUMBER") || tokens.get(position).getType().equals("VAR")){
                    logOp = true;
                }
            }
        }

        position = logOp ? position + 1: old_position;
        return logOp;
    }

    private boolean myIf(){
        int old_position = position;
        boolean myIf = false;

        if(getType().equals("IF")){
            if(getType().equals("LP")){
                if(logOp() && getType().equals("RP")){
                    myIf = body();
                }
            }
        }

        position = myIf ? position : old_position;
        return myIf;
    }

    private boolean body(){
        int old_position = position;
        boolean body = false;
        if(getType().equals("LP_F")){
            if(new_lang()){
                body = getType().equals("RP_F");
            }
        }

        position = body ? position : old_position;
        return body;
    }

    private boolean new_lang(){
        while(!tokens.get(position).getType().equals("RP_F")){
            if(!expr()){
                System.out.println("Error in : " + tokens.get(position).value + " position : " + position);
                return false;
            }
        }
        return true;
    }

    private boolean myList(){
        int old_position = position;
        boolean myList = false;

        if(getType().equals("LIST")){
            if(getType().equals("VAR")){
                myList = getType().equals("DOT-ZAP");
            }
        }

        position = myList ? position : old_position;
        return myList;
    }

    private boolean mySet(){
        int old_position = position;
        boolean mySet = false;

        if(getType().equals("SET")){
            if(getType().equals("VAR")){
                mySet = getType().equals("DOT-ZAP");
            }
        }

        position = mySet ? position : old_position;
        return mySet;
    }

    private boolean collectionsOperations() {
        int old_position = position;
        boolean collectionsOperations = false;

        if (getType().equals("VAR")) {
            if(getType().equals("COL_OP")){
                if(tokens.get(position).getValue().equals("size")){
                    collectionsOperations = getType().equals("DOT-ZAP");
                } else {
                    if(value()){
                        collectionsOperations = getType().equals("DOT-ZAP");
                    }
                }
            }
        }

        position = collectionsOperations ? position : old_position;
        return collectionsOperations;
    }

    private boolean print() {
        int old_position = position;
        boolean print = false;

        if (getType().equals("PRINT")) {
                print = getType().equals("DOT-ZAP");
        }

        position = print ? position : old_position;
        return print;
    }

    private String getType(){//возвращает следующий токен
        try {
            return tokens.get(position++).getType();
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("Error: Lexeme \"" + "\" expected");
            System.exit(1);
        }
        return null;
    }
}
