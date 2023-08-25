package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_smtp;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

public class WTUPCP_IOUtils {
    public static final char DIR_SEPARATOR = File.separatorChar;
    public static final String LINE_SEPARATOR;

    static {
        WTUPCP_StringBuilderWriter wTUPCP_StringBuilderWriter = new WTUPCP_StringBuilderWriter(4);
        PrintWriter printWriter = new PrintWriter(wTUPCP_StringBuilderWriter);
        printWriter.println();
        LINE_SEPARATOR = wTUPCP_StringBuilderWriter.toString();
        printWriter.close();
    }

    public static void closeQuietly(InputStream inputStream) {
        closeQuietly((Closeable) inputStream);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    public static String toString(InputStream inputStream) throws IOException {
        return toString(inputStream, Charset.defaultCharset());
    }

    public static String toString(InputStream inputStream, Charset charset) throws IOException {
        WTUPCP_StringBuilderWriter wTUPCP_StringBuilderWriter = new WTUPCP_StringBuilderWriter();
        copy(inputStream, wTUPCP_StringBuilderWriter, charset);
        return wTUPCP_StringBuilderWriter.toString();
    }

    public static void copy(InputStream inputStream, Writer writer, Charset charset) throws IOException {
        copy(new InputStreamReader(inputStream, WTUPCP_Charsets.toCharset(charset)), writer);
    }

    public static int copy(Reader reader, Writer writer) throws IOException {
        long copyLarge = copyLarge(reader, writer);
        if (copyLarge > 2147483647L) {
            return -1;
        }
        return (int) copyLarge;
    }

    public static long copyLarge(Reader reader, Writer writer) throws IOException {
        return copyLarge(reader, writer, new char[4096]);
    }

    public static long copyLarge(Reader reader, Writer writer, char[] cArr) throws IOException {
        long j = 0;
        while (true) {
            int read = reader.read(cArr);
            if (-1 == read) {
                return j;
            }
            writer.write(cArr, 0, read);
            j += (long) read;
        }
    }
}
