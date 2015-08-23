package com.youthor.zz.common.util;

import junit.framework.TestCase;

import com.youthor.zz.common.xml.XmlElement;

public class XmlUtilTest extends TestCase {
    public void testMergeFile () {
        XmlElement xmlElement = XmlUtil.parseFile("E:\\workspace\\zz\\sourcecode\\WebContent\\web.xml");
        FileUtil.stringToFile("c:/", "merge_1.xml", xmlElement.toString());
        XmlUtil.mergeFile(xmlElement, "E:\\workspace\\zz\\sourcecode\\WebContent\\web_1.xml", true);
        FileUtil.stringToFile("c:/", "merge.xml", xmlElement.toString());
    }
}
