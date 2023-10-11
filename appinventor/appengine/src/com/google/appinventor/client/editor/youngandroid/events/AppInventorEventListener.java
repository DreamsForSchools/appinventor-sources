package com.google.appinventor.client.editor.youngandroid.events;

public interface AppInventorEventListener {
  public void onBlockEvent(BlocklyEvent event);
  public void onCompanionEvent(CompanionEvent event);
  public void onDesignerEvent(DesignerEvent event);
}
