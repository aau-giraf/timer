package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class DepartmentTable {
    public static final String TABLE_NAME = "department";

    public static final String COLUMN_ID = "department_id";
    public static final String COLUMN_NAME = "department_name";
    public static final String COLUMN_ADDRESS = "department_address";
    public static final String COLUMN_PHONE = "department_phone";
    public static final String COLUMN_EMAIL = "department_email";
    public static final String COLUMN_IDSUPERDEPARTMENT = "super_department_id";
    public static final String COLUMN_AUTHOR = "department_author";

    public static final ForeignKeyDefinition ID_SUPER_DEPARTMENT_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_IDSUPERDEPARTMENT, 
                    TABLE_NAME, 
                    COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition AUTHOR_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_AUTHOR, 
                    UserTable.TABLE_NAME, 
                    UserTable.COLUMN_ID,
                    OnDeleteSetNull
            );
    
    
    public static final TableDefinition DEPARTMENT_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(COLUMN_ID),
            new ForeignKeyDefinition[]
                    {
                            ID_SUPER_DEPARTMENT_FOREIGN_KEY,
                            AUTHOR_FOREIGN_KEY,
                    },
            new ColumnDefinition(COLUMN_NAME, Text, NotNull),
            new ColumnDefinition(COLUMN_ADDRESS, Text, NotNull),
            new ColumnDefinition(COLUMN_PHONE, Text, NotNull),
            new ColumnDefinition(COLUMN_EMAIL, Text, NotNull),
            new ColumnDefinition(COLUMN_IDSUPERDEPARTMENT, Integer),
            new ColumnDefinition(COLUMN_AUTHOR, Integer)
    );
}
