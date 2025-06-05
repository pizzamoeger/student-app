const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendScheduledNotification = functions.pubsub
    .schedule("every day 08:00")
    .timeZone("Europe/Berlin")
    .onRun(async (context) => {
      const db = admin.firestore();

      try {
        const usersSnapshot = await db.collection("users").get();
        const tokens = [];

        usersSnapshot.forEach((doc) => {
          const data = doc.data();
          if (data.fcmToken) {
            tokens.push(data.fcmToken);
          }
        });

        const message = {
          notification: {
            title: "Good Morning!",
            body: "Don't forget to check your timetable for today.",
          },
        };

        const sendPromises = tokens.map((token) =>
          admin.messaging().send({...message, token}),
        );

        await Promise.all(sendPromises);
        console.log("Notifications sent!");
      } catch (error) {
        console.error("Error sending notifications:", error);
      }
    });
