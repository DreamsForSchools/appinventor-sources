package com.google.appinventor.client.wizards;

import com.google.appinventor.client.Ode;
import com.google.appinventor.client.OdeAsyncCallback;
import com.google.appinventor.client.widgets.LabeledTextBox;
import com.google.appinventor.client.widgets.Validator;
import com.google.appinventor.client.youngandroid.TextValidators;
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
                String email = emailTextbox.getText();
                if(TextValidators.checkValidEmail(email)){
                    String userId = Ode.getInstance().getUser().getUserId();
                    long projectId = Ode.getInstance().getCurrentYoungAndroidProjectId();
                    Window.alert(projectId+"");
                    Ode.getInstance().getProjectService().shareProject(userId, projectId,
                            email, 1, new OdeAsyncCallback<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Window.alert("Success");
                                }
                            });
                }else {
                    Window.alert("Invalid email address!");
                }
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
}
