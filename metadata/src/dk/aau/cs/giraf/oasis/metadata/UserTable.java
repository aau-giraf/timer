package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;

public class UserTable {

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_USERNAME = "user_username";
    public static final String COLUMN_PASSWORD = "user_password";
    public static final String COLUMN_CERTIFICATE = "user_certificate";
    
    public static final TableDefinition USER_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(COLUMN_ID),
            new ColumnDefinition(COLUMN_USERNAME, Text, Unique, NotNull),
            new ColumnDefinition(COLUMN_PASSWORD, Text),
            new ColumnDefinition(COLUMN_CERTIFICATE, Text, Unique)
    );
}
