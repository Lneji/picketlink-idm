//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.01 at 07:21:12 odp. CET 
//


package org.picketlink.idm.impl.configuration.jaxb2.generated;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SystemPropertiesSubstitutionAdapter
    extends XmlAdapter<String, String>
{


    public String unmarshal(String value) {
        return (org.picketlink.idm.impl.configuration.jaxb2.SystemPropertiesSubstitutionConverter.substituteSystemProperty(value));
    }

    public String marshal(String value) {
        return (org.picketlink.idm.impl.configuration.jaxb2.SystemPropertiesSubstitutionConverter.printValue(value));
    }

}