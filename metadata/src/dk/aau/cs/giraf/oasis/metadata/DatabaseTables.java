package dk.aau.cs.giraf.oasis.metadata;


import java.util.ArrayList;
import java.util.List;

public class DatabaseTables {

    public static final String AUTHORITY = "dk.aau.cs.giraf.oasis.localdb.provider";

    public static final TableDefinition Definitions[] = new TableDefinition[]
            {
                    UserTable.USER_TABLE,
                    DepartmentTable.DEPARTMENT_TABLE,
                    ProfileTable.PROFILE_TABLE,
                    PictogramTable.PICTOGRAM_TABLE,
                    TagTable.TAG_TABLE,
                    CategoryTable.CATEGORY_TABLE,
                    ApplicationTable.APPLICATION_TABLE,
                    AdminOfTable.ADMIN_OF_TABLE,
                    DepartmentPictogramTable.DEPARTMENT_PICTOGRAM_TABLE,
                    ProfilePictogramTable.PROFILE_PICTOGRAM_TABLE,
                    DepartmentApplicationTable.DEPARTMENT_APPLICATION_TABLE,
                    ProfileApplicationTable.PROFILE_APPLICATION_TABLE,
                    PictogramTagTable.PICTOGRAM_TAG_TABLE,
                    PictogramCategoryTable.PICTOGRAM_CATEGORY_TABLE,
                    ProfileCategoryTable.PROFILE_CATEGORY_TABLE,
                    GuardianOfTable.GUARDIAN_OF_TABLE,
                    FrameTable.FRAME_TABLE,
                    SequenceTable.SEQUENCE_TABLE,
                    FramePictogramTable.FRAME_PICTOGRAM_TABLE
            };

    public static List<String> getCreationStrings(){
        ArrayList<String> list = new ArrayList<String>();

        for(TableDefinition td : Definitions){
            list.add(td.getSQLCreateString());
        }

        return list;
    }
    
    public static List<String> getDeletionStrings(){
        ArrayList<String> list = new ArrayList<String>();

        for(TableDefinition td : Definitions){
            list.add(td.getSQLDropString());
        }

        return list;
    }
    
    public static List<ExportDefinition> getExported(){
        ArrayList<ExportDefinition> list = new ArrayList<ExportDefinition>();
        
        for(TableDefinition td: Definitions){
            list.add(td.ListExport);
            list.add(td.RowExport);
        }
        
        list.add(AdminOfTable.DEPARTMENT_ADMIN);
        list.add(DepartmentApplicationTable.DEPARTMENT_APPLICATION);
        list.add(DepartmentPictogramTable.DEPARTMENT_PICTOGRAM);
        list.add(GuardianOfTable.GUARDIAN_CHILD);
        list.add(ProfileApplicationTable.PROFILE_APPLICATION);
        list.add(ProfileCategoryTable.PROFILE_CATEGORY);
        list.add(ProfilePictogramTable.PROFILE_PICTOGRAM);
        list.add(PictogramCategoryTable.PICTOGRAM_CATEGORY);
        list.add(PictogramTagTable.PICTOGRAM_TAG);
        list.add(FramePictogramTable.FRAME_PICTOGRAM);
        
        return list;
    }
}
