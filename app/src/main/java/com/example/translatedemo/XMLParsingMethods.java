package com.example.translatedemo;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * author : xumingming
 * data : 2022/3/30
 * description ：
 * email : 835683840@qq.com
 */
public class XMLParsingMethods {



    public static List<StringKey> readXmlByPull(InputStream inputStream){
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream,"UTF-8");
            int eventType = parser.getEventType();

            StringKey currenPerson = null;
            List<StringKey> persons = null;

            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:/**【文档开始事件】**/
                        persons = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:/**【元素（即标签）开始事件】**/
                        String name = parser.getName();
                        if(name.equals("string")){
                            currenPerson = new StringKey();
                            currenPerson.setKey(parser.getAttributeValue(0));
                        }
                        break;
                    case XmlPullParser.END_TAG:/**【元素结束事件】**/
                        if(parser.getName().equalsIgnoreCase("string") && currenPerson != null){
                            persons.add(currenPerson);
                            currenPerson = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return persons;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
