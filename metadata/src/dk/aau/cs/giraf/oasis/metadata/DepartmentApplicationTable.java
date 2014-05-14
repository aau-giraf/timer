package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class DepartmentApplicationTable {

    public static final String TABLE_NAME = "department_application";
    public static final String COLUMN_IDDEPARTMENT = "department_id";
    public static final String COLUMN_IDAPPLICATION = "application_id";

    public static final ForeignKeyDefinition ID_DEPARTMENT_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDDEPARTMENT,
                    DepartmentTable.TABLE_NAME,
                    DepartmentTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_APPLICATION_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDAPPLICATION,
                    ApplicationTable.TABLE_NAME,
                    ApplicationTable.COLUMN_ID,
                    OnDeleteCascade
            );


    public static final TableDefinition DEPARTMENT_APPLICATION_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDDEPARTMENT, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDAPPLICATION, Integer, NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_DEPARTMENT_FOREIGN_KEY,
                    ID_APPLICATION_FOREIGN_KEY
            }
    );

    public static final MultiTableExport DEPARTMENT_APPLICATION =
            new MultiTableExport(
                    "department_with_application",
                    ID_DEPARTMENT_FOREIGN_KEY,
                    ID_APPLICATION_FOREIGN_KEY
            );
}
