package com.example.smartescortapp.netty;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;

/**
 * 字节数组缓存，用来存储字节数组，在拆包时用来存储未读的字节数组
 * @author 章可政
 * @date 2021/2/6 23:46
 */
public class BufferMemory {
    /**
     *
     */
    private final ByteArrayBuilder builder = new ByteArrayBuilder();

    /**
     * 允许偏移来将builder中的数组读进一个len长度的数组
     * @param data 目标数组
     * @param offset 偏移量
     * @param len 长度
     */
    public void Write(byte[] data, int offset, int len) {
        builder.write(data, offset, len);
    }

    /**
     * 取出builder中的字节数组
     * @return builder中的字节数组
     */
    public byte[] toByteArray() {
        return builder.toByteArray();
    }

    /**
     * 清空builder中的字节数组
     */
    public void reSet() {
        builder.reset();
    }


    /**
     * 将一个byte数组读进builder中
     * @param data 目标数组
     * @param offset 偏移量
     * @param len 长度
     */
    public void Read(byte[] data, int offset, int len) {
        byte[] bytes = toByteArray();
        if (len >= 0) System.arraycopy(bytes, offset, data, 0, len);
    }

    /**
     * builder中的字节数组长度
     * @return 数组的长度
     */
    public int length() {
        return toByteArray().length;
    }


}
