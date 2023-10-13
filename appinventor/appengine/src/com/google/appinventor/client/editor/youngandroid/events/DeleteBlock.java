package com.google.appinventor.client.editor.youngandroid.events;

import com.google.gwt.core.client.JavaScriptObject;

public class DeleteBlock extends JavaScriptObject implements BlocklyEvent {
  public static final String TYPE;
  
  static {
    TYPE = init(DeleteBlock.class);
  }
  
  private static native String init(Class<DeleteBlock> clazz)/*-{
    clazz.jsType = Blockly.Events.Delete;
  }-*/;

  static native void create()/*-{
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
  public boolean isRealtime() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setRealtime(boolean realtime) {
    // TODO Auto-generated method stub
    
  }

  public String getUserId() {
    // TODO Auto-generated method stub
    return "0";
  }

  @Override
  public boolean isTransient() {
    // TODO Auto-generated method stub
    return false;
  }

}
