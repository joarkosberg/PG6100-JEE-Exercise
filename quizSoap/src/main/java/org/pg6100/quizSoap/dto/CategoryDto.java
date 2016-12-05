package org.pg6100.quizSoap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryDto {

    @XmlElement
    public Long id;

    @XmlElement
    public String name;

    public CategoryDto(){}

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
