package com.ben.p_01_aidl_server;

import com.ben.p_01_aidl_server.Person;

interface IPassObjType {
    String passPerson(in Person person);
}