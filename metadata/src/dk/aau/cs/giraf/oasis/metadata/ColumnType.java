package dk.aau.cs.giraf.oasis.metadata;

enum  ColumnType {
    Integer("INTEGER"),
    Text("TEXT"),
    Blob("BLOB");
    
    private final String SqlType;
    
    private ColumnType(String sqlType){
        SqlType = sqlType;
    }
    
    public String getSqlType(){
        return SqlType;
    }
}
