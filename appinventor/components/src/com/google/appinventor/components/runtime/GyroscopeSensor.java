// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Component providing data from the device's gyroscope sensor.
 */
@DesignerComponent(version = YaVersion.GYROSCOPESENSOR_COMPONENT_VERSION,
    description = "Non-visible component that can measure angular velocity in three " +
    "dimensions in units of degrees per second.</p>" +
    "<p>In order to function, the component must have its <code>Enabled</code> property set to " +
    "True, and the device must have a gyroscope sensor.",
    category = ComponentCategory.SENSORS,
    nonVisible = true,
    iconName = "images/gyroscopesensor.png")

@SimpleObject
public class GyroscopeSensor extends AndroidNonvisibleComponent
    implements SensorEventListener, Deleteable, OnPauseListener, OnResumeListener {

  // Properties
  private boolean enabled;
  private float xAngularVelocity; // degrees per second
  private float yAngularVelocity; // degrees per second
  private float zAngularVelocity; // degrees per second

  // Sensor information
  private final SensorManager sensorManager;
  private final Sensor gyroSensor;
  private boolean listening;

  /**
   * Creates a new GyroscopeSensor component.
   */
  public GyroscopeSensor(ComponentContainer container) {
    super(container.$form());

    // Get sensors, and start listening.
    sensorManager = (SensorManager) form.getSystemService(Context.SENSOR_SERVICE);
    gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    // Begin listening in onResume() and stop listening in onPause().
    form.registerForOnResume(this);
    form.registerForOnPause(this);

    // Set default property values.
    Enabled(true);
  }

  private void startListening() {
    if (!listening) {
      sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
      listening = true;
    }
  }

  private void stopListening() {
    if (listening) {
      sensorManager.unregisterListener(this);
      listening = false;

      // Throw out sensor information that will go stale.
      xAngularVelocity = 0;
      yAngularVelocity = 0;
      zAngularVelocity = 0;
    }
  }

  // Events

  /**
   * GyroscopeChanged event handler.
   */
  @SimpleEvent(description = "Indicates that the gyroscope sensor data has changed. The " +
      "timestamp parameter is the time in nanoseconds at which the event occurred.")
  public void GyroscopeChanged(
      float xAngularVelocity, float yAngularVelocity, float zAngularVelocity, long timestamp) {
    EventDispatcher.dispatchEvent(this, "GyroscopeChanged",
        xAngularVelocity, yAngularVelocity, zAngularVelocity, timestamp);
  }

  // Properties

  /**
   * Available property getter method (read-only property).
   *
   * @return {@code true} indicates that a gyroscope sensor is available,
   *         {@code false} that it isn't
   */
  @SimpleProperty(description = "Indicates whether a gyroscope sensor is available.",
      category = PropertyCategory.BEHAVIOR)
  public boolean Available() {
    return sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).size() > 0;
  }

  /**
   * Enabled property getter method.
   *
   * @return {@code true} indicates that the sensor generates events,
   *         {@code false} that it doesn't
   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR)
  public boolean Enabled() {
    return enabled;
  }

  /**
   * Enabled property setter method.
   *
   * @param enabled  {@code true} enables sensor event generation,
   *                 {@code false} disables it
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description = "If enabled, then sensor events will be generated and " +
      "XAngularVelocity, YAngularVelocity, and ZAngularVelocity properties will have " +
      "meaningful values.")
  public void Enabled(boolean enabled) {
    if (this.enabled != enabled) {
      this.enabled = enabled;
      if (enabled) {
        startListening();
      } else {
        stopListening();
      }
    }
  }

  /**
   * XAngularVelocity property getter method (read-only property).
   *
   * <p>To return meaningful values the sensor must be enabled.</p>
   *
   * @return  current angular velocity around x axis
   */
  @SimpleProperty(description = "The angular velocity around the X axis, in degrees per second.",
      category = PropertyCategory.BEHAVIOR)
  public float XAngularVelocity() {
    return xAngularVelocity;
  }

  /**
   * YAngularVelocity property getter method (read-only property).
   *
   * <p>To return meaningful values the sensor must be enabled.</p>
   *
   * @return  current angular velocity around y axis
   */
  @SimpleProperty(description = "The angular velocity around the Y axis, in degrees per second.",
      category = PropertyCategory.BEHAVIOR)
  public float YAngularVelocity() {
    return yAngularVelocity;
  }

  /**
   * ZAngularVelocity property getter method (read-only property).
   *
   * <p>To return meaningful values the sensor must be enabled.</p>
   *
   * @return  current angular velocity around z axis
   */
  @SimpleProperty(description = "The angular velocity around the Z axis, in degrees per second.",
      category = PropertyCategory.BEHAVIOR)
  public float ZAngularVelocity() {
    return zAngularVelocity;
  }

  // SensorListener implementation

  /**
   * Responds to changes in the gyroscope sensors.
   *
   * @param sensorEvent an event from the gyroscope sensor
   */
  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    if (enabled) {

      xAngularVelocity = (float) Math.toDegrees(sensorEvent.values[0]);
      yAngularVelocity = (float) Math.toDegrees(sensorEvent.values[1]);
      zAngularVelocity = (float) Math.toDegrees(sensorEvent.values[2]);

      // Raise event.
      GyroscopeChanged(xAngularVelocity, yAngularVelocity, zAngularVelocity,
          sensorEvent.timestamp);
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }

  // Deleteable implementation

  @Override
  public void onDelete() {
    stopListening();
  }

  // OnPauseListener implementation

  public void onPause() {
    stopListening();
  }

  // OnResumeListener implementation

  public void onResume() {
    if (enabled) {
      startListening();
    }
  }
}
