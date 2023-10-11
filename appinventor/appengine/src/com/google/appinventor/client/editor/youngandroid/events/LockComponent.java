package com.google.appinventor.client.editor.youngandroid.events;

import com.google.gwt.core.client.JavaScriptObject;

public class LockComponent extends JavaScriptObject {
  public static final String TYPE;

  static {
    TYPE = init(LockComponent.class);
  }

  protected LockComponent() {}

  private static native String init(Class<LockComponent> clazz)/*-{
    clazz.jsType = AI.Events.LockComponent;
    return clazz.jsType.prototype.type;
  }-*/;

  public static native LockComponent create(String editorId, String uuid, String userEmail)/*-{
    var component = {
      id: uuid,
      userEmail: userEmail};
    return new AI.Events.LockComponent(editorId, component);
  }-*/;

  public final native boolean recordUndo()/*-{
    return this.recordUndo;
  }-*/;

  public final native String getType()/*-{
    return this.type;
  }-*/;

  public final native <T> T as(Class<T> eventType)/*-{
    return eventType && eventType.jsType && eventType.jsType.prototype.type == this.type ?
      this : null;
  }-*/;

  public final native String getEditorId()/*-{
    return this.editorId;
  }-*/;

  public final native String getProjectId()/*-{
    return this.projectId;
  }-*/;

  public final native boolean isRealtime()/*-{
    return this.realtime;
  }-*/;

  public final native void setRealtime(boolean realtime)/*-{
    this.realtime = realtime;
  }-*/;

  public final native String getComponentId() /*-{
    return this.componentId;
  }-*/;

  public final native String getUserEmail()/*-{
    return this.userEmail;
  }-*/;

  public final native JavaScriptObject toJson() /*-{
    return this.toJson();
  }-*/;

  public static final native LockComponent fromJson(JavaScriptObject json)/*-{
    var event = new AI.Events.LockComponent(null, null);
    event.fromJson(json);
    return event;
  }-*/;

  public final native boolean isTransient()/*-{
    return !this.persist;
  }-*/;

  public final native void run()/*-{
    return this.run();
  }-*/;
}


