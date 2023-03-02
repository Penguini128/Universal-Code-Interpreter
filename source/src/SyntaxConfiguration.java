public class SyntaxConfiguration {

    private ConfigSettings settings;
    private SyntaxNode assemblerSyntax;
    private SyntaxNode compilerSyntax;

    SyntaxConfiguration(ConfigSettings settings, SyntaxNode assemblerSyntax, SyntaxNode compilerSyntax) {
        this.settings = settings;
        this.assemblerSyntax = assemblerSyntax;
        this.compilerSyntax = compilerSyntax;
    }

    public ConfigSettings getConfigSettings() { return settings; }
    public String getConfigName() { return settings.getConfigName(); }
    public SyntaxNode getAssemblerSyntax() { return assemblerSyntax; }
    public SyntaxNode getCompilerSyntax() { return compilerSyntax; }

    public String toString() { return settings.getConfigName(); }
    public boolean equals(SyntaxConfiguration other) { return (settings.getConfigName().equals(other.getConfigSettings().getConfigName())); }

}
