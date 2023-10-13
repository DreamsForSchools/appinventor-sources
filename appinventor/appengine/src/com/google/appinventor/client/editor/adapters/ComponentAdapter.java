package com.google.appinventor.client.editor.adapters;

import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.gwt.core.client.JavaScriptObject;

public class ComponentAdapter extends JavaScriptObject implements IComponent {

  static {
    setupPrototype();
  }

  private static native void setupPrototype()/*-{
    AI.Adapter.Component.prototype.select = function(color){
      var component = this.component;
      var args = Array.prototype.slice.call(arguments);
      $entry(function(){
        component.@com.google.appinventor.client.editor.simple.components.MockComponent::selectColor(Ljava/lang/String;).apply(component, args)
      })();
    }

    AI.Adapter.Component.prototype.deselect = function(){
      var component = this.component;
      var args = Array.prototype.slice.call(arguments);
      $entry(function(){
        component.@com.google.appinventor.client.editor.simple.components.MockComponent::deselect().apply(component, args)
      })();
    }

    AI.Adapter.Component.prototype.setItemBackgroundColor = function(color){
      var component = this.component;
      var args = Array.prototype.slice.call(arguments);
      $entry(function(){
        component.@com.google.appinventor.client.editor.simple.components.MockComponent::setItemBackgroundColor(Ljava/lang/String;).apply(component, args)
      })();
    }

    AI.Adapter.Component.prototype.clearItemBackgroundColor = function(){
      var component = this.component;
      var args = Array.prototype.slice.call(arguments);
      $entry(function(){
        component.@com.google.appinventor.client.editor.simple.components.MockComponent::clearItemBackgroundColor().apply(component, args)
      })();
    }
  }-*/;

  protected ComponentAdapter() {
  }

  public static native IComponent create(DesignerAdapter editor, MockComponent component)/*-{
    return new AI.Adapter.Component(editor, component);
  }-*/;
}
