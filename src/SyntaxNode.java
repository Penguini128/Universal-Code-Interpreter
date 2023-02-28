import java.util.ArrayList;

public class SyntaxNode {

    SyntaxNode parent;
    ArrayList<SyntaxNode> children;
    String syntaxWord;
    boolean validEnding;
    String translationWord;

    SyntaxNode() {
        parent = null;
        children = new ArrayList<SyntaxNode>();
        syntaxWord = null;
        validEnding = false;
        translationWord = null;
    }

    SyntaxNode(SyntaxNode parent, String syntaxWord) {
        this.parent = parent;
        parent.children.add(this);
        children = new ArrayList<SyntaxNode>();
        this.syntaxWord = syntaxWord;
        validEnding = false;
        translationWord = null;
    }

    public String getTranslationWord() { return translationWord; }
    public boolean isValidEnding() { return validEnding; }

    public void setTranslationWord(String s) { translationWord = s; }


}