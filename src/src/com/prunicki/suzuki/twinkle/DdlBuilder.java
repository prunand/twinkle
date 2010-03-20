package com.prunicki.suzuki.twinkle;

public class DdlBuilder {
    public static final int TABLE_KEY_INDEX = 0;
    public static final String TABLE_KEY_NAME = "_id";
    
    public static final int TEXT_TYPE = 0;
    public static final int INTEGER_TYPE = 1;
    
    public static ColumnDef createColumnDef(String name, int type, boolean nullable) {
        convertType(type);  //Validate the type
        return new ColumnDef(name, type, nullable);
    }
    
    public static final String createDDL(String tableName, ColumnDef[] columnDefs) {
        String typeStr = null;
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        sb.append('\"');
        sb.append(tableName);
        sb.append("\" (");
        sb.append('\"');
        sb.append(TABLE_KEY_NAME);
        sb.append('\"');
        sb.append(" integer primary key autoincrement not null,\n");
        for (int i = 0; i < columnDefs.length; i++) {
            typeStr = convertType(columnDefs[i].type);
            sb.append('\"');
            sb.append(columnDefs[i].name);
            sb.append("\" ");
            sb.append(typeStr);
            if (!columnDefs[i].nullable) {
                sb.append(" not null");
            }
            if (i != columnDefs.length - 1) {
                sb.append(",\n");
            }
        }
        sb.append(");");
        
        return sb.toString();
    }
    
    private static String convertType(int type) {
        String typeStr = null;
        switch(type) {
            case TEXT_TYPE:
                typeStr = "text";
                break;
            case INTEGER_TYPE:
                typeStr = "integer";
                break;
            default:
                throw new IllegalArgumentException("Unrecognized type " + type);
        }
        
        return typeStr;
    }
    
    public static class ColumnDef {
        final String name;
        final int type;
        final boolean nullable;
        
        private ColumnDef(String columnName, int type, boolean nullable) {
            this.name = columnName;
            this.type = type;
            this.nullable = nullable;
        }
    }
}
