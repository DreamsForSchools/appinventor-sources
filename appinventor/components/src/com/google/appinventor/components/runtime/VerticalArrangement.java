// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.ComponentConstants;
import com.google.appinventor.components.common.YaVersion;

/**
 * A vertical arrangement of components
 * @author sharon@google.com (Sharon Perl)
 *
 */

@DesignerComponent(version = YaVersion.VERTICALARRANGEMENT_COMPONENT_VERSION,
    description = "A formatting element in which to place components " +
    "that should be displayed one below another.  (The first child component " +
    "is stored on top, the second beneath it, etc.)  If you wish to have " +
    "components displayed next to one another, use " +
    "<code>HorizontalArrangement</code> instead.",
    category = ComponentCategory.LAYOUT)
@SimpleObject
public class VerticalArrangement extends HVArrangement {

  public VerticalArrangement(ComponentContainer container) {
    super(container, ComponentConstants.LAYOUT_ORIENTATION_VERTICAL,
      ComponentConstants.NONSCROLLABLE_ARRANGEMENT);
  }

}
