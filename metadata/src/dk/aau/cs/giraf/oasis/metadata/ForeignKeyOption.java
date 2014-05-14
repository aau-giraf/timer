package dk.aau.cs.giraf.oasis.metadata;

enum ForeignKeyOption {
    OnDeleteRestrict("ON DELETE RESTRICT"),
    OnDeleteNoAction("ON DELETE NO ACTION"),
    OnDeleteCascade("ON DELETE CASCADE"),
    OnDeleteSetNull("ON DELETE SET NULL"),
    
    OnUpdateRestrict("ON UPDATE RESTRICT"),
    OnUpdateNoAction("ON UPDATE NO ACTION"),
    OnUpdateCascade("ON UPDATE CASCADE"),
    OnUpdateSetNull("ON UPDATE SET NULL");
    
    
    private final String Option;

    ForeignKeyOption(String option){
        Option = option;
    }

    public String getSqlOption(){
        return Option;
    }
}
