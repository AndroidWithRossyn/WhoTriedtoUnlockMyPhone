package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_smtp;

import java.nio.charset.Charset;

public class WTUPCP_Charsets {

    public static Charset toCharset(Charset charset) {
        return charset == null ? Charset.defaultCharset() : charset;
    }
}
