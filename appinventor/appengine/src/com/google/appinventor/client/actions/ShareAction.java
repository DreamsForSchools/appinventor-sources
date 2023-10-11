package com.google.appinventor.client.actions;

import com.google.appinventor.client.wizards.ShareProjectWizard;
import com.google.gwt.user.client.Command;

public class ShareAction implements Command {
    @Override
    public void execute() {
        new ShareProjectWizard().center();
    }
}
