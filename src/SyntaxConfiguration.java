public class SyntaxConfiguration {

    private String configName;
    private String assemblerSyntaxFileName;
    private String compilerSyntaxFileName;
    private String assemblableFileExtension;
    private String compilableFileExtension;
    private SyntaxNode assemblerSyntax;
    private SyntaxNode compilerSyntax;

    private boolean configTxtContainsErrors = false;

    SyntaxConfiguration(String[] configs, SyntaxNode assemblerSyntax, SyntaxNode compilerSyntax) {
        configName = configs[0];
        assemblerSyntaxFileName = configs[1];
        assemblableFileExtension = configs[2];
        compilerSyntaxFileName = configs[3];
        compilableFileExtension = configs[4];
        for (int i = 1; i < configs.length; i++) { if (configs[i] == null) configTxtContainsErrors = true; }
        this.assemblerSyntax = assemblerSyntax;
        this.compilerSyntax = compilerSyntax;
    }

    public String getConfigName() { return configName; }
    public String getAssemblerSyntaxFileName() { return assemblerSyntaxFileName; }
    public String getCompilerSyntaxFileName() { return compilerSyntaxFileName; }
    public String getAssemblableFileExtension() { return assemblableFileExtension; }
    public String getCompilableFileExtension() { return compilableFileExtension; }
    public SyntaxNode getAssemblerSyntax() { return assemblerSyntax; }
    public SyntaxNode getCompilerSyntax() { return compilerSyntax; }

    public boolean getConfigTxtErrors() { return configTxtContainsErrors; }

}
