package org.yangyi.project.shiro.serializer;

import org.apache.shiro.io.SerializationException;
import org.apache.shiro.io.Serializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringSerializer implements Serializer<String> {

    private final Charset charset;

    public StringSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public StringSerializer(Charset charset) {
        this.charset = charset;
    }

    @Override
    public byte[] serialize(String s) throws SerializationException {
        return (null == s ? null : s.getBytes(charset));
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return (null == bytes ? null : new String(bytes, charset));
    }

}
