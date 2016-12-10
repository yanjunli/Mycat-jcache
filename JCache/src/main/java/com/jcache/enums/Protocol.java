/*
 *  文件创建时间： 2016年11月29日
 *  文件创建者: tangww
 *  所属工程: JCache
 *  CopyRights Received EMail Dev. Dept. 21CN 
 *
 *  备注: 
 */
package com.jcache.enums;

/**
 * 
 * 类功能描述：TODO
 *
 * <p> 版权所有：21CN.com
 * <p> 未经本公司许可，不得以任何方式复制或使用本程序任何部分 <p>
 * 
 * @author <a href="mailto:tangww@corp.21cn.com">tangww</a>
 * @version newEDM
 * @since 2016年11月29日 
 *
 */
public enum Protocol {
	
	asciiProt( 3 ), /* arbitrary value. */
	binaryProt( 0 ),
	negotiatingProt( 0 );/* Discovering the protocol */

	private int value = 0;

	private Protocol(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue( int value ) {
		this.value = value;
	}
}
