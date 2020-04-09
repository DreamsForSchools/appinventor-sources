// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2019 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.explorer.dialogs;

import com.google.appinventor.client.Ode;
import com.google.appinventor.client.explorer.project.Project;
import com.google.appinventor.client.wizards.NewProjectWizard.NewProjectCommand;
import com.google.appinventor.client.wizards.TemplateUploadWizard;
import com.google.appinventor.client.wizards.youngandroid.NewYoungAndroidProjectWizard;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;

/**
 * A dialog containing options to begin 3 different tutorials or being a new
 * project from scratch. Should appear when the user currently has no projects
 * in their projects list.
 */
public class NoProjectDialogBox extends DialogBox {

  private static NoProjectDialogBoxUiBinder uiBinder =
      GWT.create(NoProjectDialogBoxUiBinder.class);

  interface NoProjectDialogBoxUiBinder extends UiBinder<Widget, NoProjectDialogBox> {
  }

  /**
   * Class to open a new project with the tutorial's contents when the user
   * clicks on the "Go to Tutorial" button.
   */
  private class NewTutorialProject implements NewProjectCommand {
    public void execute(Project project) {
      Ode.getInstance().openYoungAndroidProjectInDesigner(project);
    }
  }

  @UiField
  Button closeDialogBox;
  @UiField
  Button goToGettingStarted;
  @UiField
  Button goToMeetLarry;
  @UiField
  Button goToWhackAMole;
  @UiField
  Button noDialogNewProject;

  /**
   * Creates a new dialog box when the user has no current projects in their
   * projects list. This will give them an option to open a tutorial project or
   * create their own project.
   */
  public NoProjectDialogBox() {
    this.setStylePrimaryName("ode-noDialogDiv");
    add(uiBinder.createAndBindUi(this));
    this.center();
    this.setAnimationEnabled(true);
    this.setAutoHideEnabled(true);
  }

  @UiHandler("closeDialogBox")
  void handleClose(ClickEvent e) {
    this.hide();
  }

  @UiHandler("goToGettingStarted")
  void handleGoToGettingStarted(ClickEvent e) {
    this.hide();
    Window.open("https://www.dreamsforschools.org/codeathome-mobileapps/#lets-whack-a-mole", "_blank", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes");
    // new WindowOpenClickHandler("https://www.dreamsforschools.org/codeathome-mobileapps/#getting-started");
    // new TemplateUploadWizard().createProjectFromExistingZip("HelloPurr", new NewTutorialProject());
  }

  @UiHandler("goToMeetLarry")
  void handleGoToMeetLarry(ClickEvent e) {
    this.hide();
    Window.open("https://www.dreamsforschools.org/codeathome-mobileapps/#lets-whack-a-mole", "_blank", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes");
    // new WindowOpenClickHandler("https://www.dreamsforschools.org/codeathome-mobileapps/#lets-meet-larry");
    // TemplateUploadWizard.openProjectFromTemplate("http://appinventor.mit.edu/yrtoolkit/yr/aiaFiles/talk_to_me/TalkToMe.asc", new NewTutorialProject());
  }

  @UiHandler("goToWhackAMole")
  void handleGoToWhackAMole(ClickEvent e) {
    this.hide();
    Window.open("https://www.dreamsforschools.org/codeathome-mobileapps/#lets-whack-a-mole", "_blank", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes");
    // new WindowOpenClickHandler("https://www.dreamsforschools.org/codeathome-mobileapps/#lets-whack-a-mole");
    // TemplateUploadWizard.openProjectFromTemplate("http://appinventor.mit.edu/yrtoolkit/yr/aiaFiles/hello_bonjour/translate_tutorial.asc", new NewTutorialProject());
  }

  @UiHandler("noDialogNewProject")
  void handleNewProject(ClickEvent e) {
    this.hide();
    new NewYoungAndroidProjectWizard(null).show();
  }
}
