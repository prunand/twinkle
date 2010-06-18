/*
 * Copyright 2010 Andrew Prunicki
 * 
 * This file is part of Twinkle.
 * 
 * Twinkle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Twinkle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Twinkle.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.prunicki.suzuki.twinkle.db;

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
