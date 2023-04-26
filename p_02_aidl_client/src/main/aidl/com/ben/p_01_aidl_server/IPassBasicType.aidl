package com.ben.p_01_aidl_server;

interface IPassBasicType {
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    int passIntType(int data);
    String passStrType(in String data);

}