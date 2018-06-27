var URL = 'http://localhost:8080/food-order/rest'
$(document).ready(function() {
	isLoggedIn();
	$('form#formaPrijava').submit(function(event) {
		event.preventDefault();
		let username = $('input[name="inputPrijavaKorIme"]').val();
		let password = $('input[name="inputPrijavaLozinka"]').val();
		$('#error').hide();
		$.post({
			url: URL + '/user/login',
			data: JSON.stringify({username: username, password: password}),
			contentType: 'application/json',
			success: function(data) {
				alert(data.responseText);
				
			},
			
			error: function(message) {
				alert(message.responseText);
			}
		});
		
		refresh();
	});
	
	$('form#formaRegistracija').submit(function(event) {
		event.preventDefault();
		let firstName = $('input[name="inputIme"]').val();
		let lastName = $('input[name="inputPrezime"]').val();
		let telephone = $('input[name="inputTelefon"]').val();
		let email = $('input[name="inputEmail"]').val();
		let username = $('input[name="inputKorIme"]').val();
		let password = $('input[name="inputLozinka"]').val();
		
		userData = JSON.stringify({
			firstName: firstName,
			lastName: lastName,
			telephone: telephone,
			email: email,
			username: username,
			password: password,
			}); 
		$('#error').hide();
		$.post({
			url: URL+ '/user/registration',
			data: userData,
			contentType: 'application/json',
			success: function(data) {
				alert(data.responseText);
			},
			
			error: function(message) {
				console.log(message);
				$('#error').text(message.responseText);
				$('#error').show();
			}
		});
	});
	
	$('#navBtnOdjava').click(function () {
		$.post({
	        url: URL + "/user/logout"
	    }).then(function (message) {	   
	    	$('#btnOdjava').hide();
	        refresh();
	    });
    });
	
	
});
function refresh() {
    location.reload(true);
}
function isLoggedIn() {
    $.ajax({
        url: URL + "/user/currentUser"
    }).then(function (data) {

        if (data == undefined) {
        	$('#navBtnPrijava').show();
            $('#navBtnReg').show();
            $('#navBtnOdjava').hide();
            $('#userPorudzbineTab').hide();
    		$('#userOmiljenoTab').hide();
    		$('#adminPanel').hide();
        	$('#dostavljacPanel').hide();
        	$('#userPanel').show();
            
        }else{
   
        
	        if (data.role == "USER") {
	        	$('#userPanel').show();
	        		$('#userPorudzbineTab').show();
	        		$('#userOmiljenoTab').show();
	        	$('#adminPanel').hide();
	        	$('#dostavljacPanel').hide();
			}else
	        if (data.role == "ADMIN") {           	
	        	$('#userPanel').hide();
	        	$('#dostavljacPanel').hide();
	        	$('#adminPanel').show();
			}else
	        if (data.role == "DELIVERY") {           	
	        	$('#userPanel').hide();
	        	$('#dostavljacPanel').show();
	        	$('#adminPanel').hide();
			}
	        $('#navBtnPrijava').hide();
            $('#navBtnReg').hide();
            $('#navBtnOdjava').show();
	        
        }
        

    });
}