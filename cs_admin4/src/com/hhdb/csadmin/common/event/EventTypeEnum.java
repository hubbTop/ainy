package com.hhdb.csadmin.common.event;
/**
 * @author hyz
 * COMMON 基本类型，凡是不需要传参数event都用这个类型。每个插件只能接受处理一个COMMON 类型
 * GET_OBJ 这个类型的event发送出去后必须能得到一个返回Object对象的event。每个插件也只能接受处理一个GET_OBJ类型
 * REPLY 返回类型。只要是插件里receEvent方法处理event后返回的Event（除了ErrorEvent之外）都是REPLY类型
 * CMD 这是为了处理一些特殊情况的类型。通过给cmd设值，来判断接收方接收事件的时候该给予什么样的逻辑，发送时一般new一个CmdEvent
 * ERROR 接收event之后出现异常，错误的时候返回的event类型。一般new一个ErrorEvent
 */
public enum EventTypeEnum {
	COMMON,GET_OBJ,REPLY,CMD,ERROR
}
