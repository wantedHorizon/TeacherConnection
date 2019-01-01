

'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/notifications/{UserId}/{notification_id}').onWrite((change, context) =>
{
    const UserId = context.params.UserId;
    const notification_id = context.params.notification_id;

    console.log('The user Id is : ', UserId);

    if(!change.after.exists())
    {
        return console.log('A Notification has been deleted from the database : ', notification_id);
    }

    if (!change.after.exists())
    {
        return console.log('A notification has been deleted from the database:', notification);
        //return null;
    }

    const deviceToken = admin.database().ref(`/users/${UserId}/device_token`).once('value');

    return deviceToken.then(result =>
    {
        const token_id = result.val();
        const payload = {
            notification : {
                title : "Friend Request",
                body : "You've received a new Connection Request",
                icon : "default"
            }
        };

        return admin.messaging().sendToDevice(token_id, payload).then(response => {

            console.log('This was the notification Feature');
             throw new Error("Invalid condition");

        });
    });
});
