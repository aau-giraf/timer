package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class ProfileTable {
    public static final String TABLE_NAME = "profile";
    public static final String COLUMN_ID = "profile_id";
    public static final String COLUMN_NAME = "profile_name";
    public static final String COLUMN_PHONE = "profile_phone";
    public static final String COLUMN_PICTURE = "profile_image";
    public static final String COLUMN_EMAIL = "profile_email";
    public static final String COLUMN_ROLE = "profile_role";
    public static final String COLUMN_ADDRESS = "profile_address";
    public static final String COLUMN_SETTINGS = "profile_settings";
    public static final String COLUMN_IDUSER = "user_id";
    public static final String COLUMN_IDDEPARTMENT = "department_id";
    public static final String COLUMN_AUTHOR = "profile_author";

    public static final ForeignKeyDefinition ID_USER_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_IDUSER, 
                    UserTable.TABLE_NAME, 
                    UserTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_DEPARTMENT_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_IDDEPARTMENT, 
                    DepartmentTable.TABLE_NAME, 
                    DepartmentTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition AUTHOR_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_AUTHOR, 
                    UserTable.TABLE_NAME, 
                    UserTable.COLUMN_ID,
                    OnDeleteSetNull
            );
    
    
    public static final TableDefinition PROFILE_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(COLUMN_ID),
            new ForeignKeyDefinition[]
                    {
                            ID_USER_FOREIGN_KEY,
                            ID_DEPARTMENT_FOREIGN_KEY,
                            AUTHOR_FOREIGN_KEY
                    },
            new ColumnDefinition(COLUMN_NAME, Text, NotNull),
            new ColumnDefinition(COLUMN_PHONE, Text),
            new ColumnDefinition(COLUMN_PICTURE, Blob),
            new ColumnDefinition(COLUMN_EMAIL, Text),
            new ColumnDefinition(COLUMN_ROLE, Integer, NotNull),
            new ColumnDefinition(COLUMN_ADDRESS, Text, NotNull),
            new ColumnDefinition(COLUMN_SETTINGS, Blob),
            new ColumnDefinition(COLUMN_IDUSER, Integer, Unique),
            new ColumnDefinition(COLUMN_IDDEPARTMENT, Integer, NotNull),
            new ColumnDefinition(COLUMN_AUTHOR, Integer)
    );
    
    public static final MultiTableExport PROFILE_DEPARTMENT = 
            new MultiTableExport(
                    "profile_department",
                    ID_DEPARTMENT_FOREIGN_KEY
            );


}
