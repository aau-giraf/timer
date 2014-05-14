package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;

public class TagTable {
    public static final String TABLE_NAME = "tag";

    public static final String COLUMN_ID = "tag_id";
    public static final String COLUMN_NAME = "tag_name";
    
    public static final TableDefinition TAG_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(COLUMN_ID),
            new ColumnDefinition(COLUMN_NAME, Text, NotNull)
    );
}
