import java.util.LinkedList;
import java.util.Optional;

public class TokenManager {
    
    // Class Instance Variable
    private LinkedList<Token> tokens;

    
    /**
     * Constructor
     * @param tokens
     */
    public TokenManager(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }


    ////////////////////
    // Public Methods //
    ////////////////////
    /**
     * Peek "j" tokens ahead
     * @return the token - if we aren't past the end of the token list
     */
    public Optional<Token> Peek(int j){
        return (tokens.size() <= j) ? Optional.empty() : Optional.of(tokens.get(j));
    }

    /**
     * @return true (boolean) if the token list is not empty
     */
    public boolean MoreTokens(){
        return (tokens.isEmpty()) ? false : true;
    }

    /**
     * Looks at the head of the list
     * If the token type of the head is the same as what was passed in
     * remove that token from the list and return it
     * In all other cases, returns Optional.Empty()
     * @param t
     * @return
     */
    public Optional<Token> MatchAndRemove(Token.TokenType t){
        return (tokens.peekFirst().getType() == t) ? Optional.of(tokens.pollFirst()) : Optional.empty();
    }
}