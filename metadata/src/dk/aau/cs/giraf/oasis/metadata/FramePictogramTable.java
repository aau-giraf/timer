package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.NotNull;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.Integer;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.OnDeleteCascade;

public class FramePictogramTable {

    public static final String TABLE_NAME = "frame_pictogram";
    public static final String COLUMN_IDPICTOGRAM= "pictogram_id";
    public static final String COLUMN_IDFRAME = "frame_id";

    public static final ForeignKeyDefinition ID_PICTOGRAM_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDPICTOGRAM,
                    PictogramTable.TABLE_NAME,
                    PictogramTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_FRAME_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDFRAME,
                    FrameTable.TABLE_NAME,
                    FrameTable.COLUMN_ID,
                    OnDeleteCascade
            );

    public static final TableDefinition FRAME_PICTOGRAM_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDFRAME, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDPICTOGRAM, Integer, NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_FRAME_FOREIGN_KEY,
                    ID_PICTOGRAM_FOREIGN_KEY
            }
    );

    public static final MultiTableExport FRAME_PICTOGRAM =
            new MultiTableExport(
                    "frame_with_pictogram",
                    ID_FRAME_FOREIGN_KEY,
                    ID_PICTOGRAM_FOREIGN_KEY
            );
}
