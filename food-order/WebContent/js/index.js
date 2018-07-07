var URL = 'http://localhost:8080/food-order/rest'

$(document).ready(function() {


	$(".categFilter").on('click', function(){
		$('#categRestFilter').find('.active').removeClass('active')
		$(this).addClass('active');
		var filter = $(this).attr('id');

		divs = $("#userSviRestorani").children();
		if (filter == 'allCateg') {
			divs.show();
			return;
		}
	    filter = filter.includes('_')?filter.replace("_", " "):filter;
	    for (i = 0; i < divs.length; i++) {
	    	name = divs.eq(i).find(".restCategory").text();
	        if (name.indexOf(filter) > -1) {
	        	divs.eq(i).show();
	        } else {
	        	divs.eq(i).hide();
	        }
	    }
       
    });
	
	$(document).on('click', '.btn', function(){
	    //alert($(this).attr('class'))
	    var btn = $(this);
	    var btnClass = $(this).attr('class');
	    if (btnClass.includes('editRestButton')) {
	    	getRestaurantForEdit(btn.attr('id').replace('editRest', ''));
		}
	    if (btnClass.includes('editItemButton')) {
	    	getItemForEdit(btn.attr('id').replace('editItem', ''));
		}
	    if (btnClass.includes('editVehicleButton')) {
	    	getVehicleForEdit(btn.attr('id').replace('editVehicle', ''));
		}
	    if (btnClass.includes('editUserButton')) {
	    	getUserForEdit(btn.attr('id').replace('editUser', ''));
		}
	    if (btnClass.includes('deleteRestButton')) {
	    	deleteRestaurant(btn.attr('id').replace('deleteRest', ''));
		}
	    if (btnClass.includes('deleteItemButton')) {
	    	deleteItem(btn.attr('id').replace('deleteItem', ''));
		}
	    if (btnClass.includes('deleteVehicleButton')) {
	    	deleteVehicle(btn.attr('id').replace('deleteVehicle', ''));
		}
	    if (btnClass.includes('favoriteBtn' ) && !btnClass.includes('btn-danger' )) {
	    	addToFavorite(btn.attr('id').replace('favorite', ''));
		}
	    if (btnClass.includes('favoriteBtn' ) && btnClass.includes('btn-danger' )) {
	    	removeFromFavorite(btn.attr('id').replace('favorite', ''));
		}
	    if (btnClass.includes('ponudaBtn' )) {
	    	getPonuduRestorana(btn.attr('id').replace('ponuda', ''));
		}
	    if (btnClass.includes('naruciItemBtn' )) {
	    	itemId = btn.attr('id').replace('naruci', '');
	    	restId = btn.closest('tr').find('.restId').text();
	    	getPonuduRestorana(restId, itemId);
		}
	    if (btnClass.includes('preuzPorudzButton' )) {
	    	orderId = btn.attr('id').replace('preuzPorudz', '');
	    	row = $(this).closest('tr');
	    	preuzmiPorudz(orderId);
		}
	    if (btnClass.includes('closeOrderDeliveryButton' )) {
	    	orderId = btn.attr('id').replace('closeOrderDelivery', '');
	    	completeOrderDelivery(orderId);
		}
	    if (btnClass.includes('alertOk' ) || btnClass.includes('alertClose') ) {
	    	$('#alertModal').find('#alertMessage').text('');
	    	$('#alertModal').find('#alertTitle').text('');
	    	$('#alertModal').modal('toggle')
		}
	
		function setFormData(form, data) {
			var formData = form.serializeArray();
			var unindexed_array = formData;
		    var indexed_array = {};

		   $.map(unindexed_array, function(n, i) {
		    indexed_array[n['name']] = n['value'];
		   });
			
			for ( var input in indexed_array) {
				$(form).find('input[name='+input+']').val(data[input]);
			}

		}
		
});


	isLoggedIn();

	//ZA TEST
	//getUsers();
	//getVehicles();
	//getItems();
	//getRestaurants();
	//getUsers();
	function logIn(){
		
	}
	$('form#formaPrijava').submit(function(event) {
		event.preventDefault();
		$('#errorLog').text('');
		let username = $('input[name="inputPrijavaKorIme"]').val();
		let password = $('input[name="inputPrijavaLozinka"]').val();
		$.post({
			url: URL + '/user/login',
			data: JSON.stringify({username: username, password: password}),
			contentType: 'application/json',
			success: function(data) {
            	showAlertModal('Uspesno ste se prijavili.');

				$('#modalPrijava').modal('toggle');
				if ($('#modalPonuda').find('h3').text() !== '' && data.role == 'Kupac') {
					$('#modalPonuda').modal('toggle');
				}else{
					isLoggedIn();
				}
				isLoggedIn();
			},error: function(message) {
				//alert(message.responseText);
				$('#errorLog').text(message.responseText);
				$('#errorLog').show();
				$('#errorLog').delay(5000).fadeOut('slow');
			}
		});
		
	//isLoggedIn();
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
            	showAlertModal('Uspesno ste se registrovali.');
            	$('#modalRegistracija').modal('toggle');
			},error: function(message) {
				$('#errorReg').text(message.responseText);
				$('#errorReg').show();
				$('#errorReg').delay(5000).fadeOut('slow');
			}
		});
	});
	$('form#formaPretragaRest').submit(function(event) {
		event.preventDefault();

		var form = $(this);
        var action = form.attr("action");
        var data = form.serializeArray();
        var formData = getFormData(data);
        $.ajax({
        	url: URL + "/restaurant/search",
        	type: action,
        	contentType: 'application/json',
        	data: JSON.stringify(formData),
        	success: function(data){
        		console.log(data);
        		$('#searchRestResult').empty();
        		var rests = data.restaurants;
        		if (data.restaurants != null && rests.length > 0) {
        			for (var i = 0; i < rests.length; i++) {
            			card = restaurantCard(rests[i]);
            			$('#searchRestResult').append(card);
    				}
				}else{
					$('#searchRestResult').append('<h3>Nema rezultata</h3>');
				}
        		
        	},
        	error: function(data){
        		console.log(data);
        	}
        });
	});
	
	$('form#formaPretragaItem').submit(function(event) {
		event.preventDefault();

		var form = $(this);
        var action = form.attr("action");
        var data = form.serializeArray();
        var formData = getFormData(data);
		
        $.ajax({
        	url: URL + "/item/search", 
        	type: action, 
        	contentType: 'application/json',
        	data: JSON.stringify(formData),
        	success: function(data){
        		console.log(data);
        		$('#searchItemResult').empty();
        		var items = data.items;
        		if (data.items != null && items.length > 0) {
        			for (var i = 0; i < items.length; i++) {
            			row = popItemTableRow(items[i]);
            			$('#searchItemResult').append(row);
    				}
				}else{
					$('#searchItemResult').append('<h3>Nema rezultata</h3>');
				}
        	},
        	error: function(message){
        		console.log(message);
        	}
        });
	});
		
	
	$("#navBtnPretraga").on('click', function(){
		$('#userPanel').hide();
		$('#searchPanel').show();
		
	});
	$("#sendOrder").on('click', function(){
		//getPonuduRestorana(id)
		
		var orderItems = [];
		var points = $("#usedPoints").val();
		var items = $("#ponudaRest").children();
		var note = $("#inputNapNar").val();
		var address = $("#inputOrdAddr").val();
		var quant;
		var id;
		for (var i = 0; i < items.length; i++) {
			var item = {
					id:"",
					quantity:""
			};
			id = items.eq(i).attr('id').replace('ponudaItem', '');
			quant = items.eq(i).find('.itemQuant').val();
			if (quant > 0) {
				item.id = id;
				item.quantity = quant;
				orderItems.push(item);
			}
		}
		oi = JSON.stringify(orderItems);
		data =JSON.stringify({
			orderItems:orderItems,
			note:note, 
			points:points,
			address:address
			
		});
		$.post({
			url: URL+ '/order',
			data: data,
			contentType: 'application/json',
			success: function(order) {
            	showAlertModal('Porudzbina je poslata.');

				row = orderUserTableRow(order);
				$('#userOrdersTable').append(row);
				$('#modalPonuda').modal('toggle');
				userPoints = sessionStorage.getItem('userPoints');
				sessionStorage.setItem('userPoints', Number(userPoints) - Number(points));
				clearOrderModal();
			},error: function(message) {
				if (message.status == '401') {
					$('#modalPonuda').modal('toggle');
					$('#modalPrijava').modal('toggle');
				}
				if (message.status == '400') {
					$('#errorOrder').text(message.responseText);
					$('#errorOrder').show();
					$('#errorOrder').delay(5000).fadeOut('slow');
				}
				
			}
		});


	});
	//Forma za dodavanje restorana
	
	$('form#formNoviRestoran').submit(function(){

		event.preventDefault();
		var form = $("#formNoviRestoran");
        var action = form.attr("action");
        var data = form.serializeArray();
        var formData = getFormData(data);
        $.ajax({
            url: URL + action, 
            type : "POST", 
            contentType: 'application/json', 
            data : JSON.stringify(formData),
            success : function(data) {
            	showAlertModal('Uspesno ste dodali restoran.');

                $('#selectRestaurantId').append($('<option>', { value : data.id }).text(data.name));
                $('#selectEditRestaurantId').append($('<option>', { value : data.id }).text(data.name));
                $('#itemSearchRestId').append($('<option>', { value : data.id }).text(data.name));
                $('#adminRestTable').append(restaurantTableRow(data));
                
                console.log(data);
            },
            error: function(xhr, resp, text) {
                console.log(xhr, resp, text);
            }
        })
        $('#modalNoviRestoran').modal('toggle');
        });
	//Izmena restorana
	$('form#formEditRestoran').submit(function(){
		event.preventDefault();
		var form = $("#formEditRestoran");
        var action = form.attr("action");
        var data = form.serializeArray();
        var formData = getFormData(data);

        var restId;
        $.ajax({
            url: URL + action, 
            type : "PUT", 
            contentType: 'application/json', 
            data : JSON.stringify(formData),
            success : function(data) {
            	showAlertModal('Restoran '+data.name+' je izmenjen.');

                //sad treba zameniti gde god se restoran nalazi
                restId = data.id;
                //zameni u selectu
                $('#selectRestaurantId option[value='+restId+']').text(data.name);
                $('#selectEditRestaurantId option[value='+restId+']').text(data.name);
                //zameni u kartici ili ne mora jer je admin a on nema prikaz kartica
                
                //zameni u admin tabeli
                row = restaurantTableRow(data);
                $('#rowRestaurant'+restId+'').replaceWith(row);
                $("#modalEditRestoran").modal('toggle');

            },
            error: function(message) {
                console.log(message.responseText);
                alert(message.responseText);
            }
        })
    });
	//Dodavanje artikla
	$('form#formNoviArtikal').submit(function(){
		event.preventDefault()
		var form = $("#formNoviArtikal");
        var action = form.attr("action");
        var data = form.serializeArray();
        var formData = getFormData(data);
        var restName = $("#selectRestaurantId option:selected").text();
        formData.restaurantName = restName;
        var newdata = [];
        $.ajax({
            url: URL + action, 
            type : "POST", 
            contentType: 'application/json', 
            data : JSON.stringify(formData),
            success : function(data) {
                
            	showAlertModal('Artikal '+data.name+' je dodat.');
                console.log(data);
                
                $('#adminItemTable').append(itemTableRow(data));
            },
            error: function(xhr, resp, text) {
                console.log(xhr, resp, text);
            }
        })
        $('#modalNoviArtikal').modal('toggle');

    });
	$('form#formEditArtikal').submit(function(){
		event.preventDefault();
		var form = $("#formEditArtikal");
        var action = form.attr("action");
        var data = form.serializeArray();
        var formData = getFormData(data);
        var restName = $("#selectEditRestaurantId option:selected").text();
        formData.restaurantName = restName;
        var restId;
        $.ajax({
            url: URL + action, 
            type : "PUT", 
            contentType: 'application/json', 
            data : JSON.stringify(formData),
            success : function(data) {
            	showAlertModal('Artikal '+data.name+' je izmenjen.');
                //sad treba zameniti gde god se restoran nalazi
                itemId = data.id;
                //zameni u admin tabeli
                row = itemTableRow(data);
                $('#rowItem'+itemId+'').replaceWith(row);
                $("#modalEditArtikal").modal('toggle');

            },
            error: function(xhr, resp, text) {
                console.log(xhr, resp, text);
            }
        })
    });
	//Dodavanje vozila
	$('form#formNovoVozilo').submit(function(){
		event.preventDefault();
		var form = $("#formNovoVozilo");
        var action = form.attr("action");
        var data = form.serializeArray();
        var formData = getFormData(data);

        $.ajax({
            url: URL + action, 
            type : "POST", 
            contentType: 'application/json', 
            data : JSON.stringify(formData),
            success : function(data) {
                
            	showAlertModal('Vozilo je uspesno dodato.');
                console.log(data);
                $('#adminVehicleTable').append(vehicleTableRow(data));
                $('#modalNovoVozilo').modal('toggle');
            },
            error: function(message) {
            	$('#inputVozError').text(message.responseText);
				$('#inputVozError').show();
				$('#inputVozError').delay(5000).fadeOut('slow');
            }
        })
       

    });
	$('form#formEditVozilo').submit(function(){
		event.preventDefault();
		var form = $("#formEditVozilo");
        var action = form.attr("action");
        var data = form.serializeArray();
        var formData = getFormData(data);
        var restId;
        $.ajax({
            url: URL + action, 
            type : "PUT", 
            contentType: 'application/json', 
            data : JSON.stringify(formData),
            success : function(data) {
                
                showAlertModal('Uspesno ste izmenili vozilo.', 'success');
                //zameni u admin tabeli
                row = vehicleTableRow(data);
                $('#rowVEhicle'+data.id+'').replaceWith(row);
                $("#modalEditVozilo").modal('toggle');

            },
            error: function(message) {
            	$('#inputEditVozError').text(message.responseText);
				$('#inputEditVozError').show();
				$('#inputEditVozError').delay(5000).fadeOut('slow');
            }
        })
    });
	$('form#formEditKorisnik').submit(function(){
		event.preventDefault();
		var form = $("#formEditKorisnik");
        var action = form.attr("action");
        var data = form.serializeArray();
        var formData = getFormData(data);
        var restId;
        $.ajax({
            url: URL + action, 
            type : "PUT", 
            contentType: 'application/json', 
            data : JSON.stringify(formData),
            success : function(data) {

                showAlertModal('Izmenjen korisnik '+data.username+'.', 'success');
                //zameni u admin tabeli
                row = userTableRow(data);
                $('#rowUser'+data.id+'').replaceWith(row);
                $("#modalEditKorisnik").modal('toggle');

            },
            error: function(xhr, resp, text) {
                console.log(xhr, resp, text);
            }
        })
    });
	function preuzmiPorudz(orderId){

		
		getVehiclesForDelivery();
		$('#modalDostavVozila').find('#orderId').val(orderId);
		$('#modalDostavVozila').modal('toggle');
	}
	$("#acceptVehicleAndOrder").on('click', function(){
		var vehicleId = $('input[name=radio]:checked').val();
		var orderId = $('#modalDostavVozila').find('#orderId').val();
		if (vehicleId == null || orderId == null) {
			return;
		}
		
		$.ajax({
			url: URL + "/delivery/accept/"+orderId+"/"+vehicleId,
			type: "POST",
			success: function(data){

				row = orderDostTableRow(data);
				$('#dostMyOrdersTable').append(row);
				$('#dostMyOrdersTable').find('#preuzPorudz'+data.id+'').closest('tr').remove();
				$('#modalDostavVozila').modal('toggle');
				$('#openOrder'+data.id+'').remove();
				showAlertModal('Preuzeta porudzbina broj ' + data.id);
				
			},
			error: function(message){
				showAlertModal(message.responseText);
			}
		});
		
	});
	function completeOrderDelivery(orderId){
		$.ajax({
			url: URL + "/delivery/complete/"+orderId,
			type: "POST",
			success: function(data){

				$("#closeOrderDelivery"+data.id).addClass(' btn-success');
				$("#closeOrderDelivery"+data.id).prop("disabled",true);
				$("#orderStatus"+data.id).text('Dostavljeno');
				showAlertModal('Porudzbina je dostavljena.');
			},
			error: function(xhr, resp, text){
				showAlertModal('Greska');
				console.log(xhr);
				console.log(resp);
				console.log(text);
			}
		});
		
	}
	function getPonuduRestorana(restId, itemId){

		var drinks = [];
		var meals = [];
		var row;
		$.get({
			url: URL + '/restaurant/'+restId,
			success: function(data) {
				$('#modalPonuda').find('h3').text(data.name);
				for (var i = 0; i < data.drinks.length; i++) {
					row = itemPonudaTableRow(data.drinks[i]);
					$('#ponudaRest').append(row);
				}
				for (var i = 0; i < data.meals.length; i++) {
					row = itemPonudaTableRow(data.meals[i]);
					$('#ponudaRest').append(row);
				}

				if (itemId !== null) {
					$('#modalPonuda').find('#ponudaItem'+itemId+'').addClass(' table-active')
				}
				userPoints = sessionStorage.getItem('userPoints');
				if (userPoints !== null) {
					$('#modalPonuda').find('#userPoints').text(userPoints);
				}else{
					$('#modalPonuda').find('#userPoints').text('0');
				}
				
				$('#modalPonuda').modal('toggle');
			},
			
			error: function(message) {
				alert(message.responseText);
			}
		});
	}
	function getFormData(data) {
		   var unindexed_array = data;
		   var indexed_array = {};

		   $.map(unindexed_array, function(n, i) {
		    indexed_array[n['name']] = n['value'];
		   });

		   return indexed_array;
		}
	function setFormData(form, data) {
		var formData = form.serializeArray();
		var unindexed_array = formData;
	    var indexed_array = {};

	   $.map(unindexed_array, function(n, i) {
	    indexed_array[n['name']] = n['value'];
	   });
		
		for ( var input in indexed_array) {
			$(form).find('input[name='+input+']').val(data[input]);
		}

	}
	function deleteRestaurant(id){
		console.log(id);
		var meals = [];
		var drinks = [];
		$.ajax({
            url: URL + '/restaurant/'+id, 
            type : "DELETE", 
            success : function(data) {
                showAlertModal(data);
                $("#selectRestaurantId option[value="+id+"]").remove()
                $("#selectEditRestaurantId option[value="+id+"]").remove();
                meals = data.meals;
                drinks = data.drinks;
                if (meals != null) {
                	for (var i = 0; i < meals.length; i++) {
                    	$('#rowItem'+meals[i].id+'').remove();
    				}
				}
                if (drinks != null) {
                	for (var i = 0; i < drinks.length; i++) {
                    	$('#rowItem'+drinks[i].id+'').remove();
    				}
				}
                
                $('#rowRestaurant'+id).remove();

            },
            error: function(message) {
            	showAlertModal(message.responseText);
            }
        })
	}
	function deleteItem(id){
		console.log(id);

		$.ajax({
            url: URL + '/item/'+id, 
            type : "DELETE", 
            success : function(data) {
            	showAlertModal(data);
            	$('#rowItem'+id+'').remove();  

                console.log(data);
            },
            error: function(message) {
            	showAlertModal(message.responseText);
            }
        })
	}
	function deleteVehicle(id){
		console.log(id);

		$.ajax({
            url: URL + '/vehicle/'+id, 
            type : "DELETE", 
            success : function(data) {
            	showAlertModal(data);
            	$('#rowVehicle'+id).remove();  

                console.log(data);
            },
            error: function(message) {
            	showAlertModal(message.responseText);
            }
        })
	}
	function addToFavorite(id){
		
		$.ajax({
            url: URL + '/restaurant/add-favorite/'+id, 
            type : "POST", 
            success : function(data) {
                alert('Dodat u omiljene' + data.name);
                
                row = restaurantCard(data, 'favorite');
                $('#userOmiljeniRestorani').append(row);
                $('#favorite'+data.id+'').addClass('btn-danger');

                console.log(data);
            },
            error: function(xhr, resp, text) {
                console.log(xhr, resp, text);
                alert(xhr.responseText);
                if (xhr.status == '400') {
                	$('#favorite'+id+'').addClass('btn-danger');
				}
               
            }
        })
	}
	function removeFromFavorite(id){
		
		$.ajax({
            url: URL + '/restaurant/remove-favorite/'+id, 
            type : "POST", 
            success : function(data) {
                alert('Uklonjen iz omiljenih' + data.name);
                
                card = restaurantCard(data);
                $('#userOmiljeniRestorani').find('#rest'+data.id+'').remove();
                $('#favorite'+data.id+'').removeClass('btn-danger');
                
                console.log(data);
            },
            error: function(xhr, resp, text) {
                console.log(xhr, resp, text);
                alert(xhr.responseText);
                if (xhr.status == '400') {
                	$('#favorite'+id+'').removeClass('btn-danger');
				}
            }
        })
	}
	function getRestaurantForEdit(id){
		form = $("#formEditRestoran");
		console.log(id);
		$.get({
			url: URL + '/restaurant/'+id,
			success: function(data) {
				
				$(form).find('input[name=id]').val(data.id);
				$(form).find('input[name=name]').val(data.name);
				$(form).find('input[name=address]').val(data.address);
				var categ = 0;
				if (data.restCateg == 'Domaca_kuhinja') {
					categ = 0;
				}
				if (data.restCateg == 'Rostilj') {
					categ = 1;
				}
				if (data.restCateg == 'Kineski_restoran') {
					categ = 2;
				}
				if (data.restCateg == 'Indijski_restoran') {
					categ = 3;
				}
				if (data.restCateg == 'Poslasticarnica') {
					categ = 4;
				}
				if (data.restCateg == 'Picerija') {
					categ = 5;
				}
				
				$("#selectEditKategRest").val(categ);
		    	$("#modalEditRestoran").modal('toggle');
			},
			
			error: function(message) {
				alert(message.responseText);
			}
		});
	}
	function getItemForEdit(id){
		form = $("#formEditArtikal");
		console.log(id);
		$.get({
			url: URL + '/item/'+id,
			success: function(data) {
				
				$(form).find('input[name=id]').val(data.id);
				$(form).find('input[name=name]').val(data.name);
				$(form).find('input[name=description]').val(data.description);
				$(form).find('input[name=price]').val(data.price);
				$(form).find('input[name=quantity]').val(data.quantity);
				var categ = 0;
				if (data.restCateg == 'Jelo') {
					categ = 0;
				}
				if (data.restCateg == 'Pice') {
					categ = 1;
				}
				$("#selectEditKategArt").val(categ);
				$("#selectEditRestaurantId").val(data.restaurantId);
		    	$("#modalEditArtikal").modal('toggle');
			},
			
			error: function(message) {
				alert(message.responseText);
			}
		});		
	}
	function getVehicleForEdit(id){
		form = $("#formEditVozilo");
		console.log(id);
		$.get({
			url: URL + '/vehicle/'+id,
			success: function(data) {
				
				$(form).find('input[name=id]').val(data.id);
				$(form).find('input[name=brand]').val(data.brand);
				$(form).find('input[name=model]').val(data.model);
				$(form).find('input[name=productionYear]').val(data.productionYear);
				$(form).find('input[name=numberPlate]').val(data.numberPlate);
				$(form).find('input[name=note]').val(data.note);
				var categ = 0;
				if (data.restCateg == 'BIKE') {
					categ = 0;
				}
				if (data.restCateg == 'MOPED') {
					categ = 1;
				}
				if (data.restCateg == 'CAR') {
					categ = 2;
				}
				
				$("#selectEditTipVoz").val(categ);
		    	$("#modalEditVozilo").modal('toggle');
			},
			
			error: function(message) {
				alert(message.responseText);
			}
		});		
	}
	function getUserForEdit(id){
		form = $("#formEditKorisnik");
		console.log(id);
		$.get({
			url: URL + '/user/'+id,
			success: function(data) {
				
				$(form).find('input[name=id]').val(data.id);
				$(form).find('input[name=firstName]').val(data.firstName);
				$(form).find('input[name=lastName]').val(data.lastName);
				var role = 0;
				if (data.role == 'Kupac') {
					role = 0;
				}
				if (data.role == 'Admin') {
					role = 1;
				}
				if (data.role == 'Dostavljac') {
					role = 2;
				}
				
				$("#selectEditUserRole").val(role);
		    	$("#modalEditKorisnik").modal('toggle');
			},
			
			error: function(message) {
				alert(message.responseText);
			}
		});		
	}		
	function getRestaurants(user){
		let userFavorites= [];
		if (user != null && user.role == 'Kupac') {
			for (var i = 0; i < user.favoriteRest.length; i++) {
				userFavorites.push( user.favoriteRest[i].id);
			}

		}
		
		$.ajax({
	        url: URL + "/restaurant"
	    }).then(function (data) {
	    	
	    	console.log(data);
	    	$('#adminRestTable').empty();
	    	$('#selectRestaurantId').empty();
	    	$('#selectEditRestaurantId').empty();
	    	$('#userSviRestorani').empty();
	    	$('#userOmiljeniRestorani').empty();
	    	for (var i = 0; i < data.length; i++) {
	    		if (data[i] !== null) {
											
		    		if (user == null) {
		    			$('#userSviRestorani').append(restaurantCard(data[i]));
		    			//$('.modalProba').append(restaurantCard(data[i]));
		    			$('#itemSearchRestId').append($('<option>', { value : data[i].id }).text(data[i].name));
		    			continue;
		    		}
		    		if(user.role == 'Admin'){
		    			//dodaj u modalni za unos artikla
		    			$('#selectRestaurantId').append($('<option>', { value : data[i].id }).text(data[i].name));
		    			$('#selectEditRestaurantId').append($('<option>', { value : data[i].id }).text(data[i].name));
		    			//dodaj u tabelu
		    			$('#adminRestTable').append(restaurantTableRow(data[i]));
		    			
		    		}
	    			if (user.role == 'Kupac') {
	    				$('#itemSearchRestId').append($('<option>', { value : data[i].id }).text(data[i].name));

	    				if(jQuery.inArray(data[i].id, userFavorites) !== -1){
	    					$('#userOmiljeniRestorani').append(restaurantCard(data[i], 'favorite'));
	    					$('#userSviRestorani').append(restaurantCard(data[i], 'favorite'));
	    					continue;
						}else{
							$('#userSviRestorani').append(restaurantCard(data[i]));
						}
	
					}
	    		}
	    		
	    	}
	    	
	    });
		    

	}
	function setFavoriteButton(rests){
		for ( var x in userFavorites) {
	    	 $('#userOmiljeniRestorani').find('#favorite'+userFavorites[x]+'').addClass(' btn-danger');
	    	 $('#userSviRestorani').find('#favorite'+userFavorites[x]+'').addClass(' btn-danger');
		}
	   
	}
	function getUsers(){
		$.ajax({
	        url: URL + "/user"
	    }).then(function (data) {
	    	
	    	console.log(data);
	    	$('#adminUserTable').empty();
	    	for (var i = 0; i < data.length; i++) {
	    		row = userTableRow(data[i])
	    		$('#adminUserTable').append(row);

	    	}
	    	
	    });
	}
	function getOrdersForDelivery(){
		$.ajax({
	        url: URL + "/order/open"
	    }).then(function (data) {
	    	$('#dostOpenOrdersTable').empty();
	    	for (var i = 0; i < data.length; i++) {
	    		row = openOrdesTableRow(data[i]);
	    		$('#dostOpenOrdersTable').append(row);

	    	}
	    })
	}
	function getAllOrders(){
		$.ajax({
	        url: URL + "/order"
	    }).then(function (data) {
	    	$('#adminOrderTable').empty();
	    	for (var i = 0; i < data.length; i++) {
	    		row = adminOrdesTableRow(data[i]);
	    		$('#adminOrderTable').append(row);

	    	}
	    })
	}
	function getItems(){
		$.ajax({
	        url: URL + "/item"
	    }).then(function (data) {
	    	$('#adminItemTable').empty();
	    	for (var i = 0; i < data.length; i++) {
	    		row = itemTableRow(data[i])
	    		$('#adminItemTable').append(row);

	    	}
	    });
	}
	function getPopularItems(){
		$.ajax({
	        url: URL + "/item/popularItems"
	    }).then(function (data) {
	    	$('#popularItems').empty();
	    	for (var i = 0; i < data.length; i++) {
	    		row = popItemTableRow(data[i])
	    		$('#popularItems').append(row);
	    	}
	    });
	}
	function getVehicles(){
		$.ajax({
	        url: URL + "/vehicle"
	    }).then(function (data) {
	    	$('#adminVehicleTable').empty();
	    	for (var i = 0; i < data.length; i++) {
	    		row = vehicleTableRow(data[i])
	    		$('#adminVehicleTable').append(row);
	    	}
	    });
	}
	function vehicleTableRow(data){
		var vehicleType;
		var status;
		if (data.vehicleType == 'CAR') {
			vehicleType = 'Automobil'
		}else
			if (data.vehicleType == 'MOPED') {
				vehicleType = 'Skuter'
			}else{
				
				vehicleType = 'Bicikl'
			}
		if (data.occupied == true) {
			status = 'Zauzet';
		}else
			status = 'Slobodan';
		row = '	<tr id="rowVehicle'+data.id+'">'
			+' 		<td>'+data.brand+'</td>'
			+' 		<td>'+data.model+'</td>'
			+'		<td>'+data.numberPlate+'</td>'
			+'		<td>'+data.productionYear+'</td>'
			+'		<td>'+vehicleType+'</td>'
			+'		<td id="vehicleStatus"'+data.id+'>'+status+'</td>'
			+'		<td>'+data.note+'</td>'
			+'		<td>'
			+'	      	<div class="btn-group btn-sm" role="group" >'
			+'			    <button type="button" class="btn btn-primary btn-sm editVehicleButton" id=editVehicle'+data.id+'>Izmeni</button>'
			+'			    <button type="button" class="btn btn-danger btn-sm deleteVehicleButton" id=deleteVehicle'+data.id+'>Izbrisi</button>'
			+'			</div>'
			+'      </td>'
		    +'</tr>';
		return row;
	}


	$("#cancelOrder").on('click', function(){
		clearOrderModal();
		
	});
	function clearOrderModal(){
		$('#ponudaRest').empty();
		$('#userPoints').text('');
		$('#totalPrice').text('');
		$('#usedPoints').val(0);
		$('#ponudaRestName').text('');
	}
	$(document).on('click', '.incQuantBtn', function() {                                                                                                                     
		var oldQuantElem = $(this).closest('tr').find('.itemQuant');
		oldQuant = Number(oldQuantElem.val());
		if (oldQuant >= 20) {
			return;
		}
		oldQuantElem.val(oldQuant+1);
		var cena = $(this).closest('tr').find('#price').text().replace(' RSD', '');
		 cena = Number(cena);
		 
		 var priceElem = $('#modalPonuda').find('#totalPrice');
		 var price = Number(priceElem.text());
		 var newPrice = price + cena;
		 priceElem.text(newPrice);
		 var discPoints = Number($('#modalPonuda').find('#usedPoints').val());
		 var discPrice =newPrice * (1 - (discPoints * 3)/100);
		 $('#modalPonuda').find('#discountPrice').text(discPrice.toFixed(2));
	});
	$(document).on('click', '.decQuantBtn', function() {                                                                                                                     
		var oldQuantElem = $(this).closest('tr').find('.itemQuant');
		oldQuant = Number(oldQuantElem.val());
		if (oldQuant <= 0) {
			return;
		}
		oldQuantElem.val(oldQuant-1);
		var cena = $(this).closest('tr').find('#price').text().replace(' RSD', '');
		 cena = Number(cena);
		 
		 var priceElem = $('#modalPonuda').find('#totalPrice');
		 var price = Number(priceElem.text());
		 var newPrice = price - cena;
		 priceElem.text(newPrice);
		 var discPoints = Number($('#modalPonuda').find('#usedPoints').val());
		 var discPrice = newPrice * (1 - (discPoints * 3)/100);
		 $('#modalPonuda').find('#discountPrice').text(discPrice.toFixed(2));
	});
	$(document).on('click', '.incBonusBtn', function() {                                                                                                                     
		var oldBonusElem = $(this).closest('tr').find('#usedPoints');
		oldBonus = Number(oldBonusElem.val());
		userPoints = sessionStorage.getItem("userPoints");
		if (oldBonus >= 10 || oldBonus >= userPoints) {
			return;
		}
		newBonus = oldBonus+1;
		oldBonusElem.val(newBonus);
		totPrice = Number($('#modalPonuda').find('#totalPrice').text());
		var discPrice = totPrice * (1 - (newBonus * 3)/100);
		$('#modalPonuda').find('#discountPrice').text(discPrice.toFixed(2));

		 //$('#modalPonuda').find('#discount').text((oldBonus + 1)*3 + '%');
		
	});
	$(document).on('click', '.decBonusBtn', function() {                                                                                                                     
		var oldBonusElem = $(this).closest('tr').find('#usedPoints');
		oldBonus = Number(oldBonusElem.val());
		userPoints = sessionStorage.getItem("userPoints");
		if (oldBonus <= 0 || userPoints <= 0 ) {
			return;
		}
		newBonus = oldBonus-1;
		oldBonusElem.val(newBonus);
		totPrice = Number($('#modalPonuda').find('#totalPrice').text());
		var discPrice = totPrice * (1 - (newBonus * 3)/100);
		$('#modalPonuda').find('#discountPrice').text(discPrice.toFixed(2) );
	});
	$(document).on('click', '.itemQuant', function() {                                                                                                                     
		var oldQuantElem = $(this);
		oldQuant = Number(oldQuantElem.val());
		
	});
	function orderUserTableRow(data){
		var newItem;
		if (data.delivery == null) {
			dostavljac = 'Nema'
		}else{
			dostavljac = data.dostavljac;
		}
	row='			<tr>'
		+'		      <td>'+data.id+'</td>'
		+'		      <td>'+data.orderDate+' '+data.orderTime+'</td>'
		+'		      <td>'+dostavljac+'</td>'
		+'		      <td>'+data.orderStatus+'</td>'
		+'		      <td>'+data.note+'</td>'
		+'		      <td>'+data.totalPrice+'</td>'
		+'		      <td>'+data.discount+'</td>'
		+'		      <td>'
		+'		      	<div class="dropdown">'
		+'				  <button type="button" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown">'
		+'				    Artikli'
		+'				  </button>'
		+'				  <div class="dropdown-menu dropdown-menu-right">'
		+'				    <table class="table table-sm dropdown-item">'
		+'						<thead>'
		+'							<tr>'		
		+'								<th>Naziv</th>'
		+'							    <th>Kolicina</th>'
		+'							    <th>Restoran</th>'
		+'					  		</tr>'
		+'					  	</thead>'
		+'					  	<tbody>';
			for (var i = 0; i < data.orderedItems.length; i++) {
				newItem = data.orderedItems[i].item;
				orderItem = data.orderedItems[i];
			
		         			row+='	<tr>'
		+'						  		<td>'+newItem.name+'</td>'
		+'						  		<td>'+orderItem.quantity+'</td>'
		+'						  		<td>'+newItem.restaurantName+'</td>'
		+'						  	</tr>'
		+'						  	<tr>';
			}
			
		row+='					</tbody>'
		+'					</table>'  	
		+'				  </div>'
		+'				</div>'
		+'		      </td>'
		+'		    </tr>';
		return row;
	}
	function itemPonudaTableRow(data){
		var jedMere;
		if (data.itemType == 'MEAL') {
			jedMere = 'gr';
		}else
			if (data.itemType == 'DRINK') {
				jedMere = 'ml';
			}else{
				jedMere = '';
			}
		
		row='	<tr id="ponudaItem'+data.id+'">'
			+'		<td>'+data.name+'</td>'
			+'		<td>'+data.description+'</td>'
			+'		<td id="price">'+data.price+'<label> RSD</label></td>'
			+'		<td>'+data.quantity+' '+jedMere +'</td>'
			+'		<td>'
			+'			  <input id="number" class="itemQuant" type="number" value="0" min="0" max="20" disabled>'
			+'		</td>'
			+'	    <td>'
			+'			    <button type="button" class="btn btn-success btn-sm incQuantBtn" ">	&#43;</button>'
			+'				<button type="button" class="btn btn-danger btn-sm decQuantBtn" ">&#8722;</button>'
			+'		</td>'
			+'	</tr>';
			
			return row;
		}
	function popItemTableRow(data){
		var jedMere;
		if (data.itemType == 'MEAL') {
			jedMere = 'gr';
		}else
			if (data.itemType == 'DRINK') {
				jedMere = 'ml';
			}else{
				jedMere = '';
			}
	row='	<tr id="popItem'+data.id+'">'
		+'		<td>'+data.name+'</td>'
		+'		<td>'+data.description+'</td>'
		+'		<td>'+data.price+' RSD</td>'
		+'		<td>'+data.quantity+' '+jedMere +'</td>'
		+'		<td>'+data.restaurantName+'</td>'
		+'		<td class="restId d-none">'+data.restaurantId+'</td>'
		+'	    <td>'
		+'		  	<div class="btn-group btn-sm" role="group" aria-label="First group">'
		+'			    <button type="button" class="btn btn-primary btn-sm naruciItemBtn" id="naruci'+data.id+'">Naruci</button>'
		+'			</div>'
		+'		</td>'
		+'	</tr>';
		
		return row;
	}

	function userTableRow(data){
	row='	<tr id="rowUser'+data.id+'">'
	+'	      <td>'+data.firstName+' '+data.lastName+'</td>'
	+'	      <td>'+data.telephone+'</td>'
	+'	      <td>'+data.email+'</td>'
	+'	      <td>'+data.username+'</td>'
	+'	      <td>'+data.role+'</td>'
	+'	      <td>'+data.registrDate+'</td>'
	+'	      <td>'+data.discountPoints+'</td>'
	+'	      <td>'
	+'	      	<div class="btn-group btn-sm" role="group" aria-label="First group">'
	+'			    <button type="button" class="btn btn-primary btn-sm editUserButton" id="editUser'+data.id+'">Izmeni tip</button>'
	+'			  </div>'
	+'	      </td>'
	+'	    </tr>';
	return row;
	
	}
	function itemTableRow(data){
	row='		<tr id="rowItem'+data.id+'">'
		+'	      <td>'+data.name+'</td>'
		+'	      <td>'+data.description+'</td>'
		+'	      <td>'+data.price+'</td>'
		+'	      <td>'+data.quantity+'</td>'
		+'	      <td>'+data.restaurantName+'</td>'
		+'	      <td>'
		+'	      	<div class="btn-group btn-sm" role="group" aria-label="First group">'
		+'			    <button type="button" class="btn btn-primary btn-sm editItemButton" id=editItem'+data.id+'>Izmeni</button>'
		+'			    <button type="button" class="btn btn-danger btn-sm deleteItemButton" id=deleteItem'+data.id+'>Izbrisi</button>'
		+'			  </div>'
		+'	      </td>'
		+'	    </tr>';
	return row;
	}
	function restaurantTableRow(data){
		var meals = data.meals;
		var drinks = data.drinks;
		var categ = data.restCateg;
		var restCateg = categ.includes('_')?categ.replace("_", " "):categ;
	row='			<tr id="rowRestaurant'+data.id+'">'
		+'		      <td>'+data.name+'</td>'
		+'		      <td>'+data.address+'</td>'
		+'		      <td>'+restCateg+'</td>'
		+'		      <td>'
		+'		      	<div class="dropdown">'
		+'				  <button type="button" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown">'
		+'				    Artikli'
		+'				  </button>'
		+'				  <div class="dropdown-menu">'
		+'					<b>Jela</b>'	
		+'				    <ol class="dropdown-item">';
							for ( var meal in meals) {
								row+='<li> '+meals[meal].name+'</li>';
							}
							row+='</ol><b>Pica</b><ol class="dropdown-item">'
							for ( var drink in drinks) {
								row+='<li> '+drinks[drink].name+'</li>';
							}
						
	row+='				    </ol>'
		+'				  </div>'
		+'				</div>'
		+'		      </td>'
		+'		      <td>'
		+'		      	<div class="btn-group btn-sm" role="group" aria-label="First group">'
		+'				    <button type="button" class="btn btn-primary btn-sm editRestButton" id=editRest'+data.id+'>Izmeni</button>'
		+'				    <button type="button" class="btn btn-danger btn-sm deleteRestButton" id=deleteRest'+data.id+'>Izbrisi</button>'
		+'				 </div>'
		+'		      </td>'
		+'		    </tr>';
		return row;
	
	
	}
	function ed(){
		id = this.id;
		idd = e;
		getRest(id);
		getRest(idd);
	}
	function restaurantCard(data, type){
		var favBtnClass;
		if (type == 'favorite') {
			favBtnClass = ' btn-danger';
		}else{
			favBtnClass = '';
		}
			
		var categ = data.restCateg;
		var restCateg = categ.includes('_')?categ.replace("_", " "):categ;
		rest =	'	<div  id="rest' +data.id +'" class="card mb-2 ">'
				+'	  	<div class="card-body">'
				+'	    	<h5 class="card-title">'+data.name+'</h5>'
				+'	    	<button class="btn  float-right favoriteBtn'+favBtnClass+'" id="favorite'+data.id+'">&#10084;</button>'						
				+'				<table class="table table-sm">'
				+'				  <tbody>'
				+'				    <tr class="row">'
				+'				      <td class="col">Adresa:</td>'
				+'				      <td class="col">'+data.address+'</td>'
				+'				    </tr>'
				+'				    <tr class="row">'
				+'				      <td class="col">Kategorija:</td>'
				+'				      <td class="col restCategory">'+restCateg+'</td>'
				+'				    </tr>'
				+'				  </tbody>'
				+'				</table>'
				+'	    	<button class="btn btn-primary btn-sm float-right ponudaBtn" id="ponuda'+data.id+'">Pogledaj ponudu</button>'			    	
				+'		</div>'
				+'	</div>';
			
		
		return rest;
	}
	function getVehiclesForDelivery(){
		$.ajax({
	        url: URL + "/vehicle/forDelivery"
	    }).then(function (data) {
	    	$('#deliveryVehicleTable').empty();
	    	for (var i = 0; i < data.length; i++) {
	    		row = vehicleForDeliveryTableRow(data[i])
	    		$('#deliveryVehicleTable').append(row);
	    	}
	    })
		
	}
	function vehicleForDeliveryTableRow(data){
		var vehicleType;
		if (data.vehicleType == 'CAR') {
			vehicleType = 'Automobil';
		}
		if (data.vehicleType == 'MOPED') {
			vehicleType = 'Skuter';
		}
		if (data.vehicleType == 'BIKE') {
			vehicleType = 'Bicikl';
		}
	row='			<tr>'
		+'			<td>'+data.brand+'</td>'
		+'		    <td>'+data.model+'</td>'
		+'		    <td>'+data.numberPlate+'</td>'
		+'		    <td>'+data.productionYear+'.</td>'
		+'		    <td>'+vehicleType+'</td>'
		+'		    <td>'+data.status+'</td>'
		+'		    <td>'+data.note+'</td>'
		+'		    <td>'
		+'		    	<div class="radio">'
		+'						<label><input type="radio"  value="'+data.id+'" name="radio"></label>'
		+'				</div>'
		+'			 </td>'
		+'	    </tr>';
	return row;
	}
	function  openOrdesTableRow(data){
		var status = data.orderStatus
		var orderStatus = status.includes('_')?status.replace("_", " "):status;
		row='			<tr id="openOrder'+data.id+'">'
			+'		      <td>'+data.id+'</td>'
			+'		      <td>'+data.orderDate+' '+data.orderTime+'</td>'
			+'		      <td>'+data.buyerUsername+'</td>'
			+'		      <td>'+data.address+'</td>'
			+'		      <td>'
			+'		      	<div class="dropdown">'
			+'				  <button type="button" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown">'
			+'				    Artikli'
			+'				  </button>'
			+'				  <div class="dropdown-menu dropdown-menu-right">'
			+'				    <table class="table table-sm dropdown-item">'
			+'						<thead>'
			+'							<tr>'		
			+'								<th>Naziv</th>'
			+'							    <th>Kolicina</th>'
			+'							    <th>Restoran</th>'
			+'					  		</tr>'
			+'					  	</thead>'
			+'					  	<tbody>';
				for (var i = 0; i < data.orderedItems.length; i++) {
					newItem = data.orderedItems[i].item;
					orderItem = data.orderedItems[i];
				
			         			row+='	<tr>'
			+'						  		<td>'+newItem.name+'</td>'
			+'						  		<td>'+orderItem.quantity+'</td>'
			+'						  		<td>'+newItem.restaurantName+'</td>'
			+'						  	</tr>'
			+'						  	<tr>';
				}
				
			row+='					</tbody>'
			+'					</table>'  	
			+'				  </div>'
			+'				</div>'
			+'		      </td>'
			+'		      <td>'+orderStatus+'</td>'
			+'		      <td>'+data.note+'</td>'
			+'		      <td>'+data.totalPrice+'</td>'
			+'	      	  <td>'
			+'			    <button type="button" class="btn btn-primary btn-sm preuzPorudzButton" id="preuzPorudz'+data.id+'">Preuzmi</button>'
			+'	          </td>'
			+'		    </tr>';
			return row;
			
			
			
		}
	function  adminOrdesTableRow(data){
		var status = data.orderStatus
		if (status == 'Dostava_u_toku') {
			status = 'Dostava u toku';
		}
		row='			<tr>'
			+'		      <td>'+data.id+'</td>'
			+'		      <td>'+data.orderDate+' '+data.orderTime+'</td>'
			+'		      <td>'+data.buyerUsername+'</td>'
			+'		      <td>'+data.address+'</td>'
			+'		      <td>'
			+'		      	<div class="dropdown">'
			+'				  <button type="button" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown">'
			+'				    Artikli'
			+'				  </button>'
			+'				  <div class="dropdown-menu dropdown-menu-right">'
			+'				    <table class="table table-sm dropdown-item">'
			+'						<thead>'
			+'							<tr>'		
			+'								<th>Naziv</th>'
			+'							    <th>Kolicina</th>'
			+'							    <th>Restoran</th>'
			+'					  		</tr>'
			+'					  	</thead>'
			+'					  	<tbody>';
				for (var i = 0; i < data.orderedItems.length; i++) {
					newItem = data.orderedItems[i].item;
					orderItem = data.orderedItems[i];
				
			         			row+='	<tr>'
			+'						  		<td>'+newItem.name+'</td>'
			+'						  		<td>'+orderItem.quantity+'</td>'
			+'						  		<td>'+newItem.restaurantName+'</td>'
			+'						  	</tr>'
			+'						  	<tr>';
				}
				
			row+='					</tbody>'
			+'					</table>'  	
			+'				  </div>'
			+'				</div>'
			+'		      </td>'
			+'		      <td>'+status+'</td>'
			+'		      <td>'+data.note+'</td>'
			+'		      <td>'+data.totalPrice+'</td>'
			+'		      <td>'+data.deliveryUsername+'</td>'
			+'	      	  <td>'
			+'		      	<div class="btn-group btn-sm" role="group" aria-label="First group">'
			+'			    <button type="button" class="btn btn-primary btn-sm editPorudzButton" id="editPorudz'+data.id+'">Izmeni</button>'
			+'			    <button type="button" class="btn btn-danger btn-sm deletePorudzButton" id="editPorudz'+data.id+'">Izbrisi</button>'
			+'				 </div>'
			+'	          </td>'
			+'		    </tr>';
			return row;
			
			
			
		}
	function  orderDostTableRow(data){
		var status = data.orderStatus
		if (status == 'Dostava_u_toku') {
			status = 'Dostava u toku';
		}
		var orderStatus = status.includes('_')?status.replace("_", " "):status;
	row='			<tr>'
		+'		      <td>'+data.id+'</td>'
		+'		      <td>'+data.orderDate+' '+data.orderTime+'</td>'
		+'		      <td>'+data.buyerUsername+'</td>'
		+'		      <td>'+data.address+'</td>'
		+'		      <td>'
		+'		      	<div class="dropdown">'
		+'				  <button type="button" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown">'
		+'				    Artikli'
		+'				  </button>'
		+'				  <div class="dropdown-menu dropdown-menu-right">'
		+'				    <table class="table table-sm dropdown-item">'
		+'						<thead>'
		+'							<tr>'		
		+'								<th>Naziv</th>'
		+'							    <th>Kolicina</th>'
		+'							    <th>Restoran</th>'
		+'					  		</tr>'
		+'					  	</thead>'
		+'					  	<tbody>';
			for (var i = 0; i < data.orderedItems.length; i++) {
				newItem = data.orderedItems[i].item;
				orderItem = data.orderedItems[i];
			
		         			row+='	<tr>'
		+'						  		<td>'+newItem.name+'</td>'
		+'						  		<td>'+orderItem.quantity+'</td>'
		+'						  		<td>'+newItem.restaurantName+'</td>'
		+'						  	</tr>'
		+'						  	<tr>';
			}
			
		row+='					</tbody>'
		+'					</table>'  	
		+'				  </div>'
		+'				</div>'
		+'		      </td>'
		+'		      <td id="orderStatus'+data.id+'">'+status+'</td>'
		+'		      <td>'+data.note+'</td>'
		+'		      <td>'+data.totalPrice+'</td>'
		+'	      	  <td>'
		+'			    <button type="button" class="btn btn-primary btn-sm closeOrderDeliveryButton" id="closeOrderDelivery'+data.id+'">Dostavljeno</button>'
		+'	          </td>'
		+'		    </tr>';
		return row;
		
		
		
	}
	$('#navBtnOdjava').click(function () {
		$.post({
	        url: URL + "/user/logout"
	    }).then(function (message) {
	    	sessionStorage.clear();
	    	$('#btnOdjava').hide();
	        refresh();
	    });
    });
	
function getDostavljacOrders(user){
	var order;
	$('#dostMyOrdersTable').empty();
	for (var i = 0; i < user.deliveryOrders.length; i++) {
		order = user.deliveryOrders[i];
		row = orderDostTableRow(order);
		$('#dostMyOrdersTable').append(row);
		if (order.orderStatus == 'Dostavljeno') {
			$("#closeOrderDelivery"+order.id).addClass(' btn-success');
			$("#closeOrderDelivery"+order.id).prop("disabled",true);
		}
		if (order.orderStatus == 'Otkazano') {
			$("#closeOrderDelivery"+order.id).addClass(' btn-danger');
			$("#closeOrderDelivery"+order.id).prop("disabled",true);
		}
		
		
	}
}	
function getUserOrders(user){
	var order;
	for (var i = 0; i < user.orders.length; i++) {
		order = user.orders[i];
		row = orderUserTableRow(order);
		$('#userOrdersTable').append(row);
	}
}
function showAlertModal(message){

		$('#alertModal').find("#alertMessage").text(message);
		$('#alertModal').modal('toggle');

}
function refresh() {
    location.reload(true);
}
function isLoggedIn() {
    $.ajax({
        url: URL + "/user/currentUser"
    }).then(function (user) {

        if (user == null) {
        	$('#navBtnPrijava').show();
            $('#navBtnReg').show();
            $('#navBtnOdjava').hide();
            $('#userPorudzbineTab').hide();
    		$('#userOmiljenoTab').hide();
    		$('#adminPanel').hide();
        	$('#dostavljacPanel').hide();
        	$('#userPanel').show();
        	$('#navBtnPretraga').show();
        	getRestaurants(user);
        	getPopularItems();
        }else{
   
        
	        if (user.role == "Kupac") {
	        	$('#userPanel').show();
	        		$('#userPorudzbineTab').show();
	        		$('#userOmiljenoTab').show();
	        	$('#adminPanel').hide();
	        	$('#dostavljacPanel').hide();
	        	$('#navBtnPretraga').show();
	        	getRestaurants(user);
	        	getUserOrders(user);
	        	getPopularItems();
	        	sessionStorage.setItem("userPoints", user.discountPoints);
			}else
	        if (user.role == "Admin") {           	
	        	$('#userPanel').hide();
	        	$('#dostavljacPanel').hide();
	        	$('#adminPanel').show();
	        	getRestaurants(user);
	        	getAllOrders();
	        	getUsers();
	        	getVehicles();
	        	getItems();
	        	$('#navBtnPretraga').hide();
			}else
	        if (user.role == "Dostavljac") {           	
	        	$('#userPanel').hide();
	        	$('#dostavljacPanel').show();
	        	$('#adminPanel').hide();
	        	getDostavljacOrders(user);
	        	getOrdersForDelivery();
	        	$('#navBtnPretraga').hide();
			}
	        $('#navBtnPrijava').hide();
            $('#navBtnReg').hide();
            $('#navBtnOdjava').show();
	        
        }
        

    });
}
});