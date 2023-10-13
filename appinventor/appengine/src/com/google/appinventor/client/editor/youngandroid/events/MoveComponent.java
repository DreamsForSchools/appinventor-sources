package com.google.appinventor.client.editor.youngandroid.events;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Move Component Event
 */
public class MoveComponent extends JavaScriptObject {

  public static final String TYPE;

  static {
    TYPE = init(MoveComponent.class);
  }

  protected MoveComponent() {}

  private static native String init(Class<MoveComponent> clazz)/*-{
    clazz.jsType = AI.Events.MoveComponent;
    return clazz.jsType.prototype.type;
  }-*/;

  public static native MoveComponent create(String editorId, String uuid, String parentId, int index)/*-{
    var component = {
      id: uuid,
      parentId: parentId,
      index: index};
    return new AI.Events.MoveComponent(editorId, component);
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

  public final native String getParentId()/*-{
    return this.parentId;
  }-*/;

  public final native int getIndex()/*-{
    return this.index;
  }-*/;

  public final native JavaScriptObject toJson() /*-{
    return this.toJson();
  }-*/;

  public static final native MoveComponent fromJson(JavaScriptObject json)/*-{
    var event = new AI.Events.MoveComponent(null, null);
    event.fromJson(json);
    return event;
  }-*/;

  public final native boolean isTransient() /*-{
    return !this.persist;
  }-*/;

  public final native void run()/*-{
    return this.run();
  }-*/;

}
