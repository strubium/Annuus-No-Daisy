//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.cao.awa.annuus.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOUtil {

    public static void write(OutputStream output, InputStream input) throws IOException {
        output.write(input.readAllBytes());
        output.close();
        input.close();
    }

    public static void write(OutputStream output, byte[] input) throws IOException {
        output.write(input);
        output.close();
    }

    public static void write(Writer writer, String input) throws IOException {
        writer.write(input);
        writer.close();
    }

    public static String read(Reader input) throws IOException {
        char[] chars = new char[4096];
        StringBuilder builder = new StringBuilder();

        int length;
        while((length = input.read(chars)) != -1) {
            builder.append(chars, 0, length);
        }

        input.close();
        return builder.toString();
    }
}
