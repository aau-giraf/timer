package dk.aau.cs.giraf.oasis.metadata;

public class MultiTableExport implements ExportDefinition {

    private final String ExportedName;
    private final String Table;
    
    MultiTableExport(String exportedName, ForeignKeyDefinition... keys){
        ExportedName = exportedName;

        if (keys.length < 1) {
            Table = "WARNING";
            return;
        }
        
        StringBuilder sb = new StringBuilder();

        sb.append(keys[0].getLocalTable());

        for(int i = 0; i < keys.length; i++)
        {
            ForeignKeyDefinition fk = keys[i];
            sb.append(" INNER JOIN ");
            sb.append(fk.ForeignTable);
            sb.append(" ON (");
            sb.append(fk.getLocalTable());
            sb.append(".");
            sb.append(fk.LocalKey);
            sb.append(" = ");
            sb.append(fk.ForeignTable);
            sb.append(".");
            sb.append(fk.ForeignKey);
            sb.append(")");
        }

        Table = sb.toString();
    }
    
    @Override
    public String getPath() {
        return ExportedName;
    }

    @Override
    public String getTable() {
        return Table;
    }

    @Override
    public String getSelection() {
        return null;
    }
}
