package com.google.appinventor.client.editor.youngandroid.events;

public interface BlocklyEvent extends AppInventorEvent {
  public String getBlockId();
  public String getWorkspaceId();
  public String getGroup();
}
