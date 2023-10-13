package com.google.appinventor.client.editor.youngandroid.events;

import com.google.gwt.core.client.JavaScriptObject;

public final class ScreenSwitch extends JavaScriptObject implements ScreenEvent {
  public static final String TYPE;

  static {
    TYPE = init(ScreenSwitch.class);
  }

  protected ScreenSwitch() {}

  private static native String init(Class<ScreenSwitch> clazz)/*-{
    clazz.jsType = AI.Events.ScreenSwitch;
    return clazz.jsType.prototype.type;
  }-*/;

  public native boolean recordUndo()/*-{
    return false;
  }-*/;

  public native String getType()/*-{
    return this.type;
  }-*/;

  public native <T> T as(Class<T> eventType)/*-{
    return eventType && eventType.jsType && eventType.jsType.prototype.type == this.type ?
      this : null;
  }-*/;

  public native String getProjectId()/*-{
    return this.projectId;
  }-*/;

  public native String getUserId()/*-{
    return this.userId;
  }-*/;

  public native boolean isRealtime()/*-{
    return this.realtime;
  }-*/;

  public native void setRealtime(boolean realtime)/*-{
    this.realtime = realtime;
  }-*/;

  public native boolean isTransient()/*-{
    return !this.persist;
  }-*/;

}
