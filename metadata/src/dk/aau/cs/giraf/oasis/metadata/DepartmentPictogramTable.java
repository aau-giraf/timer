package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class DepartmentPictogramTable {

    public static final String TABLE_NAME = "department_pictogram";
    public static final String COLUMN_IDDEPARTMENT = "department_id";
    public static final String COLUMN_IDPICTOGRAM = "pictogram_id";

    public static final ForeignKeyDefinition ID_DEPARTMENT_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_IDDEPARTMENT, 
                    DepartmentTable.TABLE_NAME, 
                    DepartmentTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_PICTOGRAM_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_IDPICTOGRAM, 
                    PictogramTable.TABLE_NAME, 
                    PictogramTable.COLUMN_ID,
                    OnDeleteCascade
            );
    
    
    public static final TableDefinition DEPARTMENT_PICTOGRAM_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDDEPARTMENT, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDPICTOGRAM, Integer, NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_DEPARTMENT_FOREIGN_KEY,
                    ID_PICTOGRAM_FOREIGN_KEY
            }
    );
    
    public static final MultiTableExport DEPARTMENT_PICTOGRAM = 
            new MultiTableExport(
                    "department_with_pictogram",
                    ID_DEPARTMENT_FOREIGN_KEY,
                    ID_PICTOGRAM_FOREIGN_KEY
            );
}
