package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class PictogramTable {

    public static final String TABLE_NAME = "pictogram";
    public static final String COLUMN_ID = "pictogram_id";
    public static final String COLUMN_NAME = "pictogram_name";
    public static final String COLUMN_PUB = "pictogram_ispublic";
    public static final String COLUMN_IMAGEDATA = "pictogram_image";
    public static final String COLUMN_SOUNDDATA = "pictogram_sound";
    public static final String COLUMN_EDITABLEIMAGE = "pictogram_editable_image";
    public static final String COLUMN_INLINETEXT = "pictogram_inline_text";
    public static final String COLUMN_AUTHOR = "pictogram_author";

    public static final ForeignKeyDefinition ID_FOREIGN_KEY = 
            new ForeignKeyDefinition(
                    COLUMN_AUTHOR,
                    UserTable.TABLE_NAME, 
                    UserTable.COLUMN_ID,
                    OnDeleteSetNull
            );
    
    
    public static final TableDefinition PICTOGRAM_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(COLUMN_ID),
            new ForeignKeyDefinition[]
                    {
                            ID_FOREIGN_KEY
                    },
            new ColumnDefinition(COLUMN_NAME, Text, NotNull),
            new ColumnDefinition(COLUMN_PUB, Integer, NotNull),
            new ColumnDefinition(COLUMN_IMAGEDATA, Blob),
            new ColumnDefinition(COLUMN_SOUNDDATA, Blob),
            new ColumnDefinition(COLUMN_EDITABLEIMAGE, Blob),
            new ColumnDefinition(COLUMN_INLINETEXT, Text),
            new ColumnDefinition(COLUMN_AUTHOR, Integer)
    );
}
