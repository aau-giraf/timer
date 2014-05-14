package dk.aau.cs.giraf.oasis.metadata;

class ForeignKeyDefinition {
    private String localTable;
    public final String ForeignTable;
    public final String LocalKey, ForeignKey;
    public final ForeignKeyOption[] Options;

    public ForeignKeyDefinition(String localKey, String foreignTable, String foreignKey, ForeignKeyOption... options) {
        LocalKey = localKey;
        ForeignTable = foreignTable;
        ForeignKey = foreignKey;
        Options = options;
    }

    public String getLocalTable() {
        return localTable;
    }

    public void setLocalTable(String localTable) {
        this.localTable = localTable;
    }
    
    public String ToSQLStatement()
    {
        String opts = "";
        if(Options != null){

            StringBuilder b = new StringBuilder();

            for(ForeignKeyOption o : Options){
                b.append(o.getSqlOption());
                b.append(" ");
            }

            opts = b.toString();
        }
        
        return String.format("FOREIGN KEY(%s) REFERENCES %s(%s) %s", LocalKey, ForeignTable, ForeignKey, opts);
    }
}
