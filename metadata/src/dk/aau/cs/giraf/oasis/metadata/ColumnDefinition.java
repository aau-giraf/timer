package dk.aau.cs.giraf.oasis.metadata;


class ColumnDefinition {
    
    public ColumnDefinition(String columnName, ColumnType type, ColumnOption... options) {
        ColumnName = columnName;
        Type = type;
        Options = options;
    }

    public final String ColumnName;
    public final ColumnType Type;
    public final ColumnOption[] Options;
    
    
    public String ToSQLStatement()
    {
        String opts = "";
        if(Options != null){
            
            StringBuilder b = new StringBuilder();
            
            for(ColumnOption o : Options){
                b.append(o.getSqlOption());
                b.append(" ");
            }
            
            opts = b.toString();
        }
        
        
        return String.format("%s %s %s", ColumnName, Type.getSqlType(), opts);
    }
}
