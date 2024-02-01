package com.google.appinventor.client;

import com.google.appinventor.client.editor.FileEditor;
import com.google.appinventor.client.editor.ProjectEditor;
import com.google.appinventor.client.editor.simple.components.FormChangeListener;
import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.editor.youngandroid.DesignToolbar;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.editor.youngandroid.events.*;
import com.google.appinventor.client.explorer.project.Project;
import com.google.appinventor.client.wizards.FileUploadWizard;
import com.google.appinventor.common.version.AppInventorFeatures;
import com.google.appinventor.shared.rpc.project.ProjectNode;
import com.google.appinventor.shared.rpc.project.youngandroid.*;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;

import java.util.logging.Logger;


/**
 * This class manages group collaboration.
 */
public class CollaborationManager implements FormChangeListener {
    private static final Logger LOG = Logger.getLogger(CollaborationManager.class.getName());

    public static final String FILE_UPLOAD = "file_upload";
    public static final String FILE_DELETE = "file_delete";
    public static final String SCREEN_ADD = "screen_add";
    public static final String SCREEN_REMOVE = "screen_remove";

    private boolean broadcast;
    // TODO(xinyue): Modify this to support multi screen
    public String screenChannel;

    public CollaborationManager() {
        exportToJavascriptMethod();
        broadcast = true;
        screenChannel = "";
    }

    public void enableBroadcast() {
        broadcast = true;
    }

    public void disableBroadcast() {
        broadcast = false;
    }

    public void setScreenChannel(String screenChannel) {
        this.screenChannel = screenChannel;
    }

    public String getScreenChannel() {
        return this.screenChannel;
    }

    @Override
    public void onComponentPropertyChanged(MockComponent component, String propertyName, String propertyValue) {
        if(broadcast){
            ChangeProperty event = ChangeProperty.create(Ode.getCurrentChannel(), Ode.getCurrentScreen(), component.getUuid(), propertyName, propertyValue);
            broadcastComponentEvent(event.toJson());
        }
    }

    @Override
    public void onComponentRemoved(MockComponent component, boolean permanentlyDeleted) {
        if (broadcast) {
            DeleteComponent event = DeleteComponent.create(Ode.getCurrentChannel(), Ode.getCurrentScreen(), component.getUuid());
            broadcastComponentEvent(event.toJson());
        }
    }

    @Override
    public void onComponentAdded(MockComponent component) {
        if (broadcast) {
            CreateComponent event = CreateComponent.create(Ode.getCurrentChannel(), Ode.getCurrentScreen(), component.getUuid(), component.getType());
            broadcastComponentEvent(event.toJson());
        }
    }

    @Override
    public void onComponentRenamed(MockComponent component, String oldName) {
        if(broadcast){
            ChangeProperty event = ChangeProperty.create(Ode.getCurrentChannel(), Ode.getCurrentScreen(), component.getUuid(), MockComponent.PROPERTY_NAME_NAME, component.getName());
            broadcastComponentEvent(event.toJson());
        }
    }

    @Override
    public void onComponentMoved(MockComponent component, String newParentId, int index) {
        if(broadcast){
            MoveComponent event = MoveComponent.create(Ode.getCurrentChannel(), Ode.getCurrentScreen(), component.getUuid(), newParentId, index);
            broadcastComponentEvent(event.toJson());
        }
    }

    @Override
    public void onComponentSelectionChange(MockComponent component, boolean selected) {
        if (component.isForm()) {
            return;
        }
        if (AppInventorFeatures.enableComponentLocking()) {
            if (isComponentLocked(Ode.getCurrentChannel(), Ode.getCurrentUserEmail(), component.getUuid())) {
                return;
            }
        }
        if (broadcast) {
            SelectComponent event = SelectComponent.create(
                    Ode.getCurrentChannel(), Ode.getCurrentScreen(), component.getUuid(), Ode.getInstance().getUser().getUserEmail(), selected);
            broadcastComponentEvent(event.toJson());
            if (AppInventorFeatures.enableComponentLocking()) {
                if (selected) {
                    LockComponent lockEvent = LockComponent.create(
                            Ode.getCurrentChannel(), Ode.getCurrentScreen(), component.getUuid(), Ode.getInstance().getUser().getUserEmail());
                    lockEvent.run();
                    broadcastComponentEvent(lockEvent.toJson());
                    setLockedComponent(Ode.getCurrentChannel(), component.getUuid());
                } else {
                    UnlockComponent unlockEvent = UnlockComponent.create(
                            Ode.getCurrentChannel(), Ode.getCurrentScreen(), component.getUuid(), Ode.getInstance().getUser().getUserEmail());
                    unlockEvent.run();
                    broadcastComponentEvent(unlockEvent.toJson());
                    removeLockedComponent(Ode.getCurrentChannel());
                }
            }
        }
    }

    public native void setLockedComponent(String channel, String componentId) /*-{
    $wnd.userLockedComponent[channel] = componentId;
  }-*/;

    public native void removeLockedComponent(String channel) /*-{
    delete $wnd.userLockedComponent[channel];
  }-*/;

    // Component event here stands for YaFormEditor event. i.e. the phone screen editor.
    public native void broadcastComponentEvent(JavaScriptObject eventJson)/*-{
    var msg = {
      "channel": $wnd.Ode_getCurrentChannel(),
      "user": $wnd.userEmail,
      "source": "Designer",
      "event": eventJson
    };
    console.log(msg);
    $wnd.socket.emit("component", msg);
  }-*/;

    public native void broadcastFileEvent(String type, String projectId, String fileName) /*-{
    var msg = {
      "channel" : $wnd.Ode_getCurrentChannel(),
      "user": $wnd.userEmail,
      "source": "Media",
      "projectId": projectId,
      "type": type,
      "fileName": fileName
    };
    $wnd.socket.emit("file", msg);
  }-*/;

    public native void broadcastScreenAdd(String type, String projectId, String formFileId, String blocksFileId, String modDate, String formName) /*-{
    var msg = {
      "channel" : $wnd.Ode_getCurrentChannel(),
      "user": $wnd.userEmail,
      "source": "ScreenAdd",
      "projectId": projectId,
      "formFileId": formFileId,
      "blocksFileId": blocksFileId,
      "modDate": modDate,
      "formName": formName,
      "type": type,
    };
    $wnd.socket.emit("screen", msg);
  }-*/;

    public native void broadcastScreenRemove(String type, String projectId, String nodeFileId, String formFileId, String blocksFileId, String yailFileId, String modDate, String formName) /*-{
    var msg = {
      "channel" : $wnd.Ode_getCurrentChannel(),
      "user": $wnd.userEmail,
      "source": "ScreenRemove",
      "projectId": projectId,
      "nodeFileId": nodeFileId,
      "formFileId": formFileId,
      "blocksFileId": blocksFileId,
      "yailFileId": yailFileId,
      "modDate": modDate,
      "formName": formName,
      "type": type,
    };
    $wnd.socket.emit("screen", msg);
  }-*/;

    // TODO: Delete this, unneeded.
    // Called whenever the screen switches. Weird name. Maybe call it screenChange()
    public native void componentSocketEvent(String channel)/*-{

//        if(!(channel in $wnd.lockedBlocksByChannel)) {
//            $wnd.lockedBlocksByChannel[channel] = {};
//        }
  }-*/;

    public native void connectCollaborationServer(String server, String userEmail) /*-{
    $wnd.socket = $wnd.io.connect(server, {autoConnect: true});
    $wnd.userEmail = userEmail;
    $wnd.colors = ['#999999','#f781bf','#a65628','#ffff33','#ff7f00','#984ea3','#4daf4a','#377eb8','#e41a1c'];
    $wnd.userColorMap = new $wnd.Map();
    $wnd.userColorMap.rmv = $wnd.userColorMap["delete"];
    $wnd.subscribedChannel = new $wnd.Set();
    $wnd.socketEvents = {};
    if($wnd.AIFeature_enableComponentLocking()){
      // track locked components and blocks by all users
      $wnd.lockedComponentsByChannel = {};
      $wnd.lockedBlocksByChannel = {};
    }
    // track locked component and block by client self
    $wnd.userLockedComponent = {};
    $wnd.userLockedBlock = {};
    $wnd.socket.emit("userConnect", userEmail);

    // When sharing a project, an event is emitted to the user's email channel. The user's client will then add the
    // project to the project view.
    $wnd.socket.on(userEmail, function(msg){
      var msgJSON = JSON.parse(msg);
      var projectId = String(msgJSON["project"]);
      $wnd.Ode_addSharedProject(projectId);
    });
  }-*/;

    public native void joinProject(String projectId) /*-{
    $wnd.socket.emit("projectChannel", projectId);
    $wnd.project = projectId;
    $wnd.DesignToolbar_removeAllJoinedUser();
    var msg = {
      "project": projectId,
      "user": $wnd.userEmail
    };
    $wnd.socket.emit("userJoin", msg);
    if(!$wnd.userColorMap.has(projectId)){
      $wnd.userColorMap.set(projectId, new $wnd.Map());
      $wnd.userColorMap.get(projectId).rmv = $wnd.userColorMap.get(projectId)["delete"];
    }

    //// PREVIOUSLY SCREEN CHANNEL SPECIFIC

    // userLastSelection is the latest block selected by other user, mapped by user email
    // lockedComponent is the components locked by other users. Key is the component id, value is userEmail and timestamp
    // lockedBlock is the block locked by other users. Key is the block id, value is userEmail and timestamp


    // Contains all the Blockly Workspaces
    $wnd.workspaces = Blockly.allWorkspaces;

    console.log(Object.keys($wnd.workspaces));

    if($wnd.AIFeature_enableComponentLocking()){
      // get status of this channel from other users
      var getStatusMsg = {
        "channel" : projectId,
        "user" : $wnd.userEmail,
        "source" : "GetStatus"
      };
      $wnd.socket.emit("getStatus", getStatusMsg);

      // Channel is this case stands for project id
      // Create a new lockComponents/Blocks set for each channel if it does not exist.
      if(!(projectId in $wnd.lockedComponentsByChannel)) {
        $wnd.lockedComponentsByChannel[projectId] = {};
      }
    }


    //// BACK TO PREVIOUS PROJECT CHANNEL LOGIC
    if($wnd.socketEvents[projectId]){
      return;
    }

    $wnd.socket.on(projectId, function(msg){
      var msgJSON = JSON.parse(msg);

      if(msgJSON["channel"]!=$wnd.project){
        return;
      }

      var color = "";
      var userFrom = msgJSON["user"];
      var userColor = msgJSON["userColor"];
      var colorMap = $wnd.userColorMap.get(msgJSON["channel"]);
      var blockly_workspace_name = msgJSON["channel"] + "_" + msgJSON["screen"];

      if(userFrom==$wnd.userEmail) {
        $wnd.userColor = userColor;
      } else {
        switch(msgJSON["source"]){
          case "join":
            if(!colorMap.has(userFrom) && msgJSON.userColor){
              // color = $wnd.colors.pop();
              colorMap.set(userFrom, userColor);
            }
            $wnd.DesignToolbar_addJoinedUser(userFrom, colorMap.get(userFrom));
            if($wnd.AIFeature_enableComponentLocking()){
              if(Blockly.mainWorkspace && Blockly.mainWorkspace.getParentSvg()
                  && !Blockly.mainWorkspace.getParentSvg().getElementById("blocklyLockedPattern-"+userFrom)){
                Blockly.Collaboration.createPattern(userFrom, $wnd.userColorMap.get(msgJSON["project"]).get(userFrom));
              }
            }
            break;
          case "leave":
            if(colorMap.has(userFrom)){
              c = colorMap.get(userFrom);
              // $wnd.colors.push(c);
              colorMap.rmv(userFrom);
            }
            $wnd.DesignToolbar_removeJoinedUser(userFrom);
            if($wnd.AIFeature_enableComponentLocking()){
              for(var projectId in $wnd.lockedComponentsByChannel) {
                if(projectId == msgJSON["project"]) {
                  Blockly.Collaboration.removeLockedComponent(projectId, userFrom);
                }
              }
              for(var channel in $wnd.lockedBlocksByChannel) {
                if(channel == blockly_workspace_name) {
                  Blockly.Collaboration.removeLockedBlock(channel, userFrom);
                }
              }
            }
            break;

          //// PREVIOUSLY SCREEN CHANNEL SPECIFIC
          case "Designer":
            var componentEvent = AI.Events.ComponentEvent.fromJson(msgJSON["event"]);
            $wnd.Ode_disableBroadcast();
            componentEvent.run();
            $wnd.Ode_enableBroadcast();
            break;

          case "Block":
            // Get Blockly workspace specific to a screen
            var workspace = $wnd.workspaces[blockly_workspace_name];
            var newEvent = Blockly.Events.fromJson(msgJSON["event"], workspace);
            Blockly.Events.disable();
            switch (newEvent.type) {
              case Blockly.Events.CREATE:
                newEvent.run(true);
                var block = workspace.getBlockById(newEvent.blockId);
                if (workspace.rendered) {
                  if(workspace.getParentSvg().parentNode.offsetParent){
                    block.initSvg();
                    block.render();
                  } else {
                    workspace.blocksNeedingRendering.push(block);
                  }
                } else {
                  workspace.blocksNeedingRendering.push(block);
                }
                break;
              case Blockly.Events.MOVE:
                if($wnd.AIFeature_enableComponentLocking()){
                  new AI.Events.UnlockBlock(blockly_workspace_name, newEvent.blockId, userFrom).run();
                }
                newEvent.run(true);
                new AI.Events.SelectBlock(blockly_workspace_name, newEvent.blockId, userFrom).run();
                break;
              case Blockly.Events.UI:
                new AI.Events.SelectBlock(blockly_workspace_name, newEvent.newValue, userFrom).run();
                break;
              default:
                newEvent.run(true);
            }
            Blockly.Events.enable();
            break;

          // Status from users.
          case "Status":
            if(msgJSON["lockedComponentId"]){
              new AI.Events.LockComponent(projectId, {id: msgJSON["lockedComponentId"], userEmail: userFrom}).run();
            }
            if(msgJSON["lockedBlockId"]){
              new AI.Events.SelectBlock(blockly_workspace_name, msgJSON["lockedBlockId"], userFrom).run();
            }
            break;

         // If someone asked for the status.
          case "GetStatus":
            var msg = {
              "channel" : projectId,
              "user" : $wnd.userEmail,
              "source" : "Status"
            };
            if(projectId in $wnd.userLockedComponent){
              msg["lockedComponentId"] = $wnd.userLockedComponent[projectId];
            }
            if(projectId in $wnd.userLockedBlock){
              msg["lockedBlockId"] = $wnd.userLockedBlock[projectId];
            }
            // Emits the status back to the sender.
            $wnd.socket.emit("status", msg);
            break;

          case "Media":
            console.log(msgJSON);
            $wnd.CollaborationManager_updateAsset(msgJSON["type"], msgJSON["projectId"], msgJSON["fileName"]);
            break;

          case "ScreenAdd":
            console.log(msgJSON);
            $wnd.CollaborationManager_updateScreenAdd(
                msgJSON["type"],
                msgJSON["projectId"],
                msgJSON["formFileId"],
                msgJSON["blocksFileId"],
                msgJSON["modDate"],
                msgJSON["formName"]
            );
            break;

          case "ScreenRemove":
            console.log(msgJSON);
            $wnd.CollaborationManager_updateScreenRemove(
                msgJSON["type"],
                msgJSON["projectId"],
                msgJSON["nodeFileId"],
                msgJSON["formFileId"],
                msgJSON["blocksFileId"],
                msgJSON["yailFileId"],
                msgJSON["modDate"],
                msgJSON["formName"]
            );
            break;
        }
      }
    });
    $wnd.socketEvents[projectId] = true;
  }-*/;

    public native void leaveProject()/*-{
    var msg = {
      "project": $wnd.project,
      "user": $wnd.userEmail,
      "userColor": $wnd.userColor
    };
    $wnd.project = "";

    // This no longer does anything since migrating to all project-channel based.
    $wnd.CollaborationManager_setCurrentScreenChannel("");
    $wnd.socket.emit("userLeave", msg);
    // Stop listening to this project channel.
    $wnd.socket.off($wnd.project)
  }-*/;

    public native void switchLeader(String leaderId, String leaderEmail)/*-{
    var msg = {
      "project": $wnd.project,
      "user": $wnd.userEmail,
      "leader": leaderId,
      "leaderEmail": leaderEmail
    }
    $wnd.socket.emit("leader", msg);
  }-*/;

    public static native boolean isComponentLocked(String channel, String userEmail, String componentId) /*-{
    // temporary hack
    if (channel in $wnd.lockedComponentsByChannel) {
        if(componentId in $wnd.lockedComponentsByChannel[channel]){
          if($wnd.lockedComponentsByChannel[channel][componentId]!=userEmail) {
            return true;
          }
        }
    }
    return false;
  }-*/;

    public static native boolean isBlockLocked(String channel, String userEmail, String blockId) /*-{
    if(blockId in $wnd.lockedBlocksByChannel[channel]){
      if($wnd.lockedBlocksByChannel[channel][blockId]!=userEmail) {
        return true;
      }
    }
    return false;
  }-*/;

    public static native void updateSourceTree(String channel, String screenName, String userEmail) /*-{
    var lockedComponent = $wnd.lockedComponentsByChannel[channel];
    var editor = top.getDesignerForForm(channel, screenName);
    for (var componentId in lockedComponent) {
      if (lockedComponent.hasOwnProperty(componentId)) {
        var component = editor.getComponentByUuid(componentId);
        var userLocked = lockedComponent[componentId];
        if (component) {
            if(userEmail!=userLocked){
              if($wnd.userColorMap.get(editor.projectId).has(userLocked)){
                component.setItemBackgroundColor($wnd.userColorMap.get(editor.projectId).get(userLocked));
              }
            }
        }
      }
    }
  }-*/;

    public native void setWorkspaceReadOnly(String projectId, boolean readOnly) /*-{
    for(var formName in Blockly.allWorkspaces){
      if(formName.split('_')[0]==projectId){
        Blockly.allWorkspaces[formName].options.readOnly = readOnly;
      }
    }
    return;
  }-*/;

    public static void setCurrentScreenChannel(String channel) {
        Ode.getInstance().getCollaborationManager().setScreenChannel(channel);
    }

    public static void updateScreenAdd(String type, String projectIdString, String formFileId, String blocksFileId, String modDateString, String formName){
        long projectId = Long.parseLong(projectIdString);
        long modDate = Long.parseLong(modDateString);
        final Ode ode = Ode.getInstance();
        final YoungAndroidProjectNode projectRootNode = (YoungAndroidProjectNode) ode.getCurrentYoungAndroidProjectRootNode();
        final Project project = ode.getProjectManager().getProject(projectRootNode);
        final YoungAndroidPackageNode packageNode = projectRootNode.getPackageNode();

        if(type.equals(SCREEN_ADD)){
            ode.updateModificationDate(projectId, modDate);

            // Add the new form and blocks nodes to the project
            project.addNode(packageNode, new YoungAndroidFormNode(formFileId));
            project.addNode(packageNode, new YoungAndroidBlocksNode(blocksFileId));

            // Add the screen to the DesignToolbar.
            // We need to do this once the form editor and blocks editor have been
            // added to the project editor (after the files are completely loaded).
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    ProjectEditor projectEditor =
                            ode.getEditorManager().getOpenProjectEditor(project.getProjectId());
                    FileEditor formEditor = projectEditor.getFileEditor(formFileId);
                    FileEditor blocksEditor = projectEditor.getFileEditor(blocksFileId);
                    if (formEditor != null && blocksEditor != null && !ode.screensLocked()) {
                        DesignToolbar designToolbar = Ode.getInstance().getDesignToolbar();
                        long projectId = formEditor.getProjectId();
                        designToolbar.addScreen(projectId, formName, formEditor,
                                blocksEditor);
                    } else {
                        // The form editor and/or blocks editor is still not there. Try again later.
                        Scheduler.get().scheduleDeferred(this);
                    }
                }
            });

        }
    }

    public static void updateScreenRemove(String type, String projectIdString, String nodeFileId, String formFileId, String blocksFileId, String yailFileId, String modDateString, String formName){
        long projectId = Long.parseLong(projectIdString);
        long modDate = Long.parseLong(modDateString);
        final Ode ode = Ode.getInstance();
        final YoungAndroidProjectNode projectRootNode = (YoungAndroidProjectNode) ode.getCurrentYoungAndroidProjectRootNode();
        final ProjectNode node = ode.getCurrentYoungAndroidProjectRootNode().findNode(nodeFileId);

        if(type.equals(SCREEN_REMOVE)){
            // We need to close both the form editor and the blocks editor
            String[] fileIds = new String[2];
            fileIds[0] = formFileId;
            fileIds[1] = blocksFileId;
            ode.getEditorManager().closeFileEditors(projectId, fileIds);

            Project project = ode.getProjectManager().getProject(node);

            // Remove all related nodes (form, blocks, yail) from the project.
            for (ProjectNode sourceNode : node.getProjectRoot().getAllSourceNodes()) {
                if (sourceNode.getFileId().equals(formFileId)
                        || sourceNode.getFileId().equals(blocksFileId)
                        || sourceNode.getFileId().equals(yailFileId)) {
                    project.deleteNode(sourceNode);
                }
            }
            ode.getDesignToolbar().removeScreen(project.getProjectId(), formName);
            ode.updateModificationDate(projectId, modDate);
        }
    }



    public static void updateAsset(String type, String projectIdString, String fileName) {
        long projectId = Long.parseLong(projectIdString);
        Project project = Ode.getInstance().getProjectManager().getProject(projectId);
        if(type.equals(FILE_UPLOAD)){
            YoungAndroidAssetsFolder assetsFolder = ((YoungAndroidProjectNode) project.getRootNode()).getAssetsFolder();
            FileUploadWizard.finishUpload(assetsFolder, fileName, null);
            return;
        }
        if (type.equals(FILE_DELETE)) {
            ProjectNode deleteNode = project.getRootNode().findNode(fileName);
            project.deleteNode(deleteNode);
            return;
        }
        return;
    }

    public static native void exportToJavascriptMethod()/*-{
    $wnd.CollaborationManager_isComponentLocked =
        $entry(@com.google.appinventor.client.CollaborationManager::isComponentLocked(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
    $wnd.CollaborationManager_isBlockLocked =
        $entry(@com.google.appinventor.client.CollaborationManager::isBlockLocked(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
    $wnd.CollaborationManager_setCurrentScreenChannel =
        $entry(@com.google.appinventor.client.CollaborationManager::setCurrentScreenChannel(Ljava/lang/String;));
    $wnd.CollaborationManager_updateAsset =
        $entry(@com.google.appinventor.client.CollaborationManager::updateAsset(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
    $wnd.CollaborationManager_updateScreenAdd =
        $entry(@com.google.appinventor.client.CollaborationManager::updateScreenAdd(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
    $wnd.CollaborationManager_updateScreenRemove =
        $entry(@com.google.appinventor.client.CollaborationManager::updateScreenRemove(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
  }-*/;

    //(String type, Long projectId, String nodeFileId, String formFileId, String blocksFileId, String yailFileId, Long modDate)
}