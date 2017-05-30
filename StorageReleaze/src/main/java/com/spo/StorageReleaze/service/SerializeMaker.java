package com.spo.StorageReleaze.service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Created by Antoha12018 on 18.05.2017.
 */
public class SerializeMaker {

    public static <T> String serializeToXML(T object) {

        String serializedString;
        try {
            XStream xStream = new XStream(new DomDriver());
            serializedString = xStream.toXML(object);

        } catch (XStreamException e) {
            e.printStackTrace();
            return null;
        }
        return serializedString;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserializeFromXML(String stringForDes) {
        T object;
        try {
            XStream xStream = new XStream(new DomDriver());
            object = (T) xStream.fromXML(stringForDes);
        } catch (XStreamException exp) {
            exp.printStackTrace();
            return null;
        }
        return object;
    }

}
