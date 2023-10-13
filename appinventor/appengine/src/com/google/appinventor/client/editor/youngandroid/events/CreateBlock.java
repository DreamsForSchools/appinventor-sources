package com.google.appinventor.client.editor.youngandroid.events;

import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.gwt.core.client.JavaScriptObject;

public class CreateBlock extends JavaScriptObject implements BlocklyEvent {
  public static final String TYPE;
  
  static {
    TYPE = init(CreateBlock.class);
  }
  
  public static native CreateBlock construct(long projectId, MockComponent component)/*-{
    return new Blockly.Events.ComponentAdded(projectId, component.@com.google.appinventor.client.editor.simple.components.MockComponent::getProject()("Uuid"));
  }-*/;
  
  private static native String init(Class<CreateBlock> clazz)/*-{
    clazz.jsType = Blockly.Events.Create;
  }-*/;

  @Override
  public native String getBlockId()/*-{
    return this.blockId ? this.blockId : null;
  }-*/;

  @Override
  public native String getWorkspaceId()/*-{
    return this.workspaceId ? this.workspaceId : null;
  }-*/;

  @Override
  public native String getGroup()/*-{
    return this.group
  }-*/;

  @Override
  public native boolean recordUndo()/*-{
    return this.recordUndo;
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
  public String getProjectId() {
    // TODO Auto-generated method stub
    return "";
  }

  @Override
  public native boolean isRealtime()/*-{
    return this.realtime;
  }-*/;

  @Override
  public native void setRealtime(boolean realtime)/*-{
    this.realtime = realtime;
  }-*/;

  public native String getUserId()/*-{
    return this.userId;
  }-*/;

  @Override
  public native boolean isTransient()/*-{
    return !this.persist;
  }-*/;

}
