package com.google.appinventor.client.editor.adapters;

import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.gwt.core.client.JavaScriptObject;

public final class DesignerAdapter extends JavaScriptObject implements IDesigner {

  static {
    setupPrototype();
  }

  private static native void setupPrototype()/*-{
    AI.Adapter.Designer.prototype.addComponent = function(uuid, type) {
      var editor = this.editor;
      var args = Array.prototype.slice.call(arguments);
      $entry(function() { editor.@com.google.appinventor.client.editor.youngandroid.YaFormEditor::addComponent(Ljava/lang/String;Ljava/lang/String;).apply(editor, args) })();
    };
    AI.Adapter.Designer.prototype.getComponentByUuid = function(uuid) {
      var editor = this.editor;
      var args = Array.prototype.slice.call(arguments);
      return $entry(function() { return editor.@com.google.appinventor.client.editor.youngandroid.YaFormEditor::getComponentByUuid(Ljava/lang/String;).apply(editor, args) })();
    };
    AI.Adapter.Designer.prototype.removeComponent = function(uuid) {
      var editor = this.editor;
      var args = Array.prototype.slice.call(arguments);
      $entry(function() { editor.@com.google.appinventor.client.editor.youngandroid.YaFormEditor::removeComponent(Ljava/lang/String;).apply(editor, args) })();
    };
    AI.Adapter.Designer.prototype.renameComponent = function(uuid, name) {
      var editor = this.editor;
      var args = Array.prototype.slice.call(arguments);
      $entry(function() { editor.@com.google.appinventor.client.editor.youngandroid.YaFormEditor::renameComponent(Ljava/lang/String;Ljava/lang/String;).apply(editor, args) })();
    };
    AI.Adapter.Designer.prototype.setProperty = function(uuid, name, value) {
      var editor = this.editor;
      var args = Array.prototype.slice.call(arguments);
      $entry(function() { editor.@com.google.appinventor.client.editor.youngandroid.YaFormEditor::setProperty(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;).apply(editor, args) })();
    };
    AI.Adapter.Designer.prototype.moveComponent = function(uuid, parentUuid, index) {
      var editor = this.editor;
      var args = Array.prototype.slice.call(arguments);
      $entry(function() { editor.@com.google.appinventor.client.editor.youngandroid.YaFormEditor::moveComponent(Ljava/lang/String;Ljava/lang/String;I).apply(editor, args) })();
    };
  }-*/;

  protected DesignerAdapter() {
  }

  public static native DesignerAdapter make(String formName, YaFormEditor editor)/*-{
    return new AI.Adapter.Designer(formName, editor);
  }-*/;

  @Override
  public final native void addComponent(String uuid, String type)/*-{
    this.addComponent(uuid, type);
  }-*/;
  
  @Override
  public final native IComponent getComponentByUuid(String uuid)/*-{
    return this.getComponentByUuid(uuid);
  }-*/;
  
  @Override
  public final native void removeComponent(String uuid)/*-{
    this.removeComponent(uuid);
  }-*/;
  
  @Override
  public final native void renameComponent(String uuid, String name)/*-{
    this.renameComponent(uuid, name);
  }-*/;
  
  @Override
  public final native void setProperty(String uuid, String name, String value)/*-{
    this.setProperty(uuid, name, value);
  }-*/;

  @Override
  public final native void moveComponent(String uuid, String parentUuid, int index)/*-{
    this.moveComponent(uuid, parentUuid, index);
  }-*/;
}
