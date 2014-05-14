package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class PictogramTagTable {

    public static final String TABLE_NAME = "pictogram_tag";
    public static final String COLUMN_IDPICTOGRAM = "pictogram_id";
    public static final String COLUMN_IDTAG = "tag_id";

    public static final ForeignKeyDefinition ID_PICTOGRAM_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDPICTOGRAM,
                    PictogramTable.TABLE_NAME,
                    PictogramTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_TAG_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDTAG,
                    TagTable.TABLE_NAME,
                    TagTable.COLUMN_ID,
                    OnDeleteCascade
            );


    public static final TableDefinition PICTOGRAM_TAG_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDPICTOGRAM, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDTAG, Integer, NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_PICTOGRAM_FOREIGN_KEY,
                    ID_TAG_FOREIGN_KEY
            }
    );

    public static final MultiTableExport PICTOGRAM_TAG =
            new MultiTableExport(
                    "pictogram_with_tag",
                    ID_PICTOGRAM_FOREIGN_KEY,
                    ID_TAG_FOREIGN_KEY
            );
}
