package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class GuardianOfTable {

    public static final String TABLE_NAME = "guardian_of";
    public static final String COLUMN_IDGUARDIAN = "guardian_id";
    public static final String COLUMN_IDCHILD = "child_id";

    public static final ForeignKeyDefinition ID_GUARDIAN_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDGUARDIAN,
                    ProfileTable.TABLE_NAME,
                    ProfileTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_CHILD_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDCHILD,
                    ProfileTable.TABLE_NAME,
                    ProfileTable.COLUMN_ID,
                    OnDeleteCascade
            );

    public static final TableDefinition GUARDIAN_OF_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDGUARDIAN, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDCHILD, Integer, NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_GUARDIAN_FOREIGN_KEY,
                    ID_CHILD_FOREIGN_KEY
            }
    );

    public static final MultiTableExport GUARDIAN_CHILD =
            new MultiTableExport(
                    "guardian_with_child",
                    ID_CHILD_FOREIGN_KEY
            );
}
