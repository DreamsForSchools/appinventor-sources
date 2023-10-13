package com.google.appinventor.client.wizards;

import com.google.appinventor.client.Ode;
import com.google.appinventor.client.OdeAsyncCallback;
import com.google.appinventor.client.widgets.LabeledTextBox;
import com.google.appinventor.client.widgets.Validator;
import com.google.appinventor.client.youngandroid.TextValidators;
import com.google.appinventor.common.version.AppInventorFeatures;
import com.google.appinventor.shared.rpc.project.UserProject;
import com.google.appinventor.shared.rpc.user.User;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;

import static com.google.appinventor.client.Ode.MESSAGES;
/**
 * A Wizard for sharing project with others.
 */
public class ShareProjectWizard extends Wizard{

    private LabeledTextBox emailTextbox;

    public ShareProjectWizard() {
        super(MESSAGES.shareProjectWizardCaption(), true, false);

        // Initialize the UI
        setStylePrimaryName("ode-DialogBox");

        emailTextbox = new LabeledTextBox(MESSAGES.shareProjectEmailLabel(), new Validator() {
            @Override
            public boolean validate(String value) {
                return true;
            }

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }
        });

        emailTextbox.getTextBox().addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                int keyCode = event.getNativeKeyCode();
                if (keyCode == KeyCodes.KEY_ENTER) {
                    handleOkClick();
                } else if (keyCode == KeyCodes.KEY_ESCAPE) {
                    handleCancelClick();
                }
            }
        });

        VerticalPanel page = new VerticalPanel();

        page.add(emailTextbox);
        addPage(page);

        initFinishCommand(new Command() {
            @Override
            public void execute() {
                final String email = emailTextbox.getText().toLowerCase();
                // TODO(Xinyue Deng): add email address format checking & check if email is in the system.
                final User user = Ode.getInstance().getUser();
                long projectId = Ode.getInstance().getCurrentYoungAndroidProjectId();
                Ode.getInstance().getProjectService().shareProject(user.getUserId(), projectId,
                        email, 1, new OdeAsyncCallback<Long>() {
                            @Override
                            public void onSuccess(Long projectId) {
                                publishShareProject(email, projectId.toString());
                                Window.alert("Success");
                            }
                        });
            }
        });
    }

    @Override
    public void show() {
        super.show();
        // Wizard size (having it resize between page changes is quite annoying)
        int width = 340;
        int height = 40;
        this.center();

        setPixelSize(width, height);
        super.setPagePanelHeight(85);

        DeferredCommand.addCommand(new Command() {
            public void execute() {
                emailTextbox.setFocus(true);
                emailTextbox.selectAll();
            }
        });
    }

    public native void publishShareProject(String email, String projectId)/*-{
    var msg = {
      "channel": email,
      "project": parseInt(projectId, 10)};
    console.log(msg);
    $wnd.socket.emit("shareProject", msg);
  }-*/;
}
