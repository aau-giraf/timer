package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class ProfileApplicationTable {

    public static final String TABLE_NAME = "profile_application";
    public static final String COLUMN_IDPROFILE = "profile_id";
    public static final String COLUMN_IDAPPLICATION = "application_id";
    public static final String COLUMN_SETTINGS = "profile_application_settings";

    public static final ForeignKeyDefinition ID_PROFILE_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDPROFILE,
                    ProfileTable.TABLE_NAME,
                    ProfileTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_APPLICATION_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDAPPLICATION,
                    ApplicationTable.TABLE_NAME,
                    ApplicationTable.COLUMN_ID,
                    OnDeleteCascade
            );

    public static final TableDefinition PROFILE_APPLICATION_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDPROFILE, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDAPPLICATION, Integer, NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_PROFILE_FOREIGN_KEY,
                    ID_APPLICATION_FOREIGN_KEY
            },
            new ColumnDefinition(COLUMN_SETTINGS, Blob)
    );

    public static final MultiTableExport PROFILE_APPLICATION =
            new MultiTableExport(
                    "profile_with_application",
                    ID_PROFILE_FOREIGN_KEY,
                    ID_APPLICATION_FOREIGN_KEY
            );
}
