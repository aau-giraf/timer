package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;

class PrimaryKeyDefinition {
    public final ColumnDefinition Keys[];
    
    public PrimaryKeyDefinition(ColumnDefinition... keys)
    {
        Keys = keys;
    }
    
    public PrimaryKeyDefinition(String primaryKeyColumn)
    {
        this(new ColumnDefinition(primaryKeyColumn, Integer));
    }
    
    public String ToSQLStatement()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("PRIMARY KEY(");
        
        String prefix = "";
        for(ColumnDefinition cd : Keys)
        {
            sb.append(prefix);
            sb.append(cd.ColumnName);
            prefix = ",";
        }
        
        sb.append(")");
        
        return sb.toString();
    }
}
