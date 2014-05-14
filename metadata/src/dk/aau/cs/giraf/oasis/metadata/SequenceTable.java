package dk.aau.cs.giraf.oasis.metadata;


import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class SequenceTable {
    public static final String TABLE_NAME = "sequence";
    public static final String COLUMN_ID = "sequence_id";
    public static final String COLUMN_PROFILEID = "profile_id";
    public static final String COLUMN_NAME = "sequence_name";
    public static final String COLUMN_SEQUENCETYPE = "sequence_type";
    public static final String COLUMN_PICTOGRAMID = "pictogram_id";


    public static final ForeignKeyDefinition ID_PICTOGRAM_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_PICTOGRAMID,
                    PictogramTable.TABLE_NAME,
                    PictogramTable.COLUMN_ID,
                    OnDeleteSetNull
            );
    public static final ForeignKeyDefinition ID_PROFILE_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_PROFILEID,
                    ProfileTable.TABLE_NAME,
                    ProfileTable.COLUMN_ID,
                    OnDeleteCascade
            );
    
    public static final TableDefinition SEQUENCE_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(COLUMN_ID),
            new ForeignKeyDefinition[]{
                    ID_PICTOGRAM_FOREIGN_KEY,
                    ID_PROFILE_FOREIGN_KEY
            },
            new ColumnDefinition(COLUMN_PROFILEID, Integer, NotNull),
            new ColumnDefinition(COLUMN_NAME, Text, NotNull),
            new ColumnDefinition(COLUMN_SEQUENCETYPE, Integer, NotNull),
            new ColumnDefinition(COLUMN_PICTOGRAMID, Integer)
    );
}
