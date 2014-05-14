package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class CategoryTable {

    public static final String TABLE_NAME = "category";
    public static final String COLUMN_ID = "category_id";
    public static final String COLUMN_NAME = "category_name";
    public static final String COLUMN_COLOUR = "category_colour";
    public static final String COLUMN_ICON = "category_icon";
    public static final String COLUMN_IDSUPERCATEGORY = "super_category_id";

    public static final ForeignKeyDefinition ID_SUPER_CATEGORY_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_IDSUPERCATEGORY, 
                    TABLE_NAME, 
                    CategoryTable.COLUMN_ID,
                    OnDeleteCascade
            );
    
    public static final TableDefinition CATEGORY_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(COLUMN_ID),
            new ForeignKeyDefinition[]{
                    ID_SUPER_CATEGORY_FOREIGN_KEY
            },
            new ColumnDefinition(COLUMN_NAME, Text, NotNull),
            new ColumnDefinition(COLUMN_COLOUR, Integer, NotNull),
            new ColumnDefinition(COLUMN_ICON, Blob),
            new ColumnDefinition(COLUMN_IDSUPERCATEGORY, Integer)
    );
}
