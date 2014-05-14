package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class ApplicationTable {

    public static final String TABLE_NAME = "application";
    public static final String COLUMN_ID = "application_id";
    public static final String COLUMN_NAME = "application_name";
    public static final String COLUMN_VERSION = "application_version";
    public static final String COLUMN_ICON = "application_icon";
    public static final String COLUMN_PACKAGE = "application_package";
    public static final String COLUMN_ACTIVITY = "application_activity";
    public static final String COLUMN_DESCRIPTION = "application_description";
    public static final String COLUMN_AUTHOR = "application_author";

    public static final ForeignKeyDefinition AUTHOR_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_AUTHOR, 
                    UserTable.TABLE_NAME, 
                    UserTable.COLUMN_ID,
                    OnDeleteSetNull
            );
    
    
    public static final TableDefinition APPLICATION_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(COLUMN_ID),
            new ForeignKeyDefinition[]{
                    AUTHOR_FOREIGN_KEY
            },
            new ColumnDefinition(COLUMN_NAME, Text, NotNull),
            new ColumnDefinition(COLUMN_VERSION, Text, NotNull),
            new ColumnDefinition(COLUMN_ICON, Blob, NotNull),
            new ColumnDefinition(COLUMN_PACKAGE, Text, NotNull),
            new ColumnDefinition(COLUMN_ACTIVITY, Text, NotNull),
            new ColumnDefinition(COLUMN_DESCRIPTION, Text, NotNull),
            new ColumnDefinition(COLUMN_AUTHOR, Integer)
    );
}
