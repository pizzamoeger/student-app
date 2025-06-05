/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

// const {onRequest} = require("firebase-functions/v2/https");
// const logger = require("firebase-functions/logger");

const {onSchedule} = require("firebase-functions/v2/pubsub");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendScheduledNotifications = onSchedule("every 1 minutes",
    async (event) => {
      const now = Date.now();

      const notificationsRef = admin.firestore().collection("notifications");
      const snapshot = await notificationsRef.where("scheduledTime", "<=", now)
          .where("sent", "==", false).get();

      if (snapshot.empty) {
        console.log("No notifications to send");
        return null;
      }

      const batch = admin.firestore().batch();

      const sendPromises = [];

      snapshot.forEach((doc) => {
        const data = doc.data();
        const message = {
          token: data.fcmToken,
          notification: {
            title: data.title,
            body: data.body,
          },
          android: {
            notification: {
              channelId: "scheduled_notifications",
              sound: "default",
            },
          },
        };

        sendPromises.push(
            admin.messaging().send(message).then(() => {
              console.log(`Notification sent to token ${data.fcmToken}`);
              batch.update(doc.ref, {sent: true});
            }).catch((err) => {
              console.error("Error sending notification:", err);
            }),
        );
      });

      await Promise.all(sendPromises);
      return batch.commit();
    });

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
