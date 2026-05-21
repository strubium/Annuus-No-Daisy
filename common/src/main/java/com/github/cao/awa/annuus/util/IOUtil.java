//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.cao.awa.annuus.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOUtil {
    public IOUtil() {
    }

    public static void write(OutputStream output, InputStream input) throws IOException {
        output.write(input.readAllBytes());
        output.close();
        input.close();
    }

    public static void write(OutputStream output, InputStream input, byte[] buffer) throws IOException {
        int length;
        while((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }

        output.close();
        input.close();
    }

    public static void write(OutputStream output, byte[] input) throws IOException {
        output.write(input);
        output.close();
    }

    public static void write(OutputStream output, String input) throws IOException {
        write0(output, input);
        output.close();
    }

    public static void write0(OutputStream output, String input) throws IOException {
        output.write(input.getBytes());
    }

    public static void write(Writer writer, String input) throws IOException {
        write0(writer, input);
        writer.close();
    }

    public static void write0(Writer writer, String input) throws IOException {
        writer.write(input);
    }

    public static void write0(OutputStream writer, byte[] input) throws IOException {
        writer.write(input);
    }

    public static void write(Writer writer, Reader reader) throws IOException {
        char[] chars = new char[4096];

        int length;
        while((length = reader.read(chars)) != -1) {
            writer.write(chars, 0, length);
        }

        writer.close();
        reader.close();
    }

    public static void write(Writer writer, char[] input) throws IOException {
        writer.write(input);
        writer.close();
    }

    public static String read(InputStream input) throws IOException {
        return read((Reader)(new InputStreamReader(input)));
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

    public static int read0(InputStream input, byte[] bytes) throws IOException {
        return input.read(bytes);
    }

    public static String readLine(BufferedReader input) throws IOException {
        return input.readLine();
    }

    public static byte[] readBytes(InputStream input) throws IOException {
        return input.readAllBytes();
    }

    public static byte[] readBytes(Reader input) throws IOException {
        char[] chars = new char[4096];
        StringBuilder builder = new StringBuilder();

        int length;
        while((length = input.read(chars)) != -1) {
            builder.append(chars, 0, length);
        }

        input.close();
        return builder.toString().getBytes();
    }

    public static char[] readChars(InputStream input) throws IOException {
        return readChars((Reader)(new InputStreamReader(input)));
    }

    public static char[] readChars(Reader input) throws IOException {
        char[] chars = new char[4096];
        StringBuilder builder = new StringBuilder();

        int length;
        while((length = input.read(chars)) != -1) {
            builder.append(chars, 0, length);
        }

        input.close();
        return builder.toString().toCharArray();
    }

    public static void copy(String from, String to) throws IOException {
        copy(new File(from), new File(to));
    }

    public static void copy(File from, File to) throws IOException {
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(from));
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(to));
        byte[] buff = new byte[8192];

        int length;
        while((length = input.read(buff, 0, buff.length)) != -1) {
            output.write(buff, 0, length);
        }

        input.close();
        output.close();
    }
}
