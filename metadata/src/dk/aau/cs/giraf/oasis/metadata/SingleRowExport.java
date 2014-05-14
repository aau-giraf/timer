package dk.aau.cs.giraf.oasis.metadata;


public class SingleRowExport extends TableExport {
    
    SingleRowExport(TableDefinition tableDef, String exportedName) {
        super(tableDef, exportedName);
    }

    SingleRowExport(TableDefinition tableDef){
        this(tableDef, tableDef.TableName);
    }
    
    @Override
    public String getPath() {
        return super.getPath() + "/#";
    }

    @Override
    public String getTable() {
        return super.getTable();
    }

    @Override
    public String getSelection() {
        return TableDef.getIdColumn() + " = ?";
    }
}
