package com.study.reflect;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.*;
@Getter
@Setter
public class Test {
    private int age;
    private String name;
    private int testint;

    public Test(int age) {
        this.age = age;
    }

    public Test(int age, String name) {
        this.age = age;
        this.name = name;
        System.out.println("age:"+age+",name:"+name);
    }

    private Test(String name) {
        this.name = name;
        System.out.println("name:"+name);
    }

    public Test() {
        System.out.println("Test类无参构造方法");
    }
    private void welcome(String tips){
        System.out.println(tips);
    }

    /**
     * 获取属性名数组
     * */
    private static String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
//            System.out.println(fields[i].getType());
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    /* 根据属性名获取属性值
     * 通过属性的get方法
     * */
    private static Object getFieldValueByName(String fieldName, Object o) {

        try {
            //获取属性名称首字母转大写
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            //拼接方法名称
            String getter = "get" + firstLetter + fieldName.substring(1);
            //获取get方法
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * 反射调用类,方法名，类型，参数
     */
//    public static void main(String[] args) {
//        Test test = new Test();
//        Class c4 = test.getClass();
//        System.out.println(c4);
//        Constructor[] constructors;
//        //通过getDeclaredConstructors可以返回类的所有构造方法
//        constructors = c4.getDeclaredConstructors();
//        System.out.println(constructors);
//        for(int i = 0;i < constructors.length;i++){
//            //constructors[i].getModifiers()可以得到构造方法的类型
//            System.out.println(Modifier.toString(constructors[i].getModifiers())+"参数:");
//            //getParameterTypes可以得到构造方法的所有参数,返回的是一个Class[]数组
//            Class[] parametertypes = constructors[i].getParameterTypes();
//            for(int j = 0; j < parametertypes.length; j++){
//                System.out.print(parametertypes[j].getName() + " ");
//            }
//
//        }
//    }

    /**
     * 反射共有构造方法
     */
//    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        Test test = new Test();
//        //调用构造方法
//        Class c4 = test.getClass();
//        System.out.println(c4);
//        Constructor constructors;
//        Class[] p = {int.class,String.class};
//        constructors = c4.getDeclaredConstructor(p);
//        constructors.newInstance(24,"HuangLinqing");
//        System.out.println("");
//    }

    /**
     * 反射调用私有构造方法
     * @param args
     */
//    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        Test test = new Test();
//        //调用构造方法
//        Class c4 = test.getClass();
//        System.out.println(c4);
//        Constructor constructors;
//        Class[] p = {String.class};
//        constructors = c4.getDeclaredConstructor(p);
//        constructors.setAccessible(true);
//        constructors.newInstance("HuangLinqing");
//    }

    /**
     * 调用类的私有方法
     */
//    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
//        //不能new生成新的实例,可以用newInstance来生成
//        Class c4 = Test.class;
//        System.out.println(c4);
//        Constructor constructors;
//        Class[] p4 = {String.class};
//        //获取私有方法,name方法名,p4为参数
//        Method method = c4.getDeclaredMethod("welcome",p4);
//        method.setAccessible(true);
//        Object o = "试验反射调用方法";
//        //执行反射方法
//        method.invoke(c4.newInstance(),o);
//    }
    /**
     * 获取类的私有字段并修改值
     */
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Class<?> cls = Class.forName("com.study.reflect.Test");
        Class<?> c4 = cls.getClass();
        //修改字段
        Field field = c4.getDeclaredField("name");
        field.setAccessible(true);
        field.set(c4,"perfect");
        System.out.println("修改字段属性名称："+field.getName());
        System.out.println("修改字段属性值:"+field.get(c4));
        //获取所有属性信息
        Field[] field1 = c4.getDeclaredFields();
        //获取所有属性名称

        StringBuilder sbName = new StringBuilder();
        StringBuilder sbValue = new StringBuilder();
        String[] fielIdName = getFiledName(c4);
        for(int j=0 ; j<fielIdName.length ; j++){     //遍历所有属性
            String name = fielIdName[j];    //获取属性的名字
            Object value = getFieldValueByName(name,c4);
            sbName.append(name);
            sbValue.append(value);
            if(j != fielIdName.length - 1) {
                sbName.append("/");
                sbValue.append("/");
            }
        }
        System.out.println("attribute name:"+sbName.toString());
        System.out.println("attribute value:"+sbValue.toString());

    }
}
