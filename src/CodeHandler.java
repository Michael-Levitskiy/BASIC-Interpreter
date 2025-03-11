import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeHandler {
    //////////////////////////////
    // Class instance variables //
    //////////////////////////////
    private String file;
    private int index = 0;


    /////////////////
    // Constructor //
    /////////////////
    /**
     * Given a fileName, convert file to a string
     * @param fileName (String) of the fileName to use
     */
    public CodeHandler(String fileName) {
        Path myPath = Paths.get(fileName);
        try{
            this.file =  new String(Files.readAllBytes(myPath));
        } catch (IOException e){
            System.err.println("Could not read bytes from file");
            System.exit(0);
        }
    }

    
    //////////////
    // Accessor //
    //////////////
    /**
     * @return the value of index
     */
    public int getIndex(){
        int toReturn = this.index;
        return toReturn;
    }


    ////////////////////
    // Public Methods //
    ////////////////////
    /**
     * Looks 'i' characters ahead and returns that character
     * Does not move the index
     * @param i (int) - the number of chars ahead to look
     * @return the char
     */
    public char Peek(int i){
        if (this.index + i >= this.file.length()){
            return '\0';
        }
        return file.charAt(this.index+i);
    }
    
    /**
     * Returns a string of the next 'i' chars
     * Doesn't move the index
     * @param i (int) - number of chars to return
     * @return the substring
     */
    public String PeekString(int i){
        // check if you're peeking outside the string
        if(this.index+i >= this.file.length()){
            return "";
        }
        return file.substring(this.index, this.index+i);
    }
    
    /**
     * @return the next char and move the index
     */
    public char GetChar(){
        char next = file.charAt(index);
        this.index++;
        return next;
    }

    /**
     * Moves the index ahead 'i' positions
     * @param i (int) - the number of positions to move ahead
     */
    public void Swallow(int i){
        this.index += i;
    }
    
    /**
     * @return true if we are at the end of the document
     */
    public boolean IsDone(){
        boolean done = (this.file.length() == this.index) ? true : false;
        return done;
    }
    
    /**
     * @return the rest of the document as a string
     */
    public String Remainder(){
        return this.file.substring(this.index);
    }
}