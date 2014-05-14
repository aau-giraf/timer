package dk.aau.cs.giraf.oasis.metadata;

public class TableDefinition {

    final String TableName;
    final ForeignKeyDefinition ForeignKeys[];
    final PrimaryKeyDefinition PrimaryKey;
    final ColumnDefinition[] Columns;
    
    public final TableExport ListExport;
    public final SingleRowExport RowExport;

    TableDefinition(String tableName, PrimaryKeyDefinition primaryKey, ForeignKeyDefinition foreignKeys[], ColumnDefinition... columns) {
        TableName = tableName;
        PrimaryKey = primaryKey;
        ForeignKeys = foreignKeys;
        Columns = columns;
        
        ListExport = new TableExport(this);
        RowExport = new SingleRowExport(this);
        
        if(ForeignKeys == null)
            return;
        
        for (ForeignKeyDefinition fk : ForeignKeys)
        {
            fk.setLocalTable(TableName);
        }
    }
    
    TableDefinition(String tableName, PrimaryKeyDefinition primaryKey, ColumnDefinition... columns)
    {
        this(tableName, primaryKey, null, columns);
    }
    
    TableDefinition(String tableName, String primaryKeyName, ColumnDefinition... columns)
    {
        this(tableName, new PrimaryKeyDefinition(primaryKeyName), null, columns);
    }
    
    public String getIdColumn()
    {
        if(PrimaryKey.Keys.length == 1)
            return PrimaryKey.Keys[0].ColumnName;
        
        return "";
    }
    
    public String getSQLCreateString() {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE ");
        sb.append(TableName);
        sb.append(" (");

        String prefix = "";
        
        for(ColumnDefinition cb : PrimaryKey.Keys)
        {
            sb.append(prefix);
            sb.append(cb.ToSQLStatement());
            prefix = ", ";
        }

        for (ColumnDefinition cb : Columns)
        {
            sb.append(prefix);
            sb.append(cb.ToSQLStatement());
            prefix = ", ";
        }

        if(ForeignKeys != null)
        {
            for (ForeignKeyDefinition fkd : ForeignKeys)
            {
                sb.append(prefix);
                sb.append(fkd.ToSQLStatement());
                prefix = ", ";
            }
        }

        sb.append(prefix);
        
        sb.append(PrimaryKey.ToSQLStatement());

        sb.append(");");

        return sb.toString();
    }

    public String getSQLDropString()
    {
        return String.format("DROP TABLE IF EXISTS %s", TableName);   
    }
}
