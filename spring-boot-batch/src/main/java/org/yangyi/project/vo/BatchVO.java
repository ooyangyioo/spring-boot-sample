package org.yangyi.project.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "batch")
public class BatchVO {

    @JacksonXmlProperty(localName = "batchId")
    @JsonProperty("batchId")
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;

    @JacksonXmlProperty(localName = "batchName")
    @JsonProperty("batchName")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
