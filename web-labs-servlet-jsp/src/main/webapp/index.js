$(document).ready(function () {
	var index = 0;
	function loadMessages() {
		$.get('talk?index=' + index, function(data) {
			var values = data.split('|');
			index = values[0];
			for (var i = 1; i < values.length; i++) {
				$('#messages').append(new Option(values[i], i));
			}
		});
	}
	$('#message').keyup(function (event) {
		if (event.key === "Enter"
			&& $('#name').val().length > 0
			&& $('#message').val().length > 0) {
			$.post('talk', $('#name').val() + ' : ' + $('#message').val(), function () {});
			$('#message').val('');
		}
	});
	setInterval(loadMessages, 1000);
});