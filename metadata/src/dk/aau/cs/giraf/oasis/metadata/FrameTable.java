package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.NotNull;
import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.Unique;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.Integer;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.Text;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.OnDeleteCascade;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.OnDeleteSetNull;

public class FrameTable {
    public static final String TABLE_NAME = "frame";
    public static final String COLUMN_ID = "frame_id";
    public static final String COLUMN_SEQUENCEID = "sequence_id";
    public static final String COLUMN_POSX = "frame_pos_x";
    public static final String COLUMN_POSY = "frame_pos_y";
    public static final String COLUMN_PICTOGRAMID = "pictogram_id";
    public static final String COLUMN_NESTEDSEQUENCEID = "nested_sequence_id";



    public static final ForeignKeyDefinition ID_PICTOGRAM_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_PICTOGRAMID,
                    PictogramTable.TABLE_NAME,
                    PictogramTable.COLUMN_ID,
                    OnDeleteSetNull
            );
    public static final ForeignKeyDefinition ID_SEQUENCE_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_SEQUENCEID,
                    SequenceTable.TABLE_NAME,
                    SequenceTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_NESTED_SEQUENCE_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_NESTEDSEQUENCEID,
                    SequenceTable.TABLE_NAME,
                    SequenceTable.COLUMN_ID,
                    OnDeleteSetNull
            );
    
    public static final TableDefinition FRAME_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(COLUMN_ID),
            new ForeignKeyDefinition[]{
                    ID_PICTOGRAM_FOREIGN_KEY,
                    ID_SEQUENCE_FOREIGN_KEY,
                    ID_NESTED_SEQUENCE_FOREIGN_KEY
            },
            new ColumnDefinition(COLUMN_SEQUENCEID,Integer, NotNull),
            new ColumnDefinition(COLUMN_POSX,Integer, NotNull),
            new ColumnDefinition(COLUMN_POSY,Integer,NotNull),
            new ColumnDefinition(COLUMN_PICTOGRAMID,Integer),
            new ColumnDefinition(COLUMN_NESTEDSEQUENCEID,Integer, Unique)
    );
}
