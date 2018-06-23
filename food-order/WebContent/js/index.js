$(document).ready(function() {
	$('form#formaPrijava').submit(function(event) {
		event.preventDefault();
		let username = $('input[name="inputPrijavaEmail"]').val();
		let password = $('input[name="inputPrijavaLozinka"]').val();
		$('#error').hide();
		$.post({
			url: 'rest/login',
			data: JSON.stringify({username: username, password: password}),
			contentType: 'application/json',
			success: function(data) {
				alert(data);
			},
			
			error: function(message) {
				alert(message);
			}
		});
	});
});
