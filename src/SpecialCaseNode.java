public class SpecialCaseNode {

    String caseName;
    boolean customSpecialCase;
    char escapeIndicator;
    String formatString;

    SpecialCaseNode(String caseName, String formatString) {
        this.caseName = caseName;
        customSpecialCase = false;
        this.formatString = formatString;
    }

    SpecialCaseNode(String caseName, char escapeIndicator, String formaString) {
        this.caseName = caseName;
        customSpecialCase = true;
        this.escapeIndicator = escapeIndicator;
        this.formatString = formaString;
    }

    public boolean isCustomSpecialCase() { return customSpecialCase; }

}