package io.mycat.mcache.model;

/**
 * 协议类型
 * @author liyanjun
 *
 */
public enum Protocol {
    binary,
    negotiating, /* Discovering the protocol */
    ascii; /* arbitrary value. */
}
