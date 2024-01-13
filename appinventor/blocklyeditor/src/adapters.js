// -*- mode: javascript; c-basic-offset: 2; -*-
// Copyright © 2016 Massachusetts Institute of Technology. All rights reserved.

/**
 * @license
 * @fileoverview Declares adapter objects for interfacing with GWT objects from JavaScript.
 *
 * @author Evan W. Patton <ewpatton@mit.edu>
 */

'use strict';

goog.provide('AI.Adapter');

/**
 * A JavaScript Adapter for the AI Designer interface.
 * @param {string} formName The name of the form, e.g. 1234_Screen1
 * @param {com.google.appinventor.client.editor.youngandroid.YaFormEditor} editor The YaFormEditor
 * that implements the IDesigner interface.
 * @constructor
 */
AI.Adapter.Designer = function(formName, editor) {
  this.editor = editor;
  var parts = formName.split('_');
  this.projectId = parts[0];
  this.formName = formName;
};

/**
 * Project identifier
 * @type {number}
 */
AI.Adapter.Designer.prototype.projectId = null;

/**
 * Add a component to the screen
 * @type {?function(this: AI.Adapter.Designer, uuid: string, type: string, properties)}
 * @param {string} uuid The UUID of the component to be created
 * @param {string} type The type of the component
 * @param {Object<string, string>} properties A dictionary of properties for the component
 */
AI.Adapter.Designer.prototype.addComponent = null;

/**
 * Remove a component from the screen. Note: this method will not prompt the user for confirmation,
 * so it should only be called under conditions where it makes sense to remove a component without
 * first asking the user.
 * @type {?function(this: AI.Adapter.Designer, uuid: string)}
 * @param {string} uuid The UUID of the component to be removed
 */
AI.Adapter.Designer.prototype.removeComponent = null;

/**
 * Rename a component on the screen. This is a convenience method and is equivalent to:
 * <code>
 *   designer.setProperty(uuid, 'Name', name);
 * </code>
 * @type {?function(this: AI.Adapter.Designer, uuid: string, name: string)}
 * @param {string} uuid The UUID of the component whose name will be changed.
 * @param {string} name The new name of the component.
 */
AI.Adapter.Designer.prototype.renameComponent = null;

/**
 * Get a component adapter for the given component UUID. If there is no Component for the given
 * UUID, null is returned. Otherwise, an instance of AI.Adapter.Component will be returned that
 * adapts the GWT MockComponent as a JavaScript object.
 * @type {?function(this: AI.Adapter.Designer, uuid: string): ?AI.Adapter.Component}
 */
AI.Adapter.Designer.prototype.getComponentByUuid = null;

/**
 * Set the value of a property.
 * @type {?function(this: AI.Adapter.Designer, uuid: string, property: string, value)}
 * @param {string} uuid The UUID of the component whose property will be changed.
 * @param {string} property The name of the property to be changed.
 * @param value The new value of the property.
 */
AI.Adapter.Designer.prototype.setProperty = null;

/**
 * A JavaScript Adapter for a MockComponent in the AI Designer interface.
 * @param {AI.Adapter.Designer} editor A Designer adapter for the editor containing the component.
 * @param {Object} component The MockComponent to be wrapped.
 * @constructor
 */
AI.Adapter.Component = function(editor, component) {
  this.editor = editor;
  this.component = component;
};

/**
 * Change the component border color which indicates who selects it.
 * @type {?function(color: string)}
 * @param {string} color The color of the border color.
 */
AI.Adapter.Component.prototype.select = null;

/**
 * Remove the component border color which indicates it isn't selected by anyone.
 * @type {?function()}
 */
AI.Adapter.Component.prototype.deselect = null;

/**
 * Change the component item background color in source tree explorer.
 * @type {?function(color: string)}
 * @param {string} color The color of the background color.
 */
AI.Adapter.Component.prototype.setItemBackgroundColor = null;

/**
 * Clear the component item background color in source tree explorer.
 * @type {?function()}
 */
AI.Adapter.Component.prototype.clearItemBackgroundColor = null;
