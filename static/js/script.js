console.log("this is script file");

const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};

const search = () => {
	let keyword = $("#search-input").val();
	if (keyword == "") {
		$(".search-result").hide();
	} else {
		console.log(keyword);
		let url = `http://localhost:8080/search/${keyword}`;
		fetch(url)
			.then((response) => {
				return response.json();
			})
			.then((data) => {
				let text = `<div class='list-group'>`;
				data.forEach((contact) => {
					text += `<a href='/user/${contact.cid}/contactdetails' class='list-group-item list-group-item-action '> ${contact.cname}</a>`;
				});
				text += `</div>`;
				$(".search-result").html(text);
				$(".search-result").show();
			});
	}
};

const paymentStarted = () => {
	console.log("payment started..");
	let amount = $("#paymentfield").val().trim();
	console.log(amount);
	if (amount.trim() === '' || amount == null) {
		swal({
			title: "Error!",
			text: "Amount is required !",
			type: "error",
		});
		return;
	}
	$.ajax({
		url: '/user/createorder',
		data: JSON.stringify({ amount: amount, info: 'order_request' }),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function (response) {
			if (response.status == 'created') {
				displayRazorpay(response);
			}
		},
		error: function (error) {
			console.log(error);
			swal({
				title: "Error!",
				text: "Something went wrong !",
				type: "error",
			});
		}
	})
};

function displayRazorpay(response) {
	const options = {
		key: "Enter your razorpay key_id",
		amount: response.amount.toString(),
		currency: 'INR',
		name: "Contact Manager",
		description: "Test Transaction",
		image: 'http:/image/diamond.jpg',
		order_id: response.id,
		handler: function (response) {
			console.log(response.razorpay_payment_id);
			console.log(response.razorpay_order_id);
			console.log(response.razorpay_signature);
			updatePaymentOnServer(
				response.razorpay_payment_id,
				response.razorpay_order_id,
				"paid"
			);
		},
		prefill: {
			name: "",
			email: "",
			contact: "",
		},
		notes: {
			address: "Contact Manager",
		},
		theme: {
			color: "#61dafb",
		},
	};

	const rzp = new Razorpay(options);
	rzp.on("payment.failed", function (response) {
		console.log(response.error.code);
		console.log(response.error.description);
		console.log(response.error.source);
		console.log(response.error.step);
		console.log(response.error.reason);
		console.log(response.error.metadata.order_id);
		console.log(response.error.metadata.payment_id);
		swal({
			title: "Error!",
			text: "Oops Payment Failed !",
			type: "error",
		});
	});
	rzp.open();
}

function updatePaymentOnServer(payment_id, order_id, status) {
	$.ajax({
		url: '/user/updateorder',
		data: JSON.stringify({ payment_id: payment_id, order_id: order_id, status: status }),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function (response) {
			console.log("Payment details successfully stored on the server.");
			swal("Payment is Successful");
		},
		error: function (error) {
			swal({
				title: "Successful!",
				text: "Your Payment is successful, but we did not get it on the server",
				type: "error",
			});
		}
	});
}
