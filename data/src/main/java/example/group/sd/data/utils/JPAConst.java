package example.group.sd.data.utils;

public interface JPAConst {
    String SCHEMA = "public";
    String JSONB = "jsonb";
    String PGUUID = "pg-uuid";
    Long DEFAULT_ORDERED_VALUE = Long.valueOf(999999L);
    String DEFAULT_ORDERED_COLUMN_DEFINITION = "bigint default 999999";

    public interface D {
        String BaseObject = "BASEOBJ";
        String BaseType = "BASETYPE";
        String Classifier = "CLASSIFIER";
        String ClassifierItem = "CLASSFITEM";
        String ObjectLinkType = "OBJLNKTYPE";
        String Tag = "TAG";
    }

    public interface Schemas {
        String DICTIONARY = "dict";
        String BUSINESS = "public";
        String SECURITY = "security";
    }
}
