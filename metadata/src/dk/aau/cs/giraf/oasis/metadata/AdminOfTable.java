package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class AdminOfTable {

    public static final String TABLE_NAME = "admin_of";
    public static final String COLUMN_IDUSER = "user_id";
    public static final String COLUMN_IDDEPARTMENT= "department_id";

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
    
    public static final TableDefinition ADMIN_OF_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDUSER, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDDEPARTMENT, Integer, NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_USER_FOREIGN_KEY,
                    ID_DEPARTMENT_FOREIGN_KEY
            }
    );

    public static final MultiTableExport DEPARTMENT_ADMIN =  
            new MultiTableExport(
                    "department_with_admin", 
                    ID_USER_FOREIGN_KEY, 
                    ID_DEPARTMENT_FOREIGN_KEY
            );
}
