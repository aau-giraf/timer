package dk.aau.cs.giraf.oasis.metadata;

enum ColumnOption {
    NotNull("NOT NULL"),
    Unique("UNIQUE");
    
    private final String Option;
    
    ColumnOption(String option){
        Option = option;
    }
    
    public String getSqlOption(){
        return Option;
    }
}
