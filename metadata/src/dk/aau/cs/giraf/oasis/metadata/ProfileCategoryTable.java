package dk.aau.cs.giraf.oasis.metadata;

import static dk.aau.cs.giraf.oasis.metadata.ColumnOption.*;
import static dk.aau.cs.giraf.oasis.metadata.ColumnType.*;
import static dk.aau.cs.giraf.oasis.metadata.ForeignKeyOption.*;

public class ProfileCategoryTable {

    public static final String TABLE_NAME = "profile_category";
    public static final String COLUMN_IDPROFILE = "profile_id";
    public static final String COLUMN_IDCATEGORY = "category_id";

    public static final ForeignKeyDefinition ID_PROFILE_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDPROFILE,
                    ProfileTable.TABLE_NAME,
                    ProfileTable.COLUMN_ID,
                    OnDeleteCascade
            );
    public static final ForeignKeyDefinition ID_CATEGORY_FOREIGN_KEY =
            new ForeignKeyDefinition(
                    COLUMN_IDCATEGORY,
                    CategoryTable.TABLE_NAME,
                    CategoryTable.COLUMN_ID,
                    OnDeleteCascade
            );


    public static final TableDefinition PROFILE_CATEGORY_TABLE = new TableDefinition(
            TABLE_NAME,
            new PrimaryKeyDefinition(
                    new ColumnDefinition(COLUMN_IDPROFILE, Integer, NotNull),
                    new ColumnDefinition(COLUMN_IDCATEGORY, Integer, NotNull)
            ),
            new ForeignKeyDefinition[]{
                    ID_PROFILE_FOREIGN_KEY,
                    ID_CATEGORY_FOREIGN_KEY
            }
    );

    public static final MultiTableExport PROFILE_CATEGORY =
            new MultiTableExport(
                    "profile_with_category",
                    ID_PROFILE_FOREIGN_KEY,
                    ID_CATEGORY_FOREIGN_KEY
            );
}
