package com.gman.telegram.bot;

public interface DocumentMarshaller {

    <T> String marshal(T document);

    <T> T unmarshal(String str);

    <T> T unmarshal(String str, Class clazz);
}