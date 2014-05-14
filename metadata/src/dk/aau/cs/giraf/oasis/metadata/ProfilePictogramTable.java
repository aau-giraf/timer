package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class ProfilePictogramTable {

    public static final String TABLE_NAME = "profile_pictogram";
    public static final String COLUMN_IDPROFILE = "profile_id";
    public static final String COLUMN_IDPICTOGRAM = "pictogram_id";

    public static final ForeignKeyDefinition ID_PROFILE_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_IDPROFILE, 
                    ProfileTable.TABLE_NAME, 
                    ProfileTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_PICTOGRAM_FOREING_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_IDPICTOGRAM, 
                    PictogramTable.TABLE_NAME, 
                    PictogramTable.COLUMN_ID,
                    OnDeleteCascade
            );


    public static final TableDefinition PROFILE_PICTOGRAM_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDPROFILE, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDPICTOGRAM, Integer, NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_PROFILE_FOREIGN_KEY,
                    ID_PICTOGRAM_FOREING_KEY
            }
    );

    public static final MultiTableExport PROFILE_PICTOGRAM = 
            new MultiTableExport(
                    "profile_with_pictogram",
                    ID_PROFILE_FOREIGN_KEY,
                    ID_PICTOGRAM_FOREING_KEY
            );
}
