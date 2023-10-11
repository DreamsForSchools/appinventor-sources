package com.google.appinventor.client.editor.youngandroid.events;

import com.google.gwt.core.client.JavaScriptObject;

public class ChangeProperty extends JavaScriptObject {

    public static final String TYPE;

    static {
        TYPE = init(ChangeProperty.class);
    }

    protected ChangeProperty() {}

    private static native String init(Class<ChangeProperty> clazz)/*-{
//    clazz.jsType = AI.Events.ComponentProperty;
//    return clazz.jsType.prototype.type;
  }-*/;

    public static native ChangeProperty create(String editorId, String uuid, String propertyName, String value)/*-{
//    var component = {
//      id: uuid,
//      property: propertyName,
//      value: value};
//    return new AI.Events.ComponentProperty(editorId, component);
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

    public final native String getProperty()/*-{
    return this.property;
  }-*/;

    public final native String getValue()/*-{
    return this.value;
  }-*/;

    public final native JavaScriptObject toJson() /*-{
    return this.toJson();
  }-*/;

    public static final native ChangeProperty fromJson(JavaScriptObject json)/*-{
//    var event = new AI.Events.ComponentProperty(null, null);
//    event.fromJson(json);
//    return event;
  }-*/;

    public final native boolean isTransient() /*-{
    return !this.persist;
  }-*/;

    public final native void run()/*-{
    return this.run();
  }-*/;

}
