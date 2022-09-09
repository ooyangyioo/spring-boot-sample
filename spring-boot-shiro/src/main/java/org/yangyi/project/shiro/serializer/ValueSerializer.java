package org.yangyi.project.shiro.serializer;

import org.apache.shiro.io.ClassResolvingObjectInputStream;
import org.apache.shiro.io.SerializationException;
import org.apache.shiro.io.Serializer;

import java.io.*;

public class ValueSerializer implements Serializer<Object> {

    private static final byte[] EMPTY_ARRAY = new byte[0];

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (null == o) {
            return EMPTY_ARRAY;
        }

        if (!(o instanceof Serializable)) {
            throw new SerializationException(o.getClass().getName() + " 未实现 Serializable 接口");
        }
        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bytesOutputStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            return bytesOutputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(o.getClass().getName() + "序列化异常", e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (null == bytes || bytes.length == 0) {
            return null;
        }

        ByteArrayInputStream bytesInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ClassResolvingObjectInputStream(bytesInputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            throw new SerializationException("反序列化异常", e);
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
