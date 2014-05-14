package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class PictogramCategoryTable {

    public static final String TABLE_NAME = "pictogram_category";
    public static final String COLUMN_IDPICTOGRAM= "pictogram_id";
    public static final String COLUMN_IDCATEGORY = "category_id";

    public static final ForeignKeyDefinition ID_PICTOGRAM_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDPICTOGRAM,
                    PictogramTable.TABLE_NAME,
                    PictogramTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_CATEGORY_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDCATEGORY,
                    CategoryTable.TABLE_NAME,
                    CategoryTable.COLUMN_ID,
                    OnDeleteCascade
            );


    public static final TableDefinition PICTOGRAM_CATEGORY_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDPICTOGRAM, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDCATEGORY, Integer,NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_PICTOGRAM_FOREIGN_KEY,
                    ID_CATEGORY_FOREIGN_KEY
            }
    );

    public static final MultiTableExport PICTOGRAM_CATEGORY =
            new MultiTableExport(
                    "pictogram_with_category",
                    ID_PICTOGRAM_FOREIGN_KEY,
                    ID_CATEGORY_FOREIGN_KEY
            );
}
