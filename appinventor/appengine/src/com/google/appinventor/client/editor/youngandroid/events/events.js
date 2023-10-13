/* -*- mode: javascript; js-indent-level: 2; -*- */
/**
 * 
 */

// Copied from goog.inherits, which is not available at the time this code is evaluated.
function inherits(childCtor, parentCtor) {
  /** @constructor */
  function tempCtor() {}
  tempCtor.prototype = parentCtor.prototype;
  childCtor.superClass_ = parentCtor.prototype;
  childCtor.prototype = new tempCtor();
  /** @override */
  childCtor.prototype.constructor = childCtor;

  /**
   * Calls superclass constructor/method.
   *
   * This function is only available if you use goog.inherits to
   * express inheritance relationships between classes.
   *
   * NOTE: This is a replacement for goog.base and for superClass_
   * property defined in childCtor.
   *
   * @param {!Object} me Should always be "this".
   * @param {string} methodName The method name to call. Calling
   *     superclass constructor can be done with the special string
   *     'constructor'.
   * @param {...*} var_args The arguments to pass to superclass
   *     method/constructor.
   * @return {*} The return value of the superclass method/constructor.
   */
  childCtor.base = function(me, methodName, var_args) {
    // Copying using loop to avoid deop due to passing arguments object to
    // function. This is faster in many JS engines as of late 2014.
    var args = new Array(arguments.length - 2);
    for (var i = 2; i < arguments.length; i++) {
      args[i - 2] = arguments[i];
    }
    return parentCtor.prototype[methodName].apply(me, args);
  };
}

window.EventFactory = {
  constructors: {}, deserializers: {},
  registerEvent: function(eventCtor) {
    window.EventFactory.constructors[eventCtor.prototype.type] = eventCtor;
    window.EventFactory.deserializers[eventCtor.prototype.type] = eventCtor.prototype.fromJson;
  }
}

if (window.AppInventor == undefined) window.AppInventor = {};
window.AppInventor.Events = {};
window.AppInventor.Events.Abstract = function() {
};
window.AppInventor.Events.prototype.type = null;
window.AppInventor.Events.prototype.projectId = null;
window.AppInventor.Events.prototype.realtime = false;
window.AppInventor.Events.prototype.userId = null;

// Designer events
window.AppInventor.Events.COMPONENT_CREATE = 'component.create';
window.AppInventor.Events.COMPONENT_MOVE = 'component.move';
window.AppInventor.Events.COMPONENT_DELETE = 'component.delete';
window.AppInventor.Events.COMPONENT_PROPERTY_CHANGE = 'component.property.change';
window.AppInventor.Events.SCREEN_CREATE = 'screen.create';
window.AppInventor.Events.SCREEN_DELETE = 'screen.delete';
window.AppInventor.Events.SCREEN_SWITCH = 'screen.switch';

// Companion events
window.AppInventor.Events.COMPANION_CONNECTED = 'companion.connected';
window.AppInventor.Events.COMPANION_DISCONNECTED = 'companion.disconnected';

/**
 * Event fired when a new component is created in the Designer view.
 *
 * @constructor
 * @param {Number} projectId
 * @param {string} uuid
 * @param {string} componentType
 */
window.AppInventor.Events.CreateComponent = function(uuid, componentType) {
  this.componentUuid = uuid;
  this.componentType = componentType;
};
inherits(window.AppInventor.Events.CreateComponent, window.AppInventor.Events.Abstract);

window.AppInventor.Events.CreateComponent.prototype.type = window.AppInventor.Events.COMPONENT_CREATE;

window.AppInventor.Events.CreateComponent.prototype.toJson = function() {

};

window.AppInventor.Events.CreateComponent.prototype.fromJson = function(json) {
  
};

window.EventFactory.registerEvent(window.AppInventor.Events.CreateComponent);

/**
 * Event fired when a component is moved in the view hierarchy in the Designer view.
 * 
 * @constructor
 * @param {string} uuid
 * @param {string} parentUuid
 * @param {Number} index
 */
window.AppInventor.Events.MoveComponent = function(uuid, parentUuid, index) {
  this.componentUuid = uuid;
  this.parentUuid = parentUuid;
  this.position = index;
};
inherits(window.AppInventor.Events.MoveComponent, window.AppInventor.Events.Abstract);

window.AppInventor.Events.MoveComponent.prototype.type = window.AppInventor.Events.COMPONENT_MOVE;

window.AppInventor.Events.MoveComponnet.prototype.toJson = function() {
  
};

window.AppInventor.Events.MoveComponent.prototype.fromJson = function(json) {

};

window.EventFactory.registerEvent(window.AppInventor.Events.MoveComponent);

/**
 * Event fired when a component is deleted in the Designer view.
 * 
 * @constructor
 * @param {string} uuid
 */
window.AppInventor.Events.DeleteComponent = function(uuid) {
  this.componentUuid = uuid;
};
inherits(window.AppInventor.Events.DeleteComponent, window.AppInventor.Events.Abstract);

window.AppInventor.Events.DeleteComponent.prototype.type = window.AppInventor.Events.COMPONENT_DELETE;

window.AppInventor.Events.DeleteComponent.prototype.toJson = function() {
};

window.AppInventor.Events.DeleteComponent.prototype.fromJson = function(json) {
};

window.EventFactory.registerEvent(window.AppInventor.Events.DeleteComponent);

/**
 * Event fired when the property of a component is changed.
 * 
 * @constructor
 * @param {string} uuid
 * @param {string} propertyName
 * @param {string} propertyValue
 */
window.AppInventor.Events.ComponentPropertyChange = function(uuid, propertyName, propertyValue) {
  this.componentUuid = uuid;
  this.propertyName = propertyName;
  this.propertyValue = propertyValue;
};
inherits(window.AppInventor.Events.ComponentPropertyChange, window.AppInventor.Events.Abstract);

window.AppInventor.Events.ComponentPropertyChange.prototype.type = window.AppInventor.Events.COMPONENT_PROPERTY_CHANGE;

window.AppInventor.Events.ComponentPropertyChange.prototype.toJson = function() {
};

window.AppInventor.Events.ComponentPropertyChange.prototype.fromJson = function(json) {
};

window.EventFactory.registerEvent(window.AppInventor.Events.ComponentPropertyChange);


