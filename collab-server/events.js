var redis = require('redis')

var debug = true;
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

var pub = redis.createClient();
// Each project id in this map contains a set of all the users who joined.
var projectJoinedUser = new Map();

// These socket.on() events are mostly just subscriptions. The socket event names should be clearer but I'm not going to refactor it now.
var events = function(io){
    io.on('connection', function(socket){
        var sub = redis.createClient();
        var subscribedChannel = new Set();
        var userEmail = "";
        var projectID = "";
        var screenChannels   = new Set();

        // Subscribe to own redis user channel
        socket.on('userChannel', function(passedEmail){
            if(!subscribedChannel.has(passedEmail)){
                subscribedChannel.add(passedEmail);
                userEmail = passedEmail;
                sub.subscribe(passedEmail);
                debugging(userEmail + " subscribe to user channel "+passedEmail);
            }
        });

        // Subscribe to project channel and pass the project id.
        // Further emissions will be sent to/from the redis project channel.
        // The project channel will also handle all screen changes now.
        socket.on('projectChannel', function(passedProjectId){
            if(!subscribedChannel.has(passedProjectId)){
                subscribedChannel.add(passedProjectId);
                projectID = passedProjectId;
                sub.subscribe(passedProjectId);
                debugging(userEmail + " subscribe to project channel "+passedProjectId);
            }
        });

        // Subscribe to a screen channel, each socket should only have one screen channel on fly
        // TODO(xinyue): Modify this to support multiple screens, user should subscribe all screen channels
        // in the project when user joins the project, and ubsubscribe
        // all screen channels when leave the project.
        socket.on("screenChannel", function(newScreenChannel){
            if(!screenChannels.has(newScreenChannel)){
                screenChannels.add(newScreenChannel);
            } 
            
            // if(screenChannel!==""){
            //     sub.unsubscribe(screenChannel);
            //     debugging(userEmail + " unsubscribe to screen channel "+ screenChannel);
            // }
            // screenChannel = newScreenChannel;
            sub.subscribe(newScreenChannel);
            debugging(userEmail + " subscribe to screen channel "+ newScreenChannel);
        });

        // Publish changes to user channel when a project is shared
        socket.on('shareProject', function(msg){
            // The msg["channel"] stands for the userEmail we want to share it with.
            pub.publish(msg["channel"], JSON.stringify(msg));

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

            var joinedUsers;

            // Create a set of joined users for the project if one does not already exist. If it does, just get the set of joined users.
            if (projectJoinedUser.has(msg["project"])){
                joinedUsers = projectJoinedUser.get(msg["project"]);
            } else {
                joinedUsers = new Set();
                projectJoinedUser.set(msg["project"], joinedUsers);
            }
            
            joinedUsers.add(msg["user"]);
            // Send the the names of all the joined users to the user who just joined the project. 
            // To the user, it is like they all just joined ath the same time they did. 
            // This is unclear coding since they use the same type as below. Oh well.
            joinedUsers.forEach(function(e){
                var pubMsg = {
                    "channel": msg["project"],
                    "source" : "join",
                    "user" : e
                };
                socket.emit(msg["project"], JSON.stringify(pubMsg));
            });

            var pubSelf = {
                "channel": msg["project"],
                "source" : "join",
                "user" : userEmail
            };

            if(msg["project"]){
                // Send that the user just joined to all the other already existing users in the project.
                pub.publish(msg["project"], JSON.stringify(pubSelf));
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

            if(msg["project"]){
                pub.publish(msg["project"], JSON.stringify(pubMsg));
                var lmsg = {
                    timestamp : Date.now(),
                    user : userEmail,
                    projectId : msg["project"],
                    source : "user.leave",
                }
                logging(lmsg);
            }
        });

        // This isn't used at the moment. "Leader" collaboration model where only one person makes the changes.
        socket.on('leader', function(msg){
            debugging(userEmail+" on leader "+msg);
            var pubMsg = {
                "project" : msg["project"],
                "type" : "leader",
                "user" : msg["user"],
                "leader" : msg["leader"],
                "leaderEmail" : msg["leaderEmail"]
            };
            pub.publish(msg["project"], JSON.stringify(pubMsg));
        });

        // Publish changes to screen channel when blocks changed
        socket.on('block', function(msg){
            debugging(userEmail+" on block "+ msg);
            pub.publish(msg["channel"], JSON.stringify(msg));
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
            debugging(userEmail+" on component "+ msg);
            pub.publish(msg["channel"], JSON.stringify(msg));

            // The below is just for logging.
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
            pub.publish(msg["channel"], JSON.stringify(msg));
        });

        // get status from others
        socket.on("getStatus", function(msg){
            pub.publish(msg["channel"], JSON.stringify(msg));
        });

        // file upload
        socket.on("file", function(msg){
            pub.publish(msg["channel"], JSON.stringify(msg));
        });

        // receive subscribe message
        sub.on('message', function(ch, msg){
            debugging(userEmail + " receive message on "+ch+" msg: "+msg);
            // Socket.emit only sends it to that one user.
            // Yes, it should use io.emit/broadcast and what not.
            // Redis is not used in the intended way here. Redis should be used to synchronize socket servers. Oh well.
            socket.emit(ch, msg);
        });

        //disconnection
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
                pub.publish(projectID, JSON.stringify(pubMsg));
                var lmsg = {
                    timestamp : Date.now(),
                    user : userEmail,
                    projectId : projectID,
                    source : "Other",
                    eventType: "user.leave"
                }
                logging(lmsg);
                projectID = "";
            }
            // Manually disconnect the client connection.
            sub.quit()
        });
    });
}

module.exports = events;
