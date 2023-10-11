package com.google.appinventor.client.editor.youngandroid.events;

import com.google.gwt.core.client.JavaScriptObject;

public class CompanionConnected extends JavaScriptObject implements CompanionEvent {
  public static final String TYPE;

  static {
    TYPE = init(CompanionConnected.class);
  }

  protected CompanionConnected() {}

  private static native String init(Class<CompanionConnected> clazz)/*-{
    clazz.jsType = AI.Events.CompanionConnected;
    return clazz.jsType.prototype.type;
  }-*/;

  @Override
  public native boolean recordUndo()/*-{
    return false;
  }-*/;

  @Override
  public native String getType()/*-{
    return this.type;
  }-*/;

  @Override
  public native <T> T as(Class<T> eventType)/*-{
    return eventType && eventType.jsType && eventType.jsType.prototype.type == this.type ?
      this : null;
  }-*/;

  @Override
  public native String getProjectId()/*-{
    return this.projectId;
  }-*/;

  public native String getUserId()/*-{
    return this.userId;
  }-*/;

  @Override
  public native boolean isRealtime()/*-{
    return this.realtime;
  }-*/;

  @Override
  public native void setRealtime(boolean realtime)/*-{
    this.realtime = realtime;
  }-*/;

  @Override
  public native boolean isTransient()/*-{
    return !this.persist;
  }-*/;

}
