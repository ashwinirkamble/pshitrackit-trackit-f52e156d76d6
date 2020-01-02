package com.premiersolutionshi.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.domain.IssueComments;

public class JsonUtils {
    private static final String LINE_BREAK = System.getProperty("line.separator");

    /**
     * Code was from:
     * https://stackoverflow.com/questions/1042798/retrieving-the-inherited-attribute-names-values-using-java-reflection
     * 
     * @param fields
     * @param type
     * @return
     */
    protected static List<Field> getAllFields(Class<?> type) {
        List<Field> allFields = getAllFields(new ArrayList<Field>(), type);
        Collections.reverse(allFields);
        return allFields;
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    /**
     * This code was copy-pasted from
     * "https://coderwall.com/p/wrqcsg/java-reflection-get-all-methods-in-hierarchy"
     * 
     * @param objectClass
     * @return Array of all methods.
     */
    private static Method[] getAllMethodsInHierarchy(Class<?> objectClass) {
        Set<Method> allMethods = new HashSet<Method>();
        Method[] declaredMethods = objectClass.getDeclaredMethods();
        Method[] methods = objectClass.getMethods();
        if (objectClass.getSuperclass() != null) {
            Class<?> superClass = objectClass.getSuperclass();
            Method[] superClassMethods = getAllMethodsInHierarchy(superClass);
            allMethods.addAll(Arrays.asList(superClassMethods));
        }
        allMethods.addAll(Arrays.asList(declaredMethods));
        allMethods.addAll(Arrays.asList(methods));
        List<Method> allMethodsList = new ArrayList<>(allMethods);
        Collections.reverse(allMethodsList);
        return allMethodsList.toArray(new Method[allMethodsList.size()]);
    }

    private static Map<String, Method> getMethodMap(Class<?> clazz) {
        Map<String, Method> map = new HashMap<String, Method>();
        Method[] allMethodsInHierarchy = getAllMethodsInHierarchy(clazz);
        for (Method method : allMethodsInHierarchy) {
            map.put(method.getName(), method);
        }
        return map;
    }

    public static String toJson(Object object) {
        return toJson(object, true);
    }

    public static String toJson(Object object, boolean printSubDomains) {
        if (object == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        Class<?> clazz = object.getClass();
        Map<String, Method> methodMap = getMethodMap(clazz);
        result.append("{");

        // determine fields declared in this class only (no fields of superclass)
        List<Field> fields = getAllFields(clazz);
        // print field names paired with their values
        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> type = field.getType();
            String typeName = type.getName();
            //avoid printing these two properties.
            if (fieldName.equals("multipartRequestHandler")
                || fieldName.equals("servlet")
                || fieldName.equals("action")
            ) {
                continue;
            }
            Method method = getMethodByName(methodMap, typeName, fieldName);
            if (method != null) {
                Object fieldValue = getFieldValue(object, method, field);
                if (fieldValue != null) {
                    if (typeName.startsWith("java.lang.String")) {
                        result.append(fieldName).append(":\"").append(fieldValue.toString()).append("\",");
                    }
                    else if (typeName.startsWith("java.util.") //Date
                        || typeName.startsWith("java.time.") //LocalDate, LocalDateTime
                    ) {
                        result.append(fieldName).append(": new Date('").append(fieldValue.toString()).append("'),");
                    }
                    else if (typeName.startsWith("java.lang.")//Integer, Double, Float etc.
                        || typeName.equals("int")
                        || typeName.equals("double")
                        || typeName.equals("float")
                        || typeName.equals("long")
                        || typeName.equals("boolean")) {
                        result.append(fieldName).append(":").append(fieldValue.toString()).append(",");
                    }
                    else if (typeName.startsWith("java.util.ArrayList")
                            || typeName.startsWith("java.util.List")
                        ) {
                            appendArrayListOutput(result, printSubDomains, fieldName, fieldValue);
                        }
                    else {
                        if (printSubDomains) {
                            String output;
                            if (fieldValue instanceof BaseDomain) {
                                BaseDomain baseDomain = (BaseDomain) fieldValue;
                                output = toJson(baseDomain);
                            }
                            else {
                                output = "\"" + fieldValue.toString() + "\""; 
                            }
                            result.append(fieldName).append(":").append(escapeJs(output)).append(",");
                        }
                    }
                }
            }
        }
        return result.append("}").toString();
    }

    protected static String escapeJs(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (!StringUtils.isEmpty(str)) {
                return "\"" + str.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "&quot;").replaceAll("'", "\\\\\'") + "\"";
            }
            return "";
        }
        return obj.toString();

    }
    private static void appendArrayListOutput(StringBuilder result, boolean printSubDomains, String fieldName, Object invoke) {
        ArrayList<? extends Object> list = (ArrayList<?>) invoke;
        result.append(fieldName).append(":[");
        if (list != null && !list.isEmpty()) {
            if (printSubDomains) {
                result.append(LINE_BREAK);
                if (list.get(0) instanceof BaseDomain) {
                    BaseDomain baseDomain = (BaseDomain) list.get(0);
                    result.append(toJson(baseDomain));
                    @SuppressWarnings("unchecked")
                    ArrayList<? extends BaseDomain> arrayList = (ArrayList<BaseDomain>) list;
                    for (int i = 1; i < arrayList.size(); i++) {
                        BaseDomain domain = arrayList.get(i);
                        result.append(",");
                        result.append(toJson(domain));
                    }
                }
            }
            else {
                int size = list == null ? 0 : list.size();
                result.append("size=").append(size);
            }
        }
        result.append("]").append(LINE_BREAK);
    }

    private static Object getFieldValue(Object object, Method method, Field field) {
        Object invoke = null;
        if (method != null && method.getParameters().length == 0) {
            try {
                invoke = method.invoke(object);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                //System.out.println(ex);
            }
        }
        return invoke;
    }

    private static Method getMethodByName(Map<String, Method> methodMap, String typeName, String fieldName) {
        String getOrIs = typeName == "boolean" ? "is" : "get";
        String getMethodName = getOrIs + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1);
        Method method = methodMap.get(getMethodName);
        return method;
    }


    public static void main(String[] args) {
//        ConfiguredSystem cs = new ConfiguredSystem();
//        cs.setShipFk(1);
//        cs.setAccessVersionHistory("asdfsadfasdf");
//        cs.setCreatedBy("asdfsd");
//        cs.setLastUpdatedDate(LocalDateTime.now());
//        Laptop laptop = new Laptop();
//        laptop.setComputerName("COMPUTER NAME");
//        cs.setLaptop(laptop);
//        System.out.println(cs.toString(0, false));
        Issue issue = new Issue();
        issue.setConfiguredSystem(new ConfiguredSystem());
//        issue.setShip(new Ship());
        IssueComments comments = new IssueComments();
        comments.setComments("asdf3e8f8sadfas");
        issue.getIssueCommentList().add(comments);
        IssueComments comments2 = new IssueComments();
        comments2.setComments("asdf3e8f8sadfas");
        issue.getIssueCommentList().add(comments2);
        issue.getIssueCommentList().add(comments2);
        System.out.println(issue.toString(0, false));
    }
}
