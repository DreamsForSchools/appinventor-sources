package com.google.appinventor.client.editor.youngandroid.events;

import com.google.gwt.core.client.JavaScriptObject;

public final class NativeEventHelper {
  private NativeEventHelper() {}

  public static native boolean isTransient(AppInventorEvent event)/*-{
    return event && event.isTransient;
  }-*/;

  public static native AppInventorEvent asEvent(JavaScriptObject object)/*-{
    return object instanceof AI.Events.Abstract ? object : null;
  }-*/;

  public static native <T> T as(JavaScriptObject object, Class<T> clazz)/*-{
    if (!object) {
      return null;
    }
    return clazz && clazz.jsType && object.type == clazz.jsType ? object : null;
  }-*/;
}
