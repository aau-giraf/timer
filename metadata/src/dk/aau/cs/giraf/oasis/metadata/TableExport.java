package dk.aau.cs.giraf.oasis.metadata;

public class TableExport implements ExportDefinition {
    protected final String TableName;
    protected final String ExportedName;
    protected final TableDefinition TableDef;

    TableExport(TableDefinition tableDef, String exportedName){
        TableDef = tableDef;
        TableName = tableDef.TableName;
        ExportedName = exportedName;
    }
    
    TableExport(TableDefinition tableDef){
        this(tableDef, tableDef.TableName);
    }

    @Override
    public String getPath() {
        return ExportedName;
    }

    @Override
    public String getTable() {
        return TableName;
    }

    @Override
    public String getSelection() {
        return null;
    }
}
