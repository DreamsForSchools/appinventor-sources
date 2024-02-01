package com.google.appinventor.client.editor.adapters;

public interface IDesigner {
  public void addComponent(String uuid, String type, String affectedScreenName);
  public void removeComponent(String uuid, String affectedScreenName);
  public void renameComponent(String uuid, String name, String affectedScreenName);
  public IComponent getComponentByUuid(String uuid);
  public void setProperty(String uuid, String property, String value);
  public void moveComponent(String uuid, String parentUuid, int index);
}
