/* Ties a protocol into flyer.js, to use between frames */

//very primitive button click functionality
function broadcast_click(clickName) {
    flyer.broadcast({
	channel: "demo",
	topic: "button.clicked",
	data: {clickName: clickName},
    });
}

//subscriptiong to primitive button functionality
function subscribe_click(clickName, callback) {
    flyer.subscribe({
	channel: "demo",
	topic: "button.clicked",
	callback: function(data) {
	    if (data.clickName == clickName) {
		callback(data);
	    }
	}
    });
}
