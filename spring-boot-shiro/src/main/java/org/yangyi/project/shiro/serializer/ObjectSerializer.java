package org.yangyi.project.shiro.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializer implements RedisSerializer<Object> {

    public static final int BYTE_ARRAY_OUTPUT_STREAM_SIZE = 128;

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        byte[] result = new byte[0];
        if (null == o)
            return result;

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(BYTE_ARRAY_OUTPUT_STREAM_SIZE);
        if (!(o instanceof Serializable)) {
            throw new SerializationException(o.getClass().getName() + " 未实现Serializable接口");
        }

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            result = byteStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(o + " 序列化错误", e);
        }
        return result;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        Object result;
        if (bytes == null || bytes.length == 0)
            return null;
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
            result = objectInputStream.readObject();
        } catch (Exception e) {
            throw new SerializationException("反序列化错误", e);
        }
        return result;
    }

}
