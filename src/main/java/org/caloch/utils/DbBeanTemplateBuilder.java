package org.caloch.utils;

public class DbBeanTemplateBuilder {

    public String header = "package org.caloch.beans;\n" +
            "\n" +
            "import org.caloch.core.Column;\n" +
            "import org.caloch.core.Entity;\n" +
            "import org.caloch.core.JsonIgnore;\n";


    public String getClassOpener(String tableName) {
        return "public class " + tableName + " extends Entity {\n";
    }

    public String getFieldString(String fieldName, String fieldType) {
        return "public " + resolveDbTypeToJava(fieldType) + " " + fieldName + ";\n";
    }

    public String classCloser = "}";

    public String resolveDbTypeToJava(String dbType) {
        if (dbType.equals("int")) {
            return "int";
        } else if (dbType.equals("bigint")) {
            return "long";
        }else if (dbType.equals("varchar")||dbType.equals("char")) {
            return "String";
        }else if (dbType.equals("bit")) {
            return "boolean";
        }
        return "String";
    }
}
