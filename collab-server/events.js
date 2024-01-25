var debug = false;
var log = true;

var debugging = function(msg) {
    if (debug){
        console.log(msg);
    }
};

var logging = function(obj) {
    if (log){
        console.log(JSON.stringify(obj));
    }
};

// Each project id in this map contains a set of all the users who joined.
var projectJoinedUser = new Map();
var projectAvailColors = new Map();
const colors = ['#999999','#f781bf','#a65628','#ffff33','#ff7f00','#984ea3','#4daf4a','#377eb8','#e41a1c'];

// These socket.on() events are mostly just subscriptions. The socket event names should be clearer but I'm not going to refactor it now.
var events = function(io){
    io.on('connection', function(socket){
        var subscribedChannel = new Set();
        var userEmail = "";
        var projectID = "";
        var screenChannels   = new Set();

        // Subscribe to user channel
        // TODO: See if this can be removed.
        socket.on('userConnect', function(msg){
            userEmail = msg;
            debugging(userEmail + " saved to server");
        });

        // The project channel will also handle all screen changes now.
        // TODO: See if this can be removed.
        socket.on('projectChannel', function(passedProjectId){
            projectID = passedProjectId;
            debugging(userEmail + " subscribe to project channel "+passedProjectId);
        });

        // Publish changes to user channel when a project is shared
        socket.on('shareProject', function(msg){
            // The msg["channel"] stands for the userEmail we want to share it with.
            socket.broadcast.emit(msg["channel"], JSON.stringify(msg));

            var lmsg = {
                timestamp : Date.now(),
                user : userEmail,
                projectId : msg["project"],
                source : "Other",
                eventType: "share",
                shareTo: msg["channel"]
            }
            logging(lmsg);
            debugging(userEmail + " on shareProject "+msg);
        });

        // Publish changes to project channel when a user opens a project
        socket.on('userJoin', function(msg){
            debugging(userEmail+" on userJoin "+ msg);

            // The current project id the user has open.
            projectID = msg["project"];

            const uemail = msg["user"]
            let joinedUsers = {};
            let availColors = []

            // Create a set of joined users for the project if one does not already exist. If it does, just get the set of joined users.
            if (projectJoinedUser.has(msg["project"])){
                joinedUsers = projectJoinedUser.get(msg["project"]);
            } else {
                joinedUsers = new Set();
                projectJoinedUser.set(msg["project"], joinedUsers);
            }

            console.log(projectAvailColors)

            // Create a set of available colors for users for the project if one does not already exist. If it does, just get the set of available colors.
            if (projectAvailColors.has(msg["project"])){
                availColors = projectAvailColors.get(msg["project"]);
            } else {
                availColors = colors;
                projectAvailColors.set(msg["project"], colors);
            }

            console.log("availColors", availColors)

            // Assign next color to new user
            // joinedUsers.add(msg["user"]);
            // let userColor = colors[colors.length % (colors.length - Object.keys(joinedUsers).length)];
            let userColor = availColors.pop();
            joinedUsers[uemail] = userColor;

            projectAvailColors.set(msg["project"], availColors);

            console.log(joinedUsers)

            for (const [user, color] of Object.entries(joinedUsers)) {
                var pubMsg = {
                    "channel": msg["project"],
                    "source" : "join",
                    "user" : user,
                    "userColor": color
                };
                // Emit to the user who just joined all the users who have joined.
                socket.emit(msg["project"], JSON.stringify(pubMsg));
                console.log("pubMsg", pubMsg)
            };

            var pubSelf = {
                "channel": msg["project"],
                "source" : "join",
                "user" : uemail,
                "userColor": userColor
            };

            console.log("pubSelf", pubSelf)

            if(msg["project"]){
                // Send that the user just joined to all the other already existing users in the project.
                socket.broadcast.emit(msg["project"], JSON.stringify(pubSelf));
                var lmsg = {
                    timestamp : Date.now(),
                    user : userEmail,
                    projectId : msg["project"],
                    source : "user.join",
                }
                logging(lmsg);
            }
        });

        // Publish changes to project channel when a user closes a project
        socket.on('userLeave', function(msg){
            debugging(userEmail+" on userLeave "+ msg);
            projectID = "";
            var pubMsg = {
                "channel": msg["project"],
                "source" : "leave",
                "user" : msg["user"]
            };

            // Delete the user from the project user list they were in.
            if(projectJoinedUser.has(msg["project"])){
                projectJoinedUser.get(msg["project"]).delete(msg["user"]);
            }

            // Add the color back into the project's available color
            if (projectAvailColors.has(msg["project"])) {
                projectAvailColors.get(msg["project"]).push(msg["userColor"]);
            }

            if(msg["project"]){
                socket.broadcast.emit(msg["project"], JSON.stringify(pubMsg));
                var lmsg = {
                    timestamp : Date.now(),
                    user : userEmail,
                    projectId : msg["project"],
                    source : "user.leave",
                }
                logging(lmsg);
            }
        });


        // Publish changes to screen channel when blocks changed
        socket.on('block', function(msg){
            socket.broadcast.emit(msg["channel"], JSON.stringify(msg));

            // Logging
            debugging(userEmail+" on block "+ msg);
            var proj = msg["channel"].split("_")[0];
            var evt = msg["event"];
            switch (evt["type"]) {
                case "create":
                case "delete":
                    var lmsg = {
                        timestamp : Date.now(),
                        user : userEmail,
                        projectId : proj,
                        source : "Block",
                        eventType: evt["type"],
                        blockId: evt["blockId"]
                    }
                    logging(lmsg);
                    break;
                case "move":
                    var lmsg = {
                        timestamp : Date.now(),
                        user : userEmail,
                        projectId : proj,
                        source : "Block",
                        eventType: evt["type"],
                        blockId: evt["blockId"],
                        parentId: evt["newParentId"]
                    }
                    logging(lmsg);
                    break;
                case "change":
                    var lmsg = {
                        timestamp : Date.now(),
                        user : userEmail,
                        projectId : proj,
                        source : "Block",
                        eventType: evt["type"],
                        blockId: evt["blockId"],
                        propertyName: evt["name"]
                    }
                    logging(lmsg);
                    break;
                default:
                    break;
            }
        });

        // Designer events, i.e. the YaFormEditor i.e. the phone screen page.
        socket.on('component', function(msg){
            socket.broadcast.emit(msg["channel"], JSON.stringify(msg));

            // Logging
            debugging(userEmail+" on component "+ msg);
            var evt = msg["event"];
            switch (evt["type"]) {
                case "component.create":
                case "component.delete":
                    var lmsg = {
                        timestamp : Date.now(),
                        user : userEmail,
                        projectId : evt["projectId"],
                        source : "Designer",
                        eventType: evt["type"],
                        componentId: evt["componentId"]
                    }
                    logging(lmsg);
                    break;
                case "component.move":
                    var lmsg = {
                        timestamp : Date.now(),
                        user : userEmail,
                        projectId : evt["projectId"],
                        source : "Designer",
                        eventType: evt["type"],
                        componentId: evt["componentId"],
                        parentId: evt["parentId"]
                    }
                    logging(lmsg);
                    break;
                case "component.property":
                    var lmsg = {
                        timestamp : Date.now(),
                        user : userEmail,
                        projectId : evt["projectId"],
                        source : "Designer",
                        eventType: evt["type"],
                        componentId: evt["componentId"],
                        propertyName: evt["property"]
                    }
                    logging(lmsg);
                    break;
                default:
                    break;
            }
        });

        // The "channel" name here is so ambiguous and hard to decipher without looking at the design paper. Whatever. 
        // It's either a user channel, project channel, or formerly a "screen" channel.
        // publish latest status
        socket.on("status", function(msg){
            socket.broadcast.emit(msg["channel"], JSON.stringify(msg));
        });

        // get status from others
        socket.on("getStatus", function(msg){
            socket.broadcast.emit(msg["channel"], JSON.stringify(msg));
        });

        // file upload
        socket.on("file", function(msg){
            socket.broadcast.emit(msg["channel"], JSON.stringify(msg));
        });


        //disconnection
        // socket is deleted on disconnect, so all the event handlers are automatically cleaned up.
        socket.on("disconnect", function(){
            debugging(userEmail+" connection is off");
            var pubMsg = {
                "project": projectID,
                "type" : "leave",
                "user" : userEmail
            };

            if(projectJoinedUser.has(projectID)){
                projectJoinedUser.get(projectID).delete(userEmail);
            }

            if(projectID!=""){
                socket.broadcast.emit(projectID, JSON.stringify(pubMsg));
                var lmsg = {
                    timestamp : Date.now(),
                    user : userEmail,
                    projectId : projectID,
                    source : "Other",
                    eventType: "user.leave"
                }
                logging(lmsg);
            }
        });
    });
}

module.exports = events;
