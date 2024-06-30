package com.yangyi.project.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.apache.mina.filter.codec.textline.TextLineEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class MinaCodeFactory implements ProtocolCodecFactory {

    private final TextLineEncoder encoder;
    private final TextLineDecoder decoder;

    public MinaCodeFactory() {
        encoder = new TextLineEncoder(Charset.forName("gb2312"), LineDelimiter.UNIX);
        decoder = new TextLineDecoder(Charset.forName("gb2312"), LineDelimiter.AUTO);
    }


    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
